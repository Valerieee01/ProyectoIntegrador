package userClass;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class BotonRenderer extends JPanel implements TableCellRenderer {
    private final JButton btnEditar = new JButton("✏️");
    private final JButton btnEliminar = new JButton("🗑️");

    public BotonRenderer() {
    	
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        btnEditar.setFocusable(false);
        btnEliminar.setFocusable(false);
        btnEditar.setMargin(new Insets(2, 5, 2, 5));
        btnEliminar.setMargin(new Insets(2, 5, 2, 5));
        btnEditar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        btnEliminar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        btnEditar.setPreferredSize(new Dimension(50, 50));
        btnEliminar.setPreferredSize(new Dimension(50, 50));
        btnEditar.setBackground(new Color(255, 255, 198)); 
        btnEliminar.setBackground(new Color(255, 255, 198));


        add(btnEditar);
        add(btnEliminar);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }

}

