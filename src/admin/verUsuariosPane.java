package admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class verUsuariosPane extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtCorreo, txtContrasena, txtNombre, txtApellido, txtEdad, txtCargo;
    private int idSeleccionado = -1;

    public verUsuariosPane() {
        setLayout(null);
        setBackground(new Color(240, 240, 240));

        JLabel lblTitulo = new JLabel("Gestión de Usuarios");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitulo.setBounds(20, 10, 300, 30);
        add(lblTitulo);

        JLabel[] labels = {
            new JLabel("Correo:"),
            new JLabel("Contraseña:"),
            new JLabel("Nombre:"),
            new JLabel("Apellido:"),
            new JLabel("Edad:"),
            new JLabel("Cargo:")
        };

        JTextField[] fields = {
            txtCorreo = new JTextField(),
            txtContrasena = new JTextField(),
            txtNombre = new JTextField(),
            txtApellido = new JTextField(),
            txtEdad = new JTextField(),
            txtCargo = new JTextField()
        };

        for (int i = 0; i < labels.length; i++) {
            labels[i].setBounds(20, 50 + (i * 30), 100, 25);
            add(labels[i]);
            fields[i].setBounds(130, 50 + (i * 30), 200, 25);
            add(fields[i]);
        }

        JButton btnModificar = new JButton("Modificar");
        btnModificar.setBounds(350, 50, 100, 25);
        add(btnModificar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(460, 50, 100, 25);
        add(btnEliminar);

        model = new DefaultTableModel(new String[]{"Identificación", "Correo", "Contraseña", "Nombre", "Apellido", "Edad", "Cargo"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 230, 700, 200);
        add(scrollPane);

        cargarUsuarios();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = table.getSelectedRow();
                if (fila != -1) {
                    idSeleccionado = Integer.parseInt(model.getValueAt(fila, 0).toString());
                    txtCorreo.setText(model.getValueAt(fila, 1).toString());
                    txtContrasena.setText(model.getValueAt(fila, 2).toString());
                    txtNombre.setText(model.getValueAt(fila, 3).toString());
                    txtApellido.setText(model.getValueAt(fila, 4).toString());
                    txtEdad.setText(model.getValueAt(fila, 5).toString());
                    txtCargo.setText(model.getValueAt(fila, 6).toString());
                }
            }
        });

        btnModificar.addActionListener(e -> modificarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
    }

    private void cargarUsuarios() {
        model.setRowCount(0);
        try (Connection con = util.ConexionBD.obtenerConexionAdmin();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM solicitante")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("identificacion"), rs.getString("correo"), rs.getString("contrasenia"),
                        rs.getString("nombre"), rs.getString("apellido"), rs.getInt("edad"), rs.getString("cargo")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + ex.getMessage());
        }
    }

    private void modificarUsuario() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para modificar.");
            return;
        }
        try (Connection con = util.ConexionBD.obtenerConexionAdmin();
             PreparedStatement ps = con.prepareStatement("UPDATE solicitante SET correo=?, contrasenia=?, nombre=?, apellido=?, edad=?, cargo=? WHERE identificacion=?")) {

            ps.setString(1, txtCorreo.getText().trim());
            ps.setString(2, txtContrasena.getText().trim());
            ps.setString(3, txtNombre.getText().trim());
            ps.setString(4, txtApellido.getText().trim());
            ps.setInt(5, Integer.parseInt(txtEdad.getText().trim()));
            ps.setString(6, txtCargo.getText().trim());
            ps.setInt(7, idSeleccionado);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Usuario modificado.");
            cargarUsuarios();
            idSeleccionado = -1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage());
        }
    }

    private void eliminarUsuario() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection con =  util.ConexionBD.obtenerConexionAdmin();
                 PreparedStatement ps = con.prepareStatement("DELETE FROM solicitante WHERE identificacion=?")) {
                ps.setInt(1, idSeleccionado);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Usuario eliminado.");
                cargarUsuarios();
                idSeleccionado = -1;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
            }
        }
    }
}
