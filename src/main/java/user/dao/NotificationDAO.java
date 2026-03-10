package user.dao;

import config.DbConfig;
import user.model.Notification;

import java.util.Collections;
import java.util.List;

public class NotificationDAO extends DbConfig
{


    public List<Notification> getNotificationByPage(int page, int pageSize,int userId)
    {
        System.out.println("DB được gọi nè");
        int offset = (page - 1) * pageSize;

        String query = "SELECT * FROM notifications\n" +
                "WHERE user_id = :userId\n" +
                "ORDER BY is_read ASC, created_at DESC\n" +
                "LIMIT " + pageSize + " OFFSET " + offset;


        try
        {
            List<Notification> res = get().withHandle(h ->
                    h.createQuery(query)
                            .bind("userId", userId)
                            .mapToBean(Notification.class)
                            .list()
            );
//            System.out.println("Res from DAO: " + res);
            return res;
        } catch (Exception e)
        {
            e.printStackTrace();
            return Collections.emptyList();
        }

    }


    public boolean markAllRead(int userId)
    {
        String query = "update notifications\n" +
                        "set is_read = 1\n" +
                        "where user_id = :userId";

        int rows = get().withHandle(h->
                            h.createUpdate(query)
                            .bind("userId", userId)
                            .execute()
                );

        return rows > 0;
    }

    public boolean createNotification(int userId, String title, String message) {
        String sql = """
            INSERT INTO notifications (user_id, title, message, is_read, created_at)
            VALUES (:userId, :title, :message, 0, NOW())
        """;
        return get().withHandle(handle ->
            handle.createUpdate(sql)
                .bind("userId", userId)
                .bind("title", title)
                .bind("message", message)
                .execute() > 0
        );
    }
}
