package admin.model;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class Blog {
    private int id;
    private int userId;
    private String title;
    private String content;
    private String thumbnail;
    private String url;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

