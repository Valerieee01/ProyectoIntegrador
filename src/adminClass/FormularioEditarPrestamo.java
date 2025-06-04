package adminClass;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class FormularioEditarPrestamo extends JDialog {
	 private JTextField txtElemento, txtFechaInicio, txtFechaFin, txtEstado;
	    private JButton btnGuardar;
	    private int filaEditar;

	    public FormularioEditarPrestamo(JFrame parent, JTable tabla, int fila) {
	        super(parent, "Editar Préstamo", true);
	        this.filaEditar = fila;

	        setLayout(new GridLayout(5, 2, 10, 10));
	        setSize(400, 250);
	        setLocationRelativeTo(parent);

	        // Obtener valores actuales de la fila
	        String elemento = tabla.getValueAt(fila, 1).toString();
	        String fechaInicio = tabla.getValueAt(fila, 2).toString();
	        String fechaFin = tabla.getValueAt(fila, 3).toString();
	        String estado = tabla.getValueAt(fila, 4).toString();

	        add(new JLabel("Elemento:"));
	        txtElemento = new JTextField(elemento);
	        add(txtElemento);

	        add(new JLabel("Fecha Inicio (yyyy-mm-dd):"));
	        txtFechaInicio = new JTextField(fechaInicio);
	        add(txtFechaInicio);

	        add(new JLabel("Fecha Fin (yyyy-mm-dd):"));
	        txtFechaFin = new JTextField(fechaFin);
	        add(txtFechaFin);

	        add(new JLabel("Estado:"));
	        txtEstado = new JTextField(estado);
	        add(txtEstado);

	        btnGuardar = new JButton("Guardar");
	        btnGuardar.addActionListener(e -> {
	            if (validarCampos()) {
	                tabla.setValueAt(txtElemento.getText().trim(), filaEditar, 1);
	                tabla.setValueAt(txtFechaInicio.getText().trim(), filaEditar, 2);
	                tabla.setValueAt(txtFechaFin.getText().trim(), filaEditar, 3);
	                tabla.setValueAt(txtEstado.getText().trim(), filaEditar, 4);
	                dispose();
	            }
	        });

	        add(new JLabel()); // Espacio
	        add(btnGuardar);
	    }

	    private boolean validarCampos() {
	        String elemento = txtElemento.getText().trim();
	        String fechaInicio = txtFechaInicio.getText().trim();
	        String fechaFin = txtFechaFin.getText().trim();
	        String estado = txtEstado.getText().trim();

	        if (elemento.isEmpty() || fechaInicio.isEmpty() || fechaFin.isEmpty() || estado.isEmpty()) {
	            JOptionPane.showMessageDialog(this, "Todos los campos deben estar completos.", "Error", JOptionPane.ERROR_MESSAGE);
	            return false;
	        }

	        if (!fechaInicio.matches("\\d{4}-\\d{2}-\\d{2}") || !fechaFin.matches("\\d{4}-\\d{2}-\\d{2}")) {
	            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Usa yyyy-mm-dd.", "Error", JOptionPane.ERROR_MESSAGE);
	            return false;
	        }

	        return true;
	    }
}

