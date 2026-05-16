package com.grupo.gestion.empleados.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Entity
@Table(name = "empleados")
@Data @NoArgsConstructor @AllArgsConstructor
public class Empleado {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false) private String nombre;
    @Column(nullable = false) private String apellido;
    @Column(unique = true, nullable = false) private String email;
    private String telefono;
    private String estado;
}
