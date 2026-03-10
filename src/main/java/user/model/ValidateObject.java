package user.model;

public class ValidateObject
{
    private String usernameError;
    private String emailError;
    private String phoneError;
    private String passwordError;
    private String confirmPasswordError;

    // Getters
    public String getUsernameError() { return usernameError; }
    public String getEmailError() { return emailError; }
    public String getPhoneError() { return phoneError; }
    public String getPasswordError() { return passwordError; }
    public String getConfirmPasswordError() { return confirmPasswordError; }

    // Setters
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

        public String getErrorMessage() {
        StringBuilder sb = new StringBuilder();
        if (usernameError != null) sb.append(usernameError).append("\n");
        if (emailError != null) sb.append(emailError).append("\n");
        if (phoneError != null) sb.append(phoneError).append("\n");
        if (passwordError != null) sb.append(passwordError).append("\n");
        if (confirmPasswordError != null) sb.append(confirmPasswordError).append("\n");
        return sb.toString().trim();
    }
}
