package user.dao;

import config.DbConfig;
import user.model.StoreReview;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AboutDAO extends DbConfig
{
    // Lấy 6 reviews mới nhất và tốt nhất (rating cao) - Trang đầu tiên
    public List<StoreReview> getTopReviews()
    {
        return getReviews(0, 6);
    }
    
    // Lấy reviews với phân trang - Loại bỏ reviews không có name và avt_url
    public List<StoreReview> getReviews(int offset, int limit)
    {
        String sql = """
            SELECT 
                sr.id, 
                sr.store_id, 
                sr.user_id, 
                sr.rating, 
                sr.comment, 
                sr.created_at,
                u.name, 
                u.avt_url
            FROM store_reviews sr
            INNER JOIN users u ON sr.user_id = u.id
            WHERE sr.store_id = 1 
                AND u.name IS NOT NULL 
                AND u.name != '' 
                AND u.avt_url IS NOT NULL 
                AND u.avt_url != ''
            ORDER BY sr.rating DESC, sr.created_at DESC
            LIMIT :limit OFFSET :offset
        """;

        return get().withHandle(handle ->
            handle.createQuery(sql)
                .bind("offset", offset)
                .bind("limit", limit)
                .map(new StoreReviewMapper())
                .list()
        );
    }
    
    // Đếm tổng số reviews hợp lệ (có name và avt_url)
    public int getTotalReviewsCount()
    {
        String sql = """
            SELECT COUNT(*) 
            FROM store_reviews sr
            INNER JOIN users u ON sr.user_id = u.id
            WHERE sr.store_id = 1 
                AND u.name IS NOT NULL 
                AND u.name != '' 
                AND u.avt_url IS NOT NULL 
                AND u.avt_url != ''
        """;
        
        return get().withHandle(handle ->
            handle.createQuery(sql)
                .mapTo(Integer.class)
                .one()
        );
    }

    // Mapper để map ResultSet sang StoreReview object
    private static class StoreReviewMapper implements RowMapper<StoreReview>
    {
        @Override
        public StoreReview map(ResultSet rs, StatementContext ctx) throws SQLException
        {
            StoreReview review = new StoreReview();
            review.setId(rs.getInt("id"));
            review.setStoreId(rs.getInt("store_id"));
            review.setUserId(rs.getInt("user_id"));
            review.setRating(rs.getInt("rating"));
            review.setComment(rs.getString("comment"));
            review.setCreatedAt(rs.getTimestamp("created_at"));
            review.setUserName(rs.getString("name"));
            review.setUserAvatar(rs.getString("avt_url"));
            
            return review;
        }
    }
}
