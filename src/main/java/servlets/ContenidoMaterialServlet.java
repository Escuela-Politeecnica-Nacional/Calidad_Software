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
import services.MaterialDocumentService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "contenidoMaterialServlet", urlPatterns = "/estudiante/material/contenido")
public class ContenidoMaterialServlet extends HttpServlet {
    private final MaterialRepository materialRepository = new MaterialRepository();
    private final AdquisicionMaterialRepository adquisicionRepository = new AdquisicionMaterialRepository();
    private final MaterialAccessService accessService = new MaterialAccessService();
    private final MaterialDocumentService documentService = new MaterialDocumentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Optional<Usuario> estudianteOpt = ServletUtils.estudianteDesdeSesion(session);
        if (estudianteOpt.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Long id = parseLong(req.getParameter("id"));
        Optional<Material> materialOpt = id == null ? Optional.empty() : materialRepository.findById(id);
        if (materialOpt.isEmpty() || !accessService.puedeVerVistaPrevia(materialOpt.get())) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Material material = materialOpt.get();
        boolean completo = "true".equalsIgnoreCase(req.getParameter("completo"));
        boolean adquirido = adquisicionRepository.exists(estudianteOpt.get().getIdPersona(), material.getId());
        if (completo && !accessService.puedeVerCompleto(material, adquirido)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "El documento completo requiere adquisición.");
            return;
        }

        int pagina = completo ? Math.max(1, parsePage(req.getParameter("pagina"))) : 1;
        File file = documentService.resolveFile(getServletContext(), material);
        if (file == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        BufferedImage image = documentService.renderPage(file, pagina);
        resp.setContentType("image/png");
        resp.setHeader("Content-Disposition", "inline");
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("X-Content-Type-Options", "nosniff");
        resp.setHeader("Content-Security-Policy", "default-src 'none'");
        ImageIO.write(image, "png", resp.getOutputStream());
    }

    private int parsePage(String value) {
        try {
            return Integer.parseInt(ServletUtils.value(value));
        } catch (NumberFormatException ex) {
            return 1;
        }
    }

    private Long parseLong(String value) {
        try {
            return Long.valueOf(ServletUtils.value(value));
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
