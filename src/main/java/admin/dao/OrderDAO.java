package admin.dao;

import admin.Enums.Payment;
import admin.model.orders.*;

import java.sql.Timestamp;

import config.DbConfig;
import user.model.order.Order;
import user.model.order.OrderItem;
import java.util.List;

public class OrderDAO extends DbConfig {

    // admin
    public int countOrders() {
        String query = """
                SELECT Count(DISTINCT id) from orders
                """;
        return get().withHandle(h ->
                h.createQuery(query).mapTo(int.class).one()
        );
    }


    // admin
    public List<OrderCard> getOrders(int pageIndex, int pageSize) {

        String query = """
                SELECT O.order_code, PI.img_url, P.name, O.order_status, O.total_price, O.created_at, O.id
                FROM ORDERS AS O JOIN order_items AS OI ON
                O.id = OI.order_id
                JOIN products AS P ON
                OI.product_id = P.id
                JOIN product_images AS PI ON
                P.id = PI.product_id AND PI.is_main = 1
                GROUP BY O.order_code
                ORDER BY O.created_at DESC
                limit :limit offset :offset
                """;

        return get()
                .withHandle(h ->
                        h.createQuery(query)
                                .bind("limit", pageSize)
                                .bind("offset", (pageIndex - 1) * pageSize)
                                .map((rs, ctx) -> {
                                    OrderCard orderCard = new OrderCard();

                                    orderCard.setOrder_code(rs.getString("order_code"));
                                    orderCard.setName(rs.getString("name"));
                                    orderCard.setUrl(rs.getString("img_url"));
                                    orderCard.setTotal_price(rs.getDouble("total_price"));
                                    orderCard.setOrder_status(rs.getString("order_status"));
                                    Timestamp ts = rs.getTimestamp("created_at");
                                    orderCard.setCreated_at(ts.toLocalDateTime());
                                    orderCard.setOrderID(rs.getInt("id"));


                                    return orderCard;
                                })
                                .list()
                );
    }


    public List<OrderCard> searchOrders(String orderName, int pageIndex, int pageSize) {
        String query = """
                SELECT O.order_code, PI.img_url, P.name, O.order_status, O.total_price, O.created_at, O.id
                FROM ORDERS AS O JOIN order_items AS OI ON
                O.id = OI.order_id
                JOIN products AS P ON
                OI.product_id = P.id
                JOIN product_images AS PI ON
                P.id = PI.product_id AND PI.is_main = 1
                WHERE P.name like :name
                GROUP BY O.order_code
                ORDER BY O.created_at DESC
                limit :limit offset :offset
                """;
        var data = get().withHandle(h ->
                h.createQuery(query)
                        .bind("name", "%" + orderName + "%")
                        .bind("limit", pageSize)
                        .bind("offset", (pageIndex - 1) * pageSize)
                        .map((rs, ctx) -> {
                            OrderCard orderCard = new OrderCard();

                            orderCard.setOrder_code(rs.getString("O.order_code"));
                            orderCard.setName(rs.getString("P.name"));
                            orderCard.setUrl(rs.getString("PI.img_url"));
                            orderCard.setTotal_price(rs.getDouble("O.total_price"));
                            orderCard.setOrder_status(rs.getString("order_status"));

                            Timestamp ts = rs.getTimestamp("created_at");
                            orderCard.setCreated_at(ts.toLocalDateTime());

                            orderCard.setOrderID(rs.getInt("id"));

                            return orderCard;
                        })
                        .list());
        return data;
    }

    public int countSearchOrders(String orderName) {
        String sql = """
                    SELECT COUNT(DISTINCT O.id)
                    FROM ORDERS O
                    JOIN order_items OI ON O.id = OI.order_id
                    JOIN products P ON OI.product_id = P.id
                    WHERE P.name LIKE :name
                """;

        return get().withHandle(h ->
                h.createQuery(sql)
                        .bind("name", "%" + orderName + "%")
                        .mapTo(int.class)
                        .one()
        );
    }

