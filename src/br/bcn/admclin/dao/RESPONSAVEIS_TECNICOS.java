package br.bcn.admclin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.model.Responsaveis_Tecnicos;

/**
 * 
 * @author Cesar Schutz
 */
public class RESPONSAVEIS_TECNICOS {
    public static boolean conseguiuConsulta;

    /**
     * Consulta todos os responsáveis técnicos exitentes no Banco De Dados.
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultar(Connection con) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select * from RESPONSAVEIS_TECNICOS order by nome");
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Responsáveis Técnicos. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * Verifica se Responsável Técnico já existe antes de cadastra-lo no Banco de Dados.
     * 
     * @param Connection
     * @param ResponsavelTecnicoMODEL
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean getConsultarParaSalvarNovoRegistro(Connection con, Responsaveis_Tecnicos responsavelTecnico) {
        boolean existe = true;
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select * from RESPONSAVEIS_TECNICOS where cpf=?");
            stmtQuery.setString(1, responsavelTecnico.getCpf());
            ResultSet resultSet = stmtQuery.executeQuery();
            if (!resultSet.next()) {
                existe = false;
            }
            conseguiuConsulta = true;
        } catch (SQLException e) {
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null,
                "Erro ao consultar se Responsável Técnico existe. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return existe;
        }
    }

    /**
     * Verifica se Responsável Técnico já existe antes de fazer um update no Banco de Dados.
     * 
     * @param Connection
     * @param ResponsavelTecnicoMODEL
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean getConsultarParaAtualizarRegistro(Connection con, Responsaveis_Tecnicos responsavelTecnico) {
        boolean existe = true;
        try {
            PreparedStatement pstmt =
                con.prepareStatement("select * from RESPONSAVEIS_TECNICOS where (cpf=?) and (rtid!=?)");
            pstmt.setString(1, responsavelTecnico.getCpf());
            pstmt.setInt(2, responsavelTecnico.getRtId());
            ResultSet resultSet = pstmt.executeQuery();
            if (!resultSet.next()) {
                existe = false;
            }
            conseguiuConsulta = true;
        } catch (SQLException e) {
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null,
                "Erro ao consultar se Responsável Técnico existe. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return existe;
        }
    }

    /**
     * Salva um novo Responsável Técnico no Banco De Dados.
     * 
     * @param Connection
     * @param ResponsavelTecnicoMODEL
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean setCadastrar(Connection con, Responsaveis_Tecnicos responsavelTecnico) {
        boolean cadastro = false;
        String sql =
            "insert into RESPONSAVEIS_TECNICOS (usuarioid,dat,nome,conselho,registro_conselho,cpf,uf,registro_ans) values(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, responsavelTecnico.getUsuarioId());
            stmt.setDate(2, responsavelTecnico.getData());
            stmt.setString(3, responsavelTecnico.getNome());
            stmt.setString(4, responsavelTecnico.getConselho());
            stmt.setString(5, responsavelTecnico.getRegistroConselho());
            stmt.setString(6, responsavelTecnico.getCpf());
            stmt.setString(7, responsavelTecnico.getUf());
            stmt.setString(8, responsavelTecnico.getRegistroAns());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Responsável Técnico. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return cadastro;
        }
    }

    /**
     * Atualiza um Responsável Técnico no Banco De Dados.
     * 
     * @param Connection
     * @param ResponsavelTecnicoMODEL
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean setUpdate(Connection con, Responsaveis_Tecnicos responsavelTecnico) {
        boolean atualizo = false;
        String sql =
            "update RESPONSAVEIS_TECNICOS  set usuarioid=?, dat=?, nome=?, conselho=?, registro_conselho=?, cpf=?, uf=?, registro_ans=? where rtid=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, responsavelTecnico.getUsuarioId());
            stmt.setDate(2, responsavelTecnico.getData());
            stmt.setString(3, responsavelTecnico.getNome());
            stmt.setString(4, responsavelTecnico.getConselho());
            stmt.setString(5, responsavelTecnico.getRegistroConselho());
            stmt.setString(6, responsavelTecnico.getCpf());
            stmt.setString(7, responsavelTecnico.getUf());
            stmt.setString(8, responsavelTecnico.getRegistroAns());
            stmt.setInt(9, responsavelTecnico.getRtId());
            stmt.executeUpdate();
            stmt.close();
            atualizo = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Responsável Técnico. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return atualizo;
        }
    }

    /**
     * Deleta um Responsável Técnico no Banco de Dados.
     * 
     * @param Connection
     * @param ClasseDeExameMODEL
     * @return boolean
     */
    @SuppressWarnings("finally")
    public static boolean setDeletar(Connection con, Responsaveis_Tecnicos responsavelTecnico) {
        boolean deleto = false;
        String sql = "delete from RESPONSAVEIS_TECNICOS where rtid=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, responsavelTecnico.getRtId());
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Responsável Técnico. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return deleto;
        }
    }
}
