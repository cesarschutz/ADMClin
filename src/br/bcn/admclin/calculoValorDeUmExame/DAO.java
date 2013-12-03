/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.calculoValorDeUmExame;

import java.sql.*;

import javax.swing.JOptionPane;

/**
 *
 * @author CeSaR
 */
public class DAO {
    
    /**
     * Consultando os materiais do exame
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarMateriaisDoExame(Connection con, int handle_convenio, int handle_exame){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select t.handle_material, t.qtdMaterial, m.codigo, m.nome as nomeMaterial from tabelas as t INNER JOIN materiais as m on m.handle_material = t.handle_material where t.handle_material!=0 and t.handle_convenio=? and t.handle_exame=?");
            stmtQuery.setInt(1, handle_convenio);
            stmtQuery.setInt(2, handle_exame);
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Exames. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Consultando os valores dos materiais do exame
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarValoresMateriaisDoExame(Connection con,int handle_material, Date dataDoExame){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select * from valoresMateriais where handle_material=? and dataAValer <=? order by dataAValer");
            stmtQuery.setInt(1, handle_material);
            stmtQuery.setDate(2, dataDoExame);
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Exames. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Consultando a porcentagem  do convenio e do paciente
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarPorcentagemDePagamentoDeClienteEConvenio(Connection con, int handle_convenio){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select porcentPaciente, porcentConvenio from convenio where handle_convenio=?");
            stmtQuery.setInt(1, handle_convenio);
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar porcentagem de Paciente. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    /**
     * Consultando a o redutor do convenio
     */
    @SuppressWarnings("finally")
    public static ResultSet getRedutorDoConvenio(Connection con, int handle_convenio){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select redutor from convenio where handle_convenio=?");
            stmtQuery.setInt(1, handle_convenio);
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar o Redutor do Convênio. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Consultando os valores de ch do convenio de acordo com a data selecionada
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarValorDoCHDoConvenio(Connection con,int handle_convenio, Date dataDoExame){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select * from convenioCH where handle_convenio=? and dataAValer <=? order by dataAValer");
            stmtQuery.setInt(1, handle_convenio);
            stmtQuery.setDate(2, dataDoExame);
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar valor de CH do Convênio. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Consultando os valores de filme do convenio de acordo com a data selecionada
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarValorDoFILMEDoConvenio(Connection con,int handle_convenio, Date dataDoExame){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select * from convenioFILME where handle_convenio=? and dataAValer <=? order by dataAValer");
            stmtQuery.setInt(1, handle_convenio);
            stmtQuery.setDate(2, dataDoExame);
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar valor de FILME do Convênio. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    /**
     * Consultando o valor do exame naquele convenio
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarValoresDoExame(Connection con, int handle_convenio, int handle_exame){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select cofch1, cofch2, coefFilme from tabelas where handle_convenio=? and handle_exame=?");
            stmtQuery.setInt(1, handle_convenio);
            stmtQuery.setInt(2, handle_exame);
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Valores do Exame. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    
}
