/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.dao;

import java.sql.*;

import javax.swing.JOptionPane;

import br.bcn.admclin.model.A_Agendamentos;

/**
 *
 * @author CeSaR
 */
public class A_AGENDAMENTOS {
    
    //variavel utilizada para quando SALVAR veriricar o handle_ap que foi feito naquele agendamento
    public static int handle_apDoAgendamentoCadastrado;
    
    /**
     * Consulta os dados de um agendamento
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarDadosDeUmAgendamento(Connection con, int handle_ap){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select * from a_agendamentos where handle_ap=?");
        stmtQuery.setInt(1, handle_ap);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao verificar dados do Agendamento. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    
    /**
     * Consulta Todos os agendamentos desta agenda existentes no Banco de Dados 
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarAgendamentos(Connection con, int handle_agenda, Date dia){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select * from a_agendamentos where handle_agenda =? and dia=? order by hora");
            stmtQuery.setInt(1, handle_agenda);
            stmtQuery.setDate(2, dia);
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao verificar os Agendamentos. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    
    /**
     * Salva um novo agendamento no Banco De Dados.
     * @param Connection
     * @param PacienteMODEL
     * @return Boolean
     */
    public static boolean setCadastrar(Connection con, A_Agendamentos agendamento){
        boolean cadastro = false;
        String sql = "insert into a_agendamentos (dia,nascimento,dat,"
                + "HANDLE_AGENDA,HORA,HANDLE_PACIENTE,HANDLE_CONVENIO,HANDLE_EXAME,USUARIOID,DURACAODOEXAME,DURACAODOAGENDAMENTO,handle_ap,"
                + "nomePaciente,telefone,celular,nomeConvenio,nomeExame,observacao,cpfPaciente,lado,material,modalidade"
                + ",ch_convenio, filme_convenio, ch1_exame, ch2_exame, filme_exame, lista_materiais) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try{

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, agendamento.getDia());
            stmt.setDate(2, agendamento.getNascimento());
            stmt.setDate(3, agendamento.getDat());

            stmt.setInt(4, agendamento.getHANDLE_AGENDA());
            stmt.setInt(5, agendamento.getHORA());
            stmt.setInt(6, 0);
            stmt.setInt(7, 0);
            stmt.setInt(8, 0);
            stmt.setInt(9, agendamento.getUSUARIOID());
            stmt.setInt(10, agendamento.getDURACAODOEXAME());
            stmt.setInt(11, agendamento.getDuracaoAgendamento());
            stmt.setInt(12, agendamento.getHANDLE_AP());

            stmt.setString(13, agendamento.getNomePaciente());
            stmt.setString(14, agendamento.getTelefone());
            stmt.setString(15, agendamento.getCelular());
            stmt.setString(16, agendamento.getNomeConvenio());
            stmt.setString(17, agendamento.getNomeExame());
            stmt.setString(18, agendamento.getObservacao());
            stmt.setString(19, agendamento.getCpfPaciente());

            stmt.setString(20, agendamento.getLado());
            stmt.setString(21, agendamento.getMaterial());
            
            stmt.setString(22, agendamento.getModalidade());
            
            stmt.setDouble(23, Double.valueOf(agendamento.getCh_convenio()));
            stmt.setDouble(24, Double.valueOf(agendamento.getFilme_convenio()));
            stmt.setDouble(25, Double.valueOf(agendamento.getCh1_exame()));
            stmt.setDouble(26, Double.valueOf(agendamento.getCh2_exame()));
            stmt.setDouble(27, Double.valueOf(agendamento.getFilme_exame()));
            stmt.setString(28, agendamento.getLista_materiais());
            
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao registrar Agendamento. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
    /**
     * Consulta antes de salvar
     * @param Connection
     * @return ResultSet
     */
    public static boolean getConsultarAntesDeSalvar(Connection con, int handle_agenda, Date dia, int horario){
        boolean existe = true;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select * from a_agendamentos where handle_agenda =? and dia=? and hora=? and nomepaciente!=?");
            stmtQuery.setInt(1, handle_agenda);
            stmtQuery.setDate(2, dia);
            stmtQuery.setInt(3, horario);
            stmtQuery.setString(4, "RESERVADO");
            ResultSet resultSet = stmtQuery.executeQuery();
            if(!resultSet.next()){
                existe = false;
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Exame existe. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return existe;
        }
    }
    
    //metodo para somar 1 no handle_ap
    public static void setSomarHandleAP(Connection con){  
        ResultSet resultSet = null;
        String sql = "select gen_id(GEN_A_AGENDAMENTOS_HANDLE_AP,1) from rdb$database";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            resultSet = stmt.executeQuery();
            while(resultSet.next()){

            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao somar handle_ap. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        } 
    }
    
    public static int getHandleAP(Connection con){
        ResultSet resultSet = null;
        
        String sql = "select gen_id(GEN_A_AGENDAMENTOS_HANDLE_AP,0) from rdb$database";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            resultSet = stmt.executeQuery();
            while(resultSet.next()){
                handle_apDoAgendamentoCadastrado = resultSet.getInt("gen_id");
            }
            return handle_apDoAgendamentoCadastrado;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao buscar valor de handle_ap. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
            return 0;
        }
    }
    
    /**
     * Deleta um Agendamento do Banco De Dados
     * @param Connection
     * @param ExameMODEL 
     * @return boolean
     */
    public static boolean setDeletar(Connection con, int handle_ap){
        boolean deleto = false; 
        String sql="delete from a_AGENDAMENTOS where HANDLE_AP=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, handle_ap);
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Agendamento. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
    
}
