package admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import adminClass.BotonEditor;
import adminClass.BotonRenderer;
import adminClass.FormularioEditarPrestamo;

public class PrestamosPane extends JPanel {

	private static final long serialVersionUID = 1L;
	private static DefaultTableModel modelo;
	private static JTable tabla;


	/**
	 * Create the panel.
	 */
	public PrestamosPane() {
		setLayout(new BorderLayout());
	    setBackground(new Color(255, 255, 198));


	    String[] columnas = {"ID", "Elemento", "Fecha Inicio", "Fecha Fin", "Estado", "Acciones"};
	    Object[][] datos = {

	    };
	
	    modelo = new DefaultTableModel(datos, columnas) {
	        /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
	        public boolean isCellEditable(int row, int column) {
	            return column == 5; // Solo columna de acciones editable
	        }
	    };
	
	    tabla = new JTable(modelo);
	    tabla.setRowHeight(40);
	    tabla.getColumn("Acciones").setCellRenderer(new BotonRenderer());
	    tabla.getColumn("Acciones").setCellEditor(new BotonEditor(new JCheckBox(), this));
	
	    JScrollPane scroll = new JScrollPane(tabla);
	    add(scroll, BorderLayout.CENTER);
	    cargarPrestamosDesdeBD();
	}
	
	public void cargarPrestamosDesdeBD() {

	    try (Connection conn = util.ConexionBDSoli.obtenerConexionSolicitante()) {
	        String sql = """
	            SELECT rp.id, rp.objetoSolicita, rp.FechaHoraInicio, rp.FechaHoraFin,
	                   rpp.estadoPrestamo
	            FROM AdminGestrorPrestamos.RegistroPrestamo rp
	            JOIN AdminGestrorPrestamos.ReportesPrestamo rpp ON rp.id = rpp.idPrestamo
	            ORDER BY rp.FechaHoraInicio DESC
	        """;

	        PreparedStatement stmt = conn.prepareStatement(sql);
	        ResultSet rs = stmt.executeQuery();

	        modelo.setRowCount(0); // Limpiar tabla

	        while (rs.next()) {
	            Object[] fila = {
	                rs.getLong("id"),
	                rs.getString("objetoSolicita"),
	                rs.getDate("FechaHoraInicio"),
	                rs.getDate("FechaHoraFin"),
	                rs.getString("estadoPrestamo"),
	                "Editar / Eliminar"
	            };
	            modelo.addRow(fila);
	        }

	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(this, "Error al cargar los préstamos: " + e.getMessage());
	    }
	}

	


	public void editarPrestamo(Long idPrestamo, int filaTabla) {
		FormularioEditarPrestamo form = new FormularioEditarPrestamo(null, tabla, filaTabla);
	    form.setVisible(true);

	    // Luego de cerrar el formulario, actualiza en la BD con los nuevos valores
	    String nuevoElemento = tabla.getValueAt(filaTabla, 1).toString();
	    String nuevaFechaInicio = tabla.getValueAt(filaTabla, 2).toString();
	    String nuevaFechaFin = tabla.getValueAt(filaTabla, 3).toString();
	    String nuevoEstado = tabla.getValueAt(filaTabla, 4).toString();

	    try (Connection conn = util.ConexionBDSoli.obtenerConexionSolicitante()) {
	        String update1 = "UPDATE AdminGestrorPrestamos.RegistroPrestamo SET objetoSolicita = ?, FechaHoraInicio = ?, FechaHoraFin = ? WHERE id = ?";
	        String update2 = "UPDATE AdminGestrorPrestamos.ReportesPrestamo SET estadoPrestamo = ? WHERE idPrestamo = ?";

	        try (PreparedStatement ps1 = conn.prepareStatement(update1);
	             PreparedStatement ps2 = conn.prepareStatement(update2)) {

	            ps1.setString(1, nuevoElemento);
	            ps1.setDate(2, Date.valueOf(nuevaFechaInicio));
	            ps1.setDate(3, Date.valueOf(nuevaFechaFin));
	            ps1.setLong(4, idPrestamo);
	            ps1.executeUpdate();

	            ps2.setString(1, nuevoEstado);
	            ps2.setLong(2, idPrestamo);
	            ps2.executeUpdate();

	            JOptionPane.showMessageDialog(null, "Préstamo actualizado correctamente.");
	            cargarPrestamosDesdeBD();
	        }

	    } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(null, "Error al editar: " + ex.getMessage());
	    }
	}


	
	public void eliminarPrestamo(Long idPrestamo) {
	    int confirmar = JOptionPane.showConfirmDialog(null,
	        "¿Estás seguro de eliminar el préstamo ID: " + idPrestamo + "?", "Confirmar",
	        JOptionPane.YES_NO_OPTION);

	    if (confirmar == JOptionPane.YES_OPTION) {
	        try (Connection conn = util.ConexionBDSoli.obtenerConexionSolicitante()) {
	            conn.setAutoCommit(false);

	            String deleteReporte = "DELETE FROM AdminGestrorPrestamos.ReportesPrestamo WHERE idPrestamo = ?";
	            String deleteSala = "DELETE FROM AdminGestrorPrestamos.PrestamoSala WHERE idPrestamo = ?";
	            String deleteEquipo = "DELETE FROM AdminGestrorPrestamos.PrestamoEquipo WHERE idPrestamo = ?";
	            String deleteRegistro = "DELETE FROM AdminGestrorPrestamos.RegistroPrestamo WHERE id = ?";

	            try (
	                PreparedStatement ps1 = conn.prepareStatement(deleteReporte);
	                PreparedStatement ps2 = conn.prepareStatement(deleteSala);
	                PreparedStatement ps3 = conn.prepareStatement(deleteEquipo);
	                PreparedStatement ps4 = conn.prepareStatement(deleteRegistro)
	            ) {
	                ps1.setLong(1, idPrestamo); ps1.executeUpdate();
	                ps2.setLong(1, idPrestamo); ps2.executeUpdate();
	                ps3.setLong(1, idPrestamo); ps3.executeUpdate();
	                ps4.setLong(1, idPrestamo); ps4.executeUpdate();

	                conn.commit();
	                JOptionPane.showMessageDialog(null, "Préstamo eliminado correctamente.");
	                cargarPrestamosDesdeBD();
	            } catch (SQLException e) {
	                conn.rollback();
	                throw e;
	            }

	        } catch (SQLException ex) {
	            JOptionPane.showMessageDialog(null, "Error al eliminar: " + ex.getMessage());
	        }
	    }
	}

}
