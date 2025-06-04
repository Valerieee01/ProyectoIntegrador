package admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class crearEdificios extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtNombre;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAgregar, btnModificar, btnEliminar;
    private int idSeleccionado = -1;
    private JLabel lblCodigo;
    private JTextField textFieldCodigo;

    public crearEdificios() {
        setLayout(null);
        setBackground(new Color(255, 255, 187));

        JLabel lblTitulo = new JLabel("Gestión de Edificios");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitulo.setBounds(20, 10, 300, 30);
        add(lblTitulo);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 98, 80, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(103, 98, 200, 25);
        add(txtNombre);

        btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new Color(187, 221, 255));
        btnAgregar.setBounds(320, 78, 100, 25);
        add(btnAgregar);

        btnModificar = new JButton("Modificar");
        btnModificar.setBackground(new Color(187, 221, 255));
        btnModificar.setBounds(430, 78, 100, 25);
        add(btnModificar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(187, 221, 255));
        btnEliminar.setBounds(540, 78, 100, 25);
        add(btnEliminar);

        model = new DefaultTableModel(new String[]{"ID", "Nombre"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 152, 620, 250);
        add(scrollPane);
        
        lblCodigo = new JLabel("Codigo:");
        lblCodigo.setBounds(20, 60, 80, 25);
        add(lblCodigo);
        
        textFieldCodigo = new JTextField();
        textFieldCodigo.setText("");
        textFieldCodigo.setBounds(103, 60, 200, 25);
        add(textFieldCodigo);

        cargarEdificios();

        // Evento para seleccionar fila
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = table.getSelectedRow();
                if (fila != -1) {
                    idSeleccionado = Integer.parseInt(table.getValueAt(fila, 0).toString());
                    txtNombre.setText(table.getValueAt(fila, 1).toString());
                }
            }
        });

        // Agregar edificio
        btnAgregar.addActionListener(e -> agregarEdificio());

        // Modificar edificio
        btnModificar.addActionListener(e -> modificarEdificio());

        // Eliminar edificio
        btnEliminar.addActionListener(e -> eliminarEdificio());
    }

    // Método para cargar edificios en la tabla
    private void cargarEdificios() {
        model.setRowCount(0);
        try (Connection con = util.ConexionBD.obtenerConexionAdmin();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, nombre FROM edificio")) {

            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("nombre")});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar edificios: " + ex.getMessage());
        }
    }

    // Método para agregar
    private void agregarEdificio() {
    	String id = textFieldCodigo.getText().trim(); 
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
            return;
        }
        String sql = "INSERT INTO edificio (id, nombre) VALUES (?, ?)";
        try (Connection con = util.ConexionBD.obtenerConexionAdmin();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, nombre);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Edificio agregado.");
            cargarEdificios();
            txtNombre.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar: " + ex.getMessage());
        }
    }

    // Método para modificar
    private void modificarEdificio() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un edificio para modificar.");
            return;
        }
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
            return;
        }
        String sql = "UPDATE edificio SET nombre = ? WHERE id = ?";
        try (Connection con = util.ConexionBD.obtenerConexionAdmin();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, idSeleccionado);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Edificio modificado.");
            cargarEdificios();
            txtNombre.setText("");
            idSeleccionado = -1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage());
        }
    }

    // Método para eliminar
    private void eliminarEdificio() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un edificio para eliminar.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM edificio WHERE id = ?";
            try (Connection con = util.ConexionBD.obtenerConexionAdmin();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idSeleccionado);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Edificio eliminado.");
                cargarEdificios();
                txtNombre.setText("");
                idSeleccionado = -1;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
            }
        }
    }
}
