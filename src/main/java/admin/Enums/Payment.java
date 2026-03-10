package admin.Enums;

public enum Payment {
    PAID("Tiền mặt"), PENDING("Chờ nhận hàng"), FAILED("Ngân hàng");

    private String status;

    Payment(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
