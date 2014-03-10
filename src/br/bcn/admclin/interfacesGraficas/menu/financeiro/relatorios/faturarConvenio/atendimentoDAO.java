/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.faturarConvenio;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.model.Atendimento_Exames;

/**
 * 
 * @author Cesar Schutz
 */
public class atendimentoDAO {
    /**
     * Consulta Todos os atendimentos existentes no Banco de Dados
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarAtendimentosPorPeriodoEConvenio(Connection con, Date diaInicial, Date diaFinal,
        int handle_convenio) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select distinct m.crm as crmMedico, m.nome as nomeMedico, p.nome as nomePaciente, p.nascimento as nascimentoPaciente, a.handle_at, a.data_atendimento, a.modalidade, a.matricula_convenio, a.hora_atendimento, a.flag_laudo, a.flag_faturado "
                    + "from atendimentos A inner join pacientes p on a.handle_paciente = p.handle_paciente "
                    + "inner join medicos m on a.handle_medico_sol = m.medicoid "
                    + "inner join atendimento_exames e on a.handle_at = e.handle_at "
                    + "where (data_atendimento > ?  or data_atendimento = ?) and (data_atendimento < ?  or data_atendimento = ?) and (a.flag_conciliado is null or a.flag_conciliado = 0) and handle_convenio = ? order by data_atendimento, a.handle_at");
            stmtQuery.setDate(1, diaInicial);
            stmtQuery.setDate(2, diaInicial);
            stmtQuery.setDate(3, diaFinal);
            stmtQuery.setDate(4, diaFinal);
            stmtQuery.setInt(5, handle_convenio);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar os Atendimentos. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    @SuppressWarnings("finally")
    public static ResultSet getConsultarAtendimentosPorPeriodoEGrupo(Connection con, Date diaInicial, Date diaFinal,
        int grupo_id) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select distinct m.crm as crmMedico, m.nome as nomeMedico, p.nome as nomePaciente, p.nascimento as nascimentoPaciente, a.handle_at, a.data_atendimento, a.modalidade, a.matricula_convenio, a.handle_convenio, a.hora_atendimento, a.flag_laudo, a.flag_faturado "
                    + "from atendimentos A inner join pacientes p on a.handle_paciente = p.handle_paciente "
                    + "inner join medicos m on a.handle_medico_sol = m.medicoid "
                    + "inner join convenio c on a.handle_convenio = c.handle_convenio "
                    + "inner join atendimento_exames e on a.handle_at = e.handle_at "
                    + "where (data_atendimento > ?  or data_atendimento = ?) and (data_atendimento < ?  or data_atendimento = ?) and (a.flag_conciliado is null or a.flag_conciliado = 0) and c.grupoid = ? order by data_atendimento, a.handle_at");
            stmtQuery.setDate(1, diaInicial);
            stmtQuery.setDate(2, diaInicial);
            stmtQuery.setDate(3, diaFinal);
            stmtQuery.setDate(4, diaFinal);
            stmtQuery.setInt(5, grupo_id);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar os Atendimentos. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * Consulta Todos os atendimentos existentes no Banco de Dados
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarExamesDosAtendimento(Connection con, int handle_at, int handle_convenio) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select distinct a.atendimento_exame_id, t.sinonimo, t.cod_exame, e.modalidade, a.lado, a.material, a.valor_correto_convenio, a.valor_correto_exame, a.ch_convenio, a.valor_desconto, a.redutor, "
                    + "a.filme_convenio, a.ch1_exame, a.ch2_exame, a.filme_exame, a.lista_materiais from atendimento_exames A "
                    + "inner join exames e on a.handle_exame = e.handle_exame "
                    + "inner join tabelas t on a.handle_exame = t.handle_exame and t.handle_convenio = ? "
                    + "where handle_at = ? order by t.sinonimo");
            stmtQuery.setInt(1, handle_convenio);
            stmtQuery.setInt(2, handle_at);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar os Exames. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /**
     * Consulta Todos os atendimentos existentes no Banco de Dados
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarValorDeUmAtendimento(Connection con, int handle_at) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select valor_correto_convenio from atendimento_exames where handle_at=?");
            stmtQuery.setInt(1, handle_at);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar valor do Atendimento. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

    /*
     * Altera o flag_faturado para 1 (que foi faturado)
     */
    @SuppressWarnings("finally")
    public static boolean setAtualizarFlagFaturado(Connection con, int handle_at, int flag_faturado) {

        boolean cadastro = false;
        
        // pegando data do sistema
        Calendar hoje = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String dataDeHoje = format.format(hoje.getTime());
        try {
            Date dataAtual = new java.sql.Date(format.parse(dataDeHoje).getTime());
               
            String sql = "update ATENDIMENTOS set flag_faturado=?, data_fatura=? where handle_at=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, flag_faturado);
            stmt.setDate(2, dataAtual);
            stmt.setInt(3, handle_at);
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao definir atendimento como Faturado. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return cadastro;
        }
    }

