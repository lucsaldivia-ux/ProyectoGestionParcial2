package com.grupo.gestion.departamentos.repository;
import com.grupo.gestion.departamentos.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {}
