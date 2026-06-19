package servlets;

import Enums.CategoriaMaterial;
import Enums.EstadoMaterial;
import Enums.MateriasCatalogo;
import Enums.Rol;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import schemas.Material;
import schemas.Usuario;
import repositories.JpaUtil;
import repositories.MaterialRepository;
import servlets.validators.ArchivoMaterialValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@WebServlet(name = "subirMaterialServlet", urlPatterns = "/tutor/subir")
@MultipartConfig(maxFileSize = 25 * 1024 * 1024)
public class SubirMaterialServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/jsp/tutor/subir-material.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!esTutor(req, resp)) return;

        HttpSession session = req.getSession(false);
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        prepararMateriasYPerfil(req, u);

        req.setAttribute("categorias", CategoriaMaterial.values());
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    private static void prepararMateriasYPerfil(HttpServletRequest req, Usuario u) {
        if (u != null && u.getIdPersona() != null) {
            EntityManager em = JpaUtil.createEntityManager();
            try {
                schemas.Tutor tutorPerfil = em.find(schemas.Tutor.class, u.getIdPersona());
                req.setAttribute("tutorPerfil", tutorPerfil);
                if (tutorPerfil != null && tutorPerfil.getCarrera() != null) {
                    req.setAttribute("materiasOpciones",
                            MateriasCatalogo.porCarreraParaTutor(tutorPerfil.getCarrera(), tutorPerfil.getSemestre()));
                } else {
                    req.setAttribute("materiasOpciones", MateriasCatalogo.todasOpcionesBusqueda());
                }
            } finally {
                em.close();
            }
        } else {
            req.setAttribute("materiasOpciones", MateriasCatalogo.todasOpcionesBusqueda());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!esTutor(req, resp)) {
            return;
        }
        HttpSession session = req.getSession(false);

        String titulo = req.getParameter("titulo");
        String descripcion = req.getParameter("descripcion");
        String categoria = req.getParameter("nombreMateria");
        String materiaCodigo = ServletUtils.value(req.getParameter("materia"));
        String precioStr = req.getParameter("costo");
        Part archivoPart = req.getPart("archivo");
        if (titulo == null || titulo.isBlank()) {
            req.setAttribute("error", "El título es obligatorio.");
            req.setAttribute("categorias", CategoriaMaterial.values());
            prepararMateriasYPerfil(req, (Usuario) session.getAttribute("usuarioLogueado"));
            req.getRequestDispatcher(VIEW).forward(req, resp);
            return;
        }
        if (descripcion == null || descripcion.isBlank()) {
            req.setAttribute("error", "La descripción es obligatoria.");
            req.setAttribute("categorias", CategoriaMaterial.values());
            prepararMateriasYPerfil(req, (Usuario) session.getAttribute("usuarioLogueado"));
            req.getRequestDispatcher(VIEW).forward(req, resp);
            return;
        }
        if (materiaCodigo.isBlank()) {
            req.setAttribute("error", "Debes seleccionar una materia para publicar el material.");
            req.setAttribute("categorias", CategoriaMaterial.values());
            prepararMateriasYPerfil(req, (Usuario) session.getAttribute("usuarioLogueado"));
            req.getRequestDispatcher(VIEW).forward(req, resp);
            return;
        }

        Double costo;
        try {
            costo = Double.parseDouble(ServletUtils.value(precioStr));
            if (costo < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            req.setAttribute("error", "El precio debe ser un número mayor o igual a cero.");
            req.setAttribute("categorias", CategoriaMaterial.values());
            prepararMateriasYPerfil(req, (Usuario) session.getAttribute("usuarioLogueado"));
            req.getRequestDispatcher(VIEW).forward(req, resp);
            return;
        }

        if (archivoPart == null || archivoPart.getSize() == 0 || archivoPart.getSubmittedFileName() == null
                || archivoPart.getSubmittedFileName().isBlank()) {
            req.setAttribute("error", "Debes adjuntar un archivo PDF.");
            req.setAttribute("categorias", CategoriaMaterial.values());
            prepararMateriasYPerfil(req, (Usuario) session.getAttribute("usuarioLogueado"));
            req.getRequestDispatcher(VIEW).forward(req, resp);
            return;
        }

        String nombreArchivoOriginal = archivoPart.getSubmittedFileName();
        String extension = ArchivoMaterialValidator.obtenerExtension(nombreArchivoOriginal);

        if (!".pdf".equalsIgnoreCase(extension)) {
            req.setAttribute("error", "Para garantizar la vista previa y lectura en plataforma, el archivo debe ser PDF.");
            req.setAttribute("categorias", CategoriaMaterial.values());
            prepararMateriasYPerfil(req, (Usuario) session.getAttribute("usuarioLogueado"));
            req.getRequestDispatcher(VIEW).forward(req, resp);
            return;
        }

        String uploadsDir = getServletContext().getRealPath("")
                + File.separator + "WEB-INF" + File.separator + "uploads" + File.separator + "materiales";
        Files.createDirectories(Paths.get(uploadsDir));
        String nombreArchivoGuardado = UUID.randomUUID() + extension;
        String rutaArchivo = uploadsDir + File.separator + nombreArchivoGuardado;

        try (InputStream input = archivoPart.getInputStream();
            OutputStream output = new FileOutputStream(rutaArchivo)) {
            input.transferTo(output);
        }

        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        String nombreUsuario = "Tutor";

        schemas.Tutor tutorRef = null;
        if (u.getIdPersona() != null) {
            EntityManager em = JpaUtil.createEntityManager();
            try {
                tutorRef = em.find(schemas.Tutor.class, u.getIdPersona());
                if (tutorRef != null && tutorRef.getNombre() != null) {
                    nombreUsuario = nombreCompleto(tutorRef);
                }
            } finally {
                em.close();
            }
        }

        if (tutorRef != null && tutorRef.getCarrera() != null && !materiaCodigo.isBlank()) {
            if (!MateriasCatalogo.codigoPermitidoParaTutor(tutorRef.getCarrera(), tutorRef.getSemestre(), materiaCodigo)) {
                req.setAttribute("error", "La materia no corresponde a tu carrera y semestre (solo niveles ya cursados).");
                req.setAttribute("categorias", CategoriaMaterial.values());
                prepararMateriasYPerfil(req, u);
                req.getRequestDispatcher(VIEW).forward(req, resp);
                return;
            }
        }

        String idMateriaVal = materiaCodigo.isBlank() ? categoria : materiaCodigo;
        MateriasCatalogo.Opcion materiaOpcion = tutorRef != null && tutorRef.getCarrera() != null
                ? MateriasCatalogo.porCarrera(tutorRef.getCarrera()).stream()
                        .filter(opcion -> opcion.getCodigo().equalsIgnoreCase(materiaCodigo))
                        .findFirst()
                        .orElse(null)
                : MateriasCatalogo.buscarPorCodigo(materiaCodigo).orElse(null);
        String nombreMateriaVal = java.util.Optional.ofNullable(materiaOpcion)
                .map(MateriasCatalogo.Opcion::getNombre)
                .orElse(categoria != null && !categoria.isBlank() ? categoria : materiaCodigo);
        CategoriaMaterial categoriaMaterial = resolverCategoria(categoria);

        Material material = Material.builder()
                .titulo(titulo)
                .descripcion(descripcion)
                .nombreArchivo(nombreArchivoGuardado)
                .idMateria(idMateriaVal)
                .nombreMateria(nombreMateriaVal)
                .rutaArchivo("WEB-INF/uploads/materiales/" + nombreArchivoGuardado)
                .tipoArchivo(extension.substring(1))
                .costo(costo)
                .estado(EstadoMaterial.PENDIENTE)
                .fechaEnvio(java.time.LocalDateTime.now())
                .usuario(nombreUsuario)
                .idTutor(tutorRef == null ? null : tutorRef.getId())
                .carrera(materiaOpcion != null ? materiaOpcion.getCarrera()
                        : tutorRef == null ? null : tutorRef.getCarrera())
                .semestre(materiaOpcion == null ? null : materiaOpcion.getSemestre())
                .categoria(categoriaMaterial)
                .build();

        new MaterialRepository().save(material);

        session.setAttribute("flashMensaje", "Material enviado correctamente");
        resp.sendRedirect(req.getContextPath() + "/tutor/subir");
    }

    private boolean esTutor(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || !(session.getAttribute("usuarioLogueado") instanceof Usuario u)
                || u.getRol() != Rol.TUTOR) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    private CategoriaMaterial resolverCategoria(String nombre) {
        if (nombre == null) {
            return null;
        }
        for (CategoriaMaterial categoria : CategoriaMaterial.values()) {
            if (categoria.name().equalsIgnoreCase(nombre) || categoria.getNombre().equalsIgnoreCase(nombre)) {
                return categoria;
            }
        }
        return null;
    }

    private String nombreCompleto(schemas.Tutor tutor) {
        return java.util.stream.Stream.of(
                        tutor.getNombre(), tutor.getSegundoNombre(), tutor.getApellido(), tutor.getSegundoApellido())
                .filter(value -> value != null && !value.isBlank())
                .collect(java.util.stream.Collectors.joining(" "));
    }
}
