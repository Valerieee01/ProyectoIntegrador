package userClass;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import user.paneHistorialPrestamos; // Asegúrate de importar tu panel

public class BotonEditor extends DefaultCellEditor {
    private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
    private final JButton btnEditar = new JButton("✏️");
    private final JButton btnEliminar = new JButton("🗑️");

    private final paneHistorialPrestamos panelPadre; // referencia al panel padre

    public BotonEditor(JCheckBox checkBox, paneHistorialPrestamos panelPadre) {
        super(checkBox);
        this.panelPadre = panelPadre; // guardar la referencia

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

        panel.add(btnEditar);
        panel.add(btnEliminar);

        btnEditar.addActionListener(e -> {
            JTable table = (JTable) SwingUtilities.getAncestorOfClass(JTable.class, (Component) e.getSource());
            if (table != null) {
                int row = table.getEditingRow();
                Long idPrestamo = Long.parseLong(table.getValueAt(row, 0).toString());
                panelPadre.editarPrestamo(idPrestamo, row); // llamar método del panel
            }
            fireEditingStopped();
        });

        btnEliminar.addActionListener(e -> {
            JTable table = (JTable) SwingUtilities.getAncestorOfClass(JTable.class, (Component) e.getSource());
            if (table != null) {
                int row = table.getEditingRow();
                Long idPrestamo = Long.parseLong(table.getValueAt(row, 0).toString());
                panelPadre.eliminarPrestamo(idPrestamo); // llamar método del panel
            }
            fireEditingStopped();
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }

}