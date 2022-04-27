package es.itrafa.dam_psp_ud5_t1;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class Paginas {

    public static final String primeraCabecera = "Content-Type:text/html;charset=UTF-8";
    
    public static final String html_index="<html><head>" +
            "<title>Hola Mundo en HTML</title></head><body>" +
"  <h2>mensaje de bienvenida con la hora actual</h2>" +
"  <p><a href=\"info.html\">Inicio</a></p>" +
"  <p><a href=\"/listado.html\">CSS Tutorial</a></p>" +
            "</body></html>";
    
    public static final String html_contenido="<html>" +
"  <head>" +
"    <meta charset=\"utf-8\">" +
"    <title>Inicio</title>" +
"  </head>" +
"  <body>" +
"  <h2>otros</h2>" +
"  <p><a href=\"info.html\">Inicio</a></p>" +
"  <p><a href=\"/listado.html\">CSS Tutorial</a></p>" +
"  </body>" +
"</html>";
    
    public static final String hrml_noEncontrado = "<html>" +
"  <head>" +
"    <meta charset=\"utf-8\">" +
"    <title>noEncontrado</title>" +
"  </head>" +
"  <body>" +
"    <p>ERROR Pagina no encontrada</p>" +
"  </body>" +
"</html>";


}
