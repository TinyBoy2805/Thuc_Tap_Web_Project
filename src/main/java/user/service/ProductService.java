package user.service;

import user.dao.ProductDao;
import user.model.ProductReview;
import user.model.product.Product;
import user.model.product.ProductCard;
import user.model.product.ProductImage;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private ProductDao productDao = new ProductDao();

    public List<Product> getProducts() throws SQLException {
        return this.productDao.getListProduct();
    }

    public List<ProductCard> getProductByPage(int page, int pageSize) throws SQLException {
        return this.productDao.getProductByPage(page, pageSize);
    }

    public int getTotalProducts() throws SQLException {
        return this.productDao.getTotalProducts();
    }

    public Product getOneProduct(int id) {
        return this.productDao.getProduct(id);
    }

    public List<ProductCard> getProductsByName(String productName) {
        return this.productDao.getProductsByName(productName);
    }

    public List<ProductCard> getProductsByCategory(String categoryParam) {
        return this.productDao.getProductsByCategory(categoryParam);
    }

    public List<ProductCard> getFilteredProducts(List<String> brands, List<String> types, List<Integer> ratings) {
        return this.productDao.getFilteredProducts(brands, types, ratings);
    }

    public List<ProductImage> getImagesByProductId(int productId) {
        return this.productDao.getImagesByProductId(productId);
    }

    public List<ProductReview> getProductReviewsByProductId(int id) {
        return this.productDao.getProductReviewsByProductId(id);
    }

    public double getAvgRating(List<ProductReview> reviews) {
        double total = 0;
        for (ProductReview r : reviews) {
            total += r.getRating();
        }

        return Math.ceil((double) (total / reviews.size()));
    }

    public List<ProductReview> getProductReviewsByProductIdHasPagination(int id, int pageReview, int pageReviewSize) {
        return this.productDao.getProductReviewsByProductIdHasPagination(id, pageReview, pageReviewSize);
    }

    public List<ProductCard> getProductsByCategoryHasPagination(String category, int pageProduct, int pageProductSize) {
        return this.productDao.getProductsByCategoryHasPagination(category, pageProduct, pageProductSize);
    }

    public void saveReview(int userId, int productId, int rating, String comment) {
        this.productDao.saveReview(userId, productId, rating, comment);
    }
}
