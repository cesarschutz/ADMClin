/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.atendimentos.registrarAtendimento;

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

import br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcento;
import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSomenteLetras;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.MEDICOS;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

/**
 * 
 * @author Cesar Schutz
 */
public class JIFAtendimentoSelecionarUmMedicoSolicitante extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    private Connection con = null;
    public String nomePesquisado;

    /**
     * Creates new form JIFAtendimentoSelecionarUmPaciente
     */
    public JIFAtendimentoSelecionarUmMedicoSolicitante(String nomeParaPesquisar) {
        this.nomePesquisado = nomeParaPesquisar;
        initComponents();
        jTFNomeMedico.setText(nomePesquisado);
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

            
            ResultSet resultSet = null;
            boolean isNumber;
            try {
                @SuppressWarnings("unused")
                int x = Integer.valueOf(nomePesquisado);
                isNumber = true;
            } catch (Exception e) {
                isNumber = false;
            }
            
            if(isNumber){
            	resultSet = MEDICOS.getConsultarPorCRM(con, nomePesquisado);
            }else{
            	resultSet = MEDICOS.getConsultar(con, sql);
            }
            		
            try {
                while (resultSet.next()) {
                    if (resultSet.getInt("medicoid") > 0) {
                        modelo.addRow(new String[] { Integer.toString(resultSet.getInt("medicoid")),
                            resultSet.getString("nome"), resultSet.getString("crm"), resultSet.getString("ufcrm"),
                            resultSet.getString("nascimento"), resultSet.getString("telefone"),
                            resultSet.getString("celular") });
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
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(250);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(40);

        // aumentando tamanho da linha
        jTable1.setRowHeight(30);

        // selecionar somente uma linha na tabela
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jTable1.setAutoCreateRowSorter(true);

        jBSelecionarMedico.setVisible(false);
        jBEditarMedico.setVisible(false);
    }

    public void botaoSelecionar() {
    	if(jTable1.getSelectedRow() >= 0){
	        String handle_medico = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 0);
	        String nomeMedico = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
	
	        JIFCadastroDeAtendimento.jTFHANDLE_MEDICO_SOL.setText(handle_medico);
	        JIFCadastroDeAtendimento.jTFMedicoSol.setText(nomeMedico);
	
	        // setando a variavel de hanle_paciente. para usar no cadastramento do atendimento
	        JIFCadastroDeAtendimento.handle_medico_sol = Integer.valueOf(handle_medico);
	
	        this.dispose();
	        janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante = null;
	        janelaPrincipal.internalFrameAtendimento.setVisible(true);
	        janelaPrincipal.internalFrameAtendimento.verificaMedicoSemAlerta();
    	}

    }

    /*
     * metodo que abre o paciente selecionado para editar e seleciona-lo
     */
    public void botaoEditarMedico() {
    	if(jTable1.getSelectedRow() >= 0){
	        //if (JIFAtendimentoAgenda.veioDaPesquisa) {
	        //    janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante.setVisible(false);
	        //} else {
	            janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante.setVisible(false);
	        //}
	
	        int handle_medico = Integer.valueOf(String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0)));
	        janelaPrincipal.internalFrameAtendimentoCadastroMedicos = new JIFCMedicosAtendimentos("editar", handle_medico);
	
	        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameAtendimentoCadastroMedicos);
	        janelaPrincipal.internalFrameAtendimentoCadastroMedicos.setVisible(true);
	        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
	        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
	        int lIFrame = janelaPrincipal.internalFrameAtendimentoCadastroMedicos.getWidth();
	        int aIFrame = janelaPrincipal.internalFrameAtendimentoCadastroMedicos.getHeight();
	
	        janelaPrincipal.internalFrameAtendimentoCadastroMedicos.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2
	            - aIFrame / 2);
    	}
    }

    /*
     * metodo que abre o o cadastro de paciente para cadastrar um paciente e seleciona-lo
     */
    public void botaoNovoMedico() {
      //if (JIFAtendimentoAgenda.veioDaPesquisa) {
        //    janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante.setVisible(false);
        //} else {
            janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante.setVisible(false);
        //}

        janelaPrincipal.internalFrameAtendimentoCadastroMedicos = new JIFCMedicosAtendimentos("novo", 0);

        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameAtendimentoCadastroMedicos);
        janelaPrincipal.internalFrameAtendimentoCadastroMedicos.setVisible(true);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameAtendimentoCadastroMedicos.getWidth();
        int aIFrame = janelaPrincipal.internalFrameAtendimentoCadastroMedicos.getHeight();

        janelaPrincipal.internalFrameAtendimentoCadastroMedicos.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2
            - aIFrame / 2);
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
        jTFNomeMedico = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(64), null, 0);
        jBPesquisaMedico = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jTFMensagemParaUsuario = new javax.swing.JTextField();
        jBNovoMedico = new javax.swing.JButton();
        jBEditarMedico = new javax.swing.JButton();
        jBSelecionarMedico = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Selecione um Médico Solicitante",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {
            { null, "Cesar Augusto Schutz Fagundes", "011.006.330-90", null, "18/09/1987", "(51) 3330-5518",
                "(51) 8432-4587" }, { null, null, null, null, null, null, null },
            { null, null, null, null, null, null, null }, { null, null, null, null, null, null, null } }, new String[] {
            "medicoid", "Nome", "CRM", "UF CRM", "Nascimento", "Telefone", "Celular" }) {
            private static final long serialVersionUID = 1L;
            boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false };

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

        jLabel1.setText("Nome / CRM");

        jTFNomeMedico.setText("jTextField1");
        jTFNomeMedico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFNomeMedicoKeyReleased(evt);
            }
        });

        jBPesquisaMedico.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/Lupa.png"))); // NOI18N
        jBPesquisaMedico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBPesquisaMedicoActionPerformed(evt);
            }
        });
        jBPesquisaMedico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBPesquisaMedicoKeyReleased(evt);
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
                                    .addComponent(jTFNomeMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 287,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jBPesquisaMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
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
                                    .addComponent(jTFNomeMedico, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                            .addComponent(jBPesquisaMedico, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 318,
                        javax.swing.GroupLayout.PREFERRED_SIZE).addGap(91, 91, 91)));

        jBCancelar.setBackground(new java.awt.Color(113, 144, 224));
        jBCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemSetaParaEsquerda.png"))); // NOI18N
        jBCancelar.setText("Voltar");
        jBCancelar.setPreferredSize(new java.awt.Dimension(89, 39));
        jBCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCancelarActionPerformed(evt);
            }
        });
        jBCancelar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBCancelarKeyPressed(evt);
            }
        });

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        jBNovoMedico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/novo.png"))); // NOI18N
        jBNovoMedico.setText("Novo Médico");
        jBNovoMedico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBNovoMedicoActionPerformed(evt);
            }
        });
        jBNovoMedico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBNovoMedicoKeyPressed(evt);
            }
        });

        jBEditarMedico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/atualizar.png"))); // NOI18N
        jBEditarMedico.setText("Editar Médico");
        jBEditarMedico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEditarMedicoActionPerformed(evt);
            }
        });
        jBEditarMedico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBEditarMedicoKeyPressed(evt);
            }
        });

        jBSelecionarMedico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemBotaoOk.png"))); // NOI18N
        jBSelecionarMedico.setText("Selecionar Médico");
        jBSelecionarMedico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSelecionarMedicoActionPerformed(evt);
            }
        });
        jBSelecionarMedico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBSelecionarMedicoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)
            .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 792, Short.MAX_VALUE)
            .addGroup(
                layout
                    .createSequentialGroup()
                    .addComponent(jBCancelar, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jBSelecionarMedico)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBEditarMedico)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBNovoMedico)
                    .addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout
                .createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 421,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 44,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(
                    layout
                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jBNovoMedico)
                        .addComponent(jBCancelar, javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jBEditarMedico)
                        .addComponent(jBSelecionarMedico)).addContainerGap()));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCancelarActionPerformed
        this.dispose();
        janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante = null;
        janelaPrincipal.internalFrameAtendimento.setVisible(true);
    }// GEN-LAST:event_jBCancelarActionPerformed

    private void jBPesquisaMedicoKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBPesquisaMedicoKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && jTFNomeMedico.getText().length() >= 3) {
            preenchendoATabela(jTFNomeMedico.getText().toUpperCase());
            jTFNomeMedico.setText(jTFNomeMedico.getText().toUpperCase());
            jTFMensagemParaUsuario.setText("");
        } else if (jTFNomeMedico.getText().length() < 3 && evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Mínimo 3 caracteres para pesquisa");
        }
    }// GEN-LAST:event_jBPesquisaMedicoKeyReleased

    private void jBPesquisaMedicoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBPesquisaMedicoActionPerformed
        if (jTFNomeMedico.getText().length() >= 3) {
            preenchendoATabela(jTFNomeMedico.getText().toUpperCase());
            jTFNomeMedico.setText(jTFNomeMedico.getText().toUpperCase());
            jTFMensagemParaUsuario.setText("");
        } else {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Mínimo 3 caracteres para pesquisa");
        }
    }// GEN-LAST:event_jBPesquisaMedicoActionPerformed

    private void jTFNomeMedicoKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTFNomeMedicoKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && jTFNomeMedico.getText().length() >= 3) {
            preenchendoATabela(jTFNomeMedico.getText().toUpperCase());
            jTFMensagemParaUsuario.setText("");
        } else if (jTFNomeMedico.getText().length() < 3 && evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Mínimo 3 caracteres para pesquisa");
        }
    }// GEN-LAST:event_jTFNomeMedicoKeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable1MouseClicked
        jBSelecionarMedico.setVisible(true);
        jBEditarMedico.setVisible(true);
    }// GEN-LAST:event_jTable1MouseClicked

    private void jBSelecionarMedicoKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBSelecionarMedicoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoSelecionar();
        }
    }// GEN-LAST:event_jBSelecionarMedicoKeyPressed

    private void jBSelecionarMedicoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBSelecionarMedicoActionPerformed
        botaoSelecionar();
    }// GEN-LAST:event_jBSelecionarMedicoActionPerformed

    private void jBEditarMedicoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBEditarMedicoActionPerformed
        botaoEditarMedico();
    }// GEN-LAST:event_jBEditarMedicoActionPerformed

    private void jBEditarMedicoKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBEditarMedicoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoEditarMedico();
        }
    }// GEN-LAST:event_jBEditarMedicoKeyPressed

    private void jBNovoMedicoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBNovoMedicoActionPerformed
        botaoNovoMedico();
    }// GEN-LAST:event_jBNovoMedicoActionPerformed

    private void jBNovoMedicoKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBNovoMedicoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoNovoMedico();
        }
    }// GEN-LAST:event_jBNovoMedicoKeyPressed

    private void jBCancelarKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBCancelarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.dispose();
            janelaPrincipal.internalFrameAtendimento.setVisible(true);
        } // TODO add your handling code here:
    }// GEN-LAST:event_jBCancelarKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBEditarMedico;
    private javax.swing.JButton jBNovoMedico;
    private javax.swing.JButton jBPesquisaMedico;
    private javax.swing.JButton jBSelecionarMedico;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    public static javax.swing.JTextField jTFNomeMedico;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
