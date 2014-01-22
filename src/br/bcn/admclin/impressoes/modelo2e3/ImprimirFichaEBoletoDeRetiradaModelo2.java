/*
 * quando trocar o formulario deve se dar 11 linhas para fica na posição correta
 * o nome do compartilhamento é definido na janela de usuarios - impressora para Ficha de Sala
 */
package br.bcn.admclin.impressoes.modelo2e3;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;

import br.bcn.admclin.ClasseAuxiliares.ESCPrinter;
import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.dbris.USUARIOS;

/**
 * @author Cesar Schutz
 */
public class ImprimirFichaEBoletoDeRetiradaModelo2 {
    private Connection con = null;
    private ESCPrinter imprimir = new ESCPrinter(USUARIOS.impressora_ficha, true);
    private int handle_at;
    private List<ImprimirFichaEBoletoDeRetiradaModelo2MODEL> listaDeExames =
        new ArrayList<ImprimirFichaEBoletoDeRetiradaModelo2MODEL>();

    public ImprimirFichaEBoletoDeRetiradaModelo2(int handle_at) {
        this.handle_at = handle_at;
    }

    public boolean imprimir() {
        boolean imprimiu = false;
        con = br.bcn.admclin.dao.dbris.Conexao.fazConexao();
        try {
            getDadosParaAFicha();
            // inicializando a impressora
            imprimir.initialize();
            // imprimindo o boleto de retirada (parte superior da ficha)
            imprimirCanhotoDeRetirada();
            // imprimindo a ficha em si
            imprimirFicha();
            imprimiu = true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao imprimir Ficha. Procure o Administrador.", "Erro",
                JOptionPane.ERROR_MESSAGE);
        }

