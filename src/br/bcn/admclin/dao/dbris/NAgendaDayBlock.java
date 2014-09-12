package br.bcn.admclin.dao.dbris;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.model.A_feriadosN;
import br.bcn.admclin.dao.model.Nagendadayblock;
public class NAgendaDayBlock {
	
	static Connection con;

	@SuppressWarnings("finally")
    public static ArrayList<Nagendadayblock> getConsultar(int weekDay, int NAGDID) {
        ArrayList<Nagendadayblock> listaBloqueios = new ArrayList<Nagendadayblock>();
        listaBloqueios.clear();
        ResultSet resultSet = null;
        con = Conexao.fazConexao();
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select * from Nagendadayblock where weekday = ? and NAGDID = ?");
            stmtQuery.setInt(1, weekDay);
            stmtQuery.setInt(2, NAGDID);
            resultSet = stmtQuery.executeQuery();
            while (resultSet.next()) {
            	Nagendadayblock bloqueio = new Nagendadayblock();
            	bloqueio.setAGENDADBID(resultSet.getInt("AGENDADBID"));
            	bloqueio.setNAGDID(resultSet.getInt("NAGDID"));
            	bloqueio.setWEEKDAY(resultSet.getInt("WEEKDAY"));
            	bloqueio.setHORARIO(resultSet.getInt("HORARIO"));              
            	listaBloqueios.add(bloqueio);
            }
            Conexao.fechaConexao(con);
        } catch (SQLException e) {
            Conexao.fechaConexao(con);
            listaBloqueios.clear();
            JOptionPane.showMessageDialog(null, "Erro ao consultar Bloqueios. Procure o Administrador." + e,
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return listaBloqueios;
        }
    }
	
	 @SuppressWarnings("finally")
	    public static boolean setCadastrar(int handle_agenda, int weekDay, int horario) {
	        String sql = "insert into NAgendaDayBlock (NAGDID,WEEKDAY,HORARIO) values(?,?,?)";
	        try {
	        	con = Conexao.fazConexao();
	            PreparedStatement stmt = con.prepareStatement(sql);
	            stmt.setInt(1, handle_agenda);
	            stmt.setInt(2, weekDay);
	            stmt.setInt(3, horario);
	            stmt.executeUpdate();
	            stmt.close();
	            Conexao.fechaConexao(con);
	            return true;
	        } catch (SQLException e) {
	        	Conexao.fechaConexao(con);
	            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Bloqueio. Procure o Administrador.", "ERRO",
	                javax.swing.JOptionPane.ERROR_MESSAGE);
	            return false;
	        }
	    }
	 
	 @SuppressWarnings("finally")
	    public static boolean setDeletar(int handle_agenda, int weekDay, int horario) {
	        String sql = "delete from NAgendaDayBlock where NAGDID = ? and  WEEKDAY = ? and HORARIO = ?";
	        try {
	        	con = Conexao.fazConexao();
	            PreparedStatement stmt = con.prepareStatement(sql);
	            stmt.setInt(1, handle_agenda);
	            stmt.setInt(2, weekDay);
	            stmt.setInt(3, horario);
	            stmt.executeUpdate();
	            stmt.close();
	            Conexao.fechaConexao(con);
	            return true;
	        } catch (SQLException e) {
	        	Conexao.fechaConexao(con);
	            JOptionPane.showMessageDialog(null, "Erro ao Desbloquear. Procure o Administrador." + e, "ERRO",
	                javax.swing.JOptionPane.ERROR_MESSAGE);
	            return false;
	        }
	    }
	
}
