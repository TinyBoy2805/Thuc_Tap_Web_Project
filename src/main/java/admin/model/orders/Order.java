package admin.model.orders;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {


    private double totalPrice;
    private double shippingFee;
    private double discountAmount;
    private double finalAmount;

    public Order() {

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("totalPrice=").append(totalPrice);
        sb.append(", shippingFee=").append(shippingFee);
        sb.append(", discountAmount=").append(discountAmount);
        sb.append(", finalAmount=").append(finalAmount);
        sb.append('}');
        return sb.toString();
    }
}
