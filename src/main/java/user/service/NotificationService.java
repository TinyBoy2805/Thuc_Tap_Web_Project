package user.service;

import user.dao.NotificationDAO;
import user.model.Notification;

import java.util.List;

public class NotificationService
{

    private NotificationDAO notificationDAO;
    public NotificationService()
    {
        this.notificationDAO = new NotificationDAO();
    }



    public List<Notification> getNotificationByPage(int page, int pageSize, int userId)
    {
//        System.out.println("Service được gọi nè");
        return this.notificationDAO.getNotificationByPage(page, pageSize, userId);
    }


    public boolean markAllRead(int userId)
    {
        return this.notificationDAO.markAllRead(userId);
    }

    public boolean createNotification(int userId, String title, String message) {
        return this.notificationDAO.createNotification(userId, title, message);
    }
}
