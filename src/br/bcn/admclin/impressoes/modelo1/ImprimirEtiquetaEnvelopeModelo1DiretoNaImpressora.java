/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.impressoes.modelo1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.ClasseAuxiliares.ESCPrinter;
import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.OSvalidator;
import br.bcn.admclin.dao.dbris.ATENDIMENTOS;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

/**
 *
 * @author BCN
 */
public class ImprimirEtiquetaEnvelopeModelo1DiretoNaImpressora {

	private int handle_at;
	private String caminhoImpressora;
	private String nomeDoArquivo = janelaPrincipal.internalFrameJanelaPrincipal.codigoParaImpressoesLinux
			+ "CODIGOBARRAS";

	public ImprimirEtiquetaEnvelopeModelo1DiretoNaImpressora(int handle_at) {
		this.handle_at = handle_at;
		try {
			buscarInformaçõesDoAtendimento();
			writeFile();
		} catch (SQLException e) {
			
		}
	}

	// variaveis da tabela atendimentos
	private String nome_paciente, nome_medico_sol;
	private String modalidade;
	private String data_atendimento, hora_atendimento;

	private void buscarInformaçõesDoAtendimento() throws SQLException {
		// buscando as informações do atendimento
		Connection con = Conexao.fazConexao();
		ResultSet resultSet = ATENDIMENTOS.getConsultarDadosDeUmAtendimento(con, handle_at);
		while (resultSet.next()) {
			nome_paciente = resultSet.getString("nomePac");
			nome_medico_sol = resultSet.getString("nomeMed");

			data_atendimento = MetodosUteis
					.converterDataParaMostrarAoUsuario(resultSet
							.getString("data_atendimento"));
			hora_atendimento = MetodosUteis
					.transformarMinutosEmHorario(resultSet
							.getInt("hora_atendimento"));
			modalidade = "";

		}
	}

	private void instanciarImpressora() {
		if (!OSvalidator.isWindows() && !OSvalidator.isMac()) {
			caminhoImpressora = nomeDoArquivo;
		} else {
			caminhoImpressora = USUARIOS.impressora_etiqueta_envelope;
		}
	}

	private void imprimirNotaCasoSejaLinux() throws IOException {
		if (!OSvalidator.isWindows() && !OSvalidator.isMac()) {
			Runtime.getRuntime().exec(
					"lpr -P " + USUARIOS.impressora_etiqueta_envelope + " "
							+ nomeDoArquivo);
		}
	}

	static final String NEW_LINE = System.getProperty("line.separator"); 
	public boolean writeFile() {
		try {
			instanciarImpressora();
			PrintWriter fo = new PrintWriter(new FileOutputStream(new File(
					caminhoImpressora)));
			
			
			/**
			fo.print((char) 2);
			fo.print("n");
			fo.print((char) 13);
			fo.print((char) 10);
			fo.print((char) 2);
			fo.print("M0600");
			fo.print((char) 13);
			fo.print((char) 10);
			fo.print("K170");
			fo.print((char) 13);
			fo.print((char) 10);
			fo.print("O0220");
			fo.print("V0");
			fo.print((char) 13);
			fo.print((char) 10);
			fo.print((char) 2);
			fo.print("f320");
			fo.print((char) 13);
			fo.print((char) 10);
			fo.print((char) 2);
			fo.print("c0000");
			fo.print((char) 13);
			fo.print((char) 10);
			fo.print((char) 2);
			fo.print("r");
			fo.print((char) 13);
			fo.print((char) 10);
			fo.print((char) 1);
			fo.print("D");
			fo.print((char) 13);
			fo.print((char) 10);
			fo.print((char) 2);
			fo.print("L");
			fo.print((char) 13);
			fo.print((char) 10);
			fo.print("D11");
			fo.print((char) 13);
			fo.print((char) 10);
			fo.print("H10");
			fo.print((char) 13);
			fo.print((char) 10);
			fo.print("PC");
			fo.print((char) 13);
			fo.print((char) 10);
			fo.print("A2");
			fo.print((char) 13);
			fo.print((char) 10);
			fo.print("1E0308000100024" + handle_at);
			fo.print((char) 13);
			fo.print((char) 10);
			fo.print("1E0308000100232" + handle_at);
			fo.print((char) 13);
			fo.print((char) 10);
			fo.print("Q0001");
			fo.print((char) 13);
			fo.print((char) 10);
			fo.print("E");
			fo.print((char) 13);
			fo.print((char) 10);
			**/
			fo.close();

			imprimirNotaCasoSejaLinux();
			return true;
		} catch (Exception e) {
			System.out.println("Erro: " + e);
			return false;
		}
	}

}
