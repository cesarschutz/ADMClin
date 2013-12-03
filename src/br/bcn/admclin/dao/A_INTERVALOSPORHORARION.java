package br.bcn.admclin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.model.A_intervalosPorHorarioN;

/**
 *
 * @author BCN
 */
public class A_INTERVALOSPORHORARION {
    public static boolean conseguiuConsulta;
    /**
     * Consulta Todos os Intervalos por Horario existentes no Banco de Dados para colocar na lista!!!
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultar(Connection con){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select * from A_INTERVALOSPORHORARION order by nome");
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Intervalos por Horário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Verifica se nome de intervalo por horarios ja existe 
     * @param Connection
     * @param A_intervalosPorHorarioN
     * @return boolean
     */
    public static boolean getConsultarParaSalvarRegistro(Connection con, A_intervalosPorHorarioN model){
       boolean existe = true;
        try{
          PreparedStatement stmtQuery = con.prepareStatement("select * from A_INTERVALOSPORHORARION where nome=?");
          stmtQuery.setString(1, model.getNome());
          ResultSet resultSet = stmtQuery.executeQuery();
          if(!resultSet.next()){
                existe = false;
            }
          conseguiuConsulta = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Intervalo por Horário já existe. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return existe;
        }
    }
    
    /**
     * Verifica se nome de intervalo por horarios ja existe antes de atualizalo
     * @param Connection
     * @param A_intervalosPorHorarioN
     * @return boolean
     */
    public static boolean getConsultarParaAtualizarRegistro(Connection con, A_intervalosPorHorarioN model){
       boolean existe = true;
        try{
          PreparedStatement stmtQuery = con.prepareStatement("select * from A_INTERVALOSPORHORARION where nome=? and a_intervaloporhorarionid!=?");
          stmtQuery.setString(1, model.getNome());
          stmtQuery.setInt(2, model.getA_intervaloPorHorarioNId());
          ResultSet resultSet = stmtQuery.executeQuery();
          if(!resultSet.next()){
                existe = false;
            }
          conseguiuConsulta = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Intervalo por Horário já existe. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return existe;
        }
    }
    
    /**
     * Consulta o ID de um intervalo por horario atraves do nome!
     * @param Connection
     * @return ResultSet
     */
    public static int getConsultarIdDeUmNomeCadastrado(Connection con, A_intervalosPorHorarioN model){
        int intervaloPorHorarioNId = 0;
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select a_intervaloPorHorarionid from a_intervalosPorHorarion where nome=?");
        stmtQuery.setString(1, model.getNome());
        resultSet = stmtQuery.executeQuery();
        while(resultSet.next()){
            intervaloPorHorarioNId = resultSet.getInt("a_intervaloPorHorarionid");
        }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar ID do intervalo por Horário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return intervaloPorHorarioNId;
        }
    }
    
    /**
     * Consulta os dados de um Intervalo por Horario
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarDadosDeUmIntervaloPorHorario(Connection con, int intervaloPorHorarioId){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select * from a_intervalosporhorarion where a_intervaloporhorarionid=?");
        stmtQuery.setInt(1, intervaloPorHorarioId);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Intervalo por Horário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Cadastra um novo intervalo por horario no Banco de Dados.
     * @param Connection 
     * @param A_intervalosPorHorarioN
     * @return Boolean
     */
    public static boolean setCadastrar(Connection con, A_intervalosPorHorarioN model){
        boolean cadastro = false;
        String sql = "insert into A_INTERVALOSPORHORARION (dat,usuarioid,nome,horarioinicial,horariofinal,seg,ter,qua,qui,sex,sab,dom) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, model.getDat());
            stmt.setInt(2, model.getUsuarioId());
            stmt.setString(3, model.getNome());
            stmt.setInt(4, model.getHorarioInicial());
            stmt.setInt(5, model.getHorarioFinal());
            stmt.setInt(6, model.getSeg());
            stmt.setInt(7, model.getTer());
            stmt.setInt(8, model.getQua());
            stmt.setInt(9, model.getQui());
            stmt.setInt(10, model.getSex());
            stmt.setInt(11, model.getSab());
            stmt.setInt(12, model.getDom());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Intervalo por Horário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
    /**
     * Deleta um Intervalo por Horario do Banco De Dados
     * @param Connection
     * @param ExameMODEL 
     * @return boolean
     */
    public static boolean setDeletar(Connection con, int intervaloPorHorarioId){
        boolean deleto = false; 
        String sql="delete from A_INTERVALOSPORHORARION where A_INTERVALOPORHORARIONID=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, intervaloPorHorarioId);
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Intervalo por Horário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
    
    //atualizar agenda
    public static boolean setAtualizar(Connection con, A_intervalosPorHorarioN model){
        boolean cadastro = false;
        String sql = "update A_INTERVALOSPORHORARION set dat=?, usuarioid=?, nome=?, horarioinicial=?, horariofinal=?, seg=?, ter=?, qua=?, qui=?, sex=?, sab=?, dom=? where A_INTERVALOPORHORARIONID=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, model.getDat());
            stmt.setInt(2, model.getUsuarioId());
            stmt.setString(3, model.getNome());
            stmt.setInt(4, model.getHorarioInicial());
            stmt.setInt(5, model.getHorarioFinal());
            stmt.setInt(6, model.getSeg());
            stmt.setInt(7, model.getTer());
            stmt.setInt(8, model.getQua());
            stmt.setInt(9, model.getQui());
            stmt.setInt(10, model.getSex());
            stmt.setInt(11, model.getSab());
            stmt.setInt(12, model.getDom());
            stmt.setInt(13, model.getA_intervaloPorHorarioNId());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Intervalo por Horário. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
}
