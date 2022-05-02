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

    public static final String HTML_INDEX
            = "<! DOCTYPE html>"
            + "<html>"
            + "<head>"
            + "   <meta charset=\"utf-8\">"
            + "   <title>Inicio</title>"
            + "</head>"
            + "<body>"
            + "  <h2>Bienvenido: Son las " + txtHora + " del " + txtFecha + "</h2>"
            + "  <p><a href=\"/\">Inicio</a></p>"
            + "  <p><a href=\"/info\">Info portal</a></p>"
            + "  <p><a href=\"/listado\">Listado</a></p>"
            + "</body></html>";

    public static final String HTML_INFO
            = "<!DOCTYPE html>"
            + "<html>"
            + "<head>"
            + "   <meta charset=\"utf-8\">"
            + "   <title>Info</title>"
            + "</head>"
            + "<body>"
            + "   <h2>Bienvenido al servicio de réplicas de RedIRIS</h2>"
            + "<p>El servicio de réplicas de RedIRIS ofrece un conjunto de copias"
            + " de repositorios de distintos sitios de interés para la comunidad "
            + "académica y de investigación. Está disponible vía Web, FTP y rsync."
            + "<p>Si desea contactar con nosotros para sugerir una réplica que "
            + "piense que deba ser replicada, puede contactar con nosotros, "
            + "indicándonos detalles de la misma, así como de la organización a "
            + "la que pertenece."
            + "<p>Ir a <a href=\"/\">Inicio</a></p>"
            + "</body>" + "</html>";


    public static final String hrml_noEncontrado
            = "<!DOCTYPE html>"
            + "<html>"
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
