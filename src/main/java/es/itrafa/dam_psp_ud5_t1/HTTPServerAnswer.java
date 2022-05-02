package es.itrafa.dam_psp_ud5_t1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * Responde a las peticiónes de cada cliente hechas a través del servidor http
 * 
 * @see HTTPServerAnswer
 * @author it-rafamartinez
 */
public class HTTPServerAnswer extends Thread {

    private static final Logger LOG;

    private final Socket socket;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        LOG = Logger.getLogger(HTTPServerAnswer.class.getName());
    }

    public HTTPServerAnswer(Socket client) {

        this.socket = client;
    }

    @Override
    public void run() {
        String peticion;
        long tiempoInicio = System.currentTimeMillis();
        long tiempoFin;

        try {
            //  String httpResponse = "HTTP/1.1 200 OK";
            // socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
            InputStreamReader insSR = new InputStreamReader(socket.getInputStream());
            BufferedReader bufLeer = new BufferedReader(insSR);

            PrintWriter print = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"), true);

            if ((peticion = bufLeer.readLine()) == null) {

                LOG.info(String.format("(%s) Petición http nula: %s",
                        HTTPServerAnswer.currentThread().getName(),
                        peticion));
            } else {
                if (peticion.startsWith("GET")) {
                    LOG.info(String.format("(%s) Petición http GET recibida: %s",
                            HTTPServerAnswer.currentThread().getName(),
                            peticion));

                    validGetAnswer(print, peticion, HTTPServerAnswer.currentThread().getName());

                } else {
                    LOG.info(String.format("(%s) Petición http No GET recibida: %s",
                            HTTPServerAnswer.currentThread().getName(),
                            peticion));

                }
                insSR.close();
                bufLeer.close();
                socket.close();
                tiempoFin = System.currentTimeMillis();
                LOG.info(String.format("(%s) Tiempo respuesta petición HTTP: %d ms; Cerrando Hilo",
                        HTTPServerAnswer.currentThread().getName(),
                        tiempoFin - tiempoInicio));
            }
        } catch (IOException ex) {
            LOG.severe(String.format("ERROR: %s", ex.getLocalizedMessage()));
        }

    }

    private void validGetAnswer(PrintWriter print, String peticion, String threadName) {
        String method = peticion.replaceAll(" ", "");
        String html;
        String initLine;

        initLine = Mensajes.INITIAL_LINE_OK;

        //extrae la subcadena entre GET y HTTP
        method = method.substring(3, method.lastIndexOf("HTTP"));

        if (method.length() == 0) {
            method = "/";
        }
        switch (method) {
            case "/":
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
                String time = dtf.format(LocalDateTime.now());

                html
                        = "<! DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "   <meta charset=\"utf-8\">"
                        + "   <title>Inicio</title>"
                        + "</head>"
                        + "<body>"
                        + "  <h2>Bienvenido: Son las " + time + "</h2>"
                        + "  <p><a href=\"/\">Inicio</a></p>"
                        + "  <p><a href=\"/info\">Info portal</a></p>"
                        + "  <p><a href=\"/listado\">Listado</a></p>"
                        + "</body></html>";

                SendMail sendMail = new SendMail(
                        "AVISO dam_psp_ud5_1",
                        "Se accedió a la página de inicio del servidor http dam_psp_ud5_t1 a las "
                        + time, threadName);
                sendMail.start();
                LOG.info(String.format("(%s) Hilo envío email iniciado en %s",
                        HTTPServerAnswer.currentThread().getName(), sendMail.getName()));

                break;
            case "/info":
                html
                        = "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "   <meta charset=\"utf-8\">"
                        + "   <title>Info</title>"
                        + "</head>"
                        + "<body>"
                        + "   <h2>Bienvenido al servicio de réplicas de RedIRIS</h2>"
                        + "<p>El servicio de réplicas de RedIRIS ofrece un conjunto de copias"
                        + " de repositorios de distintos sitios de interés para la comunidad "
                        + "académica y de investigación. Está disponible vía Web, FTP y rsync."
                        + "<p>Si desea contactar con nosotros para sugerir una réplica que "
                        + "piense que deba ser replicada, puede contactar con nosotros, "
                        + "indicándonos detalles de la misma, así como de la organización a "
                        + "la que pertenece."
                        + "<p>Ir a <a href=\"/\">Inicio</a></p>"
                        + "</body>" + "</html>";
                break;

            case "/listado":
                ReadFTP ftp = new ReadFTP();
                DataFolder listFiles = ftp.getFilesList();

                html
                        = "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "   <meta charset=\"utf-8\">"
                        + "   <title>Listado</title>"
                        + "<style>"
                        + "table, th, td {border:1px solid black;}"
                        + "</style>"
                        + "</head>"
                        + "<body>"
                        + "   <h2>Listado archivos en " + listFiles.getPath() + "</h2>";
                html
                        += "<table style=\"width:100%\">"
                        + "<tr>"
                        + "<th>Nombre</th>"
                        + "<th>Tipo</th>"
                        + "<th>Tamaño</th>"
                        + "</tr>";

                for (DataFile file : listFiles.getFiles()) {
                    html
                            += "<tr>"
                            + "<td>" + file.getName() + "</td>"
                            + "<td>" + file.getType() + "</td>"
                            + "<td>" + file.getSize() + "</td>"
                            + "</tr>";

                }

                html
                        += "</table>"
                        + "<p>Ir a <a href=\"/\">Inicio</a></p>"
                        + "</body>" + "</html>";
                break;
            default:
                html
                        = "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "<meta charset=\"utf-8\">"
                        + "<title>Página no encontrada</title>"
                        + "</head>"
                        + "<body>"
                        + "<p>ERROR: Página no encontrada</p>"
                        + "<p>Ir a <a href=\"/\">Inicio</a></p>"
                        + "</body>"
                        + "</html>";
                print.println(Mensajes.INITIAL_LINE_NotFound);
                break;
        }

        print.println(initLine);
        print.println(Mensajes.FIRSTHEADER);
        print.println("Content-Lenght: " + html.length() + 1);
        print.println("\n");
        print.println(html);

    }
}
