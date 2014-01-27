package br.bcn.admclin.dao.dbris;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.ClasseAuxiliares.OSvalidator;
import br.bcn.admclin.dao.model.Usuario;

/**
 * Classe DAO para tabela USUARIOS
 * @author BCN
 */
public class USUARIOS {
    public static String statusUsuario = null;
    public static String nomeUsuario = null;
    public static String senhaAssinaturaDeLaudo = "";
    public static boolean senhaAssinaturaConferida = false;
    public static String impressora_ficha = null;
    public static String impressora_nota_fiscal = null;
    public static String impressora_etiqueta_envelope = null;
    public static String impressora_codigo_de_barras = null;
    public static String pasta_raiz = "-";
    public static int usrId;
    /**
     * Consulta todos os usuários exitentes no Banco De Dados.
     * @param Connection
     * @return ResultSet
     * @throws SQLException 
     */
    public static ResultSet getConsultar(Connection con) throws SQLException{
        ResultSet resultSet = null;
        PreparedStatement stmtQuery = con.prepareStatement("select * from USUARIOS order by NM_USUARIO");
        resultSet = stmtQuery.executeQuery();
        return resultSet;        
    }
    /**
     * Verifica se Usuário já existe antes de cadastra-lo no Banco de Dados.
     * @param Connection
     * @param UsuarioMODEL
     * @return boolean
     * @throws SQLException 
     */
    public static boolean getConsultarParaSalvarNovoRegistro(Connection con, Usuario usuario) throws SQLException{
        boolean existe = true;
        PreparedStatement stmtQuery = con.prepareStatement("select * from USUARIOS where NM_USUARIO=? and PW_SENHA=?");
        stmtQuery.setString(1, usuario.getUsuario());
        stmtQuery.setString(2, usuario.getSenha());
        ResultSet resultSet = stmtQuery.executeQuery();
        if(!resultSet.next()){
            existe = false;
        }
        return existe;     
    }
    /**
     * Verifica se usuário já existe antes de atualiza-lo no Banco De Dados.
     * @param Connection
     * @param UsuarioModel
     * @return boolean
     * @throws SQLException 
     */
    public static boolean getConsultarParaAtualizarRegistro(Connection con, Usuario usuario) throws SQLException{
        boolean existe = true;
        PreparedStatement stmtQuery = con.prepareStatement("select * from USUARIOS where (NM_USUARIO=? and PW_SENHA=?) and (USRID!=?)");
        stmtQuery.setString(1, usuario.getUsuario());
        stmtQuery.setString(2, usuario.getSenha());
        stmtQuery.setInt(3, usuario.getUsrid());
        ResultSet resultSet = stmtQuery.executeQuery();
        if(!resultSet.next()){
            existe = false;
        }
        return existe; 
    }
    /**
     * Salva um novo Usuário no Banco De Dados.
     * @param Connection
     * @param UsuarioModel 
     * @return  boolean
     * @throws SQLException 
     */
    public static boolean setCadastrar(Connection con, Usuario usuario) throws SQLException{
        String sql = "insert into USUARIOS (DS_UNIDADE,NM_USUARIO,PW_SENHA,FG_ESTATUS,EMAIL,ENVIA_EMAILS, usuarioid, dat, IMPRESSORA_FICHA, IMPRESSORA_NOTA_FISCAL, IMPRESSORA_ETIQUETA_ENVELOPE, PASTA_RAIZ, IMPRESSORA_CODIGO_DE_BARRAS) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, usuario.getDescricao());
        stmt.setString(2, usuario.getUsuario());
        stmt.setString(3, usuario.getSenha());
        stmt.setString(4, usuario.getEstatus());
        stmt.setString(5, usuario.getEmail());
        stmt.setString(6, usuario.getEnvia_email());
        stmt.setInt(7, usuario.getUsuarioId());
        stmt.setDate(8, usuario.getDat());
        stmt.setString(9, usuario.getImpressora_ficha());
        stmt.setString(10, usuario.getImpressora_nota_fiscal());
        stmt.setString(11, usuario.getImpressora_etiqueta_envelope());
        stmt.setString(12, usuario.getPasta_raiz());
        stmt.setString(13, usuario.getImpressora_codigo_de_barras());
        stmt.executeUpdate();
        stmt.close();
        return true;
    }
    /**
     * Atualiza um contato no Banco De Dados.
     * @param Connection
     * @param UsuarioModel 
     * @return boolean
     * @throws SQLException 
     */
    public static boolean setUpdate(Connection con, Usuario usuario) throws SQLException{
        boolean atualizo = false;
        String sql = "update USUARIOS set DS_UNIDADE=?, NM_USUARIO=?, PW_SENHA=?, FG_ESTATUS=?, EMAIL=?, ENVIA_EMAILS=?, USUARIOID=?, dat=?, IMPRESSORA_FICHA=?, IMPRESSORA_NOTA_FISCAL=?, IMPRESSORA_ETIQUETA_ENVELOPE=?, PASTA_RAIZ=?, IMPRESSORA_CODIGO_DE_BARRAS=? where USRID=?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, usuario.getDescricao());
        stmt.setString(2, usuario.getUsuario());
        stmt.setString(3, usuario.getSenha());
        stmt.setString(4, usuario.getEstatus());
        stmt.setString(5, usuario.getEmail());
        stmt.setString(6, usuario.getEnvia_email());
        stmt.setInt(7, usuario.getUsuarioId());
        stmt.setDate(8, usuario.getDat());
        stmt.setString(9, usuario.getImpressora_ficha());
        stmt.setString(10, usuario.getImpressora_nota_fiscal());
        stmt.setString(11, usuario.getImpressora_etiqueta_envelope());
        stmt.setString(12, usuario.getPasta_raiz());
        stmt.setString(13, usuario.getImpressora_codigo_de_barras());
        stmt.setInt(14, usuario.getUsrid());
        stmt.executeUpdate();
        stmt.close();
        atualizo = true;
        return atualizo;
    }
    //atualiza a senha de assinatura de laudos
    public static boolean setUpdateAssinaturaLaudo(String novaSenha){
    	try {
    		Connection con = Conexao.fazConexao();
        	String sql = "update USUARIOS set senha_assinatura_laudo=? where USRID=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, novaSenha);
            stmt.setInt(2, usrId);
            stmt.executeUpdate();
            stmt.close();
            return true;
		} catch (Exception e) {
			return false;
		}
    	
        
    }
    
