package user.dao;

import config.DbConfig;
import user.model.order.Order;
import user.model.order.OrderItem;

import java.util.List;

public class OrderDao extends DbConfig {

    public int createOrder(Order order) {
        String sql = """
            INSERT INTO orders (user_id, address_id, order_code, total_price, shipping_fee, discount_amount, final_amount, payment_status, order_status)
            VALUES (:userId, :addressId, :orderCode, :totalPrice, :shippingFee, :discountAmount, :finalAmount, :paymentStatus, :orderStatus)
        """;
        return get().withHandle(handle ->
                handle.createUpdate(sql)
                        .bindBean(order)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public void createOrderItem(OrderItem item) {
        String sql = """
            INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase)
            VALUES (:orderId, :productId, :quantity, :priceAtPurchase)
        """;
        get().useHandle(handle ->
                handle.createUpdate(sql)
                        .bindBean(item)
                        .execute()
        );
    }

    public List<Order> getOrdersByUserId(int userId) {
        String sql = "SELECT * FROM orders WHERE user_id = :userId ORDER BY created_at DESC";
        return get().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .mapToBean(Order.class)
                        .list()
        );
    }

    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        String sql = """
            SELECT oi.*, p.name as productName, pi.img_url as productUrl
            FROM order_items oi
            JOIN products p ON oi.product_id = p.id
            LEFT JOIN product_images pi ON p.id = pi.product_id AND pi.is_main = 1
            WHERE oi.order_id = :orderId
        """;
        return get().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("orderId", orderId)
                        .mapToBean(OrderItem.class)
                        .list()
        );
    }

    public boolean hasOrderedProduct(int userId, int productId) {
        String sql = """
            SELECT COUNT(*)
            FROM orders o
            JOIN order_items oi ON o.id = oi.order_id
            WHERE o.user_id = :userId
            AND oi.product_id = :productId
            AND o.order_status = 'DELIVERED'
        """;
        return get().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .bind("productId", productId)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }

    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE id = :orderId";
        return get().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("orderId", orderId)
                        .mapToBean(Order.class)
                        .findOne()
                        .orElse(null)
        );
    }
}
