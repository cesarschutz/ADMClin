package br.bcn.admclin.dao.dbris;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe DAO da tabela DADOS_EMPRESA
 * @author Cesar Schutz
 */
public class DADOS_EMPRESA {
    /**
     * Busca as informações da empresa no banco de dados.
     * @return ResultSet
     * @throws SQLException 
     */
    public static ResultSet getConsultar(Connection con) throws SQLException{
        ResultSet resultSet = null;
        PreparedStatement stmtQuery = con.prepareStatement("select * from DADOS_EMPRESA");
        resultSet = stmtQuery.executeQuery();
        return resultSet;
    }
}