package admin.service;

import admin.dao.AuthDao;
import admin.Enums.PasswordStrength;
import admin.Enums.RegisterError;
import admin.model.ValidateObject;
import admin.model.User;
import org.apache.commons.validator.routines.EmailValidator;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;


public class AuthService {
    private AuthDao authDao = new AuthDao();

    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EmailValidator.getInstance().isValid(email);
    }

    public boolean isExistEmail(String email) {
        return this.authDao.existsByEmail(email);
    }


    public String hashPasswordUsingMD5(String password, String salt, String pepper) throws NoSuchAlgorithmException {
        MessageDigest msd = MessageDigest.getInstance("MD5");

        String combined = password + salt + pepper;

        byte[] digest = msd.digest(combined.getBytes(StandardCharsets.UTF_8));

        BigInteger bigInt = new BigInteger(1, digest);

        return String.format("%032x", bigInt);
    }

    private boolean isValidPhone(String phone) {
        String cleanPhone = phone.replaceAll("[\\s\\-\\.]", "");
        String phoneRegex = "^(0|\\+84)(\\d{9,10})$";
        return cleanPhone.matches(phoneRegex);
    }

    private boolean isMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public PasswordStrength checkPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return PasswordStrength.REQUIRED;
        }

        if (password.length() < 10) {
            return PasswordStrength.TOO_SHORT;
        }

        if (!password.matches(".*\\d.*")) {
            return PasswordStrength.NO_DIGIT;
        }

        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            return PasswordStrength.NO_SPECIAL_CHAR;
        }

        return PasswordStrength.STRONG;
    }

    public boolean isWeakPassword(String password) {
        return checkPasswordStrength(password).isWeak();
    }


    public int createNewUser(String username, String hashedPassword, String email, String phone, String salt) {
        return this.authDao.insertAndGetId(username, email, "user", hashedPassword, phone, salt);
    }

    public ValidateObject validateRegistration(String username, String password, String confirm_password, String email, String phone) {
        ValidateObject errors = new ValidateObject();

        // Validate username
        if (username == null || username.trim().isEmpty()) {
            errors.setUsernameError("Yêu cầu tên người dùng");
        } else if (username.length() < 3) {
            errors.setUsernameError("Tên nên có tối thiểu từ 3 kí tự");
        }

        // Validate email
        if (email == null || email.trim().isEmpty()) {
            errors.setEmailError("Yêu cầu email");
        } else {
            RegisterError emailCheck = RegisterError.NONE; // INVALID_EMAIL_FORMAT hoặc EMAIL_EXIST

            if (this.isExistEmail(email)) emailCheck = RegisterError.EMAIL_EXIST;
            else if (!this.isValidEmail(email)) emailCheck = RegisterError.INVALID_EMAIL_FORMAT;

            switch (emailCheck) {
                case INVALID_EMAIL_FORMAT:
                    errors.setEmailError("Sai định dạng email");
                    break;
                case EMAIL_EXIST:
                    errors.setEmailError("Email đã tồn tại");
                    break;
            }
        }

        // Validate phone
        if (phone == null || phone.trim().isEmpty()) {
            errors.setPhoneError("Yêu cầu số điện thoại");
        } else {
            RegisterError phoneCheck = RegisterError.NONE; // PHONE_INVALID
            if (!this.isValidPhone(phone)) phoneCheck = RegisterError.PHONE_INVALID;
            if (phoneCheck == RegisterError.PHONE_INVALID) {
                errors.setPhoneError("Sai định dạng số điện thoại");
            }
        }

        // Validate password
        if (password == null || password.trim().isEmpty()) {
            errors.setPasswordError("Yêu cầu mật khẩu");
        } else {
            PasswordStrength strength = checkPasswordStrength(password);
            if (strength.isWeak()) {
                errors.setPasswordError(strength.getMessage());
            }
        }

        // Validate confirm password
        if (confirm_password == null || !confirm_password.equals(password)) {
            errors.setConfirmPasswordError("Mật khẩu không khớp");
        }

        return errors;
    }

    public void createTokenAndExpiredTime(int userId, String token, Timestamp expirationTime, String type) {
        this.authDao.createTokenAndExpiredTime(userId, token, expirationTime, type);
    }

    public boolean checkToken(String token, String type) {
        boolean hasToken = this.authDao.checkToken(token);
        boolean hasTime = this.authDao.checkTokenExpired(token);
        boolean hasNotUsed = this.authDao.checkTokenNotUsed(token);
        boolean hasType = this.authDao.checkType(type);


        if (hasToken && hasTime && hasNotUsed && hasType) {
            this.authDao.setTokenUsed(token);
        }

        return hasToken && hasTime && hasNotUsed && hasType;
    }

    public int getUserIdFromVerifyToken(String token) {
        return this.authDao.getUserIdFromVerifyToken(token);
    }

    public boolean setVerifyUser(int userId) {
        return this.authDao.setVerifyUser(userId);
    }

    public void setVerified(String email) {
        AuthDao authDao = new AuthDao();
        authDao.updateVerified(email);
    }

    public User findByEmailOrPhone(String input) {
        return this.authDao.findByEmailOrPhone(input);
    }

    /**
     * Đổi mật khẩu cho khách hàng bởi admin
     *
     * @param id              ID người dùng
     * @param newPassword     mật khẩu mới
     * @param confirmPassword xác nhận mật khẩu
     * @return true nếu đổi thành công, false nếu xác nhận không khớp hoặc yếu
     */
    public boolean adminChangeUserPassword(int id, String newPassword, String confirmPassword) {
        if (!isMatch(newPassword, confirmPassword)) {
            return false;
        }
        PasswordStrength strength = checkPasswordStrength(newPassword);
        if (strength.isWeak()) {
            return false;
        }
        // Lấy salt của user
        String salt;
        try {
            salt = this.authDao.getSaltByUserId(id);
        } catch (Exception e) {
            return false;
        }
        if (salt == null) return false;
        String pepper = "TOI IU NLU-FIT";
        String hashedPassword;
        try {
            hashedPassword = hashPasswordUsingMD5(newPassword, salt, pepper);
        } catch (Exception e) {
            return false;
        }
        // Cập nhật mật khẩu
        return this.authDao.updateUserPassword(id, hashedPassword);
    }

    public boolean setNewPassword(int userId, String hashedPassword, StringBuilder salt) {
        return this.authDao.setNewPassword(userId, hashedPassword, salt);
    }
}
