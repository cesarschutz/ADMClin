/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.faturarConvenio;

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
public class atendimentoDAO {
    /**
     * Consulta Todos os atendimentos existentes no Banco de Dados
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarAtendimentosPorPeriodoEConvenio(Connection con, Date diaInicial, Date diaFinal,
        int handle_convenio) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select m.crm as crmMedico, p.nome as nomePaciente, p.nascimento as nascimentoPaciente, a.handle_at, a.data_atendimento, a.modalidade, a.matricula_convenio, a.hora_atendimento, a.flag_laudo, a.flag_faturado "
                    + "from atendimentos A inner join pacientes p on a.handle_paciente = p.handle_paciente "
                    + "inner join medicos m on a.handle_medico_sol = m.medicoid "
                    + "where (data_atendimento > ?  or data_atendimento = ?) and (data_atendimento < ?  or data_atendimento = ?) and handle_convenio = ? order by data_atendimento, a.handle_at");
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

    @SuppressWarnings("finally")
    public static ResultSet getConsultarAtendimentosPorPeriodoEGrupo(Connection con, Date diaInicial, Date diaFinal,
        int grupo_id) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select m.crm as crmMedico, p.nome as nomePaciente, p.nascimento as nascimentoPaciente, a.handle_at, a.data_atendimento, a.modalidade, a.matricula_convenio, a.handle_convenio, a.hora_atendimento, a.flag_laudo, a.flag_faturado "
                    + "from atendimentos A inner join pacientes p on a.handle_paciente = p.handle_paciente "
                    + "inner join medicos m on a.handle_medico_sol = m.medicoid "
                    + "inner join convenio c on a.handle_convenio = c.handle_convenio "
                    + "where (data_atendimento > ?  or data_atendimento = ?) and (data_atendimento < ?  or data_atendimento = ?) and c.grupoid = ? order by data_atendimento, a.handle_at");
            stmtQuery.setDate(1, diaInicial);
            stmtQuery.setDate(2, diaInicial);
            stmtQuery.setDate(3, diaFinal);
            stmtQuery.setDate(4, diaFinal);
            stmtQuery.setInt(5, grupo_id);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar os Atendimentos. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * Consulta Todos os atendimentos existentes no Banco de Dados
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarExamesDosAtendimento(Connection con, int handle_at, int handle_convenio) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select distinct a.atendimento_exame_id, t.sinonimo, t.cod_exame, e.modalidade, a.lado, a.material, a.valor_correto_convenio, a.valor_correto_exame, a.ch_convenio, a.valor_desconto, a.redutor, "
                    + "a.filme_convenio, a.ch1_exame, a.ch2_exame, a.filme_exame, a.lista_materiais from atendimento_exames A "
                    + "inner join exames e on a.handle_exame = e.handle_exame "
                    + "inner join tabelas t on a.handle_exame = t.handle_exame and t.handle_convenio = ? "
                    + "where handle_at = ? order by t.sinonimo");
            stmtQuery.setInt(1, handle_convenio);
            stmtQuery.setInt(2, handle_at);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar os Exames. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * Consulta Todos os atendimentos existentes no Banco de Dados
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarValorDeUmAtendimento(Connection con, int handle_at) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select valor_correto_convenio from atendimento_exames where handle_at=?");
            stmtQuery.setInt(1, handle_at);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar valor do Atendimento. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /*
     * Altera o flag_faturado para 1 (que foi faturado)
     */
    @SuppressWarnings("finally")
    public static boolean setAtualizarFlagFaturado(Connection con, int handle_at, int flag_faturado) {
        boolean cadastro = false;
        String sql = "update ATENDIMENTOS set flag_faturado=? where handle_at=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, flag_faturado);
            stmt.setInt(2, handle_at);
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao definir atendimento como Faturado. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return cadastro;
        }
    }
}
