
package br.bcn.admclin.dao;

import ClasseAuxiliares.MetodosUteis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import br.bcn.admclin.model.ValoresMateriais;

/**
 * Classe DAO da tabela ValoresMateriais
 * @author BCN
 */
public class VALORESMATERIAIS {
    
    
    /**
     * consulta se data digitada eh maior que a ultima data cadastrada naquele Material
     * @param Connection
     * @return ResultSet
     */
    public static boolean getConsultarSeDataEhMenorQueAultimaCadastrada(Connection con, int handle_material, String dataAcadastrar, JTextField mensagemParaOUsuario){
        boolean ok = false;
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select * from valoresmateriais where handle_material=? order by dataavaler");
        stmtQuery.setInt(1, handle_material);
        resultSet = stmtQuery.executeQuery();
        while(resultSet.next()){
            //verificando data
            String dataBanco = MetodosUteis.converterDataParaMostrarAoUsuario(resultSet.getString("dataavaler"));
            DateFormat  format = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date dataDigitada = format.parse( dataAcadastrar  );
            java.util.Date dataBancoCorreta = format.parse( dataBanco  );
            
            int x = dataDigitada.compareTo(dataBancoCorreta);
            
            if(x>0){
                ok = true;
            }else{
                ok = false;
            }
        }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao verificar Datas para Cadastrar Novo Valor. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
            ok = false;
        }finally{
            if(!ok){
                mensagemParaOUsuario.setForeground(new java.awt.Color(255, 0, 0));
                mensagemParaOUsuario.setText("Data menor que a última cadastrada");
            }
            return ok;
        }
    }
    /**
     * Consulta Todos os valores de Materiais existentes no Banco de Dados.
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultar(Connection con, int handle_material){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select * from valoresmateriais where handle_material=? order by dataavaler Desc");
        stmtQuery.setInt(1, handle_material);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Valores do Material. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    /**
     * Cadastra um novo valor de material de acordo com o seu ID.
     * @param con
     * @param valorMaterialModel
     * @param handle_material
     * @return  se cadastrou ou nao
     */
    public static boolean setCadastrar(Connection con, ValoresMateriais valorMaterialModel, int handle_material){
        boolean cadastro = false;
        String sqlInsrtValor = "insert into valoresmateriais (handle_material,valor,dataavaler,dat,usuarioid) values(?,?,?,?,?)";
        try{
            //inserindo valor no material
            PreparedStatement stmtInsertValor = con.prepareStatement(sqlInsrtValor);
            stmtInsertValor.setInt(1, handle_material);
            stmtInsertValor.setDouble(2, Double.valueOf(valorMaterialModel.getValor()));
            stmtInsertValor.setDate(3, valorMaterialModel.getDataAValer());
            stmtInsertValor.setDate(4, valorMaterialModel.getData());
            stmtInsertValor.setInt(5, USUARIOS.usrId);
            stmtInsertValor.executeUpdate();
            stmtInsertValor.close();           
            cadastro = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Valor de Material. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    /**
     * Deleta uma valores de material quando deletado um material no Banco de Dados.
     * @param Connection
     * @param materiaisMODEL 
     * @return Boolean
     */
    public static boolean setDeletar(Connection con, int handle_material){
        boolean deleto = false;
        String sql= "delete from valoresmateriais where handle_material=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, handle_material);
            stmt.executeUpdate();
            stmt.close();    
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Valor de Material. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
    
    /**
     * Deleta um Valor de um material do Banco De Dados
     * @param Connection
     * @param valorMaterialId 
     * @return boolean
     */
    public static boolean setDeletarUmValor(Connection con, int valorMaterialId){
        boolean deleto = false; 
        String sql="delete from valoresMateriais where valorMaterialId=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, valorMaterialId);
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Valor de Material. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
}
