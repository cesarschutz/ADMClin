/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.impressoes.modelo2e3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
public class ImprimirEtiquetaEnvelopeModelo6 {

	private String nome_area_de_atendimento;
    private int handle_at;
    private String str_handle_at;
    private String caminhoImpressora;
    private String nomeDoArquivo = janelaPrincipal.internalFrameJanelaPrincipal.codigoParaImpressoesLinux + "CODIGOBARRAS";
    
    public ImprimirEtiquetaEnvelopeModelo6(int handle_at) {
    	this.handle_at = handle_at;
		this.str_handle_at = String.valueOf(handle_at);
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
			nome_paciente = resultSet.getString("nomepac");
			nome_medico_sol = resultSet.getString("nomeMed");

			data_atendimento = MetodosUteis
					.converterDataParaMostrarAoUsuario(resultSet
							.getString("data_atendimento"));
			hora_atendimento = MetodosUteis
					.transformarMinutosEmHorario(resultSet
							.getInt("hora_atendimento"));
			modalidade = "";
			nome_area_de_atendimento = resultSet.getString("nome_area");

		}
	}
    
    private void instanciarImpressora(){
        if(!OSvalidator.isWindows() && !OSvalidator.isMac()){
            caminhoImpressora = nomeDoArquivo;
        }else{
            caminhoImpressora = USUARIOS.impressora_codigo_de_barras;
        }
    }
    
    private PrintWriter fo;
    
    public boolean writeFile(){
    	try{ 
            fo = new PrintWriter(new FileOutputStream(new File("\\\\localhost\\Bematech_LB-1000")));

            fo.print("SIZE 4,2.5");
            fo.print((char) 10);
            fo.print((char) 13);
            fo.print("GAP 0.1,0");
            fo.print((char) 10);
            fo.print((char) 13);
            fo.print("CLS");
            fo.print((char) 10);
            fo.print((char) 13);
            fo.print("BACKFEED 260");
            fo.print((char) 10);
            fo.print((char) 13);
            fo.print("DIRECTION 0");
            fo.print((char) 10);
            fo.print((char) 13);
            fo.print("REFERENCE 0,0");
            fo.print((char) 10);
            fo.print((char) 13);
            fo.print("OFFSET 0.00 mm");
            fo.print((char) 10);
            fo.print((char) 13);
            fo.print("SET CUTTER OFF");
            fo.print((char) 10);
            fo.print((char) 13);
            fo.print("SET TEAR OFF");
            fo.print((char) 10);
            fo.print((char) 13);
            fo.print("TEXT 25,10,  \"4\",0,1,1, \"DATA  : "+data_atendimento+" Num:"+str_handle_at +"\"");
            fo.print((char) 10);
            fo.print((char) 13);
            fo.print("TEXT 25,60, \"4\",0,1,1, \"NOME  : " + nome_paciente + "\"");
            fo.print((char) 10);
            fo.print((char) 13);
            fo.print("TEXT 25,110,\"4\",0,1,1, \"MEDICO: "+nome_medico_sol+"\"");
            fo.print((char) 10);
            fo.print((char) 13);
            fo.print("TEXT 25,160,\"4\",0,1,1, \"EXAME : "+nome_area_de_atendimento+"\"");
            fo.print((char) 10);
            fo.print((char) 13);
            fo.print("TEXT 25,250,\"5\",0,1,1, \"       viaimagem\"");
            fo.print((char) 10);
            fo.print((char) 13);
            fo.print("TEXT 25,350,\"4\",0,1,1, \"Rua Celestino do Nascimento, 473\"");
            fo.print((char) 10);
            fo.print((char) 13);
            fo.print("TEXT 25,400,\"4\",0,1,1, \"Xanxere - SC - TEL(49) 3433-6666\"");
            fo.print((char) 10);
            fo.print((char) 13);
            fo.print("PRINT 1,1");
            fo.print((char) 10);
            fo.print((char) 13);
            fo.print("FEED 260");
            fo.print((char) 10);
            fo.print((char) 13);

            return true;
        }catch(Exception e){
            System.out.println("Erro: " + e);
            return false;
        }finally{
        	try {
        		fo.close();
		} catch (Exception e2) {
		}
        }
    }
    
    
}
