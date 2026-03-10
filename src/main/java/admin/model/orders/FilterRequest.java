package admin.model.orders;

import java.time.LocalDate;

public class FilterRequest {
    private String status;
    private LocalDate orderDate;
    private double from;
    private double to;

    public FilterRequest(){}

    public FilterRequest(String status, LocalDate orderDate, double from, double to) {
        this.status = status;
        this.orderDate = orderDate;
        this.from = from;
        this.to = to;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    public double getFrom() { return from; }
    public void setFrom(double from) { this.from = from; }

    public double getTo() { return to; }
    public void setTo(double to) { this.to = to; }

    @Override
    public String toString() {
        return "FilterRequest{" +
                "status='" + status + '\'' +
                ", orderDate=" + orderDate +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
