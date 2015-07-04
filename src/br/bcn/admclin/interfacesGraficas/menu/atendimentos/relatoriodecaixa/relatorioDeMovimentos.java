package br.bcn.admclin.interfacesGraficas.menu.atendimentos.relatoriodecaixa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;

import br.bcn.admclin.ClasseAuxiliares.OSvalidator;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

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

public class relatorioDeMovimentos {
	
	private double valorSomadoPaciente = 0;
	private double valorSomadoConvenio = 0;
	private double valorSomadoTotal = 0;

	private Date dataInicial = null, dataFinal = null;
	private String dataInicialString, dataFinalString;
	private Connection con = null;

	public relatorioDeMovimentos(Date dataInicial, Date dataFinal) {
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
			caminho = USUARIOS.pasta_raiz + "\\relatorioDeCaixa\\";
		} else {
			caminho = USUARIOS.pasta_raiz + "/relatorioDeCaixa/";
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
			JOptionPane
					.showMessageDialog(
							br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameJanelaPrincipal,
							"Erro ao elaborar Relatório de Caixa. Procure o Administrador.");
			return false;
		} finally {
			Conexao.fechaConexao(con);
		}
	}

	// metodo que busca os atendimentos de acordo com a classe
	public List<Model> listaDeAtendimentos = new ArrayList<Model>();

	private void consultarAtendimentos() throws SQLException {
		listaDeAtendimentos.removeAll(listaDeAtendimentos);
		listaDeAtendimentos = DAOrelatorioDeMovimentos.getConsultarAtendimentos(
				dataInicial, dataFinal);
	}

	String nomeArquivo = "Relatorio_de_movimento_" + janelaPrincipal.getNumeroSequencialDoSistemaParaPDF() + ".pdf";
	
	public void criandoFatura() throws FileNotFoundException, DocumentException {
		Rectangle rect = new Rectangle(PageSize.A4.rotate());
		Document document = new Document(rect, 20, 20, 20, 20); // colocar as
																// margens
		PdfWriter.getInstance(document, new FileOutputStream(caminho
				+ nomeArquivo));
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
		cell = new PdfPCell(new Phrase("Relatório de Movimento", fontNegrito11));
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
		cell = new PdfPCell(new Phrase("Período: " + dataInicialString + " à "
				+ dataFinalString, fontNegrito11));
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
		PdfPTable tabelaCabecalho = new PdfPTable(8);
		tabelaCabecalho.setWidths(new int[] { 7, 6, 28, 14, 10, 11, 8, 16 });
		tabelaCabecalho.setWidthPercentage(100);

		cell = new PdfPCell(new Phrase("DATA", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaCabecalho.addCell(cell);

		cell = new PdfPCell(new Phrase("FICHA", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaCabecalho.addCell(cell);

		cell = new PdfPCell(new Phrase("PACIENTE", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaCabecalho.addCell(cell);

		cell = new PdfPCell(new Phrase("CONVÊNIO", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaCabecalho.addCell(cell);

		cell = new PdfPCell(new Phrase("VALOR PACIENTE", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaCabecalho.addCell(cell);

		cell = new PdfPCell(new Phrase("VALOR CONVÊNIO", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tabelaCabecalho.addCell(cell);

		cell = new PdfPCell(new Phrase("VALOR TOTAL", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tabelaCabecalho.addCell(cell);

		cell = new PdfPCell(new Phrase("OPERADOR", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tabelaCabecalho.addCell(cell);

		cell = new PdfPCell(new Phrase("", fontNegrito8));
		cell.setBorder(Rectangle.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setColspan(8);
		tabelaCabecalho.addCell(cell);

		document.add(tabelaCabecalho);
		
		//coloca cada atendimento na tabela
		int ultimoHanleAtEnviado = 0;
		for (Model model : listaDeAtendimentos) {
			if(ultimoHanleAtEnviado != model.getHandle_at()){
				ultimoHanleAtEnviado = model.getHandle_at();
				document.add(criaRegistro(model.getHandle_at()));
			}
		}
		
		PdfPTable tabelaFinal = new PdfPTable(1);
		tabelaFinal.setWidths(new int[] { 100 });
		tabelaFinal.setWidthPercentage(100);
		
		//colocando o final do relatorio
		cell = new PdfPCell(new Phrase("", fontNegrito8));
		cell.setBorder(Rectangle.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaFinal.addCell(cell);
		
		cell = new PdfPCell(new Phrase("", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaFinal.addCell(cell);
		
		cell = new PdfPCell(new Phrase("", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaFinal.addCell(cell);
		
		cell = new PdfPCell(new Phrase("", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaFinal.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Valor Total Paciente: " + converterDoubleString(valorSomadoPaciente), fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaFinal.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Valor Total Convênio: " + converterDoubleString(valorSomadoConvenio), fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaFinal.addCell(cell);
		
		cell = new PdfPCell(new Phrase("", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaFinal.addCell(cell);
		
		cell = new PdfPCell(new Phrase("", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaFinal.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Valor Total: " + converterDoubleString(valorSomadoTotal), fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaFinal.addCell(cell);
		
		cell = new PdfPCell(new Phrase("", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaFinal.addCell(cell);
		
		cell = new PdfPCell(new Phrase("", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaFinal.addCell(cell);
		
		cell = new PdfPCell(new Phrase("", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaFinal.addCell(cell);
		
		cell = new PdfPCell(new Phrase("", fontNegrito8));
		cell.setBorder(Rectangle.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaFinal.addCell(cell);
		
		document.add(tabelaFinal);

		// fechando o documento
		document.close();

	}

	private PdfPTable criaRegistro(int handle_at) throws DocumentException {
		
		Model atendimento = null;
		
		for (Model model : listaDeAtendimentos) {
			if(model.getHandle_at() == handle_at){
				atendimento = model;
				break;
			}
		}
		
		

		PdfPCell cell;

		Font fontNegrito8 = FontFactory.getFont("Calibri", 9, Font.NORMAL);

		PdfPTable tabelaCabecalho = new PdfPTable(8);
		tabelaCabecalho.setWidths(new int[] { 7, 6, 28, 14, 10, 11, 8, 16 });
		tabelaCabecalho.setWidthPercentage(100);
		
		DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        String dataCerta = fmt.format(atendimento.getData());

		cell = new PdfPCell(new Phrase(dataCerta, fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaCabecalho.addCell(cell);

		cell = new PdfPCell(new Phrase(String.valueOf(atendimento.getHandle_at()), fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaCabecalho.addCell(cell);

		cell = new PdfPCell(new Phrase(atendimento.getPaciente(), fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaCabecalho.addCell(cell);

		cell = new PdfPCell(new Phrase(atendimento.getConvenio(), fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabelaCabecalho.addCell(cell);

		cell = new PdfPCell(new Phrase(calculaValorTotalPaciente(handle_at), fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tabelaCabecalho.addCell(cell);

		cell = new PdfPCell(new Phrase(calculaValorTotalConvenio(handle_at), fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tabelaCabecalho.addCell(cell);

		cell = new PdfPCell(new Phrase(calculaValorTotalAtendimento(handle_at), fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tabelaCabecalho.addCell(cell);

		cell = new PdfPCell(new Phrase(atendimento.getUsuario(), fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tabelaCabecalho.addCell(cell);

		return tabelaCabecalho;
	}

	private String calculaValorTotalConvenio(int handle_at) {
		double valorTotalConvenio = 0;
		for (Model model : listaDeAtendimentos) {
			if(model.getHandle_at() == handle_at){
				valorTotalConvenio = valorTotalConvenio + Double.valueOf(model.getValor_convenio());
			}
		}
		valorSomadoConvenio = valorSomadoConvenio + valorTotalConvenio;
		return converterDoubleString(valorTotalConvenio);
	}

	private String calculaValorTotalPaciente(int handle_at) {
		double valorTotalPaciente = 0;
		for (Model model : listaDeAtendimentos) {
			if(model.getHandle_at() == handle_at){
				valorTotalPaciente = valorTotalPaciente + Double.valueOf(model.getValor_paciente());
			}
		}
		valorSomadoPaciente = valorSomadoPaciente + valorTotalPaciente;
		
		return converterDoubleString(valorTotalPaciente);
	}

	private String calculaValorTotalAtendimento(int handle_at) {
		double valorTotalConvenio = 0;
		for (Model model : listaDeAtendimentos) {
			if(model.getHandle_at() == handle_at){
				valorTotalConvenio = valorTotalConvenio + Double.valueOf(model.getValor_convenio());
			}
		}
		
		double valorTotalPaciente = 0;
		for (Model model : listaDeAtendimentos) {
			if(model.getHandle_at() == handle_at){
				valorTotalPaciente = valorTotalPaciente + Double.valueOf(model.getValor_paciente());
			}
		}
		
		double valorTotalAtendimento = valorTotalConvenio + valorTotalPaciente;
		
		valorSomadoTotal = valorSomadoTotal + valorTotalAtendimento;
		return converterDoubleString(valorTotalAtendimento);
	}

	private String converterDoubleString(double precoDouble) {  
	    /*Transformando um double em 2 casas decimais*/  
	    BigDecimal valor = new BigDecimal(precoDouble);   //limita o número de casas decimais      
	    NumberFormat nf = NumberFormat.getCurrencyInstance();   
	    return nf.format (valor);  
	}
	
	/*
	 * Metodo que abri um arquivo pdf (que acamos de criar)
	 */
	private void abrirFichaPDF() throws IOException {
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
