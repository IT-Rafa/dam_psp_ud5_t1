package es.itrafa.dam_psp_ud5_t1;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * Gestiona la lectura de los archivos en servidor FTP
 * 
 * @author it-ra
 */
public class ReadFTP {

    // Formato para logs
    private static final Logger LOG;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        LOG = Logger.getLogger(ReadFTP.class.getName());
    }
/**
 * lee los archivos en servidor FTP y los convierte en DataFolder.
 * Usado por HTTPServerAnswer para construir la página /listado
 * 
 * @return Archivos en ruta FTP (DEBIAN)
 */
    public DataFolder getFilesList() {
        // Preparamos contenedor para manejar todo como texto para el HTML
        // DataFolder almacena la ruta y los datos de cada archivo como DataFile
        DataFolder ftpListFiles = new DataFolder();
        
        //Preparamos conexión con servidor FTP
        FTPClient client = new FTPClient();
        
        // Datos de acceso
        String sFTP = "ftp.rediris.es";
        String sUser = "anonymous";
        String sPassword = "";

        try {
            // Conectamos al servidor FTP
            client.connect(sFTP);
            // Intentamos Acceso
            boolean login = client.login(sUser, sPassword);
            if (login) {
                // Si accedemos
                LOG.info(String.format("Acceso a %s concedido. Recogiendo lista archivos", sFTP));
                
                // Nos movemos a la ruta debian, desde la raiz
                client.changeWorkingDirectory("debian");
                
                // Capturamos los datos de los archivos en la ruta indicada
           
                FTPFile[] files = client.listFiles();
                
                //Guardamos la ruta en la que estamos
                ftpListFiles.setPath(client.printWorkingDirectory());

                // Guardamos y preparamos los datos de cada archivo en esa ruta
                for (FTPFile ftpfile : files) {
                    // Creamos contenedor para la info de cada archivo
                    DataFile file = new DataFile();
                    // Guardamos nombre fichero
                    file.setName(ftpfile.getName());

                    // Guardamos tipo
                    if (ftpfile.isDirectory()) {
                        file.setType("Carpeta");
                    } else if (ftpfile.isFile()) {
                        file.setType("Fichero");
                    } else if (ftpfile.isSymbolicLink()) {
                        file.setType("Enlace");
                    } else { // isUnknown
                        file.setType("Desconocido");
                    }
                    
                    // Guardamos tamaño (como string)
                    file.setSize(String.format("%d", ftpfile.getSize()));
                    
                    //Almacenamos dato del archivo en el los datos de la carpeta
                    ftpListFiles.getFiles().add(file);
                }
                // Hacemos que las carpetas aparezcan antes que los archivos
                // Ordenamos las carpetas por orden alfabetico del título (mejorable pero basta)
                // Carpeta, desconocido, enlace, fichero
                Collections.sort(ftpListFiles.getFiles());
                
            } else {
                // Falla acceso
                LOG.warning(String.format("Acceso a %s no válido", sFTP));
            }
            // Cerramos sesión
            boolean logout = client.logout();
            // Comprobamos fin sesión ok
            if (logout) {
                LOG.warning(String.format("Cerrando conexión FTP con %s", sFTP));
            } else {
                LOG.warning(String.format("Conexión FTP con %s no se finalizo correctamente", sFTP));

            }
            client.disconnect();

        } catch (IOException ex) {
            LOG.severe(String.format("ERROR acceso FTP", ex.getLocalizedMessage()));
        }
        return ftpListFiles;

    }
}
