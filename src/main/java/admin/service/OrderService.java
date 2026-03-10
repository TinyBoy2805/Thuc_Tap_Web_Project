package admin.service;

import user.dao.NotificationDAO;
import admin.dao.OrderDAO;
import admin.dao.ProductDAO;
import user.model.order.Order;
import user.model.order.OrderItem;
import admin.model.orders.CustomerInfo;
import admin.model.orders.FilterRequest;
import admin.model.orders.OrderCard;
import admin.model.orders.PageInformation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderService {

    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;
    private final int PAGE_SIZE = 8;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.productDAO = new ProductDAO();
    }

    public PageInformation<OrderCard> getOrders(int pageIndex) {
        int totalOrders = this.orderDAO.countOrders();
        int totalPage = (totalOrders % PAGE_SIZE != 0) ? (totalOrders / PAGE_SIZE) + 1 : totalOrders / PAGE_SIZE;

        List<OrderCard> data = this.orderDAO.getOrders(pageIndex, PAGE_SIZE);

        PageInformation<OrderCard> infor = new PageInformation<>();
        infor.setData(data);
        infor.setPageIndex(pageIndex);
        infor.setPageSize(PAGE_SIZE);
        infor.setTotalItems(totalOrders);
        infor.setTotalPage(totalPage);

        return infor;
    }

    public PageInformation<OrderCard> searchOrder(String orderName, int pageIndex) {
        int totalOrders = this.orderDAO.countSearchOrders(orderName);
        int totalPage = (totalOrders % PAGE_SIZE != 0) ? (totalOrders / PAGE_SIZE) + 1 : totalOrders / PAGE_SIZE;

        List<OrderCard> data = this.orderDAO.searchOrders(orderName, pageIndex, PAGE_SIZE);

        PageInformation<OrderCard> infor = new PageInformation<>();
        infor.setData(data);
        infor.setPageIndex(pageIndex);
        infor.setPageSize(PAGE_SIZE);
        infor.setTotalItems(totalOrders);
        infor.setTotalPage(totalPage);

        return infor;
    }

    public PageInformation<OrderCard> filterOrders(Map<String, Object> filterRaw, int pageIndex) {
        // lấy trạng thái trên filter
        String status = (filterRaw.get("status")) != null ? (String) filterRaw.get("status") : null;
        // lấy ngày đặt hàng
        String filterDate = (filterRaw.get("orderDate")) != null ? (String) filterRaw.get("orderDate") : null;
        LocalDate date = filterDate != null ? LocalDate.parse(filterDate) : null;
        // lấy khoảng tiền
        double from = Double.parseDouble((String) filterRaw.get("from"));
        double to = Double.parseDouble((String) filterRaw.get("to"));

        FilterRequest fr = new FilterRequest(status, date, from, to);

        int totalOrders = this.orderDAO.countFilterOrders(fr);
        int totalPage = (totalOrders % PAGE_SIZE != 0) ? (totalOrders / PAGE_SIZE) + 1 : totalOrders / PAGE_SIZE;

        List<OrderCard> data = this.orderDAO.filterOrder(fr, pageIndex, PAGE_SIZE);

        PageInformation<OrderCard> infor = new PageInformation<>();
        infor.setData(data);
        infor.setPageIndex(pageIndex);
        infor.setPageSize(PAGE_SIZE);
        infor.setTotalItems(totalOrders);
        infor.setTotalPage(totalPage);

        return infor;
    }

    // Method from first version of OrderService
    public List<OrderItem> getOrderItemByID(String orderID) {
        return this.orderDAO.getOrderItemByID(orderID);
    }

    public CustomerInfo getCustomerInfoByOrder(String orderID) {
        return this.orderDAO.getCustomerInfoByOrder(orderID);
    }

    public Order getTotalPriceByOrder(String orderID) {
        return this.orderDAO.getTotalPriceByOrder(orderID);
    }

    // Methods from second version of OrderService

}
