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

    public ArrayList<String> getFilesList() {
        ArrayList<String> ftpListFiles = new ArrayList<>();
        FTPClient client = new FTPClient();
        String sFTP = "ftp.rediris.es";
        String sUser = "anonymous";
        String sPassword = "";

        try {
            client.connect(sFTP);
            boolean login = client.login(sUser, sPassword);
            if (login) {
                LOG.info(String.format("Acceso a %s concedido. Recogiendo lista archivos", sFTP));
                FTPFile[] files = client.listFiles();

// iterates over the files and prints details for each
                DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String[] tipos = {"Fichero", "Carpeta", "Enlace simbólico"};

                for (FTPFile file : files) {
                    String name = file.getName();
                    if (file.isDirectory()) {
                        name = "[" + name + "]";
                    }
                    name += "\t\t tamaño:" + file.getSize();
                    name += "\t\t tipo:" + tipos[file.getType()];
                    ftpListFiles.add(name);
                }
                Collections.sort(ftpListFiles);
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
