/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.faturarConvenio;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.OSvalidator;
import br.bcn.admclin.dao.db.JLAUDOS;
import br.bcn.admclin.dao.dbris.CONVENIO;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;
import br.bcn.admclin.interfacesGraficas.menu.atendimentos.fichasDeAtendimentos.criaPDFdoLaudo;

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
public class faturaConvenio {

	// variaveis que vamos utilizar para a construção do pdf
	private Connection con = null;
	private int handle_convenio = 0, grupo_id = 0;
	private String nome = null, tipo = null;
	private Date dataInicial = null, dataFinal = null;
	public List<atendimentoModel> listaDeAtendimentos = new ArrayList<atendimentoModel>();
	boolean geraLaudos;
	private double valorTotal1RxGeral;
	private double valorTotal2TomografiaComputadorizada;
	private double valorTotal3Ecografia;
	private double valorTotal4Mamografia;
	private double valorTotal5EcoMamaria;
	private double valorTotal6Ressonancia;
	private double valorTotal7Densitometria;
	private double valorTotal8EcoDoppler;
	private double valorTotal9ExamesComplexos;
	
	String nomeArquivo;

	// convenio
	public faturaConvenio(String tp, Date dataInicial, Date dataFinal,
			String nome, int handle_convenio,
			List<atendimentoModel> listaAtendimentos, boolean GeraLaudos) {
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
		this.nome = nome.replace(" ", "_");
		nomeArquivo = this.nome + "_" + janelaPrincipal.getNumeroSequencialDoSistemaParaPDF() + ".pdf";
		this.handle_convenio = handle_convenio;
		this.tipo = tp;
		this.listaDeAtendimentos = listaAtendimentos;
		this.geraLaudos = GeraLaudos;
	}

	// grupo
	public faturaConvenio(String tp, Date dataInicial, Date dataFinal,
			String nome, int grupo_id, int x,
			List<atendimentoModel> listaAtendimentos, boolean GeraLaudos) {
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
		this.nome = "Grupo" + nome.replace(" ", "_");
		nomeArquivo = this.nome + "_" + janelaPrincipal.getNumeroSequencialDoSistemaParaPDF() + ".pdf";
		this.grupo_id = grupo_id;
		this.tipo = tp;
		this.listaDeAtendimentos = listaAtendimentos;
		this.geraLaudos = GeraLaudos;
	}

	String caminho = "";

	private void criandoAPastaParaSalvarOArquivo() {
		if (OSvalidator.isWindows()) {
			if (tipo.equals("grupo")) {
				caminho = USUARIOS.pasta_raiz
						+ "\\FaturasDeConveniosPorGrupo\\" + nome + "\\";
			} else {
				caminho = USUARIOS.pasta_raiz + "\\FaturasDeConvenios\\" + nome
						+ "\\";
			}

		} else {
			if (tipo.equals("grupo")) {
				caminho = USUARIOS.pasta_raiz + "/FaturasDeConveniosPorGrupo/"
						+ nome + "/";
			} else {
				caminho = USUARIOS.pasta_raiz + "/FaturasDeConvenios/" + nome
						+ "/";
			}
		}
		File dir = new File(caminho);
		dir.mkdirs();
	}

	private int numero_fatura;

	private void buscaNumeroFatura() throws SQLException {
		if (tipo.equals("grupo")) {
			con = Conexao.fazConexao();
			ResultSet rs = CONVENIO.getConsultarDadosDeUmGrupo(con, grupo_id);
			while (rs.next()) {
				numero_fatura = rs.getInt("numero_fatura");
				if (numero_fatura == 0)
					numero_fatura++;
			}
		} else {
			// se for convenio
			con = Conexao.fazConexao();
			ResultSet rs = CONVENIO.getConsultarDadosDeUmConvenio(con,
					handle_convenio);
			while (rs.next()) {
				numero_fatura = rs.getInt("numero_fatura");
				if (numero_fatura == 0)
					numero_fatura++;
			}
		}
	}

