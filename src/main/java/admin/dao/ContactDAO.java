package admin.dao;

import admin.model.Contact;
import config.DbConfig;

import java.util.*;
import java.sql.*;

public class ContactDAO extends DbConfig {

    public List<Contact> getAllContacts() {
        String sql = "SELECT * FROM contacts ORDER BY created_at DESC";
        return get().withHandle(h ->
                h.createQuery(sql)
                        .map((rs, ctx) -> mapContact(rs))
                        .list()
        );
    }

    public Contact getContactById(int id) {
        String sql = "SELECT * FROM contacts WHERE id = :id";
        return get().withHandle(h ->
                h.createQuery(sql)
                        .bind("id", id)
                        .map((rs, ctx) -> mapContact(rs))
                        .findFirst()
                        .orElse(null)
        );
    }

    public void markAsRead(int id) {
        String sql = "UPDATE contacts SET is_read = 1 WHERE id = :id";
        get().withHandle(h ->
                h.createUpdate(sql)
                        .bind("id", id)
                        .execute()
        );
    }

    public void deleteContact(int id) {
        String sql = "DELETE FROM contacts WHERE id = :id";
        get().withHandle(h ->
                h.createUpdate(sql)
                        .bind("id", id)
                        .execute()
        );
    }

    public void replyContact(int id, String response, int adminId) {
        String sql = "UPDATE contacts SET admin_response = :response, responded_at = NOW(), admin_id = :adminId, is_read = 1 WHERE id = :id";
        get().withHandle(h ->
                h.createUpdate(sql)
                        .bind("response", response)
                        .bind("adminId", adminId)
                        .bind("id", id)
                        .execute()
        );
    }

    private Contact mapContact(ResultSet rs) throws SQLException {
        Contact c = new Contact();
        c.setId(rs.getInt("id"));
        c.setUserId(rs.getInt("user_id"));
        c.setName(rs.getString("name"));
        c.setEmail(rs.getString("email"));
        c.setPhone(rs.getString("phone"));
        c.setTopic(rs.getString("topic"));
        c.setMessage(rs.getString("message"));
        c.setAdminResponse(rs.getString("admin_response"));
        c.setRespondedAt(rs.getTimestamp("responded_at"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        c.setAdminId(rs.getInt("admin_id"));
        c.setIsRead(rs.getBoolean("is_read"));
        return c;
    }

}
