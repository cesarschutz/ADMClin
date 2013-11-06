/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ClasseAuxiliares;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author BCN
 */
public class dados_empresa_dao {
    /*
     * metodo que busca as informações da empresa
     */
    public static ResultSet getConsultar(Connection con){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select * from dados_empresa");
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar dados da Empresa. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
}
