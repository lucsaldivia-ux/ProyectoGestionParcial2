package com.grupo.gestion.departamentos.model;
import jakarta.persistence.*;
import lombok.Data; import lombok.NoArgsConstructor; import lombok.AllArgsConstructor;
@Entity @Table(name = "departamentos")
@Data @NoArgsConstructor @AllArgsConstructor
public class Departamento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Column(nullable = false, unique = true) private String nombre;
    private String descripcion;
    private String estado;
}
