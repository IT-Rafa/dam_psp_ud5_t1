/*
 * Enunciado Tarea para PSP05
 *
 * Ejercicio 1.
 * Crea un pequeño programa que simule un server HTTP multihilo
 * (es decir que atienda a varios clientes a la vez). El servidor le dará un mensaje
 * de bienvenida con la hora actual y las posibilidades que tiene (peticiones GET).
 *      • Una petición GET con simple información del portal.
 *      • Y otra petición GET que muestre el listado de ficheros de un directorio 
 *        publico usando FTP (por ejemplo ftp.rediris.es).
          En esa página debes mostrar el directorio actual y el listado de ficheros 
 *        con tipo y tamaño. Tipo: fichero o carpeta.

 * En el momento que un cliente accede a dicho servicio (página de bienvenida) se le mandará al administrador (tu) un email por SMTP con SSL.
 * Indicando la hora de acceso del cliente.
 */
package es.itrafa.dam_psp_ud5_t1;
