package com.gestion.existencias.repository;

import com.gestion.existencias.model.Existencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExistenciaRepository extends JpaRepository<Existencia, Long> {}
