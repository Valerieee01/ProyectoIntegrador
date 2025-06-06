package admin;

import java.awt.Color;
import java.awt.Font;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class crearSalasPane extends JPanel {

	private static final long serialVersionUID = 1L;

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtObservaciones;
    private JComboBox<String> comboEdificios;
    private java.util.Map<String, Integer> mapaEdificios; // nombre -> id
    private JTextField textFieldCodigoSala;

    public crearSalasPane() {
        setLayout(null);
        setBackground(new Color(255, 255, 206));

        JLabel lblObservaciones = new JLabel("Observaciones:");
        lblObservaciones.setBounds(33, 75, 100, 25);
        add(lblObservaciones);

        txtObservaciones = new JTextField();
        txtObservaciones.setBounds(143, 75, 200, 25);
        add(txtObservaciones);
        
        JLabel lblIdEdificio = new JLabel("Edificio:");
        lblIdEdificio.setBounds(33, 111, 100, 20);
        add(lblIdEdificio);

        comboEdificios = new JComboBox<>();
        comboEdificios.setBounds(143, 111, 200, 25);
        add(comboEdificios);

        mapaEdificios = new java.util.HashMap<>();

        // Cargar edificios en el combo
        cargarEdificiosDesdeBD();

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBounds(33, 147, 100, 30);
        add(btnAgregar);

        JButton btnModificar = new JButton("Modificar");
        btnModificar.setBounds(143, 147, 100, 30);
        add(btnModificar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(253, 147, 100, 30);
        add(btnEliminar);

        // Tabla
        String[] columnas = {"Código", "Observaciones", "ID Edificio"};
        tableModel = new DefaultTableModel(columnas, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(23, 189, 500, 250);
        add(scrollPane);
        
        JLabel labelCodigoSalas = new JLabel("Codigo:");
        labelCodigoSalas.setBounds(33, 39, 100, 25);
        add(labelCodigoSalas);
        
        textFieldCodigoSala = new JTextField();
        textFieldCodigoSala.setBounds(143, 39, 200, 25);
        add(textFieldCodigoSala);
        
        JLabel lblSalas = new JLabel("Registrar Salas de Informatica");
        lblSalas.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblSalas.setHorizontalAlignment(SwingConstants.LEFT);
        lblSalas.setBounds(33, 11, 361, 30);
        add(lblSalas);

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
                }
                
                int idEdificio = (int) tableModel.getValueAt(fila, 3);

                // Buscar nombre por idEdificio para mostrarlo en el combo
                String nombreEdificio = null;
                for (var entry : mapaEdificios.entrySet()) {
                    if (entry.getValue() == idEdificio) {
                        nombreEdificio = entry.getKey();
                        break;
                    }
                }
                comboEdificios.setSelectedItem(nombreEdificio);
            }
        });
    }

    
    private void cargarEdificiosDesdeBD() {
        try {
            Connection con = util.ConexionBD.obtenerConexionAdmin();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, nombre FROM edificio");

            comboEdificios.removeAllItems();
            mapaEdificios.clear();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                comboEdificios.addItem(nombre);
                mapaEdificios.put(nombre, id);
            }

            rs.close();
            st.close();
            con.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar edificios: " + e.getMessage());
        }
    }
    
    
    private void agregarSala() {
        String codigoTexto = textFieldCodigoSala.getText();
        String obs = txtObservaciones.getText();
        String nombreEdificio = (String) comboEdificios.getSelectedItem();

        if (codigoTexto.isEmpty() || obs.isEmpty() || nombreEdificio == null) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        int idEdificio = mapaEdificios.get(nombreEdificio);
        int codigo;

        try {
            codigo = Integer.parseInt(codigoTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El código debe ser un número entero.");
            return;
        }

        try {
            Connection con = util.ConexionBD.obtenerConexionAdmin();
            String sql = "INSERT INTO sala_informatica (codigo, observaciones, idEdificio) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, codigo);
            ps.setString(2, obs);
            ps.setInt(3, idEdificio);
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
        String nombreEdificio = (String) comboEdificios.getSelectedItem();
        
        if (nombreEdificio == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un edificio válido.");
            return;
        }
        int idEdificio = mapaEdificios.get(nombreEdificio);
        modificarSalaBD(codigo, obs, idEdificio);
        cargarSalasDesdeBD(); // refrescar tabla
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
                Connection con = util.ConexionBD.obtenerConexionAdmin();
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
    
    
    
     private void modificarSalaBD(int codigo, String obs, int idEd) {
    	  try {
              Connection con = util.ConexionBD.obtenerConexionAdmin();
              String sql = "UPDATE sala_informatica SET observaciones = ?, idEdificio = ? WHERE codigo = ?";
              PreparedStatement ps = con.prepareStatement(sql);
              ps.setString(1, obs);
              ps.setInt(2, idEd);
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
     
     
    private void cargarSalasDesdeBD() {
        try {
            Connection con = util.ConexionBD.obtenerConexionAdmin();
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