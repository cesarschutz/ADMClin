package br.bcn.admclin.interfacesGraficas.menu.financeiro.alterarvaloresnotaipe;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.dbris.Conexao;

public class ExameNotaIpeDAO {

	@SuppressWarnings("finally")
	public static ArrayList<ExameNotaIpeMODEL> getExamesPorNota(int numeroNota){
		ArrayList<ExameNotaIpeMODEL> listaExames = new ArrayList<>();
		
	        ResultSet resultSet = null;
	        Connection con = Conexao.fazConexao();
	        try {
	            PreparedStatement stmtQuery = con.prepareStatement("select a.matricula_convenio, a.handle_at, e.valor_correto_convenio, e.NUMERO_REF_NOTA_IPE, e.atendimento_exame_id, a.data_atendimento, j.nome as nome_exame, p.nome as nome_paciente from atendimento_exames e" 
					+ " inner join atendimentos a   on a.handle_at = e.handle_at "
					+ " inner join exames j on j.handle_exame = e.handle_exame "
					+ " inner join pacientes p on p.handle_paciente = a.handle_paciente "
					+ " where e.NUMERO_NOTA_IPE = ? order by a.handle_at, e.NUMERO_REF_NOTA_IPE");
	            stmtQuery.setInt(1, numeroNota);
	            resultSet = stmtQuery.executeQuery();
	            while (resultSet.next()) {
	            	ExameNotaIpeMODEL exame = new ExameNotaIpeMODEL();
	            	
	            	SimpleDateFormat formatoData  = new SimpleDateFormat("dd");
	            	Date data_atendimento = resultSet.getDate("data_atendimento");
	            	String dia = formatoData.format(data_atendimento);
	            	
	            	exame.setDia(dia);
	        		exame.setExame(resultSet.getString("nome_exame"));
	        		exame.setFicha(resultSet.getString("handle_at"));
	        		exame.setMatricula(resultSet.getString("matricula_convenio"));
	        		exame.setPaciente(resultSet.getString("nome_paciente"));
	        		exame.setValor(String.valueOf(resultSet.getDouble("valor_correto_convenio")));
	        		exame.setAtendimtno_exame_id(resultSet.getInt("atendimento_exame_id"));
	        		exame.setNUMERO_REF_NOTA_IPE(resultSet.getString("numero_ref_nota_ipe"));
	        		listaExames.add(exame);
	            }
	            Conexao.fechaConexao(con);
	        } catch (SQLException e) {
	            Conexao.fechaConexao(con);
	            JOptionPane.showMessageDialog(null, "Erro ao consultar exames desta nota. Procure o Administrador.",
	                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
	        } finally {
	            return listaExames;
	        }
	}
	
	public static boolean atualizaValorConvenio(int atendimento_exame_id, double valorCorretoConvenio){
		try {
			Connection con = Conexao.fazConexao();
			String sql2 = "update atendimento_exames set valor_correto_convenio= ? where atendimento_exame_id = ?";
            PreparedStatement stmt2 = con.prepareStatement(sql2);
            stmt2.setDouble(1, valorCorretoConvenio);
            stmt2.setInt(2, atendimento_exame_id);
            stmt2.executeUpdate();
            return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao tentar atualizar Valor do Convênio.");
			return false;
		}
	}
}