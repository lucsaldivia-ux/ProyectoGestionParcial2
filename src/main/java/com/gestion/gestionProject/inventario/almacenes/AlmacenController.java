package com.gestion.gestionProject.inventario.almacenes;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventario/almacenes")
public class AlmacenController {

	private final AlmacenService almacenService;

	public AlmacenController(AlmacenService almacenService) {
		this.almacenService = almacenService;
	}

	@GetMapping
	public Map<String, Object> list() {
		return almacenService.listPlaceholder();
	}

}
