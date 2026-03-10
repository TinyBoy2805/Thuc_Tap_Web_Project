package user.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class Voucher implements Serializable
{
    private int id;
    private String category_name;
    private String name;
    private String code;
    private String description;
    private double discount_amount;
    private double discount_percentage;
    private LocalDate start_date;
    private LocalDate end_date;
    private  int usage_limit;
    private  int current_amount;
    private double min_order_value;
    private VoucherType voucher_type;

    public Voucher() {}

    public Voucher(int id, String category_name, String name, String code, String description, double discount_amount, double discount_percentage, LocalDate start_date, LocalDate end_date, int usage_limit, int current_amount, double min_order_value, VoucherType voucher_type)
    {
        this.id = id;
        this.category_name = category_name;
        this.name = name;
        this.code = code;
        this.description = description;
        this.discount_amount = discount_amount;
        this.discount_percentage = discount_percentage;
        this.start_date = start_date;
        this.end_date = end_date;
        this.usage_limit = usage_limit;
        this.current_amount = current_amount;
        this.min_order_value = min_order_value;
        this.voucher_type = voucher_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(double discount_amount) {
        this.discount_amount = discount_amount;
    }

    public double getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(double discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public int getUsage_limit() {
        return usage_limit;
    }

    public void setUsage_limit(int usage_limit) {
        this.usage_limit = usage_limit;
    }

    public int getCurrent_amount() {
        return current_amount;
    }

    public void setCurrent_amount(int current_amount) {
        this.current_amount = current_amount;
    }

    public double getMin_order_value() {
        return min_order_value;
    }

    public void setMin_order_value(double min_order_value) {
        this.min_order_value = min_order_value;
    }

    public VoucherType getVoucher_type() {
        return voucher_type;
    }

    public void setVoucher_type(VoucherType voucher_type) {
        this.voucher_type = voucher_type;
    }
}