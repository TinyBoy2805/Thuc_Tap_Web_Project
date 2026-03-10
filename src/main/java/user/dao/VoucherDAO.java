package user.dao;

import config.DbConfig;
import user.model.Voucher;
import java.util.List;

public class VoucherDAO extends DbConfig
{

    public List<Voucher> getVouchers(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String sql = """
                SELECT * FROM vouchers
                WHERE current_amount > 0
                AND start_date <= CURDATE()
                AND end_date >= CURDATE()
                ORDER BY id DESC
                LIMIT :limit OFFSET :offset
                """;
        return get().withHandle(h ->
                h.createQuery(sql)
                        .bind("limit", pageSize)
                        .bind("offset", offset)
                        .mapToBean(Voucher.class)
                        .list()
        );
    }

    public boolean addVoucherToUser(int userId, int voucherId) {
        try {
            return get().inTransaction(handle -> {
                Integer exists = handle.createQuery("SELECT COUNT(*) FROM user_vouchers WHERE user_id = :uid AND voucher_id = :vid")
                        .bind("uid", userId)
                        .bind("vid", voucherId)
                        .mapTo(Integer.class)
                        .one();

                if (exists > 0) return false;

                Integer currentAmount = handle.createQuery("SELECT current_amount FROM vouchers WHERE id = :vid")
                        .bind("vid", voucherId)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(0);

                if (currentAmount <= 0) return false;

                int updated = handle.createUpdate("UPDATE vouchers SET current_amount = current_amount - 1 WHERE id = :vid AND current_amount > 0")
                        .bind("vid", voucherId)
                        .execute();

                if (updated == 0) return false;

                handle.createUpdate("INSERT INTO user_vouchers(user_id, voucher_id) VALUES (:uid, :vid)")
                        .bind("uid", userId)
                        .bind("vid", voucherId)
                        .execute();

                return true;
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Voucher> getUserVouchers(int userId) {
        String sql = """
                SELECT v.*
                FROM vouchers v
                JOIN user_vouchers uv ON v.id = uv.voucher_id
                WHERE uv.user_id = :uid
                AND v.end_date >= CURDATE()
                """;
        return get().withHandle(h ->
                h.createQuery(sql)
                        .bind("uid", userId)
                        .mapToBean(Voucher.class)
                        .list()
        );
    }

    public boolean markVoucherAsUsed(int userId, int voucherId) {
        try {
            int rows = get().withHandle(h ->
                    h.createUpdate("DELETE FROM user_vouchers WHERE user_id = :uid AND voucher_id = :vid")
                            .bind("uid", userId)
                            .bind("vid", voucherId)
                            .execute()
            );
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Voucher getVoucherByCode(String code) {
        return get().withHandle(h ->
                h.createQuery("SELECT * FROM vouchers WHERE code = :code")
                        .bind("code", code)
                        .mapToBean(Voucher.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public List<Voucher> findAll() {
        return get().withHandle(h ->
                h.createQuery("SELECT * FROM vouchers ORDER BY id DESC")
                        .mapToBean(Voucher.class)
                        .list()
        );
    }

    public void insert(Voucher voucher) {
        String sql = """
                INSERT INTO vouchers(
                    category_id, code, description,
                    discount_amount, discount_percentage,
                    start_date, end_date,
                    usage_limit, current_amount,
                    min_order_value, voucher_type
                ) VALUES (
                    :categoryId, :code, :description,
                    :discountAmount, :discountPercentage,
                    :startDate, :endDate,
                    :usageLimit, :currentAmount,
                    :minOrderValue, :voucherType
                )
                """;
        get().useHandle(h ->
                h.createUpdate(sql)
                        .bind("categoryId", voucher.getCategory_name())
                        .bind("code", voucher.getCode())
                        .bind("description", voucher.getDescription())
                        .bind("discountAmount", voucher.getDiscount_amount())
                        .bind("discountPercentage", voucher.getDiscount_percentage())
                        .bind("startDate", voucher.getStart_date())
                        .bind("endDate", voucher.getEnd_date())
                        .bind("usageLimit", voucher.getUsage_limit())
                        .bind("currentAmount", voucher.getCurrent_amount())
                        .bind("minOrderValue", voucher.getMin_order_value())
                        .bind("voucherType", voucher.getVoucher_type() != null ? voucher.getVoucher_type().name() : null)
                        .execute()
        );
    }

    public Voucher findById(int id) {
        return get().withHandle(h ->
                h.createQuery("SELECT * FROM vouchers WHERE id = :id")
                        .bind("id", id)
                        .mapToBean(Voucher.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public int deleteById(int id) {
        return get().withHandle(h ->
                h.createUpdate("DELETE FROM vouchers WHERE id = :id")
                        .bind("id", id)
                        .execute()
        );
    }

    public int update(Voucher voucher) {
        String sql = """
                UPDATE vouchers SET
                    category_id = :categoryId,
                    code = :code,
                    description = :description,
                    discount_amount = :discountAmount,
                    discount_percentage = :discountPercentage,
                    start_date = :startDate,
                    end_date = :endDate,
                    usage_limit = :usageLimit,
                    current_amount = :currentAmount,
                    min_order_value = :minOrderValue,
                    voucher_type = :voucherType
                WHERE id = :id
                """;
        return get().withHandle(h ->
                h.createUpdate(sql)
                        .bind("categoryId", voucher.getCategory_name())
                        .bind("code", voucher.getCode())
                        .bind("description", voucher.getDescription())
                        .bind("discountAmount", voucher.getDiscount_amount())
                        .bind("discountPercentage", voucher.getDiscount_percentage())
                        .bind("startDate", voucher.getStart_date())
                        .bind("endDate", voucher.getEnd_date())
                        .bind("usageLimit", voucher.getUsage_limit())
                        .bind("currentAmount", voucher.getCurrent_amount())
                        .bind("minOrderValue", voucher.getMin_order_value())
                        .bind("voucherType", voucher.getVoucher_type() != null ? voucher.getVoucher_type().name() : null)
                        .bind("id", voucher.getId())
                        .execute()
        );
    }
}
