/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.financeiro.faturarConvenio.arquivoTxtDoIpe.gerarLaudos;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 *
 * @author cesar
 */
public class LaudoPDFModeloTresRodape extends PdfPageEventHelper {

    /**
     * The header text.
     */
    String header;
    /**
     * The template with the total number of pages.
     */
    PdfTemplate total;

    /**
     * Allows us to change the content of the header.
     *
     * @param header The new header String
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Creates the PdfTemplate that will hold the total number of pages.
     *
     * @param writer
     * @param document
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
     * com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        try {
            total = writer.getDirectContent().createTemplate(30, 16);
        } catch (Exception e) {
            System.out.println("EREO onOpenDocument: " + e + "\n" + e.getMessage());
        }

    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {        
        //carrega o logotipo
        Image img0 = null;
        //logotipo
        try {
            String urlDaAssinatura = "http://" + MakeLaudoPdf.domain + "/" + MakeLaudoPdf.portalname + "/img/logo.png";
            img0 = Image.getInstance(urlDaAssinatura);
            img0.setAlignment(Element.ALIGN_CENTER);
        } catch (Exception e) {
            //System.out.println("Erro ao buscar logo: " + e);
        }
        
        
        //coloca logotipo na pagina
        PdfPTable table = new PdfPTable(1);
        try {
            table.setWidths(new int[]{100});
            table.setTotalWidth(505);
            PdfPCell cell = new PdfPCell(img0);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            table.writeSelectedRows(0, -1, 45, 820, writer.getDirectContent());
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
        
        
        
        
        
        
        
        //carrega o rodape
        Image imgRodaPe = null;
        //logotipo
        try {
            String urlDaAssinatura = "http://" + MakeLaudoPdf.domain + "/" + MakeLaudoPdf.portalname + "/img/logoRodaPe.png";
            imgRodaPe = Image.getInstance(urlDaAssinatura);
            imgRodaPe.setAlignment(Element.ALIGN_CENTER);
        } catch (Exception e) {
            //System.out.println("Erro ao buscar logo: " + e);
        }
        
        
        //coloca rodape na pagina
        PdfPTable tableRodaPe = new PdfPTable(1);
        try {
            tableRodaPe.setWidths(new int[]{100});
            tableRodaPe.setTotalWidth(500);
            PdfPCell cell = new PdfPCell(imgRodaPe);
            cell.setBorder(Rectangle.NO_BORDER);
            tableRodaPe.addCell(cell);
            //table.getDefaultCell().setHorizontalAlignment(Rectangle.NO_BORDER);
            tableRodaPe.writeSelectedRows(0, -1, 45, 70, writer.getDirectContent());
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
        
        /* MODELO QUE DA CERTO
        PdfPTable table = new PdfPTable(3);
        try {
            table.setWidths(new int[]{24, 24, 2});
            table.setTotalWidth(527);
            table.setLockedWidth(true);
            table.getDefaultCell().setFixedHeight(20);
            table.getDefaultCell().setBorder(Rectangle.BOTTOM);
            table.addCell(header);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(String.format("Page %d of", writer.getPageNumber()));
            PdfPCell cell = new PdfPCell(Image.getInstance(total));
            cell.setBorder(Rectangle.BOTTOM);
            table.addCell(cell);
            table.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
                */
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(total, Element.ALIGN_LEFT, new Phrase(String.valueOf(writer.getPageNumber() - 1)), 2, 2, 0);
    }

}