        return imprimiu;

    }

    private void imprimirCanhotoDeRetirada() {
        // imprimindo o nome do paciente
        imprimir.print("                   " + arrumarTamanhoDaString(listaDeExames.get(0).getNomePaciente(), 48));

        imprimir.lineFeed(); // vai para a linha 13
        imprimir.lineFeed(); // vai para a linha 14

        // imprimindo exame data e nº
        // 19 espaços //3 espaços //2 espaços
        imprimir.print("                   "
            + arrumarTamanhoDaString(nomeModalidade(listaDeExames.get(0).getModalidade()), 21) + "   "
            + arrumarTamanhoDaString(listaDeExames.get(0).getDataEntregaExame(), 13) + "  "
            + arrumarTamanhoDaString(String.valueOf(handle_at), 9));

        imprimir.lineFeed(); // vai para a linha 15
        imprimir.lineFeed(); // vai para a linha 16
        imprimir.lineFeed(); // vai para a linha 17
        imprimir.lineFeed(); // vai para a linha 18
        imprimir.lineFeed(); // vai para a linha 19

    }

    private void imprimirFicha() {
        // imprimir numero e nome do paciente
        imprimir.print("    " + arrumarTamanhoDaString(String.valueOf(handle_at), 19) + "      "
            + arrumarTamanhoDaString(listaDeExames.get(0).getNomePaciente(), 38));

        imprimir.lineFeed(); // vai para a linha 20
        imprimir.lineFeed(); // vai para a linha 21

        // imprimir hora
        Calendar hoje = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        String horaAtual = format.format(hoje.getTime());
        imprimir.print("    " + arrumarTamanhoDaString(String.valueOf(horaAtual), 19));

        imprimir.lineFeed(); // vai para a linha 22
        imprimir.lineFeed(); // vai para a linha 23

        // imprimir data do atendimento o exame e idade e peso
        imprimir.print("    " + arrumarTamanhoDaString(listaDeExames.get(0).getDataAtendimento(), 19) + "      "
            + arrumarTamanhoDaString(listaDeExames.get(0).getIdadePaciente(), 22) + "   "
            + arrumarTamanhoDaString(listaDeExames.get(0).getPesoPaciente(), 13));

        imprimir.lineFeed(); // vai para a linha 24
        imprimir.lineFeed(); // vai para a linha 25

        // imprimir hora marcada e telefone
        imprimir.print("        " + arrumarTamanhoDaString(listaDeExames.get(0).getHoraAtendimento(), 15) + "      "
            + arrumarTamanhoDaString(listaDeExames.get(0).getTelefonePaciente(), 38));

        imprimir.lineFeed(); // vai para a linha 26
        imprimir.lineFeed(); // vai para a linha 27

        // imprimir exames e endereço e convenio
        for (int i = 0; i < 6; i++) {
            String nomeDoExame = " ";
            try {
                nomeDoExame = listaDeExames.get(i).getNomeExame();
            } catch (Exception e) {
                nomeDoExame = " ";
            }

            if (i == 0) {
                imprimir.print("    " + arrumarTamanhoDaString(nomeDoExame, 19) + "      "
                    + arrumarTamanhoDaString(listaDeExames.get(0).getEnderecoPaciente(), 38));
                imprimir.lineFeed();
            } else if (i == 4) {
                imprimir.print("    " + arrumarTamanhoDaString(nomeDoExame, 19) + "      "
                    + arrumarTamanhoDaString(String.valueOf(listaDeExames.get(0).getNomeConvenio()), 38));
                imprimir.lineFeed();
            } else {
                imprimir.print("    " + arrumarTamanhoDaString(nomeDoExame, 19));
                imprimir.lineFeed();
            }
        }
        // imprimindo duracao e medico solicitante
        imprimir.print("                  " + arrumarTamanhoDaString(listaDeExames.get(0).getDuracaoAtendimento(), 5)
            + "      " + arrumarTamanhoDaString(listaDeExames.get(0).getNomeMedicoSolicitante(), 38));

        // dando espaço para a proxima ficha ficar na lcoalização correta
        for (int i = 0; i < 24; i++) {
            imprimir.lineFeed();
        }
    }

    private void getDadosParaAFicha() throws SQLException {
        ResultSet resultSet = ImprimirFichaEBoletoDeRetiradaModelo2DAO.getConsultarDadosDaFicha(con, handle_at);
        listaDeExames.clear();
        while (resultSet.next()) {
            ImprimirFichaEBoletoDeRetiradaModelo2MODEL exame = new ImprimirFichaEBoletoDeRetiradaModelo2MODEL();
            exame.setNomePaciente(resultSet.getString("nomePaciente"));
            exame.setModalidade(resultSet.getString("modalidade"));
            exame.setDataEntregaExame(MetodosUteis.converterDataParaMostrarAoUsuario(resultSet
                .getString("data_exame_pronto")));
            exame.setDataAtendimento(MetodosUteis.converterDataParaMostrarAoUsuario(resultSet
                .getString("data_atendimento")));
            try {
                exame.setIdadePaciente(MetodosUteis.calculaIdade(resultSet.getString("nascimento"), "dd/MM/yyyy"));
            } catch (Exception e) {
                exame.setIdadePaciente("-");
            }
            exame.setPesoPaciente(resultSet.getString("peso"));
            exame.setHoraAtendimento(MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("hora_atendimento")));
            exame.setTelefonePaciente(resultSet.getString("telefone"));
            exame.setEnderecoPaciente(resultSet.getString("endereco"));
            exame.setDuracaoAtendimento(MetodosUteis.transformarMinutosEmHorario(resultSet
                .getInt("duracao_atendimento")));
            exame.setNomeConvenio(resultSet.getString("nomeConvenio"));
            exame.setNomeMedicoSolicitante(resultSet.getString("nomeMedico"));
            exame.setNomeExame(resultSet.getString("nomeExame"));

            listaDeExames.add(exame);
        }
    }

    private String arrumarTamanhoDaString(String stringASerArrumada, int numeroDeCasasQueDeveTer) {
        if (stringASerArrumada.length() > numeroDeCasasQueDeveTer) { // se string for maior que o numero de casas
            // cortamos ela de acordo com o numero de casas
            stringASerArrumada = stringASerArrumada.substring(0, numeroDeCasasQueDeveTer);

        } else if (stringASerArrumada.length() < numeroDeCasasQueDeveTer) { // se string for menor que o numero de casas

            // colocamos as casas que faltam
            int numeroDeCasasQueFaltam = numeroDeCasasQueDeveTer - stringASerArrumada.length();
            for (int i = 0; i < numeroDeCasasQueFaltam; i++) {
                stringASerArrumada = stringASerArrumada + " ";
            }

        }

        return stringASerArrumada;
    }

    // metodo que retorna a nomenclatura correta de acordo com a modalidade
    private String nomeModalidade(String modalidade) {
        if ("CR".equals(modalidade)) {
            return "RX";

        } else if ("CT".equals(modalidade)) {
            return "TC";

        } else if ("MG".equals(modalidade)) {
            return "MG";

        } else if ("DX".equals(modalidade)) {
            return "RX";

        } else if ("RF".equals(modalidade)) {
            return "FL";

        } else if ("NM".equals(modalidade)) {
            return "MN";

        } else if ("US".equals(modalidade)) {
            return "US";

        } else if ("DR".equals(modalidade)) {
            return "RX";

        } else if ("MR".equals(modalidade)) {
            return "RM";

        } else if ("OT".equals(modalidade)) {
            return "OT";

        } else if ("DO".equals(modalidade)) {
            return "DE";

        } else if ("OD".equals(modalidade)) {
            return "OD";

        } else if ("TR".equals(modalidade)) {
            return "TR";
        } else {
            JOptionPane.showMessageDialog(br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameJanelaPrincipal,
                "Erro nas modalidades. Procure o Administrador.");
            return "OT";
        }
    }
}
