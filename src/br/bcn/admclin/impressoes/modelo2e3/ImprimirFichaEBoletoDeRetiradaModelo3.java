/*
 * quando trocar o formulario deve se dar 8 linhas para fica na posição correta
 * o nome do compartilhamento é definido na janela de usuarios - impressora para Ficha de Sala
 */
package br.bcn.admclin.impressoes.modelo2e3;

import java.io.IOException;
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
import br.bcn.admclin.ClasseAuxiliares.OSvalidator;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

/**
 * @author Cesar Schutz
 */
public class ImprimirFichaEBoletoDeRetiradaModelo3 {
    private Connection con = null;
    private ESCPrinter imprimir;
    private int handle_at;
    private List<ImprimirFichaEBoletoDeRetiradaModelo2MODEL> listaDeExames =
        new ArrayList<ImprimirFichaEBoletoDeRetiradaModelo2MODEL>();
    private String nomeDoArquivo = janelaPrincipal.internalFrameJanelaPrincipal.codigoParaImpressoesLinux + "FICHA";

    public ImprimirFichaEBoletoDeRetiradaModelo3(int handle_at) {
        this.handle_at = handle_at;
    }
    
    private void instanciarImpressora(){
        if(!OSvalidator.isWindows() && !OSvalidator.isMac()){
            imprimir = new ESCPrinter(nomeDoArquivo, true);
        }else{
            imprimir = new ESCPrinter(USUARIOS.impressora_ficha, true);
        }
    }
    
    private void imprimirNotaCasoSejaLinux() throws IOException{
        if(!OSvalidator.isWindows() && !OSvalidator.isMac()){
            Runtime.getRuntime().exec("lpr -P " + USUARIOS.impressora_ficha + " " + nomeDoArquivo);  
        }
    }

    public boolean imprimir() {
        boolean imprimiu = false;
        con = br.bcn.admclin.dao.dbris.Conexao.fazConexao();
        try {
            instanciarImpressora();
            getDadosParaAFicha();
            // inicializando a impressora
            imprimir.initialize();
            // imprimindo o boleto de retirada (parte superior da ficha)
            imprimirCanhotoDeRetirada();
            // imprimindo a ficha em si
            imprimirFicha();
            imprimirNotaCasoSejaLinux();
            imprimiu = true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao imprimir Ficha. Procure o Administrador.", "Erro",
                JOptionPane.ERROR_MESSAGE);
        }

