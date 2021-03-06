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
    public static ResultSet getConsultar(Connection con, int id_dados_empresa) throws SQLException{
        ResultSet resultSet = null;
        PreparedStatement stmtQuery = con.prepareStatement("select * from DADOS_EMPRESA where dados_empresa_id = ?");
        stmtQuery.setInt(1, id_dados_empresa);
        resultSet = stmtQuery.executeQuery();
        return resultSet;
    }
    
    public static ResultSet getConsultarEmpresa(Connection con) throws SQLException{
        ResultSet resultSet = null;
        PreparedStatement stmtQuery = con.prepareStatement("select * from DADOS_EMPRESA");
        resultSet = stmtQuery.executeQuery();
        return resultSet;
    }
    
    public static int getConsultarIdDadosEmpresaDeUmAtendimento(Connection con, int handle_at) throws SQLException{
        ResultSet resultSet = null;
        PreparedStatement stmtQuery = con.prepareStatement("select id_dados_empresa from atendimentos " +
        		"join convenio on atendimentos.handle_convenio = convenio.handle_convenio " +
        		"where atendimentos.handle_at = ?");
        stmtQuery.setInt(1, handle_at);
        resultSet = stmtQuery.executeQuery();
        while (resultSet.next()){
			return resultSet.getInt("id_dados_empresa");
		}
        return 0;
    }
}
