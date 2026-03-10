package admin.dao;

import admin.Enums.Role;
import admin.exception.InternalServerErrorException;
import admin.exception.UserNotFoundException;
import admin.model.User;
import config.DbConfig;

import java.sql.Timestamp;
import java.util.List;

// nem ngoai le o logic nghiep vu (tim user), boc try-catch o logic query
public class AuthDao extends DbConfig {

    public User getUserByName(String name) {
        try {
            return get().withHandle(h -> h.createQuery("SELECT * FROM users WHERE name = :name")
                    .bind("name", name)
                    .mapToBean(User.class)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new UserNotFoundException("User not found with name " + name)));
        } catch (InternalServerErrorException e) {
            throw new InternalServerErrorException("Internal Server Error 500");
        }
    }

    public User getUserByEmailOrPhone(String value) {
        return get().withHandle(h -> //get() : get connection to db from BaseDao
                //lambda function that return User
                h.createQuery("SELECT * FROM users WHERE email = :v OR phone_number = :v")
                        .bind("v", value)
                        //bind variable v into SQL with input info (value)
                        .map((rs, _) ->
                                User.builder()
                                        .id(rs.getInt("id"))
                                        .name(rs.getString("name"))
                                        .email(rs.getString("email"))
                                        .role(Role.valueOf(rs.getString("role")))
                                        .password_hashed(rs.getString("password_hashed"))
                                        .phone_number(rs.getString("phone_number"))
                                        .avt_url(rs.getString("avt_url"))
                                        .salt(rs.getString("salt"))
                                        .verified(rs.getInt("verified"))
                                        .build())
                        .findFirst()
                        .orElseThrow(() -> new UserNotFoundException("User not found with Email or Phone " + value))
        );
    }

    // try - catch
    public boolean existsByEmail(String email) {
        return get().withHandle(h ->
                h.createQuery("SELECT 1 FROM users WHERE email = :email")
                        .bind("email", email)
                        .mapTo(Integer.class)
                        .findFirst()
                        .isPresent()
        );
    }

    public void insert(User user) {
        get().useHandle(h ->
                h.createUpdate("""
                                    INSERT INTO users(name, email, role, password_hashed, phone_number, salt, verified)
                                    VALUES (:name, :email, :role, :password, :phone, :salt, 0)
                                """)
                        .bind("name", user.getName())
                        .bind("email", user.getEmail())
                        .bind("role", user.getRole().name())
                        .bind("password", user.getPassword_hashed())
                        .bind("phone", user.getPhone_number())
                        .bind("salt", user.getSalt())
                        .execute()
        );
    }

    public int insertAndGetId(String name, String email, String role, String password_hashed, String phone_number, String salt) {
        return get().withHandle(h ->
                h.createUpdate("""
                                    INSERT INTO users(name, email, role, password_hashed, phone_number, salt, verified)
                                    VALUES (:name, :email, :role, :password, :phone, :salt, 0)
                                """)
                        .bind("name", name)
                        .bind("email", email)
                        .bind("role", role)
                        .bind("password", password_hashed)
                        .bind("phone", phone_number)
                        .bind("salt", salt)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one()
        );
    }


    //user.service continue to call DAO to activate account into db
    public void activateAccount(String email) {
        get().useHandle(h ->
                h.createUpdate("UPDATE users SET verified = 1 WHERE email = :email")
                        .bind("email", email)
                        .execute()
        );
    }

    //update email for user
    public void updateEmail(String oldEmail, String newEmail) {
        get().useHandle(h ->
                h.createUpdate("UPDATE users SET email = :new, verified = 0 WHERE email = :old")
                        .bind("old", oldEmail)
                        .bind("new", newEmail)
                        .execute()
        );
    }


    public void saveOTP(String email, String otp) {
        String sql = "UPDATE users SET otp=:otp WHERE email=:email";
        get().withHandle(h -> h.createUpdate(sql)
                .bind("otp", otp).bind("email", email).execute());
    }

    public String getOTP(String email) {
        String sql = "SELECT otp FROM users WHERE email=:email";
        return get().withHandle(h ->
                h.createQuery(sql).bind("email", email)
                        .mapTo(String.class).findOne().orElse(null)
        );
    }
