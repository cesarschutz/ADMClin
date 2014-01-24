package br.bcn.admclin.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.dbris.Conexao;

public class JLAUDOS {

    /**
     * Consulta o laudo de um atendimento
     * @return retorna ERRO se der erro e VAZIO se nao tiver nada
     */
    public static String getConsultarLaudo(int handle_at) {
        Connection con = Conexao.fazConexaoPAC();
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select laudo from jlaudos where handle_at = ?");
            stmtQuery.setInt(1, handle_at);
            resultSet = stmtQuery.executeQuery();
            String laudo = "vazio";
            while (resultSet.next()){
                laudo = resultSet.getString("laudo");
            }
            Conexao.fechaConexao(con);
            return laudo;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Laudo. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            Conexao.fechaConexao(con);
            return "erro";
        }
    }
    
    /**
     * Salva o laudo em um atendimento
     */
    @SuppressWarnings("finally")
    public static boolean setCadastrarLaudo(int handle_at, String laudo, String dataExame, String usr) {
        boolean cadastro = false;
        Connection con = Conexao.fazConexaoPAC();
        String sql = "update or insert into jlaudos (handle_at, laudo, flagsign, flagrisupdate, DATESIGN, usr) values(?,?,?,?,?,?) matching (handle_at)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1,handle_at);
            stmt.setString(2, laudo);
            stmt.setInt(3, 0);
            stmt.setInt(4, 0);
            stmt.setString(5, dataExame);
            stmt.setString(6, usr);
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Laudo. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
            Conexao.fechaConexao(con);
        } finally {
            return cadastro;
        }
    }
    
    /**
     * Consulta o flagsign de um registro
     * @throws SQLException 
     */
    public static int getConsultarFlagSign(int handle_at) throws SQLException {
        Connection con = Conexao.fazConexaoPAC();
        ResultSet resultSet = null;
        PreparedStatement stmtQuery = con.prepareStatement("select FLAGSIGN from jlaudos where handle_at = ?");
        stmtQuery.setInt(1, handle_at);
        resultSet = stmtQuery.executeQuery();
        int flagsign = 0;
        while (resultSet.next()){
            flagsign = resultSet.getInt("flagsign");
        }
        Conexao.fechaConexao(con);
        return flagsign;
    }
    
    /**
     * Consulta se existe o handle at na tabela STUDY_DONE
     * @throws SQLException 
     */
    public static boolean getConsultarStdAccession(int handle_at) throws SQLException {
        Connection con = Conexao.fazConexaoPAC();
        ResultSet resultSet = null;
        PreparedStatement stmtQuery = con.prepareStatement("select STDID from STUDY_DONE where STD_ACCESSION = ?");
        stmtQuery.setString(1, String.valueOf(handle_at));
        resultSet = stmtQuery.executeQuery();
        boolean retorno = false;
        while (resultSet.next()){
            retorno = true;
        }
        Conexao.fechaConexao(con);
        return retorno;
    }
    
    /**
     * Consulta o radiologista
     * @throws SQLException 
     */
    public static String getConsultarRadiologista(int handle_at) throws SQLException {
        Connection con = Conexao.fazConexaoPAC();
        ResultSet resultSet = null;
        PreparedStatement stmtQuery = con.prepareStatement("select radiologista from STUDY_DONE where STD_ACCESSION = ?");
        stmtQuery.setString(1, String.valueOf(handle_at));
        resultSet = stmtQuery.executeQuery();
        String retorno = "";
        while (resultSet.next()){
            retorno = resultSet.getString("radiologista");
        }
        Conexao.fechaConexao(con);
        return retorno;
    }
    
    /**
     * Salva os Flags de assinatura de laudo
     * @throws SQLException 
     */
    public static void setAssinarComStudyDone(int flagSign, int flagRisUpdate, String flag2, String radiologista, int status1) throws SQLException{
        Connection conPac = Conexao.fazConexaoPAC();
        Connection conRis = Conexao.fazConexao();
        
        conPac.setAutoCommit(false);
        conRis.setAutoCommit(false);
        
        
    }
    
    /**
     * Salva os Flags de assinatura de laudo
     * @throws SQLException 
     */
    public static void setAssinarSemStudyDone(int flagSign, int flagRisUpdate, int status1) throws SQLException{
        
    }
    
}
