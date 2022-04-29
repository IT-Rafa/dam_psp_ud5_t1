/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.itrafa.dam_psp_ud5_t1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juan Morillo Fernandez
 */
public class HiloDespachador extends Thread {

    private static final Logger LOG = Logger.getLogger(HiloDespachador.class.getName());

    private final Socket socket;

    public HiloDespachador(Socket client) {

        this.socket = client;
    }

    public void run() {
        String peticion;
        String html;

        try {
            //  String httpResponse = "HTTP/1.1 200 OK";
            // socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
            InputStreamReader insSR = new InputStreamReader(socket.getInputStream());
            BufferedReader bufLeer = new BufferedReader(insSR);

            PrintWriter print = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"), true);

            if ((peticion = bufLeer.readLine()) == null) {
                LOG.log(Level.INFO, "({0}) Petición recibida nula",
                        HiloDespachador.currentThread().getName()
                );
            } else {
                LOG.log(Level.INFO, "({0}) Petición http recibida: {1}",
                        new Object[]{HiloDespachador.currentThread().getName(),
                            peticion}
                );
                if (peticion.startsWith("GET")) {

                    //extrae la subcadena entre GET y HTTP
                    peticion = peticion.replaceAll(" ", "");
                    peticion = peticion.substring(3, peticion.lastIndexOf("HTTP"));

                    if (peticion.length() == 0 || peticion.equals("/")) {
                        html = Paginas.html_index;
                        print.println(Mensajes.lineaInicial_OK);
                        print.println(Paginas.primeraCabecera);
                        print.println("Content-Lenght: " + html.length() + 1);
                        print.println("\n");
                        print.println(html);

                    } else if (peticion.equals("/info")) {
                        html = Paginas.html_info;
                        print.println(Mensajes.lineaInicial_OK);
                        print.println(Paginas.primeraCabecera);
                        print.println("Content-Lenght: " + html.length() + 1);
                        print.println("\n");
                        print.println(html);

                    } else if (peticion.equals("/listado")) {
                        html = Paginas.html_listado;
                        print.println(Mensajes.lineaInicial_OK);
                        print.println(Paginas.primeraCabecera);
                        print.println("Content-Lenght: " + html.length() + 1);
                        print.println("\n");
                        print.println(html);

                    } else {
                        html = Paginas.hrml_noEncontrado;
                        print.println(Mensajes.lineaInicial_NotFound);
                        print.println(Paginas.primeraCabecera);
                        print.println("Content-Lenght: " + html.length() + 1);
                        print.println("\n");
                        print.println(html);
                    }
                }
                insSR.close();
                bufLeer.close();
                socket.close();
                
                Mail mail = new Mail();
                mail.send();
            }
        } catch (IOException ex) {
            System.out.println("IOException capturada");
        }

    }
}
