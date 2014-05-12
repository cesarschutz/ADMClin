package br.bcn.admclin.interfacesGraficas.menu.atendimentos.agendamentos;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

import br.bcn.admclin.dao.dbris.NAGENDAMENTOS;
import br.bcn.admclin.dao.model.Nagendamentos;
import br.bcn.admclin.dao.model.NagendamentosExames;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.agenda.TratamentoParaRegistrarAtendimentoApartirDeAgendamento;


/**
 *
 * @author cesar
 */
public class JIFListaAgendamentos extends javax.swing.JInternalFrame {

    /**
     * Creates new form JIFCadastroAgendaDesc
     */
    public JIFListaAgendamentos() {
        initComponents();
        tirandoBarraDeTitulo();
        iniciarClasse();
    }
    
    private void reescreverMetodoActionPerformanceDoDatePicker() {
        jXDatePicker1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                preencheTabela();
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        
                    }
                });
            }
        });
    }
    
    private Date pegandoDataDoDataPicker() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date data = null;
        java.util.Date dataSelecionada = jXDatePicker1.getDate();
        // criando um formato de data
        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
        // colocando data selecionado no formato criado acima
        String data2 = dataFormatada.format(dataSelecionada);

        try {
            data = new java.sql.Date(format.parse(data2).getTime());
            return data;
        } catch (ParseException ex) {
            return null;
        }
    }
    
    private void ativandoSelecaoDeLinhaComBotaoDireitoDoMouse() {
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (e.getButton() == MouseEvent.BUTTON3) {
                    int col = jTable1.columnAtPoint(e.getPoint());
                    int row = jTable1.rowAtPoint(e.getPoint());
                    if (col != -1 && row != -1) {
                        jTable1.setColumnSelectionInterval(col, col);
                        jTable1.setRowSelectionInterval(row, row);
                    }
                }

                // colocando a seleção na celula clicada
                int linhaSelecionada = jTable1.getSelectedRow();
                int colunaSelecionada = jTable1.getSelectedColumn();

                jTable1.editCellAt(linhaSelecionada, colunaSelecionada);                
            }
        });
    }
    
    private void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }
    
    private void iniciarClasse(){
        ativandoSelecaoDeLinhaComBotaoDireitoDoMouse();
        reescreverMetodoActionPerformanceDoDatePicker();
        jXDatePicker1.setFormats(new String[] { "E dd/MM/yyyy" });
        jXDatePicker1.setLinkDate(System.currentTimeMillis(), "Hoje");
        
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        
        jTable1.setRowHeight(30);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        preencheTabela();
    }   

    private void preencheTabela(){
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        
        ArrayList<Nagendamentos> listaAgendamentos = NAGENDAMENTOS.getConsultar(pegandoDataDoDataPicker());
        for (Nagendamentos agendamento : listaAgendamentos) {
            modelo.addRow(new Object[] { agendamento, agendamento.getPACIENTE(), agendamento.getTELEFONE(), agendamento.getCELULAR(), agendamento.getNOME_CONVENIO() });
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

        jPanel2 = new javax.swing.JPanel();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agendamentos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTable1.setModel(new DefaultTableModel(
            new Object[][] {
            },
            new String[] {
                "nagendamento", "Paciente", "Telefone", "Celular", "Conv\u00EAnio"
            }
        ) {
            boolean[] columnEditables = new boolean[] {
                false, false, false, false, false
            };
            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(15);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(15);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(100);
        jTable1.getColumnModel().getColumn(2).setMinWidth(100);
        jTable1.getColumnModel().getColumn(2).setMaxWidth(100);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(100);
        jTable1.getColumnModel().getColumn(3).setMinWidth(100);
        jTable1.getColumnModel().getColumn(3).setMaxWidth(100);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 963, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable1MouseClicked
        int colunaClicada = jTable1.columnAtPoint(evt.getPoint());
        int linhaClicada = jTable1.rowAtPoint(evt.getPoint());
        int linhaSelecionada = jTable1.getSelectedRow();
        
        if (MouseEvent.BUTTON3 == evt.getButton() && linhaClicada == linhaSelecionada){
            abrirPopUp(evt);
        }
        
    }
    
    private void abrirPopUp(java.awt.event.MouseEvent evt){
        ImageIcon iconeMenuAtendimento = new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/menuAtendimento.png"));
        Nagendamentos agendamentoClicado = (Nagendamentos) jTable1.getValueAt(jTable1.getSelectedRow(), 0);
        JPopupMenu popup = new JPopupMenu();
        
        // menu registrar atendimento
        JMenuItem registrarAtendimento = new JMenuItem("Registrar Atendimento", iconeMenuAtendimento);
        registrarAtendimento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TratamentoParaRegistrarAtendimentoApartirDeAgendamento tratamentoDeAgendamento = new TratamentoParaRegistrarAtendimentoApartirDeAgendamento((Nagendamentos)jTable1.getValueAt(jTable1.getSelectedRow(), 0));
            }
        });
        popup.add(registrarAtendimento);
        
        
        //preenchendo os exames
        ArrayList<NagendamentosExames> listaDeExamesDoAgendamento = agendamentoClicado.getListaExames();
        //precorre os exames do agendamento
        int id_area = 0;
        for (int i = 0; i < listaDeExamesDoAgendamento.size(); i++) {
            if(listaDeExamesDoAgendamento.get(i).getID_AREAS_ATENDIMENTO() == id_area){
                //coloca o nome do exame
                JMenuItem exame = new JMenuItem("    -  " + listaDeExamesDoAgendamento.get(i).getNomeExame());
                exame.setForeground(java.awt.Color.blue);  
                popup.add(exame);
            }else{
                //coloca separador
                popup.addSeparator();
                //coloca o nome da area de atendimento
                JMenuItem areaDeAtendimento = new JMenuItem("ÁREA: " + listaDeExamesDoAgendamento.get(i).getNomeAreaAtendimento());
                areaDeAtendimento.setForeground(java.awt.Color.red); 
                popup.add(areaDeAtendimento);
                //coloca o nome do exame
                JMenuItem exame = new JMenuItem("    -  " + listaDeExamesDoAgendamento.get(i).getNomeExame());
                exame.setForeground(java.awt.Color.blue); 
                popup.add(exame);
                
                id_area = listaDeExamesDoAgendamento.get(i).getID_AREAS_ATENDIMENTO();
            }
        }
        // mostra na tela
        int x = evt.getX();
        int y = evt.getY();
        popup.show(jTable1, x, y);
    }
    
    //Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    // End of variables declaration//GEN-END:variables
}
