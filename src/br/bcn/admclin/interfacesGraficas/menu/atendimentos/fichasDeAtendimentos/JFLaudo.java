package br.bcn.admclin.interfacesGraficas.menu.atendimentos.fichasDeAtendimentos;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;

public class JFLaudo extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtCesarAugustoSchutz;
    private JTextField textField;
    private JTextField textField_1;
    private JLabel lblDataAtendimento;
    private JLabel lblSexo;
    private JTextField textField_2;
    private JLabel lblCdigo;
    private JTextField txtCr;
    private JTextField textField_4;
    private JLabel lblCrm;
    private JLabel lblModalidade;
    private JTextField txtTheodoroMennaBarretos;
    private JLabel lblMdico;
    private JTextField textField_3;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JFLaudo frame = new JFLaudo();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public JFLaudo() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 696, 480);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblPaciente = new JLabel("Paciente:");
        lblPaciente.setBounds(119, 6, 59, 14);
        contentPane.add(lblPaciente);
        
        txtCesarAugustoSchutz = new JTextField();
        txtCesarAugustoSchutz.setText("CESAR AUGUSTO SCHUTZ FAGUNDES");
        txtCesarAugustoSchutz.setBounds(119, 23, 418, 28);
        contentPane.add(txtCesarAugustoSchutz);
        txtCesarAugustoSchutz.setColumns(10);
        
        textField = new JTextField();
        textField.setText("15/02/1959");
        textField.setColumns(10);
        textField.setBounds(549, 23, 78, 28);
        contentPane.add(textField);
        
        JLabel lblNascimento = new JLabel("Nascimento:");
        lblNascimento.setBounds(549, 6, 78, 14);
        contentPane.add(lblNascimento);
        
        textField_1 = new JTextField();
        textField_1.setText("15/02/1959");
        textField_1.setColumns(10);
        textField_1.setBounds(6, 23, 101, 28);
        contentPane.add(textField_1);
        
        lblDataAtendimento = new JLabel("Data Atendimento:");
        lblDataAtendimento.setBounds(6, 6, 101, 14);
        contentPane.add(lblDataAtendimento);
        
        lblSexo = new JLabel("Sexo:");
        lblSexo.setBounds(639, 6, 44, 14);
        contentPane.add(lblSexo);
        
        textField_2 = new JTextField();
        textField_2.setText("15/02/1959");
        textField_2.setColumns(10);
        textField_2.setBounds(6, 80, 101, 28);
        contentPane.add(textField_2);
        
        lblCdigo = new JLabel("Código:");
        lblCdigo.setBounds(6, 63, 101, 14);
        contentPane.add(lblCdigo);
        
        txtCr = new JTextField();
        txtCr.setText("MR");
        txtCr.setColumns(10);
        txtCr.setBounds(639, 80, 44, 28);
        contentPane.add(txtCr);
        
        textField_4 = new JTextField();
        textField_4.setText("123456789");
        textField_4.setColumns(10);
        textField_4.setBounds(549, 80, 78, 28);
        contentPane.add(textField_4);
        
        lblCrm = new JLabel("CRM:");
        lblCrm.setBounds(549, 63, 78, 14);
        contentPane.add(lblCrm);
        
        lblModalidade = new JLabel("Mod:");
        lblModalidade.setBounds(639, 63, 44, 14);
        contentPane.add(lblModalidade);
        
        txtTheodoroMennaBarretos = new JTextField();
        txtTheodoroMennaBarretos.setText("THEODORO MENNA BARRETOS DUARTE");
        txtTheodoroMennaBarretos.setColumns(10);
        txtTheodoroMennaBarretos.setBounds(119, 80, 418, 28);
        contentPane.add(txtTheodoroMennaBarretos);
        
        lblMdico = new JLabel("Médico:");
        lblMdico.setBounds(119, 63, 59, 14);
        contentPane.add(lblMdico);
        
        textField_3 = new JTextField();
        textField_3.setText("MR");
        textField_3.setColumns(10);
        textField_3.setBounds(639, 23, 44, 28);
        contentPane.add(textField_3);
        
        JTextArea txtrTesteBlaBla = new JTextArea();
        txtrTesteBlaBla.setText("AQUI VAI O LAUDO\r\nNADA CONFIRMADO\r\nDOMENTE A IDEIA INICIAL\r\n");
        txtrTesteBlaBla.setBounds(6, 120, 677, 270);
        contentPane.add(txtrTesteBlaBla);
        
        JButton button_3 = new JButton("Salvar");
        button_3.setIcon(new ImageIcon(JFLaudo.class.getResource("/br/bcn/admclin/imagens/salvar.png")));
        button_3.setBounds(6, 402, 158, 42);
        contentPane.add(button_3);
        
        JButton btnAssinar = new JButton("Assinar");
        btnAssinar.setIcon(new ImageIcon(JFLaudo.class.getResource("/br/bcn/admclin/imagens/botaoAssinar.png")));
        btnAssinar.setBounds(516, 402, 167, 42);
        contentPane.add(btnAssinar);
        
        JButton btnGerarPdf = new JButton("Gerar PDF");
        btnGerarPdf.setIcon(new ImageIcon(JFLaudo.class.getResource("/br/bcn/admclin/imagens/pdf.png")));
        btnGerarPdf.setBounds(346, 402, 158, 42);
        contentPane.add(btnGerarPdf);
        
        JButton btnGravarCdigo = new JButton("Gravar Código");
        btnGravarCdigo.setIcon(new ImageIcon(JFLaudo.class.getResource("/br/bcn/admclin/imagens/botaoCadastrarCodigo.png")));
        btnGravarCdigo.setBounds(176, 402, 158, 42);
        contentPane.add(btnGravarCdigo);
        
        
        iniciarClasse();
    }
    
    private void iniciarClasse(){
        setIconImage(getToolkit().createImage(getClass().getResource("/br/bcn/admclin/imagens/imagemIconePrograma.png")));

        this.setTitle("Laudo");
        
        setResizable(false);
    }
}
