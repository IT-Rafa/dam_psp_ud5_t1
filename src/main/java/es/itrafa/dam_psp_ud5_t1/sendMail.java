package es.itrafa.dam_psp_ud5_t1;

import java.util.Properties;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail extends Thread {

    private static final Logger LOG;

    private final String subjectText;
    private final String messageTxt;
private final String threadName;
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        LOG = Logger.getLogger(SendMail.class.getName());
    }

    SendMail(String subjectText, String messageTxt, String threadName) {
        this.subjectText = subjectText;
        this.messageTxt = messageTxt;
        this.threadName = threadName;
    }

    @Override
    public void run() {
        // Recipient's email ID needs to be mentioned.
        String to = "it-rafa@bit-acora.com";

        // Sender's email ID needs to be mentioned
        String from = "it-rafa@bit-acora.com";
        // Para enviar desde GMAIL, si tienes seguridad en 2 pasos, se necesita una
        // "contraseña para aplicación" creada en la página de configuración 
        // de la cuenta. En seguridad
        String pw = "K_taP[A2S@#y";
        // Assuming you are sending email from through gmails smtp
        String host = "smtp.dondominio.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pw);
            }
        });

        // Used to debug SMTP issues
        //session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subjectText);

            // Now set the actual message
            message.setText(messageTxt);
            // Send message
            Transport.send(message);

            LOG.info(String.format("(%s) Enviado aviso e-mail a %s",
                    HTTPServerAnswer.currentThread().getName(),
                    to));

        } catch (MessagingException ex) {
            LOG.severe(String.format("(%s lanzado desde hilo en HTTPServerAnswer (%s) ERROR Envío aviso e-mail a %s; %s",
                    HTTPServerAnswer.currentThread().getName(), threadName,
                    to, ex.getLocalizedMessage()));

        }
    }
}
