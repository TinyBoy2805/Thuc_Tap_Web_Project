package admin.dao;

import admin.exception.DataNotFoundException;
import admin.model.product.AdminProductCard;
import admin.model.product.FilterRequest;
import config.DbConfig;
import user.model.product.Product;
import user.model.product.ProductImage;

import org.jdbi.v3.core.statement.PreparedBatch;

import java.util.List;

/**
 * boc try catch cung voi custom exception cu the cho tung phuong thuc -> de tim bugs
 * thay the cac model thanh builder().build() -> clean code
 */
public class ProductDAO extends DbConfig {

    public void insert(List<Product> products) {
        try {
            get().useHandle(handle -> {
                PreparedBatch batch = handle.prepareBatch("insert into products(id,name,price) values(:id,:name,:price)");
                products.forEach(product -> batch.bindBean(product).add());
                batch.execute();
            });
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to insert product: " + e);
        }
    }

    // admin DAO cho product
    public int getTotalProducts() {
        try {
            String query = "SELECT COUNT(DISTINCT p.id) as total FROM products p";
            return get().withHandle(h ->
                    h.createQuery(query)
                            .mapTo(Integer.class)
                            .one()
            );
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to get total products: " + e);
        }
    }

    public List<AdminProductCard> getProducts(int pageIndex, int pageSize) {
        try {
            int offset = (pageIndex - 1) * pageSize;
            String query = """
                    SELECT p.id, p.name, p.price, p.buy_count, pi.img_url, p.quantity, p.is_active
                    FROM products p
                    left join product_images pi on pi.product_id = p.id
                    where pi.is_main = 1
                    Group by p.id
                    limit :limit OFFSET :offset
                    """;

            return get().withHandle(h ->
                    h.createQuery(query)
                            .bind("limit", pageSize)
                            .bind("offset", offset)
                            .mapToBean(AdminProductCard.class)
                            .list()
            );
        } catch (RuntimeException e) {
            throw new DataNotFoundException("Cannot find products with %d".formatted(pageIndex));
        }
    }

    public int countSearchProduct(String productName) {
        String query = """
                SELECT Count(DISTINCT p.id)
                                FROM products p
                                WHERE p.name like :name
                """;
        return get().withHandle(h ->
                h.createQuery(query)
                        .bind("name", "%" + productName + "%")
                        .mapTo(int.class)
                        .one()
        );
    }

    public List<AdminProductCard> searchProduct(String productName, int pageIndex, int pageSize) {
        String query = """
                SELECT p.id, p.name, p.price, p.buy_count, pi.img_url, p.quantity, p.is_active
                                FROM products p
                                left join product_images pi on pi.product_id = p.id
                                WHERE p.name like :name
                                GROUP BY p.id
                                limit :limit OFFSET :offset
                """;
        return get().withHandle(h ->
                h.createQuery(query)
                        .bind("name", "%" + productName + "%")
                        .bind("limit", pageSize)
                        .bind("offset", (pageIndex - 1) * pageSize)
                        .mapToBean(AdminProductCard.class)
                        .list());
    }

    public int countFilterProducts(FilterRequest fr) {
        StringBuilder sql = new StringBuilder("""
                    SELECT COUNT(DISTINCT P.id)
                    FROM products P
                    JOIN categories C ON P.category_id = C.id
                    WHERE 1 = 1
                """);

        if (fr.getQuantity() > 0) {
            sql.append(" AND P.quantity >= :quantity");
        }

        if (fr.getCategory() != null && !fr.getCategory().isBlank()) {
            sql.append(" AND C.name LIKE :category COLLATE utf8mb4_unicode_ci");
        }

        if (fr.getStatus() != null && !fr.getStatus().isBlank()) {
            sql.append("""
                        AND (
                            (:status = 'active'      AND P.quantity >= 50)
                         OR (:status = 'low'    AND P.quantity > 0 AND P.quantity < 50)
                         OR (:status = 'out-of-stock' AND P.quantity = 0)
                        )
                    """);
        }

        return get().withHandle(h -> {
            var q = h.createQuery(sql.toString());

            if (fr.getQuantity() > 0) {
                q.bind("quantity", fr.getQuantity());
            }

            if (fr.getCategory() != null && !fr.getCategory().isBlank()) {
                q.bind("category", fr.getCategory());
            }

            if (fr.getStatus() != null && !fr.getStatus().isBlank()) {
                q.bind("status", fr.getStatus());
            }

            return q.mapTo(int.class).one();
        });
    }

