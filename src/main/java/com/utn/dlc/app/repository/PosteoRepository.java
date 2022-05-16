package com.utn.dlc.app.repository;

import com.utn.dlc.app.entity.Posteo;
import com.utn.dlc.app.entity.PosteoPKId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosteoRepository extends JpaRepository<Posteo, PosteoPKId> {
}
