/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.impressoes.modelo2e3.fichaEBoletoDeRetirada;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

/**
 * 
 * @author Cesar Schutz
 */
public class ImprimirFichaEBoletoDeRetiradaModelo2DAO {

    /**
     * Consulta os dados para impressao da ficha modelo 02
     * 
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarDadosDaFicha(Connection con, int handle_at) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("select distinct x.atendimento_exame_id, a.modalidade,a.data_exame_pronto,a.data_atendimento,a.hora_atendimento,a.duracao_atendimento,p.nome as nomePaciente,p.nascimento,p.peso,p.telefone,p.cidade,p.uf,p.altura,p.endereco,c.nome as nomeConvenio,m.nome as nomeMedico,e.nome as nomeExame "
                    + "from atendimentos a "
                    + "inner join atendimento_exames x on a.handle_at = x.handle_at "
                    + "inner join exames e on x.handle_exame = e.handle_exame "
                    + "inner join pacientes p on a.handle_paciente = p.handle_paciente "
                    + "inner join medicos m on a.handle_medico_sol = m.medicoid "
                    + "inner join convenio c  on a.handle_convenio = c.handle_convenio " + "where a.handle_at = ?");
            stmtQuery.setInt(1, handle_at);
            resultSet = stmtQuery.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar os Exames do Paciente. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }

}
