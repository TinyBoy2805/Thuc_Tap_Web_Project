package user.dao;

import admin.exception.DataNotFoundException;
import admin.model.Blog;
import config.DbConfig;

import java.util.List;

public class BlogDAO extends DbConfig {
    public List<Blog> getBlogs(int page, int pageSize) {
        int offset = (page - 1) * pageSize;

        String query = "SELECT id, user_id AS userId, title, content, thumbnail, url, created_at AS createdAt, updated_at AS updatedAt " +
                "FROM blogs " +
                "ORDER BY created_at DESC " +
                "LIMIT :limit OFFSET :offset";

        return get().withHandle(h ->
        {
            return h.createQuery(query)
                    .bind("limit", pageSize)
                    .bind("offset", offset)
                    .mapToBean(Blog.class)
                    .list();
        });
    }

    public List<Blog> getAllBlogs() {
        String sql = "SELECT * FROM blogs ORDER BY id ASC";
        return get().withHandle(h ->
                h.createQuery(sql)
                        .map((rs, _) -> Blog
                                .builder()
                                .id(rs.getInt("id"))
                                .userId(rs.getInt("user_id"))
                                .title(rs.getString("title"))
                                .content(rs.getString("content"))
                                .thumbnail(rs.getString("thumbnail"))
                                .url(rs.getString("url"))
                                .createdAt(rs.getTimestamp("created_at"))
                                .updatedAt(rs.getTimestamp("updated_at"))
                                .build())
                        .list()
        );
    }

    public boolean insertBlog(Blog blog) {
        String sql = "INSERT INTO blogs (user_id, title, content, thumbnail, url, created_at, updated_at) VALUES (:userId, :title, :content, :thumbnail, :url, :createdAt, :updatedAt)";
        int rows = get().withHandle(h ->
                h.createUpdate(sql)
                        .bind("userId", blog.getUserId())
                        .bind("title", blog.getTitle())
                        .bind("content", blog.getContent())
                        .bind("thumbnail", blog.getThumbnail())
                        .bind("url", blog.getUrl())
                        .bind("createdAt", blog.getCreatedAt())
                        .bind("updatedAt", blog.getUpdatedAt())
                        .execute()
        );
        return rows > 0;
    }

    public Blog getBlogById(int id) {
        String sql = "SELECT * FROM blogs WHERE id = :id";
        return get().withHandle(h ->
                h.createQuery(sql)
                        .bind("id", id)
                        .map((rs, _) -> Blog
                                .builder()
                                .id(rs.getInt("id"))
                                .userId(rs.getInt("user_id"))
                                .title(rs.getString("title"))
                                .content(rs.getString("content"))
                                .thumbnail(rs.getString("thumbnail"))
                                .url(rs.getString("url"))
                                .createdAt(rs.getTimestamp("created_at"))
                                .updatedAt(rs.getTimestamp("update_at"))
                                .build())
                        .findOne()
                        .orElseThrow(() -> new DataNotFoundException("Blog not found with id " + id)));
    }

    public boolean deleteBlogById(int id) {
        try {
            String sql = "DELETE FROM blogs WHERE id = :id";
            int rows = get().withHandle(h ->
                    h.createUpdate(sql)
                            .bind("id", id)
                            .execute()
            );
            return rows > 0;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateBlog(Blog blog) {
        String sql = "UPDATE blogs SET title = :title, content = :content, thumbnail = :thumbnail, url = :url, created_at = :createdAt, updated_at = :updatedAt WHERE id = :id";
        int rows = get().withHandle(h ->
                h.createUpdate(sql)
                        .bind("id", blog.getId())
                        .bind("title", blog.getTitle())
                        .bind("content", blog.getContent())
                        .bind("thumbnail", blog.getThumbnail())
                        .bind("url", blog.getUrl())
                        .bind("createdAt", blog.getCreatedAt())
                        .bind("updatedAt", blog.getUpdatedAt())
                        .execute()
        );
        return rows > 0;
    }

}
