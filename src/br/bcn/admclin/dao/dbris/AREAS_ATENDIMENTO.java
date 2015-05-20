package br.bcn.admclin.dao.dbris;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.model.Areas_atendimento;
import br.bcn.admclin.dao.model.Tb_ClassesDeExames;

public class AREAS_ATENDIMENTO {
    
    static ArrayList<Areas_atendimento> listaAreasAtendimento = new ArrayList<Areas_atendimento>();
    static Connection con;
    
    
    @SuppressWarnings("finally")
    public static ArrayList<Areas_atendimento> getConsultar() {
        listaAreasAtendimento.clear();
        
        //cria a area de atendimento com handle = 0 com nome Selecione uma area de atendimento
        Areas_atendimento areaAtend = new Areas_atendimento();
        areaAtend.setNome("Selecione uma Área de Atendimento");
        areaAtend.setId_areas_atendimento(0);
        listaAreasAtendimento.add(areaAtend);
        
        ResultSet resultSet = null;
        con = Conexao.fazConexao();
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select * from areas_atendimento where id_areas_atendimento != 0 order by nome");
            resultSet = stmtQuery.executeQuery();
            while (resultSet.next()) {
                Areas_atendimento area = new Areas_atendimento();
                area.setNome(resultSet.getString("nome"));
                area.setId_areas_atendimento(resultSet.getInt("id_areas_atendimento"));
                listaAreasAtendimento.add(area);
            }
            Conexao.fechaConexao(con);
        } catch (SQLException e) {
            Conexao.fechaConexao(con);
            JOptionPane.showMessageDialog(null, "Erro ao consultar Agendas. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return listaAreasAtendimento;
        }
    }
    
    @SuppressWarnings("finally")
    public static ArrayList<Areas_atendimento> getConsultarComOpcaoDeTodasAsAreas() {
        listaAreasAtendimento.clear();
        ResultSet resultSet = null;
        con = Conexao.fazConexao();
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select * from areas_atendimento where id_areas_atendimento != 0 order by nome");
            resultSet = stmtQuery.executeQuery();
            Areas_atendimento area1 = new Areas_atendimento();
            area1.setNome("TODAS AS ÁREAS");
            area1.setId_areas_atendimento(0);
            listaAreasAtendimento.add(area1);
            while (resultSet.next()) {
                Areas_atendimento area = new Areas_atendimento();
                area.setNome(resultSet.getString("nome"));
                area.setId_areas_atendimento(resultSet.getInt("id_areas_atendimento"));
                listaAreasAtendimento.add(area);
            }
            Conexao.fechaConexao(con);
        } catch (SQLException e) {
            Conexao.fechaConexao(con);
            JOptionPane.showMessageDialog(null, "Erro ao consultar Areas de Atendimentos. Procure o Administrador." + e,
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return listaAreasAtendimento;
        }
    }
    
    @SuppressWarnings("finally")
    public static boolean setCadastrar(Areas_atendimento model) {
        boolean cadastro = false;
        con = Conexao.fazConexao();
        String sql = "insert into AREAS_ATENDIMENTO (nome) values(?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, model.getNome());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
            Conexao.fechaConexao(con);
        } catch (SQLException e) {
            Conexao.fechaConexao(con);
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Area de Atendimento. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return cadastro;
        }
    }
    
    @SuppressWarnings("finally")
    public static boolean setUpdate(Areas_atendimento model) {
        boolean atualizo = false;
        con = Conexao.fazConexao();
        String sql = "update areas_atendimento set nome=? where id_areas_atendimento=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, model.getNome());
            stmt.setInt(2, model.getId_areas_atendimento());
            stmt.executeUpdate();
            stmt.close();
            atualizo = true;
            Conexao.fechaConexao(con);
        } catch (SQLException e) {
            Conexao.fechaConexao(con);
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Área de Atendimento. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return atualizo;
        }
    }
    
    @SuppressWarnings("finally")
    public static boolean setDeletar(Areas_atendimento model) {
        boolean deleto = false;
        con = Conexao.fazConexao();
        String sql = "delete from areas_atendimento where id_areas_atendimento=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, model.getId_areas_atendimento());
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
            Conexao.fechaConexao(con);
        } catch (SQLException e) {
            Conexao.fechaConexao(con);
            JOptionPane.showMessageDialog(null, "Erro ao deletar Área de Atendimento. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return deleto;
        }
    }
}
