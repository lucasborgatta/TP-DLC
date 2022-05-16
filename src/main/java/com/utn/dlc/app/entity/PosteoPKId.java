package com.utn.dlc.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class PosteoPKId implements Serializable {

    private Long id_documento;
    private String nombre_palabra;


    public Long getId() {
        return id_documento;
    }

    public void setId(Long id) {
        this.id_documento = id;
    }

    public String getNombrePalabra() {
        return nombre_palabra;
    }

    public void setNombrePalabra(String nombrePalabra) {
        this.nombre_palabra = nombrePalabra;
    }
}
