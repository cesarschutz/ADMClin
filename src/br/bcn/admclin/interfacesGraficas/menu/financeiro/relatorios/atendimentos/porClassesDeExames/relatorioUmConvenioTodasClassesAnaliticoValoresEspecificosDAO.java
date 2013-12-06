/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.atendimentos.porClassesDeExames;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

/**
 * 
 * @author Cesar Schutz
 */
public class relatorioUmConvenioTodasClassesAnaliticoValoresEspecificosDAO {

    /**
     * Consulta Todos os atendimentos existentes no Banco de Dados
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarAtendimentos(Connection con, Date diaInicial, Date diaFinal, int handle_convenio) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select a.data_atendimento, e.handle_at, p.nome as nomePaciente, j.nome as nomeConvenio, x.nome as nomeExame, e.ch_convenio, e.filme_convenio, e.ch1_exame, e.ch2_exame, e.filme_exame, e.lista_materiais, c.nome as nomeClasseDeExame, e.redutor, e.valor_desconto, e.valor_correto_exame, e.material from atendimento_exames e "
                    + "inner join atendimentos a on e.handle_at = a.handle_at "
                    + "inner join exames x on e.handle_exame = x.handle_exame "
                    + "inner join tb_classesexames c on x.handle_classedeexame = c.cod "
                    + "inner join pacientes p on a.handle_paciente = p.pacienteid "
                    + "inner join convenio j on a.handle_convenio = j.handle_convenio "
                    + "where(a.data_atendimento > ? or a.data_atendimento = ?)  and  (a.data_atendimento < ? or a.data_atendimento = ?) and j.handle_convenio = ?"
                    + "order by c.cod asc,a.data_atendimento asc");
            stmtQuery.setDate(1, diaInicial);
            stmtQuery.setDate(2, diaInicial);
            stmtQuery.setDate(3, diaFinal);
            stmtQuery.setDate(4, diaFinal);
            stmtQuery.setInt(5, handle_convenio);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar os Atendimentos. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }
}
