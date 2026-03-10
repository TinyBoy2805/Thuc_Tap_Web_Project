package user.service;

import user.dao.ContactDao;

public class ContactService
{
    private ContactDao contactDao;
    public ContactService()
    {
        this.contactDao = new ContactDao();
    }

    public boolean sendMessage(int userId, String name, String email, String phone, String subject, String message)
    {
        return this.contactDao.sendMessage(userId, name, email, phone, subject, message);
    }



}
