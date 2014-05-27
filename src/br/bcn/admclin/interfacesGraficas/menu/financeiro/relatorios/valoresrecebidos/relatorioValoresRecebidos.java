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
    private int handle_convenio;
    private String nomeConvenio;

    public relatorioValoresRecebidos(Date dataInicial, Date dataFinal, String nomeConvenio, int handle_convenio) {
        this.nomeConvenio = nomeConvenio;
        this.handle_convenio = handle_convenio;
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
            JOptionPane.showMessageDialog(
                br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameJanelaPrincipal,
                "Erro ao elaborar Relatório. Procure o Administrador." + e);
            return false;
        } finally {
            Conexao.fechaConexao(con);
        }
    }

    // metodo que busca os atendimentos de acordo com a classe
    public List<relatorioValoresRecebidossMODEL> listaDeAtendimentos;

    private void consultarAtendimentos() throws SQLException {
        listaDeAtendimentos =
            relatorioValoresRecebidosDAO.getConsultarAtendimentosTodosOsConvenios(dataInicial, dataFinal,
                handle_convenio);
    }

    public void criandoFatura() throws FileNotFoundException, DocumentException {
        Rectangle rect = new Rectangle(PageSize.A4.rotate());
        Document document = new Document(rect, 20, 20, 20, 20); // colocar as margens
        PdfWriter.getInstance(document, new FileOutputStream(caminho + "relatorioDeValoresRecebidos.pdf"));
        document.open();

        Font fontNegrito11 = FontFactory.getFont("Calibri", 11, Font.BOLD);
        Font fontNegrito9 = FontFactory.getFont("Calibri", 9, Font.BOLD);
        Font fontNegrito8 = FontFactory.getFont("Calibri", 8, Font.BOLD);
        Font font8 = FontFactory.getFont("Calibri", 8, Font.NORMAL);

        PdfPCell cell;

        // tabela de cabeçalho
        PdfPTable tablePrincipal = new PdfPTable(1);
        tablePrincipal.setWidths(new int[] { 100 });
        tablePrincipal.setWidthPercentage(100);

        // colocando o
        cell = new PdfPCell(new Phrase("Relatório de Valores Recebidos", fontNegrito11));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Convênio: " + nomeConvenio, fontNegrito11));
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

        // colocando os exames
        // varre a lista
        double totalValorPac = 0;
        double totalValorFat = 0;
        double totalValorConv = 0;
        double totalValorDif = 0;
        double totalValorTotal = 0;
        for (int i = 0; i < listaDeAtendimentos.size(); i++) {
            PdfPTable tabelaExames = new PdfPTable(9);
            tabelaExames.setWidths(new int[] { 7, 6, 24, 23, 8, 8, 8, 8, 8 });
            tabelaExames.setWidthPercentage(100);

            //data
            cell = new PdfPCell(new Phrase(listaDeAtendimentos.get(i).getData(), font8));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            tabelaExames.addCell(cell);

            //handle_at
            cell = new PdfPCell(new Phrase(String.valueOf(listaDeAtendimentos.get(i).getCodigo()), font8));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            tabelaExames.addCell(cell);

            //paciente
            cell = new PdfPCell(new Phrase(listaDeAtendimentos.get(i).getPaciente(), font8));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            tabelaExames.addCell(cell);

            //exame
            cell = new PdfPCell(new Phrase(listaDeAtendimentos.get(i).getExame(), font8));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            tabelaExames.addCell(cell);

            //valor paciente
            double valorPacienteDouble = Double.valueOf(listaDeAtendimentos.get(i).getValorPaciente());
            totalValorPac += valorPacienteDouble;
            String valorPacienteString = String.valueOf(MetodosUteis.colocarZeroEmCampoReais(valorPacienteDouble)).replace(".", ",");
            
            cell = new PdfPCell(new Phrase(valorPacienteString, font8));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            tabelaExames.addCell(cell);

            //valor faturado
            double valorFaturadoDouble = Double.valueOf(listaDeAtendimentos.get(i).getValorFaturado());
            totalValorFat += valorFaturadoDouble;
            String valorFaturadoString = String.valueOf(MetodosUteis.colocarZeroEmCampoReais(valorFaturadoDouble)).replace(".", ",");
            
            cell = new PdfPCell(new Phrase(valorFaturadoString, font8));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            tabelaExames.addCell(cell);

            //valor convenio pagou
            double valorConvenioPagoDouble = Double.valueOf(listaDeAtendimentos.get(i).getValorPagoConvenio());
            totalValorConv += valorConvenioPagoDouble;
            String valorConvenioString = String.valueOf(MetodosUteis.colocarZeroEmCampoReais(valorConvenioPagoDouble)).replace(".", ",");
            //se for 0 retira o valor
            if(valorConvenioPagoDouble == 0){
                valorConvenioString = "-";
            }
            
            cell = new PdfPCell(new Phrase(valorConvenioString, font8));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            tabelaExames.addCell(cell);

            //diferença            
            double valorDiferencaDouble = valorConvenioPagoDouble - valorFaturadoDouble;
            
            String valorDiferencaString = String.valueOf(MetodosUteis.colocarZeroEmCampoReais(valorDiferencaDouble)).replace(".", ",");
            //se convenio ainda nao pagou, a diferença fica vazia
            if(valorConvenioPagoDouble == 0){
                valorDiferencaString = "-";
            } else{
                //só soma a diferena se o convenio tiver pago algo
                //se nao diminui 0,00 e o total da diferença fica errado
                totalValorDif = totalValorDif + (valorDiferencaDouble);
            }
                        
            cell = new PdfPCell(new Phrase(valorDiferencaString, font8));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            tabelaExames.addCell(cell);
            
            //total
            double valorTotalDouble = valorPacienteDouble + valorConvenioPagoDouble;
            totalValorTotal += valorTotalDouble;
            String valorTotalString = String.valueOf(MetodosUteis.colocarZeroEmCampoReais(valorTotalDouble)).replace(".", ",");
            
            cell = new PdfPCell(new Phrase(valorTotalString, font8));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            tabelaExames.addCell(cell);
            
            document.add(tabelaExames);
        }
        
        //colocando linha em branco
        PdfPTable tabelaLinhaEmBranco = new PdfPTable(1);
        tabelaLinhaEmBranco.setWidths(new int[] { 100 });
        tabelaLinhaEmBranco.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("", fontNegrito8));
        cell.setBorder(Rectangle.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaLinhaEmBranco.addCell(cell);
        
        document.add(tabelaLinhaEmBranco);
        
        
        //agora colocando os totais
        PdfPTable tabelaTotais = new PdfPTable(9);
        tabelaTotais.setWidths(new int[] { 7, 6, 24, 23, 8, 8, 8, 8, 8 });
        tabelaTotais.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaTotais.addCell(cell);

        cell = new PdfPCell(new Phrase("", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaTotais.addCell(cell);

        cell = new PdfPCell(new Phrase("", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaTotais.addCell(cell);

        cell = new PdfPCell(new Phrase("Totais:", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabelaTotais.addCell(cell);

        //total pac
        String valorTotalPacienteString = String.valueOf(MetodosUteis.colocarZeroEmCampoReais(totalValorPac)).replace(".", ",");
        cell = new PdfPCell(new Phrase(valorTotalPacienteString, fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabelaTotais.addCell(cell);

        //total FAT
        String valorTotalFaturadoString = String.valueOf(MetodosUteis.colocarZeroEmCampoReais(totalValorFat)).replace(".", ",");
        cell = new PdfPCell(new Phrase(valorTotalFaturadoString, fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabelaTotais.addCell(cell);

        //total convenio
        String valorTotalConvenioString = String.valueOf(MetodosUteis.colocarZeroEmCampoReais(totalValorConv)).replace(".", ",");
        cell = new PdfPCell(new Phrase(valorTotalConvenioString, fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabelaTotais.addCell(cell);

        //total diferença
        String valorTotalDiferencaString = String.valueOf(MetodosUteis.colocarZeroEmCampoReais(totalValorDif)).replace(".", ",");
        cell = new PdfPCell(new Phrase(valorTotalDiferencaString, fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabelaTotais.addCell(cell);

        //total total
        String valorTotalTotalString = String.valueOf(MetodosUteis.colocarZeroEmCampoReais(totalValorTotal)).replace(".", ",");
        cell = new PdfPCell(new Phrase(valorTotalTotalString, fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabelaTotais.addCell(cell);
        
        document.add(tabelaTotais);

        // fechando o documento
        document.close();

    }

    /*
     * Metodo que abri um arquivo pdf (que acamos de criar)
     */
    private void abrirFichaPDF() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        if (OSvalidator.isWindows()) {
            runtime.exec("cmd /c \"" + caminho + "relatorioDeValoresRecebidos.pdf");
        } else if (OSvalidator.isMac()) {
            runtime.exec("open " + caminho + "relatorioDeValoresRecebidos.pdf");
        } else {
            runtime.exec("gnome-open " + caminho + "relatorioDeValoresRecebidos.pdf");
        }
    }
}
