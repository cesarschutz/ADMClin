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
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;


public class NAGENDASDESC {

    
    static Connection con;
    
    @SuppressWarnings("finally")
    public static ArrayList<Nagendasdesc> getConsultar() {
        ArrayList<Nagendasdesc> listaAgendas = new ArrayList<Nagendasdesc>();
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
    
    @SuppressWarnings("finally")
    public static ArrayList<Nagenda> getTurnosDaAgenda(int nagdid) {
        ArrayList<Nagenda> listaTurno = new ArrayList<Nagenda>();
        listaTurno.clear();
        ResultSet resultSet = null;
        con = Conexao.fazConexao();
        try {
            PreparedStatement stmtQuery = con.prepareStatement("select * from nagenda where nagdid = ?  order by weekday");
            stmtQuery.setInt(1, nagdid);
            resultSet = stmtQuery.executeQuery();
            while (resultSet.next()) {
                Nagenda turno = new Nagenda();
                turno.setWeekday(resultSet.getInt("weekday"));              
                turno.setDuracao(resultSet.getInt("duracao"));              
                turno.setStart1(resultSet.getInt("start1"));              
                turno.setEnd1(resultSet.getInt("end1")); 
                turno.setStart2(resultSet.getInt("start2"));              
                turno.setEnd2(resultSet.getInt("end2"));  
                turno.setStart3(resultSet.getInt("start3"));              
                turno.setEnd3(resultSet.getInt("end3"));  
                turno.setStart4(resultSet.getInt("start4"));              
                turno.setEnd4(resultSet.getInt("end4"));  
                listaTurno.add(turno);
            }
            Conexao.fechaConexao(con);
        } catch (SQLException e) {
            Conexao.fechaConexao(con);
            listaTurno.clear();
            JOptionPane.showMessageDialog(null, "Erro ao consultar Turnos da Agenda. Procure o Administrador." + e,
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return listaTurno;
        }
    }
    
    public static boolean setCadastrar(Nagendasdesc agenda, ArrayList<Nagenda> turnosAgenda){
        
        try {
            con = Conexao.fazConexao();
            con.setAutoCommit(false);
            
            //salvando agenda
            String sqlAgenda = "insert into nagendasdesc (name, descricao, ativa, id_areas_atendimento) values(?,?,?,?)";
            PreparedStatement stmtAgenda = con.prepareStatement(sqlAgenda);
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
            for (Nagenda nagenda : turnosAgenda) {
                String sqlTurno = "insert into nagenda (nagdid, weekday, duracao, start1, end1, start2, end2, start3, end3, start4, end4) values(?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement stmtTurnosAgenda = con.prepareStatement(sqlTurno);
                stmtTurnosAgenda.setInt(1, chavePrimariaAgenda);
                stmtTurnosAgenda.setInt(2, nagenda.getWeekday());
                stmtTurnosAgenda.setInt(3, nagenda.getDuracao());
                stmtTurnosAgenda.setInt(4, nagenda.getStart1());
                stmtTurnosAgenda.setInt(5, nagenda.getEnd1());
                stmtTurnosAgenda.setInt(6, nagenda.getStart2());
                stmtTurnosAgenda.setInt(7, nagenda.getEnd2());
                stmtTurnosAgenda.setInt(8, nagenda.getStart3());
                stmtTurnosAgenda.setInt(9, nagenda.getEnd3());
                stmtTurnosAgenda.setInt(10, nagenda.getStart4());
                stmtTurnosAgenda.setInt(11, nagenda.getEnd4());
                stmtTurnosAgenda.execute();
                stmtTurnosAgenda.close();
            }
            //commitando a transação
            con.commit();
            Conexao.fechaConexao(con);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "Erro ao cadastrar Agenda. Procure o Administrador.");
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
    
    public static boolean setAtualizar(Nagendasdesc agenda, ArrayList<Nagenda> turnosAgenda){
        
        try {
            con = Conexao.fazConexao();
            con.setAutoCommit(false);
            
            //apaga turnos
            String sqlDeletaTurnos = "delete from nagenda where nagdid = ?";
            PreparedStatement stmtDeletaTurnos = con.prepareStatement(sqlDeletaTurnos);
            stmtDeletaTurnos.setInt(1, agenda.getNagdid());
            stmtDeletaTurnos.execute();
            stmtDeletaTurnos.close();
            
            //atualiza agenda
            String sqlAgenda = "update nagendasdesc set name = ?, descricao = ?, ativa = ?, id_areas_atendimento = ? where nagdid = ?";
            PreparedStatement stmtAgenda = con.prepareStatement(sqlAgenda);
            stmtAgenda.setString(1, agenda.getName());
            stmtAgenda.setString(2, agenda.getDescricao());
            stmtAgenda.setInt(3, agenda.getAtiva());
            stmtAgenda.setInt(4, agenda.getId_areas_atendimento());
            stmtAgenda.setInt(5, agenda.getNagdid());
            stmtAgenda.execute();
            stmtAgenda.close();

            //cadastrando os turnos
            for (Nagenda nagenda : turnosAgenda) {
                String sqlTurno = "insert into nagenda (nagdid, weekday, duracao, start1, end1, start2, end2, start3, end3, start4, end4) values(?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement stmtTurnosAgenda = con.prepareStatement(sqlTurno);
                stmtTurnosAgenda.setInt(1, agenda.getNagdid());
                stmtTurnosAgenda.setInt(2, nagenda.getWeekday());
                stmtTurnosAgenda.setInt(3, nagenda.getDuracao());
                stmtTurnosAgenda.setInt(4, nagenda.getStart1());
                stmtTurnosAgenda.setInt(5, nagenda.getEnd1());
                stmtTurnosAgenda.setInt(6, nagenda.getStart2());
                stmtTurnosAgenda.setInt(7, nagenda.getEnd2());
                stmtTurnosAgenda.setInt(8, nagenda.getStart3());
                stmtTurnosAgenda.setInt(9, nagenda.getEnd3());
                stmtTurnosAgenda.setInt(10, nagenda.getStart4());
                stmtTurnosAgenda.setInt(11, nagenda.getEnd4());
                stmtTurnosAgenda.execute();
                stmtTurnosAgenda.close();
            }
            //commitando a transação
            con.commit();
            Conexao.fechaConexao(con);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "Erro ao Atualizar Agenda. Procure o Administrador.");
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
    
public static boolean setDeletar(Nagendasdesc agenda){
        
        try {
            con = Conexao.fazConexao();
            con.setAutoCommit(false);
            
            //apaga turnos
            String sqlDeletaTurnos = "delete from nagenda where nagdid = ?";
            PreparedStatement stmtDeletaTurnos = con.prepareStatement(sqlDeletaTurnos);
            stmtDeletaTurnos.setInt(1, agenda.getNagdid());
            stmtDeletaTurnos.execute();
            stmtDeletaTurnos.close();
            
            //atualiza agenda
            String sqlAgenda = "delete from nagendasdesc where nagdid = ?";
            PreparedStatement stmtAgenda = con.prepareStatement(sqlAgenda);
            stmtAgenda.setInt(1, agenda.getNagdid());
            stmtAgenda.execute();
            stmtAgenda.close();
            //commitando a transação
            con.commit();
            Conexao.fechaConexao(con);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "Erro ao Deletar Agenda. Procure o Administrador.");
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
