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
 * Responde a las peticiónes de cada cliente, hechas a través del servidor http,
 * en hilos independientes
 *
 * @see HTTPServerAnswer
 * @author it-rafamartinez
 */
public class HTTPServerAnswer extends Thread {

    // PROPERTIES
    private final Socket socket;

    // Formato para logs
    private static final Logger LOG;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        LOG = Logger.getLogger(HTTPServerAnswer.class.getName());
    }

    // CONSTRUCTORS
    /**
     * Crea instancia usando el socket del cliente
     *
     * @param client socket usado para comunicarse con cliente
     */
    public HTTPServerAnswer(Socket client) {
        this.socket = client;
    }

    // METHODS
    /**
     * Hilo encargado de contestar al cliente. Controla tiempo respuesta. Si es
     * una petición HTTP GET para /, /info o /listado, devuelve página montada,
     * si no devuelve no encontrado
     *
     */
    @Override
    public void run() {

        // Almacena petición recibida
        String peticion;

        // Para control tiempo respuesta
        long tiempoInicio = System.currentTimeMillis();
        long tiempoFin;

        try {
            // Captura flujo de entrada del socket
            InputStreamReader insSR = new InputStreamReader(socket.getInputStream());
            // Adapta flujo entrada como buffer para lectura
            BufferedReader bufLeer = new BufferedReader(insSR);
            // Captura flujo de salida del socket y prepara  para escritura
            PrintWriter print = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"), true);

            // Lee petición cliente y actua según lo recibido
            if ((peticion = bufLeer.readLine()) == null) {
                LOG.info(String.format("(%s) Petición http nula: %s",
                        HTTPServerAnswer.currentThread().getName(),
                        peticion));
            } else {
                if (peticion.startsWith("GET")) {
                    LOG.info(String.format("(%s) Petición http GET recibida: %s",
                            HTTPServerAnswer.currentThread().getName(),
                            peticion));
                    // controla peticiones GET válidas
                    validGetAnswer(print, peticion, HTTPServerAnswer.currentThread().getName());

                } else {
                    LOG.info(String.format("(%s) Petición http No GET recibida: %s",
                            HTTPServerAnswer.currentThread().getName(),
                            peticion));

                }
                //Fin comunicación cliente. Cerramos todo
                insSR.close();
                bufLeer.close();
                socket.close();

                // Controlamos tiempo 
                tiempoFin = System.currentTimeMillis();
                LOG.info(String.format("(%s) Tiempo respuesta petición HTTP: %d ms; Cerrando Hilo",
                        HTTPServerAnswer.currentThread().getName(),
                        tiempoFin - tiempoInicio));
            }

        } catch (java.net.SocketException ex) {
            LOG.severe(String.format("ERROR: Servidor HTTP fallo comunicación cliente por socket; %s",
                    ex.getLocalizedMessage()));

        } catch (java.io.IOException ex) {
            LOG.severe(String.format("ERROR: Servidor HTTP fallo comunicación cliente por entrada/salida; %s",
                    ex.getLocalizedMessage()));
        }

    }

    /**
     * Controla las peticiones GET válidas. Construye cabecera respuesta y código HTML
     * 
     * @param print Salida flujo a cliente lista para escritura
     * @param peticion Petición del cliente
     * @param threadName Nombre del hilo de respuesta al cliente
     */
    private void validGetAnswer(PrintWriter print, String peticion, String threadName) {
        // Cabecera con estado de la respuesta
        String startline;

        // Contiene página web 
        String html;

        //limpia petición (creamos variable para no modificar parámetro)
        String pageAsked = peticion.replaceAll(" ", "");
        // Captura ruta en petición GET
        pageAsked = pageAsked.substring(3, pageAsked.lastIndexOf("HTTP"));

        // petición vacia envia a página inicio
        if (pageAsked.length() == 0) {
            pageAsked = "/";
        }

        // Según página pedida, se prepara el código html y cabecera HTML
        switch (pageAsked) {
            case "/":
                // código respuesta
                startline = ResponseHTTPMsg.STARTLINE_OK;
                // Preparamos hora y fecha del sistema
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
                String time = dtf.format(LocalDateTime.now());

                // Montamos página inicio (BIENVENIDA)
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

                // Enviamos email al administrador a través de hilo independiente
                // para no ralentizar el envío del html
                SendMail sendMail = new SendMail(
                        "AVISO dam_psp_ud5_1",
                        "Se accedió a la página de inicio del servidor http dam_psp_ud5_t1 a las "
                        + time, threadName);
                sendMail.start();

                LOG.info(String.format("(%s) Hilo envío email iniciado en %s",
                        HTTPServerAnswer.currentThread().getName(), sendMail.getName()));

                break;

            case "/info":
                // código respuesta
                startline = ResponseHTTPMsg.STARTLINE_OK;
                // Montamos página (INFORMACIÓN PORTAL)
                html
                        = "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "   <meta charset=\"utf-8\">"
                        + "   <title>Info</title>"
                        + "</head>"
                        + "<body>"
                        + "   <h2>Info del servidor creado para Tarea para PSP05</h2>"
                        + "<ul>"
                        + "<li><h3>inicio (/): Bienvenida, hora y enlaces a las opciones</h3></li>"
                        + "<li><h3>info (/info): Información de enlaces</h3></li>"
                        + "<li><h3>listado (/listado): Lista archivos en ftp.rediris.es/debian</h3></li>"
                        + "</ul>"
                        + "<p>Ir a <a href=\"/\">Inicio</a></p>"
                        + "</body>" + "</html>";
                break;

            case "/listado":
                // código respuesta
                startline = ResponseHTTPMsg.STARTLINE_OK;

                // Montamos página (LISTADO FICHEROS FTP)
                // Capturamos ficheros FTP con clase creada para contener los datos
                // de una carpeta
                ReadFTP ftp = new ReadFTP();
                DataFolder listFiles = ftp.getFilesList();

                // los datos se mostrarán en tabla. Añadimos CSS insertado en HTML
                // para dar formato a tabla
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
                        + "<body>";

                // Añadimos título tabla con la ruta relativa al servidor FTP
                html
                        += "<h2>Listado archivos en [" + listFiles.getPath() + "]</h2>";

                // Añadimos cabecera tabla
                html
                        += "<table style=\"width:100%\">"
                        + "<tr>"
                        + "<th>Nombre</th>"
                        + "<th>Tipo</th>"
                        + "<th>Tamaño</th>"
                        + "</tr>";
                // Añadimos datos de los archivos a tabla
                for (DataFile file : listFiles.getFiles()) {
                    html
                            += "<tr>"
                            + "<td>" + file.getName();

                    if (file.getType().equalsIgnoreCase("Carpeta")) {
                        html += "/";
                    }

                    html
                            += "</td>"
                            + "<td>" + file.getType() + "</td>"
                            + "<td>" + file.getSize() + "</td>"
                            + "</tr>";
                }
                // cerramos tabla
                html
                        += "</table>"
                        + "<p>Ir a <a href=\"/\">Inicio</a></p>"
                        + "</body>" + "</html>";
                break;
            default:
                startline = ResponseHTTPMsg.STARTLINE_NOTFOUND;
                // Montamos página para recurso no encontrado
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
                break;
        }
        // Enviamos respuesta con código html correspondiente
        print.println(startline);
        print.println(ResponseHTTPMsg.HEADER_CONTENTTYPE);
        print.println(ResponseHTTPMsg.HEADER_CONTENTLENGHT + html.length() + 1);
        print.println("\n");
        print.println(html);

    }
}
