package admin.model.orders;

import lombok.Setter;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Setter
public class CustomerInfo {
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String paymentMethod;
    private LocalDateTime orderCreateAt;

    public CustomerInfo() {}

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CustomerInfo{");
        sb.append("name='").append(name).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", phoneNumber='").append(phoneNumber).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", paymentMethod=").append(paymentMethod);
        sb.append(", orderCreateAt=").append(orderCreateAt);
        sb.append('}');
        return sb.toString();
    }

    public Date getOrderCreateAtDate() {
        if (orderCreateAt == null) return null;
        return Date.from(orderCreateAt.atZone(ZoneId.systemDefault()).toInstant());
    }
}
