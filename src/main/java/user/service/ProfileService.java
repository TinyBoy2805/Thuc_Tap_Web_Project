package user.service;

import admin.service.AuthService;
import user.dao.AddressDao;
import admin.dao.AuthDao;
import user.dao.UserDAO;
import user.model.Address;
import admin.model.User;
import user.model.UserProfile;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

public class ProfileService
{
    private UserDAO userDAO;
    private AddressDao addressDao;
    private AuthService authService;
    private AuthDao authDao;

    public ProfileService()
    {
        this.userDAO = new UserDAO();
        this.addressDao = new AddressDao();
        this.authService = new AuthService();
        this.authDao = new AuthDao();
    }


    public UserProfile getUserProfile(Integer userId)
    {
        User user = this.userDAO.getUserById(userId);
        if (user == null) return null;
        List<Address> addresses = this.addressDao.getAllAddressesByUserId(userId);
        System.out.println(addresses);
        UserProfile userProfile = new UserProfile();

        userProfile.setName(user.getName());
        userProfile.setEmail(user.getEmail());
        userProfile.setPhone_number(user.getPhone_number());
        userProfile.setAvt_url(user.getAvt_url());
        userProfile.setMyAddresses(addresses);

        System.out.println("lay db duoc roi");
        return userProfile;
    }

//    public boolean updateAdminProfile(int userId, String name, String email, String phone, String avtUrl) {
//        boolean ok1 = userDAO.updateName(userId, name);
//        boolean ok2 = userDAO.updateEmail(userId, email);
//        boolean ok3 = userDAO.updatePhone(userId, phone);
//        boolean ok4 = true;
//        if (avtUrl != null && !avtUrl.isEmpty()) {
//            ok4 = userDAO.updateAvatar(userId, avtUrl);
//        }
//        return ok1 && ok2 && ok3 && ok4;
//    }

    public boolean changeAvt(Integer userId, String avtUrl)
    {
        return this.userDAO.changeAvt(userId, avtUrl);
    }

    public boolean deleteAccount(int userId)
    {
        return this.userDAO.deleteAccount(userId);
    }

    public long addAddress(int userId, String houseNumber, String road, String district, String city, String hamlet, String ward, boolean isDefault) {
        return this.addressDao.addAddress(userId, houseNumber, road, district, city, hamlet, ward, isDefault);
    }

    public boolean deleteAddress(long addressId) {
        return this.addressDao.deleteAddress(addressId);
    }

    public boolean setDefaultAddress(int userId, long addressId) {
        return this.addressDao.setDefaultAddress(userId, addressId);
    }

    public boolean updateAddress(long addressId, String houseNumber, String road, String district, String city, String hamlet, String ward, boolean is_default, int userId)
    {
        return this.addressDao.updateAddress(addressId, houseNumber, road, district, city, hamlet, ward, is_default, userId);
    }

    public boolean updateName(int userId, String newName) {
        return this.userDAO.updateName(userId, newName);
    }

    public boolean updatePhone(int userId, String newPhone) {
        return this.userDAO.updatePhone(userId, newPhone);
    }

    public boolean updatePassword(int userId, String oldPassword, String newPassword) throws NoSuchAlgorithmException {
        User user = this.userDAO.getUserById(userId);
        if (user == null) return false;

        String hashedOld = authService.hashPasswordUsingMD5(oldPassword, user.getSalt(), "");
        if (!hashedOld.equals(user.getPassword_hashed())) {
            return false;
        }

        String newSalt = UUID.randomUUID().toString();
        String hashedNew = authService.hashPasswordUsingMD5(newPassword, newSalt, "");
        return authDao.setNewPassword(userId, hashedNew, new StringBuilder(newSalt));
    }
}
