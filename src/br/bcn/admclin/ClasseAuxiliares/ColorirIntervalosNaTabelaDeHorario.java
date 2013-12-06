package br.bcn.admclin.ClasseAuxiliares;


import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class ColorirIntervalosNaTabelaDeHorario implements TableCellRenderer {

  public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

    @Override
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(
        table, value, isSelected, hasFocus, row, column);
    ((JLabel) renderer).setOpaque(true);
    Color foreground, background;
    if (isSelected) {
      foreground = Color.BLACK;
      background = new java.awt.Color(113,144,224);
    } else {
      if (row % 2 == 0) {
          
          String flagDePintura = (String) table.getValueAt(row, 1);
          
          if("2".equals(flagDePintura)){
              foreground = Color.BLACK;
              background = Color.BLACK;
          }else{
              foreground = Color.BLACK;
              background = new java.awt.Color(230,230,230);
          }
          
        
        
      } else {
          
        String flagDePintura = (String) table.getValueAt(row, 1);
          
          if("2".equals(flagDePintura)){
              foreground = Color.BLACK;
              background = Color.BLACK;
          }else{
              foreground = Color.BLACK;
              background = Color.WHITE;
          }
        
      }
    }
    renderer.setForeground(foreground);
    renderer.setBackground(background);
    return renderer;
  }
}