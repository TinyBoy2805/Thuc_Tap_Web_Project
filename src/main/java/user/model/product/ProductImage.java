package user.model.product;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ProductImage implements Serializable {
    private int id;
    private int product_id;
    private String img_url;
    private boolean is_main;

    public ProductImage() {}

    public ProductImage(int id, int product_id, String img_url, boolean is_main) {
        this.id = id;
        this.product_id = product_id;
        this.img_url = img_url;
        this.is_main = is_main;
    }

    public ProductImage(String img_url, int isMain) {
        this.img_url = img_url;
        this.is_main = (isMain == 1);
    }

    // Compatibility methods for code using 'url' and 'isMain' (int)
    public String getUrl() {
        return img_url;
    }

    public void setUrl(String url) {
        this.img_url = url;
    }

    public int getIsMain() {
        return is_main ? 1 : 0;
    }

    public void setIsMain(int isMain) {
        this.is_main = (isMain == 1);
    }
}
