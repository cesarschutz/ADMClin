package br.bcn.admclin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.model.Especialidades_Medicas;

/**
 * 
 * @author Cesar Schutz
 */
public class ESPECIALIDADES_MEDICAS {
    public static boolean conseguiuConsulta;

    /**
     * Consulta Todas as Especialidades Medicas existentes no Banco de Dados.
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultar(Connection con) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select * from especialidades_medicas order by descricao");
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Especialidades Médicas. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * Verifica se Especialidade Medica já existe antes de cadastra-lo no Banco de Dados.
     * 
     * @param Connection
     * @param Especialidades_Medicas
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean getConsultarParaSalvarNovoRegistro(Connection con, Especialidades_Medicas model) {
        boolean existe = true;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select * from especialidades_medicas where descricao=?");
            stmtQuery.setString(1, model.getDescricao());
            ResultSet resultSet = stmtQuery.executeQuery();
            if (!resultSet.next()) {
                existe = false;
            }
            conseguiuConsulta = true;
        } catch (SQLException e) {
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null,
                "Erro ao consultar se Especialidade Médica já existe. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return existe;
        }
    }

    /**
     * Verifica se Especialidade Medica já existe antes de fazer um update no Banco de Dados.
     * 
     * @param Connection
     * @param Especialidades_Medicas
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean getConsultarParaAtualizarRegistro(Connection con, Especialidades_Medicas model) {
        boolean existe = true;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select * from especialidades_medicas where (descricao=?) and (emid!=?)");
            stmtQuery.setString(1, model.getDescricao());
            stmtQuery.setInt(2, model.getEmId());
            ResultSet resultSet = stmtQuery.executeQuery();
            if (!resultSet.next()) {
                existe = false;
            }
            conseguiuConsulta = true;
        } catch (SQLException e) {
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null,
                "Erro ao consultar se Especialidade Médica já existe. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return existe;
        }
    }

    /**
     * Cadastra uma nova Especialidade Medica no Banco de Dados.
     * 
     * @param Connection
     * @param Especialidades_Medicas
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setCadastrar(Connection con, Especialidades_Medicas model) {
        boolean cadastro = false;
        String sql = "insert into especialidades_medicas (descricao,usuarioid,dat) values(?,?,?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, model.getDescricao());
            stmt.setInt(2, model.getUsuarioId());
            stmt.setDate(3, model.getDat());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Especialidade Médica. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return cadastro;
        }
    }

    /**
     * Atualiza uma Especialidade Medica no Banco De Dados.
     * 
     * @param Connection
     * @param Especialidades_Medicas
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setUpdate(Connection con, Especialidades_Medicas model) {
        boolean atualizo = false;
        String sql = "update especialidades_medicas set descricao=?, usuarioid=?, dat=? where emid=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, model.getDescricao());
            stmt.setInt(2, model.getUsuarioId());
            stmt.setDate(3, model.getDat());
            stmt.setInt(4, model.getEmId());
            stmt.executeUpdate();
            stmt.close();
            atualizo = true;
        } catch (SQLException e) {
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Especialidade Médica. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return atualizo;
        }
    }

    /**
     * Deleta uma Especialidade medica no Banco de Dados.
     * 
     * @param Connection
     * @param Especialidades_Medicas
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setDeletar(Connection con, Especialidades_Medicas model) {
        boolean deleto = false;
        String sql = "delete from especialidades_medicas where emid=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, model.getEmId());
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Especialidade Médica. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return deleto;
        }
    }

    /**
     * Verifica se Especialidade Médica está sendo utilizada por algum Medico, antes de apagar no Banco de Dados.
     * 
     * @param Connection
     * @param String
     *            codigo
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean getConsultarSeEspecialidadeEstaSendoUtilizada(Connection con, int codTabela) {
        boolean utilizada = true;
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select * from medicos where emid=" + codTabela);
            ResultSet resultSet = stmtQuery.executeQuery();
            if (!resultSet.next()) {
                utilizada = false;
            }
            conseguiuConsulta = true;
        } catch (SQLException e) {
            conseguiuConsulta = false;
            JOptionPane
                .showMessageDialog(
                    null,
                    "Erro ao verficar se Especialidade Medica está sendo utilizada por algum Médico.\nProcure o Administrador.",
                    "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return utilizada;
        }
    }

}
