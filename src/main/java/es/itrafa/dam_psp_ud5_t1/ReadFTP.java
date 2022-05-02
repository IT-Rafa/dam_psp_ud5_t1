package es.itrafa.dam_psp_ud5_t1;

import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author it-ra
 */
public class ReadFTP {

    private final FTPClient client = new FTPClient();
    private final String sFTP = "ftp.rediris.es";
    private final String sUser = "anonymous";
    private final String sPassword = "";

    protected String getFiles() {
        String listFiles = "";
        try {
            client.connect(sFTP);
            boolean login = client.login(sUser, sPassword);
            if (login) {
                System.out.println("Usuario correcto");
            } else {
                System.out.println("Usuario incorrecto");
                client.disconnect();
                System.exit(1);
            }

            System.out.println("El directorio actual es:" + client.printWorkingDirectory());
            FTPFile[] files = client.listFiles();

            //array para visualizar tipos de ficheros
            String[] tipos = {"Fichero", "Directorio", "Enlace simbólico"};

            for (FTPFile file : files) {
                String details = file.getName();
                if (file.isDirectory()) {
                    details = "[" + details + "]";
                }
                details += "\t\t tamaño:" + file.getSize();
                details += "\t\t tipo:" + tipos[file.getType()];
                System.out.println(details);
            }
            boolean logout = client.logout();
            if (logout) {
                System.out.println("Desconectando...");
            } else {
                System.out.println("Error al hacer logout...");
            }
            client.disconnect();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return listFiles;
    }

}