    public List<OrderCard> filterOrder(FilterRequest filter, int pageIndex, int pageSize) {
        StringBuilder sql = new StringBuilder("""
                SELECT O.order_code, PI.img_url, P.name, O.order_status, O.total_price, O.created_at, O.id
                FROM ORDERS AS O 
                JOIN order_items AS OI ON O.id = OI.order_id
                JOIN products AS P ON OI.product_id = P.id
                JOIN product_images AS PI ON P.id = PI.product_id AND PI.is_main = 1
                WHERE 1 = 1
                """);
        if (filter.getStatus() != null) {
            sql.append(" AND O.order_status = :status");
        }
        if (filter.getOrderDate() != null) {
            sql.append(" AND DATE(O.created_at) = :orderDate");
        }
        if (filter.getFrom() > 0) {
            sql.append(" AND O.total_price >= :from");
        }
        if (filter.getTo() > 0) {
            sql.append(" AND O.total_price <= :to");
        }
        sql.append(" GROUP BY O.order_code ORDER BY O.created_at DESC limit :limit offset :offset");
        return get().withHandle(h -> {
            var query = h.createQuery(sql.toString());
            if (filter.getStatus() != null) {
                query.bind("status", filter.getStatus());
            }

            if (filter.getOrderDate() != null) {
                query.bind("orderDate", filter.getOrderDate());
            }

            if (filter.getFrom() > 0) {
                query.bind("from", filter.getFrom());
            }

            if (filter.getTo() > 0) {
                query.bind("to", filter.getTo());
            }

            query.bind("limit", pageSize);

            query.bind("offset", (pageIndex - 1) * pageSize);

            return query.map((rs, ctx) -> {
                        OrderCard orderCard = new OrderCard();

                        orderCard.setOrder_code(rs.getString("O.order_code"));
                        orderCard.setName(rs.getString("P.name"));
                        orderCard.setUrl(rs.getString("PI.img_url"));
                        orderCard.setTotal_price(rs.getDouble("O.total_price"));
                        orderCard.setOrder_status(rs.getString("order_status"));

                        Timestamp ts = rs.getTimestamp("created_at");
                        orderCard.setCreated_at(ts.toLocalDateTime());

                        orderCard.setOrderID(rs.getInt("id"));

                        return orderCard;
                    })
                    .list();
        });

    }

    public int countFilterOrders(FilterRequest f) {
        StringBuilder sql = new StringBuilder("""
                    SELECT COUNT(DISTINCT O.id)
                    FROM ORDERS O
                    JOIN order_items OI ON O.id = OI.order_id
                    JOIN products P ON OI.product_id = P.id
                    WHERE 1=1
                """);

        if (f.getStatus() != null)
            sql.append(" AND O.order_status = :status");

        if (f.getOrderDate() != null)
            sql.append(" AND DATE(O.created_at) = :orderDate");

        if (f.getFrom() > 0)
            sql.append(" AND O.total_price >= :from");

        if (f.getTo() > 0)
            sql.append(" AND O.total_price <= :to");

        return get().withHandle(h -> {
            var q = h.createQuery(sql.toString());
            if (f.getStatus() != null) q.bind("status", f.getStatus());
            if (f.getOrderDate() != null) q.bind("orderDate", f.getOrderDate());
            if (f.getFrom() > 0) q.bind("from", f.getFrom());
            if (f.getTo() > 0) q.bind("to", f.getTo());
            return q.mapTo(int.class).one();
        });
    }

    public List<OrderItem> getOrderItemByID(String orderID) {
        String query = """
                SELECT oi.quantity, oi.price_at_purchase, 
                p.name, pi.img_url, o.order_status, o.order_code
                from order_items oi
                JOIN products p
                on p.id = oi.product_id
                JOIN product_images pi
                ON pi.product_id = p.id
                JOIN orders o
                on o.id = oi.order_id
                WHERE oi.order_id = :id AND pi.is_main = 1
                """;
        return get().withHandle(h ->
                h.createQuery(query)
                        .bind("id", Integer.parseInt(orderID))
                        .mapToBean(OrderItem.class)
                        .list()
        );
    }

    public CustomerInfo getCustomerInfoByOrder(String orderID) {
        String query = """
                SELECT u.name, u.email, u.phone_number, CONCAT(a.house_number, ', ', a.road, ', ', a.hamlet, ', ',
                 a.ward, ', ', a.district, ', ', a.city) as address, o.payment_status, o.created_at
                FROM users u
                JOIN orders o
                on u.id = o.user_id
                join adresses a
                ON u.id = a.user_id
                WHERE o.id = :id
                """;
        return get().withHandle(h ->
                h.createQuery(query)
                        .bind("id", Integer.parseInt(orderID))
                        .map((rs, ctx) -> {
                            CustomerInfo info = new CustomerInfo();
                            info.setName(rs.getString("name"));
                            info.setEmail(rs.getString("email"));
                            info.setAddress(rs.getString("address"));
                            info.setPhoneNumber(rs.getString("phone_number"));
                            String paymentStatus = rs.getString("payment_status");
                            info.setPaymentMethod(Payment.valueOf(paymentStatus.trim().toUpperCase()).getStatus());
                            Timestamp ts = rs.getTimestamp("created_at");
                            info.setOrderCreateAt(ts.toLocalDateTime());
                            return info;
                        })
                        .one()
        );
    }

    public Order getTotalPriceByOrder(String orderID){
        String query = """
                SELECT o.total_price, o.shipping_fee, o.discount_amount, o.final_amount
                From orders o
                where o.id = :id
                GROUP BY o.order_code
                """;
        return get().withHandle(h ->
                h.createQuery(query)
                        .bind("id", Integer.parseInt(orderID))
                        .mapToBean(Order.class)
                        .one()
                );
    }


}