//
//    public void updateVerified(String email) {
//        String sql = "UPDATE users SET verified=1, otp=NULL WHERE email=:email";
//        get().withHandle(h -> h.createUpdate(sql)
//                .bind("email", email).execute());
//    }

    // Lấy danh sách tất cả người dùng có vai trò là khách hàng
    public List<User> getAllCustomers() {
        return get().withHandle(handle ->
                handle.createQuery("""
                                SELECT id, name, email, phone_number,
                                    avt_url,
                                    role
                                FROM users
                                WHERE role = 'customer'
                                """)
                        .mapToBean(User.class)
                        .list()
        );
    }

    // Lấy chi tiết 1 khách hàng theo ID
    public User getUserById(int id) {
        return get().withHandle(handle ->
                handle.createQuery("""
                                SELECT id, name, email, role, phone_number, avt_url
                                FROM users
                                WHERE id = :id
                                """)
                        .bind("id", id)
                        .mapToBean(User.class)
                        .findOne()
                        .orElse(null)
        );
    }
    // vler t eo nghe gi het, nhan vao day di
    //auth deu dung cho ca 2 ma, dung roi, nhung authDao nay dang nam trong admin, hay chuyen no sang folder Auth

    // Cập nhật thông tin khách hàng
    public void updateUserInfo(int id, String name, String email, String phoneNumber, String avtUrl) {
        get().useHandle(h ->
                h.createUpdate("""
                                    UPDATE users
                                    SET name = :name,
                                        email = :email,
                                        phone_number = :phoneNumber,
                                        avt_url = :avtUrl
                                    WHERE id = :id
                                """)
                        .bind("id", id)
                        .bind("name", name)
                        .bind("email", email)
                        .bind("phoneNumber", phoneNumber)
                        .bind("avtUrl", avtUrl)
                        .execute()
        );
    }

    // Cập nhật đường dẫn ảnh đại diện
    public void updateAvatar(int id, String avtUrl) {
        get().useHandle(h ->
                h.createUpdate("UPDATE users SET avt_url = :avtUrl WHERE id = :id")
                        .bind("id", id)
                        .bind("avtUrl", avtUrl)
                        .execute()
        );
    }

    // Xóa tài khoản người dùng
    public void deleteUser(int id) {
        get().useHandle(h ->
                h.createUpdate("DELETE FROM users WHERE id = :id")
                        .bind("id", id)
                        .execute()
        );
    }

    // Tìm kiếm khách hàng theo tên
    public List<User> searchCustomersByName(String keyword) {
        return get().withHandle(handle ->
                handle.createQuery("""
                                    SELECT id, name, email, phone_number, avt_url, role
                                    FROM users
                                    WHERE role = 'customer' AND LOWER(name) LIKE CONCAT('%', LOWER(:keyword), '%')
                                """)
                        .bind("keyword", keyword)
                        .mapToBean(User.class)
                        .list()
        );
    }

    // Cập nhật thông tin khách hàng
    public void updateUserInfo(int id, String name, String email, String phoneNumber) {
        get().useHandle(h ->
                h.createUpdate("""
                                    UPDATE users
                                    SET name = :name,
                                        email = :email,
                                        phone_number = :phoneNumber
                                    WHERE id = :id
                                """)
                        .bind("id", id)
                        .bind("name", name)
                        .bind("email", email)
                        .bind("phoneNumber", phoneNumber)
                        .execute()
        );
    }

    public void updatePassword(String email, String hashPassword) {
        get().useHandle(h ->
                h.createUpdate("""
                                UPDATE users
                                SET password_hashed = :password
                                WHERE email = :email
                                """)
                        .bind("password", hashPassword)
                        .bind("email", email)
                        .execute()
        );
    }

    public void createTokenAndExpiredTime(int userId, String token, Timestamp expirationTime, String type) {
        get().useHandle(h ->
                h.createUpdate("""
                                    INSERT INTO verification_tokens(user_id, token, type, expires_at, used)
                                    VALUES (:userId, :token, :type, :expiresAt, 0)
                                """)
                        .bind("userId", userId)
                        .bind("token", token)
                        .bind("expiresAt", expirationTime)
                        .bind("type", type)
                        .execute()
        );
    }

    public boolean checkToken(String token) {
        return get().withHandle(h ->
                h.createQuery("SELECT COUNT(*) FROM verification_tokens WHERE token = :token")
                        .bind("token", token)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }

    public boolean checkTokenExpired(String token) {
        return get().withHandle(h ->
                h.createQuery("""
                                    SELECT COUNT(*) FROM verification_tokens 
                                    WHERE token = :token 
                                    AND expires_at > NOW()
                                """)
                        .bind("token", token)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }

    public boolean checkTokenNotUsed(String token) {
        return get().withHandle(h ->
                h.createQuery("""
                                    SELECT COUNT(*) FROM verification_tokens 
                                    WHERE token = :token 
                                    AND used = 0
                                """)
                        .bind("token", token)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }

    public void setTokenUsed(String token) {
        get().useHandle(h ->
                h.createUpdate("UPDATE verification_tokens SET used = 1 WHERE token = :token")
                        .bind("token", token)
                        .execute()
        );
    }

    public int getUserIdFromVerifyToken(String token) {
        return get().withHandle(h ->
                h.createQuery("SELECT user_id FROM verification_tokens WHERE token = :token")
                        .bind("token", token)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(-1) // Trả về -1 nếu không tìm thấy
        );
    }

    public boolean setVerifyUser(int userId) {
        int rowsAffected = get().withHandle(h ->
                h.createUpdate("UPDATE users SET verified = 1 WHERE id = :userId")
                        .bind("userId", userId)
                        .execute()
        );

        return rowsAffected > 0;
    }

    public User findByEmailOrPhone(String input) {
        return get().withHandle(h ->
                h.createQuery("""
                                    SELECT * FROM users 
                                    WHERE email = :input OR phone_number = :input
                                    LIMIT 1
                                """)
                        .bind("input", input)
                        .mapToBean(User.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public String getSaltByUserId(int id) {
        return get().withHandle(h ->
                h.createQuery("SELECT salt FROM users WHERE id = :id")
                        .bind("id", id)
                        .mapTo(String.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public boolean updateUserPassword(int id, String hashedPassword) {
        int rows = get().withHandle(h ->
                h.createUpdate("UPDATE users SET password_hashed = :pwd WHERE id = :id")
                        .bind("pwd", hashedPassword)
                        .bind("id", id)
                        .execute()
        );
        return rows > 0;
    }

    public void updateVerified(String email) {
        String sql = "UPDATE users SET verified=1, otp=NULL WHERE email=:email";
        get().withHandle(h -> h.createUpdate(sql)
                .bind("email", email).execute());
    }

    public boolean checkType(String type) {
        String query = """
                SELECT COUNT(*) FROM verification_tokens\s
                                WHERE type = :type AND used = 0
                """;

        return get().withHandle(h ->
                h.createQuery(query)
                        .bind("type", type)
                        .mapTo(Integer.class)
                        .one() > 0
        );

    }

    public boolean setNewPassword(int userId, String hashedPassword, StringBuilder salt) {
        String sql = "UPDATE users SET password_hashed = :pwd, salt = :salt WHERE id = :uid";
        int rows = get().withHandle(h ->
                h.createUpdate(sql)
                        .bind("pwd", hashedPassword)
                        .bind("salt", salt)
                        .bind("uid", userId)
                        .execute()
        );
        return rows > 0;
    }
}

