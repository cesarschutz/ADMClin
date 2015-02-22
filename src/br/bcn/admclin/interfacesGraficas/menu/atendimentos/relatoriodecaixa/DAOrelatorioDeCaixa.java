/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.atendimentos.relatoriodecaixa;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.dbris.Conexao;

/**
 * 
 * @author Cesar Schutz
 */
public class DAOrelatorioDeCaixa {

	/**
	 * Consulta Todos os atendimentos existentes no Banco de Dados
	 * 
	 * @param Connection
	 * @return ResultSet
	 */
	@SuppressWarnings("finally")
	public static List<Model> getConsultarAtendimentos(Date diaInicial, Date diaFinal) {
		ResultSet resultSet = null;
		List<Model> lista = new ArrayList<>();
		try {
			Connection con = Conexao.fazConexao();
			PreparedStatement stmtQuery = con
					.prepareStatement("select a.data_atendimento, a.handle_at, c.handle_convenio, c.nome as nome_convenio, p.pacienteid, p.nome as nome_paciente, "
							+ "e.valor_correto_convenio, e.valor_correto_paciente, e.valor_correto_exame, u.usrid, u.nm_usuario from atendimento_exames e "
							
							+ "inner join atendimentos a on e.handle_at = a.handle_at "
							+ "inner join pacientes p on a.handle_paciente = p.pacienteid "
							+ "inner join convenio c on a.handle_convenio = c.handle_convenio "
							+ "inner join usuarios u on a.usuarioid = u.usrid "
							+ "where(a.data_atendimento > ? or a.data_atendimento = ?)  and  (a.data_atendimento < ? or a.data_atendimento = ?) "
							+ "order by a.data_atendimento asc, u.nm_usuario asc");
			stmtQuery.setDate(1, diaInicial);
			stmtQuery.setDate(2, diaInicial);
			stmtQuery.setDate(3, diaFinal);
			stmtQuery.setDate(4, diaFinal);
			resultSet = stmtQuery.executeQuery();
			
			
			
			while(resultSet.next()){
				Model model = new Model();
				model.setData(resultSet.getDate("data_atendimento"));
				model.setHandle_at(resultSet.getInt("handle_at"));
				model.setConvenio(resultSet.getString("nome_convenio"));
				model.setPaciente(resultSet.getString("nome_paciente"));
				model.setValor_convenio(resultSet.getString("valor_correto_convenio"));
				model.setValor_paciente(resultSet.getString("valor_correto_paciente"));
				model.setValor_total(resultSet.getString("valor_correto_exame"));
				model.setUsuario(resultSet.getString("nm_usuario"));
				lista.add(model);
			}
		} catch (SQLException e) {
			JOptionPane
					.showMessageDialog(
							null,
							"Erro ao consultar os Atendimentos. Procure o Administrador." + e,
							"ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
		} finally {
			return lista;
		}
	}
}
