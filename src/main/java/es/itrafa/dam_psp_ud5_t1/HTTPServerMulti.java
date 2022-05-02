package es.itrafa.dam_psp_ud5_t1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 *
 * @author it-rafamartinez
 */
public class HTTPServerMulti {

    private static final Logger LOG;
    private static final int PORT = 8090;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$s] [%3$s] %n  %5$s %n");
        LOG = Logger.getLogger(HTTPServerMulti.class.getName());
    }

    public static void main(String[] args) throws IOException {

        try {
            ServerSocket socServidor = new ServerSocket(PORT);

            LOG.info(String.format("Servidor HTTP activo en puerto %d", PORT));

            HTTPServerAnswer hilo;
            Socket socCliente;

            while (true) {
                socCliente = socServidor.accept();
                hilo = new HTTPServerAnswer(socCliente);
                hilo.start();
            }
        } catch (java.net.BindException ex) {
            LOG.severe(String.format("Servidor HTTP no puede iniciarse; %s %d %s",
                    "El puerto", PORT, "está siendo usado."));

        }
    }
}