    public List<AdminProductCard> filterProduct(FilterRequest filter, int pageIndex, int pageSize) {

        StringBuilder sql = new StringBuilder("""
                    SELECT
                        P.id,
                        P.name,
                        P.price,
                        P.quantity,
                        P.is_active,
                        PI.img_url,
                        P.buy_count
                    FROM products P
                    left join product_images pi on pi.product_id = p.id
                    JOIN categories C ON P.category_id = C.id
                    WHERE 1 = 1 AND PI.is_main = 1
                """);

        if (filter.getQuantity() >= 0) {
            sql.append(" AND P.quantity >= :quantity");
        }

        if (filter.getCategory() != null && !filter.getCategory().isBlank()) {
            sql.append(" AND C.name LIKE :category COLLATE utf8mb4_unicode_ci");
        }

        if (!filter.getStatus().isBlank()) {
            sql.append("""
                        AND (
                            (:status = 'active'      AND P.quantity >= 50)
                         OR (:status = 'low'    AND P.quantity > 0 AND P.quantity < 50)
                         OR (:status = 'out-of-stock' AND P.quantity = 0)
                        )
                    """);
        }

        sql.append(" ORDER BY P.id DESC LIMIT :limit OFFSET :offset");

        return get().withHandle(h -> {
            var query = h.createQuery(sql.toString());

            if (filter.getQuantity() >= 0) {
                query.bind("quantity", filter.getQuantity());
            }

            if (!filter.getStatus().isBlank()) {
                query.bind("status", filter.getStatus());
            }

            if (filter.getCategory() != null && !filter.getCategory().isBlank()) {
                query.bind("category", filter.getCategory());
            }

            query.bind("limit", pageSize);
            query.bind("offset", (pageIndex - 1) * pageSize);

            return query.map((rs, ctx) -> {
                AdminProductCard p = new AdminProductCard();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                p.setQuantity(rs.getInt("quantity"));
                p.setBuy_count(rs.getInt("buy_count"));
                p.set_active(rs.getBoolean("is_active"));
                p.setImg_url(rs.getString("img_url"));
                return p;
            }).list();
        });
    }

    public int getBrandIDByName(String brandName) {
        String query = """
                SELECT id
                FROM brands
                WHERE name like :brand
                """;
        return get().withHandle(h ->
                h.createQuery(query)
                        .bind("brand", brandName)
                        .mapTo(int.class)
                        .one()
        );
    }

    public int getCategoryIDByName(String categoryName) {
        String query = """
                SELECT id
                FROM categories
                WHERE name like :category
                """;
        return get().withHandle(h ->
                h.createQuery(query)
                        .bind("category", categoryName)
                        .mapTo(int.class)
                        .one()
        );
    }

