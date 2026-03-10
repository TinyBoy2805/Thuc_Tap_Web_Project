package user.service;

import user.dao.ProductDao;
import user.model.product.ProductCard;

public class CartService
{
    private ProductDao ProductDao;

    public CartService()
    {
        this.ProductDao = new ProductDao();
    }

    public ProductCard getProductCard(int productId)
    {
        return this.ProductDao.getProductCard(productId);
    }
}
