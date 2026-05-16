package com.gestion.gestionProject.inventario.almacenes.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.gestion.gestionProject.inventario.almacenes.AlmacenRepository;
import com.gestion.gestionProject.inventario.almacenes.AlmacenService;

@Service
public class AlmacenServiceImpl implements AlmacenService {

	private final AlmacenRepository almacenRepository;

	public AlmacenServiceImpl(AlmacenRepository almacenRepository) {
		this.almacenRepository = almacenRepository;
	}

	@Override
	public Map<String, Object> listPlaceholder() {
		return Map.of(
				"domain", "almacenes",
				"items", almacenRepository.findAll());
	}

}