    public int addNewProduct(Product product, List<ProductImage> productImages) {
        int brandID = getBrandIDByName(product.getBrand());
        int categoryID = getCategoryIDByName(product.getCategory());

        String createNewProduct = """
                INSERT INTO products (name, description, brand_id, category_id, price, is_active, buy_count, start_date, end_date, quantity)
                VALUES (:name, :description, :brandID, :categoryID, :price, 1, 0, :startDate, :endDate, :quantity)
                """;

        int productID = get().withHandle(h ->
                h.createUpdate(createNewProduct)
                        .bind("name", product.getName())
                        .bind("description", product.getDescription())
                        .bind("brandID", brandID)
                        .bind("categoryID", categoryID)
                        .bind("startDate", product.getStartDate())
                        .bind("endDate", product.getEndDate())
                        .bind("price", product.getPrice())
                        .bind("quantity", product.getQuantity())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(int.class)
                        .one()
        );

        String updateProductIMG = """
                INSERT INTO product_images(product_id, img_url, is_main)
                VALUES (:productID, :imgURL, :isMain)
                """;
        for (ProductImage image : productImages) {
            get().withHandle(h ->
                    h.createUpdate(updateProductIMG)
                            .bind("productID", productID)
                            .bind("imgURL", image.getUrl())
                            .bind("isMain", image.getIsMain())
                            .execute()
            );
        }

        return productID;
    }


    public Product getProductByID(int productID) {
        String query = """
                SELECT p.name, p.price, p.id, p.description, b.name as brand, c.name as category, p.quantity, p.start_date, p.end_date, p.is_active
                From products p
                join brands b on p.brand_id = b.id
                join categories c on p.category_id = c.id
                where p.id = :productID
                """;
        return get().withHandle(h ->
                h.createQuery(query)
                        .bind("productID", productID)
                        .mapToBean(Product.class)
                        .one()
        );
    }


    public List<ProductImage> getImagesByID(int productID) {
        String query = """
                Select pi.*
                from product_images pi
                join products p on pi.product_id = p.id
                WHERE pi.product_id = :productID
                """;
        return get().withHandle(h ->
                h.createQuery(query)
                        .bind("productID", productID)
                        .map((rs, ctx) ->
                        {
                            ProductImage productImage = new ProductImage();
                            productImage.setUrl(rs.getString("img_url"));
                            productImage.setIsMain(rs.getInt("is_main"));
                            return productImage;
                        })
                        .list()
        );
    }

    public void updateProduct(Product product, List<ProductImage> images, int productID) {
        int brandID = getBrandIDByName(product.getBrand());
        int categoryID = getCategoryIDByName(product.getCategory());

        String updateProduct = """
                Update products
                Set name = :name, description = :description, brand_id = :brandID, category_id = :categoryID,
                 start_date = :startDate, end_date = :endDate, price = :price, quantity = :quantity
                Where id = :productID
                """;

        get().withHandle(h ->
                h.createUpdate(updateProduct)
                        .bind("productID", productID)
                        .bind("name", product.getName())
                        .bind("description", product.getDescription())
                        .bind("brandID", brandID)
                        .bind("categoryID", categoryID)
                        .bind("startDate", product.getStartDate())
                        .bind("endDate", product.getEndDate())
                        .bind("price", product.getPrice())
                        .bind("quantity", product.getQuantity())
                        .execute()
        );


        get().withHandle(h ->
                h.createUpdate("DELETE FROM product_images WHERE product_id = :productID")
                        .bind("productID", productID)
                        .execute()
        );


        String updateProductIMG = """
                INSERT INTO product_images(product_id, img_url, is_main)
                VALUES (:productID, :imgURL, :isMain)
                """;
        for (ProductImage image : images) {
            get().withHandle(h ->
                    h.createUpdate(updateProductIMG)
                            .bind("productID", productID)
                            .bind("imgURL", image.getUrl())
                            .bind("isMain", image.getIsMain())
                            .execute()
            );
        }

    }

    public boolean deleteProduct(int productID) {
        try {
            get().useTransaction(handle -> {
                handle.createUpdate("DELETE FROM product_images WHERE product_id = :productID")
                        .bind("productID", productID)
                        .execute();

                int rowsAffected = handle.createUpdate("DELETE FROM products WHERE id = :productID")
                        .bind("productID", productID)
                        .execute();


                if (rowsAffected == 0) {
                    throw new RuntimeException("Product not found with ID: " + productID);
                }
            });
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to delete products with id " + productID);
        }
    }
}
