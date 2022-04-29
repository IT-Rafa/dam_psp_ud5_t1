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

    public static void main(String[] args) throws IOException {
        LOG.info("Inicio programa");
        ServerSocket socServidor = new ServerSocket(8090);
        HiloDespachador hilo;
        Socket socCliente;

        while (true) {
            socCliente = socServidor.accept();
            hilo = new HiloDespachador(socCliente);
            hilo.start();
        }
    }
}
