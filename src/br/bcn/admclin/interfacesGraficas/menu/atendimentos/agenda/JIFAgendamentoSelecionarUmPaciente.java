/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.atendimentos.agenda;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.documentoSomenteLetras;
import br.bcn.admclin.dao.Conexao;
import br.bcn.admclin.dao.PACIENTES;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

/**
 * 
 * @author Cesar Schutz
 */
public class JIFAgendamentoSelecionarUmPaciente extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    private Connection con = null;
    public String nomePesquisado;

    /**
     * Creates new form JIFAgendamentoSelecionarUmPaciente
     */
    public JIFAgendamentoSelecionarUmPaciente(String nomeParaPesquisar) {
        this.nomePesquisado = nomeParaPesquisar;
        initComponents();
        jTFNomePaciente.setText(nomePesquisado);
        tirandoBarraDeTitulo();
        preenchendoATabela(nomePesquisado);
        iniciarClasse();
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    public void preenchendoATabela(String nomePesquisado) {
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        con = Conexao.fazConexao();
        String[] nomesParaPesquisar = null;
        nomesParaPesquisar = MetodosUteis.formatarParaPesquisarNome(nomePesquisado);

        if (nomesParaPesquisar != null) {
            String sql = null;
            if (nomesParaPesquisar.length == 1)
                sql = nomesParaPesquisar[0] + "%";
            if (nomesParaPesquisar.length == 2)
                sql = nomesParaPesquisar[0] + "%" + nomesParaPesquisar[1] + "%";
            if (nomesParaPesquisar.length == 3)
                sql = nomesParaPesquisar[0] + "%" + nomesParaPesquisar[1] + "%" + nomesParaPesquisar[2] + "%";
            if (nomesParaPesquisar.length == 4)
                sql =
                    nomesParaPesquisar[0] + "%" + nomesParaPesquisar[1] + "%" + nomesParaPesquisar[2] + "%"
                        + nomesParaPesquisar[3] + "%";
            ResultSet resultSet = PACIENTES.getConsultar(con, sql);
            try {
                while (resultSet.next()) {
                    if (resultSet.getInt("handle_paciente") > 0) {
                        modelo.addRow(new String[] { Integer.toString(resultSet.getInt("handle_paciente")),
                            resultSet.getString("nome"), resultSet.getString("cpf"), resultSet.getString("nascimento"),
                            resultSet.getString("telefone"), resultSet.getString("celular") });
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Não foi possivel atualizar a tabela. Procure o administrador.",
                    "ATENÇÃO!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
            Conexao.fechaConexao(con);
        } else {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Nome inválido.");
        }

    }

    public void iniciarClasse() {
        // sumindo colunade flag de pintura da tabela de hroarios
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);

        // definindo tamaho das colunas
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(350);

        // aumentando tamanho da linha
        jTable1.setRowHeight(30);

        // selecionar somente uma linha na tabela
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jTable1.setAutoCreateRowSorter(true);
    }

    public void botaoSelecionar() {
        String handle_paciente = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 0);
        String nomePaciente = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
        String cpfPaciente = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
        String nascimentoPaciente = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 3);
        String telefonePaciente = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 4);
        String celularPaciente = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 5);

        JIFAgendamento.jTFHANDLE_PACIENTE.setText(handle_paciente);
        JIFAgendamento.jTFPaciente.setText(nomePaciente);

        cpfPaciente.replace("\\.", "");
        cpfPaciente.replace("-", "");
        JIFAgendamento.jTFCpfPaciente.setText(cpfPaciente);
        JIFAgendamento.jTFNascimento.setText(nascimentoPaciente);
        JIFAgendamento.jTFTelefone.setText(telefonePaciente);
        JIFAgendamento.jTFCelular.setText(celularPaciente);

        // guardando em memoria os telefones para verificar se foi alterado
        JIFAgendamento.telefonePacienteSelecionado = telefonePaciente;
        JIFAgendamento.celularPacienteSelecionado = celularPaciente;

        // setando a variavel de hanle_paciente. para usar no cadastramento do agendamento
        JIFAgendamento.handle_paciente = Integer.valueOf(handle_paciente);

        JIFAgendamento.jTFNascimento.setEnabled(false);

        JIFAgendamento.jTFCpfPaciente.setEnabled(false);

        this.dispose();
        janelaPrincipal.internalFrameAgendamentoSelecionarUmPaciente = null;
        janelaPrincipal.internalFrameAgendamento.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTFNomePaciente = new javax.swing.JTextField(new documentoSomenteLetras(64), null, 0);
        jBPesquisaPaciente = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTFMensagemParaUsuario = new javax.swing.JTextField();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Selecione um Paciente",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {
                { null, "Cesar Augusto Schutz Fagundes", "011.006.330-90", "18/09/1987", "(51) 3330-5518",
                    "(51) 8432-4587" }, { null, null, null, null, null, null }, { null, null, null, null, null, null },
                { null, null, null, null, null, null } }, new String[] { "handle_paciente", "Nome", "CPF",
                "Nascimento", "Telefone", "Celular" }) {
            private static final long serialVersionUID = 1L;
            boolean[] canEdit = new boolean[] { false, false, false, false, false, false };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setText("Nome");

        jTFNomePaciente.setText("jTextField1");
        jTFNomePaciente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFNomePacienteKeyReleased(evt);
            }
        });

        jBPesquisaPaciente.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/menu/atendimentos/agenda/imagens/Lupa.png"))); // NOI18N
        jBPesquisaPaciente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBPesquisaPacienteActionPerformed(evt);
            }
        });
        jBPesquisaPaciente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBPesquisaPacienteKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(
                                jPanel1Layout
                                    .createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTFNomePaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 287,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jBPesquisaPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                        javax.swing.GroupLayout.PREFERRED_SIZE).addGap(0, 393, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE))
                    .addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(
                                javax.swing.GroupLayout.Alignment.TRAILING,
                                jPanel1Layout
                                    .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTFNomePaciente, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                            .addComponent(jBPesquisaPaciente, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 318,
                        javax.swing.GroupLayout.PREFERRED_SIZE).addGap(91, 91, 91)));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/cancelar.png"))); // NOI18N
        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jButton2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jButton2KeyReleased(evt);
            }
        });

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)
            .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 792, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup().addComponent(jButton2).addGap(0, 685, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout
                .createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 421,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 44,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton2)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
        janelaPrincipal.internalFrameAgendamento.setVisible(true);
    }// GEN-LAST:event_jButton2ActionPerformed

    private void jButton2KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jButton2KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.dispose();
            janelaPrincipal.internalFrameAgendamento.setVisible(true);
        }
    }// GEN-LAST:event_jButton2KeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable1MouseClicked
        botaoSelecionar();
    }// GEN-LAST:event_jTable1MouseClicked

    private void jBPesquisaPacienteKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBPesquisaPacienteKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && jTFNomePaciente.getText().length() >= 3) {
            preenchendoATabela(jTFNomePaciente.getText().toUpperCase());
            jTFNomePaciente.setText(jTFNomePaciente.getText().toUpperCase());
            jTFMensagemParaUsuario.setText("");
        } else if (jTFNomePaciente.getText().length() < 3 && evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Mínimo 3 caracteres para pesquisa");
        }
    }// GEN-LAST:event_jBPesquisaPacienteKeyReleased

    private void jBPesquisaPacienteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBPesquisaPacienteActionPerformed
        if (jTFNomePaciente.getText().length() >= 3) {
            preenchendoATabela(jTFNomePaciente.getText().toUpperCase());
            jTFNomePaciente.setText(jTFNomePaciente.getText().toUpperCase());
            jTFMensagemParaUsuario.setText("");
        } else {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Mínimo 3 caracteres para pesquisa");
        }
    }// GEN-LAST:event_jBPesquisaPacienteActionPerformed

    private void jTFNomePacienteKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTFNomePacienteKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && jTFNomePaciente.getText().length() >= 3) {
            preenchendoATabela(jTFNomePaciente.getText().toUpperCase());
            jTFMensagemParaUsuario.setText("");
        } else if (jTFNomePaciente.getText().length() < 3 && evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Mínimo 3 caracteres para pesquisa");
        }
    }// GEN-LAST:event_jTFNomePacienteKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBPesquisaPaciente;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    private javax.swing.JTextField jTFNomePaciente;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
