package br.bcn.admclin.dao.dbris;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JOptionPane;

import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

/**
 * Classe responsavel por conexões com Banco de Dados e fechamento de conexão.
 * 
 * @author Cesar Schutz
 */
public class Conexao {

    public static Connection con = null;

    /**
     * Faz conexao com o banco de dados FireBird do RIS
     * 
     * @return Connection
     */
    @SuppressWarnings("finally")
    public static Connection fazConexao() {

        try {

            // carrega o drive do banco de dados
            Class.forName("org.firebirdsql.jdbc.FBDriver");

            // setando propriedades para que pegue as informaçoes do banco com ascento e caracteres especais e etc
            Properties props = new Properties();
            props.put("user", "SYSDBA");
            props.put("password", "masterkey");
            props.put("charset", "UTF8");
            props.put("lc_ctype", "ISO8859_1");

            con =
                DriverManager.getConnection("jdbc:firebirdsql:" + janelaPrincipal.RISIP + "/3050:"
                    + janelaPrincipal.RISDB, props);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal,
                "Erro no drive de conexão. Procure o administrador", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal,
                "Erro na conexao! Procure o administrador", "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            return con;
        }
    }

    /**
     * Faz conexao com o banco de dados FireBird com o banco pac
     * 
     * @return Connection
     */
    @SuppressWarnings("finally")
    public static Connection fazConexaoPAC() {

        try {

            // carrega o drive do banco de dados
            Class.forName("org.firebirdsql.jdbc.FBDriver");

            // setando propriedades para que pegue as informaçoes do banco com ascento e caracteres especais e etc
            Properties props = new Properties();
            props.put("user", "SYSDBA");
            props.put("password", "masterkey");
            props.put("charset", "UTF8");
            props.put("lc_ctype", "ISO8859_1");

            con =
                DriverManager.getConnection("jdbc:firebirdsql:" + janelaPrincipal.RISIP + "/3050:"
                    + janelaPrincipal.PACDB, props);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal,
                "Erro no drive de conexão. Procure o administrador", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal,
                "Erro na conexao! Procure o administrador", "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            return con;
        }
    }
    
    /**
     * Metodo para fechar uma conexão copm o banco de dados.
     * 
     * @param Connection
     */
    public static void fechaConexao(Connection conn) {
        try {
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal,
                "Erro ao fechar conexao. Procure o administrador", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

}
