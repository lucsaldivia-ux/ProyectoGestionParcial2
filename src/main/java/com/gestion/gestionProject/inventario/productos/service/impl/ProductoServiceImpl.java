package com.gestion.gestionProject.inventario.productos.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.gestion.gestionProject.inventario.productos.ProductoRepository;
import com.gestion.gestionProject.inventario.productos.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService {

	private final ProductoRepository productoRepository;

	public ProductoServiceImpl(ProductoRepository productoRepository) {
		this.productoRepository = productoRepository;
	}

	@Override
	public Map<String, Object> listPlaceholder() {
		return Map.of(
				"domain", "productos",
				"items", productoRepository.findAll());
	}

}
