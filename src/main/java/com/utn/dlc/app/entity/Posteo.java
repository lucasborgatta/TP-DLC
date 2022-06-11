package com.utn.dlc.app.entity;

import com.utn.dlc.app.repository.PosteoRepository;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity(name = "posteos")
@Table(name = "posteos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Posteo {

    @Id
    @Column(name = "nombre_palabra")
    private String nombre_palabra;

    @Column(name = "id_documento")
    private Long id_documento;

    @Column(name = "frecuencia")
    private Long frecuencia;

    public Long getId_documento() {
        return id_documento;
    }

    public void setId_documento(Long id) {
        this.id_documento = id;
    }

    public String getNombre_palabra() {
        return nombre_palabra;
    }

    public void setNombre_palabra(String nombrePalabra) {
        this.nombre_palabra = nombrePalabra;
    }

    public Long getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(Long frecuenciaAparicion) {
        this.frecuencia = frecuenciaAparicion;
    }

}