	private void validarNumeroFatura() {
		int resposta = JOptionPane.showConfirmDialog(null,
				"Deseja validar essa fatura?", "ATENÇÃO", 0);
		if (resposta == JOptionPane.YES_OPTION) {
			// aqui atualizamos o numero d fatura
			if (tipo.equals("grupo")) {
				boolean salvou = false;
				while (!salvou) {
					con = Conexao.fazConexao();
					salvou = CONVENIO.setUpdateNumeroFaturoGrupoConvenios(con,
							numero_fatura + 1, grupo_id);
				}
			} else {
				boolean salvou = false;
				while (!salvou) {
					con = Conexao.fazConexao();
					salvou = CONVENIO.setUpdateNumeroFaturaConvenio(con,
							numero_fatura + 1, handle_convenio);
				}
			}
			// aqui marcamos o flag faturado dos atendimentos
			for (atendimentoModel atendimento : listaDeAtendimentos) {
				con = Conexao.fazConexao();
				atendimentoDAO.setAtualizarFlagFaturado(con,
						atendimento.getHandle_at(), 1);
			}

		}
	}

	public boolean gerarFatura() {
		try {
			con = Conexao.fazConexao();
			criandoAPastaParaSalvarOArquivo();
			buscaNumeroFatura();
			criandoAFatura();
			validarNumeroFatura();
			abrirFichaPDF();
			if (geraLaudos) {
				//gerarLaudos();
			}
			return true;
		} catch (Exception e) {
			JOptionPane
					.showMessageDialog(
							br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameJanelaPrincipal,
							"Erro ao gerar Fatura. Procure o Administrador.");
			return false;
		} finally {
			Conexao.fechaConexao(con);
		}

	}
	
	

