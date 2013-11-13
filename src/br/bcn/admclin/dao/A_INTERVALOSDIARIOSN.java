package br.bcn.admclin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.model.A_intervalosdiariosN;

/**
 *
 * @author BCN
 */
public class A_INTERVALOSDIARIOSN {
    public static boolean conseguiuConsulta;
    /**
     * Consulta Todos os Intervalos Diarios existentes no Banco de Dados para colocar na lista!!!
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultar(Connection con){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select * from a_intervalosdiariosn order by nome");
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Intervalos Diários. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Verifica se nome de intervalo diario ja existe 
     * @param Connection
     * @param A_intervalosdiariosN
     * @return boolean
     */
    public static boolean getConsultarParaSalvarRegistro(Connection con, A_intervalosdiariosN model){
       boolean existe = true;
        try{
          PreparedStatement stmtQuery = con.prepareStatement("select * from a_intervalosdiariosN where nome=?");
          stmtQuery.setString(1, model.getNome());
          ResultSet resultSet = stmtQuery.executeQuery();
          if(!resultSet.next()){
                existe = false;
            }
          conseguiuConsulta = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Intervalo Diário já existe. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return existe;
        }
    }
    
    /**
     * Verifica se nome de intervalo diario ja existe antes de atualizalo
     * @param Connection
     * @param A_intervalosdiariosN
     * @return boolean
     */
    public static boolean getConsultarParaAtualizarRegistro(Connection con, A_intervalosdiariosN model){
       boolean existe = true;
        try{
          PreparedStatement stmtQuery = con.prepareStatement("select * from a_intervalosdiariosN where nome=? and a_intervalodiarionid!=?");
          stmtQuery.setString(1, model.getNome());
          stmtQuery.setInt(2, model.getA_intervaloDiarioId());
          ResultSet resultSet = stmtQuery.executeQuery();
          if(!resultSet.next()){
                existe = false;
            }
          conseguiuConsulta = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Intervalo Diário já existe. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return existe;
        }
    }
    
    /**
     * Consulta o ID de um intervalo Diario atraves do nome!
     * @param Connection
     * @return ResultSet
     */
    public static int getConsultarIdDeUmNomeCadastrado(Connection con, A_intervalosdiariosN model){
        int intervaloDiarioNId = 0;
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select a_intervalodiarionid from a_intervalosdiariosN where nome=?");
        stmtQuery.setString(1, model.getNome());
        resultSet = stmtQuery.executeQuery();
        while(resultSet.next()){
            intervaloDiarioNId = resultSet.getInt("a_intervalodiarionid");
        }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar ID do intervalo Diário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return intervaloDiarioNId;
        }
    }
    
    /**
     * Consulta os dados de um Intervalo diario
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarDadosDeUmIntervaloPorHorario(Connection con, int intervaloDiarioNId){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select * from a_intervalosdiariosN where a_intervalodiarionid=?");
        stmtQuery.setInt(1, intervaloDiarioNId);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Intervalo Diário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Cadastra uma novo intervalo diario no Banco de Dados.
     * @param Connection 
     * @param A_intervalosdiariosN
     * @return Boolean
     */
    public static boolean setCadastrar(Connection con, A_intervalosdiariosN model){
        boolean cadastro = false;
        String sql = "insert into a_intervalosdiariosN (dat,usuarioid,nome,horarioinicial,horariofinal,diaDoIntervalo,descricao) values(?,?,?,?,?,?,?)";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, model.getDat());
            stmt.setInt(2, model.getUsuarioId());
            stmt.setString(3, model.getNome());
            stmt.setInt(4, model.getHorarioInicial());
            stmt.setInt(5, model.getHorarioFinal());
            stmt.setDate(6, model.getDiadoIntervalo());
            stmt.setString(7, model.getDescricao());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Intervalo Diário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
    /**
     * Deleta um Intervalo Diario do Banco De Dados
     * @param Connection
     * @param intervaloDiarioNId 
     * @return boolean
     */
    public static boolean setDeletar(Connection con, int intervaloDiarioNId){
        boolean deleto = false; 
        String sql="delete from a_intervalosdiariosN where a_intervalodiarionid=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, intervaloDiarioNId);
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Intervalo Diário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
    
    //atualizar agenda
    public static boolean setAtualizar(Connection con, A_intervalosdiariosN model){
        boolean cadastro = false;
        String sql = "update a_intervalosdiariosN set dat=?, usuarioid=?, nome=?, horarioinicial=?, horariofinal=?,  diadointervalo=?, descricao=? where a_intervalodiarionid=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, model.getDat());
            stmt.setInt(2, model.getUsuarioId());
            stmt.setString(3, model.getNome());
            stmt.setInt(4, model.getHorarioInicial());
            stmt.setInt(5, model.getHorarioFinal());
            stmt.setDate(6, model.getDiadoIntervalo());
            stmt.setString(7, model.getDescricao());
            stmt.setInt(8, model.getA_intervaloDiarioId());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Intervalo Diário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
}
