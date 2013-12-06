package br.bcn.admclin.interfacesGraficas.menu.cadastros.pessoal;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcento;
import br.bcn.admclin.dao.Conexao;
import br.bcn.admclin.dao.USUARIOS;
import br.bcn.admclin.dao.ESPECIALIDADES_MEDICAS;
import br.bcn.admclin.dao.model.Especialidades_Medicas;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 * 
 * @author Cesar Schutz
 */
public class JIFCEspecialidadesMedicas extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    String novoOuEditar;
    int especialidadeMedicaId;
    private Connection con = null;
    java.sql.Date dataDeHojeEmVariavelDate = null;

    public void pegandoDataDoSistema() {
        // pegando data do sistema
        Calendar hoje = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String dataDeHoje = format.format(hoje.getTime());
        try {
            dataDeHojeEmVariavelDate = new java.sql.Date(format.parse(dataDeHoje).getTime());
        } catch (ParseException ex) {

        }
    }

    /**
     * Creates new form JIFCEspecialidadesMedicas
     */
    public JIFCEspecialidadesMedicas(String novoOuEditar, int especialidadeMedicaId) {
        initComponents();
        this.novoOuEditar = novoOuEditar;
        this.especialidadeMedicaId = especialidadeMedicaId;
        iniciarClasse();
        pegandoDataDoSistema();
        tirandoBarraDeTitulo();
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    /** Metodo que vai no construtor da classe. Eexecuta operações antes de abrir a janela. */
    public void iniciarClasse() {
        // colocando maximo de caracteres nos jtextfield
        jTDescricao.setDocument(new DocumentoSemAspasEPorcento(64));

        if ("novo".equals(novoOuEditar)) {
            jBApagarRegistro.setVisible(false);
            jBAtualizarRegistro.setVisible(false);
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                "Cadastro de nova Especialidade Médica", javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.DEFAULT_POSITION));

        } else {
            int cont = 0;
            while (cont < JIFCEspecialidadesMedicasVisualizar.listaEspecialidadesMedicas.size()) {
                int codObjetos = JIFCEspecialidadesMedicasVisualizar.listaEspecialidadesMedicas.get(cont).getEmId();
                if (especialidadeMedicaId == codObjetos) {
                    jTDescricao.setText(JIFCEspecialidadesMedicasVisualizar.listaEspecialidadesMedicas.get(cont)
                        .getDescricao());
                }
                cont++;
            }
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Especialidade Médica",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            jBSalvarRegistro.setVisible(false);
        }

    }

    /**
     * Volta a janela ao seu estado inicial, dexando inativo os jTextField por exemplo. Atualiza a tabela de acordo com
     * os objetos.
     */
    public void botaoCancelar() {
        this.dispose();
        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameEspecialidadeMedicas = null;

        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameEspecialidadeMedicasVisualizar =
            new JIFCEspecialidadesMedicasVisualizar();
        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.jDesktopPane1
            .add(br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameEspecialidadeMedicasVisualizar);
        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameEspecialidadeMedicasVisualizar.setVisible(true);
        int lDesk = br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameEspecialidadeMedicasVisualizar.getWidth();
        int aIFrame = br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameEspecialidadeMedicasVisualizar.getHeight();

        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameEspecialidadeMedicasVisualizar.setLocation(
            lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }

    /** Salva uma nova Especialidade Medica na banco de dados. */
    public void botaoSalvarRegistro() {
        boolean descricaoPreenchida =
            MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTDescricao, 2, jTFMensagemParaUsuario);

        if (descricaoPreenchida) {
            con = Conexao.fazConexao();
            Especialidades_Medicas especialidadeMedicaMODELO = new Especialidades_Medicas();
            especialidadeMedicaMODELO.setDescricao(jTDescricao.getText());
            boolean existe = ESPECIALIDADES_MEDICAS.getConsultarParaSalvarNovoRegistro(con, especialidadeMedicaMODELO);
            Conexao.fechaConexao(con);
            if (ESPECIALIDADES_MEDICAS.conseguiuConsulta) {
                if (existe) {
                    JOptionPane.showMessageDialog(null, "Especialidade Médica já existe", "ATENÇÃO",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // fazer a inserção no banco
                    con = Conexao.fazConexao();
                    especialidadeMedicaMODELO.setUsuarioId(USUARIOS.usrId);
                    especialidadeMedicaMODELO.setDat(dataDeHojeEmVariavelDate);
                    boolean cadastro = ESPECIALIDADES_MEDICAS.setCadastrar(con, especialidadeMedicaMODELO);
                    Conexao.fechaConexao(con);
                    // atualiza tabela
                    if (cadastro) {
                        botaoCancelar();
                    }
                }
            }
        }
    }

    /** Atualiza uma Especialidade Medica no banco de dados. */
    public void botaoAtualizarRegistro() {
        boolean descricaoPreenchida =
            MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTDescricao, 2, jTFMensagemParaUsuario);

        if (descricaoPreenchida) {
            // fazendo um if para verificar se descricao ou referencia ja existem
            con = Conexao.fazConexao();
            Especialidades_Medicas especialidadeMedica = new Especialidades_Medicas();
            especialidadeMedica.setDescricao(jTDescricao.getText());
            especialidadeMedica.setEmId(especialidadeMedicaId);
            boolean existe = ESPECIALIDADES_MEDICAS.getConsultarParaAtualizarRegistro(con, especialidadeMedica);
            Conexao.fechaConexao(con);
            if (ESPECIALIDADES_MEDICAS.conseguiuConsulta) {
                if (existe) {
                    JOptionPane.showMessageDialog(null, "Especialidade Médica já existe", "ATENÇÃO",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } else {
                    con = Conexao.fazConexao();
                    especialidadeMedica.setUsuarioId(USUARIOS.usrId);
                    especialidadeMedica.setDat(dataDeHojeEmVariavelDate);
                    boolean atualizo = ESPECIALIDADES_MEDICAS.setUpdate(con, especialidadeMedica);
                    Conexao.fechaConexao(con);
                    if (atualizo) {
                        // dexando janela como no inicio
                        botaoCancelar();
                    }
                }
            }
        }
    }

    /** Apaga a Especialidade Medica selecionada do banco de dados. */
    public void botaoApagarRegistro() {
        int resposta =
            JOptionPane.showConfirmDialog(null, "Deseja realmente deletar essa Especialidade Médica?", "ATENÇÃO", 0);
        if (resposta == JOptionPane.YES_OPTION) {
            con = Conexao.fazConexao();
            boolean utilizada =
                ESPECIALIDADES_MEDICAS.getConsultarSeEspecialidadeEstaSendoUtilizada(con, especialidadeMedicaId);
            Conexao.fechaConexao(con);
            if (ESPECIALIDADES_MEDICAS.conseguiuConsulta) {
                if (utilizada) {
                    JOptionPane.showMessageDialog(null,
                        "Esta Especialidade Médica não pode ser deletada pois está sendo utilizada por algum Médico",
                        "ATENÇÃO", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // fazer o delete de acordo com o codigo
                    Especialidades_Medicas especialidadeMedicaMODEL = new Especialidades_Medicas();
                    especialidadeMedicaMODEL.setEmId(especialidadeMedicaId);
                    con = Conexao.fazConexao();
                    boolean deleto = ESPECIALIDADES_MEDICAS.setDeletar(con, especialidadeMedicaMODEL);
                    Conexao.fechaConexao(con);
                    // atualizar tabela
                    if (deleto) {
                        botaoCancelar();
                    }
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTDescricao = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jBCancelar = new javax.swing.JButton();
        jBSalvarRegistro = new javax.swing.JButton();
        jBApagarRegistro = new javax.swing.JButton();
        jBAtualizarRegistro = new javax.swing.JButton();
        jTFMensagemParaUsuario = new javax.swing.JTextField();

        setTitle("Cadastro de Especialidades Médicas");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados da Especialidade Médica",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

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

        jLabel5.setText("Descrição");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTDescricao, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                            .addComponent(jLabel5)).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel5)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTDescricao, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

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

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18));
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)
            .addGroup(
                layout.createSequentialGroup().addComponent(jBAtualizarRegistro)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBApagarRegistro)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBSalvarRegistro)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBCancelar)
                    .addContainerGap())
            .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout
                .createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 44,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jBAtualizarRegistro).addComponent(jBApagarRegistro)
                        .addComponent(jBSalvarRegistro).addComponent(jBCancelar))));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 404) / 2, (screenSize.height - 227) / 2, 404, 227);
    }// </editor-fold>//GEN-END:initComponents

    private void jTDescricaoFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTDescricaoFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("Mínimo 2 caracteres");
    }// GEN-LAST:event_jTDescricaoFocusGained

    private void jTDescricaoFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTDescricaoFocusLost
        jTFMensagemParaUsuario.setText("");
        boolean ok = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTDescricao, 2, jTFMensagemParaUsuario);
        if (ok) {
            jTDescricao.setBackground(new java.awt.Color(255, 255, 255));
        }
    }// GEN-LAST:event_jTDescricaoFocusLost

    private void jTDescricaoKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTDescricaoKeyReleased
        jTDescricao.setText(jTDescricao.getText().toUpperCase());
    }// GEN-LAST:event_jTDescricaoKeyReleased

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar();
    }// GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBCancelarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoCancelar();
        }
    }// GEN-LAST:event_jBCancelarKeyPressed

    private void jBSalvarRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBSalvarRegistroActionPerformed
        botaoSalvarRegistro();
    }// GEN-LAST:event_jBSalvarRegistroActionPerformed

    private void jBSalvarRegistroFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBSalvarRegistroFocusLost
        jTFMensagemParaUsuario.setText("");
        jTDescricao.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jBSalvarRegistroFocusLost

    private void jBSalvarRegistroKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBSalvarRegistroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoSalvarRegistro();
        }
    }// GEN-LAST:event_jBSalvarRegistroKeyPressed

    private void jBApagarRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBApagarRegistroActionPerformed
        botaoApagarRegistro();
    }// GEN-LAST:event_jBApagarRegistroActionPerformed

    private void jBApagarRegistroFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBApagarRegistroFocusLost

    }// GEN-LAST:event_jBApagarRegistroFocusLost

    private void jBApagarRegistroKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBApagarRegistroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoApagarRegistro();
        }
    }// GEN-LAST:event_jBApagarRegistroKeyPressed

    private void jBAtualizarRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBAtualizarRegistroActionPerformed
        botaoAtualizarRegistro();

    }// GEN-LAST:event_jBAtualizarRegistroActionPerformed

    private void jBAtualizarRegistroFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBAtualizarRegistroFocusLost
        jTFMensagemParaUsuario.setText("");
        jTDescricao.setBackground(new java.awt.Color(255, 255, 255)); // TODO add your handling code here:
    }// GEN-LAST:event_jBAtualizarRegistroFocusLost

    private void jBAtualizarRegistroKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBAtualizarRegistroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoAtualizarRegistro();
        }
    }// GEN-LAST:event_jBAtualizarRegistroKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBApagarRegistro;
    private javax.swing.JButton jBAtualizarRegistro;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBSalvarRegistro;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTDescricao;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    // End of variables declaration//GEN-END:variables
}
