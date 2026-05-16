package com.gestion.gestionProject.inventario.ordenescompra.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.gestion.gestionProject.inventario.ordenescompra.OrdenCompraRepository;
import com.gestion.gestionProject.inventario.ordenescompra.OrdenCompraService;

@Service
public class OrdenCompraServiceImpl implements OrdenCompraService {

	private final OrdenCompraRepository ordenCompraRepository;

	public OrdenCompraServiceImpl(OrdenCompraRepository ordenCompraRepository) {
		this.ordenCompraRepository = ordenCompraRepository;
	}

	@Override
	public Map<String, Object> listPlaceholder() {
		return Map.of(
				"domain", "ordenescompra",
				"items", ordenCompraRepository.findAll());
	}

}
