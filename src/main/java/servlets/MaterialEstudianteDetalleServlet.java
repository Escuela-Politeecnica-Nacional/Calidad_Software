package servlets;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import repositories.AdquisicionMaterialRepository;
import repositories.JpaUtil;
import repositories.MaterialRepository;
import schemas.Estudiante;
import schemas.Material;
import schemas.Tutor;
import schemas.Usuario;
import services.MaterialAccessService;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "materialEstudianteDetalleServlet", urlPatterns = "/estudiante/material/detalle")
public class MaterialEstudianteDetalleServlet extends HttpServlet {
    private final MaterialRepository materialRepository = new MaterialRepository();
    private final AdquisicionMaterialRepository adquisicionRepository = new AdquisicionMaterialRepository();
    private final MaterialAccessService accessService = new MaterialAccessService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Optional<Usuario> estudianteOpt = ServletUtils.estudianteDesdeSesion(session);
        if (estudianteOpt.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Long id = parseId(req.getParameter("id"));
        Optional<Material> materialOpt = id == null ? Optional.empty() : materialRepository.findById(id);
        if (materialOpt.isEmpty() || !accessService.estaPublicado(materialOpt.get())) {
            ServletUtils.flash(session, "flashError", "El material solicitado no está disponible.");
            resp.sendRedirect(req.getContextPath() + "/estudiante/marketplace");
            return;
        }

        Usuario usuario = estudianteOpt.get();
        Material material = materialOpt.get();
        boolean adquirido = adquisicionRepository.exists(usuario.getIdPersona(), material.getId());
        req.setAttribute("material", material);
        req.setAttribute("adquirido", adquirido);
        req.setAttribute("vistaPreviaDisponible", accessService.puedeVerVistaPrevia(material));

        try (EntityManager em = JpaUtil.createEntityManager()) {
            if (usuario.getIdPersona() != null) {
                req.setAttribute("estudiantePerfil", em.find(Estudiante.class, usuario.getIdPersona()));
            }
            if (material.getIdTutor() != null) {
                Tutor tutor = em.find(Tutor.class, material.getIdTutor());
                req.setAttribute("tutorMaterial", tutor);
            }
        }
        consumirFlash(session, req);
        req.getRequestDispatcher("/WEB-INF/jsp/estudiante/detalle-material.jsp").forward(req, resp);
    }

    private Long parseId(String value) {
        try {
            return Long.valueOf(ServletUtils.value(value));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private void consumirFlash(HttpSession session, HttpServletRequest req) {
        for (String key : new String[]{"flashMensaje", "flashError"}) {
            Object value = session.getAttribute(key);
            if (value != null) {
                req.setAttribute(key, value);
                session.removeAttribute(key);
            }
        }
    }
}
