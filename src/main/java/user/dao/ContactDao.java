package user.dao;

import config.DbConfig;

public class ContactDao extends DbConfig  {
    public boolean sendMessage(int userId, String name, String email, String phone, String subject, String message) {
        String sql = """
                    INSERT INTO contacts (user_id, name, email, phone, topic, message, status, admin_id, created_at)
                    VALUES (:userId, :name, :email, :phone, :topic, :message, 'pending', 1, NOW())
                """;

        try {
            get().useHandle(handle -> handle.createUpdate(sql)
                    .bind("userId", userId)
                    .bind("name", name)
                    .bind("email", email)
                    .bind("phone", phone)
                    .bind("topic", subject)
                    .bind("message", message)
                    .execute());
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
