package repositories;

import Enums.EstadoMaterial;
import org.junit.jupiter.api.Test;
import schemas.Material;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdquisicionMaterialRepositoryTest {

    @Test
    void adquisicionSeRegistraUnaSolaVezPorEstudianteYMaterial() {
        Material material = new MaterialRepository().save(Material.builder()
                .titulo("Documento para biblioteca")
                .estado(EstadoMaterial.APROBADO)
                .tipoArchivo("pdf")
                .fechaEnvio(LocalDateTime.now())
                .build());
        AdquisicionMaterialRepository repository = new AdquisicionMaterialRepository();

        assertTrue(repository.acquire(999_001L, material));
        assertTrue(repository.exists(999_001L, material.getId()));
        assertFalse(repository.acquire(999_001L, material));
        assertTrue(repository.findOne(999_001L, material.getId()).isPresent());
    }
}
