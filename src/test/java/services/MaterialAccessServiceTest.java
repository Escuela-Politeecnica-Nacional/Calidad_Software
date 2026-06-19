package services;

import Enums.EstadoMaterial;
import org.junit.jupiter.api.Test;
import schemas.Material;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MaterialAccessServiceTest {
    private final MaterialAccessService service = new MaterialAccessService();

    @Test
    void materialAprobadoPermiteVistaPreviaPdf() {
        Material material = Material.builder()
                .estado(EstadoMaterial.APROBADO)
                .tipoArchivo("pdf")
                .build();

        assertTrue(service.puedeVerVistaPrevia(material));
        assertFalse(service.puedeVerCompleto(material, false));
    }

    @Test
    void contenidoCompletoRequiereAdquisicion() {
        Material material = Material.builder()
                .estado(EstadoMaterial.APROBADO)
                .tipoArchivo("pdf")
                .build();

        assertTrue(service.puedeVerCompleto(material, true));
    }

    @Test
    void materialPendienteNoSePublica() {
        Material material = Material.builder()
                .estado(EstadoMaterial.PENDIENTE)
                .tipoArchivo("pdf")
                .build();

        assertFalse(service.estaPublicado(material));
        assertFalse(service.puedeVerVistaPrevia(material));
    }
}
