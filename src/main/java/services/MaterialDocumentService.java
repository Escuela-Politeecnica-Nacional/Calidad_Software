package services;

import jakarta.servlet.ServletContext;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import schemas.Material;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MaterialDocumentService {

    public File resolveFile(ServletContext context, Material material) throws IOException {
        if (context == null || material == null || material.getRutaArchivo() == null) {
            return null;
        }
        String relativePath = material.getRutaArchivo().replace('\\', '/');
        while (relativePath.startsWith("/")) {
            relativePath = relativePath.substring(1);
        }
        String realPath = context.getRealPath("/" + relativePath);
        if (realPath == null) {
            return null;
        }

        File file = new File(realPath).getCanonicalFile();
        String secureRootPath = context.getRealPath("/WEB-INF/uploads/materiales");
        String legacyRootPath = context.getRealPath("/uploads/materiales");
        boolean insideSecureRoot = secureRootPath != null
                && file.toPath().startsWith(new File(secureRootPath).getCanonicalFile().toPath());
        boolean insideLegacyRoot = legacyRootPath != null
                && file.toPath().startsWith(new File(legacyRootPath).getCanonicalFile().toPath());
        if ((!insideSecureRoot && !insideLegacyRoot) || !file.isFile()) {
            return null;
        }
        return file;
    }

    public int countPages(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            return document.getNumberOfPages();
        }
    }

    public BufferedImage renderPage(File file, int requestedPage) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            int pageIndex = Math.max(0, Math.min(requestedPage - 1, document.getNumberOfPages() - 1));
            PDFRenderer renderer = new PDFRenderer(document);
            return renderer.renderImageWithDPI(pageIndex, 135, ImageType.RGB);
        }
    }
}
