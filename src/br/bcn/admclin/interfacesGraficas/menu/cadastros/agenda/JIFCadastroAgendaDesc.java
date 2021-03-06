package br.bcn.admclin.interfacesGraficas.menu.cadastros.agenda;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcento;
import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.dbris.AREAS_ATENDIMENTO;
import br.bcn.admclin.dao.dbris.NAGENDASDESC;
import br.bcn.admclin.dao.model.Areas_atendimento;
import br.bcn.admclin.dao.model.Nagenda;
import br.bcn.admclin.dao.model.Nagendasdesc;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author cesar
 */
public class JIFCadastroAgendaDesc extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    ArrayList<Integer> listaIdAreaDeAtendimento= new ArrayList<Integer>();
    JIFCadastroAgendaTurnos internalFrameAgendaTurnos = new JIFCadastroAgendaTurnos();
    Nagendasdesc agenda;
    
    /**
     * Creates new form JIFCadastroAgendaDesc
     */
    public JIFCadastroAgendaDesc() {
        initComponents();
        iniciarClasse();
        jBAtualizar.setVisible(false);
        jBDeletar.setVisible(false);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastrar Agenda",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
    }
    
    public JIFCadastroAgendaDesc(Nagendasdesc agenda) {
        initComponents();
        iniciarClasse();
        this.agenda = agenda;
        
        jBSalvar.setVisible(false);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Agenda",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        
        //preenche as informações da agenda
        jCBAtiva.setSelectedIndex(agenda.getAtiva());
        jTFNome.setText(agenda.getName());
        jTADescricao.setText(agenda.getDescricao());
        for (int i = 0; i < listaIdAreaDeAtendimento.size(); i++) {
          if(listaIdAreaDeAtendimento.get(i) == agenda.getId_areas_atendimento()){
              jCBAreaDeAtendimento.setSelectedIndex(i);
          }
        } 
        
        //preenchendo os turnos
        ArrayList<Nagenda> listaTurno = NAGENDASDESC.getTurnosDaAgenda(agenda.getNagdid());
        for (Nagenda nagenda : listaTurno) {
            switch (nagenda.getWeekday()) {
                case 1:
                    montaTurnos(internalFrameAgendaTurnos.jCBAtivaDomingo, internalFrameAgendaTurnos.jTableDomingo, internalFrameAgendaTurnos.jTFDuracaoDomingo, nagenda);
                    break;
                case 2:
                    montaTurnos(internalFrameAgendaTurnos.jCBAtivaSegunda, internalFrameAgendaTurnos.jTableSegunda, internalFrameAgendaTurnos.jTFDuracaoSegunda, nagenda);
                    internalFrameAgendaTurnos.jBReplicar.setEnabled(true);
                    break;
                case 3:
                    montaTurnos(internalFrameAgendaTurnos.jCBAtivaTerca, internalFrameAgendaTurnos.jTableTerca, internalFrameAgendaTurnos.jTFDuracaoTerca, nagenda);
                    break;
                case 4:
                    montaTurnos(internalFrameAgendaTurnos.jCBAtivaQuarta, internalFrameAgendaTurnos.jTableQuarta, internalFrameAgendaTurnos.jTFDuracaoQuarta, nagenda);
                    break;
                case 5:
                    montaTurnos(internalFrameAgendaTurnos.jCBAtivaQuinta, internalFrameAgendaTurnos.jTableQuinta, internalFrameAgendaTurnos.jTFDuracaoQuinta, nagenda);
                    break;
                case 6:
                    montaTurnos(internalFrameAgendaTurnos.jCBAtivaSexta, internalFrameAgendaTurnos.jTableSexta, internalFrameAgendaTurnos.jTFDuracaoSexta, nagenda);
                    break;
                case 7:
                    montaTurnos(internalFrameAgendaTurnos.jCBAtivaSabado, internalFrameAgendaTurnos.jTableSabado, internalFrameAgendaTurnos.jTFDuracaoSabado, nagenda);
                    break;
                default:
                    break;
            }
        }
    }
    
    private void montaTurnos(JCheckBox checkBox, JTable tabela, JTextField textFieldDuracao, Nagenda turno){
        checkBox.setSelected(true);
        checkBox.setEnabled(true);
        
        textFieldDuracao.setEnabled(true);
        textFieldDuracao.setText(MetodosUteis.transformarMinutosEmHorario(turno.getDuracao()));
        
        tabela.setVisible(true);
        tabela.setValueAt( MetodosUteis.transformarMinutosEmHorario(turno.getStart1()), 0, 1);
        tabela.setValueAt(MetodosUteis.transformarMinutosEmHorario(turno.getEnd1()), 1, 1);
        
        tabela.setValueAt(MetodosUteis.transformarMinutosEmHorario(turno.getStart2()), 0, 2);
        tabela.setValueAt(MetodosUteis.transformarMinutosEmHorario(turno.getEnd2()), 1, 2);
        
        tabela.setValueAt(MetodosUteis.transformarMinutosEmHorario(turno.getStart3()), 0, 3);
        tabela.setValueAt(MetodosUteis.transformarMinutosEmHorario(turno.getEnd3()), 1, 3);
        
        tabela.setValueAt(MetodosUteis.transformarMinutosEmHorario(turno.getStart4()), 0, 4);
        tabela.setValueAt(MetodosUteis.transformarMinutosEmHorario(turno.getEnd4()), 1, 4);
    }
    
    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }
    
    private void iniciarClasse(){
        tirandoBarraDeTitulo();
        jTFNome.setDocument(new DocumentoSemAspasEPorcento(64));
        jTADescricao.setDocument(new DocumentoSemAspasEPorcento(500));
        jTADescricao.setLineWrap(true);
        preencheAreasDeAtendimento();
        
        janelaPrincipal.jDesktopPane1.add(internalFrameAgendaTurnos);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = internalFrameAgendaTurnos.getWidth();
        int aIFrame = internalFrameAgendaTurnos.getHeight();

        internalFrameAgendaTurnos.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
        
    }
    
    private void preencheAreasDeAtendimento() {
        jCBAreaDeAtendimento.addItem("SEM ÁREA");
        listaIdAreaDeAtendimento.add(0);
        ArrayList<Areas_atendimento> areas = AREAS_ATENDIMENTO.getConsultar();
        for (Areas_atendimento areas_atendimento : areas) {
            jCBAreaDeAtendimento.addItem(areas_atendimento.getNome());
            int id_area_atendimento = areas_atendimento.getId_areas_atendimento();
            listaIdAreaDeAtendimento.add(id_area_atendimento);
        } 
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jCBAtiva = new javax.swing.JComboBox();
        jCBAreaDeAtendimento = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTFNome = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTADescricao = new javax.swing.JTextArea();
        jBTurnos = new javax.swing.JButton();
        jBTurnos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                botaoTurnos();
            }
        });
        jBVoltar = new javax.swing.JButton();
        jBVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                botaoVoltar();
            }
        });
        jBVoltar.setIcon(new ImageIcon(JIFCadastroAgendaDesc.class.getResource("/br/bcn/admclin/imagens/imagemSetaParaEsquerda.png")));
        jBSalvar = new javax.swing.JButton();
        jBSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                botaoSalvar();
            }
        });
        jBSalvar.setIcon(new ImageIcon(JIFCadastroAgendaDesc.class.getResource("/br/bcn/admclin/imagens/salvar.png")));
        jBAtualizar = new javax.swing.JButton();
        jBAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                botaoAtualizar();
            }
        });
        jBAtualizar.setIcon(new ImageIcon(JIFCadastroAgendaDesc.class.getResource("/br/bcn/admclin/imagens/atualizar.png")));
        jBDeletar = new javax.swing.JButton();
        jBDeletar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                botaoDeletar();
            }
        });
        jBDeletar.setIcon(new ImageIcon(JIFCadastroAgendaDesc.class.getResource("/br/bcn/admclin/imagens/deletar.png")));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agenda", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel1.setText("Ativa:");

        jCBAtiva.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Não", "Sim" }));

        jLabel2.setText("Área de Atendimento");

        jLabel3.setText("Nome:");

        jLabel4.setText("Descrição:");

        jTADescricao.setColumns(20);
        jTADescricao.setRows(5);
        jScrollPane1.setViewportView(jTADescricao);

        jBTurnos.setText("Turnos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel1)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(jCBAtiva, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addComponent(jLabel2)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(jCBAreaDeAtendimento, GroupLayout.PREFERRED_SIZE, 219, GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(jLabel4)
                        .addComponent(jLabel3))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(jBTurnos, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING, false)
                                .addComponent(jScrollPane1, Alignment.LEADING)
                                .addComponent(jTFNome, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
                            .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jCBAtiva, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(jCBAreaDeAtendimento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jTFNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(jLabel4)
                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(jBTurnos))
        );
        jPanel1.setLayout(jPanel1Layout);

        jBVoltar.setText("Voltar");

        jBSalvar.setText("Salvar");

        jBAtualizar.setText("Atualizar");

        jBDeletar.setText("Deletar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jBVoltar)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jBSalvar)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jBAtualizar)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jBDeletar)))
                    .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(jBVoltar, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jBSalvar)
                        .addComponent(jBAtualizar)
                        .addComponent(jBDeletar))
                    .addContainerGap(21, Short.MAX_VALUE))
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void botaoTurnos(){
        this.setVisible(false);
        internalFrameAgendaTurnos.setVisible(true);
    }
    
    private void botaoVoltar(){
        this.dispose();
        janelaPrincipal.internalFrameCadastroAgendasDesc = null;
        
        janelaPrincipal.internalFrameCadastroAgendasVisualizar = new JIFCadastroAgendaVisualizar();
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameCadastroAgendasVisualizar);
        janelaPrincipal.internalFrameCadastroAgendasVisualizar.setVisible(true);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameCadastroAgendasVisualizar.getWidth();
        int aIFrame = janelaPrincipal.internalFrameCadastroAgendasVisualizar.getHeight();

        janelaPrincipal.internalFrameCadastroAgendasVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }
    
    private void botaoSalvar(){
        ArrayList<Nagenda> listaTurnos = null;
        
        //verificando o nome
        if(jTFNome.getText().length() < 3){
            JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "O nome da agenda deve ter no mínimo 3 dígitos.");
            return;
        }
        
        //contruindo os turnos
        try {//
            listaTurnos = criaTurnos();
            if(listaTurnos.size() < 1 || listaTurnos == null){
                JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "Selecione ao menos 1 turno.");
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "Erro nos turnos da agenda. Verifique e tente novamente.");
            return;
        }
        
        //montando a agenda
        Nagendasdesc agenda = new Nagendasdesc();
        agenda.setAtiva(jCBAtiva.getSelectedIndex());
        agenda.setId_areas_atendimento(listaIdAreaDeAtendimento.get(jCBAreaDeAtendimento.getSelectedIndex()));
        agenda.setName(jTFNome.getText());
        agenda.setDescricao(jTADescricao.getText());
        
        //cadastra a agenda
        boolean cadastrou = NAGENDASDESC.setCadastrar(agenda, listaTurnos);
        if(cadastrou){
            botaoVoltar();
        }
        
    }
    
    private void botaoAtualizar(){
        ArrayList<Nagenda> listaTurnos = null;
        
        //verificando o nome
        if(jTFNome.getText().length() < 3){
            JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "O nome da agenda deve ter no mínimo 3 dígitos.");
            return;
        }
        
        //contruindo os turnos
        try {//
            listaTurnos = criaTurnos();
            if(listaTurnos.size() < 1 || listaTurnos == null){
                JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "Selecione ao menos 1 turno.");
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "Erro nos turnos da agenda. Verifique e tente novamente.");
            return;
        }
        
        //montando a agenda
        agenda.setAtiva(jCBAtiva.getSelectedIndex());
        agenda.setId_areas_atendimento(listaIdAreaDeAtendimento.get(jCBAreaDeAtendimento.getSelectedIndex()));
        agenda.setName(jTFNome.getText());
        agenda.setDescricao(jTADescricao.getText());
        
        //atualiza a agenda
        boolean atualizou = NAGENDASDESC.setAtualizar(agenda, listaTurnos);
        if(atualizou){
            botaoVoltar();
        }
        
    }
    
    private void botaoDeletar(){
        //cadastra a agenda
        boolean deletou = NAGENDASDESC.setDeletar(agenda);
        if(deletou){
            botaoVoltar();
        }
    }
    //cria turnos
    private ArrayList<Nagenda> criaTurnos(){
        ArrayList<Nagenda> listaTurnosAgenda = new ArrayList<>();
        
        //domingo
        if(internalFrameAgendaTurnos.jCBAtivaDomingo.isSelected()){
            Nagenda turnoDomingo = criaTurnoDeUmDia(internalFrameAgendaTurnos.jTableDomingo);
            turnoDomingo.setWeekday(1);
            turnoDomingo.setDuracao(MetodosUteis.transformarHorarioEmMinutos(internalFrameAgendaTurnos.jTFDuracaoDomingo.getText()));
            listaTurnosAgenda.add(turnoDomingo);
        }
        
        //segunda
        if(internalFrameAgendaTurnos.jCBAtivaSegunda.isSelected()){
            Nagenda turnoSegunda = criaTurnoDeUmDia(internalFrameAgendaTurnos.jTableSegunda);
            turnoSegunda.setWeekday(2);
            turnoSegunda.setDuracao(MetodosUteis.transformarHorarioEmMinutos(internalFrameAgendaTurnos.jTFDuracaoSegunda.getText()));
            listaTurnosAgenda.add(turnoSegunda);
        }
        
        //terça
        if(internalFrameAgendaTurnos.jCBAtivaTerca.isSelected()){
            Nagenda turnoTerca = criaTurnoDeUmDia(internalFrameAgendaTurnos.jTableTerca);
            turnoTerca.setWeekday(3);
            turnoTerca.setDuracao(MetodosUteis.transformarHorarioEmMinutos(internalFrameAgendaTurnos.jTFDuracaoTerca.getText()));
            listaTurnosAgenda.add(turnoTerca);
        }
        
        //quarta
        if(internalFrameAgendaTurnos.jCBAtivaQuarta.isSelected()){
            Nagenda turnoQuarta = criaTurnoDeUmDia(internalFrameAgendaTurnos.jTableQuarta);
            turnoQuarta.setWeekday(4);
            turnoQuarta.setDuracao(MetodosUteis.transformarHorarioEmMinutos(internalFrameAgendaTurnos.jTFDuracaoQuarta.getText()));
            listaTurnosAgenda.add(turnoQuarta);
        }
        
        //quinta
        if(internalFrameAgendaTurnos.jCBAtivaQuinta.isSelected()){
            Nagenda turnoQuinta = criaTurnoDeUmDia(internalFrameAgendaTurnos.jTableQuinta);
            turnoQuinta.setWeekday(5);
            turnoQuinta.setDuracao(MetodosUteis.transformarHorarioEmMinutos(internalFrameAgendaTurnos.jTFDuracaoQuinta.getText()));
            listaTurnosAgenda.add(turnoQuinta);
        }
        
        //sexta
        if(internalFrameAgendaTurnos.jCBAtivaSexta.isSelected()){
            Nagenda turnoSexta = criaTurnoDeUmDia(internalFrameAgendaTurnos.jTableSexta);
            turnoSexta.setWeekday(6);
            turnoSexta.setDuracao(MetodosUteis.transformarHorarioEmMinutos(internalFrameAgendaTurnos.jTFDuracaoSexta.getText()));
            listaTurnosAgenda.add(turnoSexta);
        }
        
        //sabado
        if(internalFrameAgendaTurnos.jCBAtivaSabado.isSelected()){
            Nagenda turnoSabado = criaTurnoDeUmDia(internalFrameAgendaTurnos.jTableSabado);
            turnoSabado.setWeekday(7);
            turnoSabado.setDuracao(MetodosUteis.transformarHorarioEmMinutos(internalFrameAgendaTurnos.jTFDuracaoSabado.getText()));
            listaTurnosAgenda.add(turnoSabado);
        }
        
        return listaTurnosAgenda;
    }
    
    //cria turno de um unico dia
    private Nagenda criaTurnoDeUmDia(JTable tabelaDoDia){
        Nagenda turnoAgenda = new Nagenda();
        
        //criando os turnos do dia
        turnoAgenda.setStart1(MetodosUteis.transformarHorarioEmMinutos(String.valueOf(tabelaDoDia.getValueAt(0, 1))));
        turnoAgenda.setEnd1(MetodosUteis.transformarHorarioEmMinutos(String.valueOf(tabelaDoDia.getValueAt(1, 1))));
        turnoAgenda.setStart2(MetodosUteis.transformarHorarioEmMinutos(String.valueOf(tabelaDoDia.getValueAt(0, 2))));
        turnoAgenda.setEnd2(MetodosUteis.transformarHorarioEmMinutos(String.valueOf(tabelaDoDia.getValueAt(1, 2))));
        turnoAgenda.setStart3(MetodosUteis.transformarHorarioEmMinutos(String.valueOf(tabelaDoDia.getValueAt(0, 3))));
        turnoAgenda.setEnd3(MetodosUteis.transformarHorarioEmMinutos(String.valueOf(tabelaDoDia.getValueAt(1, 3))));
        turnoAgenda.setStart4(MetodosUteis.transformarHorarioEmMinutos(String.valueOf(tabelaDoDia.getValueAt(0, 4))));
        turnoAgenda.setEnd4(MetodosUteis.transformarHorarioEmMinutos(String.valueOf(tabelaDoDia.getValueAt(1, 4))));
        
        return turnoAgenda;
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBAtualizar;
    private javax.swing.JButton jBDeletar;
    private javax.swing.JButton jBSalvar;
    private javax.swing.JButton jBTurnos;
    private javax.swing.JButton jBVoltar;
    private javax.swing.JComboBox jCBAreaDeAtendimento;
    private javax.swing.JComboBox jCBAtiva;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTADescricao;
    private javax.swing.JTextField jTFNome;
    // End of variables declaration//GEN-END:variables
}
