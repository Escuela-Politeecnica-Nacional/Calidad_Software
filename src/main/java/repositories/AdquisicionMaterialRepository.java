package repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import schemas.AdquisicionMaterial;
import schemas.Material;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AdquisicionMaterialRepository {

    public boolean exists(Long idEstudiante, Long idMaterial) {
        if (idEstudiante == null || idMaterial == null) {
            return false;
        }
        try (EntityManager em = JpaUtil.createEntityManager()) {
            Long total = em.createQuery(
                            "SELECT COUNT(a) FROM AdquisicionMaterial a " +
                                    "WHERE a.idEstudiante = :estudiante AND a.material.id = :material",
                            Long.class)
                    .setParameter("estudiante", idEstudiante)
                    .setParameter("material", idMaterial)
                    .getSingleResult();
            return total != null && total > 0;
        }
    }

    public boolean acquire(Long idEstudiante, Material material) {
        if (idEstudiante == null || material == null || material.getId() == null) {
            return false;
        }
        if (exists(idEstudiante, material.getId())) {
            return false;
        }

        try (EntityManager em = JpaUtil.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                Material managedMaterial = em.getReference(Material.class, material.getId());
                AdquisicionMaterial adquisicion = AdquisicionMaterial.builder()
                        .idEstudiante(idEstudiante)
                        .material(managedMaterial)
                        .fechaAdquisicion(LocalDateTime.now())
                        .build();
                em.persist(adquisicion);
                tx.commit();
                return true;
            } catch (RuntimeException ex) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                if (exists(idEstudiante, material.getId())) {
                    return false;
                }
                throw ex;
            }
        }
    }

    public List<AdquisicionMaterial> findByEstudiante(Long idEstudiante) {
        if (idEstudiante == null) {
            return List.of();
        }
        try (EntityManager em = JpaUtil.createEntityManager()) {
            return em.createQuery(
                            "SELECT a FROM AdquisicionMaterial a JOIN FETCH a.material " +
                                    "WHERE a.idEstudiante = :estudiante ORDER BY a.fechaAdquisicion DESC",
                            AdquisicionMaterial.class)
                    .setParameter("estudiante", idEstudiante)
                    .getResultList();
        }
    }

    public Optional<AdquisicionMaterial> findOne(Long idEstudiante, Long idMaterial) {
        if (idEstudiante == null || idMaterial == null) {
            return Optional.empty();
        }
        try (EntityManager em = JpaUtil.createEntityManager()) {
            return em.createQuery(
                            "SELECT a FROM AdquisicionMaterial a JOIN FETCH a.material " +
                                    "WHERE a.idEstudiante = :estudiante AND a.material.id = :material",
                            AdquisicionMaterial.class)
                    .setParameter("estudiante", idEstudiante)
                    .setParameter("material", idMaterial)
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst();
        }
    }
}
