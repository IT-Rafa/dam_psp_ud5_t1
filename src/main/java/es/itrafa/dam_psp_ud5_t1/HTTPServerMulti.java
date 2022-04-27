package es.itrafa.dam_psp_ud5_t1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author it-rafamartinez
 */
public class HTTPServerMulti {
    
    
    public static void main (String[] args) throws IOException{
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
