package admin.model;

public class ValidateObject
{
    private String usernameError;
    private String emailError;
    private String phoneError;
    private String passwordError;
    private String confirmPasswordError;

    public String getUsernameError() { return usernameError; }
    public String getEmailError() { return emailError; }
    public String getPhoneError() { return phoneError; }
    public String getPasswordError() { return passwordError; }
    public String getConfirmPasswordError() { return confirmPasswordError; }

    public void setUsernameError(String usernameError) { this.usernameError = usernameError; }
    public void setEmailError(String emailError) { this.emailError = emailError; }
    public void setPhoneError(String phoneError) { this.phoneError = phoneError; }
    public void setPasswordError(String passwordError) { this.passwordError = passwordError; }
    public void setConfirmPasswordError(String confirmPasswordError) { this.confirmPasswordError = confirmPasswordError; }

    public boolean hasError()
    {
        return usernameError != null || emailError != null ||
                phoneError != null || passwordError != null ||
                confirmPasswordError != null;
    }

    public String getErrorMessage()
    {
        StringBuilder builder = new StringBuilder();
        if (usernameError != null) builder.append(usernameError).append("\n");
        if (emailError != null) builder.append(emailError).append("\n");
        if (phoneError != null) builder.append(phoneError).append("\n");
        if (passwordError != null) builder.append(passwordError).append("\n");
        if (confirmPasswordError != null) builder.append(confirmPasswordError).append("\n");
        return builder.toString().trim();
    }
}