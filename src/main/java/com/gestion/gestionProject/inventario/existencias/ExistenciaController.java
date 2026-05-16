package com.gestion.gestionProject.inventario.existencias;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API del módulo de dominio <em>Inventario (existencias/movimientos)</em>.
 */
@RestController
@RequestMapping("/api/v1/inventario/existencias")
public class ExistenciaController {

	private final ExistenciaService existenciaService;

	public ExistenciaController(ExistenciaService existenciaService) {
		this.existenciaService = existenciaService;
	}

	@GetMapping
	public Map<String, Object> list() {
		return existenciaService.listPlaceholder();
	}

}
