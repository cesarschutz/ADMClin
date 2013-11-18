package br.bcn.admclin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.model.Exames;

/**
 *
 * @author BCN
 */
public class EXAMES {
     public static boolean conseguiuConsulta;
    /**
     * Verifica todos os Exames cadastrados no Banco de Dados
     * @return ResultSet
     * @param Connection
     */
    public static ResultSet getConsultar(Connection con){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select * from exames order by NOME");
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Exames. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Verifica todos os Exames cadastrados no Banco de Dados por modalildade
     * @return ResultSet
     * @param Connection
     */
    public static ResultSet getConsultarPorModalidade(Connection con, String modalidade){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select * from exames where modalidade =?  order by NOME");
            stmtQuery.setString(1, modalidade);
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Exames. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Verifica se Exame já existe antes de cadastra-lo no Banco de Dados.
     * @param Connection
     * @param ExameMODEL
     * @return boolean
     */
    public static boolean getConsultarParaSalvar(Connection con, Exames exame){
        boolean existe = true;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select * from exames where nome=?");
            stmtQuery.setString(1, exame.getNOME());
            ResultSet resultSet = stmtQuery.executeQuery();
            if(!resultSet.next()){
                existe = false;
            }
            conseguiuConsulta = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Exame existe. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return existe;
        }
    }
    /**
     * Verifica se Exame já existe antes de atualiza-lo no Banco De Dados.
     * @param Connection
     * @param ExameMODEL
     * @return boolean
     */
    public static boolean getConsultarParaAtualizar(Connection con, Exames exame){
        boolean existe = true;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select * from exames where (nome=?) and (HANDLE_EXAME!=?)");
            stmtQuery.setString(1, exame.getNOME());
            stmtQuery.setInt(2, exame.getHANDLE_EXAME());
            ResultSet resultSet = stmtQuery.executeQuery();
            if(!resultSet.next()){
                existe = false;
            }
            conseguiuConsulta = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Exame existe. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return existe;
        }
    }
    /**
     * Cadastra um novo exame no banco de dados.
     * @param Connection
     * @param ExameMODEL 
     * @return Boolean
     */
    public static boolean setCadastrar(Connection con, Exames exame){
        boolean cadastro = false;
        String sql = "insert into exames (usuarioid,dat,DURACAO,nome,qtdhoras,laudo,HANDLE_CLASSEDEEXAME,modalidade) values(?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, exame.getUsuarioId());
            stmt.setDate(2, exame.getData());
            stmt.setInt(3, exame.getDuracao());
            stmt.setString(4, exame.getNOME());
            stmt.setString(5, exame.getQtdHoras());
            stmt.setString(6, exame.getLaudo());
            stmt.setInt(7, exame.getHANDLE_CLASSEDEEXAME());
            stmt.setString(8, exame.getModalidade());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro cadastrar Exame. Procure o administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    /**
     * Atualiza um Exame no Banco De Dados.
     * @param Connection
     * @param ExameModel 
     * @return Boolean
     */
    public static boolean setUpdate(Connection con, Exames exame){
        boolean cadastro = false;
        String sql = "update exames set usuarioid=?, dat=?, duracao=?, nome=?, qtdhoras=?, laudo=?, HANDLE_CLASSEDEEXAME=?, modalidade=?  where HANDLE_EXAME=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, exame.getUsuarioId());
            stmt.setDate(2, exame.getData());
            stmt.setInt(3, exame.getDuracao());
            stmt.setString(4, exame.getNOME());
            stmt.setString(5, exame.getQtdHoras());
            stmt.setString(6, exame.getLaudo());
            stmt.setInt(7, exame.getHANDLE_CLASSEDEEXAME());
            stmt.setString(8, exame.getModalidade());
            stmt.setInt(9, exame.getHANDLE_EXAME());

            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Exame. Procure o administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    /**
     * Deleta um Exame do Banco De Dados
     * @param Connection
     * @param ExameMODEL 
     * @return boolean
     */
    public static boolean setDeletar(Connection con, Exames exame){
        boolean deleto = false; 
        String sql="delete from exames where HANDLE_EXAME=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, exame.getHANDLE_EXAME());
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Exame. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
}
