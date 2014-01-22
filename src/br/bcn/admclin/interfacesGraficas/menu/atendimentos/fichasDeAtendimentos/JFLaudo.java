package br.bcn.admclin.interfacesGraficas.menu.atendimentos.fichasDeAtendimentos;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import br.bcn.admclin.dao.db.JLAUDOS;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.USUARIOS;

public class JFLaudo extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtPaciente;
    private JTextField txtNascimentoPaciente;
    private JTextField txtDataAtendimento;
    private JLabel lblDataAtendimento;
    private JLabel lblSexo;
    private JTextField txtHandle_at;
    private JLabel lblCdigo;
    private JTextField txtMod;
    private JTextField txtCrmMedico;
    private JLabel lblCrm;
    private JLabel lblModalidade;
    private JTextField txtMedico;
    private JLabel lblMdico;
    private JTextField txtSexoPaciente;
    private JTextArea txtLaudo;
    private JButton JBSalvar;
    private JButton JBAssinar;
    private JButton JBGerarPdf;
    private JButton jBGravarCodigo;

    /**
     * Create the frame.
     */
    public JFLaudo(String data, String nomePaciente, String handle_at, String medico, String crmMedico, String mod) {      
        //padrao do designer
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 738, 480);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblPaciente = new JLabel("Paciente:");
        lblPaciente.setBounds(119, 6, 59, 14);
        contentPane.add(lblPaciente);
        
        txtPaciente = new JTextField();
        txtPaciente.setBackground(new Color(240,223,212));
        txtPaciente.setEditable(false);
        txtPaciente.setBounds(119, 23, 418, 28);
        contentPane.add(txtPaciente);
        txtPaciente.setColumns(10);
        
        txtNascimentoPaciente = new JTextField();
        txtNascimentoPaciente.setHorizontalAlignment(SwingConstants.CENTER);
        txtNascimentoPaciente.setBackground(new Color(240,223,212));
        txtNascimentoPaciente.setEditable(false);
        txtNascimentoPaciente.setColumns(10);
        txtNascimentoPaciente.setBounds(549, 23, 121, 28);
        contentPane.add(txtNascimentoPaciente);
        
        JLabel lblNascimento = new JLabel("Nascimento:");
        lblNascimento.setBounds(549, 6, 78, 14);
        contentPane.add(lblNascimento);
        
        txtDataAtendimento = new JTextField();
        txtDataAtendimento.setHorizontalAlignment(SwingConstants.CENTER);
        txtDataAtendimento.setBackground(new Color(240,223,212));
        txtDataAtendimento.setEditable(false);
        txtDataAtendimento.setColumns(10);
        txtDataAtendimento.setBounds(6, 23, 101, 28);
        contentPane.add(txtDataAtendimento);
        
        lblDataAtendimento = new JLabel("Data:");
        lblDataAtendimento.setBounds(6, 6, 101, 14);
        contentPane.add(lblDataAtendimento);
        
        lblSexo = new JLabel("Sexo:");
        lblSexo.setBounds(682, 6, 44, 14);
        contentPane.add(lblSexo);
        
        txtHandle_at = new JTextField();
        txtHandle_at.setHorizontalAlignment(SwingConstants.CENTER);
        txtHandle_at.setBackground(new Color(240,223,212));
        txtHandle_at.setEditable(false);
        txtHandle_at.setColumns(10);
        txtHandle_at.setBounds(6, 80, 101, 28);
        contentPane.add(txtHandle_at);
        
        lblCdigo = new JLabel("Código:");
        lblCdigo.setBounds(6, 63, 101, 14);
        contentPane.add(lblCdigo);
        
        txtMod = new JTextField();
        txtMod.setHorizontalAlignment(SwingConstants.CENTER);
        txtMod.setBackground(new Color(240,223,212));
        txtMod.setEditable(false);
        txtMod.setColumns(10);
        txtMod.setBounds(682, 80, 44, 28);
        contentPane.add(txtMod);
        
        txtCrmMedico = new JTextField();
        txtCrmMedico.setHorizontalAlignment(SwingConstants.CENTER);
        txtCrmMedico.setBackground(new Color(240,223,212));
        txtCrmMedico.setEditable(false);
        txtCrmMedico.setColumns(10);
        txtCrmMedico.setBounds(549, 80, 121, 28);
        contentPane.add(txtCrmMedico);
        
        lblCrm = new JLabel("CRM:");
        lblCrm.setBounds(549, 63, 78, 14);
        contentPane.add(lblCrm);
        
        lblModalidade = new JLabel("Mod:");
        lblModalidade.setBounds(682, 63, 44, 14);
        contentPane.add(lblModalidade);
        
        txtMedico = new JTextField();
        txtMedico.setBackground(new Color(240,223,212));
        txtMedico.setEditable(false);
        txtMedico.setColumns(10);
        txtMedico.setBounds(119, 80, 418, 28);
        contentPane.add(txtMedico);
        
        lblMdico = new JLabel("Médico:");
        lblMdico.setBounds(119, 63, 59, 14);
        contentPane.add(lblMdico);
        
        txtSexoPaciente = new JTextField();
        txtSexoPaciente.setHorizontalAlignment(SwingConstants.CENTER);
        txtSexoPaciente.setBackground(new Color(240,223,212));
        txtSexoPaciente.setEditable(false);
        txtSexoPaciente.setColumns(10);
        txtSexoPaciente.setBounds(682, 23, 44, 28);
        contentPane.add(txtSexoPaciente);
        
        txtLaudo = new JTextArea();
        JScrollPane scroll = new JScrollPane(txtLaudo);
        scroll.setBounds(6, 120, 720, 270);
        txtLaudo.setColumns(20);
        txtLaudo.setLineWrap(true);
        txtLaudo.setRows(5);
        txtLaudo.setWrapStyleWord(true);
        txtLaudo.setDocument(new br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcentoMinusculas(6144));
        txtLaudo.setBounds(6, 120, 720, 270);
        contentPane.add(scroll);
        
        JBSalvar = new JButton("Salvar");
        JBSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
               botaoSalvar();
            }
        });
        JBSalvar.setIcon(new ImageIcon(JFLaudo.class.getResource("/br/bcn/admclin/imagens/salvar.png")));
        JBSalvar.setBounds(6, 402, 158, 42);
        contentPane.add(JBSalvar);
        
        JBAssinar = new JButton("Assinar");
        JBAssinar.setIcon(new ImageIcon(JFLaudo.class.getResource("/br/bcn/admclin/imagens/botaoAssinar.png")));
        JBAssinar.setBounds(549, 402, 177, 42);
        contentPane.add(JBAssinar);
        
        JBGerarPdf = new JButton("Gerar PDF");
        JBGerarPdf.setIcon(new ImageIcon(JFLaudo.class.getResource("/br/bcn/admclin/imagens/pdf.png")));
        JBGerarPdf.setBounds(360, 402, 177, 42);
        contentPane.add(JBGerarPdf);
        
        jBGravarCodigo = new JButton("Gravar Código");
        jBGravarCodigo.setIcon(new ImageIcon(JFLaudo.class.getResource("/br/bcn/admclin/imagens/botaoCadastrarCodigo.png")));
        jBGravarCodigo.setBounds(176, 402, 172, 42);
        contentPane.add(jBGravarCodigo);
        
        
        iniciarClasse();
        //definindo o campos
        txtDataAtendimento.setText(data);
        txtPaciente.setText(nomePaciente);
        txtHandle_at.setText(handle_at);
        txtMedico.setText(medico);
        txtCrmMedico.setText(crmMedico);
        txtMod.setText(mod);
        buscaDadosPaciente(Integer.valueOf(handle_at));
        preencheLaudo(Integer.valueOf(handle_at));
    }
    
    private void iniciarClasse(){
        setIconImage(getToolkit().createImage(getClass().getResource("/br/bcn/admclin/imagens/imagemIconePrograma.png")));

        this.setTitle("Laudo");
        
        //nao pode maximizar
        setResizable(false);
        
        //deixa por cima de todas as outras janelas
        setAlwaysOnTop(true);
        
        //centraliza na tela
        setLocationRelativeTo(null); 
        
        //focus no texto do laudo
        SwingUtilities.invokeLater(new Runnable() {   
            public void run() {   
                txtLaudo.requestFocus();   
            }   
        }); 
    }

    private void buscaDadosPaciente(int handle_at) {
        ResultSet resultSet = null;
        Connection con = Conexao.fazConexao();
        try {
            PreparedStatement stmtQuery =
                con.prepareStatement("SELECT PACIENTES.\"NASCIMENTO\" AS PACIENTES_NASCIMENTO, PACIENTES.\"SEXO\" AS PACIENTES_SEXO FROM \"PACIENTES\" PACIENTES INNER JOIN \"ATENDIMENTOS\" ATENDIMENTOS ON PACIENTES.\"PACIENTEID\" = ATENDIMENTOS.\"HANDLE_PACIENTE\" WHERE handle_at = ?");
            stmtQuery.setInt(1, handle_at);
            resultSet = stmtQuery.executeQuery();
            while(resultSet.next()){
                txtSexoPaciente.setText(resultSet.getString("sexo"));
                txtNascimentoPaciente.setText(resultSet.getString("nascimento"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar dados do Paciente. Procure o Administrador." + e, "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            
        }
    }

    private void preencheLaudo(int handle_at){
        String retorno = JLAUDOS.getConsultarLaudo(handle_at);
        if (retorno == "erro") {
            bloquear();
        } else if (retorno == "vazio") {

        } else {
            txtLaudo.setText(retorno.replaceAll("\\[\\]", "\n"));
        }
    }
    
    private void botaoSalvar() {
        int handle_at = Integer.valueOf(txtHandle_at.getText());
        String laudo = criaLaudo();
        String dataAtendimentoSplit[] = txtDataAtendimento.getText().split("/");
        String dataAtendimento = dataAtendimentoSplit[2] + dataAtendimentoSplit[1] + dataAtendimentoSplit[0];
        boolean retorno = JLAUDOS.setCadastrarLaudo(handle_at, laudo, dataAtendimento, USUARIOS.nomeUsuario);
        if(retorno){
            this.dispose();
        }
    }
    
    private void bloquear(){
        txtLaudo.setEnabled(false);
        JBSalvar.setEnabled(false);
        JBAssinar.setEnabled(false);
        JBGerarPdf.setEnabled(false);
        jBGravarCodigo.setEnabled(false);
    }
    
    private String criaLaudo() {
        String texto = "";
        if (txtLaudo.getText().length() > 0) {
            // ----------------------------------------------------------------------------------Create History
            String histoText = txtLaudo.getText();
            int totalLinesh = txtLaudo.getLineCount();
            texto = "";
            for (int i = 0; i < totalLinesh; i++) {
                try {
                    int start = txtLaudo.getLineStartOffset(i);
                    int end = txtLaudo.getLineEndOffset(i);
                    String line = histoText.substring(start, end);
                    String sline = line.replaceAll("\\n", "\\[\\]");
                    texto += sline;
                } catch (Exception ex) {
                }
            }
        }
        return texto;
    }
}
