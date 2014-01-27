package br.bcn.admclin.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.USUARIOS;

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
    public static boolean setCadastrarLaudo(boolean comStudyDone, int handle_at, String laudo, String dataExame, String usr) {        
        Connection conPac = null;
        Connection conRis = null;
        try {
            conPac = Conexao.fazConexaoPAC();
            conRis = Conexao.fazConexao();
            
            conPac.setAutoCommit(false);
            conRis.setAutoCommit(false);
            
            //muda flags na tabela jlaudos do DB pac
            String sql = "update or insert into jlaudos (handle_at, laudo, flagsign, flagrisupdate, DATESIGN, usr) values(?,?,?,?,?,?) matching (handle_at)";
            PreparedStatement stmt = conPac.prepareStatement(sql);
            stmt.setInt(1,handle_at);
            stmt.setString(2, laudo);
            stmt.setInt(3, 0);
            stmt.setInt(4, 0);
            stmt.setString(5, dataExame);
            stmt.setString(6, usr);
            stmt.executeUpdate();
            
            if(comStudyDone){
                //muda flags na tabela study_done do pac
                String sql2 = "update study_done set flag2 = ?, radiologista = ? where handle_at = ?";
                PreparedStatement stmt2 = conPac.prepareStatement(sql2);
                stmt2.setString(1, "0");
                stmt2.setString(2, USUARIOS.nomeUsuario);
                stmt2.setInt(3, handle_at);
                stmt2.executeUpdate();
            }
            
            
            
            //muda flag no atendimento do RIS
            String sql3 = "update atendimentos set status1=? where handle_at = ?";
            PreparedStatement stmt3 = conRis.prepareStatement(sql3);
            stmt3.setInt(1, 4);
            stmt3.setInt(2, handle_at);
            stmt3.executeUpdate();
            
            
            conPac.commit();
            conRis.commit();
            Conexao.fechaConexao(conRis);
            Conexao.fechaConexao(conPac);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao Salvar Laudo. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
            try {
                conPac.rollback();
                conRis.rollback();
                Conexao.fechaConexao(conRis);
                Conexao.fechaConexao(conPac);
                return false;
            } catch (SQLException e1) {
                Conexao.fechaConexao(conRis);
                Conexao.fechaConexao(conPac);
                return false;
            }
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
    public static boolean setAssinarComStudyDone(int flagSign, int flagRisUpdate, String flag2, String radiologista, int status1, int handle_at){
    	Connection conPac = null;
        Connection conRis = null;
    	try {
        	conPac = Conexao.fazConexaoPAC();
            conRis = Conexao.fazConexao();
            
            conPac.setAutoCommit(false);
            conRis.setAutoCommit(false);
            
            //muda flags na tabela jlaudos do DB pac
            String sql = "update jlaudos set flagSign=?, flagRisUpdate = ? where handle_at = ?";
            PreparedStatement stmt = conPac.prepareStatement(sql);
            stmt.setInt(1,flagSign);
            stmt.setInt(2, flagRisUpdate);
            stmt.setInt(3, handle_at);
            stmt.executeUpdate();
            
            //muda flags na tabela study_done do pac
            String sql2 = "update study_done set flag2 = ?, radiologista = ? where handle_at = ?";
            PreparedStatement stmt2 = conPac.prepareStatement(sql2);
            stmt2.setString(1,flag2);
            stmt2.setString(2, radiologista);
            stmt2.setInt(3, handle_at);
            stmt2.executeUpdate();
            
            
            //muda flag no atendimento do RIS
            String sql3 = "update atendimentos set status1=? where handle_at = ?";
            PreparedStatement stmt3 = conRis.prepareStatement(sql3);
            stmt3.setInt(1,status1);
            stmt3.setInt(2, handle_at);
            stmt3.executeUpdate();
            
            
            conPac.commit();
			conRis.commit();
			Conexao.fechaConexao(conRis);
            Conexao.fechaConexao(conPac);
			return true;
		} catch (Exception e) {
		    JOptionPane.showMessageDialog(null, "Erro ao Assinar Laudo. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
			try {
				conPac.rollback();
				conRis.rollback();
				Conexao.fechaConexao(conRis);
	            Conexao.fechaConexao(conPac);
	            return false;
			} catch (SQLException e1) {
				Conexao.fechaConexao(conRis);
	            Conexao.fechaConexao(conPac);
	            return false;
			}
		}
    }
    
    /**
     * Salva os Flags de assinatura de laudo
     * @throws SQLException 
     */
    public static boolean setAssinarSemStudyDone(int flagSign, int flagRisUpdate, int status1, int handle_at){
        Connection conPac = null;
        Connection conRis = null;
        try {
            conPac = Conexao.fazConexaoPAC();
            conRis = Conexao.fazConexao();
            
            conPac.setAutoCommit(false);
            conRis.setAutoCommit(false);
            
            //muda flags na tabela jlaudos do DB pac
            String sql = "update jlaudos set flagSign=?, flagRisUpdate = ? where handle_at = ?";
            PreparedStatement stmt = conPac.prepareStatement(sql);
            stmt.setInt(1,flagSign);
            stmt.setInt(2, flagRisUpdate);
            stmt.setInt(3, handle_at);
            stmt.executeUpdate();        
            
            //muda flag no atendimento do RIS
            String sql3 = "update atendimentos set status1=? where handle_at = ?";
            PreparedStatement stmt3 = conRis.prepareStatement(sql3);
            stmt3.setInt(1,status1);
            stmt3.setInt(2, handle_at);
            stmt3.executeUpdate();            
            
            conPac.commit();
            conRis.commit();
            
            Conexao.fechaConexao(conRis);
            Conexao.fechaConexao(conPac);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao Assinar Laudo. Procure o Administrador." + e,
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
            try {
                conPac.rollback();
                conRis.rollback();
                Conexao.fechaConexao(conRis);
                Conexao.fechaConexao(conPac);
                return false;
            } catch (SQLException e1) {
                Conexao.fechaConexao(conRis);
                Conexao.fechaConexao(conPac);
                return false;
            }
        }
    }
    
}
