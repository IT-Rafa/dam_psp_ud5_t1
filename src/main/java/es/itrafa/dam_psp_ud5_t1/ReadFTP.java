package es.itrafa.dam_psp_ud5_t1;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author it-ra
 */
public class ReadFTP {

    private static final Logger LOG = Logger.getLogger(ReadFTP.class.getName());

    public String read() {
        String ftpListFiles = null;
        FTPClient client = new FTPClient();
        String sFTP = "ftp.rediris.es";
        String sUser = "anonymous";
        String sPassword = "";

        try {
            client.connect(sFTP);
            boolean login = client.login(sUser, sPassword);
            if (login) {
                LOG.info(String.format("Acceso a %s concedido", sFTP));
                FTPFile[] files = client.listFiles();

// iterates over the files and prints details for each
                DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                for (FTPFile file : files) {
                    ftpListFiles += file.getName();
                    if (file.isDirectory()) {
                        ftpListFiles = "[" + ftpListFiles + "]";
                    }
                    ftpListFiles += "\t\t" + file.getSize();
                    ftpListFiles += "\t\t" + dateFormater.format(file.getTimestamp().getTime());

                }

            } else {
                LOG.warning(String.format("Acceso a %s no válido", sFTP));
            }

            boolean logout = client.logout();
            if (logout) {
                LOG.warning(String.format("Cerrando conexión FTP con %s", sFTP));
            } else {
                LOG.warning(String.format("Conexión FTP con %s no se finalizo correctamente", sFTP));

            }
            client.disconnect();

            /*
            Más funcionalidades
            
            Renombrar fichero:
            if(client.rename("antiguo","nuevo"))
            
            Eliminar fichero:
            if(client.deleteFile("nombre fichero"))
            
            
             */
        } catch (IOException ex) {
            LOG.severe(String.format("ERROR acceso FTP", ex.getLocalizedMessage()));
        }
        return ftpListFiles;

    }
}
