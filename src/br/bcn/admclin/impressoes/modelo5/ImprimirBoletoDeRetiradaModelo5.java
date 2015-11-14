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
package br.bcn.admclin.impressoes.modelo5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JOptionPane;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.dbris.ATENDIMENTOS;
import br.bcn.admclin.dao.dbris.ATENDIMENTO_EXAMES;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.DADOS_EMPRESA;
import br.bcn.admclin.dao.dbris.USUARIOS;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

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
public class ImprimirBoletoDeRetiradaModelo5 {

	// variaveis que vamos utilizar para a construção do pdf
	Connection con = null;
	int handle_at;

	public ImprimirBoletoDeRetiradaModelo5(int handle_at) {
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
			imprimirBoletoDeRetirada();
			//AQUI PARA ABRIR DIRETO NA IMPRESSORA
			//APAGAR AS DUAS LINHAS ACIMA QUE CRIAM E ABREM O PDF
			//if(criarArquivoTexto()) imprimirArquivoTexto();
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
	
	public void imprimirBoletoDeRetirada() throws Exception{
		
		PrintWriter fo = new PrintWriter(new FileOutputStream(new File(USUARIOS.IMPRESSORA_BOLETO_DE_RETIRADA)));
		
		//dados empresa
		fo.print(nomeEmpresa + "\n");
		fo.print("Fone: " + String.valueOf(telefoneEmpresa) + "\n");
		fo.print(enderecoEmpresa + "\n");
		fo.print("------------------------------------------------\n\n");
		
		//informa quando exame já foi retirado		
		fo.print("RETIRADA DE EXAME(S)\n");
		if ("S".equals(EXAME_ENTREGUE_AO_PACIENTE)
				&& !"S".equals(LAUDO_ENTREGUE_AO_PACIENTE)) {
			fo.print("****EXAME JÁ ENTREGUE\n");
		}
		
		//informa quando laudo já foi retirado
		if ("S".equals(LAUDO_ENTREGUE_AO_PACIENTE)
				&& !"S".equals(EXAME_ENTREGUE_AO_PACIENTE)) {
			fo.print("****LAUDO JÁ ENTREGUE\n");
		}
		
		//dados do atendimento
		fo.print("Numr: " + handle_at + "\n");
		fo.print("Data: " + data_atendimento + "\n\n") ;
		
		fo.print("Nome: " + nome_paciente + "\n");
		fo.print("Exame(s)\n");
		
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
			fo.print(exame + "\n");
		}
		
		//data de retirada
		fo.print("\n\nRETIRADA DOS EXAMES: " + data_entrega_exame + " " + hora_exame_pronto);
		fo.print("\n\n\n\n\n\n\n\n");
        fo.print((char) 27);
        fo.print((char) 109);
        
        fo.close();
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

}
