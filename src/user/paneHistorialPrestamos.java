package user;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.CardLayout;

import userClass.BotonEditor;
import userClass.BotonRenderer;

public class paneHistorialPrestamos extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public paneHistorialPrestamos() {
		setLayout(new BorderLayout());
	    setBackground(new Color(255, 255, 198));


	    String[] columnas = {"ID", "Elemento", "Fecha Inicio", "Fecha Fin", "Estado", "Acciones"};
	    Object[][] datos = {
	        {1, "Libro: Java Básico", "2025-05-01", "2025-06-01", "Activo", null},
	        {2, "Tablet Samsung", "2025-04-15", "2025-05-15", "Finalizado", null},
	        {3, "Proyector Epson", "2025-05-20", "2025-06-10", "Activo", null},
	        {1, "Libro: Java Básico", "2025-05-01", "2025-06-01", "Activo", null},
	        {2, "Tablet Samsung", "2025-04-15", "2025-05-15", "Finalizado", null},
	        {1, "Libro: Java Básico", "2025-05-01", "2025-06-01", "Activo", null},
	        {2, "Tablet Samsung", "2025-04-15", "2025-05-15", "Finalizado", null},
	        {1, "Libro: Java Básico", "2025-05-01", "2025-06-01", "Activo", null},
	        {2, "Tablet Samsung", "2025-04-15", "2025-05-15", "Finalizado", null},
	        {1, "Libro: Java Básico", "2025-05-01", "2025-06-01", "Activo", null},
	        {2, "Tablet Samsung", "2025-04-15", "2025-05-15", "Finalizado", null},   {1, "Libro: Java Básico", "2025-05-01", "2025-06-01", "Activo", null},
	        {2, "Tablet Samsung", "2025-04-15", "2025-05-15", "Finalizado", null},   {1, "Libro: Java Básico", "2025-05-01", "2025-06-01", "Activo", null},
	        {2, "Tablet Samsung", "2025-04-15", "2025-05-15", "Finalizado", null},   {1, "Libro: Java Básico", "2025-05-01", "2025-06-01", "Activo", null},
	        {2, "Tablet Samsung", "2025-04-15", "2025-05-15", "Finalizado", null},
	    };
	
	    DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return column == 5; // Solo columna de acciones editable
	        }
	    };
	
	    JTable tabla = new JTable(modelo);
	    tabla.setRowHeight(40);
	    tabla.getColumn("Acciones").setCellRenderer(new BotonRenderer());
	    tabla.getColumn("Acciones").setCellEditor(new BotonEditor(new JCheckBox()));
	
	    JScrollPane scroll = new JScrollPane(tabla);
	    add(scroll, BorderLayout.CENTER);
	}

}
