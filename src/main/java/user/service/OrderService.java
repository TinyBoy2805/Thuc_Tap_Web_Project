package user.service;

import user.dao.OrderDao;
import user.dao.ProductDao;
import user.dao.NotificationDAO;
import user.model.cart.Cart;
import user.model.cart.CartItem;
import user.model.order.Order;
import user.model.order.OrderItem;

import java.util.List;
import java.util.UUID;

public class OrderService {
    private final OrderDao orderDAO;
    private final ProductDao productDAO;
    private final NotificationDAO notificationDAO;
    private final int PAGE_SIZE = 8;

    public OrderService() {
        this.orderDAO = new OrderDao();
        this.productDAO = new ProductDao();
        this.notificationDAO = new NotificationDAO();
    }

    public int placeOrder(int userId, int addressId, Cart cart, double shippingFee, double discountAmount) {
        System.out.println("OrderService.placeOrder called");
        // 1. Create Order object
        Order order = new Order();
        order.setUserId(userId);
        order.setAddressId(addressId);
        String orderCode = "DH" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        order.setOrderCode(orderCode);
        order.setTotalPrice(cart.getTotalAmount());
        order.setShippingFee(shippingFee);
        order.setDiscountAmount(discountAmount);
        order.setFinalAmount(cart.getTotalAmount() + shippingFee - discountAmount);
        order.setPaymentStatus("PAID");
        order.setOrderStatus("DELIVERED");

        // 2. Insert Order into DB
        int orderId = orderDAO.createOrder(order);

        // 3. Create Order Items and Update Inventory
        for (CartItem cartItem : cart.getCart().values()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setProductId(cartItem.getProduct().getId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getPrice());

            orderDAO.createOrderItem(orderItem);

            // Update quantity and buy count
            productDAO.updateQuantity(cartItem.getProduct().getId(), cartItem.getQuantity());
            productDAO.increaseBuyCount(cartItem.getProduct().getId(), cartItem.getQuantity());
        }

        // 4. Create Notification
        String title = "Đặt hàng thành công!";
        String message = "Đơn hàng #" + orderCode + " của bạn đã được thanh toán và giao hàng thành công.";
        notificationDAO.createNotification(userId, title, message);

        return orderId;
    }

    public List<Order> getOrdersByUser(int userId) {
        List<Order> orders = orderDAO.getOrdersByUserId(userId);
        for (Order order : orders) {
            order.setItems(orderDAO.getOrderItemsByOrderId(order.getId()));
        }
        return orders;
    }

    public List<OrderItem> getOrderItems(int orderId) {
        return orderDAO.getOrderItemsByOrderId(orderId);
    }

    public Order getOrderById(int orderId) {
        Order order = orderDAO.getOrderById(orderId);
        if (order != null) {
            order.setItems(orderDAO.getOrderItemsByOrderId(orderId));
        }
        return order;
    }

    public boolean hasUserPurchasedProduct(int userId, int productId) {
        return orderDAO.hasOrderedProduct(userId, productId);
    }
}