	// metodo que cria a fatura
	private void criandoAFatura() throws DocumentException,
			FileNotFoundException, SQLException {
		double valorTotalConvenio = 0;
		Rectangle rect = new Rectangle(PageSize.A4);
		Document document = new Document(rect, 20, 20, 20, 20); // colocar as
																// margens
		PdfWriter.getInstance(document, new FileOutputStream(caminho + nomeArquivo));
		document.open();

		// fontes utilizadas
		Font fontNegrito15 = FontFactory.getFont("Calibri", 15, Font.BOLD);
		Font fontNegrito11 = FontFactory.getFont("Calibri", 11, Font.BOLD);
		Font font11 = FontFactory.getFont("Calibri", 11, Font.NORMAL);

		Font font8 = FontFactory.getFont("Calibri", 8, Font.NORMAL);
		Font font7 = FontFactory.getFont("Calibri", 7, Font.NORMAL);
		Font fontNegrito8 = FontFactory.getFont("Calibri", 8, Font.BOLD);

		PdfPCell cell;

		// tabela de cabeçalho
		PdfPTable tablePrincipal = new PdfPTable(1);
		tablePrincipal.setWidths(new int[] { 100 });
		tablePrincipal.setWidthPercentage(100);

		// colocando o convenio
		cell = new PdfPCell(new Phrase("Convênio: " + nome, fontNegrito15));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tablePrincipal.addCell(cell);

		// colocando o numero da fatura
		cell = new PdfPCell(new Phrase("Número da Fatura: " + numero_fatura,
				fontNegrito15));
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

		// colocando o periodo de faturamento
		SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
		String dataInicialString = format1.format(dataInicial);

		SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
		String dataFinalString = format2.format(dataFinal);

		cell = new PdfPCell(new Phrase("De: " + dataInicialString + " até "
				+ dataFinalString, fontNegrito11));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tablePrincipal.addCell(cell);

		// colocando uma linha em branco
		cell = new PdfPCell(new Phrase("", fontNegrito15));
		cell.setBorder(Rectangle.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		tablePrincipal.addCell(cell);

		// criando o documento
		document.add(tablePrincipal);

		// criando a tabela de cabeçalho
		PdfPTable tableCabecalhoPrimeiraLinha = new PdfPTable(6);
		tableCabecalhoPrimeiraLinha.setWidths(new int[] { 11, 11, 38, 10, 20,
				10 });
		tableCabecalhoPrimeiraLinha.setWidthPercentage(100);

		// colocando a data
		cell = new PdfPCell(new Phrase("FICHA", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tableCabecalhoPrimeiraLinha.addCell(cell);

		// colocando a data
		cell = new PdfPCell(new Phrase("DATA", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tableCabecalhoPrimeiraLinha.addCell(cell);

		// colocando O PACIENTE/EXAME
		cell = new PdfPCell(new Phrase("PACIENTE", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tableCabecalhoPrimeiraLinha.addCell(cell);

		// colocando a matricula
		cell = new PdfPCell(new Phrase("CRM", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tableCabecalhoPrimeiraLinha.addCell(cell);

		// colocando a matricula
		cell = new PdfPCell(new Phrase("MATRÍCULA", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tableCabecalhoPrimeiraLinha.addCell(cell);

		// colocando o total do atendimento
		cell = new PdfPCell(new Phrase("TOTAL", fontNegrito8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tableCabecalhoPrimeiraLinha.addCell(cell);

		// colocando linha em branca
		cell = new PdfPCell(new Phrase("", fontNegrito8));
		cell.setColspan(6);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tableCabecalhoPrimeiraLinha.addCell(cell);

		document.add(tableCabecalhoPrimeiraLinha);

		// criando a tabela de cabeçalho
		PdfPTable tableCabeçalhoSegundaLinha = new PdfPTable(4);
		tableCabeçalhoSegundaLinha.setWidths(new int[] { 40, 20, 20, 20 });
		tableCabeçalhoSegundaLinha.setWidthPercentage(100);

		// colocando o exame
		cell = new PdfPCell(new Phrase("CÓDIGO - MODALIDADE - EXAME", font8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tableCabeçalhoSegundaLinha.addCell(cell);

		// colocando O valor exame
		cell = new PdfPCell(new Phrase("VALOR EXAME", font8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tableCabeçalhoSegundaLinha.addCell(cell);

		// colocando celula vazia
		cell = new PdfPCell(new Phrase("REDUTOR %", font8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tableCabeçalhoSegundaLinha.addCell(cell);

		// colocando o valor convenio
		cell = new PdfPCell(new Phrase("VALOR CONVÊNIO", font8));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tableCabeçalhoSegundaLinha.addCell(cell);

		// colocando linha em branca
		cell = new PdfPCell(new Phrase("", font8));
		cell.setColspan(4);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tableCabeçalhoSegundaLinha.addCell(cell);

		document.add(tableCabeçalhoSegundaLinha);

		// criando a tabela de cabeçalho
		PdfPTable tableCabeçalhoTerceiraLinha = new PdfPTable(5);
		tableCabeçalhoTerceiraLinha.setWidths(new int[] { 40, 17, 17, 17, 9 });
		tableCabeçalhoTerceiraLinha.setWidthPercentage(100);

		// colocando o exame
		cell = new PdfPCell(new Phrase("CÓDIGO - MATERIAL", font7));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tableCabeçalhoTerceiraLinha.addCell(cell);

		// colocando O valor exame
		cell = new PdfPCell(new Phrase("VALOR UNITÁRIO", font7));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tableCabeçalhoTerceiraLinha.addCell(cell);

		// colocando celula vazia
		cell = new PdfPCell(new Phrase("QUANTIDADE", font7));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tableCabeçalhoTerceiraLinha.addCell(cell);

		// colocando o valor convenio
		cell = new PdfPCell(new Phrase("VALOR TOTAL", font7));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tableCabeçalhoTerceiraLinha.addCell(cell);

		// colocando ocelula vazia
		cell = new PdfPCell(new Phrase("", font7));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tableCabeçalhoTerceiraLinha.addCell(cell);

		// colocando linha em branca
		cell = new PdfPCell(new Phrase("", font7));
		cell.setColspan(5);
		cell.setBorder(Rectangle.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tableCabeçalhoTerceiraLinha.addCell(cell);

		document.add(tableCabeçalhoTerceiraLinha);

		// terminamos o cabeçalho

		// agora vamos colocar as informações dos atendimentos e dos exames
		for (atendimentoModel atendimento : listaDeAtendimentos) {
			// tabela de atendimento
			PdfPTable tableAtendimento = new PdfPTable(6);
			tableAtendimento.setWidths(new int[] { 11, 11, 38, 10, 20, 10 });
			tableAtendimento.setWidthPercentage(100);

			// colocando o handle_at (ficha)
			cell = new PdfPCell(new Phrase(String.valueOf(atendimento
					.getHandle_at()), fontNegrito8));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			tableAtendimento.addCell(cell);

			// colocando a data do atendimento
			DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
			String dataCerta = fmt.format(atendimento.getData_atendimento());

			cell = new PdfPCell(new Phrase("" + dataCerta, fontNegrito8));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			tableAtendimento.addCell(cell);

			// colocando o nome do paciente
			cell = new PdfPCell(new Phrase(atendimento.getNomePaciente(),
					fontNegrito8));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			tableAtendimento.addCell(cell);

			// colocando o crm
			cell = new PdfPCell(new Phrase(atendimento.getCrmMedico(),
					fontNegrito8));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			tableAtendimento.addCell(cell);

			// colocando a matricula
			cell = new PdfPCell(new Phrase(atendimento.getMatricula_convenio(),
					fontNegrito8));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			tableAtendimento.addCell(cell);

			// colocando o total do atendimento
			cell = new PdfPCell(new Phrase(
					buscarValorDeUmAtendimento(atendimento), fontNegrito8));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			tableAtendimento.addCell(cell);

			document.add(tableAtendimento);

			// agora vamos colocar os exames desse atendimento
			ResultSet resultSet;
			if (tipo.equals("grupo")) {
				resultSet = atendimentoDAO.getConsultarExamesDosAtendimento(
						con, atendimento.getHandle_at(),
						atendimento.getHandle_convenio());
			} else {
				resultSet = atendimentoDAO.getConsultarExamesDosAtendimento(
						con, atendimento.getHandle_at(), handle_convenio);
			}
			while (resultSet.next()) {
				PdfPTable tabelaExameDoAtendimento = new PdfPTable(4);
				tabelaExameDoAtendimento
						.setWidths(new int[] { 40, 20, 20, 20 });
				tabelaExameDoAtendimento.setWidthPercentage(100);

				// colocando o nome do exame

				String nomeDoExame = resultSet.getString("cod_exame") + " - "
						+ resultSet.getString("sinonimo");
				if ("CC".equals(resultSet.getString("material"))) {
					nomeDoExame = nomeDoExame + " - C/ Contraste";
				} else if ("CM".equals(resultSet.getString("material"))) {
					nomeDoExame = nomeDoExame + " - C/ Material";
				}

				if (!"".equals(resultSet.getString("lado"))
						&& resultSet.getString("lado") != null) {
					nomeDoExame = nomeDoExame + " - Lado: "
							+ resultSet.getString("lado");
				}

				cell = new PdfPCell(new Phrase(nomeDoExame, font8));
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				tabelaExameDoAtendimento.addCell(cell);

				// colocando valor exame
				double valor_desconto;
				double valor_correto_exame = Double.valueOf(resultSet
						.getString("valor_correto_exame"));
				try {
					valor_desconto = Double.valueOf(resultSet
							.getString("valor_desconto"));
				} catch (Exception e) {
					valor_desconto = 0;
				}
				double valor_total_exame = new BigDecimal(valor_correto_exame
						+ valor_desconto).setScale(2, RoundingMode.HALF_EVEN)
						.doubleValue();
				String valor_total_string = String.valueOf(valor_total_exame);

				cell = new PdfPCell(new Phrase(MetodosUteis
						.colocarZeroEmCampoReais(valor_total_string)
						.replaceAll("\\.", ","), font8));
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				tabelaExameDoAtendimento.addCell(cell);

				// colocando uma celula vazia
				cell = new PdfPCell(new Phrase(resultSet.getString("redutor"),
						font8));
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				tabelaExameDoAtendimento.addCell(cell);

				// somando o valor deste exame ao valor total
				valorTotalConvenio = valorTotalConvenio
						+ Double.valueOf(resultSet
								.getString("valor_correto_convenio"));
				// colocando o valor do convenio
				cell = new PdfPCell(new Phrase(MetodosUteis
						.colocarZeroEmCampoReais(
								resultSet.getString("valor_correto_convenio"))
						.replaceAll("\\.", ","), font8));
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				tabelaExameDoAtendimento.addCell(cell);

				document.add(tabelaExameDoAtendimento);

				// colocando os ch do exame
				double ch1_exame, ch2_exame;
				try {
					ch1_exame = Double
							.valueOf(resultSet.getString("ch1_exame"));
				} catch (Exception e) {
					ch1_exame = 0;
				}
				try {
					ch2_exame = Double
							.valueOf(resultSet.getString("ch2_exame"));
				} catch (Exception e) {
					ch2_exame = 0;
				}

				if (ch1_exame > 0 || ch2_exame > 0) {
					// criando a tabela de cabeçalho
					PdfPTable tabelaDeMaterialCH = new PdfPTable(5);
					tabelaDeMaterialCH
							.setWidths(new int[] { 40, 17, 17, 17, 9 });
					tabelaDeMaterialCH.setWidthPercentage(100);

					// colocando o ch + nome convenio
					cell = new PdfPCell(new Phrase("CH " + nome, font7));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					tabelaDeMaterialCH.addCell(cell);

					// colocando O valor do ch (ch_convenio)
					cell = new PdfPCell(new Phrase(
							MetodosUteis.colocarZeroEmCampoReais(resultSet
									.getString("ch_convenio")), font7));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
					tabelaDeMaterialCH.addCell(cell);

					// colocando a quantidade (ch1_exame + ch2_exame)
					double quantidadeCh = new BigDecimal(
							Double.valueOf(resultSet.getString("ch1_exame"))
									+ Double.valueOf(resultSet
											.getString("ch2_exame"))).setScale(
							2, RoundingMode.HALF_EVEN).doubleValue();
					cell = new PdfPCell(new Phrase(
							MetodosUteis.colocarZeroEmCampoReais(String
									.valueOf(quantidadeCh)), font7));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
					tabelaDeMaterialCH.addCell(cell);

					// colocando o valor total do ch
					// ch_convenio vezes a quantidade
					double valorTotalDoMaterialCH = new BigDecimal(
							Double.valueOf(resultSet.getString("ch_convenio"))
									* quantidadeCh).setScale(2,
							RoundingMode.HALF_EVEN).doubleValue();
					cell = new PdfPCell(new Phrase(
							MetodosUteis.colocarZeroEmCampoReais(String
									.valueOf(valorTotalDoMaterialCH)), font7));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
					tabelaDeMaterialCH.addCell(cell);

					// colocando ocelula vazia
					cell = new PdfPCell(new Phrase("", font7));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
					tabelaDeMaterialCH.addCell(cell);

					document.add(tabelaDeMaterialCH);

				}

				// colocando o filme do exame
				double filme_exame;
				try {
					filme_exame = Double.valueOf(resultSet
							.getString("filme_exame"));
				} catch (Exception e) {
					filme_exame = 0;
				}

				if (filme_exame > 0) {
					// criando a tabela de cabeçalho
					PdfPTable tabelaDeMatrialFilme = new PdfPTable(5);
					tabelaDeMatrialFilme.setWidths(new int[] { 40, 17, 17, 17,
							9 });
					tabelaDeMatrialFilme.setWidthPercentage(100);

					// colocando o filme + convenio
					cell = new PdfPCell(new Phrase("FILME " + nome, font7));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					tabelaDeMatrialFilme.addCell(cell);

					// colocando O valor do filme (filme_convenio)
					cell = new PdfPCell(new Phrase(
							MetodosUteis.colocarZeroEmCampoReais(resultSet
									.getString("filme_convenio")), font7));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
					tabelaDeMatrialFilme.addCell(cell);

					// colocando a quantidade (filme_exame)
					// double quantidade = new
					// BigDecimal(Double.valueOf(resultSet.getString("filme_exame"))).setScale(2,
					// RoundingMode.HALF_EVEN).doubleValue();
					double quantidade = Double.valueOf(MetodosUteis
							.colocarZeroEmCampoReais(resultSet
									.getString("filme_exame")));
					cell = new PdfPCell(new Phrase(String.valueOf(quantidade),
							font7));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
					tabelaDeMatrialFilme.addCell(cell);

					// colocando o valor total do filme
					// filme_convenio vezes a quantidade (quantidade eh o
					// filme_exame)
					double valorTotalDoMaterial = new BigDecimal(
							Double.valueOf(resultSet
									.getString("filme_convenio")) * quantidade)
							.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
					cell = new PdfPCell(new Phrase(
							MetodosUteis.colocarZeroEmCampoReais(String
									.valueOf(valorTotalDoMaterial)), font7));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
					tabelaDeMatrialFilme.addCell(cell);

					// colocando ocelula vazia
					cell = new PdfPCell(new Phrase("", font7));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
					tabelaDeMatrialFilme.addCell(cell);

					document.add(tabelaDeMatrialFilme);
				}

				// colocando os materiais
				if (!"".equals(resultSet.getString("lista_materiais"))
						&& !"null".equals(resultSet
								.getString("lista_materiais"))
						&& resultSet.getString("lista_materiais") != null) {
					String[] materiais = resultSet.getString("lista_materiais")
							.split("#");

					for (int i = 0; i < materiais.length; i++) {
						String[] dadosDoMaterial = materiais[i].split("&");
						// criando a tabela para adicionar o material
						// criando a tabela de cabeçalho
						PdfPTable tabelaDeMatrial = new PdfPTable(5);
						tabelaDeMatrial
								.setWidths(new int[] { 40, 17, 17, 17, 9 });
						tabelaDeMatrial.setWidthPercentage(100);

						// colocando o nome do material
						cell = new PdfPCell(new Phrase(dadosDoMaterial[0]
								+ " - " + dadosDoMaterial[2], font7));
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
						tabelaDeMatrial.addCell(cell);

						// colocando o valor unitario
						cell = new PdfPCell(new Phrase(dadosDoMaterial[3],
								font7));
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
						tabelaDeMatrial.addCell(cell);

						// colocando a quantidade
						cell = new PdfPCell(new Phrase(dadosDoMaterial[4],
								font7));
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
						tabelaDeMatrial.addCell(cell);

						// colocando o valor total do material
						cell = new PdfPCell(new Phrase(dadosDoMaterial[5],
								font7));
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
						tabelaDeMatrial.addCell(cell);

						// colocando ocelula vazia
						cell = new PdfPCell(new Phrase("", font7));
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
						tabelaDeMatrial.addCell(cell);

						document.add(tabelaDeMatrial);

					}

				}

				// colocando linha em branco no fim dos materiais

				PdfPTable table = new PdfPTable(1);
				table.setWidths(new int[] { 100 });
				table.setWidthPercentage(100);

				// colocando uma celula vazia
				cell = new PdfPCell(new Phrase("", font8));
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);

				document.add(table);

			}
			// colocando linhas em branco no fim do atendimento
			for (int i = 0; i < 4; i++) {

				PdfPTable table = new PdfPTable(1);
				table.setWidths(new int[] { 100 });
				table.setWidthPercentage(100);

				// colocando uma celula vazia
				cell = new PdfPCell(new Phrase("", font8));
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);

				document.add(table);
			}

		}

		// colocando o valor total
		PdfPTable tabelaValorTotal = new PdfPTable(1);
		tabelaValorTotal.setWidths(new int[] { 100 });
		tabelaValorTotal.setWidthPercentage(100);

		String valorTotalString = String.valueOf(new BigDecimal(
				valorTotalConvenio).setScale(2, RoundingMode.HALF_EVEN)
				.doubleValue());
		cell = new PdfPCell(new Phrase("Total: "
				+ MetodosUteis.colocarZeroEmCampoReais(valorTotalString)
						.replaceAll("\\.", ","), fontNegrito11));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tabelaValorTotal.addCell(cell);

		document.add(tabelaValorTotal);

		// colocando o valor total POR AREAS DE ATENDIMENTO
		PdfPTable tabelaTotaisAreas = new PdfPTable(1);
		tabelaTotaisAreas.setWidths(new int[] { 100 });
		tabelaTotaisAreas.setWidthPercentage(100);
		
		cell = new PdfPCell(new Phrase("", fontNegrito15));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
		tabelaTotaisAreas.addCell(cell);
		
		cell = new PdfPCell(new Phrase("", fontNegrito15));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
		tabelaTotaisAreas.addCell(cell);
		
		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
		tabelaTotaisAreas.addCell(cell);
		
		cell = new PdfPCell(new Phrase("", fontNegrito15));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
		tabelaTotaisAreas.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Valores Totais por Área de Atendimento:"));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
		tabelaTotaisAreas.addCell(cell);

		cell = new PdfPCell(new Phrase("RX Geral: " + valorTotal1RxGeral, font11));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
		tabelaTotaisAreas.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Tomografia Computadorizada: " + valorTotal2TomografiaComputadorizada, font11));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
		tabelaTotaisAreas.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Ecografia: " + valorTotal3Ecografia, font11));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
		tabelaTotaisAreas.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Mamografia: " + valorTotal4Mamografia, font11));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
		tabelaTotaisAreas.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Eco Mamaria: " + valorTotal5EcoMamaria, font11));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
		tabelaTotaisAreas.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Ressonancia: " + valorTotal6Ressonancia, font11));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
		tabelaTotaisAreas.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Densitometria: " + valorTotal7Densitometria, font11));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
		tabelaTotaisAreas.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Eco Doppler: " + valorTotal8EcoDoppler, font11));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
		tabelaTotaisAreas.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Exames Complexos: " + valorTotal9ExamesComplexos, font11));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
		tabelaTotaisAreas.addCell(cell);

		document.add(tabelaTotaisAreas);

		document.close();

	}

	/*
	 * Metodo que abri um arquivo pdf (que acamos de criar)
	 */
	private void abrirFichaPDF() throws IOException {
		Runtime runtime = Runtime.getRuntime();
		if (OSvalidator.isWindows()) {
			runtime.exec("cmd /c " + caminho + nomeArquivo);
		} else if (OSvalidator.isMac()) {
			runtime.exec("open " + caminho + nomeArquivo);
		} else {
			runtime.exec("gnome-open " + caminho + nomeArquivo);
		}
	}

	// metodo que retorna a nomenclatura correta de acordo com a modalidade
	private String nomeModalidade(String modalidade) {
		if ("CR".equals(modalidade)) {
			return "RX";

		} else if ("CT".equals(modalidade)) {
			return "TC";

		} else if ("MG".equals(modalidade)) {
			return "MG";

		} else if ("DX".equals(modalidade)) {
			return "RX";

		} else if ("RF".equals(modalidade)) {
			return "FL";

		} else if ("NM".equals(modalidade)) {
			return "MN";

		} else if ("US".equals(modalidade)) {
			return "US";

		} else if ("DR".equals(modalidade)) {
			return "RX";

		} else if ("MR".equals(modalidade)) {
			return "RM";

		} else if ("OT".equals(modalidade)) {
			return "OT";

		} else if ("DO".equals(modalidade)) {
			return "DE";

		} else if ("OD".equals(modalidade)) {
			return "OD";

		} else if ("TR".equals(modalidade)) {
			return "TR";
		} else {
			JOptionPane
					.showMessageDialog(
							br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameJanelaPrincipal,
							"Erro nas modalidades. Procure o Administrador.");
			return "OT";
		}
	}

	private String buscarValorDeUmAtendimento(atendimentoModel atendimento)
			throws SQLException {
		int handle_at = atendimento.getHandle_at();

		double valor = 0.0;

		ResultSet resultSet = atendimentoDAO.getConsultarValorDeUmAtendimento(
				con, handle_at);
		while (resultSet.next()) {
			valor += Double.valueOf(resultSet
					.getString("valor_correto_convenio"));
		}
		valor = new BigDecimal(valor).setScale(2, RoundingMode.HALF_EVEN)
				.doubleValue();

		somaValorNaAreaDeAtendimento(atendimento, valor);

		return String.valueOf(valor);
	}

	private void somaValorNaAreaDeAtendimento(atendimentoModel atendimento,
			double valor) {
		switch (atendimento.getID_AREAS_ATENDIMENTO()) {
		case 1:
			valorTotal1RxGeral = valorTotal1RxGeral + valor;
			break;
		case 2:
			valorTotal2TomografiaComputadorizada = valorTotal2TomografiaComputadorizada
					+ valor;
			break;
		case 3:
			valorTotal3Ecografia = valorTotal3Ecografia + valor;
			break;
		case 4:
			valorTotal4Mamografia = valorTotal4Mamografia + valor;
			break;
		case 5:
			valorTotal5EcoMamaria = valorTotal5EcoMamaria + valor;
			break;
		case 6:
			valorTotal6Ressonancia = valorTotal6Ressonancia + valor;
			break;
		case 7:
			valorTotal7Densitometria = valorTotal7Densitometria + valor;
			break;
		case 8:
			valorTotal8EcoDoppler = valorTotal8EcoDoppler + valor;
			break;
		case 9:
			valorTotal9ExamesComplexos = valorTotal9ExamesComplexos + valor;
			break;
		default:
			break;
		}
	}

	private void gerarLaudos() {
		for (int i = 0; i < listaDeAtendimentos.size(); i++) {
			try {
				String handle_at = String.valueOf(listaDeAtendimentos.get(i)
						.getHandle_at());

				Date data_atendimento = listaDeAtendimentos.get(0)
						.getData_atendimento();
				SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
				String dataString = f.format(data_atendimento);

				String nomePaciente = listaDeAtendimentos.get(0)
						.getNomePaciente();
				String nomeMedico = listaDeAtendimentos.get(0).getNomeMedico();

				String laudo = JLAUDOS.getConsultarLaudo(Integer
						.valueOf(handle_at));
				if (laudo.equals("vazio")) {
					laudo = "";
				}

				laudo = laudo.replaceAll("\\[\\]", "\n");
				criaPDFdoLaudo criaLaudo = new criaPDFdoLaudo(handle_at,
						dataString, nomePaciente, nomeMedico, laudo, caminho
								+ "Laudos\\");
				criaLaudo.criarPDF();
			} catch (Exception e) {
				System.out.println("erro: " + e);
			}
		}
	}
}
