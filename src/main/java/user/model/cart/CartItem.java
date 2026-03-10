package user.model.cart;

import lombok.Getter;
import lombok.Setter;
import user.model.product.ProductCard;

import java.io.Serializable;



@Getter
@Setter
public class CartItem implements Serializable
{
    private ProductCard product;
    private int quantity;
    private double price;

    public CartItem(ProductCard product, int quantity, double price)
    {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }


    public void updateQuantity(int newQuantity)
    {
        this.quantity += newQuantity;
    }

    public void decreaseQuantity()
    {
        this.quantity--;
    }
    public void increaseQuantity(int newQuantity)
    {
        this.quantity+=newQuantity;
    }



    public double getTotalPrice()
    {
        return this.quantity * this.price;
    }

}
