package user.service;

import user.dao.AboutDAO;
import user.model.StoreReview;

import java.util.List;

public class AboutService
{
    private AboutDAO aboutDAO;

    public AboutService()
    {
        this.aboutDAO = new AboutDAO();
    }

    // Lấy 6 reviews mới nhất và tốt nhất (trang đầu tiên)
    public List<StoreReview> getTopReviews()
    {
        return this.aboutDAO.getTopReviews();
    }
    
    // Lấy reviews với phân trang
    public List<StoreReview> getReviews(int page, int pageSize)
    {
        int offset = page * pageSize;
        return this.aboutDAO.getReviews(offset, pageSize);
    }
    
    // Đếm tổng số reviews
    public int getTotalReviewsCount()
    {
        return this.aboutDAO.getTotalReviewsCount();
    }
}
