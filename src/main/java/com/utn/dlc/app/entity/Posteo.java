package com.utn.dlc.app.entity;

import com.utn.dlc.app.repository.PosteoRepository;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "posteos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@IdClass(PosteoPKId.class)
public class Posteo {

    @Id
    @Column(name = "id_documento")
    private Long id_documento;

    @Id
    @Column(name = "nombre_palabra")
    private String nombre_palabra;

    @Column(name = "frecuencia")
    private Long frecuencia;

    @Column(name = "peso")
    private Double peso;

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

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }
}
