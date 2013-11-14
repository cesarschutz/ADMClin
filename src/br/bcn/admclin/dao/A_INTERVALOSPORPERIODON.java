package br.bcn.admclin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.model.A_intervalosPorPeriodoN;

/**
 *
 * @author BCN
 */
public class A_INTERVALOSPORPERIODON {
    public static boolean conseguiuConsulta;
    /**
     * Consulta Todos os Intervalos por periodo existentes no Banco de Dados para colocar na lista!!!
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultar(Connection con){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select * from a_intervalosporperiodon order by nome");
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Intervalos por Período. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Verifica se nome de intervalo por periodo ja existe 
     * @param Connection
     * @param A_intervalosPorPeriodoN
     * @return boolean
     */
    public static boolean getConsultarParaSalvarRegistro(Connection con, A_intervalosPorPeriodoN model){
       boolean existe = true;
        try{
          PreparedStatement stmtQuery = con.prepareStatement("select * from a_intervalosporperiodon where nome=?");
          stmtQuery.setString(1, model.getNome());
          ResultSet resultSet = stmtQuery.executeQuery();
          if(!resultSet.next()){
                existe = false;
            }
          conseguiuConsulta = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Intervalo por Período já existe. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return existe;
        }
    }
    
    /**
     * Verifica se nome de intervalo por periodo ja existe antes de atualizalo
     * @param Connection
     * @param a_intervalosdiariosNMODEL
     * @return boolean
     */
    public static boolean getConsultarParaAtualizarRegistro(Connection con, A_intervalosPorPeriodoN model){
       boolean existe = true;
        try{
          PreparedStatement stmtQuery = con.prepareStatement("select * from a_intervalosporperiodon where nome=? and a_intervaloporperiodonid!=?");
          stmtQuery.setString(1, model.getNome());
          stmtQuery.setInt(2, model.getA_intervaloPorPeriodoNId());
          ResultSet resultSet = stmtQuery.executeQuery();
          if(!resultSet.next()){
                existe = false;
            }
          conseguiuConsulta = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Intervalo por Período já existe. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return existe;
        }
    }
    
    /**
     * Consulta o ID de um intervalo por periodo atraves do nome!
     * @param Connection
     * @return ResultSet
     */
    public static int getConsultarIdDeUmNomeCadastrado(Connection con, A_intervalosPorPeriodoN model){
        int intervaloPorPeriodoNId = 0;
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select a_intervaloporperiodonid from a_intervalosporperiodon where nome=?");
        stmtQuery.setString(1, model.getNome());
        resultSet = stmtQuery.executeQuery();
        while(resultSet.next()){
            intervaloPorPeriodoNId = resultSet.getInt("a_intervaloporperiodonid");
        }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar ID do intervalo por Período. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return intervaloPorPeriodoNId;
        }
    }
    
    /**
     * Consulta os dados de um Intervalo por periodo
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarDadosDeUmIntervaloPorHorario(Connection con, int intervaloPorPeriodoNId){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select * from a_intervalosporperiodon where a_intervaloporperiodonid=?");
        stmtQuery.setInt(1, intervaloPorPeriodoNId);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Intervalo por Período. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Cadastra uma novo intervalo por periodo no Banco de Dados.
     * @param Connection 
     * @param A_intervalosPorPeriodoN
     * @return Boolean
     */
    public static boolean setCadastrar(Connection con, A_intervalosPorPeriodoN model){
        boolean cadastro = false;
        String sql = "insert into a_intervalosporperiodon (dat,usuarioid,nome,horarioinicial,horariofinal,diaInicial,diaFinal,descricao) values(?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, model.getDat());
            stmt.setInt(2, model.getUsuarioId());
            stmt.setString(3, model.getNome());
            stmt.setInt(4, model.getHorarioInicial());
            stmt.setInt(5, model.getHorarioFinal());
            stmt.setDate(6, model.getDiaInicial());
            stmt.setDate(7, model.getDiaFinal());
            stmt.setString(8, model.getDescricao());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Intervalo por Período. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
    /**
     * Deleta um Intervalo por periodo do Banco De Dados
     * @param Connection
     * @param intervaloPorPeriodoNId 
     * @return boolean
     */
    public static boolean setDeletar(Connection con, int intervaloPorPeriodoNId){
        boolean deleto = false; 
        String sql="delete from a_intervalosporperiodon where a_intervaloporperiodonid=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, intervaloPorPeriodoNId);
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Intervalo por Período. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
    
    //atualizar intervalo por periodo
    public static boolean setAtualizar(Connection con, A_intervalosPorPeriodoN model){
        boolean cadastro = false;
        String sql = "update a_intervalosporperiodoN set dat=?, usuarioid=?, nome=?, horarioinicial=?, horariofinal=?,  diainicial=?, diafinal=?, descricao=? where a_intervaloporperiodonid=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, model.getDat());
            stmt.setInt(2, model.getUsuarioId());
            stmt.setString(3, model.getNome());
            stmt.setInt(4, model.getHorarioInicial());
            stmt.setInt(5, model.getHorarioFinal());
            stmt.setDate(6, model.getDiaInicial());
            stmt.setDate(7, model.getDiaFinal());
            stmt.setString(8, model.getDescricao());
            stmt.setInt(9, model.getA_intervaloPorPeriodoNId());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Intervalo por Período. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
}
