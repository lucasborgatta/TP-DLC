package com.utn.dlc.app.entity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;


@Entity(name = "palabras")
@Table(name = "palabras")
@EntityListeners(AuditingEntityListener.class)

public class Palabra {

    @Id
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "cant_documentos")
    private Integer cantDocumentos;

    public void palabra() {
    }

    public void palabra(String nombre, Integer cantDocumentos){
        this.nombre = nombre;
        this.cantDocumentos = cantDocumentos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantDocumentos() {
        return this.cantDocumentos;
    }

    public void setCantDocumentos(Integer cantDocumentos) {
        this.cantDocumentos = cantDocumentos;
    }

    @Override
    public String toString() {
        return "\nPalabra [ Nombre = " + nombre + ", Cantidad de Documentos = " + cantDocumentos + " ]";
    }


}
