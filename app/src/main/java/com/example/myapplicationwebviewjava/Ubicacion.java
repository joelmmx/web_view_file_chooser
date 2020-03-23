/*
 * Licencia QPL licencia libre para el publico, privativa para uso comercial.
 * Este software no es libre, pero incluye autorización para que los particulares lo usen, lo copien,
 * lo distribuyan y lo modifiquen (incluyendo la distribución de versiones modificadas) sin propósitos lucrativos.
 */
package com.example.myapplicationwebviewjava;

/**
 *
 * @author @author joseluis.machado@ine.mx <José Luis Machado Mendoza, INE>,
 * 2019.
 */
public class Ubicacion {
    
    private String geoloc;
    private String colonia;
    private String calle;
    private String numero;
    private String cp;
    private String edificio;
    private String entrada;
    private String localidad;
    private String edmslm;
    private String longitud;
    private String latitud;
    private String entidad;

    public String getGeoloc() {
        return geoloc;
    }

    public void setGeoloc(String geoloc) {
        this.geoloc = geoloc;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getEdmslm() {
        return edmslm;
    }

    public void setEdmslm(String edmslm) {
        this.edmslm = edmslm;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    @Override
    public String toString() {
        return "Ubicacion{" +
                "geoloc='" + geoloc + '\'' +
                ", colonia='" + colonia + '\'' +
                ", calle='" + calle + '\'' +
                ", numero='" + numero + '\'' +
                ", cp='" + cp + '\'' +
                ", edificio='" + edificio + '\'' +
                ", entrada='" + entrada + '\'' +
                ", localidad='" + localidad + '\'' +
                ", edmslm='" + edmslm + '\'' +
                ", longitud='" + longitud + '\'' +
                ", latitud='" + latitud + '\'' +
                ", entidad='" + entidad + '\'' +
                '}';
    }
}
