package es.itrafa.dam_psp_ud5_t1;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author it-ra
 */
public class ReadFTP {

    private static final Logger LOG;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        LOG = Logger.getLogger(ReadFTP.class.getName());
    }

    public DataFolder getFilesList() {
        DataFolder ftpListFiles = new DataFolder();
        FTPClient client = new FTPClient();

        String sFTP = "ftp.rediris.es";
        String sUser = "anonymous";
        String sPassword = "";

        try {
            client.connect(sFTP);
            boolean login = client.login(sUser, sPassword);
            if (login) {
                LOG.info(String.format("Acceso a %s concedido. Recogiendo lista archivos", sFTP));
                client.changeWorkingDirectory("debian");
                FTPFile[] files = client.listFiles();

                DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                ftpListFiles.setPath(client.printWorkingDirectory());

                for (FTPFile ftpfile : files) {
                    DataFile file = new DataFile();
                    file.setName(ftpfile.getName());

                    if (ftpfile.isDirectory()) {
                        file.setType("Carpeta");
                    } else if (ftpfile.isFile()) {
                        file.setType("Fichero");
                    } else if (ftpfile.isSymbolicLink()) {
                        file.setType("Enlace");
                    } else { // isUnknown
                        file.setType("Desconocido");
                    }

                    file.setSize(String.format("%d", ftpfile.getSize()));
                    ftpListFiles.getFiles().add(file);
                }
                Collections.sort(ftpListFiles.getFiles());
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
