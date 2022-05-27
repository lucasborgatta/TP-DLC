package com.utn.dlc.app.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity(name = "documentos")
@Table(name = "documentos")
@EntityListeners(AuditingEntityListener.class)
public class Documento {

    @Id
	private Long id;

	@Column(name = "nombre")
	private String nombre;

	public void documento() {
	}

	public void documento(Long id, String nombre){
		this.id = id;
		this.nombre = nombre;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "\nDocumento [ ID = " + id + ", Nombre = " + nombre + " ]";
	}
	
}