package admin.model.orders;

import java.time.LocalDateTime;

public class OrderCard {
    private int orderID;
    private String order_code;
    private String name;
    private String url;
    private String order_status;
    private double total_price;
    private LocalDateTime created_at;

    public OrderCard() {}

    public int getOrderID() { return orderID; }
    public void setOrderID(int orderID) { this.orderID = orderID; }

    public String getOrder_code() { return order_code; }
    public void setOrder_code(String order_code) { this.order_code = order_code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getOrder_status() { return order_status; }
    public void setOrder_status(String order_status) { this.order_status = order_status; }

    public double getTotal_price() { return total_price; }
    public void set_total_price(double total_price) { this.total_price = total_price; }

    // DAO uses setTotal_price (lowercased p in rs.getDouble, but setter might be expected case)
    // Looking at OrderDAO line 47: orderCard.setTotal_price(rs.getDouble("total_price"));
    public void setTotal_price(double total_price) { this.total_price = total_price; }

    public LocalDateTime getCreated_at() { return created_at; }
    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }

    @Override
    public String toString() {
        return "OrderCard{" +
                "orderID=" + orderID +
                ", order_code='" + order_code + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", order_status='" + order_status + '\'' +
                ", total_price=" + total_price +
                ", created_at=" + created_at +
                '}';
    }
}
