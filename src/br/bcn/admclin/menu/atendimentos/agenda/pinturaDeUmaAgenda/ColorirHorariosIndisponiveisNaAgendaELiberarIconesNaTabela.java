package br.bcn.admclin.menu.atendimentos.agenda.pinturaDeUmaAgenda;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class ColorirHorariosIndisponiveisNaAgendaELiberarIconesNaTabela implements TableCellRenderer {

    public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {

        // pintando as linhas marcadas com flag de pintura!!
        Component renderer =
            DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        ((JLabel) renderer).setOpaque(true);
        Color foreground, background;

        // System.out.println("linha        : " + row);
        // System.out.println("flag de pintu: " + table.getValueAt(row, 3));

        if (isSelected) {
            foreground = Color.BLACK;
            background = new java.awt.Color(113, 144, 224);
        } else {
            if (row % 2 == 0) {

                // se for flag 1, pinta de cinza
                // se for flag 2 pinta de preto (troca de turno)

                String flagDePintura = (String) table.getValueAt(row, 3);

                if (("".equals(flagDePintura) || "null".equals(flagDePintura) || flagDePintura == null)
                    && (!"1".equals(flagDePintura) && !"2".equals(flagDePintura))) {
                    foreground = Color.BLACK;
                    background = new java.awt.Color(230, 230, 230);
                } else if ("1".equals(flagDePintura)) {
                    foreground = Color.BLACK;
                    background = new java.awt.Color(180, 180, 180);
                } else if ("2".equals(flagDePintura)) {
                    foreground = Color.BLACK;
                    background = Color.BLACK;
                } else if ("3".equals(flagDePintura)) {
                    foreground = Color.BLACK;
                    background = new java.awt.Color(102, 236, 249);
                } else if ("4".equals(flagDePintura)) {
                    foreground = Color.BLACK;
                    background = new java.awt.Color(67, 201, 151);
                } else {
                    foreground = Color.BLACK;
                    background = new java.awt.Color(230, 230, 230);
                }

            } else {

                String flagDePintura = (String) table.getValueAt(row, 3);

                if ("".equals(flagDePintura) || "null".equals(flagDePintura) || flagDePintura == null) {
                    foreground = Color.BLACK;
                    background = Color.WHITE;
                } else if ("1".equals(flagDePintura)) {
                    foreground = Color.BLACK;
                    background = new java.awt.Color(190, 190, 190);
                } else if ("2".equals(flagDePintura)) {
                    foreground = Color.BLACK;
                    background = Color.BLACK;
                } else if ("3".equals(flagDePintura)) {
                    foreground = Color.BLACK;
                    background = new java.awt.Color(161, 243, 251);
                } else if ("4".equals(flagDePintura)) {
                    foreground = Color.BLACK;
                    background = new java.awt.Color(130, 219, 186);
                } else {
                    foreground = Color.BLACK;
                    background = Color.WHITE;
                }

            }
        }
        renderer.setForeground(foreground);
        renderer.setBackground(background);

        // deixando coluna aceitando icones
        if (value instanceof Icon) {
            DEFAULT_RENDERER.setIcon((Icon) value);
            DEFAULT_RENDERER.setText("");
        } else {
            DEFAULT_RENDERER.setIcon(null);
        }

        // centralizando icones
        DEFAULT_RENDERER.setHorizontalAlignment(SwingConstants.CENTER);

        return renderer;
    }
}