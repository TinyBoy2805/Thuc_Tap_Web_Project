package user.dao;

import config.DbConfig;
import user.model.ProductReview;
import user.model.product.Product;
import user.model.product.ProductCard;
import user.model.product.ProductImage;

import java.util.List;

public class ProductDao extends DbConfig  {
    public List<ProductImage> getImagesByProductId(int productId) {
        String sql = """
                    SELECT *
                    FROM product_images
                    WHERE product_id = :pid
                """;

        return get().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("pid", productId)
                        .mapToBean(ProductImage.class)
                        .list()
        );
    }


    public List<ProductCard> getProductByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String query =
                "SELECT p.id, p.name, p.price, p.buy_count, avg(r.rating) as avg_rating, p.is_active, pi.img_url\n" +
                        "FROM products p \n" +
                        "left join product_images pi on pi.product_id = p.id \n" +
                        "left join reviews r on r.product_id = p.id \n" +
                        "GROUP BY p.id \n" +
                        "limit :limit OFFSET :offset";

        return get().withHandle(h ->
                h.createQuery(query)
                        .bind("limit", pageSize)
                        .bind("offset", offset)
                        .mapToBean(ProductCard.class)
                        .list()
        );
    }


    public int getTotalProducts() {
        String query = "SELECT COUNT(DISTINCT p.id) as total FROM products p";
        return get().withHandle(h ->
                h.createQuery(query)
                        .mapTo(Integer.class)
                        .one()
        );
    }


    public List<Product> getListProduct() {
        String query =
                "SELECT \n" +
                        "    p.id,\n" +
                        "    pi.product_id AS productId,\n" +
                        "    pi.img_url AS url,\n" +
                        "    p.name,\n" +
                        "    p.description,\n" +
                        "    b.name AS brand,\n" +
                        "    c.name AS category,\n" +
                        "    p.price,\n" +
                        "    p.buy_count AS buyCount,\n" +
                        "    p.start_date AS startDate,\n" +
                        "    p.end_date AS endDate,\n" +
                        "    p.quantity\n" +
                        "FROM product_images pi\n" +
                        "JOIN products p ON pi.product_id = p.id\n" +
                        "JOIN brands b ON p.brand_id = b.id\n" +
                        "JOIN categories c ON p.category_id = c.id;";

        return get().withHandle(h -> h.createQuery(query)
                .mapToBean(Product.class)
                .list());
    }

    public Product getProduct(int id) {
        String query = "SELECT \n" +
                "    p.id,\n" +
                "    pi.product_id AS productId,\n" +
                "    pi.img_url AS url,\n" +
                "    p.name,\n" +
                "    p.description,\n" +
                "    b.name AS brand,\n" +
                "    c.name AS category,\n" +
                "    p.price,\n" +
                "    p.buy_count AS buyCount,\n" +
                "    p.start_date AS startDate,\n" +
                "    p.end_date AS endDate,\n" +
                "    p.quantity,\n" +
                "    p.is_active AS isActive\n" +
                "FROM product_images pi\n" +
                "JOIN products p ON pi.product_id = p.id\n" +
                "JOIN brands b ON p.brand_id = b.id\n" +
                "JOIN categories c ON p.category_id = c.id\n" +
                "WHERE p.id=:id AND p.is_active = 1";

        return get().withHandle(h -> h.createQuery(query)
                .bind("id", id)
                .mapToBean(Product.class)
                .first());
    }

    public ProductCard getProductCard(int productId) {
        String query = "SELECT p.id, p.name, p.price, p.buy_count, avg(r.rating) as avg_rating, p.is_active, pi.img_url\n" +
                "FROM products p \n" +
                "left join product_images pi on pi.product_id = p.id\n" +
                "left join reviews r on r.product_id = p.id \n" +
                "WHERE p.id like :id\n" +
                "GROUP BY p.id";


        return get().withHandle(h ->
                h.createQuery(query)
                        .bind("id", productId)
                        .mapToBean(ProductCard.class)
                        .first()
        );

    }


    public List<ProductCard> getProductsByName(String productName) {
        String query = "SELECT p.id, p.name, p.price, p.buy_count, avg(r.rating) as avg_rating, p.is_active, pi.img_url\n" +
                "FROM products p \n" +
                "left join product_images pi on pi.product_id = p.id\n" +
                "left join reviews r on r.product_id = p.id \n" +
                "WHERE p.name like :name\n" +
                "GROUP BY p.id";


        return get().withHandle(h ->
                h.createQuery(query)
                        .bind("name", "%" + productName + "%")
                        .mapToBean(ProductCard.class)
                        .list()
        );
    }

    public List<ProductCard> getProductsByCategory(String categoryParam) {
        String query = "SELECT p.id, p.name, p.price, p.buy_count, avg(r.rating) as avg_rating, p.is_active, pi.img_url\n" +
                "FROM products p \n" +
                "left join product_images pi on pi.product_id = p.id\n" +
                "left join reviews r on r.product_id = p.id \n" +
                "join categories c on p.category_id = c.id \n" +
                "WHERE p.name like :category\n" +
                "GROUP BY p.id";


        return get().withHandle(h ->
                h.createQuery(query)
                        .bind("category", "%" + categoryParam + "%")
                        .mapToBean(ProductCard.class)
                        .list()
        );
    }

    public List<ProductCard> getFilteredProducts(List<String> brands, List<String> types, List<Integer> ratings) {
        StringBuilder query = new StringBuilder(
                "SELECT p.id, p.name, p.price, p.buy_count, " +
                        "COALESCE(AVG(r.rating), 0) as avg_rating, p.is_active, pi.img_url " +
                        "FROM products p " +
                        "LEFT JOIN product_images pi ON pi.product_id = p.id " +
                        "LEFT JOIN reviews r ON r.product_id = p.id " +
                        "LEFT JOIN brands b ON p.brand_id = b.id " +
                        "LEFT JOIN categories c ON p.category_id = c.id " +
                        "WHERE 1=1 "
        );

        // Thêm filter cho brands
        if (brands != null && !brands.isEmpty()) {
            query.append("AND b.name IN (<brands>) ");
        }

        // Thêm filter cho categories/types
        if (types != null && !types.isEmpty()) {
            query.append("AND c.name IN (<types>) ");
        }

        query.append("GROUP BY p.id ");

        // Thêm filter cho ratings (sau khi GROUP BY)
        if (ratings != null && !ratings.isEmpty()) {
            query.append("HAVING ");
            for (int i = 0; i < ratings.size(); i++) {
                if (i > 0) query.append("OR ");
                query.append("(AVG(r.rating) >= :rating").append(i)
                        .append(" AND AVG(r.rating) < :rating").append(i).append("_max) ");
            }
        }

        return get().withHandle(h -> {
            var q = h.createQuery(query.toString());

            // Bind brands
            if (brands != null && !brands.isEmpty()) {
                q.bindList("brands", brands);
            }

            // Bind types
            if (types != null && !types.isEmpty()) {
                q.bindList("types", types);
            }

            // Bind ratings
            if (ratings != null && !ratings.isEmpty()) {
                for (int i = 0; i < ratings.size(); i++) {
                    int rating = ratings.get(i);
                    q.bind("rating" + i, rating);
                    q.bind("rating" + i + "_max", rating + 1);
                }
            }

            return q.mapToBean(ProductCard.class).list();
        });
    }

    public List<ProductReview> getProductReviewsByProductId(int id) {
        String query = """
                    select r.id,u.name, u.avt_url,  r.rating, r.comment, r.created_at
                    from users u\s
                    join reviews r \s
                    on r.user_id = u.id
                    where r.product_id = :pid;
                """;

        return get().withHandle(h ->
                h.createQuery(query)
                        .bind("pid", id)
                        .mapToBean(ProductReview.class)
                        .list()
        );
    }

    public List<ProductReview> getProductReviewsByProductIdHasPagination(int id, int pageReview, int pageReviewSize) {

        int offset = (pageReview - 1) * pageReviewSize;

        String query = """
                    select r.id,u.name, u.avt_url,  r.rating, r.comment, r.created_at
                    from users u\s
                    join reviews r \s
                    on r.user_id = u.id
                    where r.product_id = :pid \s
                    limit :limit offset :offset;
                
                """;

        return get().withHandle(h ->
                h.createQuery(query)
                        .bind("pid", id)
                        .bind("limit", pageReviewSize)
                        .bind("offset", offset)
                        .mapToBean(ProductReview.class)
                        .list()
        );
    }

    public List<ProductCard> getProductsByCategoryHasPagination(String category, int pageProduct, int pageProductSize) {
        int offset = (pageProduct - 1) * pageProductSize;

        String query = "SELECT p.id, p.name, p.price, p.buy_count, avg(r.rating) as avg_rating, p.is_active, pi.img_url\n" +
                "FROM products p \n" +
                "left join product_images pi on pi.product_id = p.id\n" +
                "left join reviews r on r.product_id = p.id \n" +
                "join categories c on p.category_id = c.id \n" +
                "WHERE p.name like :category\n" +
                "GROUP BY p.id\n" +
                "limit :limit offset :offset";


        return get().withHandle(h ->
                h.createQuery(query)
                        .bind("category", "%" + category + "%")
                        .bind("limit", pageProductSize)
                        .bind("offset", offset)
                        .mapToBean(ProductCard.class)
                        .list()
        );
    }

    public boolean updateQuantity(int productId, int quantity) {
        String sql = "UPDATE products SET quantity = quantity - :qty WHERE id = :id AND quantity >= :qty";
        int updated = get().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("qty", quantity)
                        .bind("id", productId)
                        .execute()
        );
        return updated > 0;
    }

    public void increaseBuyCount(int productId, int quantity) {
        String sql = "UPDATE products SET buy_count = buy_count + :qty WHERE id = :id";
        get().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("qty", quantity)
                        .bind("id", productId)
                        .execute()
        );
    }

    public void saveReview(int userId, int productId, int rating, String comment) {
        String sql = """
                    INSERT INTO reviews (user_id, product_id, rating, comment, created_at)
                    VALUES (:userId, :productId, :rating, :comment, NOW())
                """;
        get().useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("userId", userId)
                        .bind("productId", productId)
                        .bind("rating", rating)
                        .bind("comment", comment)
                        .execute()
        );
    }
}
