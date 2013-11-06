package ClasseAuxiliares;


import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class ColunaAceitandoIcone implements TableCellRenderer {

  public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
  

    @Override
  public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
        //pintando as linhas marcadas com flag de pintura!!
        Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        ((JLabel) renderer).setOpaque(true);
        
        
        //deixando coluna aceitando icones
        if (value instanceof Icon) {  
            DEFAULT_RENDERER.setIcon((Icon)value);  
            DEFAULT_RENDERER.setText("");  
        } else {  
            DEFAULT_RENDERER.setIcon(null);  
        }
        
        //centralizando icones
        DEFAULT_RENDERER.setHorizontalAlignment(DEFAULT_RENDERER.CENTER);
        


        return renderer;
  }
}