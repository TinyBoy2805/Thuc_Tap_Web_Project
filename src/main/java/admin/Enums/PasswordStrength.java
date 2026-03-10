package admin.Enums;

public enum PasswordStrength
{
    REQUIRED("Yêu cầu mật khẩu"),
    TOO_SHORT("Độ dài tối thiểu là 10 kí tự"),
    NO_DIGIT("Mật khẩu phải bao gồm số"),
    NO_SPECIAL_CHAR("Mật khẩu phải bao gồm kí tự đặc biệt"),
    STRONG(null); // null = không có lỗi

    private final String message;

    PasswordStrength(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public boolean isWeak()
    {
        return this != STRONG;
    }
}
