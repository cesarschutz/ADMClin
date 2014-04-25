package br.bcn.admclin.dao.dbris;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.model.Nagenda;
import br.bcn.admclin.dao.model.Nagendasdesc;


public class NAGENDASDESC {

    static ArrayList<Nagendasdesc> listaAgendas = new ArrayList<Nagendasdesc>();
    static Connection con;
    
    @SuppressWarnings("finally")
    public static ArrayList<Nagendasdesc> getConsultar() {
        listaAgendas.clear();
        ResultSet resultSet = null;
        con = Conexao.fazConexao();
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select * from NAGENDASDESC order by name");
            resultSet = stmtQuery.executeQuery();
            while (resultSet.next()) {
                Nagendasdesc agenda = new Nagendasdesc();
                agenda.setNagdid(resultSet.getInt("nagdid"));
                agenda.setName(resultSet.getString("name"));
                agenda.setDescricao(resultSet.getString("descricao"));
                agenda.setAtiva(resultSet.getInt("ativa"));
                agenda.setId_areas_atendimento(resultSet.getInt("id_areas_atendimento"));                
                listaAgendas.add(agenda);
            }
            Conexao.fechaConexao(con);
        } catch (SQLException e) {
            Conexao.fechaConexao(con);
            listaAgendas.clear();
            JOptionPane.showMessageDialog(null, "Erro ao consultar Agendas. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return listaAgendas;
        }
    }
    
    public static boolean setCadastrar(Nagendasdesc agenda, ArrayList<Nagenda> turnosAgenda){
        
        try {
            con = Conexao.fazConexao();
            con.setAutoCommit(false);
            
            //salvando agenda
            String sqlAgenda = "insert into nagendasdesc (name, descricao, ativa, id_areas_atendimento) values(?,?,?,?)";
            PreparedStatement stmtAgenda = con.prepareStatement(sqlAgenda, Statement.RETURN_GENERATED_KEYS);
            stmtAgenda.setString(1, agenda.getName());
            stmtAgenda.setString(2, agenda.getDescricao());
            stmtAgenda.setInt(3, agenda.getAtiva());
            stmtAgenda.setInt(4, agenda.getId_areas_atendimento());
            stmtAgenda.execute();
            stmtAgenda.close();
            
            //buscando a chave primaria gerada
            String sqlBuscaChavePrimaria = "select max(nagdid) from nagendasdesc";
            PreparedStatement stmtBuscaChave = con.prepareStatement(sqlBuscaChavePrimaria);
            ResultSet rsBuscaChave = stmtBuscaChave.executeQuery();
            int chavePrimariaAgenda = 0;
            while (rsBuscaChave.next()) {
                chavePrimariaAgenda = rsBuscaChave.getInt("MAX");
            }

            //cadastrando os turnos
            
            
            
            
            
            
            
            con.commit();
            Conexao.fechaConexao(con);
            return true;
        } catch (Exception e) {
            System.out.println("erro: " + e);
            //se der erro fecha a conexao e retorna false
            try {
                con.rollback();
                Conexao.fechaConexao(con);
                return false;
            } catch (SQLException e1) {
                return false;
            }
        }
    }
}
