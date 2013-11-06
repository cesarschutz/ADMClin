/*
 * quando trocar o formulario deve se dar 8 linhas para fica na posição correta
 * o nome do compartilhamento é definido na janela de usuarios - impressora para Ficha de Sala
 */
package impressoes.modelo2e3.fichaEBoletoDeRetirada;

import ClasseAuxiliares.MetodosUteis;
import ClasseAuxiliares.dados_empresa_dao;
import impressoes.modelo2.etiquetaCodigoDeBarras.ImprimirEtiquetaCodigoDeBarrasModelo2;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JOptionPane;
import menu.cadastros.pessoal.dao.usuariosDAO;

/**
 * @author BCN
 */
public class ImprimirFichaEBoletoDeRetiradaModelo3 {
    private Connection con = null;
    private ESCPrinter imprimir =  new ESCPrinter(usuariosDAO.impressora_ficha, true);
    private int handle_at;
    private List<ImprimirFichaEBoletoDeRetiradaModelo2MODEL> listaDeExames = new ArrayList<ImprimirFichaEBoletoDeRetiradaModelo2MODEL>();
    
    
    public ImprimirFichaEBoletoDeRetiradaModelo3(int handle_at) {
        this.handle_at = handle_at;
    }
    
    
    public boolean imprimir(){
        boolean imprimiu = false;
        con = conexao.Conexao.fazConexao();
            try {
                getDadosParaAFicha();
                //inicializando a impressora
                imprimir.initialize();
                //imprimindo o boleto de retirada (parte superior da ficha)
                imprimirCanhotoDeRetirada();
                //imprimindo a ficha em si
                imprimirFicha();
                imprimiu = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao imprimir Ficha. Procure o Administrador.","Erro", JOptionPane.ERROR_MESSAGE);
            }

        
        return imprimiu;
        
    }
    
    private void imprimirCanhotoDeRetirada(){
        //imprimindo o nome do paciente
        imprimir.print("   " + arrumarTamanhoDaString(listaDeExames.get(0).getNomePaciente(), 67));
        
        imprimir.lineFeed(); //linha 2
        imprimir.lineFeed(); //linha 3
        
        //imprimindo exame data e nº
        imprimir.print("   " + arrumarTamanhoDaString(nomeModalidade(listaDeExames.get(0).getModalidade()), 27) + "     " + arrumarTamanhoDaString(listaDeExames.get(0).getDataEntregaExame(), 20) + "   " + arrumarTamanhoDaString(String.valueOf(handle_at), 13));
        
        
        imprimir.lineFeed(); //linha 4
        imprimir.lineFeed(); //linha 5
        imprimir.lineFeed(); //linha 6
        imprimir.lineFeed(); //linha 7
        imprimir.lineFeed(); //linha 8
        imprimir.lineFeed(); //linha 9
        imprimir.lineFeed();  //linha 10

    }
    
    private void imprimirFicha(){
        
        //imprimir a hora do atendimento linha 10 (campo: hora de chegada)
                             //69 espaços em branco
        imprimir.print("                                                                     " + arrumarTamanhoDaString(listaDeExames.get(0).getHoraAtendimento(), 11));
        
        
        
        imprimir.lineFeed();  //linha 11
        imprimir.lineFeed();  //linha 12
        //imprimi na linha 12 a data do atendimento (campo: data)
                           //62 espaços
        imprimir.print("                                                              " + arrumarTamanhoDaString(listaDeExames.get(0).getDataAtendimento(), 19));
        
        
                
        imprimir.lineFeed();  //linha 13
        //imprimir o handle at na linha 13 (campo: nº)
                        //37 espaços em branco
        imprimir.print("                                     " + arrumarTamanhoDaString(String.valueOf(handle_at), 19));        
                
        
        
        imprimir.lineFeed();  //linha 14        
        imprimir.lineFeed();  //linha 15
        //imprimi nome paciente, nome convenio e hora marcada linha 15
        imprimir.print("   " + arrumarTamanhoDaString(listaDeExames.get(0).getNomePaciente(), 31) + "       " + arrumarTamanhoDaString(String.valueOf(listaDeExames.get(0).getNomeConvenio()), 16) + "           " + arrumarTamanhoDaString(listaDeExames.get(0).getHoraAtendimento(), 6));
                
                
                
        imprimir.lineFeed();  //linha 16        
        //imprimi hora do atendimento na linha 16
        Calendar hoje = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        String horaAtual = format.format( hoje.getTime() );
                            //70 espaços em branco
        imprimir.print("                                                                      " + arrumarTamanhoDaString(String.valueOf(horaAtual), 6));
                
                
        
        imprimir.lineFeed();  //linha 17
        //imprimi endereço do paciente, telefone do paciente na linha 17
        imprimir.print("      " + arrumarTamanhoDaString(listaDeExames.get(0).getEnderecoPaciente(), 28) + "    " + arrumarTamanhoDaString(listaDeExames.get(0).getTelefonePaciente(), 19));
        
        
                
        imprimir.lineFeed(); //linha 18
        //aqui imprimimos o exame cidade uf, peso altura e idade
        for (int i = 0; i <= 4; i++) {
            String nomeDoExame = " "; 
            try {
                nomeDoExame = listaDeExames.get(i).getNomeExame();
            } catch (Exception e) {
                nomeDoExame = " ";
            }
            
            if (i==1) {
                imprimir.print("    " + arrumarTamanhoDaString(listaDeExames.get(0).getCidadePaciente(), 26) + 
                        "   " + arrumarTamanhoDaString(listaDeExames.get(0).getUfPaciente(), 24) +
                        " " + arrumarTamanhoDaString(nomeDoExame, 24));
                imprimir.lineFeed();
            } else if(i==3){
                imprimir.print("    " + arrumarTamanhoDaString(listaDeExames.get(0).getAlturaPaciente(), 8) + 
                        "    " + arrumarTamanhoDaString(String.valueOf(listaDeExames.get(0).getPesoPaciente()), 10) +
                        "    " + arrumarTamanhoDaString(String.valueOf(listaDeExames.get(0).getPesoPaciente()), 27) +
                        " " + arrumarTamanhoDaString(nomeDoExame, 24));
                imprimir.lineFeed();
            } else{
                                   //58 espaços
                imprimir.print("                                                          " + arrumarTamanhoDaString(nomeDoExame, 24));
                imprimir.lineFeed();
            }    
        } 
        
        imprimir.print("                                                                          " + arrumarTamanhoDaString(listaDeExames.get(0).getDuracaoAtendimento(),5));
        
        
        //acabou, agora vamo dar os linefeed para posicionar a proxima impressão
        for (int i = 0; i < 27; i++) {
            imprimir.lineFeed();
        }
        
    }
    
