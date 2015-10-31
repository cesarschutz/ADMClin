/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * 
 * ATENÃO!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * SE DER ERRO VERIFICAR SE É POSSIVEL COPIAR UM ARQUIVO PARA O CMINHO C:/ SE PEDIR AUTORIZAÇÃO É ESSE O ERRO
 * TEMOS QUE CONFIGURAR O WINDOWNS PARA PERMITIR ACESSO A PASTA C:/
 * 
 * papel 800
 */
package br.bcn.admclin.impressoes.modelo1;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.dbris.ATENDIMENTOS;
import br.bcn.admclin.dao.dbris.ATENDIMENTO_EXAMES;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.DADOS_EMPRESA;
import br.bcn.admclin.impressoes.imprimirdiretonaimpressora.PrintPDFFactory;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JOptionPane;

/**
 * Configuração da impressora!! baixar drive Elgin VOX:
 * https://www.elgin.com.br/
 * portalelgin/site/produto/detalhe/produtodetalhe.aspx?idprod=641&sm=p12
 * imprimir pagina teste para verificar se foi instalado corretamente.
 * 
 * Vamos em propriedades da impressora na aba Utilitarios criamos um papel 800 x
 * 800 na aba Configuração de Dispositivos definimos o papel que foi criado
 * anteriormente
 * 
 * @author Cesar Schutz
 */
public class ImprimirBoletoDeRetiradaModelo1 {

	// variaveis que vamos utilizar para a construção do pdf
	Connection con = null;
	int handle_at;

	public ImprimirBoletoDeRetiradaModelo1(int handle_at) {
		this.handle_at = handle_at;
	}

	private void criandoAPastaParaSalvarOArquivo() {
		File dir = new File("C:\\ADMClin\\boletosDeRetirada");
		dir.mkdirs();
	}

	@SuppressWarnings("finally")
	public boolean salvarEIMprimirBoletoDeRetirada() {
		boolean conseguiuSalvarEAbrirFicha = false;
		con = Conexao.fazConexao();
		try {
			criandoAPastaParaSalvarOArquivo();
			// pegando informações do atgendimento
			buscarInformacoesDaEmpresa();
			buscarInformaçõesDoAtendimento();
			buscandoOsExamesDoAtendimento();
			if(criarArquivoTexto()) imprimirArquivoTexto();
			conseguiuSalvarEAbrirFicha = true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Não foi possível Criar Etiqueta. Procure o Administrador."
							+ " 000001", "ERRO",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}finally {
			Conexao.fechaConexao(con);
			return conseguiuSalvarEAbrirFicha;
		}
	}

	private String nomeEmpresa, enderecoEmpresa;
	private String telefoneEmpresa;

	private void buscarInformacoesDaEmpresa() throws SQLException {
		int id_dados_empresa = DADOS_EMPRESA.getConsultarIdDadosEmpresaDeUmAtendimento(con, handle_at);
		ResultSet resultSet = DADOS_EMPRESA.getConsultar(con, id_dados_empresa);
		while (resultSet.next()) {
			nomeEmpresa = resultSet.getString("nome");
			telefoneEmpresa = resultSet.getString("telefone");
			enderecoEmpresa = resultSet.getString("endereco");
		}
	}

	// variaveis da tabela atendimentos
	private String nome_paciente;
	private String data_entrega_exame, hora_exame_pronto;
	private String data_atendimento;
	private String EXAME_ENTREGUE_AO_PACIENTE, LAUDO_ENTREGUE_AO_PACIENTE;

	private void buscarInformaçõesDoAtendimento() throws SQLException {
		// buscando as informações do atendimento

		ResultSet resultSet = ATENDIMENTOS.getConsultarDadosDeUmAtendimento(
				con, handle_at);
		while (resultSet.next()) {
			nome_paciente = resultSet.getString("nomePac");

			data_entrega_exame = MetodosUteis
					.converterDataParaMostrarAoUsuario(resultSet
							.getString("data_exame_pronto"));
			data_atendimento = MetodosUteis
					.converterDataParaMostrarAoUsuario(resultSet
							.getString("data_atendimento"));
			hora_exame_pronto = MetodosUteis
					.transformarMinutosEmHorario(resultSet
							.getInt("hora_exame_pronto"));

			EXAME_ENTREGUE_AO_PACIENTE = resultSet
					.getString("EXAME_ENTREGUE_AO_PACIENTE");
			LAUDO_ENTREGUE_AO_PACIENTE = resultSet
					.getString("LAUDO_ENTREGUE_AO_PACIENTE");

		}
	}

	// variaveis dos exames
	private List<String> listaDeNomeDeExamesDoAtendimento = new ArrayList<String>();
	private List<String> listaDeLadoDeExamesDoAtendimento = new ArrayList<String>();
	private List<String> listaDeMaterialDeExamesDoAtendimento = new ArrayList<String>();

