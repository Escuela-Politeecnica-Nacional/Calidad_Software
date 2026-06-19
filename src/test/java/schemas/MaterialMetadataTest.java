package schemas;

import Enums.Carrera;
import Enums.CategoriaMaterial;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MaterialMetadataTest {

    @Test
    void obtieneCarreraYSemestreDesdeCatalogoParaMaterialLegado() {
        Material material = Material.builder()
                .idMateria("ICCD244")
                .categoria(CategoriaMaterial.GUIA)
                .build();

        assertEquals(Carrera.SOFTWARE, material.getCarreraEfectiva());
        assertEquals(2, material.getSemestreEfectivo());
        assertEquals("Guía", material.getCategoriaNombre());
    }
}
