package com.gestion.gestionProject.inventario.ordenescompra;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventario/ordenescompra")
public class OrdenCompraController {

	private final OrdenCompraService ordenCompraService;

	public OrdenCompraController(OrdenCompraService ordenCompraService) {
		this.ordenCompraService = ordenCompraService;
	}

	@GetMapping
	public Map<String, Object> list() {
		return ordenCompraService.listPlaceholder();
	}

}
