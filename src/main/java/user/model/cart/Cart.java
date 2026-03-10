package user.model.cart;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import user.model.product.ProductCard;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class Cart
{
    private HashMap<Integer, CartItem> cart;

    public Cart()
    {
        this.cart = new HashMap<>();
    }

    public CartItem isExist(ProductCard new_product)
    {
        for(Map.Entry<Integer, CartItem> entry : cart.entrySet())
        {
            if(entry.getValue().getProduct() != null)
            {
                ProductCard product_in_cart = entry.getValue().getProduct();
                if(new_product.getId() == product_in_cart.getId())
                {
                    return entry.getValue();
                }
            }
        }

        return null;
    }


    public void addNewItem(CartItem newItem)
    {
        int productId = newItem.getProduct().getId();
        if(this.cart.containsKey(productId))
        {
            CartItem exist = this.cart.get(productId);
            exist.increaseQuantity(newItem.getQuantity());
        }else
        {
            cart.put(productId, newItem);
        }
    }

    public boolean deleteItemById(int id)
    {
        return this.cart.remove(id) != null;
    }


    public boolean updateQuantity(int productId, int quantity)
    {
        if(this.cart.containsKey(productId))
        {
            this.cart.get(productId).setQuantity(quantity);
            return true;
        }
        return false;
    }

    public double getTotalAmount() {
        return cart.values().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