	private void buscandoOsExamesDoAtendimento() throws SQLException {
		ResultSet resultSetExames = ATENDIMENTO_EXAMES
				.getConsultarExamesDeUmAtendimento(con, handle_at);
		while (resultSetExames.next()) {
			listaDeNomeDeExamesDoAtendimento.add(resultSetExames
					.getString("NomeExame"));
			listaDeLadoDeExamesDoAtendimento.add(resultSetExames
					.getString("lado"));
			listaDeMaterialDeExamesDoAtendimento.add(resultSetExames
					.getString("material"));
		}
	}

	private void criarFichaPdf() throws DocumentException,
			FileNotFoundException {
		Rectangle rect = new Rectangle(165.0f, 220.37f);
		Document document = new Document(rect, 0, 0, 0, 0); // colocar as
															// margens
		PdfWriter.getInstance(document, new FileOutputStream(
				"C:\\ADMClin\\boletosDeRetirada\\boletoDeRetirada" + handle_at
						+ ".pdf"));
		document.open();

		Font fontNegrito7 = FontFactory.getFont("Calibri", 7, Font.BOLD);
		Font fontNegrito9 = FontFactory.getFont("Calibri", 9, Font.BOLD);
		Font fontNegrito15 = FontFactory.getFont("Calibri", 15, Font.BOLD);
		Font fontNegrito11 = FontFactory.getFont("Calibri", 11, Font.BOLD);
		Font font9 = FontFactory.getFont("Calibri", 9, Font.NORMAL);
		Font font7 = FontFactory.getFont("Calibri", 7, Font.NORMAL);

		PdfPCell cell;

		// tabela de cabeçalho
		PdfPTable table = new PdfPTable(1);
		table.setWidths(new int[] { 100 });
		table.setWidthPercentage(100);

		// colocando o RETIRADA DE EXAME
		cell = new PdfPCell(new Phrase("RETIRADA DE EXAME(S)", fontNegrito9));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		table.addCell(cell);

		if ("S".equals(EXAME_ENTREGUE_AO_PACIENTE)
				&& !"S".equals(LAUDO_ENTREGUE_AO_PACIENTE)) {
			// colocando o RETIRADA DE EXAME
			cell = new PdfPCell(new Phrase("**EXAME JÁ ENTREGUE**", font7));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			table.addCell(cell);
		}

		if ("S".equals(LAUDO_ENTREGUE_AO_PACIENTE)
				&& !"S".equals(EXAME_ENTREGUE_AO_PACIENTE)) {
			// colocando o RETIRADA DE EXAME
			cell = new PdfPCell(new Phrase("**LAUDO JÁ ENTREGUE**", font7));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			table.addCell(cell);
		}

		// linha em branco
		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		// linha em branco
		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		// Preenchendo codigo do atendimento
		cell = new PdfPCell(new Phrase("Código Atendimento: " + handle_at,
				font9));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		table.addCell(cell);

		// Preenchendo data do atendimento
		cell = new PdfPCell(new Phrase("Data Atendimento: " + data_atendimento,
				font9));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		table.addCell(cell);

		// linha em branco
		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		table.addCell(cell);

		// Preenchendo paciente
		cell = new PdfPCell(new Phrase("Paciente: " + nome_paciente, font9));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		table.addCell(cell);

		// preenchendo o cabeçalho dos exames
		cell = new PdfPCell(new Phrase("Exame(s)", fontNegrito7));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		table.addCell(cell);

		// //preenchendo os exames
		for (int i = 0; i < listaDeNomeDeExamesDoAtendimento.size(); i++) {

			String lado = listaDeLadoDeExamesDoAtendimento.get(i);
			String exame = "";
			if ("".equals(lado)) {
				exame = listaDeNomeDeExamesDoAtendimento.get(i);
			} else {
				exame = listaDeNomeDeExamesDoAtendimento.get(i) + " - LADO: "
						+ lado;
			}
			cell = new PdfPCell(new Phrase(exame, font7));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
			table.addCell(cell);

		}

		// linha em branco
		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		// linha em branco
		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		// preenchendo a retirada
		cell = new PdfPCell(new Phrase("Data de Entrega do(s) exame(s)",
				fontNegrito9));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		table.addCell(cell);
		// preenchendo a retirada
		cell = new PdfPCell(new Phrase(data_entrega_exame + " "
				+ hora_exame_pronto, fontNegrito9));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		table.addCell(cell);

		// linha em branco
		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		// linha em branco
		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		// linha em branco
		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		// linha em branco
		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		// linha em branco
		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		// linha em branco
		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		// linha em branco
		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		// linha em branco
		cell = new PdfPCell(new Phrase("-"));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		// que ficara imporesso para a proxima impressao

		// criando os dados da empresa
		cell = new PdfPCell(new Phrase(nomeEmpresa, fontNegrito15));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		table.addCell(cell);

		// proxima linha do cabeçalho
		cell = new PdfPCell(new Phrase("Fone: "
				+ String.valueOf(telefoneEmpresa), fontNegrito11));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		table.addCell(cell);

		// proxima linha do cabeçalho
		cell = new PdfPCell(new Phrase(enderecoEmpresa, font7));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		table.addCell(cell);

		// linha em branco
		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		table.addCell(cell);

		// criando o documento
		document.add(table);
		document.close();

	}

