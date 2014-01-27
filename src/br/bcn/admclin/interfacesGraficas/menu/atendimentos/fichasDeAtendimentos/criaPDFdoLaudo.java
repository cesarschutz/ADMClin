package br.bcn.admclin.interfacesGraficas.menu.atendimentos.fichasDeAtendimentos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

import br.bcn.admclin.ClasseAuxiliares.OSvalidator;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class criaPDFdoLaudo {

    String numero;
    String dataDoExame;
    String nomePaciente;
    String nomeMedico;
    String laudo;
    String urlAssinaturaDigital;
    String caminhoPDF;
    
    public criaPDFdoLaudo(String numero, String dataDoExame, String nomePaciente, String nomeMedico, String laudo) {
        this.numero = numero;
        this.dataDoExame = dataDoExame;
        this.nomePaciente = nomePaciente;
        this.nomeMedico = nomeMedico;
        this.laudo = laudo;
        this.caminhoPDF = criandoAPastaParaSalvarOArquivo();
    }
    
    private String criandoAPastaParaSalvarOArquivo() {
        String caminhoParaSalvarPDF;
        if (OSvalidator.isWindows()) {
            caminhoParaSalvarPDF = USUARIOS.pasta_raiz + "\\Laudos\\";
        } else {
            caminhoParaSalvarPDF = USUARIOS.pasta_raiz + "/Laudos/";
        }
        File dir = new File(caminhoParaSalvarPDF);
        dir.mkdirs();
        
        return caminhoParaSalvarPDF;
    }
    
    public void criarPDF() throws DocumentException, IOException {
        // step 1
        Document document = new Document(PageSize.A4, 0, 0, 0, 0);
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(caminhoPDF + numero + ".pdf"));
        // step 3
        document.open();
        PdfPTable table0 = new PdfPTable(4);
        table0.setWidths(new int[] { 30, 60, 30, 60 });
        table0.setWidthPercentage(100);

        PdfPCell cell;
        
        
        // logotipo
        try {
            String url = "http://" + janelaPrincipal.RISIP + "/" + janelaPrincipal.RISPORTAL + "/img/logo.png";
            Image img0 = Image.getInstance(url);
            img0.scalePercent(65f);
            img0.setAlignment(Element.ALIGN_CENTER);
            document.add(img0);
            cell = new PdfPCell(img0);
            cell.setBorder(Rectangle.NO_BORDER);
            table0.addCell(cell);
        } catch (Exception e) {
        }
        

        Font fontHeader = FontFactory.getFont("Helvetica", 11, Font.BOLD);
        Font fontBody = FontFactory.getFont("Helvetica", 11, Font.NORMAL);

        PdfPTable table = new PdfPTable(4);
        table.setWidths(new int[] { 20, 15, 5, 80 });
        table.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase(" "));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(" "));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(" "));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(" "));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" "));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Número", fontHeader));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(":", fontHeader));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(numero, fontBody));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" "));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Data", fontHeader));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(":", fontHeader));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(dataDoExame, fontBody));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" "));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Nome", fontHeader));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(":", fontHeader));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(nomePaciente, fontBody));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" "));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        
        if(!nomeMedico.equals("NAO INFORMADO")){
            cell = new PdfPCell(new Phrase("Médico", fontHeader));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(":", fontHeader));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nomeMedico, fontBody));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        }
        
        table.addCell(cell);
        document.add(table);

        PdfPTable table2 = new PdfPTable(3);
        table2.setWidths(new int[] { 10, 85, 5 });
        table2.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase(" "));
        cell.setBorder(Rectangle.NO_BORDER);
        table2.addCell(cell);
        cell = new PdfPCell(new Phrase(" "));
        cell.setBorder(Rectangle.NO_BORDER);
        table2.addCell(cell);
        cell = new PdfPCell(new Phrase(" "));
        cell.setBorder(Rectangle.NO_BORDER);
        table2.addCell(cell);

        String[] linhas = laudo.split("\\x0A");
        for (int kk = 0; kk < linhas.length; kk++) {
            if (checkforbold(linhas[kk])) {
                cell = new PdfPCell(new Phrase(" "));
                cell.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell);
                cell = new PdfPCell(new Phrase(linhas[kk], fontHeader));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                cell.setLeading(13f, 0f);
                table2.addCell(cell);
                cell = new PdfPCell(new Phrase(" "));
                cell.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase(" "));
                cell.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell);
                cell = new PdfPCell(new Phrase(linhas[kk], fontBody));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                cell.setLeading(13f, 0f);
                table2.addCell(cell);
                cell = new PdfPCell(new Phrase(" "));
                cell.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell);
            }
        }

        cell = new PdfPCell(new Phrase(" "));
        cell.setBorder(Rectangle.NO_BORDER);
        table2.addCell(cell);
        cell = new PdfPCell(new Phrase(" "));
        cell.setBorder(Rectangle.NO_BORDER);
        table2.addCell(cell);
        cell = new PdfPCell(new Phrase(" "));
        cell.setBorder(Rectangle.NO_BORDER);
        table2.addCell(cell);
        document.add(table2);

        
        try {
            String url = "http://" + janelaPrincipal.RISIP + "/" + janelaPrincipal.RISPORTAL + "/pags/jassinaturaadmclin.asp?handle_at=" + numero;
            Image img = Image.getInstance(url);
            img.scalePercent(70f);
            img.setAlignment(Element.ALIGN_CENTER);
            document.add(img);
        } catch (Exception e) {
        }
        

        document.close();
    }

    public void abrindoPDF() {
        try {
            Runtime runtime = Runtime.getRuntime();
            if (OSvalidator.isWindows()) {
                runtime.exec("cmd /c \"" + caminhoPDF + numero + ".pdf");
            } else if (OSvalidator.isMac()) {
                runtime.exec("open " + caminhoPDF + numero + ".pdf");
            } else {
                runtime.exec("gnome-open " + caminhoPDF + numero + ".pdf");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao Abrir Laudo. Procure o Administrador", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean checkforbold(String s) {

        boolean rtn = false;
        if (s.length() > 64)
            return false;
        for (int i = 0; i < s.length(); i++) {
            int c = (int) s.charAt(i);

            if (((c > 31 && c < 97) || (c > 191 && c < 221)))
                rtn = true;
            else {
                rtn = false;
                break;
            }
        }
        return rtn;
    }
}
