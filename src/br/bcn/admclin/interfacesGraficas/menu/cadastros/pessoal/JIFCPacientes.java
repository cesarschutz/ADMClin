package br.bcn.admclin.interfacesGraficas.menu.cadastros.pessoal;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcento;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSomenteLetras;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.PACIENTES;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.dao.model.Pacientes;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 * 
 * @author Cesar Schutz
 */
public class JIFCPacientes extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;

    int handle_paciente = 0;

    String novoOuEditar;
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

    /** Creates new form JIFCPacientes */
    public JIFCPacientes(String novoOuEditar, int handle_paciente) {
        initComponents();
        this.novoOuEditar = novoOuEditar;
        this.handle_paciente = handle_paciente;
        iniciarClasse();

        pegandoDataDoSistema();
        tirandoBarraDeTitulo();
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    /** Metodo "construtor" **/
    public void iniciarClasse() {

        if ("editar".equals(novoOuEditar)) {
            int cont = 0;
            jBSalvarRegistro.setVisible(false);
            jBApagarRegistro.setVisible(true);
            jBAtualizarRegistro.setVisible(true);
            while (cont < JIFCPacientesVisualizar.listaPacientes.size()) {

                int codObjetos = JIFCPacientesVisualizar.listaPacientes.get(cont).getHandle_paciente();

                if (handle_paciente == codObjetos) {
                    jTFNome.setText(JIFCPacientesVisualizar.listaPacientes.get(cont).getNome());
                    jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Paciente",
                        javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
                    if (!"   .   .   -  ".equals(JIFCPacientesVisualizar.listaPacientes.get(cont).getCpf()))
                        jTFCpf.setText(JIFCPacientesVisualizar.listaPacientes.get(cont).getCpf());
                    if (!"  /  /    ".equals(JIFCPacientesVisualizar.listaPacientes.get(cont).getNascimento()))
                        jTFNascimento.setText(JIFCPacientesVisualizar.listaPacientes.get(cont).getNascimento());
                    if (!"   ".equals(JIFCPacientesVisualizar.listaPacientes.get(cont).getPeso()))
                        jTFPeso.setText(JIFCPacientesVisualizar.listaPacientes.get(cont).getPeso());
                    if (!"   ".equals(JIFCPacientesVisualizar.listaPacientes.get(cont).getAltura()))
                        jTFAltura.setText(JIFCPacientesVisualizar.listaPacientes.get(cont).getAltura());
                    if (!"(  )     -    ".equals(JIFCPacientesVisualizar.listaPacientes.get(cont).getTelefone()))
                        jTFTelefone.setText(JIFCPacientesVisualizar.listaPacientes.get(cont).getTelefone());
                    if (!"(  )     -    ".equals(JIFCPacientesVisualizar.listaPacientes.get(cont).getCelular()))
                        jTFCelular.setText(JIFCPacientesVisualizar.listaPacientes.get(cont).getCelular());
                    jTFEndereco.setText(JIFCPacientesVisualizar.listaPacientes.get(cont).getEndereco());
                    jTFBairro.setText(JIFCPacientesVisualizar.listaPacientes.get(cont).getBairro());
                    if (!"  .   -   ".equals(JIFCPacientesVisualizar.listaPacientes.get(cont).getCep()))
                        jTFCep.setText(JIFCPacientesVisualizar.listaPacientes.get(cont).getCep());
                    jTFCidade.setText(JIFCPacientesVisualizar.listaPacientes.get(cont).getCidade());
                    if (!"          ".equals(JIFCPacientesVisualizar.listaPacientes.get(cont).getRg()))
                        jTFRg.setText(JIFCPacientesVisualizar.listaPacientes.get(cont).getRg());
                    jTFProfissao.setText(JIFCPacientesVisualizar.listaPacientes.get(cont).getProfissao());
                    jTFEmail.setText(JIFCPacientesVisualizar.listaPacientes.get(cont).getEmail());
                    jTFResponsavel.setText(JIFCPacientesVisualizar.listaPacientes.get(cont).getResponsavel());
                    if (!"   .   .   -  ".equals(JIFCPacientesVisualizar.listaPacientes.get(cont).getCpfResponsavel()))
                        jTFCpfResponsavel.setText(JIFCPacientesVisualizar.listaPacientes.get(cont).getCpfResponsavel());
                    jTAObs.setText(JIFCPacientesVisualizar.listaPacientes.get(cont).getObs());
                    if (!"(  )     -    ".equals(JIFCPacientesVisualizar.listaPacientes.get(cont)
                        .getTelefone_responsavel()))
                        jTFTelefoneResponsavel.setText(JIFCPacientesVisualizar.listaPacientes.get(cont)
                            .getTelefone_responsavel());
                    if (!"".equals(JIFCPacientesVisualizar.listaPacientes.get(cont).getSexo())) {
                        if (JIFCPacientesVisualizar.listaPacientes.get(cont).getSexo().equals("M")) {
                            jCBSexo.setSelectedIndex(1);
                        } else if (JIFCPacientesVisualizar.listaPacientes.get(cont).getSexo().equals("F")) {
                            jCBSexo.setSelectedIndex(2);
                        }
                    }

                    if (!"".equals(JIFCPacientesVisualizar.listaPacientes.get(cont).getCor()))
                        jCBCor.setSelectedItem(JIFCPacientesVisualizar.listaPacientes.get(cont).getCor());
                    if (!"".equals(JIFCPacientesVisualizar.listaPacientes.get(cont).getEstadoCivil()))
                        jCBEstadoCivil.setSelectedItem(JIFCPacientesVisualizar.listaPacientes.get(cont)
                            .getEstadoCivil());
                    if (!"".equals(JIFCPacientesVisualizar.listaPacientes.get(cont).getUf()))
                        jCBUf.setSelectedItem(JIFCPacientesVisualizar.listaPacientes.get(cont).getUf());

                    if (!"".equals(jTFResponsavel.getText())) {
                        jTFCpfResponsavel.setEnabled(true);
                        jTFTelefoneResponsavel.setEnabled(true);
                    }
                }

                cont++;
            }
        }

        if ("novo".equals(novoOuEditar)) {
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastro de novo Paciente",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        }
    }

    /**
     * Volta a janela ao seu estado inicial, dexando inativo os jTextField por exemplo. Atualiza a tabela de acordo com
     * os objetos.
     */
    public void botaoCancelar() {
        this.dispose();
        janelaPrincipal.internalFramePaciente = null;

        janelaPrincipal.internalFramePacienteVisualizar = new JIFCPacientesVisualizar();
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFramePacienteVisualizar);
        janelaPrincipal.internalFramePacienteVisualizar.setVisible(true);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFramePacienteVisualizar.getWidth();
        int aIFrame = janelaPrincipal.internalFramePacienteVisualizar.getHeight();

        janelaPrincipal.internalFramePacienteVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }

    /*
     * metodo que verifica se foi preenchido o nome corretamente
     */
    public boolean verificarSeNomeENascimentoFOramPreenchidosCorretamente() {

        // verficando data se foi preenche
        boolean NascimentoOK = true;
        if (!"  /  /    ".equals(jTFNascimento.getText())) {
            NascimentoOK = MetodosUteis.verificarSeDataDeNascimentoEValida(jTFNascimento, jTFMensagemParaUsuario);
        }

        boolean nomeResponsavelOk = true;
        if (!"".equals(jTFResponsavel.getText()))
            nomeResponsavelOk = MetodosUteis.veriricarSeNomeDePessaoEhValido(jTFNome, jTFMensagemParaUsuario);

        boolean emailOk = true;
        if (!"".equals(jTFEmail.getText()))
            emailOk = MetodosUteis.verificarSeEmailEValido(jTFEmail, jTFMensagemParaUsuario);

        boolean nomeok = MetodosUteis.veriricarSeNomeDePessaoEhValido(jTFNome, jTFMensagemParaUsuario);

        if (NascimentoOK && nomeok && nomeResponsavelOk && emailOk) {
            return true;
        } else {
            return false;
        }

    }

    /** Salva uma nova classe de exame na banco de dados. */
    public void botaoSalvarRegistro() {

        if (verificarSeNomeENascimentoFOramPreenchidosCorretamente()) {
            // salva no banco
            Pacientes pacienteModel = new Pacientes();
            pacienteModel.setUsuarioId(USUARIOS.usrId);
            pacienteModel.setData(dataDeHojeEmVariavelDate);

            pacienteModel.setNome(jTFNome.getText());
            pacienteModel.setCpf(jTFCpf.getText());
            pacienteModel.setNascimento(jTFNascimento.getText());
            pacienteModel.setResponsavel(jTFResponsavel.getText());
            pacienteModel.setCpfResponsavel(jTFCpfResponsavel.getText());
            if (jCBSexo.getSelectedIndex() == 1) {
                pacienteModel.setSexo("M");
            } else if (jCBSexo.getSelectedIndex() == 2) {
                pacienteModel.setSexo("F");
            } else {
                pacienteModel.setSexo("");
            }
            pacienteModel.setPeso(jTFPeso.getText());
            pacienteModel.setAltura(jTFAltura.getText());
            pacienteModel.setTelefone(jTFTelefone.getText());
            pacienteModel.setCelular(jTFCelular.getText());
            pacienteModel.setEndereco(jTFEndereco.getText());
            pacienteModel.setBairro(jTFBairro.getText());
            pacienteModel.setCep(jTFCep.getText());
            pacienteModel.setCidade(jTFCidade.getText());
            pacienteModel.setUf((String) jCBUf.getSelectedItem());
            pacienteModel.setRg(jTFRg.getText());
            pacienteModel.setProfissao(jTFProfissao.getText());
            pacienteModel.setEmail(jTFEmail.getText());
            pacienteModel.setCor((String) jCBCor.getSelectedItem());
            pacienteModel.setEstadoCivil((String) jCBEstadoCivil.getSelectedItem());
            pacienteModel.setObs(jTAObs.getText());
            pacienteModel.setTelefone_responsavel(jTFTelefoneResponsavel.getText());
            con = Conexao.fazConexao();
            boolean cadastro = PACIENTES.setCadastrar(con, pacienteModel);
            Conexao.fechaConexao(con);
            if (cadastro) {
                botaoCancelar();
            }
        }
    }

    /** Atualiza um Paciente no banco de dados. */
    public void botaoAtualizarRegistro() {

        if (verificarSeNomeENascimentoFOramPreenchidosCorretamente()) {
            // salva no banco
            Pacientes pacienteModel = new Pacientes();
            pacienteModel.setUsuarioId(USUARIOS.usrId);
            pacienteModel.setData(dataDeHojeEmVariavelDate);
            pacienteModel.setHandle_paciente(handle_paciente);
            pacienteModel.setNome(jTFNome.getText());
            pacienteModel.setCpf(jTFCpf.getText());
            pacienteModel.setNascimento(jTFNascimento.getText());
            pacienteModel.setResponsavel(jTFResponsavel.getText());
            pacienteModel.setCpfResponsavel(jTFCpfResponsavel.getText());
            if (jCBSexo.getSelectedIndex() == 1) {
                pacienteModel.setSexo("M");
            } else if (jCBSexo.getSelectedIndex() == 2) {
                pacienteModel.setSexo("F");
            } else {
                pacienteModel.setSexo("");
            }
            pacienteModel.setPeso(jTFPeso.getText());
            pacienteModel.setAltura(jTFAltura.getText());
            pacienteModel.setTelefone(jTFTelefone.getText());
            pacienteModel.setCelular(jTFCelular.getText());
            pacienteModel.setEndereco(jTFEndereco.getText());
            pacienteModel.setBairro(jTFBairro.getText());
            pacienteModel.setCep(jTFCep.getText());
            pacienteModel.setCidade(jTFCidade.getText());
            pacienteModel.setUf((String) jCBUf.getSelectedItem());
            pacienteModel.setRg(jTFRg.getText());
            pacienteModel.setProfissao(jTFProfissao.getText());
            pacienteModel.setEmail(jTFEmail.getText());
            pacienteModel.setCor((String) jCBCor.getSelectedItem());
            pacienteModel.setEstadoCivil((String) jCBEstadoCivil.getSelectedItem());
            pacienteModel.setObs(jTAObs.getText());
            pacienteModel.setTelefone_responsavel(jTFTelefoneResponsavel.getText());
            con = Conexao.fazConexao();
            boolean atualizo = PACIENTES.setUpdate(con, pacienteModel);
            Conexao.fechaConexao(con);
            if (atualizo) {
                botaoCancelar();
            }
        }
    }

    /** Deleta o Paciente selecionado do banco de dados. */
    public void botaoApagarRegistro() {
        int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente deletar esse Paciente?", "ATENÇÃO", 0);
        if (resposta == JOptionPane.YES_OPTION) {
            // fazer o delete de acordo com o codigo
            Pacientes pacienteModel = new Pacientes();
            pacienteModel.setHandle_paciente(handle_paciente);
            con = Conexao.fazConexao();
            boolean deleto = PACIENTES.setDeletar(con, pacienteModel);
            Conexao.fechaConexao(con);
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

        jPanel1 = new javax.swing.JPanel();
        jTFNome = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(64), null, 0);
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        new br.bcn.admclin.ClasseAuxiliares.MetodosUteis();
        jTFNascimento = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("##/##/####"));
        jLabel8 = new javax.swing.JLabel();
        jCBSexo = new javax.swing.JComboBox();
        jTFPeso = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("###"));
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTFAltura = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("###"));
        jTFTelefone = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("(##) ####-####"));
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTFCelular = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("(##) ####-####"));
        jTFEndereco = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(80), null, 0);
        jLabel13 = new javax.swing.JLabel();
        jTFBairro = new javax.swing.JTextField(new DocumentoSomenteLetras(32), null, 0);
        jLabel14 = new javax.swing.JLabel();
        jTFCep = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("##.###-###"));
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTFCidade = new javax.swing.JTextField(new DocumentoSomenteLetras(32), null, 0);
        jCBUf = new javax.swing.JComboBox();
        jTFRg = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("##########"));
        jLabel18 = new javax.swing.JLabel();
        jTFProfissao = new javax.swing.JTextField(new DocumentoSomenteLetras(32), null, 0);
        jLabel19 = new javax.swing.JLabel();
        jTFEmail = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(64), null, 0);
        jLabel20 = new javax.swing.JLabel();
        jCBCor = new javax.swing.JComboBox();
        jLabel21 = new javax.swing.JLabel();
        jCBEstadoCivil = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTFCpfResponsavel = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("###.###.###-##"));
        jLabel6 = new javax.swing.JLabel();
        jTFResponsavel = new javax.swing.JTextField(new DocumentoSomenteLetras(64), null, 0);
        jLabel23 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTAObs = new javax.swing.JTextArea(new DocumentoSemAspasEPorcento(1600));
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jTFCpf = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("###.###.###-##"));
        jLabel4 = new javax.swing.JLabel();
        jTFTelefoneResponsavel = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("(##) ####-####"));
        jLabel24 = new javax.swing.JLabel();
        jTFMensagemParaUsuario = new javax.swing.JTextField();
        jBAtualizarRegistro = new javax.swing.JButton();
        jBApagarRegistro = new javax.swing.JButton();
        jBSalvarRegistro = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();

        setTitle("Cadastro de Pacientes");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados do Paciente",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel3.setText("Nome");

        jLabel5.setText("Nascimento");

        jTFNascimento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFNascimentoFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFNascimentoFocusLost(evt);
            }
        });

        jLabel8.setText("Sexo");

        jCBSexo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Masculino", "Feminino" }));

        jTFPeso.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFPesoFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFPesoFocusLost(evt);
            }
        });

        jLabel9.setText("Peso");

        jLabel10.setText("Altura");

        jTFAltura.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFAlturaFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFAlturaFocusLost(evt);
            }
        });

        jTFTelefone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFTelefoneFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFTelefoneFocusLost(evt);
            }
        });

        jLabel11.setText("Telefone");

        jLabel12.setText("Celular");

        jTFCelular.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFCelularFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFCelularFocusLost(evt);
            }
        });

        jLabel13.setText("Endereço");

        jLabel14.setText("Bairro");

        jTFCep.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFCepFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFCepFocusLost(evt);
            }
        });

        jLabel15.setText("CEP");

        jLabel16.setText("UF");

        jLabel17.setText("Cidade");

        jCBUf.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "AC", "AL", "AP", "AM", "BA", "CE",
            "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC",
            "SP", "SE", "TO" }));

        jTFRg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFRgFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFRgFocusLost(evt);
            }
        });

        jLabel18.setText("RG");

        jLabel19.setText("Profissão");

        jTFEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFEmailFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFEmailFocusLost(evt);
            }
        });

        jLabel20.setText("E-Mail");

        jCBCor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Branca", "Preta", "Outra" }));

        jLabel21.setText("Cor");

        jCBEstadoCivil.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Solteiro", "Casado",
            "Divorciado", "Viuvo", "Outro" }));

        jLabel22.setText("Estado Civil");

        jLabel7.setText("Nome Responsável");

        jTFCpfResponsavel.setEnabled(false);
        jTFCpfResponsavel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFCpfResponsavelFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFCpfResponsavelFocusLost(evt);
            }
        });

        jLabel6.setText("CPF Responsável");

        jTFResponsavel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFResponsavelKeyReleased(evt);
            }
        });

        jLabel23.setText("Observação");

        jTAObs.setColumns(20);
        jTAObs.setRows(5);
        jTAObs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTAObsKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTAObs);

        jLabel25.setText("Kg");

        jLabel26.setText("Cm");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jTFCpf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFCpfFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFCpfFocusLost(evt);
            }
        });

        jLabel4.setText("CPF");

        jTFTelefoneResponsavel.setEnabled(false);
        jTFTelefoneResponsavel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFTelefoneResponsavelFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFTelefoneResponsavelFocusLost(evt);
            }
        });

        jLabel24.setText("Telefone Responsável");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout
            .setHorizontalGroup(jPanel1Layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel1Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            jPanel1Layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTFNome)
                                .addGroup(
                                    jPanel1Layout
                                        .createSequentialGroup()
                                        .addGroup(
                                            jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(
                                                    jPanel1Layout.createSequentialGroup().addComponent(jLabel14)
                                                        .addGap(0, 0, Short.MAX_VALUE))
                                                .addGroup(
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addComponent(jTFBairro)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                        .addGroup(
                                            jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel15)
                                                .addComponent(jTFCep, javax.swing.GroupLayout.PREFERRED_SIZE, 79,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(
                                    javax.swing.GroupLayout.Alignment.TRAILING,
                                    jPanel1Layout
                                        .createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(
                                            jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel17)
                                                .addComponent(jTFCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 206,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                            jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel16)
                                                .addComponent(jCBUf, javax.swing.GroupLayout.PREFERRED_SIZE, 94,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jTFEndereco)
                                .addGroup(
                                    jPanel1Layout
                                        .createSequentialGroup()
                                        .addGroup(
                                            jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel3)
                                                .addGroup(
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addGroup(
                                                            jPanel1Layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel11)
                                                                .addComponent(jTFTelefone,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE, 102,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(
                                                            jPanel1Layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel12)
                                                                .addComponent(jTFCelular,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE, 104,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addComponent(jLabel13)
                                                .addGroup(
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addGroup(
                                                            jPanel1Layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel5)
                                                                .addComponent(jTFNascimento,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE, 80,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(
                                                            jPanel1Layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jCBSexo,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE, 90,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jLabel8))
                                                        .addGap(8, 8, 8)
                                                        .addGroup(
                                                            jPanel1Layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel9)
                                                                .addGroup(
                                                                    jPanel1Layout
                                                                        .createSequentialGroup()
                                                                        .addComponent(jTFPeso,
                                                                            javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                                                            javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(
                                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(jLabel25)))
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(
                                                            jPanel1Layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel10)
                                                                .addGroup(
                                                                    jPanel1Layout
                                                                        .createSequentialGroup()
                                                                        .addComponent(jTFAltura,
                                                                            javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                                                            javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(
                                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(jLabel26))))
                                                .addGroup(
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addGroup(
                                                            jPanel1Layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jTFCpf,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE, 103,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jLabel4))
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(
                                                            jPanel1Layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel18)
                                                                .addComponent(jTFRg,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE, 95,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 11,
                            javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(
                            jPanel1Layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(
                                    jPanel1Layout
                                        .createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addGroup(
                                            jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addGroup(
                                                            jPanel1Layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel20)
                                                                .addComponent(jLabel7)
                                                                .addComponent(jLabel23)
                                                                .addGroup(
                                                                    jPanel1Layout
                                                                        .createSequentialGroup()
                                                                        .addGroup(
                                                                            jPanel1Layout
                                                                                .createParallelGroup(
                                                                                    javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jLabel6)
                                                                                .addComponent(
                                                                                    jTFCpfResponsavel,
                                                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                    104,
                                                                                    javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addGap(18, 18, 18)
                                                                        .addGroup(
                                                                            jPanel1Layout
                                                                                .createParallelGroup(
                                                                                    javax.swing.GroupLayout.Alignment.LEADING,
                                                                                    false)
                                                                                .addComponent(
                                                                                    jLabel24,
                                                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                    Short.MAX_VALUE)
                                                                                .addComponent(jTFTelefoneResponsavel))))
                                                        .addGap(0, 0, Short.MAX_VALUE))
                                                .addGroup(
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addGroup(
                                                            jPanel1Layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel21)
                                                                .addComponent(jCBCor, 0, 132, Short.MAX_VALUE))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(
                                                            jPanel1Layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel22)
                                                                .addComponent(jCBEstadoCivil,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE, 157,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addComponent(jTFEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 307,
                                                    Short.MAX_VALUE)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 307,
                                                    Short.MAX_VALUE)
                                                .addComponent(jTFResponsavel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                    307, Short.MAX_VALUE)))
                                .addGroup(
                                    jPanel1Layout
                                        .createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                            jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel19)
                                                .addComponent(jTFProfissao, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    221, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE))).addContainerGap()));
        jPanel1Layout
            .setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel1Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            jPanel1Layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                                .addGroup(
                                    jPanel1Layout
                                        .createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTFNome, javax.swing.GroupLayout.PREFERRED_SIZE,
                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                            javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                            jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addComponent(jLabel5)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTFNascimento,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE,
                                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addGroup(
                                                            jPanel1Layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(jLabel8).addComponent(jLabel9))
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(
                                                            jPanel1Layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(jCBSexo,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jTFPeso,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jLabel25)))
                                                .addGroup(
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addComponent(jLabel10)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(
                                                            jPanel1Layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(jTFAltura,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jLabel26))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                            jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addComponent(jLabel18)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTFRg, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addComponent(jLabel4)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTFCpf, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                            jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addComponent(jLabel11)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(
                                                            jPanel1Layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(jTFTelefone,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jTFCelular,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(
                                                    jPanel1Layout.createSequentialGroup().addComponent(jLabel12)
                                                        .addGap(26, 26, 26)))
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTFEndereco, javax.swing.GroupLayout.PREFERRED_SIZE,
                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                            javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                            jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addComponent(jLabel14)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTFBairro,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE,
                                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addComponent(jLabel15)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTFCep, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                            jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel17).addComponent(jLabel16))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                            jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jCBUf, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jTFCidade, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(
                                    jPanel1Layout
                                        .createSequentialGroup()
                                        .addComponent(jLabel19)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTFProfissao, javax.swing.GroupLayout.PREFERRED_SIZE,
                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                            javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel20)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTFEmail, javax.swing.GroupLayout.PREFERRED_SIZE,
                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                            javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                            jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addComponent(jLabel21)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jCBCor, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabel7))
                                                .addGroup(
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addComponent(jLabel22)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jCBEstadoCivil,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE,
                                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTFResponsavel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                            javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                            jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addComponent(jLabel6)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTFCpfResponsavel,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE,
                                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addComponent(jLabel24)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTFTelefoneResponsavel,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE,
                                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel23)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 114,
                                            Short.MAX_VALUE))).addContainerGap()));

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        jBAtualizarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/atualizar.png"))); // NOI18N
        jBAtualizarRegistro.setText("Atualizar");
        jBAtualizarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBAtualizarRegistroKeyReleased(evt);
            }
        });

        jBApagarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/deletar.png"))); // NOI18N
        jBApagarRegistro.setText("Apagar");
        jBApagarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBApagarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBApagarRegistroActionPerformed(evt);
            }
        });
        jBApagarRegistro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBApagarRegistroKeyReleased(evt);
            }
        });

        jBSalvarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/salvar.png"))); // NOI18N
        jBSalvarRegistro.setText("Salvar");
        jBSalvarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBSalvarRegistroKeyReleased(evt);
            }
        });

        jBCancelar.setBackground(new java.awt.Color(113, 144, 224));
        jBCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemSetaParaEsquerda.png"))); // NOI18N
        jBCancelar.setText("Voltar");
        jBCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBCancelar.setPreferredSize(new java.awt.Dimension(89, 39));
        jBCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCancelarActionPerformed(evt);
            }
        });
        jBCancelar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBCancelarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)
            .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
            .addGroup(
                layout
                    .createSequentialGroup()
                    .addComponent(jBCancelar, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jBAtualizarRegistro)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBApagarRegistro)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBSalvarRegistro)
                    .addContainerGap()));
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
                    layout
                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jBAtualizarRegistro)
                        .addComponent(jBApagarRegistro)
                        .addComponent(jBSalvarRegistro)
                        .addComponent(jBCancelar, javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGap(50, 50, 50)));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 680) / 2, (screenSize.height - 583) / 2, 680, 583);
    }// </editor-fold>//GEN-END:initComponents

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar();
    }// GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBCancelarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoCancelar();
        }
    }// GEN-LAST:event_jBCancelarKeyReleased

    private void jBSalvarRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBSalvarRegistroActionPerformed
        botaoSalvarRegistro();
    }// GEN-LAST:event_jBSalvarRegistroActionPerformed

    private void jBSalvarRegistroKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBSalvarRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoSalvarRegistro();
        }
    }// GEN-LAST:event_jBSalvarRegistroKeyReleased

    private void jBAtualizarRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBAtualizarRegistroActionPerformed
        botaoAtualizarRegistro();
    }// GEN-LAST:event_jBAtualizarRegistroActionPerformed

    private void jBAtualizarRegistroKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBAtualizarRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoAtualizarRegistro();
        }
    }// GEN-LAST:event_jBAtualizarRegistroKeyReleased

    private void jBApagarRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBApagarRegistroActionPerformed
        botaoApagarRegistro();
    }// GEN-LAST:event_jBApagarRegistroActionPerformed

    private void jBApagarRegistroKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBApagarRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoApagarRegistro();
        }
    }// GEN-LAST:event_jBApagarRegistroKeyReleased

    private void jBSalvarRegistroFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBSalvarRegistroFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));
        jTFNascimento.setBackground(new java.awt.Color(255, 255, 255));
        jTFEmail.setBackground(new java.awt.Color(255, 255, 255));
        jTFResponsavel.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jBSalvarRegistroFocusLost

    private void jBAtualizarRegistroFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBAtualizarRegistroFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));
        jTFNascimento.setBackground(new java.awt.Color(255, 255, 255));
        jTFEmail.setBackground(new java.awt.Color(255, 255, 255));
        jTFResponsavel.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jBAtualizarRegistroFocusLost

    private void jTFCpfFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFCpfFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("###.###.###-##");
    }// GEN-LAST:event_jTFCpfFocusGained

    private void jTFCpfFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFCpfFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFCpfFocusLost

    private void jTFNascimentoFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFNascimentoFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("dd/mm/aaaa");
    }// GEN-LAST:event_jTFNascimentoFocusGained

    private void jTFNascimentoFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFNascimentoFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFNascimentoFocusLost

    private void jTFPesoFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFPesoFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("###");
    }// GEN-LAST:event_jTFPesoFocusGained

    private void jTFPesoFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFPesoFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFPesoFocusLost

    private void jTFAlturaFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFAlturaFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("###");
    }// GEN-LAST:event_jTFAlturaFocusGained

    private void jTFAlturaFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFAlturaFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFAlturaFocusLost

    private void jTFTelefoneFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFTelefoneFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("(##) ####-####");
    }// GEN-LAST:event_jTFTelefoneFocusGained

    private void jTFTelefoneFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFTelefoneFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFTelefoneFocusLost

    private void jTFCelularFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFCelularFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("(##) ####-####");
    }// GEN-LAST:event_jTFCelularFocusGained

    private void jTFCelularFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFCelularFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFCelularFocusLost

    private void jTFCepFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFCepFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("##.###-###");
    }// GEN-LAST:event_jTFCepFocusGained

    private void jTFCepFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFCepFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFCepFocusLost

    private void jTFRgFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFRgFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("##########");
    }// GEN-LAST:event_jTFRgFocusGained

    private void jTFRgFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFRgFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFRgFocusLost

    private void jTFEmailFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFEmailFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("xxx@xxx.xxx");
    }// GEN-LAST:event_jTFEmailFocusGained

    private void jTFEmailFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFEmailFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFEmailFocusLost

    private void jTFCpfResponsavelFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFCpfResponsavelFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("###.###.###-##");
    }// GEN-LAST:event_jTFCpfResponsavelFocusGained

    private void jTFCpfResponsavelFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFCpfResponsavelFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFCpfResponsavelFocusLost

    private void jTAObsKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTAObsKeyReleased
        jTAObs.setText(jTAObs.getText().toUpperCase());
    }// GEN-LAST:event_jTAObsKeyReleased

    private void jTFTelefoneResponsavelFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFTelefoneResponsavelFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("(##) ####-####");
    }// GEN-LAST:event_jTFTelefoneResponsavelFocusGained

    private void jTFTelefoneResponsavelFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFTelefoneResponsavelFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFTelefoneResponsavelFocusLost

    private void jTFResponsavelKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTFResponsavelKeyReleased
        if ("".equals(jTFResponsavel.getText())) {
            jTFCpfResponsavel.setEnabled(false);
            jTFCpfResponsavel.setText("");
            jTFTelefoneResponsavel.setEnabled(false);
            jTFTelefoneResponsavel.setText("");
        } else {
            jTFCpfResponsavel.setEnabled(true);
            jTFTelefoneResponsavel.setEnabled(true);
        }// TODO add your handling code here:
    }// GEN-LAST:event_jTFResponsavelKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton jBApagarRegistro;
    public static javax.swing.JButton jBAtualizarRegistro;
    public static javax.swing.JButton jBCancelar;
    public static javax.swing.JButton jBSalvarRegistro;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBCor;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBEstadoCivil;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBSexo;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBUf;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTAObs;
    private javax.swing.JTextField jTFAltura;
    private javax.swing.JTextField jTFBairro;
    private javax.swing.JTextField jTFCelular;
    private javax.swing.JTextField jTFCep;
    private javax.swing.JTextField jTFCidade;
    private javax.swing.JTextField jTFCpf;
    private javax.swing.JTextField jTFCpfResponsavel;
    private javax.swing.JTextField jTFEmail;
    private javax.swing.JTextField jTFEndereco;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    private javax.swing.JTextField jTFNascimento;
    private javax.swing.JTextField jTFNome;
    private javax.swing.JTextField jTFPeso;
    private javax.swing.JTextField jTFProfissao;
    private javax.swing.JTextField jTFResponsavel;
    private javax.swing.JTextField jTFRg;
    private javax.swing.JTextField jTFTelefone;
    private javax.swing.JTextField jTFTelefoneResponsavel;
    // End of variables declaration//GEN-END:variables
}