    private void getDadosParaAFicha() throws SQLException{
        ResultSet resultSet = ImprimirFichaEBoletoDeRetiradaModelo2DAO.getConsultarDadosDaFicha(con, handle_at);
        listaDeExames.clear();
        while(resultSet.next()){
            ImprimirFichaEBoletoDeRetiradaModelo2MODEL exame = new ImprimirFichaEBoletoDeRetiradaModelo2MODEL();
            exame.setNomePaciente(resultSet.getString("nomePaciente"));
            exame.setModalidade(resultSet.getString("modalidade"));
            exame.setDataEntregaExame(MetodosUteis.converterDataParaMostrarAoUsuario(resultSet.getString("data_exame_pronto")));
            exame.setDataAtendimento(MetodosUteis.converterDataParaMostrarAoUsuario(resultSet.getString("data_atendimento")));
            try {
                exame.setIdadePaciente(MetodosUteis.calculaIdade(resultSet.getString("nascimento"), "dd/MM/yyyy"));
            } catch (Exception e) {
                exame.setIdadePaciente("-");
            }
            exame.setPesoPaciente(resultSet.getString("peso"));
            exame.setHoraAtendimento(MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("hora_atendimento")));
            exame.setTelefonePaciente(resultSet.getString("telefone"));
            exame.setEnderecoPaciente(resultSet.getString("endereco"));
            exame.setDuracaoAtendimento(MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("duracao_atendimento")));
            exame.setNomeConvenio(resultSet.getString("nomeConvenio"));
            exame.setNomeMedicoSolicitante(resultSet.getString("nomeMedico"));
            exame.setNomeExame(resultSet.getString("nomeExame"));
            
            exame.setCidadePaciente(resultSet.getString("cidade"));
            exame.setUfPaciente(resultSet.getString("uf"));
            exame.setAlturaPaciente(resultSet.getString("altura"));
            
            listaDeExames.add(exame);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    private String arrumarTamanhoDaString(String stringASerArrumada, int numeroDeCasasQueDeveTer){
        if (stringASerArrumada.length() > numeroDeCasasQueDeveTer) { //se string for maior que o numero de casas
            //cortamos ela de acordo com o numero de casas
            stringASerArrumada = stringASerArrumada.substring(0,numeroDeCasasQueDeveTer);
            
        }else if(stringASerArrumada.length() < numeroDeCasasQueDeveTer){ // se string for menor que o numero de casas
            
            //colocamos as casas que faltam
            int numeroDeCasasQueFaltam = numeroDeCasasQueDeveTer - stringASerArrumada.length();
            for (int i = 0; i < numeroDeCasasQueFaltam; i++) {
                stringASerArrumada = stringASerArrumada + " ";
            }
            
        }
        
        return stringASerArrumada;
    }
    
    //metodo que retorna a nomenclatura correta de acordo com a modalidade
     private String nomeModalidade(String modalidade){
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
             JOptionPane.showMessageDialog(janelaPrincipal.janelaPrincipal.internalFrameJanelaPrincipal, "Erro nas modalidades. Procure o Administrador.");
             return "OT";
         }
     }
}
