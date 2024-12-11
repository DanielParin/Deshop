package dev.danielparin.backoffice_tienda.utils;



import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import dev.danielparin.backoffice_tienda.models.Pedido;
import dev.danielparin.backoffice_tienda.models.Producto;

import java.io.File;
import java.util.List;

public class PedidoFile {

    public static void generarPDF(File file, Pedido pedido, List<Producto> productos) throws Exception {
        // Crear un escritor de PDF
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Título del documento
        document.add(new Paragraph("Detalles del Pedido")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        // Información del pedido
        document.add(new Paragraph("ID del Pedido: " + pedido.getIdPedido()));
        document.add(new Paragraph("Fecha del Pedido: " + pedido.getFPedido()));
        document.add(new Paragraph("Estado: " + pedido.getEstado()));
        document.add(new Paragraph("ID del Cliente: " + pedido.getCliente().getIdCliente()));
        document.add(new Paragraph("\n"));

        // Tabla de productos
        Table table = new Table(5); // 5 columnas
        table.addHeaderCell("ID Producto");
        table.addHeaderCell("Nombre");
        table.addHeaderCell("Cantidad");
        table.addHeaderCell("Precio Unitario");
        table.addHeaderCell("Subtotal");

        double totalCoste = 0;

        for (Producto producto : productos) {
            table.addCell(String.valueOf(producto.getIdProducto()));
            table.addCell(producto.getNombre());
            table.addCell(String.valueOf(producto.getCantidad()));
            table.addCell(String.format("%.2f", producto.getPrecio()));
            double subtotal = producto.getPrecio();
            table.addCell(String.format("%.2f", subtotal));
            totalCoste += subtotal;
        }

        document.add(table);

        double iva = totalCoste * 0.21;
        double totalConIva = totalCoste + iva;

        document.add(new Paragraph("\nCoste Total (sin IVA): " + String.format("%.2f", totalCoste)));
        document.add(new Paragraph("IVA (21%): " + String.format("%.2f", iva)));
        document.add(new Paragraph("Coste Total (con IVA): " + String.format("%.2f", totalConIva))
                .setBold());

        document.close();
    }
}
