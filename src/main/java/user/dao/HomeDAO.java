package user.dao;

import config.DbConfig;
import user.model.Voucher;
import user.model.product.ProductCard;

import java.util.List;

public class HomeDAO extends DbConfig
{
    public int getAmountUsers()
    {
        String query = "SELECT COUNT(*) FROM users WHERE role = 'customer'";

        return get().withHandle(h->
                h.createQuery(query)
                        .mapTo(Integer.class)
                        .one()
                );
    }


    public int getAvgRating()
    {
        String query = "select avg(rating) as avg_rating from store_reviews";

        return get().withHandle(h->
                h.createQuery(query)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public List<String> getCategories()
    {
        String query = "select name from categories";

        return get().withHandle(h->
                h.createQuery(query)
                        .mapTo(String.class)
                        .list()
        );
    }


    public List<Voucher> getVouchers()
    {
        String query = "select \n" +
                "v.id,\n" +
                "c.name as category_name,\n" +
                "v.code,\n" +
                "v.description,\n" +
                "v.discount_amount,\n" +
                "v.discount_percentage,\n" +
                "v.start_date,\n" +
                "v.end_date,\n" +
                "v.usage_limit,\n" +
                "v.current_amount,\n" +
                "v.min_order_value,\n" +
                "v.voucher_type\n" +
                "from vouchers v \n" +
                "join categories c on c.id = v.category_id";

        return  get().withHandle(h ->
                    h.createQuery(query)
                            .mapToBean(Voucher.class)
                            .list()
                );
    }


    public List<ProductCard> getSearchTrendings()
    {
        String query =
                "SELECT \n" +
                "    p.id, \n" +
                "    p.name, \n" +
                "    p.price, \n" +
                "    p.buy_count, \n" +
                "    AVG(r.rating) as avg_rating, \n" +
                "    p.is_active, \n" +
                "    pi.img_url\n" +
                "FROM search_histories s\n" +
                "INNER JOIN products p ON p.name LIKE CONCAT('%', s.keyword, '%')\n" +
                "LEFT JOIN product_images pi ON pi.product_id = p.id\n" +
                "LEFT JOIN reviews r ON r.product_id = p.id \n" +
                "WHERE p.is_active = 1\n" +
                "GROUP BY p.id, p.name, p.price, p.buy_count, p.is_active, pi.img_url\n" +
                "ORDER BY sum(s.times) DESC\n" +
                "LIMIT 10;";


        return get().withHandle(h->
            h.createQuery(query)
                    .mapToBean(ProductCard.class)
                    .list()
        );


    }

    public List<ProductCard> getSellTrendings()
    {
        String query =
                "SELECT \n" +
                "    p.id, \n" +
                "    p.name, \n" +
                "    p.price, \n" +
                "    p.buy_count, \n" +
                "    AVG(r.rating) as avg_rating, \n" +
                "    p.is_active, \n" +
                "    pi.img_url\n" +
                "from products p\n" +
                "LEFT JOIN product_images pi ON pi.product_id = p.id\n" +
                "LEFT JOIN reviews r ON r.product_id = p.id \n" +
                "WHERE p.is_active = 1\n" +
                "GROUP BY p.id\n" +
                "order by buy_count desc\n" +
                "limit 10;";

        return get().withHandle(h->
                h.createQuery(query)
                        .mapToBean(ProductCard.class)
                        .list()
        );

    }

    public List<ProductCard> getRatingTrendings()
    {
        String query =
                "SELECT \n" +
                "    p.id, \n" +
                "    p.name, \n" +
                "    p.price, \n" +
                "    p.buy_count, \n" +
                "    AVG(r.rating) as avg_rating, \n" +
                "    p.is_active, \n" +
                "    pi.img_url\n" +
                "from products p\n" +
                "LEFT JOIN product_images pi ON pi.product_id = p.id\n" +
                "LEFT JOIN reviews r ON r.product_id = p.id \n" +
                "WHERE p.is_active = 1\n" +
                "GROUP BY p.id\n" +
                "order by avg_rating desc\n" +
                "limit 10;";

        return get().withHandle(h->
                h.createQuery(query)
                        .mapToBean(ProductCard.class)
                        .list()
        );
    }


    public void saveStoreReview(String review, int stars)
    {
        String query = "INSERT INTO store_reviews(store_id, user_id, rating, comment, created_at)\n" +
                        "VALUES \n" +
                        "(:store_id, :user_id,  :rating, :comment, NOW())";

        get().useHandle(h->
            h.createUpdate(query)
            .bind("store_id", 1)
            .bind("user_id", 1)
            .bind("rating", stars)
            .bind("comment", review)
            .execute()
        );
    }
}
