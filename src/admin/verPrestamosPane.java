package admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class verPrestamosPane extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtIdPrestamo, txtCodigo, txtTipo;
    private JButton btnModificar, btnEliminar;

    public verPrestamosPane() {
        setLayout(null);
        setBackground(new Color(255, 255, 206));

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(20, 20, 50, 25);
        add(lblTipo);

        txtTipo = new JTextField();
        txtTipo.setBounds(80, 20, 100, 25);
        add(txtTipo);

        JLabel lblIdPrestamo = new JLabel("ID Prestamo:");
        lblIdPrestamo.setBounds(200, 20, 90, 25);
        add(lblIdPrestamo);

        txtIdPrestamo = new JTextField();
        txtIdPrestamo.setBounds(300, 20, 100, 25);
        add(txtIdPrestamo);

        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setBounds(420, 20, 60, 25);
        add(lblCodigo);

        txtCodigo = new JTextField();
        txtCodigo.setBounds(490, 20, 100, 25);
        add(txtCodigo);

        btnModificar = new JButton("Modificar");
        btnModificar.setBounds(610, 20, 100, 25);
        add(btnModificar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(720, 20, 100, 25);
        add(btnEliminar);

        tableModel = new DefaultTableModel(new String[]{"ID", "ID Prestamo", " Codigo (equipo o Sala)", "Tipo"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 60, 800, 300);
        add(scrollPane);

        cargarPrestamos();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = table.getSelectedRow();
                if (fila != -1) {
                    txtTipo.setText(tableModel.getValueAt(fila, 3).toString());
                    txtIdPrestamo.setText(tableModel.getValueAt(fila, 1).toString());
                    txtCodigo.setText(tableModel.getValueAt(fila, 2).toString());
                }
            }
        });

        btnModificar.addActionListener(e -> modificarPrestamo());
        btnEliminar.addActionListener(e -> eliminarPrestamo());
    }

    private void cargarPrestamos() {
        try {
            Connection con = util.ConexionBD.obtenerConexionAdmin();
            tableModel.setRowCount(0);

            // Cargar préstamos de salas
            PreparedStatement ps1 = con.prepareStatement("SELECT id, idPrestamo, codigoSala FROM PrestamoSala");
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                tableModel.addRow(new Object[]{
                        rs1.getInt("id"),
                        rs1.getInt("idPrestamo"),
                        rs1.getInt("codigoSala"),
                        "Sala"
                });
            }

            // Cargar préstamos de equipos
            PreparedStatement ps2 = con.prepareStatement("SELECT id, idPrestamo, codigoEquipo FROM PrestamoEquipo");
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                tableModel.addRow(new Object[]{
                        rs2.getInt("id"),
                        rs2.getInt("idPrestamo"),
                        rs2.getInt("codigoEquipo"),
                        "Equipo"
                });
            }

            rs1.close();
            rs2.close();
            ps1.close();
            ps2.close();
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar préstamos: " + e.getMessage());
        }
    }

    private void modificarPrestamo() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo para modificar.");
            return;
        }

        int id = (int) tableModel.getValueAt(fila, 0);
        String tipo = txtTipo.getText();
        int nuevoIdPrestamo = Integer.parseInt(txtIdPrestamo.getText());
        int nuevoCodigo = Integer.parseInt(txtCodigo.getText());

        try {
            Connection con = util.ConexionBD.obtenerConexionAdmin();
            String sql = "";
            if (tipo.equalsIgnoreCase("Sala")) {
                sql = "UPDATE PrestamoSala SET idPrestamo = ?, codigoSala = ? WHERE id = ?";
            } else if (tipo.equalsIgnoreCase("Equipo")) {
                sql = "UPDATE PrestamoEquipo SET idPrestamo = ?, codigoEquipo = ? WHERE id = ?";
            } else {
                JOptionPane.showMessageDialog(this, "Tipo inválido.");
                return;
            }

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, nuevoIdPrestamo);
            ps.setInt(2, nuevoCodigo);
            ps.setInt(3, id);
            ps.executeUpdate();
            ps.close();
            con.close();

            JOptionPane.showMessageDialog(this, "Préstamo modificado.");
            cargarPrestamos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage());
        }
    }

    private void eliminarPrestamo() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo para eliminar.");
            return;
        }

        int id = (int) tableModel.getValueAt(fila, 0);
        String tipo = tableModel.getValueAt(fila, 3).toString();

        try {
            Connection con = util.ConexionBD.obtenerConexionAdmin();
            String sql = tipo.equals("Sala") ?
                    "DELETE FROM PrestamoSala WHERE id = ?" :
                    "DELETE FROM PrestamoEquipo WHERE id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            con.close();

            JOptionPane.showMessageDialog(this, "Préstamo eliminado.");
            cargarPrestamos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
        }
    }
}
