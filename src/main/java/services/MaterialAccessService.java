package services;

import Enums.EstadoMaterial;
import schemas.Material;

public class MaterialAccessService {

    public boolean estaPublicado(Material material) {
        return material != null && material.getEstado() == EstadoMaterial.APROBADO;
    }

    public boolean puedeVerCompleto(Material material, boolean adquirido) {
        return estaPublicado(material) && adquirido;
    }

    public boolean puedeVerVistaPrevia(Material material) {
        return estaPublicado(material) && "pdf".equalsIgnoreCase(material.getTipoArchivo());
    }
}