    public static ArrayList<Atendimento_Exames> buscaExamesParaConciliarPagamentoConvenio(String tipo, java.util.Date data1, java.util.Date data2, int handle_convenio_grupo) throws SQLException{
        String sql;
        if(tipo.equals("convenio")){
           sql = "SELECT\n" +
                           "     ATENDIMENTO_EXAMES.\"FLAG_CONCILIADO\" AS FLAG_CONCILIADO,\n" +
                           "     ATENDIMENTO_EXAMES.\"DATA_RECEBIDO_CONVENIO\" AS DATA_RECEBIDO_CONVENIO,\n" +
                           "     ATENDIMENTO_EXAMES.\"VALOR_RECEBIDO_CONVENIO\" AS VALOR_RECEBIDO_CONVENIO,\n" +
                           
                           "     ATENDIMENTO_EXAMES.\"ATENDIMENTO_EXAME_ID\" AS ATENDIMENTO_EXAME_ID,\n" +
                           "     ATENDIMENTO_EXAMES.\"HANDLE_AT\" AS ATENDIMENTO_EXAMES_HANDLE_AT,\n" +
                           "     ATENDIMENTOS.\"FLAG_FATURADO\" AS ATENDIMENTOS_FLAG_FATURADO,\n" +
                           "     ATENDIMENTOS.\"DATA_ATENDIMENTO\" AS ATENDIMENTOS_DATA_ATENDIMENTO,\n" +
                           "     PACIENTES.\"NOME\" AS PACIENTES_NOME,\n" +
                           "     EXAMES.\"NOME\" AS EXAMES_NOME,\n" +
                           "     ATENDIMENTO_EXAMES.\"VALOR_CORRETO_CONVENIO\" AS ATENDIMENTO_EXAMES_VALOR_CORRET,\n" +
                           "     CONVENIO.\"GRUPOID\" AS CONVENIO_GRUPOID,\n" +
                           "     CONVENIO.\"HANDLE_CONVENIO\" AS CONVENIO_HANDLE_CONVENIO\n" +
                           "FROM\n" +
                           "     \"ATENDIMENTOS\" ATENDIMENTOS INNER JOIN \"ATENDIMENTO_EXAMES\" ATENDIMENTO_EXAMES ON ATENDIMENTOS.\"HANDLE_AT\" = ATENDIMENTO_EXAMES.\"HANDLE_AT\"\n" +
                           "     INNER JOIN \"PACIENTES\" PACIENTES ON ATENDIMENTOS.\"HANDLE_PACIENTE\" = PACIENTES.\"PACIENTEID\"\n" +
                           "     INNER JOIN \"CONVENIO\" CONVENIO ON ATENDIMENTOS.\"HANDLE_CONVENIO\" = CONVENIO.\"CONVENIOID\"\n" +
                           "     INNER JOIN \"EXAMES\" EXAMES ON ATENDIMENTO_EXAMES.\"HANDLE_EXAME\" = EXAMES.\"EXMID\"" +
                           "where (ATENDIMENTOS.\"DATA_ATENDIMENTO\" > ?  or ATENDIMENTOS.\"DATA_ATENDIMENTO\" = ?) and (ATENDIMENTOS.\"DATA_ATENDIMENTO\" < ?  or ATENDIMENTOS.\"DATA_ATENDIMENTO\" = ?) and (ATENDIMENTOS.\"FLAG_FATURADO\" = 1) and CONVENIO.\"HANDLE_CONVENIO\" = ? order by ATENDIMENTO_EXAMES.\"HANDLE_AT\", ATENDIMENTOS_DATA_ATENDIMENTO";
        }else{
            sql = "SELECT\n" +
                            "     ATENDIMENTO_EXAMES.\"FLAG_CONCILIADO\" AS FLAG_CONCILIADO,\n" +
                            "     ATENDIMENTO_EXAMES.\"DATA_RECEBIDO_CONVENIO\" AS DATA_RECEBIDO_CONVENIO,\n" +
                            "     ATENDIMENTO_EXAMES.\"VALOR_RECEBIDO_CONVENIO\" AS VALOR_RECEBIDO_CONVENIO,\n" +
                            
                            "     ATENDIMENTO_EXAMES.\"ATENDIMENTO_EXAME_ID\" AS ATENDIMENTO_EXAME_ID,\n" +
                            "     ATENDIMENTO_EXAMES.\"HANDLE_AT\" AS ATENDIMENTO_EXAMES_HANDLE_AT,\n" +
                            "     ATENDIMENTOS.\"FLAG_FATURADO\" AS ATENDIMENTOS_FLAG_FATURADO,\n" +
                            "     ATENDIMENTOS.\"DATA_ATENDIMENTO\" AS ATENDIMENTOS_DATA_ATENDIMENTO,\n" +
                            "     PACIENTES.\"NOME\" AS PACIENTES_NOME,\n" +
                            "     EXAMES.\"NOME\" AS EXAMES_NOME,\n" +
                            "     ATENDIMENTO_EXAMES.\"VALOR_CORRETO_CONVENIO\" AS ATENDIMENTO_EXAMES_VALOR_CORRET,\n" +
                            "     CONVENIO.\"GRUPOID\" AS CONVENIO_GRUPOID,\n" +
                            "     CONVENIO.\"HANDLE_CONVENIO\" AS CONVENIO_HANDLE_CONVENIO\n" +
                            "FROM\n" +
                            "     \"ATENDIMENTOS\" ATENDIMENTOS INNER JOIN \"ATENDIMENTO_EXAMES\" ATENDIMENTO_EXAMES ON ATENDIMENTOS.\"HANDLE_AT\" = ATENDIMENTO_EXAMES.\"HANDLE_AT\"\n" +
                            "     INNER JOIN \"PACIENTES\" PACIENTES ON ATENDIMENTOS.\"HANDLE_PACIENTE\" = PACIENTES.\"PACIENTEID\"\n" +
                            "     INNER JOIN \"CONVENIO\" CONVENIO ON ATENDIMENTOS.\"HANDLE_CONVENIO\" = CONVENIO.\"CONVENIOID\"\n" +
                            "     INNER JOIN \"EXAMES\" EXAMES ON ATENDIMENTO_EXAMES.\"HANDLE_EXAME\" = EXAMES.\"EXMID\"" +
                            "where (ATENDIMENTOS.\"DATA_ATENDIMENTO\" > ?  or ATENDIMENTOS.\"DATA_ATENDIMENTO\" = ?) and (ATENDIMENTOS.\"DATA_ATENDIMENTO\" < ?  or ATENDIMENTOS.\"DATA_ATENDIMENTO\" = ?) and (ATENDIMENTOS.\"FLAG_FATURADO\" = 1) and CONVENIO.\"GRUPOID\" = ? order by ATENDIMENTO_EXAMES.\"HANDLE_AT\", ATENDIMENTOS_DATA_ATENDIMENTO";
        }
        
        Connection con = Conexao.fazConexao();
        PreparedStatement stmtQuery = con.prepareStatement(sql);
        stmtQuery.setDate(1, new java.sql.Date(data1.getTime()));
        stmtQuery.setDate(2, new java.sql.Date(data1.getTime()));
        stmtQuery.setDate(3, new java.sql.Date(data2.getTime()));
        stmtQuery.setDate(4, new java.sql.Date(data2.getTime()));
        stmtQuery.setInt(5, handle_convenio_grupo);
        ResultSet rs = stmtQuery.executeQuery();
        ArrayList<Atendimento_Exames> listaExames = new ArrayList<Atendimento_Exames>();
        while(rs.next()){
            Atendimento_Exames exame = new Atendimento_Exames();
            exame.setHANDLE_AT(rs.getInt("ATENDIMENTO_EXAMES_HANDLE_AT"));
            exame.setData(rs.getDate("ATENDIMENTOS_DATA_ATENDIMENTO"));
            exame.setPaciente(rs.getString("PACIENTES_NOME"));
            exame.setNomeExame(rs.getString("EXAMES_NOME"));
            exame.setVALOR_CORRETO_CONVENIO(rs.getDouble("ATENDIMENTO_EXAMES_VALOR_CORRET"));
            exame.setATENDIMENTO_EXAME_ID(rs.getInt("ATENDIMENTO_EXAME_ID"));
            exame.setFLAG_CONCILIADO(rs.getInt("FLAG_CONCILIADO"));
            exame.setDATA_RECEBIDO_CONVENIO(rs.getDate("DATA_RECEBIDO_CONVENIO"));
            exame.setFLAG_CONCILIADO(rs.getInt("FLAG_CONCILIADO"));
            exame.setVALOR_RECEBIDO_CONVENIO(rs.getDouble("VALOR_RECEBIDO_CONVENIO"));
            listaExames.add(exame);
        }
        Conexao.fechaConexao(con);
        return listaExames;
    }
}
