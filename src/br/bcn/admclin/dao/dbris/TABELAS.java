/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.dao.dbris;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.model.Tabelas;

/**
 *
 * @author Cesar Schutz
 */
public class TABELAS {
    
    public static boolean conseguiuConsulta;
    /**
     * Consulta Todos os Exames vinculados a uma determinada tabela
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarExamesDaTabela(Connection con, int handle_convenio, String modalidade){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select distinct a.nome, a.handle_exame, a.modalidade, b.cofch1, b.cofch2, b.coeffilme, b.sinonimo, b.cod_exame from exames a inner join tabelas b on a.handle_exame = b.handle_exame where b.handle_convenio ="+handle_convenio+" and a.modalidade='"+modalidade+"'");
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Exames da Tabela. Procure o Administrador." + e,"ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    /**
     * Consulta todos os dados de um exame de uma tabela, coeficientes e materiais
     * @param con
     * @param handle_convenio
     * @param handle_exame
     * @return 
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarDadosDeUmExame(Connection con, int handle_convenio, String handle_exame){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select a.handle_exame, a.coeffilme, a.cofch1, a.cofch2, a.handle_material, b.nome, a.qtdmaterial, a.tabelaId from tabelas as a inner join materiais as b on a.handle_material = b.handle_material where handle_convenio =" + handle_convenio + " and handle_exame ="+handle_exame);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Dados do Exame. Procure o Administrador."+e,"ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    /**
     * Cadastra um exame em uma tabela!
     * @param con
     * @param model
     * @return 
     */
    @SuppressWarnings("finally")
    public static boolean cadastrarExameAUmaTabela(Connection con, Tabelas model){
        boolean cadastro = false;
        String sqlInserirExameAUmaTabela = "insert into tabelas (usuarioid,dat, handle_convenio, handle_exame, coeffilme, cofch1, cofch2, handle_material, qtdmaterial, cod_exame, sinonimo, VAI_MATERIAIS_POR_PADRAO) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement stmt = con.prepareStatement(sqlInserirExameAUmaTabela);
            stmt.setInt(1, model.getUsuarioId());
            stmt.setDate(2, model.getDat());
            stmt.setInt(3, model.gethandle_convenio());
            stmt.setInt(4, model.gethandle_exame());
            stmt.setDouble(5, model.getCofFilme());
            stmt.setDouble(6, model.getCofCh1());
            stmt.setDouble(7, model.getCofCh2());
            stmt.setInt(8, model.gethandle_material());
            stmt.setInt(9, model.getQtdMaterial());
            stmt.setString(10, model.getCod_exame());
            stmt.setString(11, model.getSinonimo());
            stmt.setInt(12, model.getVAI_MATERIAIS_POR_PADRAO());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Exame na Tabela. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
    /**Verifica se um material esta sendo utilizado em alguma tabela antes de apaga-lo
     * 
     * @param handle_material 
     */
    @SuppressWarnings("finally")
    public static boolean verificarSeMaterialEstaSendoUtilizado(Connection con, int handle_material){
        boolean utilizada = true;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select * from tabelas where handle_material=" + handle_material);
        ResultSet resultSet = stmtQuery.executeQuery();
        if(!resultSet.next()){
                utilizada = false;
        }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar se material está sendo utilizado. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return utilizada;
        }
    }
    
    /**Verifica se um exame esta sendo utilizado em alguma tabela antes de apaga-lo
     * 
     * @param handle_exame 
     */
    @SuppressWarnings("finally")
    public static boolean verificarSeExameEstaSendoUtilizado(Connection con, int HANDLE_EXAME){
        boolean utilizada = true;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select * from tabelas where handle_exame=" + HANDLE_EXAME);
        ResultSet resultSet = stmtQuery.executeQuery();
        if(!resultSet.next()){
                utilizada = false;
        }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar se exame está sendo utilizado. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return utilizada;
        }
    }
    /**
     * Deleta um Exame de uma tabela.
     * @param Connection
     * @param UsuarioModel 
     */
    @SuppressWarnings("finally")
    public static boolean setDeletarUmExame(Connection con, String handle_exame, String handle_convenio){
        boolean deleto = false;
        String sql="delete from tabelas where handle_exame=? and handle_convenio=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, handle_exame);
            stmt.setString(2, handle_convenio);
            stmt.executeUpdate();
            stmt.close();    
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Exame da Tabela. Procure o Administrador","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
    /**
     * Deleta um material de um Exame de uma tabela.
     * @param Connection
     * @param UsuarioModel 
     */
    @SuppressWarnings("finally")
    public static boolean setDeletarUmMaterialDeUmExame(Connection con, String handle_material, String tabeleId){
        boolean deleto = false;
        String sql="delete from tabelas where handle_material=? and tabelaId=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, handle_material);
            stmt.setString(2, tabeleId);
            stmt.executeUpdate();
            stmt.close();    
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Material do Exame. Procure o Administrador","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
    /**
     * Atualiza os coeficientes de um exame!
     * @param Connection
     * @param ExameModel 
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setUpdateCoeficientesDeUmExame(Connection con, Tabelas exame){
        boolean cadastro = false;
        String sql = "update tabelas set usuarioid=?, dat=?, cofch1=?, cofch2=?, coeffilme=?, cod_exame=?, sinonimo=? where handle_convenio=? and handle_exame=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, exame.getUsuarioId());
            stmt.setDate(2, exame.getDat());
            stmt.setDouble(3, exame.getCofCh1());
            stmt.setDouble(4, exame.getCofCh2());
            stmt.setDouble(5, exame.getCofFilme());
            stmt.setString(6, exame.getCod_exame());
            stmt.setString(7, exame.getSinonimo());
            stmt.setInt(8, exame.gethandle_convenio());
            stmt.setInt(9, exame.gethandle_exame());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao atualizar coeficientes do Exame. Procure o administrador." + e,"ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
    /**
     * Consulta as modalidade
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarModalidades(Connection con){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select modalidade from modalidades order by modalidade");
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Modalidades. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }
}
