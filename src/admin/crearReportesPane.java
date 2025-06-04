package admin;

import util.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class crearReportesPane extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel model;
    private JTextField estadoField, correoField, idPrestamoField;
    private JButton btnAgregar, btnModificar, btnEliminar, btnRefrescar;

    public crearReportesPane() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"ID", "Estado", "Correo", "ID Préstamo"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.setBackground(new Color(255, 255, 185));
        estadoField = new JTextField();
        correoField = new JTextField();
        idPrestamoField = new JTextField();

        formPanel.add(new JLabel("Estado:"));
        formPanel.add(estadoField);
        formPanel.add(new JLabel("Correo:"));
        formPanel.add(correoField);
        formPanel.add(new JLabel("ID Préstamo:"));
        formPanel.add(idPrestamoField);
        add(formPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 183));
        btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new Color(170, 213, 255));
        btnModificar = new JButton("Modificar");
        btnModificar.setBackground(new Color(170, 213, 255));
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(170, 213, 255));
        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setBackground(new Color(170, 213, 255));

        buttonPanel.add(btnAgregar);
        buttonPanel.add(btnModificar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnRefrescar);
        add(buttonPanel, BorderLayout.SOUTH);

        cargarDatos();

        // Listeners
        btnAgregar.addActionListener(e -> agregarReporte());
        btnModificar.addActionListener(e -> modificarReporte());
        btnEliminar.addActionListener(e -> eliminarReporte());
        btnRefrescar.addActionListener(e -> cargarDatos());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    estadoField.setText(model.getValueAt(row, 1).toString());
                    correoField.setText(model.getValueAt(row, 2).toString());
                    idPrestamoField.setText(model.getValueAt(row, 3).toString());
                }
            }
        });
    }

    private void cargarDatos() {
        model.setRowCount(0);
        try (Connection con = ConexionBD.obtenerConexionAdmin();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM ReportesPrestamo")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("estadoPrestamo"),
                        rs.getString("correoSolicitante"),
                        rs.getInt("idPrestamo")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
        }
    }

    private void agregarReporte() {
        String estado = estadoField.getText();
        String correo = correoField.getText();
        int idPrestamo;

        try {
            idPrestamo = Integer.parseInt(idPrestamoField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID Préstamo debe ser un número entero.");
            return;
        }

        try (Connection con = ConexionBD.obtenerConexionAdmin()) {
            String sql = "INSERT INTO ReportesPrestamo (id, estadoPrestamo, correoSolicitante, idPrestamo) VALUES (SEQ_REGISTROPRESTAMO.NEXTVAL, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, estado);
            pst.setString(2, correo);
            pst.setInt(3, idPrestamo);
            pst.executeUpdate();
            cargarDatos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar reporte: " + e.getMessage());
        }
    }

    private void modificarReporte() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un reporte para modificar.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        String estado = estadoField.getText();
        String correo = correoField.getText();

        try (Connection con = ConexionBD.obtenerConexionAdmin()) {
            String sql = "UPDATE ReportesPrestamo SET estadoPrestamo = ?, correoSolicitante = ? WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, estado);
            pst.setString(2, correo);
            pst.setInt(3, id);
            pst.executeUpdate();
            cargarDatos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar reporte: " + e.getMessage());
        }
    }

    private void eliminarReporte() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un reporte para eliminar.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar el reporte?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection con = ConexionBD.obtenerConexionAdmin()) {
            String sql = "DELETE FROM ReportesPrestamo WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            cargarDatos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar reporte: " + e.getMessage());
        }
    }
}
