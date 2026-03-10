package user.model.product;

import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
@ToString
public class Product implements Serializable
{
    private int id;
    private int productId;
    private String name;
    private String description;
    private int price;
    private String url;
    private String brand;
    private String category;
    private int buyCount;
    private int quantity;
    private Date startDate;
    private Date endDate;
    private boolean isActive;
    private List<ProductImage> images;

    public Product(){}

    public Product(int id, int productId, String name, String description, int price, String url, String brand, String category, int buyCount, Date startDate, Date endDate, int quantity, boolean isActive)
    {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.url = url;
        this.brand = brand;
        this.category = category;
        this.buyCount = buyCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.quantity = quantity;
        this.isActive = isActive;
    }


    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }


}
