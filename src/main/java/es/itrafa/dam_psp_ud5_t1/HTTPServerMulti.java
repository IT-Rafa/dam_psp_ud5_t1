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

    private static final Logger LOG = Logger.getLogger(HTTPServerMulti.class.getName());
    private static final int port = 8090;

    public static void main(String[] args) throws IOException {

        try {
            ServerSocket socServidor = new ServerSocket(8090);

            LOG.info(String.format("Servidor HTTP activo en puerto %d", port));

            HTTPServerAnswer hilo;
            Socket socCliente;

            while (true) {
                socCliente = socServidor.accept();
                hilo = new HTTPServerAnswer(socCliente);
                hilo.start();
            }
        } catch (java.net.BindException ex) {
            LOG.severe(String.format("Servidor HTTP no puede iniciarse. Puerto %d está en uso. Servidor ya activo", port));

        }
    }
}