package com.gestion.gestionProject.inventario.existencias.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.gestion.gestionProject.inventario.existencias.ExistenciaRepository;
import com.gestion.gestionProject.inventario.existencias.ExistenciaService;

@Service
public class ExistenciaServiceImpl implements ExistenciaService {

	private final ExistenciaRepository existenciaRepository;

	public ExistenciaServiceImpl(ExistenciaRepository existenciaRepository) {
		this.existenciaRepository = existenciaRepository;
	}

	@Override
	public Map<String, Object> listPlaceholder() {
		return Map.of(
				"domain", "existencias",
				"items", existenciaRepository.findAll());
	}

}
