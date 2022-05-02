package es.itrafa.dam_psp_ud5_t1;

import java.util.ArrayList;

/**
 * Almacena el contenido de una  carpeta
 * 
 * @author it-ra
 */

public class DataFolder {

    private String path;
    private ArrayList<DataFile> files;

    public DataFolder() {
        this.files = new ArrayList<>();
    }


    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the files
     */
    public ArrayList<DataFile> getFiles() {
        return files;
    }

    /**
     * @param files the files to set
     */
    public void setFiles(ArrayList<DataFile> files) {
        this.files = files;
    }

}
