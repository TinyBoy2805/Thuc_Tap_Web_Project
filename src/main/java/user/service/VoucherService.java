package user.service;

import user.dao.VoucherDAO;
import user.model.Voucher;

import java.util.List;

public class VoucherService
{
    private VoucherDAO voucherDAO;

    public VoucherService()
    {
        this.voucherDAO = new VoucherDAO();
    }

    public List<Voucher> getVouchers(int page, int pageSize)
    {
        return this.voucherDAO.getVouchers(page, pageSize);
    }

    public boolean addVoucherToUser(int id, int voucherId)
    {
        return this.voucherDAO.addVoucherToUser(id, voucherId);
    }

    public List<Voucher> getUserVouchers(int userId) {
        return this.voucherDAO.getUserVouchers(userId);
    }
    public Voucher getVoucherByCode(String code) {
        return this.voucherDAO.getVoucherByCode(code);
    }

    public boolean markVoucherAsUsed(int userId, int voucherId) {
        return this.voucherDAO.markVoucherAsUsed(userId, voucherId);
    }
}
