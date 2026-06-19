package schemas;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "adquisiciones_material",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_adquisicion_estudiante_material",
                columnNames = {"id_estudiante", "id_material"}))
public class AdquisicionMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_adquisicion")
    private Long id;

    @Column(name = "id_estudiante", nullable = false)
    private Long idEstudiante;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_material", nullable = false)
    private Material material;

    @Column(name = "fecha_adquisicion", nullable = false)
    private LocalDateTime fechaAdquisicion;
}
