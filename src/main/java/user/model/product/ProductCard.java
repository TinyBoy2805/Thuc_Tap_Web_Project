package user.model.product;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCard implements Serializable
{
    private int id;
    private String name;
    private double price;
    private int buy_count;
    private double avg_rating;
    private boolean is_active;
    private String img_url;

    public ProductCard(){}

    public ProductCard(int id, String name, double price, int buy_count, double avg_rating, boolean is_active, String img_url)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.buy_count = buy_count;
        this.avg_rating = avg_rating;
        this.is_active = is_active;
        this.img_url = img_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getBuy_count() {
        return buy_count;
    }

    public void setBuy_count(int buy_count) {
        this.buy_count = buy_count;
    }

    public double getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(double avg_rating) {
        this.avg_rating = avg_rating;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

}
