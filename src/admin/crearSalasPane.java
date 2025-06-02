package admin;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class crearSalasPane extends JPanel {

	private static final long serialVersionUID = 1L;

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtObservaciones, txtIdEdificio;

    public crearSalasPane() {
        setLayout(null);
        setBackground(new Color(255, 255, 206));

        JLabel lblObservaciones = new JLabel("Observaciones:");
        lblObservaciones.setBounds(30, 30, 100, 25);
        add(lblObservaciones);

        txtObservaciones = new JTextField();
        txtObservaciones.setBounds(140, 30, 200, 25);
        add(txtObservaciones);

        JLabel lblIdEdificio = new JLabel("ID Edificio:");
        lblIdEdificio.setBounds(30, 70, 100, 25);
        add(lblIdEdificio);

        txtIdEdificio = new JTextField();
        txtIdEdificio.setBounds(140, 70, 200, 25);
        add(txtIdEdificio);

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBounds(30, 110, 100, 30);
        add(btnAgregar);

        JButton btnModificar = new JButton("Modificar");
        btnModificar.setBounds(140, 110, 100, 30);
        add(btnModificar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(250, 110, 100, 30);
        add(btnEliminar);

        // Tabla
        String[] columnas = {"Código", "Observaciones", "ID Edificio"};
        tableModel = new DefaultTableModel(columnas, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 160, 500, 250);
        add(scrollPane);

        // Eventos
        cargarSalasDesdeBD();

        btnAgregar.addActionListener(e -> agregarSala());
        btnModificar.addActionListener(e -> modificarSala());
        btnEliminar.addActionListener(e -> eliminarSala());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = table.getSelectedRow();
                if (fila != -1) {
                    txtObservaciones.setText(tableModel.getValueAt(fila, 1).toString());
                    txtIdEdificio.setText(tableModel.getValueAt(fila, 2).toString());
                }
            }
        });
    }

    private void agregarSala() {
        String obs = txtObservaciones.getText();
        String idEd = txtIdEdificio.getText();

        if (obs.isEmpty() || idEd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        try {
            Connection con = util.ConexionBD.obtenerConexion();
            String sql = "INSERT INTO sala_informatica (observaciones, idEdificio) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, obs);
            ps.setInt(2, Integer.parseInt(idEd));
            ps.executeUpdate();
            ps.close();
            con.close();
            JOptionPane.showMessageDialog(this, "Sala agregada.");
            cargarSalasDesdeBD();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar sala: " + ex.getMessage());
        }
    }

    private void modificarSala() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una sala.");
            return;
        }

        int codigo = (int) tableModel.getValueAt(fila, 0);
        String obs = txtObservaciones.getText();
        String idEd = txtIdEdificio.getText();

        try {
            Connection con = util.ConexionBD.obtenerConexion();
            String sql = "UPDATE sala_informatica SET observaciones = ?, idEdificio = ? WHERE codigo = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, obs);
            ps.setInt(2, Integer.parseInt(idEd));
            ps.setInt(3, codigo);
            ps.executeUpdate();
            ps.close();
            con.close();
            JOptionPane.showMessageDialog(this, "Sala modificada.");
            cargarSalasDesdeBD();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar sala: " + ex.getMessage());
        }
    }

    private void eliminarSala() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una sala.");
            return;
        }

        int codigo = (int) tableModel.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar sala?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection con = util.ConexionBD.obtenerConexion();
                String sql = "DELETE FROM sala_informatica WHERE codigo = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, codigo);
                ps.executeUpdate();
                ps.close();
                con.close();
                JOptionPane.showMessageDialog(this, "Sala eliminada.");
                cargarSalasDesdeBD();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar sala: " + ex.getMessage());
            }
        }
    }

    private void cargarSalasDesdeBD() {
        try {
            Connection con = util.ConexionBD.obtenerConexion();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM sala_informatica");
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("codigo"),
                        rs.getString("observaciones"),
                        rs.getInt("idEdificio")
                });
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar salas: " + e.getMessage());
        }
    }
}