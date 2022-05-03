package es.itrafa.dam_psp_ud5_t1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Inicia el servidor http para clientes múltiples
 *
 * @author it-rafamartinez
 */
public class HTTPServerMulti {

    // PROPERTIES
    private static final int PORT = 8090;

    // Formato para logs
    private static final Logger LOG;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$s] [%3$s] %n  %5$s %n");
        LOG = Logger.getLogger(HTTPServerMulti.class.getName());
    }

    // METHODS
    /**
     * Entrada programa. Arranca servidor en en puerto definido
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        try {
            // Prepara servidor en puerto
            ServerSocket socServidor = new ServerSocket(PORT);
            LOG.info(String.format("Servidor HTTP activo en puerto %d", PORT));

            // Preparamos hilo y socket para peticiones cliente
            HTTPServerAnswer hilo;
            Socket socCliente;

            // Repetimos salvo finalización forzada programa
            while (true) {
                // Nueva petición cliente
                socCliente = socServidor.accept();
                // Respondemos petición cliente en nuevo hilo
                hilo = new HTTPServerAnswer(socCliente);
                hilo.start();
            }

        } catch (java.net.BindException ex) {
            LOG.severe(String.format("ERROR: Servidor HTTP no puede iniciarse; %s %d %s",
                    "El puerto", PORT, "está siendo usado."));

        }catch (java.net.SocketException ex) {
            LOG.severe(String.format("ERROR: Servidor HTTP fallo con socket; %s",
                    ex.getLocalizedMessage()));
            
        } catch (java.io.IOException ex) {
            LOG.severe(String.format("ERROR: Servidor HTTP fallo entrada/salida; %s",
                    ex.getLocalizedMessage()));
        }
    }
}
