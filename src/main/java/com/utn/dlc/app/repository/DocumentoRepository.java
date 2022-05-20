package com.utn.dlc.app.repository;

import com.utn.dlc.app.entity.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository("docRepo")
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    @Query(value = "SELECT d.* from documentos d where  d.id=?1", nativeQuery = true)
    public List<Documento> getDocumentoById(Long id);

}
