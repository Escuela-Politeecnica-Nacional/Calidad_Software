package repositories;

import Enums.EstadoMaterial;
import Enums.Carrera;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import schemas.Material;

import java.util.List;
import java.util.Optional;

public class MaterialRepository {

    public List<Material> findAll() {
        try (EntityManager em = JpaUtil.createEntityManager()) {
            return em.createQuery(
                            "SELECT m FROM Material m ORDER BY m.fechaEnvio DESC",
                            Material.class)
                    .getResultList();
        }
    }

    public Optional<Material> findById(Long id) {
        try (EntityManager em = JpaUtil.createEntityManager()) {
            return Optional.ofNullable(em.find(Material.class, id));
        }
    }

    public Material save(Material material) {
        try (EntityManager em = JpaUtil.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                Material managed = em.merge(material);
                tx.commit();
                return managed;
            } catch (RuntimeException ex) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw ex;
            }
        }
    }

    public boolean updateEstado(Long id, EstadoMaterial estado) {
        return updateEstadoConMotivo(id, estado, null);
    }

    public boolean updateEstadoConMotivo(Long id, EstadoMaterial estado, String motivo) {
        try (EntityManager em = JpaUtil.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                Material material = em.find(Material.class, id);
                if (material == null) {
                    tx.rollback();
                    return false;
                }
                material.setEstado(estado);
                material.setMotivoRechazo(motivo);
                em.merge(material);
                tx.commit();
                return true;
            } catch (RuntimeException ex) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw ex;
            }
        }
    }

    public List<Material> findByUsuario(String nombreUsuario) {
        try (EntityManager em = JpaUtil.createEntityManager()) {
            return em.createQuery(
                            "SELECT m FROM Material m WHERE m.usuario = :usuario ORDER BY m.fechaEnvio DESC",
                            Material.class)
                    .setParameter("usuario", nombreUsuario)
                    .getResultList();
        }
    }

    public List<Material> findMarketplace(Carrera carrera, String codigoMateria, Integer semestre) {
        List<Material> aprobados;
        try (EntityManager em = JpaUtil.createEntityManager()) {
            aprobados = em.createQuery(
                            "SELECT m FROM Material m WHERE m.estado = :estado ORDER BY m.fechaEnvio DESC",
                            Material.class)
                    .setParameter("estado", EstadoMaterial.APROBADO)
                    .getResultList();
        }

        String materia = codigoMateria == null ? "" : codigoMateria.trim();
        return aprobados.stream()
                .filter(m -> carrera == null || carrera == m.getCarreraEfectiva())
                .filter(m -> materia.isBlank() || materia.equalsIgnoreCase(m.getIdMateria()))
                .filter(m -> semestre == null || semestre.equals(m.getSemestreEfectivo()))
                .toList();
    }
}
