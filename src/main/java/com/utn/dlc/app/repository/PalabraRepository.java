package com.utn.dlc.app.repository;

import com.utn.dlc.app.entity.Documento;
import com.utn.dlc.app.entity.Palabra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PalabraRepository extends JpaRepository<Palabra, String> {
    @Query(value = "INSERT INTO palabras (nombre, cant_documentos) VALUES (?1, ?2)", nativeQuery = true)
    public List<Documento> insertPalabra(String nombre, int cant_documentos);
}
