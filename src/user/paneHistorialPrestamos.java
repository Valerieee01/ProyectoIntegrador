package user;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.BorderLayout;

import userClass.BotonEditor;
import userClass.BotonRenderer;
import userClass.FormularioEditarPrestamo;
import util.UsuarioSesion;

public class paneHistorialPrestamos extends JPanel {

    private static final long serialVersionUID = 1L;
    private static DefaultTableModel modelo;
    private static JTable tabla;

    public paneHistorialPrestamos() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 255, 198));

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener((ActionEvent e) -> cargarPrestamosDesdeBD());
        panelSuperior.add(btnRefrescar);
        add(panelSuperior, BorderLayout.NORTH);

        String[] columnas = {
            "ID", "Elemento", "Especialidad", "Fecha Inicio", "Fecha Fin", "ID Solicitante", "ID Admin", "Acciones"
        };

        modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Solo columna "Acciones" editable
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(40);
        tabla.getColumn("Acciones").setCellRenderer(new BotonRenderer());
        tabla.getColumn("Acciones").setCellEditor(new BotonEditor(new JCheckBox(), this));

        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        cargarPrestamosDesdeBD(); // Cargar al iniciar
    }

    public void cargarPrestamosDesdeBD() {
        Long idUsuario = UsuarioSesion.getIdentificacion();
        if (idUsuario == null) return;

        try (Connection conn = util.ConexionBDSoli.obtenerConexionSolicitante()) {
            String sql = """
                SELECT id, objetoSolicita, especialidad, FechaHoraInicio, FechaHoraFin,
                       idSolicitante, idAdmin
                FROM AdminGestrorPrestamos.RegistroPrestamo
                WHERE idSolicitante = ?
                ORDER BY FechaHoraInicio DESC
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            modelo.setRowCount(0);

            while (rs.next()) {
                Object[] fila = {
                    rs.getLong("id"),
                    rs.getString("objetoSolicita"),
                    rs.getString("especialidad"),
                    rs.getDate("FechaHoraInicio"),
                    rs.getDate("FechaHoraFin"),
                    rs.getLong("idSolicitante"),
                    rs.getLong("idAdmin"),
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

        String nuevoElemento = tabla.getValueAt(filaTabla, 1).toString();
        String nuevaEspecialidad = tabla.getValueAt(filaTabla, 2).toString();
        String nuevaFechaInicio = tabla.getValueAt(filaTabla, 3).toString();
        String nuevaFechaFin = tabla.getValueAt(filaTabla, 4).toString();

        try (Connection conn = util.ConexionBDSoli.obtenerConexionSolicitante()) {
            String update = """
                UPDATE AdminGestrorPrestamos.RegistroPrestamo
                SET objetoSolicita = ?, especialidad = ?, FechaHoraInicio = ?, FechaHoraFin = ?
                WHERE id = ?
            """;

            PreparedStatement ps = conn.prepareStatement(update);
            ps.setString(1, nuevoElemento);
            ps.setString(2, nuevaEspecialidad);
            ps.setDate(3, Date.valueOf(nuevaFechaInicio));
            ps.setDate(4, Date.valueOf(nuevaFechaFin));
            ps.setLong(5, idPrestamo);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Préstamo actualizado correctamente.");
            cargarPrestamosDesdeBD();

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
                String deleteRegistro = "DELETE FROM AdminGestrorPrestamos.RegistroPrestamo WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(deleteRegistro);
                ps.setLong(1, idPrestamo);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(null, "Préstamo eliminado correctamente.");
                cargarPrestamosDesdeBD();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al eliminar: " + ex.getMessage());
            }
        }
    }
}
