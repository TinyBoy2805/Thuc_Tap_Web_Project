package admin.model.product;

public class AdminProductCard {
    private int id;
    private String name;
    private double price;
    private int buy_count;
    private boolean is_active;
    private String img_url;
    private int quantity;

    public AdminProductCard() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getBuy_count() { return buy_count; }
    public void setBuy_count(int buy_count) { this.buy_count = buy_count; }

    public boolean is_active() { return is_active; }
    public void set_active(boolean is_active) { this.is_active = is_active; }

    public String getImg_url() { return img_url; }
    public void setImg_url(String img_url) { this.img_url = img_url; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "AdminProductCard{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", buy_count=" + buy_count +
                ", is_active=" + is_active +
                ", img_url='" + img_url + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
