package br.bcn.admclin.interfacesGraficas.menu.atendimentos.fichasDeAtendimentos;

import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import br.bcn.admclin.dao.JHISTORIA;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JFHistoriaClinica extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    int handle_at;
    JTextArea textArea;

    /**
     * Create the frame.
     */
    public JFHistoriaClinica(int handle_at, String dataAtendimento, String nomePaciente) {
        this.handle_at = handle_at;
        this.setTitle("História Clínica   -   " + "Código: " + handle_at + " - Data: " + dataAtendimento + " - Paciente: " + nomePaciente);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 1024, 233);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        
        textArea = new JTextArea();
        
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                botaoAtualizar();
            }
        });
        btnAtualizar.setIcon(new ImageIcon(JFHistoriaClinica.class.getResource("/br/bcn/admclin/imagens/atualizar.png")));
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
            gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                    .addComponent(btnAtualizar, GroupLayout.DEFAULT_SIZE, 998, Short.MAX_VALUE)
                    .addContainerGap())
                .addComponent(textArea, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 998, Short.MAX_VALUE)
        );
        gl_contentPane.setVerticalGroup(
            gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
                    .addComponent(textArea, GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(btnAtualizar))
        );
        contentPane.setLayout(gl_contentPane);
        
        iniciarClasse();
    }
    
    private void iniciarClasse(){
        setIconImage(getToolkit().createImage(getClass().getResource("/br/bcn/admclin/imagens/imagemIconePrograma.png")));
        setAlwaysOnTop(true);
        //arrumando a localização
        Toolkit tk = Toolkit.getDefaultToolkit();
        java.awt.Dimension screenSize = tk.getScreenSize();
        this.setLocation(screenSize.width / 2 - 512, 0);
        
        this.addWindowListener(new WindowAdapter(){  
            public void windowClosing(WindowEvent  e) {  
                 fechar();
            }  
        });
        
        consultarHistoria();
    }
    
    private void fechar(){
        this.dispose();
    }
    
    private void botaoAtualizar(){
        
    }
    
    private void consultarHistoria(){
        String retorno = JHISTORIA.getConsultar(handle_at);
        System.out.println(retorno);
        if(retorno == "erro"){
            System.out.println("caiu aqui");
            fechar();
        }else if(retorno == "vazio"){
            
        } else{
            textArea.setText(retorno);
        }
    }
}
