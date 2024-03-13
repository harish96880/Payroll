import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    private final String username;
    private final String password;
    private final String smtpHost;
    private final int port;

    public EmailSender(String username, String password, String smtpHost, int port) {
        this.username = username;
        this.password = password;
        this.smtpHost = smtpHost;
        this.port = port;
    }

    public Session getSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", port);
        return Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void sendEmail(String toEmail, String subject, MimeMessage message) throws MessagingException {
        Transport transport = null;
        try {
            Session session = getSession();
            transport = session.getTransport("smtp");
            transport.connect(smtpHost, port, username, password);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            // Send the message
            transport.sendMessage(message, message.getAllRecipients());
        } finally {
            if (transport != null) {
                transport.close();
            }
        }
    }
}
