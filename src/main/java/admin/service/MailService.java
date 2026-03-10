package admin.service;

import com.sun.mail.smtp.SMTPAddressSucceededException;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Properties;
import java.util.UUID;
import java.sql.Timestamp;

public class MailService
{
    private static final String username = "michishop2025@gmail.com"; //the email that send otp to user
    private static final String password = "xlqrduewarveryej"; //app password for send email with smtp


    public MailService()
    {

    }

    public String createVerifyLink(HttpServletRequest request)
    {
        String baseUrl = request.getScheme() + "://" +
                request.getServerName() + ":" +
                request.getServerPort() +
                request.getContextPath();

        return baseUrl + "/auth/verify?token=";
    }

    public String createVerifyForgotPassLink(HttpServletRequest request)
    {
        String baseUrl = request.getScheme() + "://" +
                request.getServerName() + ":" +
                request.getServerPort() +
                request.getContextPath();

        return baseUrl + "/auth/change-pass?token=";
    }

    public String createVerifyToken()
    {
        return UUID.randomUUID().toString();
    }

    public Timestamp createExpirationTime(int minutes)
    {
        return new Timestamp(System.currentTimeMillis() + minutes * 60 * 1000);
    }

    public boolean sendVerifyLink(String to, String name, String link, int minutes)
    {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.sendpartial", "false"); // Error handling if the recipient does not exist.
        props.put("mail.smtp.reportsuccess", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");


        Session session = Session.getInstance(props,
                new Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Xác minh tài khoản của bạn");

            //content of the email
            String html = """
                    <div style="font-family: Arial, sans-serif; padding: 20px; background-color: #f4f4f4; margin: 0;">
                        <div style="max-width: 500px; margin: 0 auto; background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                            <h2 style="color: #F564A9; text-align: center; margin-top: 0; margin-bottom: 20px; font-size: 28px;">MiChiShop</h2>
                            <p style="color: #333333; font-size: 16px; line-height: 1.6; margin-bottom: 15px;">Xin chào <strong style="color: #F564A9;">%s</strong>,</p>
                            <p style="color: #333333; font-size: 16px; line-height: 1.6; margin-bottom: 15px;">Cảm ơn bạn đã đăng ký tài khoản tại MiChiShop! 🎉</p>
                            <p style="color: #333333; font-size: 16px; line-height: 1.6; margin-bottom: 20px;">Vui lòng click vào nút bên dưới để xác thực tài khoản:</p>
                            <div style="text-align: center; margin: 30px 0;">
                                <a href="%s"
                                   style="display: inline-block; background-color: #F564A9; color: #ffffff;
                                          padding: 14px 35px; text-decoration: none; border-radius: 6px;
                                          font-weight: bold; font-size: 16px;">
                                    Xác thực tài khoản
                                </a>
                            </div>
                            <p style="color: #ff0000; text-align: center; margin-top: 20px; margin-bottom: 20px; font-size: 14px; font-weight: bold;">⚠️ Liên kết có hiệu lực trong vòng %s phút</p>
                            <p style="color: #666666; font-size: 14px; line-height: 1.6; margin-bottom: 10px;">Nếu không phải bạn yêu cầu, hãy bỏ qua email này.</p>
                            <hr style="border: none; border-top: 1px solid #eeeeee; margin: 20px 0;">
                            <p style="color: #999999; font-size: 12px; text-align: center; margin-bottom: 0;">© 2025 MiChiShop - Bé khỏe, mẹ vui, cả nhà hạnh phúc</p>
                        </div>
                    </div>
                    """;
            String emailContent = String.format(html, name, link, minutes);
            message.setContent(emailContent, "text/html; charset=UTF-8");
            Transport.send(message);
            System.out.println("Email sent successfully to: " + to);
            return true;

        } catch (SendFailedException e)
        {
            System.err.println("SendFailedException caught: " + e.getMessage());

            //check if email was actually sent successfully
            //if validSentAddresses exists, email was sent despite the exception
            Address[] validSent = e.getValidSentAddresses();
            if (validSent != null && validSent.length > 0)
            {
                System.out.println("Email actually sent successfully despite exception!");
                return true;
            }

            //check for nested SMTPAddressSucceededException
            Throwable cause = e.getCause();
            if (cause instanceof SMTPAddressSucceededException)
            {
                System.out.println("Email sent successfully (detected via nested exception)");
                return true;
            }

            //check for invalid recipients (email doesn't exist)
            Address[] invalidAddresses = e.getInvalidAddresses();
            if (invalidAddresses != null && invalidAddresses.length > 0)
            {
                System.err.println("Invalid addresses detected");
                return false;
            }

            //check error message for specific SMTP codes
            String errorMsg = e.getMessage();
            if (errorMsg != null)
            {
                // 250 = Success code from SMTP
                if (errorMsg.contains("250 2.0.0 OK") || errorMsg.contains("250 2.1.5 OK"))
                {
                    System.out.println("Email sent successfully (detected via 250 code)");
                    return true;
                }

                // Mailbox not found
                if (errorMsg.contains("550") || errorMsg.contains("5.1.1"))
                {
                    return false;
                }

                // Rejected/blocked
                if (errorMsg.contains("5.7.1") || errorMsg.contains("554"))
                {
                    return false;
                }
            }

            e.printStackTrace();
            return false;

        } catch (MessagingException e)
        {
            System.err.println("MessagingException: " + e.getMessage());
            e.printStackTrace();

            if (e.getMessage() != null && e.getMessage().contains("535"))
            {
                System.err.println("Authentication failed! Please check username/password");
            }

            return false;

        } catch (Exception e)
        {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

//    public static boolean sendEmail(String to, String subject, String content) {
//        final String username = "23130041@st.hcmuaf.edu.vn";
//        final String password = "uvdlltzktezrbyog";
//
//        Properties props = new Properties();
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//
//        Session session = Session.getInstance(props,
//                new Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(username, password);
//                    }
//                });
//        try {
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(username));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
//            message.setSubject(subject);
//            message.setContent(content, "text/html; charset=UTF-8");
//            Transport.send(message);
//            System.out.println("Email sent successfully to: " + to);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    public boolean sendVerifyPasswordResetLink(String to, String name, String link, int minutes)
    {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.sendpartial", "false");
        props.put("mail.smtp.reportsuccess", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("MiChiShop - Yêu cầu thay đổi mật khẩu");

            String html = """
                    <div style="font-family: Arial, sans-serif; padding: 20px; background-color: #f4f4f4; margin: 0;">
                        <div style="max-width: 500px; margin: 0 auto; background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                            <h2 style="color: #F564A9; text-align: center; margin-top: 0; margin-bottom: 20px; font-size: 28px;">MiChiShop</h2>
                            <p style="color: #333333; font-size: 16px; line-height: 1.6; margin-bottom: 15px;">Xin chào <strong style="color: #F564A9;">%s</strong>,</p>
                            <p style="color: #333333; font-size: 16px; line-height: 1.6; margin-bottom: 15px;">Đây là yêu cầu thay đổi mật khẩu! 🔒</p>
                            <p style="color: #333333; font-size: 16px; line-height: 1.6; margin-bottom: 20px;">Vui lòng click vào nút bên dưới để thay đổi mật khẩu:</p>
                            <div style="text-align: center; margin: 30px 0;">
                                <a href="%s"
                                   style="display: inline-block; background-color: #F564A9; color: #ffffff;
                                          padding: 14px 35px; text-decoration: none; border-radius: 6px;
                                          font-weight: bold; font-size: 16px;">
                                    Đổi mật khẩu!
                                </a>
                            </div>
                            <p style="color: #ff0000; text-align: center; margin-top: 20px; margin-bottom: 20px; font-size: 14px; font-weight: bold;">⚠️ Liên kết có hiệu lực trong vòng %s phút</p>
                            <p style="color: #666666; font-size: 14px; line-height: 1.6; margin-bottom: 10px;">Nếu không phải bạn yêu cầu, hãy bỏ qua email này.</p>
                            <hr style="border: none; border-top: 1px solid #eeeeee; margin: 20px 0;">
                            <p style="color: #999999; font-size: 12px; text-align: center; margin-bottom: 0;">© 2025 MiChiShop - Bé khỏe, mẹ vui, cả nhà hạnh phúc</p>
                        </div>
                    </div>
                    """;
            String emailContent = String.format(html, name, link, minutes);

            message.setContent(emailContent, "text/html; charset=UTF-8");
            Transport.send(message);
            System.out.println("Email sent successfully to: " + to);
            return true;
        } catch (Exception e) {
            // Nếu là SMTPAddressSucceededException thì vẫn coi là gửi thành công
            Throwable cause = e.getCause();
            if (cause instanceof com.sun.mail.smtp.SMTPAddressSucceededException) {
                System.out.println("Email sent successfully (detected via SMTPAddressSucceededException)");
                return true;
            }
            // Nếu message chứa mã 250 thì cũng coi là gửi thành công
            String errorMsg = e.getMessage();
            if (errorMsg != null && (errorMsg.contains("250 2.0.0 OK") || errorMsg.contains("250 2.1.5 OK"))) {
                System.out.println("Email sent successfully (detected via 250 code)");
                return true;
            }
            System.err.println("Send email failed: " + e.getMessage());
            return false;
        }
    }

}
