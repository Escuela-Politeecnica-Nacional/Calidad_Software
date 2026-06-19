package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import repositories.AdquisicionMaterialRepository;
import repositories.MaterialRepository;
import schemas.Material;
import schemas.Usuario;
import services.MaterialAccessService;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "adquirirMaterialServlet", urlPatterns = "/estudiante/material/adquirir")
public class AdquirirMaterialServlet extends HttpServlet {
    private final MaterialRepository materialRepository = new MaterialRepository();
    private final AdquisicionMaterialRepository adquisicionRepository = new AdquisicionMaterialRepository();
    private final MaterialAccessService accessService = new MaterialAccessService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Optional<Usuario> estudianteOpt = ServletUtils.estudianteDesdeSesion(session);
        if (estudianteOpt.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Long id = parseId(req.getParameter("id"));
        Optional<Material> materialOpt = id == null ? Optional.empty() : materialRepository.findById(id);
        if (materialOpt.isEmpty() || !accessService.estaPublicado(materialOpt.get())) {
            ServletUtils.flash(session, "flashError", "No se pudo adquirir el material solicitado.");
            resp.sendRedirect(req.getContextPath() + "/estudiante/marketplace");
            return;
        }

        Material material = materialOpt.get();
        boolean creada = adquisicionRepository.acquire(estudianteOpt.get().getIdPersona(), material);
        ServletUtils.flash(session, "flashMensaje", creada
                ? "Material adquirido correctamente y añadido a tu biblioteca."
                : "Este material ya forma parte de tu biblioteca.");
        resp.sendRedirect(req.getContextPath() + "/estudiante/material/detalle?id=" + material.getId());
    }

    private Long parseId(String value) {
        try {
            return Long.valueOf(ServletUtils.value(value));
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
