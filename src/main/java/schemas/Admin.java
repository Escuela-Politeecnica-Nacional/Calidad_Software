package schemas;

import Enums.Estados;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "administradores")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin")
    private Long id;

    private String nombre;
    private String segundoNombre;
    private String apellido;
    private String segundoApellido;

    @Enumerated(EnumType.STRING)
    private Estados estado;

    @Builder.Default
    private boolean mustChangePassword = false;

    public Admin(Long id, String email, String nombre, String apellido, String password, Estados estado) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.estado = estado;
        this.mustChangePassword = false;
    }
}
