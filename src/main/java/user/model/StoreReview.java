package user.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
public class StoreReview implements Serializable
{
    private int id;
    private int storeId;
    private int userId;
    private String userName;      // Tên user để hiển thị
    private String userAvatar;    // Avatar URL của user
    private int rating;           // 1-5 sao
    private String comment;
    private Timestamp createdAt;

    public StoreReview() {}

    public StoreReview(int id, int storeId, int userId, String userName, String userAvatar, int rating, String comment, Timestamp createdAt)
    {
        this.id = id;
        this.storeId = storeId;
        this.userId = userId;
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }
}
