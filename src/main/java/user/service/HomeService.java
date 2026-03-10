package user.service;

import user.dao.HomeDAO;
import user.dao.ProductDao;
import user.model.Voucher;
import user.model.product.ProductCard;

import java.util.List;

public class HomeService
{
    private HomeDAO homeDao = new HomeDAO();
    private ProductDao productDao = new ProductDao();

    public int getAmountUsers()
    {
        return this.homeDao.getAmountUsers();
    }

    public int getAvgRating()
    {
        return this.homeDao.getAvgRating();
    }

    public List<String> getCategories()
    {
        return this.homeDao.getCategories();
    }

    public List<Voucher> getVouchers()
    {
        return this.homeDao.getVouchers();
    }

    public List<ProductCard> getSearchTrendings()
    {
        return this.homeDao.getSearchTrendings();
    }

    public List<ProductCard> getSellTrendings()
    {
        return this.homeDao.getSellTrendings();
    }

    public List<ProductCard> getRatingTrendings()
    {
        return this.homeDao.getRatingTrendings();
    }

    public List<ProductCard> getProductByPage(int page, int pageSize)
    {
        return this.productDao.getProductByPage(page, pageSize);
    }


    public void saveStoreReview(String review, int stars)
    {
        this.homeDao.saveStoreReview(review, stars);
    }
}
