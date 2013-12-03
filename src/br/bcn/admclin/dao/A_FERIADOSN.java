package br.bcn.admclin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.model.A_feriadosN;

/**
 * 
 * @author Cesar Schutz
 */
public class A_FERIADOSN {
    public static boolean conseguiuConsulta;

    /**
     * Consulta Todos os feraidos existentes no Banco de Dados para colocar na lista!!!
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultar(Connection con) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select * from a_feriadosn order by nome");
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Feriados. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * Verifica se nome de feraido ja existe
     * 
     * @param Connection
     * @param A_feriadosN
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean getConsultarParaSalvarRegistro(Connection con, A_feriadosN model) {
        boolean existe = true;
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select * from a_feriadosN where nome=?");
            stmtQuery.setString(1, model.getNome());
            ResultSet resultSet = stmtQuery.executeQuery();
            if (!resultSet.next()) {
                existe = false;
            }
            conseguiuConsulta = true;
        } catch (SQLException e) {
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Feriado já existe. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return existe;
        }
    }

    /**
     * Verifica se nome de feriado ja existe antes de atualizalo
     * 
     * @param Connection
     * @param A_feriadosN
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean getConsultarParaAtualizarRegistro(Connection con, A_feriadosN model) {
        boolean existe = true;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select * from a_feriadosN where nome=? and handle_feriadon!=?");
            stmtQuery.setString(1, model.getNome());
            stmtQuery.setInt(2, model.getHandleFeriadoN());
            ResultSet resultSet = stmtQuery.executeQuery();
            if (!resultSet.next()) {
                existe = false;
            }
            conseguiuConsulta = true;
        } catch (SQLException e) {
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Feriado já existe. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return existe;
        }
    }

    /**
     * Consulta o ID de um feriado atraves do nome!
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static int getConsultarIdDeUmNomeCadastrado(Connection con, A_feriadosN model) {
        int feriadosNId = 0;
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select handle_feriadon from a_feriadosN where nome=?");
            stmtQuery.setString(1, model.getNome());
            resultSet = stmtQuery.executeQuery();
            while (resultSet.next()) {
                feriadosNId = resultSet.getInt("handle_feriadon");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar ID do Feriado. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return feriadosNId;
        }
    }

    /**
     * Consulta os dados de um feriado
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarDadosDeUmFeriado(Connection con, int feriadosNId) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select * from a_feriadosN where handle_feriadon=?");
            stmtQuery.setInt(1, feriadosNId);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Feriados. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * Cadastra uma novo Feriado no Banco de Dados.
     * 
     * @param Connection
     * @param A_feriadosN
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setCadastrar(Connection con, A_feriadosN model) {
        boolean cadastro = false;
        String sql = "insert into a_feriadosN (dat,usuarioid,nome,diaDoferiado,descricao) values(?,?,?,?,?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, model.getDat());
            stmt.setInt(2, model.getUsuarioId());
            stmt.setString(3, model.getNome());
            stmt.setString(4, model.getDiaDoFeriado());
            stmt.setString(5, model.getDescricao());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Feriado. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return cadastro;
        }
    }

    /**
     * Deleta um Feriado do Banco De Dados
     * 
     * @param Connection
     * @param feriadoNId
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean setDeletar(Connection con, int handle_feriadon) {
        boolean deleto = false;
        String sql = "delete from a_feriadosN where handle_feriadon=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, handle_feriadon);
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

    // atualizar agenda
    @SuppressWarnings("finally")
    public static boolean setAtualizar(Connection con, A_feriadosN model) {
        boolean cadastro = false;
        String sql =
            "update a_feriadosN set dat=?, usuarioid=?, nome=?, diadoferiado=?, descricao=? where handle_feriadon=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, model.getDat());
            stmt.setInt(2, model.getUsuarioId());
            stmt.setString(3, model.getNome());
            stmt.setString(4, model.getDiaDoFeriado());
            stmt.setString(5, model.getDescricao());
            stmt.setInt(6, model.getHandleFeriadoN());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Feriado. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return cadastro;
        }
    }
}
