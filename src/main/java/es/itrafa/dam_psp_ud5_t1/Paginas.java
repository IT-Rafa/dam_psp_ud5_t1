package es.itrafa.dam_psp_ud5_t1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class Paginas {

    private static DateTimeFormatter dtfHora = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static DateTimeFormatter dtfFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static String txtHora = dtfHora.format(LocalDateTime.now());
    private static String txtFecha = dtfFecha.format(LocalDateTime.now());
    public static final String primeraCabecera = "Content-Type:text/html;charset=UTF-8";

    public static final String html_index
            = "<html>"
            + "<head>"
            + "   <meta charset=\"utf-8\">"
            + "   <title>Inicio</title>"
            + "</head>"
            + "<body>"
            + "  <h2>Bienvenido: Son las " + txtHora + " de " + txtFecha + "</h2>"
            + "  <p><a href=\"/\">Inicio</a></p>"
            + "  <p><a href=\"/info\">Info portal</a></p>"
            + "  <p><a href=\"/listado\">Listado</a></p>"
            + "</body></html>";

    public static final String html_info
            = "<html>"
            + "<head>"
            + "   <meta charset=\"utf-8\">"
            + "   <title>Inicio</title>"
            + "</head>"
            + "<body>"
            + "   <h2>info pendiente</h2>"
            + "</body>" + "</html>";

    public static final String html_listado
            = "<html>"
            + "<head>"
            + "   <meta charset=\"utf-8\">"
            + "   <title>Listado</title>"
            + "</head>"
            + "<body>"
            + "   <h2>listado pendiente</h2>"
            + "</body>" + "</html>";
    public static final String hrml_noEncontrado
            = "<html>"
            + "<head>"
            + "<meta charset=\"utf-8\">"
            + "<title>Página no encontrada</title>"
            + "</head>"
            + "<body>"
            + "<p>ERROR: Página no encontrada</p>"
            + "<p>Ir a <a href=\"/\">Inicio</a></p>"
            + "</body>"
            + "</html>";

}
