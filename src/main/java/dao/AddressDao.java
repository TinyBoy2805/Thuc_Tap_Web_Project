package dao;

import model.Address;
import java.util.List;

public class AddressDao extends BaseDao {

    // Lấy địa chỉ mặc định của user
    // Lấy địa chỉ mặc định (is_default=1) của user
    public Address getDefaultAddressByUserId(long userId) {
        String sql = "SELECT * FROM addresses WHERE user_id = :userId AND is_default = 1 LIMIT 1";
        return get().withHandle(handle ->
            handle.createQuery(sql)
                .bind("userId", userId)
                .map((rs, ctx) -> new Address(
                    rs.getLong("id"),
                    rs.getLong("user_id"),
                    rs.getString("house_number"),
                    rs.getString("road"),
                    rs.getString("district"),
                    rs.getString("city"),
                    rs.getString("hamlet"),
                    rs.getString("ward"),
                    rs.getBoolean("is_default")
                ))
                .findOne()
                .orElseThrow(() -> new RuntimeException("Khong tim thay"))
        );
    }

    // Xóa địa chỉ gắn với người dùng
    public void deleteAddressByUserId(long userId) {
        get().useHandle(h ->
            h.createUpdate("DELETE FROM addresses WHERE user_id = :userId")
                .bind("userId", userId)
                .execute()
        );
    }
    
    // Lấy TẤT CẢ địa chỉ của user (sắp xếp default lên đầu)
    public List<Address> getAllAddressesByUserId(long userId) {
        String sql = "SELECT * FROM addresses WHERE user_id = :userId ORDER BY is_default DESC, id ASC";
        return get().withHandle(handle ->
            handle.createQuery(sql)
                .bind("userId", userId)
                .map((rs, ctx) -> new Address(
                    rs.getLong("id"),
                    rs.getLong("user_id"),
                    rs.getString("house_number"),
                    rs.getString("road"),
                    rs.getString("district"),
                    rs.getString("city"),
                    rs.getString("hamlet"),
                    rs.getString("ward"),
                    rs.getBoolean("is_default")
                ))
                .list()
        );
    }
    
    // Lấy địa chỉ theo ID
    public Address getAddressById(long addressId) {
        String sql = "SELECT * FROM addresses WHERE id = :addressId";
        return get().withHandle(handle ->
            handle.createQuery(sql)
                .bind("addressId", addressId)
                .map((rs, ctx) -> new Address(
                    rs.getLong("id"),
                    rs.getLong("user_id"),
                    rs.getString("house_number"),
                    rs.getString("road"),
                    rs.getString("district"),
                    rs.getString("city"),
                    rs.getString("hamlet"),
                    rs.getString("ward"),
                    rs.getBoolean("is_default")
                ))
                .findOne()
                .orElse(null)
        );
    }
    
    // Thêm địa chỉ mới
    public long addAddress(long userId, String houseNumber, String road, String district, String city, 
                          String hamlet, String ward, boolean isDefault) {
        // Nếu đặt làm default, bỏ default của các địa chỉ khác
        if (isDefault) {
            clearDefaultAddress(userId);
        }
        
        String sql = """
            INSERT INTO addresses (user_id, house_number, road, district, city, hamlet, ward, is_default)
            VALUES (:userId, :houseNumber, :road, :district, :city, :hamlet, :ward, :isDefault)
        """;
        
        return get().withHandle(handle ->
            handle.createUpdate(sql)
                .bind("userId", userId)
                .bind("houseNumber", houseNumber)
                .bind("road", road)
                .bind("district", district)
                .bind("city", city)
                .bind("hamlet", hamlet)
                .bind("ward", ward)
                .bind("isDefault", isDefault ? 1 : 0)
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Long.class)
                .one()
        );
    }
    
    // Cập nhật địa chỉ
    public boolean updateAddress(long addressId, String houseNumber, String road, String district, 
                                String city, String hamlet, String ward, boolean isDefault, long userId) {
        if (isDefault) {
            clearDefaultAddress(userId);
        }
        String sql = """
            UPDATE addresses
            SET house_number = :houseNumber,
                road = :road,
                district = :district,
                city = :city,
                hamlet = :hamlet,
                ward = :ward,
                is_default = :isDefault
            WHERE id = :addressId
        """;
        int updated = get().withHandle(handle ->
            handle.createUpdate(sql)
                .bind("addressId", addressId)
                .bind("houseNumber", houseNumber)
                .bind("road", road)
                .bind("district", district)
                .bind("city", city)
                .bind("hamlet", hamlet)
                .bind("ward", ward)
                .bind("isDefault", isDefault ? 1 : 0)
                .execute()
        );
        return updated > 0;
    }
    
    // Xóa địa chỉ
    public boolean deleteAddress(long addressId) {
        String sql = "DELETE FROM addresses WHERE id = :addressId";
        int deleted = get().withHandle(handle ->
            handle.createUpdate(sql)
                .bind("addressId", addressId)
                .execute()
        );
        return deleted > 0;
    }
    
    // Đặt địa chỉ làm mặc định
    public boolean setDefaultAddress(long userId, long addressId) {
        // Bỏ default của tất cả địa chỉ khác
        clearDefaultAddress(userId);
        
        // Set địa chỉ này làm default
        String sql = "UPDATE addresses SET is_default = 1 WHERE id = :addressId AND user_id = :userId";
        int updated = get().withHandle(handle ->
            handle.createUpdate(sql)
                .bind("addressId", addressId)
                .bind("userId", userId)
                .execute()
        );
        return updated > 0;
    }
    
    // Bỏ default của tất cả địa chỉ
    private void clearDefaultAddress(long userId) {
        String sql = "UPDATE addresses SET is_default = 0 WHERE user_id = :userId";
        get().useHandle(handle ->
            handle.createUpdate(sql)
                .bind("userId", userId)
                .execute()
        );
    }
}

