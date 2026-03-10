package user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class ProductReview implements Serializable
{
    private int id;
    private String name;
    private String avt_url;
    private int rating;
    private String comment;
    private Timestamp created_at;

    public ProductReview(){}

    public ProductReview(int id, String name, String avt_url, int rating, String comment, Timestamp created_at)
    {
        this.id = id;
        this.name = name;
        this.avt_url = avt_url;
        this.rating = rating;
        this.comment = comment;
        this.created_at = created_at;
    }


}
