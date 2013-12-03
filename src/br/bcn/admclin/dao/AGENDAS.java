/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.model.Agenda;

/**
 *
 * @author BCN
 */
public class AGENDAS {
    public static boolean conseguiuConsulta;
    
    /**
     * Consulta os dados de uma Agenda
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarDadosDeUmaAgenda(Connection con, int handle_agenda){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select * from agendas where handle_agenda=?");
        stmtQuery.setInt(1, handle_agenda);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Agendas. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Verifica se nome de agenda ja existe sem se o ID q estamos trabalhando!
     * @param Connection
     * @param conveniosMODEL
     * @return boolean
     */
    public static boolean getConsultarParaSalvarAtualizarRegistro(Connection con, Agenda model){
       boolean existe = true;
        try{
          PreparedStatement stmtQuery = con.prepareStatement("select * from agendas where nome=? and handle_agenda!=?");
          stmtQuery.setString(1, model.getNome());
          stmtQuery.setInt(2, model.getHandle_agenda());
          ResultSet resultSet = stmtQuery.executeQuery();
          if(!resultSet.next()){
                existe = false;
            }
          conseguiuConsulta = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Agenda já existe. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return existe;
        }
    }
    /**
     * Verifica se Classe De Exame já existe antes de cadastra-lo no Banco de Dados.
     * @param Connection
     * @param Agenda
     * @return boolean
     */
    public static boolean getConsultarParaSalvarNovoRegistro(Connection con, Agenda model){
       boolean existe = true;
        try{
          PreparedStatement stmtQuery = con.prepareStatement("select * from agendas where nome=?");
          stmtQuery.setString(1, model.getNome());
          ResultSet resultSet = stmtQuery.executeQuery();
          if(!resultSet.next()){
                existe = false;
            }
          conseguiuConsulta = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Agenda já existe. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return existe;
        }
    }
    