	/*
	 * Metodo que abri um arquivo pdf (que acamos de criar)
	 */
	private void abrirFichaPDF() throws IOException {
		Runtime runtime = Runtime.getRuntime();
		runtime.exec("cmd /c start C:\\ADMClin\\boletosDeRetirada\\boletoDeRetirada"
				+ handle_at + ".pdf");
	}

	public static int calculaIdade(String dataNasc, String pattern) {
		DateFormat sdf = new SimpleDateFormat(pattern);
		Date dataNascInput = null;
		try {
			dataNascInput = sdf.parse(dataNasc);
		} catch (Exception e) {
		}

		Calendar dateOfBirth = new GregorianCalendar();
		dateOfBirth.setTime(dataNascInput);

		// Cria um objeto calendar com a data atual
		Calendar today = Calendar.getInstance();

		// Obtém a idade baseado no ano
		int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);

		dateOfBirth.add(Calendar.YEAR, age);

		if (today.before(dateOfBirth)) {
			age--;
		}
		return age;

	}

	private boolean criarArquivoTexto() {
		String textoQueSeraExrito = criaTextoDoArquivoTexto();
		
		FileWriter arquivo;
		try {

			// criando a pasta
			File dir = new File("C:\\ADMClin\\boletosDeRetirada");
			dir.mkdirs();

			// criando o arquivo
			File file = new File("C:\\ADMClin\\boletosDeRetirada\\BoletoDeRetirada"
					+ handle_at + ".txt");
			file.createNewFile();

			arquivo = new FileWriter(new File(
					"C:\\ADMClin\\boletosDeRetirada\\BoletoDeRetirada" + handle_at
							+ ".txt"));
			arquivo.write(textoQueSeraExrito);
			arquivo.close();
			return true;
		} catch (IOException e) {
			System.out.println("erro io: " + e);
			return false;
		} catch (Exception e) {
			System.out.println("erro exception: " + e);
			return false;
		}
	}

	private String criaTextoDoArquivoTexto() {
		String textoQueSeraEscrito = ((char) 27) + ((char) 69) + "RETIRADA DE EXAME(S)\r\n";

		if ("S".equals(EXAME_ENTREGUE_AO_PACIENTE)
				&& !"S".equals(LAUDO_ENTREGUE_AO_PACIENTE)) {
			textoQueSeraEscrito += "**EXAME JÁ ENTREGUE**\r\n";
		}

		if ("S".equals(LAUDO_ENTREGUE_AO_PACIENTE)
				&& !"S".equals(EXAME_ENTREGUE_AO_PACIENTE)) {
			textoQueSeraEscrito += "**LAUDO JÁ ENTREGUE**\r\n";
		}

		textoQueSeraEscrito += "\r\n";
		textoQueSeraEscrito += "Codigo: " + handle_at + "\r\n";
		textoQueSeraEscrito += "Data: " + data_atendimento + "\r\n";
		textoQueSeraEscrito += "\r\n";
		textoQueSeraEscrito += "Paciente: " + nome_paciente + "\r\n";
		textoQueSeraEscrito += "Exame(s)" + "\r\n";

		for (int i = 0; i < listaDeNomeDeExamesDoAtendimento.size(); i++) {
			String lado = listaDeLadoDeExamesDoAtendimento.get(i);
			String exame = "";
			if ("".equals(lado)) {
				exame = listaDeNomeDeExamesDoAtendimento.get(i);
			} else {
				exame = listaDeNomeDeExamesDoAtendimento.get(i) + " - LADO: "
						+ lado;
			}
			textoQueSeraEscrito += exame + "\r\n";
		}
		
		textoQueSeraEscrito += "\r\n";
		textoQueSeraEscrito += "Data de Entrega do(s) exame(s)\r\n";
		textoQueSeraEscrito += "		" + data_entrega_exame + " "+ hora_exame_pronto + "\r\n";
		textoQueSeraEscrito += "\r\n";
		textoQueSeraEscrito += "\r\n";
		textoQueSeraEscrito += nomeEmpresa + "\r\n";
		textoQueSeraEscrito += "Fone: " + telefoneEmpresa + "\r\n";
		textoQueSeraEscrito += enderecoEmpresa;
		textoQueSeraEscrito += "\r\n\r\n\r\n";
		
		return textoQueSeraEscrito;
	}

	private void imprimirArquivoTexto(){
		PrintPDFFactory printPDFFactory = new PrintPDFFactory(); 
		printPDFFactory.printPDF(new File("C:\\ADMClin\\boletosDeRetirada\\BoletoDeRetirada" + handle_at + ".txt"), "VOX");
	}
}
