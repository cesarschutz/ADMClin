/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.menu.financeiro.relatorios.faturarConvenio.arquivoTxtDoIpe;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author BCN
 */
public class GerarArquivoTxtDaFaturaDAO {
    
    /**
     * Consulta Todos exames para ser colocado no arquivo TXT do faturamento
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarAtendimentosPorPeriodoEConvenio(Connection con, Date diaInicial, Date diaFinal, int handle_convenio, int handle_at){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select distinct a.handle_at, a.matricula_convenio, a.data_atendimento, c.porcentconvenio, f.valor, e.VALOR_CORRETO_CONVENIO, e.atendimento_exame_id, e.lista_materiais, p.nome, m.crm, t.cod_exame from atendimento_exames e "
                + "inner join atendimentos a on a.handle_at = e.handle_at "
                + "inner join pacientes p on a.handle_paciente = p.handle_paciente "
                + "inner join medicos m on a.handle_medico_sol = m.medicoid "  
                + "inner join tabelas t on e.handle_exame = t.handle_exame and t.handle_convenio = ? "
                + "inner join convenio c on c.handle_convenio = t.handle_convenio "
                + "inner join convenioch f on f.handle_convenio = c.handle_convenio "    
                + "where a.handle_at=? and (data_atendimento > ?  or data_atendimento = ?) and (data_atendimento < ?  or data_atendimento = ?) and a.handle_convenio = ? order by handle_at");
            stmtQuery.setInt(1, handle_convenio);
            stmtQuery.setInt(2, handle_at);
            stmtQuery.setDate(3, diaInicial);
            stmtQuery.setDate(4, diaInicial);
            stmtQuery.setDate(5, diaFinal);
            stmtQuery.setDate(6, diaFinal);
            stmtQuery.setInt(7, handle_convenio);
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar os Atendimentos. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    
    public static ResultSet getConsultarAtendimentosPorPeriodoEGrupo(Connection con, Date diaInicial, Date diaFinal, int grupo_id, int handle_at){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select distinct a.handle_at, a.matricula_convenio, a.data_atendimento, c.porcentconvenio, c.handle_convenio, f.valor, e.VALOR_CORRETO_CONVENIO, e.atendimento_exame_id, e.lista_materiais, p.nome, m.crm, t.cod_exame from atendimento_exames e "+
                "inner join atendimentos a on a.handle_at = e.handle_at "+
                "inner join pacientes p on a.handle_paciente = p.handle_paciente "+
                "inner join medicos m on a.handle_medico_sol = m.medicoid "+
                "inner join tabelas t on e.handle_exame = t.handle_exame "+
                "inner join convenio c on c.handle_convenio = t.handle_convenio "+
                "inner join convenioch f on f.handle_convenio = c.handle_convenio "+
                "where e.handle_at=? and (data_atendimento > ? or data_atendimento = ?) and (data_atendimento < ? or data_atendimento = ?) and c.grupoid = ? and a.handle_convenio = c.handle_convenio order by c.handle_convenio, handle_at");
            stmtQuery.setInt(1, handle_at);
            stmtQuery.setDate(2, diaInicial);
            stmtQuery.setDate(3, diaInicial);
            stmtQuery.setDate(4, diaFinal);
            stmtQuery.setDate(5, diaFinal);
            stmtQuery.setInt(6, grupo_id);
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar os Atendimentos. Procure o Administrador." + e,"ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
}
