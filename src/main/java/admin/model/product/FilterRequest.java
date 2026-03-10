package admin.model.product;

public class FilterRequest {
    private String category;
    private String status;
    private int quantity;

    public FilterRequest() {}

    public FilterRequest(String category, String status, int quantity) {
        this.category = category;
        this.status = status;
        this.quantity = quantity;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "FilterRequest{" +
                "category='" + category + '\'' +
                ", status='" + status + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
