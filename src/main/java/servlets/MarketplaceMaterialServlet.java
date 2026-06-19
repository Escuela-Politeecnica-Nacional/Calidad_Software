package servlets;

import Enums.Carrera;
import Enums.MateriasCatalogo;
import Enums.Semestre;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import repositories.JpaUtil;
import repositories.MaterialRepository;
import schemas.Estudiante;
import schemas.Usuario;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "marketplaceMaterialServlet", urlPatterns = "/estudiante/marketplace")
public class MarketplaceMaterialServlet extends HttpServlet {
    private final MaterialRepository materialRepository = new MaterialRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Optional<Usuario> usuarioOpt = ServletUtils.estudianteDesdeSesion(session);
        if (usuarioOpt.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Carrera carrera = parseCarrera(req.getParameter("carrera"));
        Integer semestre = parseSemestre(req.getParameter("semestre"));
        String materia = ServletUtils.value(req.getParameter("materia"));

        req.setAttribute("materiales", materialRepository.findMarketplace(carrera, materia, semestre));
        req.setAttribute("carreras", Carrera.values());
        req.setAttribute("semestres", Semestre.values());
        req.setAttribute("materias", carrera == null
                ? MateriasCatalogo.todasOpcionesBusqueda()
                : MateriasCatalogo.porCarrera(carrera));
        req.setAttribute("carreraSeleccionada", carrera);
        req.setAttribute("semestreSeleccionado", semestre);
        req.setAttribute("materiaSeleccionada", materia);
        cargarPerfil(req, usuarioOpt.get());
        consumirFlash(session, req);
        req.getRequestDispatcher("/WEB-INF/jsp/estudiante/marketplace-materiales.jsp").forward(req, resp);
    }

    private void cargarPerfil(HttpServletRequest req, Usuario usuario) {
        if (usuario.getIdPersona() == null) {
            return;
        }
        try (EntityManager em = JpaUtil.createEntityManager()) {
            req.setAttribute("estudiantePerfil", em.find(Estudiante.class, usuario.getIdPersona()));
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

    private Carrera parseCarrera(String value) {
        try {
            return ServletUtils.value(value).isBlank() ? null : Carrera.valueOf(value);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private Integer parseSemestre(String value) {
        try {
            String normalized = ServletUtils.value(value);
            return normalized.isBlank() ? null : Integer.valueOf(normalized);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
