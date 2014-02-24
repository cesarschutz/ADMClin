/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.valoresrecebidos;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.OSvalidator;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.USUARIOS;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * 
 * @author Cesar Schutz
 */
public class relatorioValoresRecebidos {

    private Date dataInicial = null, dataFinal = null;
    private String dataInicialString, dataFinalString;
    private Connection con = null;

    public relatorioValoresRecebidos(Date dataInicial, Date dataFinal) {
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;

        // passando as datas para string
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        dataInicialString = format.format(dataInicial.getTime());
        dataFinalString = format.format(dataFinal.getTime());
    }

    String caminho;

    private void criandoAPastaParaSalvarOArquivo() {
        if (OSvalidator.isWindows()) {
            caminho = USUARIOS.pasta_raiz + "\\relatorioValoresRecebidos\\";
        } else {
            caminho = USUARIOS.pasta_raiz + "/relatorioValoresRecebidos/";
        }
        File dir = new File(caminho);
        dir.mkdirs();
    }

    public boolean gerarRelatorio() {
        try {
            con = Conexao.fazConexao();
            criandoAPastaParaSalvarOArquivo();
            consultarAtendimentos();
            criandoFatura();
            abrirFichaPDF();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameJanelaPrincipal,
                "Erro ao elaborar Relatório. Procure o Administrador." + e);
            return false;
        } finally {
            Conexao.fechaConexao(con);
        }
    }

    // metodo que busca os atendimentos de acordo com a classe
    public List<relatorioValoresRecebidossMODEL> listaDeAtendimentos;

    private void consultarAtendimentos() throws SQLException {
        listaDeAtendimentos = relatorioValoresRecebidosDAO.getConsultarAtendimentosTodosOsConvenios(dataInicial, dataFinal);
    }

    public void criandoFatura() throws FileNotFoundException, DocumentException {
        Rectangle rect = new Rectangle(PageSize.A4.rotate());
        Document document = new Document(rect, 20, 20, 20, 20); // colocar as margens
        PdfWriter.getInstance(document, new FileOutputStream(caminho + "relatorioDeValoresRecebidos.pdf"));
        document.open();

        Font fontNegrito11 = FontFactory.getFont("Calibri", 11, Font.BOLD);
        Font font9 = FontFactory.getFont("Calibri", 9, Font.NORMAL);
        Font fontNegrito8 = FontFactory.getFont("Calibri", 8, Font.BOLD);

        PdfPCell cell;

        // tabela de cabeçalho
        PdfPTable tablePrincipal = new PdfPTable(1);
        tablePrincipal.setWidths(new int[] { 100 });
        tablePrincipal.setWidthPercentage(100);

        // colocando o
        cell =
            new PdfPCell(new Phrase(
                "Relatório de Valores Recebidos", fontNegrito11));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);

        // colocando a data atual
        Calendar hoje1 = Calendar.getInstance();
        SimpleDateFormat format3 = new SimpleDateFormat("dd/MM/yyyy");
        String dataDeHoje2 = format3.format(hoje1.getTime());

        cell = new PdfPCell(new Phrase("Data: " + dataDeHoje2, fontNegrito11));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);

        // colocando o periodo
        cell = new PdfPCell(new Phrase("Período: " + dataInicialString + " à " + dataFinalString, fontNegrito11));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);

        // colocando linha em branco
        cell = new PdfPCell(new Phrase("", fontNegrito11));
        cell.setBorder(Rectangle.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);

        // adicionando tabela ao documento
        document.add(tablePrincipal);

        // adicionando tabela com o cabeçalho (informações das colunas)
        // tabela de cabeçalho
        PdfPTable tabelaCabecalho = new PdfPTable(9);
        tabelaCabecalho.setWidths(new int[] { 7, 6, 24, 23, 8, 8, 8, 8, 8 });
        tabelaCabecalho.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("DATA", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaCabecalho.addCell(cell);

        cell = new PdfPCell(new Phrase("CÓDIGO", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaCabecalho.addCell(cell);

        cell = new PdfPCell(new Phrase("PACIENTE", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaCabecalho.addCell(cell);

        cell = new PdfPCell(new Phrase("EXAME", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaCabecalho.addCell(cell);

        cell = new PdfPCell(new Phrase("VALOR PAC", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabelaCabecalho.addCell(cell);

        cell = new PdfPCell(new Phrase("VALOR FAT", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabelaCabecalho.addCell(cell);

        cell = new PdfPCell(new Phrase("VALOR CONV", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabelaCabecalho.addCell(cell);

        cell = new PdfPCell(new Phrase("DIFERENÇA", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabelaCabecalho.addCell(cell);

        cell = new PdfPCell(new Phrase("TOTAL", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabelaCabecalho.addCell(cell);

        cell = new PdfPCell(new Phrase("", fontNegrito8));
        cell.setBorder(Rectangle.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(11);
        tabelaCabecalho.addCell(cell);

        // adicionando tabela ao documento
        document.add(tabelaCabecalho);
        

        // fechando o documento
        document.close();

    }

    /*
     * Metodo que abri um arquivo pdf (que acamos de criar)
     */
    private void abrirFichaPDF() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        if (OSvalidator.isWindows()) {
            runtime.exec("cmd /c \"" + caminho
                + "relatorioDeValoresRecebidos.pdf");
            System.out.println(caminho + "relatorioDeValoresRecebidos.pdf");
        } else if (OSvalidator.isMac()) {
            runtime.exec("open " + caminho
                + "relatorioDeValoresRecebidos.pdf");
        } else {
            runtime.exec("gnome-open " + caminho
                + "relatorioDeValoresRecebidos.pdf");
        }
    }
}
