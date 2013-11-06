/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu.atendimentos.agenda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author CeSaR
 */
public class conveniosDAO {
    /**
     * Consulta Todos os Convênios existentes no Banco de Dados para colocar na lista!!!
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultar(Connection con){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select handle_convenio, nome, porcentPaciente, porcentConvenio from convenio order by nome");
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Convênios. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    /**
     * Consulta Todos os valores de ch existentes no Banco de Dados para colocar na lista!!!
     * @param Connection
     * @return ResultSet
     */
    public static boolean conseguiuConsultaCH = false;
    public static String getConsultarCh(Connection con, int handle_convenio){
        String valorCh = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select valor from convenioCh where handle_convenio=? order by dataAValer");
            stmtQuery.setInt(1, handle_convenio);
            ResultSet resultSet = stmtQuery.executeQuery();
            while(resultSet.next()){
                valorCh = resultSet.getString("valor");
            }
            conseguiuConsultaCH = false;
        }catch(SQLException e){
            conseguiuConsultaCH = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar Valores de CH do Convênio. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return valorCh;
        }
    }
    

    /**
     * Consulta Todos os valores de filme no Banco de Dados para colocar na lista!!!
     * @param Connection
     * @return ResultSet
     */
    public static boolean conseguiuConsultaFilme = false;
    public static String getConsultarFilme(Connection con, int handle_convenio){
        String valorFilme = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select valor from convenioFilme where handle_convenio=? order by dataAValer");
            stmtQuery.setInt(1, handle_convenio);
            ResultSet resultSet = stmtQuery.executeQuery();
            while(resultSet.next()){
                valorFilme = resultSet.getString("valor");
            }
            conseguiuConsultaFilme = false;
        }catch(SQLException e){
            conseguiuConsultaFilme = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar Valores de Filme do Convênio. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return valorFilme;
        }
    }
}
