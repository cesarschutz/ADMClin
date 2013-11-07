package br.bcn.admclin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Cesar Schutz
 */
public class DADOS_EMPRESA {
    /*
     * Busca as informações da empresa no banco de dados.
     */
    public static ResultSet getConsultar(Connection con) throws SQLException{
        ResultSet resultSet = null;
        PreparedStatement stmtQuery = con.prepareStatement("select * from DADOS_EMPRESA");
        resultSet = stmtQuery.executeQuery();
        return resultSet;
    }
}
