package es.itrafa.dam_psp_ud5_t1;

import java.util.Properties;
import java.util.logging.Logger;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 
 * Prepara datos para enviar e-mail
 * Los datos del emisor son de correo de dominio propio.
 * Solo lo uso para pruebas.
 * 
 * @author it-ra
 */
public class SendMail extends Thread {

    // PROPERTIES
    private final String subjectText;
    private final String messageTxt;
    private final String threadName;

    // Formato para logs
    private static final Logger LOG;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        LOG = Logger.getLogger(SendMail.class.getName());
    }

    /**
     * Constructor
     *
     * @param subjectText Concepto e-mail
     * @param messageTxt Contenido mensaje
     * @param threadName Nombre hilo desde donde se hizo la petición
     */
    SendMail(String subjectText, String messageTxt, String threadName) {
        this.subjectText = subjectText;
        this.messageTxt = messageTxt;
        this.threadName = threadName;
    }

    /**
     * Encargado de enviar el e-mail por smtp. Tanto los datos del emisor como
     * del receptor está incluidos
     * 

     */
    @Override
    public void run() {
        
        
        
        // Emisor/Nombre cuenta
        String to = "it-rafa@bit-acora.com";
        // Emisor/Contraseña
        String pw = "K_taP[A2S@#y";
        // Emisor correo saliente smtp
        String host = "smtp.dondominio.com";

        // Receptor/Nombre cuenta
        String from = "it-rafa@bit-acora.com";

        // Captura las propiedades del sistema
        Properties properties = System.getProperties();

        // Añade propiedades al sistema guardado
        // Servidor smtp
        properties.put("mail.smtp.host", host);
        // Puerto servidor smtp
        properties.put("mail.smtp.port", "465");
        // Usar seguridad ssl
        properties.put("mail.smtp.ssl.enable", "true");
        // Usar método autentificación smtp
        properties.put("mail.smtp.auth", "true");

        // Establece sesión con servidor de correo usando los datos configurados
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            // intenta acceso al servidor smtp del emisor (usando nombre cuenta y contraseña)
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pw);
            }
        });

        // Muestra info comunicación con servidor correo
        //session.setDebug(true);
        try {
            // Preparamos mensaje
            
            // Prepara el mensaje solo con datos conexión
            MimeMessage message = new MimeMessage(session);

            // Cabecera from (Emisor)
            message.setFrom(new InternetAddress(from));

            // Cabecera To (Correo Receptor)
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Cabecera subject (Info del e-mail)
            message.setSubject(subjectText, "UTF-8");

            // Cabecera text (Contenido del e-mail)
            message.setText(messageTxt, "UTF-8");
            
            // Send message
            Transport.send(message);

            LOG.info(String.format("(%s) Enviado aviso e-mail a %s",
                    HTTPServerAnswer.currentThread().getName(),
                    to));

        } catch (AuthenticationFailedException ex) {
            LOG.severe(String.format("(%s lanzado desde hilo en HTTPServerAnswer (%s) ERROR Envío aviso e-mail a %s; Fallo Autentificación",
                    HTTPServerAnswer.currentThread().getName(), threadName,
                    to, ex.getLocalizedMessage()));

        } catch (MessagingException ex) {
            LOG.severe(String.format("(%s lanzado desde hilo en HTTPServerAnswer (%s) ERROR Envío aviso e-mail a %s; %s",
                    HTTPServerAnswer.currentThread().getName(), threadName,
                    to, ex.getLocalizedMessage()));

        }
    }
}
