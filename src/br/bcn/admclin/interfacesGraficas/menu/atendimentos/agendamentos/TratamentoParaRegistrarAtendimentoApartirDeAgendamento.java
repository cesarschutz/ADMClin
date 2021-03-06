package br.bcn.admclin.interfacesGraficas.menu.atendimentos.agendamentos;

import java.util.ArrayList;

import br.bcn.admclin.dao.model.Nagendamentos;
import br.bcn.admclin.dao.model.NagendamentosExames;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;
import br.bcn.admclin.interfacesGraficas.menu.atendimentos.registrarAtendimento.JIFCadastroDeAtendimento;

public class TratamentoParaRegistrarAtendimentoApartirDeAgendamento {
    public static Nagendamentos agendamento;
    public static ArrayList<String> listaIdAreasDeAtendimento = new ArrayList<String>();

    public TratamentoParaRegistrarAtendimentoApartirDeAgendamento(Nagendamentos agendamento) {
        this.agendamento = agendamento;
        criandoListaDeIdAreaDeAtendimento();
        abrirJanelaDeRegistro();
    }
    
    /*
     * metodo que imprime o agendamento da classe
     */
    private static void imprimirAgendamento(){
        System.out.println("---------------------------------------------------------------------");
        System.out.println("paciente: " + agendamento.getPACIENTE());
        System.out.println("telefone: " + agendamento.getTELEFONE());
        System.out.println("celular: " + agendamento.getCELULAR());
        System.out.println("dia: " + agendamento.getDIA());
        System.out.println("handle_convenio: " + agendamento.getHANDLE_CONVENIO());
        System.out.println("nomeConvenio: " + agendamento.getNOME_CONVENIO());
        for (int i = 0; i < agendamento.getListaExames().size() ; i++) {
            System.out.println("exame: " + agendamento.getListaExames().get(i).getNomeExame() + "  hora exame: " + agendamento.getListaExames().get(i).getHORA() + "  - id area de atendimento: " + agendamento.getListaExames().get(i).getID_AREAS_ATENDIMENTO());
        }
        System.out.println("---------------------------------------------------------------------");
    }
    
    /*
     * Metodo que monta uma lista com os ID das areas de atendimento que o agendamento possue
     */
    private void criandoListaDeIdAreaDeAtendimento(){
        listaIdAreasDeAtendimento.clear();
        listaIdAreasDeAtendimento.add("");
        int id = 0;
        for (int i = 0; i < agendamento.getListaExames().size() ; i++) {
            if(id != agendamento.getListaExames().get(i).getID_AREAS_ATENDIMENTO()){
                id = agendamento.getListaExames().get(i).getID_AREAS_ATENDIMENTO();
                listaIdAreasDeAtendimento.add(agendamento.getListaExames().get(i).getNomeAreaAtendimento());
            }
        }
    }
    
    /*
     * metodo que monte uma lista de exames para enviar e registrar um atendimento e apaga o id_area_atendimento da lista
     */
    public static ArrayList<NagendamentosExames> montaListaDeExamesDeUmaArea(int idAreaSelecionada){
        ArrayList<NagendamentosExames> listaDeExamesAEnviar = new ArrayList<NagendamentosExames>();
        //se nao tiver mais retorna null
        if(listaIdAreasDeAtendimento.size() == 0){
            return null;
        }else{
            //varre em busca de exames com a mesma id_area_de_atendimento
            for (int i = 0; i < agendamento.getListaExames().size() ; i++) {
                if(agendamento.getListaExames().get(i).getID_AREAS_ATENDIMENTO() == idAreaSelecionada){
                    listaDeExamesAEnviar.add(agendamento.getListaExames().get(i));
                }
            }
            return listaDeExamesAEnviar;
        }
    }
    
    private void abrirJanelaDeRegistro(){
        janelaPrincipal.internalFrameAtendimento = new JIFCadastroDeAtendimento("livre", 0, true);
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameAtendimento);
        janelaPrincipal.internalFrameAtendimento.setVisible(true);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameAtendimento.getWidth();
        int aIFrame = janelaPrincipal.internalFrameAtendimento.getHeight();

        janelaPrincipal.internalFrameAtendimento.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }
}
