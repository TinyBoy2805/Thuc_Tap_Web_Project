package admin.dao;

import admin.model.MonthlyRevenue;
import admin.model.TopInventory;
import config.DbConfig;
import user.model.product.Product;

import java.util.List;

public class DashboardDao extends DbConfig {
    public long getTodayRevenue() {
        String query = """
                 SELECT COALESCE(SUM(total_price), 0) as today_revenue\s
                  FROM orders\s
                  WHERE order_status = 'COMPLETED'\s
                  AND created_at >= DATE_SUB(NOW(), INTERVAL 1 DAY)
                \s""";

        return get().withHandle(h ->
                h.createQuery(query)
                        .mapTo(Long.class)
                        .one()
        );
    }

    public long getMonthRevenue() {
        String query = """
                SELECT COALESCE(SUM(total_price), 0) as month_revenue\s
                 FROM orders\s
                 WHERE order_status = 'COMPLETED'\s
                 AND created_at >= DATE_SUB(NOW(), INTERVAL 30 DAY)
                """;
        return get().withHandle(h ->
                h.createQuery(query)
                        .mapTo(Long.class)
                        .one()
        );
    }

    public long getYearRevenue() {
        String query = """
                SELECT COALESCE(SUM(total_price), 0) as year_revenue\s
                 FROM orders\s
                 WHERE order_status = 'COMPLETED'\s
                 AND created_at >= DATE_SUB(NOW(), INTERVAL 12 MONTH)
                """;
        return get().withHandle(h ->
                h.createQuery(query)
                        .mapTo(Long.class)
                        .one()
        );
    }

    public List<Product> getTopProducts() {
        String query = "SELECT name, buy_count " +
                "FROM products " +
                "ORDER BY buy_count DESC " +
                "LIMIT 3";

        return get().withHandle(h ->
                h.createQuery(query)
                        .map((rs, ctx) ->{
                            Product product = new Product();
                            product.setName(rs.getString("name"));
                            product.setBuyCount(rs.getInt("buy_count"));
                            return product;
                        })
                        .list()
        );
    }

    public List<MonthlyRevenue> getMonthlyRevenue(int year) {
        String query = """
                SELECT MONTH(created_at) as month, SUM(total_price) as monthly_revenue
                FROM orders
                WHERE order_status = 'COMPLETED' AND YEAR(created_at) = :year
                GROUP BY MONTH(created_at)
                ORDER BY MONTH(created_at) ASC
                """;
        return get().withHandle(h ->
                h.createQuery(query)
                        .bind("year", year)
//                        .mapToBean(MonthlyRevenue.class)
                        .map((rs, ctx) -> {
                            MonthlyRevenue monthlyRevenue = new MonthlyRevenue();
                            monthlyRevenue.setMonthNumber(rs.getInt("month"));
                            monthlyRevenue.setMonthlyRevenue(rs.getLong("monthly_revenue"));
                            return monthlyRevenue;
                        })
                        .list()
        );
    }

    public List<TopInventory> getTopInventory() {
        String query = """
                SELECT p.id, p.name, b.name as brand, p.quantity, p.price
               FROM products p
               INNER JOIN brands b ON p.brand_id = b.id
               ORDER BY p.quantity DESC
               LIMIT 3
               """;

        return get().withHandle(h ->
                h.createQuery(query)
//                        .mapToBean(Product.class)
                        .map((rs, ctx) ->{
                            TopInventory topInventory = new TopInventory();
                            topInventory.setId(rs.getInt("p.id"));
                            topInventory.setName(rs.getString("p.name"));
                            topInventory.setBrand(rs.getString("brand"));
                            topInventory.setQuantity(rs.getInt("p.quantity"));
                            topInventory.setPrice(rs.getInt("p.price"));
                            return topInventory;
                        })
                        .list()
        );
    }
}