        return imprimiu;

    }

    private void imprimirCanhotoDeRetirada() {
    	
        imprimir.lineFeed(); // linha 1
        imprimir.lineFeed(); // linha 2
        imprimir.lineFeed(); // linha 3
        imprimir.lineFeed(); // linha 4
        imprimir.lineFeed(); // linha 5
        imprimir.lineFeed(); // linha 6
        imprimir.lineFeed(); // linha 7
        // imprimindo o nome do paciente
        imprimir.print("    " + arrumarTamanhoDaString(listaDeExames.get(0).getNomePaciente(), 67));

        imprimir.lineFeed(); // linha 2
        imprimir.lineFeed(); // linha 3

        // imprimindo exame data e nº
        imprimir.print("    " + arrumarTamanhoDaString(listaDeExames.get(0).getNome_area_atendimento(), 27)
            + "     " + arrumarTamanhoDaString(listaDeExames.get(0).getDataEntregaExame(), 20) + "   "
            + arrumarTamanhoDaString(String.valueOf(handle_at), 13));

        imprimir.lineFeed(); // linha 4
        imprimir.lineFeed(); // linha 5
        imprimir.lineFeed(); // linha 6
        imprimir.lineFeed(); // linha 7
        imprimir.lineFeed(); // linha 8
        imprimir.lineFeed(); // linha 9
        imprimir.lineFeed(); // linha 10
        imprimir.lineFeed(); // linha 11

    }

    private void imprimirFicha() {

        // imprimir a hora do atendimento linha 10 (campo: hora de chegada)
        // 69 espaços em branco
        imprimir.print("                                                                     "
            + arrumarTamanhoDaString(listaDeExames.get(0).getHoraAtendimento(), 10));

        imprimir.lineFeed(); // linha 11
        
        // imprimi na linha 12 a data do atendimento (campo: data)
        // 62 espaços
        imprimir.print("                                                              "
            + arrumarTamanhoDaString(listaDeExames.get(0).getDataAtendimento(), 12));

        imprimir.lineFeed(); // linha 13
        // imprimir o handle at na linha 13 (campo: nº)
        // 37 espaços em branco
        imprimir.print("                                     " + arrumarTamanhoDaString(String.valueOf(handle_at), 19));

        imprimir.lineFeed();

        // imprimi a hora marca
        imprimir.print("    " + arrumarTamanhoDaString(listaDeExames.get(0).getNomePaciente(), 54) 
        		+ "           " + arrumarTamanhoDaString(listaDeExames.get(0).getHoraAtendimento(), 6));
        
        imprimir.lineFeed();
        
        imprimir.print("                                          " + arrumarTamanhoDaString(String.valueOf(listaDeExames.get(0).getNomeConvenio()), 16));

        imprimir.lineFeed();
        imprimir.lineFeed();
        
        // imprimi hora do atendimento na linha 16
        Calendar hoje = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        String horaAtual = format.format(hoje.getTime());
        // 70 espaços em branco
        imprimir.print("                                                                      "
            + arrumarTamanhoDaString(String.valueOf(horaAtual), 6));

        imprimir.lineFeed(); 
        
        // imprimi endereço do paciente, telefone do paciente na linha 17
        imprimir.print("      " + arrumarTamanhoDaString(listaDeExames.get(0).getEnderecoPaciente(), 28) + "    "
            + arrumarTamanhoDaString(listaDeExames.get(0).getTelefonePaciente(), 19));
        
        imprimir.lineFeed(); 
        
        //colocanndo o celular 
        imprimir.print("      " + arrumarTamanhoDaString("", 28) + "    "
                + arrumarTamanhoDaString(listaDeExames.get(0).getCelularPaciente(), 19));

        
        // aqui imprimimos o exame cidade uf, peso altura e idade
        for (int i = 0; i <= 4; i++) {
            String nomeDoExame = " ";
            try {
                nomeDoExame = listaDeExames.get(i).getNomeExame();
            } catch (Exception e) {
                nomeDoExame = " ";
            }

            if (i == 2) {
                imprimir.print("    " + arrumarTamanhoDaString(listaDeExames.get(0).getCidadePaciente(), 26) + "   "
                    + arrumarTamanhoDaString(listaDeExames.get(0).getUfPaciente(), 24) + "  "
                    + arrumarTamanhoDaString(nomeDoExame, 20));
                imprimir.lineFeed();
            } else if (i == 3) {
                imprimir.print("    " + arrumarTamanhoDaString(listaDeExames.get(0).getAlturaPaciente(), 8) + "    "
                    + arrumarTamanhoDaString(String.valueOf(listaDeExames.get(0).getPesoPaciente()), 10) + "    "
                    + arrumarTamanhoDaString(String.valueOf(listaDeExames.get(0).getIdadePaciente()), 27) + "  "
                    + arrumarTamanhoDaString(nomeDoExame, 20));
                imprimir.lineFeed();
            } else {
                // 59 espaços
                imprimir.print("                                                           "
                    + arrumarTamanhoDaString(nomeDoExame, 20));
                imprimir.lineFeed();
            }
        }

        //imprimir.print("                                                                          "
            //+ arrumarTamanhoDaString(listaDeExames.get(0).getDuracaoAtendimento(), 5));

        // acabou, agora vamo dar os linefeed para posicionar a proxima impressão
        for (int i = 0; i < 21; i++) {
            imprimir.lineFeed();
        }
        imprimir.close();

    }

    private void getDadosParaAFicha() throws SQLException {
        ResultSet resultSet = ImprimirFichaEBoletoDeRetiradaModelo2DAO.getConsultarDadosDaFicha(con, handle_at);
        listaDeExames.clear();
        while (resultSet.next()) {
            ImprimirFichaEBoletoDeRetiradaModelo2MODEL exame = new ImprimirFichaEBoletoDeRetiradaModelo2MODEL();
            exame.setNome_area_atendimento(resultSet.getString("nomeAreaAtendimento"));
            exame.setNomePaciente(resultSet.getString("nomePaciente"));
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
            exame.setCelularPaciente(resultSet.getString("celular"));
            exame.setEnderecoPaciente(resultSet.getString("endereco"));
            exame.setDuracaoAtendimento(MetodosUteis.transformarMinutosEmHorario(resultSet
                .getInt("duracao_atendimento")));
            exame.setNomeConvenio(resultSet.getString("nomeConvenio"));
            exame.setNomeMedicoSolicitante(resultSet.getString("nomeMedico"));
            exame.setNomeExame(resultSet.getString("nomeExame"));
            exame.setCidadePaciente(resultSet.getString("cidade"));
            exame.setUfPaciente(resultSet.getString("uf"));
            exame.setAlturaPaciente(resultSet.getString("altura"));

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
}
