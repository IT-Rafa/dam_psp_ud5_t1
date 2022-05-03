/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.itrafa.dam_psp_ud5_t1;

/**
 * Clase estática con texto fijo de cabeceras HTTP
 * 
 * @author it-ra
 */
// Clase que intercambia mensajes para el protocolo HTTP
public class ResponseHTTPMsg {
    
    public static final String STARTLINE_OK = "HTTP/1.1 200 OK";
    public static final String STARTLINE_NOTFOUND = "HTTP/1.1 404 Not Found";
    public static final String HEADER_CONTENTTYPE = "Content-Type:text/html;charset=UTF-8";
    public static final String HEADER_CONTENTLENGHT  = "Content-Lenght: ";

}


