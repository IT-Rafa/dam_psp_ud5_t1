package es.itrafa.dam_psp_ud5_t1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juan Morillo Fernandez
 */
public class HiloDespachador extends Thread {

    private static final Logger LOG = Logger.getLogger(HiloDespachador.class.getName());

    private final Socket socket;

    public HiloDespachador(Socket client) {

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
                LOG.log(Level.INFO, "({0}) Petición recibida nula",
                        HiloDespachador.currentThread().getName()
                );
            } else {
                LOG.log(Level.INFO, "({0}) Petición http recibida: {1}",
                        new Object[]{HiloDespachador.currentThread().getName(),
                            peticion}
                );

                if (peticion.startsWith("GET")) {

                    //extrae la subcadena entre GET y HTTP
                    peticion = peticion.replaceAll(" ", "");
                    peticion = peticion.substring(3, peticion.lastIndexOf("HTTP"));
                    validGetAnswer(print, peticion);

                }
                insSR.close();
                bufLeer.close();
                socket.close();

            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "({0}) ERROR: ",
                    ex.getLocalizedMessage()
            );
        }

    }

    private void validGetAnswer(PrintWriter print, String peticion) {
        String html;
        String initLine;
        String firstHeader;
        String pageAsked;

        if (peticion.length() == 0
                || peticion.equals("/") || peticion.equals("/info")
                || peticion.equals("/listado")) {

            initLine = Mensajes.lineaInicial_OK;
            firstHeader = Paginas.primeraCabecera;

            if (peticion.equals("/")) {
                html = Paginas.html_index;
                pageAsked = "inicio";

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
                String time = dtf.format(LocalDateTime.now());

                Mail.send(
                        "AVISO dam_psp_ud5_1",
                        "Se accedio a la página de inicio del servidor http dam_psp_ud5_t1 a las "
                        + time);

            } else if (peticion.equals("/info")) {
                html = Paginas.html_info;
                pageAsked = "info";

            } else if (peticion.equals("/listado")) {
                html = Paginas.html_listado;
                pageAsked = "listado";

            } else {
                initLine = Mensajes.lineaInicial_NotFound;
                firstHeader;

                html = Paginas.hrml_noEncontrado;
                print.println(Mensajes.lineaInicial_NotFound);

            }
        } else {
            html = Paginas.hrml_noEncontrado;
            print.println(Mensajes.lineaInicial_NotFound);

        }
        print.println(Mensajes.lineaInicial_OK);
        print.println(Paginas.primeraCabecera);
        print.println("Content-Lenght: " + html.length() + 1);
        print.println("\n");
        print.println(html);

    }
}
