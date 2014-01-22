package br.bcn.admclin.dao.dbris;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.model.A_feriados;

/**
 * 
 * @author Cesar Schutz
 */
public class A_FERIADOS {

    public static boolean conseguiuConsulta;

    /**
     * Consulta Todos os feriados existentes no Banco de Dados
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarFeriadosPorAgenda(Connection con, int handle_agenda) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select a_feriados.handle_agenda, a_feriados.handle_feriadon, a_feriadosn.handle_feriadon, a_feriadosn.diadoferiado "
                    + "from a_feriados inner join a_feriadosn on a_feriados.handle_feriadon = a_feriadosn.handle_feriadon where a_feriados.handle_agenda ="
                    + handle_agenda);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao verificar os Feriados. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * Consulta os dados de um feriado
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarAgendasDeUmFeriado(Connection con, int feriadoNId) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select distinct a.handle_agenda, b.nome from a_feriados as a inner join agendas as b on a.handle_agenda = b.handle_agenda  where handle_feriadon = ?");
            stmtQuery.setInt(1, feriadoNId);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Feriado. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * Cadastra um novo feriados no Banco de Dados.
     * 
     * @param Connection
     * @param A_feriados
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setCadastrar(Connection con, A_feriados model) {
        boolean cadastro = false;
        String sql = "insert into a_feriados (handle_feriadon, handle_agenda) values(?,?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, model.getHandleFeriadoN());
            stmt.setInt(2, model.getHandleAgenda());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Agenda no Feriado. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return cadastro;
        }
    }

    /**
     * Deleta um feriado do Banco De Dados
     * 
     * @param Connection
     * @param feriadoNId
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean setDeletar(Connection con, int feriadoNId) {
        boolean deleto = false;
        String sql = "delete from a_feriados where handle_feriadon=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, feriadoNId);
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Feriado. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return deleto;
        }
    }
}