    /**
     * Cadastra uma nova agenda no Banco de Dados.
     * @param Connection 
     * @param agendasModel
     * @return Boolean
     */
    public static boolean setCadastrar(Connection con, Agenda model){
        boolean cadastro = false;
        String sql = "insert into agendas (dat,usuarioid,nome,descricao,horarioinicialTurno1,horariofinalTurno1,horarioinicialTurno2,horariofinalTurno2,horarioinicialTurno3,horariofinalTurno3,horarioinicialTurno4,horariofinalTurno4,"
                + "duracaoturno1,duracaoturno2,duracaoturno3,duracaoturno4,seg,ter,qua,qui,sex,sab,dom,modalidade_cr,modalidade_ct,modalidade_dr,modalidade_dx,modalidade_mg,modalidade_mr,modalidade_nm,"
                + "modalidade_ot,modalidade_rf,modalidade_do,modalidade_us,modalidade_od, modalidade_tr, ativa) "
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, model.getData());
            stmt.setInt(2, model.getUsuarioId());
            stmt.setString(3, model.getNome());
            stmt.setString(4, model.getDescricao());
            stmt.setInt(5, model.getHorarioInicialTurno1());
            stmt.setInt(6, model.getHorarioFinalTurno1());
            stmt.setInt(7, model.getHorarioInicialTurno2());
            stmt.setInt(8, model.getHorarioFinalTurno2());
            stmt.setInt(9, model.getHorarioInicialTurno3());
            stmt.setInt(10, model.getHorarioFinalTurno3());
            stmt.setInt(11, model.getHorarioInicialTurno4());
            stmt.setInt(12, model.getHorarioFinalTurno4());
            stmt.setInt(13, model.getDuracaoTurno1());
            stmt.setInt(14, model.getDuracaoTurno2());
            stmt.setInt(15, model.getDuracaoTurno3());
            stmt.setInt(16, model.getDuracaoTurno4());
            stmt.setInt(17, model.getSeg());
            stmt.setInt(18, model.getTer());
            stmt.setInt(19, model.getQua());
            stmt.setInt(20, model.getQui());
            stmt.setInt(21, model.getSex());
            stmt.setInt(22, model.getSab());
            stmt.setInt(23, model.getDom());
            stmt.setInt(24, model.getMODALIDADE_CR());
            stmt.setInt(25, model.getMODALIDADE_CT());
            stmt.setInt(26, model.getMODALIDADE_DR());
            stmt.setInt(27, model.getMODALIDADE_DX());
            stmt.setInt(28, model.getMODALIDADE_MG());
            stmt.setInt(29, model.getMODALIDADE_MR());
            stmt.setInt(30, model.getMODALIDADE_NM());
            stmt.setInt(31, model.getMODALIDADE_OT());
            stmt.setInt(32, model.getMODALIDADE_RF());
            stmt.setInt(33, model.getMODALIDADE_DO());
            stmt.setInt(34, model.getMODALIDADE_US());
            stmt.setInt(35, model.getMODALIDADE_OD());
            stmt.setInt(36, model.getMODALIDADE_TR());
            stmt.setInt(37, model.getAtiva());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Agenda. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    //atualizar agenda
    public static boolean setAtualizar(Connection con, Agenda model){
        boolean cadastro = false;
        String sql = "update agendas set dat=?, usuarioid=?, nome=?, descricao=?, horarioinicialturno1=?, horariofinalturno1=?, horarioinicialturno2=?, horariofinalturno2=?, horarioinicialturno3=?, horariofinalturno3=?, horarioinicialturno4=?, horariofinalturno4=?, duracaoturno1=?, duracaoturno2=?, duracaoturno3=?, duracaoturno4=?,  seg=?, ter=?, qua=?, qui=?, sex=?, sab=?, dom=?,"
                + " modalidade_cr=?, modalidade_ct=?, modalidade_dr=?, modalidade_dx=?, modalidade_mg=?, modalidade_mr=?, modalidade_nm=?, modalidade_ot=?, modalidade_rf=?, modalidade_do=?,"
                + " modalidade_us=?, modalidade_od=?, modalidade_tr=?, ativa=? where handle_agenda=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, model.getData());
            stmt.setInt(2, model.getUsuarioId());
            stmt.setString(3, model.getNome());
            stmt.setString(4, model.getDescricao());
            stmt.setInt(5, model.getHorarioInicialTurno1());
            stmt.setInt(6, model.getHorarioFinalTurno1());
            stmt.setInt(7, model.getHorarioInicialTurno2());
            stmt.setInt(8, model.getHorarioFinalTurno2());
            stmt.setInt(9, model.getHorarioInicialTurno3());
            stmt.setInt(10, model.getHorarioFinalTurno3());
            stmt.setInt(11, model.getHorarioInicialTurno4());
            stmt.setInt(12, model.getHorarioFinalTurno4());
            stmt.setInt(13, model.getDuracaoTurno1());
            stmt.setInt(14, model.getDuracaoTurno2());
            stmt.setInt(15, model.getDuracaoTurno3());
            stmt.setInt(16, model.getDuracaoTurno4());
            stmt.setInt(17, model.getSeg());
            stmt.setInt(18, model.getTer());
            stmt.setInt(19, model.getQua());
            stmt.setInt(20, model.getQui());
            stmt.setInt(21, model.getSex());
            stmt.setInt(22, model.getSab());
            stmt.setInt(23, model.getDom());
            stmt.setInt(24, model.getMODALIDADE_CR());
            stmt.setInt(25, model.getMODALIDADE_CT());
            stmt.setInt(26, model.getMODALIDADE_DR());
            stmt.setInt(27, model.getMODALIDADE_DX());
            stmt.setInt(28, model.getMODALIDADE_MG());
            stmt.setInt(29, model.getMODALIDADE_MR());
            stmt.setInt(30, model.getMODALIDADE_NM());
            stmt.setInt(31, model.getMODALIDADE_OT());
            stmt.setInt(32, model.getMODALIDADE_RF());
            stmt.setInt(33, model.getMODALIDADE_DO());
            stmt.setInt(34, model.getMODALIDADE_US());
            stmt.setInt(35, model.getMODALIDADE_OD());
            stmt.setInt(36, model.getMODALIDADE_TR());
            stmt.setInt(37, model.getAtiva());
            stmt.setInt(38, model.getHandle_agenda());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Agenda. Procure o Administrador." + e,"ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
    /**
     * Consulta Todas as Agendas existentes no Banco de Dados para colocar nos comboBox
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultar(Connection con){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select handle_agenda, nome, ativa from agendas order by nome");
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao preencher as Agendas. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
}
