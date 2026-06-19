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
import schemas.Estudiante;
import schemas.Usuario;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "bibliotecaEstudianteServlet", urlPatterns = "/estudiante/biblioteca")
public class BibliotecaEstudianteServlet extends HttpServlet {
    private final AdquisicionMaterialRepository adquisicionRepository = new AdquisicionMaterialRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Optional<Usuario> estudianteOpt = ServletUtils.estudianteDesdeSesion(session);
        if (estudianteOpt.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        Usuario usuario = estudianteOpt.get();
        req.setAttribute("adquisiciones", adquisicionRepository.findByEstudiante(usuario.getIdPersona()));
        if (usuario.getIdPersona() != null) {
            try (EntityManager em = JpaUtil.createEntityManager()) {
                req.setAttribute("estudiantePerfil", em.find(Estudiante.class, usuario.getIdPersona()));
            }
        }
        req.getRequestDispatcher("/WEB-INF/jsp/estudiante/biblioteca-digital.jsp").forward(req, resp);
    }
}
