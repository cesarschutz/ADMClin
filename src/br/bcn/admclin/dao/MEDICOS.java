package br.bcn.admclin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.model.Medicos;

/**
 * 
 * @author Cesar Schutz
 */
public class MEDICOS {
    public static boolean conseguiuConsulta;

    /**
     * Consulta Todos Medicos existentes no Banco de Dados.
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultar(Connection con, String sql) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select * from medicos where nome like ? order by nome");
            stmtQuery.setString(1, sql);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Médicos. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * verificar handle_paciente de um paicente cadastrado no banco de dados
     * 
     * @param Connection
     * @param PacienteMODEL
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static int getConsultarMedicoId(Connection con, String nome) {
        int handle_paciente = 0;
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select * from medicos where nome=?");
            stmtQuery.setString(1, nome);
            ResultSet resultSet = stmtQuery.executeQuery();
            if (resultSet.next()) {
                handle_paciente = resultSet.getInt("medicoId");
            }
        } catch (SQLException e) {
            handle_paciente = 0;
            JOptionPane.showMessageDialog(null, "Não foi possivel consultar medicoId. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return handle_paciente;
        }
    }

    /**
     * Consulta os dados de um medico exitentes no Banco De Dados.
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarDadosDeUmMedico(Connection con, int medicoId) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select * from medicos where medicoId = ?");
            stmtQuery.setInt(1, medicoId);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar dados do Médico. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * Verifica se Médico já existe antes de cadastra-lo no Banco de Dados.
     * 
     * @param Connection
     * @param Medicos
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean getConsultarParaSalvarNovoRegistro(Connection con, Medicos model) {
        boolean existe = true;
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select * from medicos where nome=?");
            stmtQuery.setString(1, model.getNome());
            ResultSet resultSet = stmtQuery.executeQuery();
            if (!resultSet.next()) {
                existe = false;
            }
            conseguiuConsulta = true;
        } catch (SQLException e) {
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Médico já existe. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return existe;
        }
    }

    /**
     * Verifica se Médico já existe antes de fazer um update no Banco de Dados.
     * 
     * @param Connection
     * @param Medicos
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean getConsultarParaAtualizarRegistro(Connection con, Medicos model) {
        boolean existe = true;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select * from medicos where (nome=?) and (medicoid!=?)");
            stmtQuery.setString(1, model.getNome());
            stmtQuery.setInt(2, model.getMedicoId());
            ResultSet resultSet = stmtQuery.executeQuery();
            if (!resultSet.next()) {
                existe = false;
            }
            conseguiuConsulta = true;
        } catch (SQLException e) {
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Médico já existe. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return existe;
        }
    }

    /**
     * Cadastra um novo Médico no Banco de Dados.
     * 
     * @param Connection
     * @param Medicos
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setCadastrar(Connection con, Medicos model) {
        boolean cadastro = false;
        String sql =
            "insert into medicos (emId, usuarioid, dat, nome, nascimento, telefone, celular, endereco, bairro, cep, cidade, uf, email, crm, ufcrm) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, model.getEmId());
            stmt.setInt(2, model.getUsuarioId());
            stmt.setDate(3, model.getDat());
            stmt.setString(4, model.getNome());
            stmt.setString(5, model.getNascimento());
            stmt.setString(6, model.getTelefone());
            stmt.setString(7, model.getCelular());
            stmt.setString(8, model.getEndereco());
            stmt.setString(9, model.getBairro());
            stmt.setString(10, model.getCep());
            stmt.setString(11, model.getCidade());
            stmt.setString(12, model.getUf());
            stmt.setString(13, model.getEmail());
            stmt.setString(14, model.getCrm());
            stmt.setString(15, model.getUfcrm());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Médico. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return cadastro;
        }
    }

    /**
     * Atualiza um Médico no Banco De Dados.
     * 
     * @param Connection
     * @param Medicos
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setUpdate(Connection con, Medicos model) {
        boolean atualizo = false;
        String sql =
            "update medicos set emId=?, usuarioid=?, dat=?, nome=?, nascimento=?, telefone=?, celular=?, endereco=?, bairro=?, cep=?, cidade=?, uf=?, email=?, crm=?, ufcrm=? where medicoid=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, model.getEmId());
            stmt.setInt(2, model.getUsuarioId());
            stmt.setDate(3, model.getDat());
            stmt.setString(4, model.getNome());
            stmt.setString(5, model.getNascimento());
            stmt.setString(6, model.getTelefone());
            stmt.setString(7, model.getCelular());
            stmt.setString(8, model.getEndereco());
            stmt.setString(9, model.getBairro());
            stmt.setString(10, model.getCep());
            stmt.setString(11, model.getCidade());
            stmt.setString(12, model.getUf());
            stmt.setString(13, model.getEmail());
            stmt.setString(14, model.getCrm());
            stmt.setString(15, model.getUfcrm());
            stmt.setInt(16, model.getMedicoId());

            stmt.executeUpdate();
            stmt.close();
            atualizo = true;
        } catch (SQLException e) {
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Médico. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return atualizo;
        }
    }

    /**
     * Deleta um Médico no Banco de Dados.
     * 
     * @param Connection
     * @param Medicos
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setDeletar(Connection con, Medicos model) {
        boolean deleto = false;
        String sql = "delete from medicos where medicoid=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, model.getMedicoId());
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Médico. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return deleto;
        }
    }
}
