/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.financeiro.faturarConvenio.arquivoTxtDoIpe.gerarLaudos;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JOptionPane;

/**
 *
 * @author BCN
 */
public class CriandoLaudoPDFModeloTres {

    private static OutputStream out;

    public static String dateFormat(String data) {
        String[] dataf;
        dataf = data.split("\\/");
        if (dataf.length == 3) {
            data = dataf[2] + "/" + dataf[1] + "/20" + dataf[0];
        }
        return data;
    }

    public static void criarPDF(String caminhoParaSalvarPDF, String numero, String dataDoExame, String nomePaciente, String nomeMedico, String laudo, String urlDaAssinatura, String numeroMaxDeLinhas, boolean colocarAssinatura, int fontSize) throws DocumentException, IOException {
        
        Document document = new Document(PageSize.A4, 0, 0, 0, 0);
        // step 2
        PdfWriter write = PdfWriter.getInstance(document, new FileOutputStream(caminhoParaSalvarPDF));
        //criamos o rodape em todas as paginas
        LaudoPDFModeloTresRodape event = new LaudoPDFModeloTresRodape();
        write.setPageEvent(new LaudoPDFModeloTresRodape());
        // step 3
        document.open();

        //criamos o cabeçalho
        criaCabecalho(document, numero, dataDoExame, nomePaciente, nomeMedico, fontSize);
        //celula
        PdfPCell cell;

        //definimos as fontes
        Font fontHeader = FontFactory.getFont("Helvetica", fontSize, Font.BOLD);
        Font fontBody = FontFactory.getFont("Helvetica", fontSize, Font.NORMAL);

        //comaçamos a imprimir o laudo
        PdfPTable table2 = new PdfPTable(3);
        table2.setWidths(new int[]{10, 85, 5});
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
        document.add(table2);

        int lines = getLinesCount(laudo);
        String[] linhas = laudo.split("\\x0A");
        int linhaAtual = 0;
        int numeroMaxDeLinhasInt = Integer.valueOf(numeroMaxDeLinhas);
        //System.out.println("Total de linhas "+numeroMaxDeLinhasInt);
        
        //Para evitar um erro na ultima linha durante a geracao do pdf
        if (linhas[linhas.length-1].trim().equals(".")) {
            linhas[linhas.length-1] = "";
        }
        int offset = 2;
        
        for (int kk = 0; kk < linhas.length; kk++) {
            if (linhas[kk].length() > 100) offset += linhas[kk].length()/100;
            //System.out.println(kk+" <- kk | linhaAtual "+linhaAtual+" numeroMaximoLinhas "+numeroMaxDeLinhasInt + " Lenght "+ linhas[kk].length()+" Offset "+offset);
            //caso tenha dado o maximo de linhas, criamos uma nova pagina
            if (linhaAtual + offset > (numeroMaxDeLinhasInt) || linhas[kk].trim().equals(".")) {
                offset = 0;
                //System.out.println("linhaAtual "+linhaAtual);
                //se linha tem ponto, quebra a pagina e soma um no kk para nao imprimir o .
                if(linhas[kk].trim().equals(".") && linhaAtual < (numeroMaxDeLinhasInt - 1)){
                    kk++;
                }
                document.add(table2);
                
                //como ira trocar de pagina
                //coloca a assinatura antes de quebrar pagina
                //colocarAssinatura(document);
                
                //zeramos a tabela
                //para retirar o texto q já foi colocado
                table2 = new PdfPTable(3);
                table2.setWidths(new int[]{10, 85, 5});
                table2.setWidthPercentage(100);
                //cria linha em branco
                cell = new PdfPCell(new Phrase(" "));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(3);
                table2.addCell(cell);
                //criamos nova pagina no documento
                document.newPage();
                criaCabecalho(document, numero, dataDoExame, nomePaciente, nomeMedico, fontSize);
                //criaCabecalho(margemEsquerdaHeader, nomePaciente, nomeMedico, dataFormated);
                linhaAtual = 0;
            }

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
            linhaAtual++;
        }
        document.add(table2);

        //colocamos linhas em branco
        table2 = new PdfPTable(3);
        table2.setWidths(new int[]{10, 85, 5});
        table2.setWidthPercentage(100);
        //cria linha em branco
        for (int i = 0; i < 2; i++) {
            cell = new PdfPCell(new Phrase(" "));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(3);
            table2.addCell(cell);
        }
        document.add(table2);
        

        if(colocarAssinatura){
            //colocando a assinatura
            colocarAssinatura(document);
        }
        

        //fecha o documento
        document.close();
        
        //abrindoPDF(caminhoParaSalvarPDF);
    }
    
    
    private static void colocarAssinatura(Document documento){
        try {
                String urlDaAssinatura = "http://" + MakeLaudoPdf.domain + "/" + MakeLaudoPdf.portalname + "/pags/jassinatura.asp?estudo=" + MakeLaudoPdf.accnum;

                Image img = Image.getInstance(urlDaAssinatura);
                img.scalePercent(50f);
                img.setAlignment(Element.ALIGN_CENTER);
                documento.add(img);
            } catch (Exception e) {
            }
    }
    private static void criaCabecalho(Document documento, String numero, String dataFormated, String nomePaciente, String nomeMedico, int fontSize) throws DocumentException {
        PdfPCell cell;

        Font fontHeader = FontFactory.getFont("Helvetica", fontSize, Font.BOLD);
        Font fontBody = FontFactory.getFont("Helvetica", fontSize, Font.NORMAL);

        int fsize = 11;
        PdfPTable table = new PdfPTable(4);
        table.setWidths(new int[]{20, 15, 5, 80});
        table.setWidthPercentage(100);

        for (int i = 0; i < 9; i++) {
            cell = new PdfPCell(new Phrase(" "));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(4);
            table.addCell(cell);
        }

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
        cell = new PdfPCell(new Phrase(dataFormated, fontBody));
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
        table.addCell(cell);
        documento.add(table);
    }

    public static void abrindoPDF(String caminhoDoArquivo) {
        try {
            Runtime runtime = Runtime.getRuntime();
            if (OSvalidator.isWindows()) {
                runtime.exec("cmd /c \"" + caminhoDoArquivo + "\"");
            } else if (OSvalidator.isMac()) {
                runtime.exec("open " + caminhoDoArquivo);
            } else {
                runtime.exec("gnome-open " + caminhoDoArquivo);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao Carregar Laudo!", "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }

    public static boolean checkforbold(String s) {

        boolean rtn = false;
        if (s.length() > 64) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            int c = (int) s.charAt(i);

            if (((c > 31 && c < 97) || (c > 191 && c < 221))) {
                rtn = true;
            } else {
                rtn = false;
                break;
            }
        }
        return rtn;
    }

    public static int getLinesCount(String texto) {
        int n = 0, nc = 0;
        for (int i = 0; i < texto.length(); i++) {
            int c = (int) texto.charAt(i);
            //System.out.print(c+ " ");
            nc += 1;
            if (nc > 80) {
                nc = 0;
                //System.out.println("");
            }
        }
        return n;
    }
}
