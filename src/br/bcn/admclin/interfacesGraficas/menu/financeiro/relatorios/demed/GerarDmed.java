/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.demed;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.OSvalidator;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

import com.lowagie.text.Chunk;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * 
 * @author Cesar Schutz
 */
public class GerarDmed {

    private List<GerarDmedMODEL> listaDmed = new ArrayList<GerarDmedMODEL>();
    private Connection con = null;
    int anoSelecionado = 0;

    String nomeArquivo;
    public GerarDmed(int anoSelecionado) {
        this.anoSelecionado = anoSelecionado;
        nomeArquivo = "demed_" + janelaPrincipal.getNumeroSequencialDoSistemaParaPDF() +anoSelecionado + ".pdf";
    }

    public void gerarDemed() {
        try {
            criandoAPastaParaSalvarOArquivo();
            buscandoInformacoes();
            gerandoPDF();
            abrindoPDF();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameJanelaPrincipal,
                "Erro ao gerar DMED. Procure o Administrador.");
        }
    }

    String caminho = "";

    private void criandoAPastaParaSalvarOArquivo() {
        if (OSvalidator.isWindows()) {
            caminho = USUARIOS.pasta_raiz + "\\Demed\\";
        } else {
            caminho = USUARIOS.pasta_raiz + "/Demed/";
        }
        File dir = new File(caminho);
        dir.mkdirs();
    }

    private void buscandoInformacoes() throws ParseException, SQLException {
        con = Conexao.fazConexao();
        java.sql.Date diaInicialSql = null, diaFinalSql = null;

        // data inicial
        String diaInicial = "01/01/" + anoSelecionado;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        diaInicialSql = new java.sql.Date(format.parse(diaInicial).getTime());

        // data final
        String diaFinal = "31/12/" + anoSelecionado;
        diaFinalSql = new java.sql.Date(format.parse(diaFinal).getTime());

        listaDmed.removeAll(listaDmed);
        ResultSet resultSet = GerarDmedDAO.getConsultarDmed(con, diaInicialSql, diaFinalSql);
        while (resultSet.next()) {
            GerarDmedMODEL atendimento = new GerarDmedMODEL();
            atendimento.setHandle_at(resultSet.getInt("handle_at"));
            atendimento.setData(resultSet.getDate("data_atendimento"));
            atendimento.setNomePaciente(resultSet.getString("nome"));
            atendimento.setNomeResponsavel(resultSet.getString("responsavel"));
            atendimento.setCpfPaciente(resultSet.getString("cpf"));
            atendimento.setCpfResponsavel(resultSet.getString("cpfresponsavel"));
            atendimento.setImpressoraUtilizadaImpressaoNota(resultSet.getString("IMPRESSORA_UTILIZADA_NOTA"));
            if(atendimento.getImpressoraUtilizadaImpressaoNota() == null){
            	atendimento.setImpressoraUtilizadaImpressaoNota("");
            }
           // JOptionPane.showMessageDialog(null, "handleAt: " + atendimento.getHandle_at() + "      impressora:" + atendimento.getImpressoraUtilizadaImpressaoNota());
            atendimento.setValorPago(Double.valueOf(resultSet.getString("valor_correto_paciente")));
            listaDmed.add(atendimento);
        }
        Conexao.fechaConexao(con);
    }
    
    

    private void gerandoPDF() throws FileNotFoundException, DocumentException {
        Document document = new Document(PageSize.A4, 20, 20, 20, 20); // colocar as margens
        PdfWriter.getInstance(document, new FileOutputStream(caminho + nomeArquivo));
        document.open();

        Font font8 = FontFactory.getFont("Calibri", 8, Font.NORMAL);
        Font fontBold8 = FontFactory.getFont("Calibri", 8, Font.BOLD);
        Font fontBold10 = FontFactory.getFont("Calibri", 10, Font.BOLD);
        
        PdfPCell cell;

        double valorTotalImpressora = 0.0;
        String ultimaImpressoraUtilizada = listaDmed.get(0).getImpressoraUtilizadaImpressaoNota();
        
        //colocando a primeira impressora utilizada
        PdfPTable tabelaPrimeiraImpressora = new PdfPTable(1);
        tabelaPrimeiraImpressora.setWidths(new int[] { 1 });
        tabelaPrimeiraImpressora.setWidthPercentage(100);
        
        if(ultimaImpressoraUtilizada == null || ultimaImpressoraUtilizada.length() < 9){
        	ultimaImpressoraUtilizada = "";
			cell = new PdfPCell(new Phrase("Impressora Utilizada: " + "Sem Impressão", fontBold10));
		}else{
			String nome[] = ultimaImpressoraUtilizada.split("\\\\");
			cell = new PdfPCell(new Phrase("Impressora Utilizada: " + nome[3], fontBold10));
		}
        
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaPrimeiraImpressora.addCell(cell);
        cell = new PdfPCell(new Phrase("", fontBold10));
        cell.setBorder(Rectangle.NO_BORDER);
        tabelaPrimeiraImpressora.addCell(cell);
        cell = new PdfPCell(new Phrase("", fontBold10));
        cell.setBorder(Rectangle.NO_BORDER);
        tabelaPrimeiraImpressora.addCell(cell);
        document.add(tabelaPrimeiraImpressora);
        
        //coloca o cabeçalho na pagina
        colocarCabecalhoNoDocumento(document);
        
        //tabela paara os atendimentos
        PdfPTable tableatendimentos = new PdfPTable(6);
        tableatendimentos.setWidths(new int[] { 8, 12, 30, 15, 28, 7 });
        tableatendimentos.setWidthPercentage(100);
        
        for (int i = 0; i < listaDmed.size(); i++) {
        	//se é a mesma impressora
        	if(!listaDmed.get(i).getImpressoraUtilizadaImpressaoNota().equals(ultimaImpressoraUtilizada)){
        		//coloca a tabela de atendimentos
        		document.add(tableatendimentos);
        		
        		//zera a tabela  de atendimentos para continuar depois
        		tableatendimentos = new PdfPTable(6);
        	    tableatendimentos.setWidths(new int[] { 8, 12, 30, 15, 28, 7 });
        	    tableatendimentos.setWidthPercentage(100);
        	    
        		//coloca o valor total
        		PdfPTable total = new PdfPTable(1);
        		total.setWidths(new int[] { 1 });	
        		total.setWidthPercentage(100);
        		
        		cell = new PdfPCell(new Phrase("Valor Total: " + String.valueOf(MetodosUteis.colocarZeroEmCampoReais(valorTotalImpressora).replace(".", ",")), fontBold8));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                total.addCell(cell);
                document.add(total);
                
                //zera o valor total da impressora
                valorTotalImpressora = 0.0;
        		
        		//atualiza a ultima impressora utilizada
        		ultimaImpressoraUtilizada = listaDmed.get(i).getImpressoraUtilizadaImpressaoNota(); 
        		
        		//quebra de pagina
        		document.add(Chunk.NEXTPAGE);  
        		
        		//imprime a impressora que esta sendo veriicada apartir de agora
        		PdfPTable tabelaImpressora = new PdfPTable(1);
        		tabelaImpressora.setWidths(new int[] { 1 });
        		tabelaImpressora.setWidthPercentage(100);
                
        		if(ultimaImpressoraUtilizada == null || ultimaImpressoraUtilizada == ""){
        			cell = new PdfPCell(new Phrase("Impressora Utilizada: " + "Sem Impressão", fontBold10));
        		}else{
        			String nome[] = ultimaImpressoraUtilizada.split("\\\\");
        			cell = new PdfPCell(new Phrase("Impressora Utilizada: " + nome[3], fontBold10));
        		}
        		
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                tabelaImpressora.addCell(cell);
                cell = new PdfPCell(new Phrase("", fontBold10));
                cell.setBorder(Rectangle.NO_BORDER);
                tabelaImpressora.addCell(cell);
                cell = new PdfPCell(new Phrase("", fontBold10));
                cell.setBorder(Rectangle.NO_BORDER);
                tabelaImpressora.addCell(cell);
                document.add(tabelaImpressora);
                
              //coloca o cabeçalho na pagina
                colocarCabecalhoNoDocumento(document);
        		
        	}
        	
        	//soma o valor total
        	valorTotalImpressora = valorTotalImpressora + listaDmed.get(i).getValorPago();
        	
        	
            // colocando a data
            DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            String dataCerta = fmt.format(listaDmed.get(i).getData());

            cell = new PdfPCell(new Phrase(dataCerta, font8));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            tableatendimentos.addCell(cell);

            // colocando a cpf paciente
            cell = new PdfPCell(new Phrase(listaDmed.get(i).getCpfPaciente(), font8));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            tableatendimentos.addCell(cell);

            // colocando a nome paciente
            cell = new PdfPCell(new Phrase(listaDmed.get(i).getNomePaciente(), font8));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            tableatendimentos.addCell(cell);

            // colocando a cpf responsavel
            cell = new PdfPCell(new Phrase(listaDmed.get(i).getCpfResponsavel(), font8));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            tableatendimentos.addCell(cell);

            // colocando a nome responsavel
            cell = new PdfPCell(new Phrase(listaDmed.get(i).getNomeResponsavel(), font8));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            tableatendimentos.addCell(cell);

            // colocando a valor
            cell =
                new PdfPCell(new Phrase(String.valueOf(MetodosUteis.colocarZeroEmCampoReais(
                    listaDmed.get(i).getValorPago()).replace(".", ",")), font8));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            tableatendimentos.addCell(cell);

        }

        document.add(tableatendimentos);
        
      //coloca o valor total
		PdfPTable total = new PdfPTable(1);
		total.setWidths(new int[] { 1 });	
		total.setWidthPercentage(100);
		
		cell = new PdfPCell(new Phrase("Valor Total: " + String.valueOf(MetodosUteis.colocarZeroEmCampoReais(valorTotalImpressora).replace(".", ",")), fontBold8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        total.addCell(cell);
        document.add(total);

        document.close();
    }
    
    private void colocarCabecalhoNoDocumento(Document document) throws DocumentException{
    	Font fontBold8 = FontFactory.getFont("Calibri", 8, Font.BOLD);
        PdfPCell cell;

        // tabela de cabeçalho
        PdfPTable tablePrincipal = new PdfPTable(6);
        tablePrincipal.setWidths(new int[] { 8, 12, 30, 15, 28, 7 });
        tablePrincipal.setWidthPercentage(100);

        // colocando a data
        cell = new PdfPCell(new Phrase("DATA", fontBold8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);

        // colocando a data
        cell = new PdfPCell(new Phrase("CPF PACIENTE", fontBold8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tablePrincipal.addCell(cell);

        // colocando a data
        cell = new PdfPCell(new Phrase("NOME PACIENTE", fontBold8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);

        // colocando a data
        cell = new PdfPCell(new Phrase("CPF RESPONSÁVEL", fontBold8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tablePrincipal.addCell(cell);

        // colocando a data
        cell = new PdfPCell(new Phrase("NOME RESPONSÁVEL", fontBold8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);

        // colocando a data
        cell = new PdfPCell(new Phrase("VALOR", fontBold8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tablePrincipal.addCell(cell);

        // linha em branco com borda
        cell = new PdfPCell(new Phrase("", fontBold8));
        cell.setBorder(Rectangle.ALIGN_BOTTOM);
        cell.setColspan(6);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);

        document.add(tablePrincipal);
    }

    private void abrindoPDF() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        if (OSvalidator.isWindows()) {
            runtime.exec("cmd /c \"" + caminho + nomeArquivo);
        } else if (OSvalidator.isMac()) {
            runtime.exec("open " + caminho + nomeArquivo);
        } else {
            runtime.exec("gnome-open " + caminho + nomeArquivo);
        }
    }
}
