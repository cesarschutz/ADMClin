package br.bcn.admclin.interfacesGraficas.menu.cadastros.agenda;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcento;
import br.bcn.admclin.dao.dbris.AREAS_ATENDIMENTO;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.TB_CLASSESDEEXAMES;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.dao.model.Areas_atendimento;
import br.bcn.admclin.dao.model.Tb_ClassesDeExames;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;

/**
 * 
 * @author Cesar Schutz
 */
public class JIFAreasAtendimento extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    String novoOuEditar;
    int id_areas_atendimento;
    String nomeArea;

    private Connection con = null;

    /**
     * Creates new form JIModelo
     */
    public JIFAreasAtendimento(String novoOuEditar, int is_areas_atendimento, String nomeArea) {
        initComponents();
        this.novoOuEditar = novoOuEditar;
        this.id_areas_atendimento = is_areas_atendimento;
        this.nomeArea = nomeArea;
        iniciarClasse();
        tirandoBarraDeTitulo();
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    /** Metodo que vai no construtor da classe. Eexecuta operações antes de abrir a janela. */
    @SuppressWarnings("unchecked")
    public void iniciarClasse() {
        // colocando maximo de caracteres nos jtextfield
        jTDescricao.setDocument(new DocumentoSemAspasEPorcento(64));

        if ("novo".equals(novoOuEditar)) {
            jBAtualizarRegistro.setVisible(false);
            jBApagarRegistro.setVisible(false);
            jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastro de nova Área de Atendimento",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        } else {
            jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Área de Atendimento",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            jBSalvarRegistro.setVisible(false);
            jTDescricao.setText(nomeArea);
        }

    } // ok

    /**
     * Volta a janela ao seu estado inicial, dexando inativo os jTextField por exemplo. Atualiza a tabela de acordo com
     * os objetos.
     */
    public void botaoCancelar() {
        this.dispose();
        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameAreasDeAtendimento = null;

        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameAreasDeAtendimentoVisualizar = new JIFAreasAtendimentoVisualizar();
        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.jDesktopPane1
            .add(br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameAreasDeAtendimentoVisualizar);
        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameAreasDeAtendimentoVisualizar.setVisible(true);
        int lDesk = br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameAreasDeAtendimentoVisualizar.getWidth();
        int aIFrame = br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameAreasDeAtendimentoVisualizar.getHeight();

        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameAreasDeAtendimentoVisualizar.setLocation(lDesk / 2 - lIFrame / 2,
            aDesk / 2 - aIFrame / 2);
    }

    /** Salva uma nova classe de exame na banco de dados. */
    public void botaoSalvarRegistro() {
        Areas_atendimento area = new Areas_atendimento();
        area.setNome(jTDescricao.getText().toUpperCase());
        boolean cadastro = AREAS_ATENDIMENTO.setCadastrar(area);
        // atualiza tabela
        if (cadastro) {
            botaoCancelar();
        }
    }

    /** Atualiza uma classe de exame no banco de dados. */
    public void botaoAtualizarRegistro() {
        Areas_atendimento area = new Areas_atendimento();
        area.setNome(jTDescricao.getText().toUpperCase());
        area.setId_areas_atendimento(id_areas_atendimento);
        boolean atualizo = AREAS_ATENDIMENTO.setUpdate(area);
        if (atualizo) {
            // dexando janela como no inicio
            botaoCancelar();
        }
    }

    /** Apaga a classe de exame selecionada do banco de dados. */
    public void botaoApagarRegistro() {
        int resposta =
            JOptionPane.showConfirmDialog(null, "Deseja realmente deletar essa Área de Atendimento?", "ATENÇÃO", 0);
        if (resposta == JOptionPane.YES_OPTION) {
            Areas_atendimento area = new Areas_atendimento();
            area.setId_areas_atendimento(id_areas_atendimento);
            boolean deleto = AREAS_ATENDIMENTO.setDeletar(area);
            // atualizar tabela
            if (deleto) {
                botaoCancelar();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jTDescricao = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jBAtualizarRegistro = new javax.swing.JButton();
        jBApagarRegistro = new javax.swing.JButton();
        jBSalvarRegistro = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();

        setTitle("Cadastro de Classes de Exames");

        jPanel3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Dados da Classe de Exame", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, null));

        jTDescricao.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTDescricaoFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTDescricaoFocusLost(evt);
            }
        });
        jTDescricao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTDescricaoKeyReleased(evt);
            }
        });

        jLabel5.setText("Nome");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(jLabel5)
                        .addComponent(jTDescricao, GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE))
                    .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel5)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(jTDescricao, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel3.setLayout(jPanel3Layout);

        jBAtualizarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/atualizar.png"))); // NOI18N
        jBAtualizarRegistro.setText("Atualizar");
        jBAtualizarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBAtualizarRegistro.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBAtualizarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAtualizarRegistroActionPerformed(evt);
            }
        });
        jBAtualizarRegistro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBAtualizarRegistroFocusLost(evt);
            }
        });
        jBAtualizarRegistro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBAtualizarRegistroKeyPressed(evt);
            }
        });

        jBApagarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/deletar.png"))); // NOI18N
        jBApagarRegistro.setText("Apagar");
        jBApagarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBApagarRegistro.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBApagarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBApagarRegistroActionPerformed(evt);
            }
        });
        jBApagarRegistro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBApagarRegistroFocusLost(evt);
            }
        });
        jBApagarRegistro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBApagarRegistroKeyPressed(evt);
            }
        });

        jBSalvarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/salvar.png"))); // NOI18N
        jBSalvarRegistro.setText("Salvar");
        jBSalvarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBSalvarRegistro.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBSalvarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSalvarRegistroActionPerformed(evt);
            }
        });
        jBSalvarRegistro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBSalvarRegistroFocusLost(evt);
            }
        });
        jBSalvarRegistro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBSalvarRegistroKeyPressed(evt);
            }
        });

        jBCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/cancelar.png"))); // NOI18N
        jBCancelar.setText("Cancelar");
        jBCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jBAtualizarRegistro)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(jBApagarRegistro)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(jBSalvarRegistro)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(jBCancelar)
                    .addContainerGap())
                .addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                    .addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(jBAtualizarRegistro)
                        .addComponent(jBApagarRegistro)
                        .addComponent(jBSalvarRegistro)
                        .addComponent(jBCancelar))
                    .addGap(21))
        );
        getContentPane().setLayout(layout);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 477) / 2, (screenSize.height - 284) / 2, 477, 220);
    }// </editor-fold>//GEN-END:initComponents

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar();
    }// GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBCancelarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoCancelar();
        }
    }// GEN-LAST:event_jBCancelarKeyPressed

    private void jBAtualizarRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBAtualizarRegistroActionPerformed
        botaoAtualizarRegistro();
    }// GEN-LAST:event_jBAtualizarRegistroActionPerformed

    private void jBAtualizarRegistroKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBAtualizarRegistroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoAtualizarRegistro();
        }
    }// GEN-LAST:event_jBAtualizarRegistroKeyPressed

    private void jBSalvarRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBSalvarRegistroActionPerformed
        botaoSalvarRegistro();
    }// GEN-LAST:event_jBSalvarRegistroActionPerformed

    private void jBSalvarRegistroKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBSalvarRegistroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoSalvarRegistro();
        }
    }// GEN-LAST:event_jBSalvarRegistroKeyPressed

    private void jBApagarRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBApagarRegistroActionPerformed
        botaoApagarRegistro();
    }// GEN-LAST:event_jBApagarRegistroActionPerformed

    private void jBApagarRegistroKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBApagarRegistroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoApagarRegistro();
        }
    }// GEN-LAST:event_jBApagarRegistroKeyPressed

    private void jBSalvarRegistroFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBSalvarRegistroFocusLost
    }// GEN-LAST:event_jBSalvarRegistroFocusLost

    private void jBApagarRegistroFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBApagarRegistroFocusLost

    }// GEN-LAST:event_jBApagarRegistroFocusLost

    private void jTDescricaoKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTDescricaoKeyReleased
        jTDescricao.setText(jTDescricao.getText().toUpperCase());
    }// GEN-LAST:event_jTDescricaoKeyReleased

    private void jBAtualizarRegistroFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBAtualizarRegistroFocusLost // TODO add your handling code here:
    }// GEN-LAST:event_jBAtualizarRegistroFocusLost

    private void jTDescricaoFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTDescricaoFocusGained
    }// GEN-LAST:event_jTDescricaoFocusGained

    private void jTDescricaoFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTDescricaoFocusLost
    }// GEN-LAST:event_jTDescricaoFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBApagarRegistro;
    private javax.swing.JButton jBAtualizarRegistro;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBSalvarRegistro;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTDescricao;
    // End of variables declaration//GEN-END:variables
}
