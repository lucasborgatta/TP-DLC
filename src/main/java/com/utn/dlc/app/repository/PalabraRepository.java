package com.utn.dlc.app.repository;

import com.utn.dlc.app.entity.Palabra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PalabraRepository extends JpaRepository<Palabra, String> {
}
