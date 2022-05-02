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
 *
 * @author Juan Morillo Fernandez
 */
public class HTTPServerAnswer extends Thread {

    private static final Logger LOG = Logger.getLogger(HTTPServerAnswer.class.getName());

    private final Socket socket;

    public HTTPServerAnswer(Socket client) {

        this.socket = client;
    }

    public void run() {
        String peticion;

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
                    //extrae la subcadena entre GET y HTTP
                    peticion = peticion.replaceAll(" ", "");
                    peticion = peticion.substring(3, peticion.lastIndexOf("HTTP"));
                    validGetAnswer(print, peticion);

                } else {
                    LOG.info(String.format("(%s) etición http No GET recibida: %s",
                            HTTPServerAnswer.currentThread().getName(),
                            peticion));

                }
                insSR.close();
                bufLeer.close();
                socket.close();
                LOG.info(String.format("(%s) Hilo cerrado",
                        HTTPServerAnswer.currentThread().getName()));
            }
        } catch (IOException ex) {
            LOG.severe(String.format("ERROR: %s", ex.getLocalizedMessage()));
        }

    }

    private void validGetAnswer(PrintWriter print, String peticion) {
        String html;
        String initLine;

        initLine = Mensajes.lineaInicial_OK;

        if (peticion.length() == 0) {
            peticion = "/";
        }
        switch (peticion) {
            case "/":
                html = Paginas.html_index;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
                String time = dtf.format(LocalDateTime.now());

                SendMail sendMail = new SendMail(
                        "AVISO dam_psp_ud5_1",
                        "Se accedio a la página de inicio del servidor http dam_psp_ud5_t1 a las "
                        + time);
                sendMail.start();
                break;
            case "/info":
                html = Paginas.html_info;
                break;
            case "/listado":
                ReadFTP ftp = new ReadFTP();
                String listFiles = ftp.getFiles();
                html
                        = "<html>"
                        + "<head>"
                        + "   <meta charset=\"utf-8\">"
                        + "   <title>Listado</title>"
                        + "</head>"
                        + "<body>"
                        + "   <h2>listado pendiente</h2>"
                        + listFiles
                        + "<p>Ir a <a href=\"/\">Inicio</a></p>"
                        + "</body>" + "</html>";
                break;
            default:
                initLine = Mensajes.lineaInicial_NotFound;
                html = Paginas.hrml_noEncontrado;
                print.println(Mensajes.lineaInicial_NotFound);
                break;
        }

        print.println(initLine);
        print.println(Paginas.primeraCabecera);
        print.println("Content-Lenght: " + html.length() + 1);
        print.println("\n");
        print.println(html);

    }
}