    /**
     * Deleta um Usuário do Banco de Dados.
     * @param Connection
     * @param UsuarioModel 
     * @throws SQLException 
     */
    public static boolean setDeletar(Connection con, Usuario usuario) throws SQLException{
        boolean deleto = false;
        String sql="delete from USUARIOS where USRID=?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, usuario.getUsrid());
        stmt.executeUpdate();
        stmt.close();    
        deleto = true;
        return deleto;
    }
    /**
     * Verifica se Usuário está cadastrado para entrar no sistema.
     * @param Connection
     * @param UsuarioMODEL
     * @return boolean
     * @throws SQLException 
     */
    public static boolean getLogin(Connection con, Usuario usuario) throws SQLException{
        PreparedStatement stmtQuery = con.prepareStatement("select * from USUARIOS where NM_USUARIO=? and PW_SENHA=?");
        stmtQuery.setString(1, usuario.getUsuario());
        stmtQuery.setString(2, usuario.getSenha());
        ResultSet resultSet = stmtQuery.executeQuery();
        if(resultSet.next()){
            statusUsuario = resultSet.getString("FG_ESTATUS");
            nomeUsuario = resultSet.getString("NM_USUARIO");
            senhaAssinaturaDeLaudo = resultSet.getString("senha_assinatura_laudo");
            if(senhaAssinaturaDeLaudo == null) senhaAssinaturaDeLaudo = "";
            usrId = resultSet.getInt("USRID");
            impressora_ficha = resultSet.getString("IMPRESSORA_FICHA");
            impressora_nota_fiscal = resultSet.getString("IMPRESSORA_NOTA_FISCAL");
            impressora_etiqueta_envelope = resultSet.getString("IMPRESSORA_ETIQUETA_ENVELOPE");
            impressora_codigo_de_barras = resultSet.getString("impressora_codigo_de_barras");
            pasta_raiz = verificarPastaRaiz(resultSet.getString("PASTA_RAIZ")) + nomeUsuario;
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * esse metodo serve para colocar a barra no final caso nao tenha. 
     * Isso por que aonde é utilizada essa pasta, ela considera que a pasta ja contem as barras no final.
     * */
    private static String verificarPastaRaiz(String pastaRaiz){
        if (pastaRaiz.equals("") || pastaRaiz == null) {
            JOptionPane.showMessageDialog(null, "Este usuário não tem pasta raiz definida. Defina uma pasta para este usuário no cadastro de usuários.", "Erro", JOptionPane.ERROR_MESSAGE);
        }else{
            //aqui trataremos a barra (caso nao tenha barra no fim da String)
            int tamanho = pastaRaiz.length(); 
            char ultimaLetra = pastaRaiz.charAt(tamanho-1);
            
            //pegamos a ultima letra, agora vamos verificar se é \ ou /. se nao for teremos que colocar de acordo com o sistema operacional
            
            if (ultimaLetra != '\\' || ultimaLetra != '/') {
                //aqui vamos colocar a barra pois nao tem
                if (OSvalidator.isWindows()) {
                    return  pastaRaiz + "\\";
                }else{
                    return  pastaRaiz + "/";
                }
            }else{
                return pastaRaiz;
            }
            
        }
        return "";
    }
    
}
