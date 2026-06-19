package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import repositories.AdquisicionMaterialRepository;
import schemas.AdquisicionMaterial;
import schemas.Usuario;
import services.MaterialDocumentService;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "lectorMaterialServlet", urlPatterns = "/estudiante/biblioteca/leer")
public class LectorMaterialServlet extends HttpServlet {
    private final AdquisicionMaterialRepository adquisicionRepository = new AdquisicionMaterialRepository();
    private final MaterialDocumentService documentService = new MaterialDocumentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Optional<Usuario> estudianteOpt = ServletUtils.estudianteDesdeSesion(session);
        if (estudianteOpt.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Long id = parseId(req.getParameter("id"));
        Optional<AdquisicionMaterial> adquisicionOpt = adquisicionRepository.findOne(
                estudianteOpt.get().getIdPersona(), id);
        if (adquisicionOpt.isEmpty()) {
            ServletUtils.flash(session, "flashError", "Debes adquirir el material para visualizarlo.");
            resp.sendRedirect(req.getContextPath() + "/estudiante/marketplace");
            return;
        }

        AdquisicionMaterial adquisicion = adquisicionOpt.get();
        File file = documentService.resolveFile(getServletContext(), adquisicion.getMaterial());
        if (file == null || !"pdf".equalsIgnoreCase(adquisicion.getMaterial().getTipoArchivo())) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Documento no disponible.");
            return;
        }

        req.setAttribute("material", adquisicion.getMaterial());
        req.setAttribute("totalPaginas", documentService.countPages(file));
        req.getRequestDispatcher("/WEB-INF/jsp/estudiante/lector-material.jsp").forward(req, resp);
    }

    private Long parseId(String value) {
        try {
            return Long.valueOf(ServletUtils.value(value));
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
