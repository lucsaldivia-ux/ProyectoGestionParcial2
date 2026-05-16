package com.gestion.gestionProject.inventario.proveedores.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.gestion.gestionProject.inventario.proveedores.ProveedorRepository;
import com.gestion.gestionProject.inventario.proveedores.ProveedorService;

@Service
public class ProveedorServiceImpl implements ProveedorService {

	private final ProveedorRepository proveedorRepository;

	public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
		this.proveedorRepository = proveedorRepository;
	}

	@Override
	public Map<String, Object> listPlaceholder() {
		return Map.of(
				"domain", "proveedores",
				"items", proveedorRepository.findAll());
	}

}
