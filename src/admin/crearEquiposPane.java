package admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import util.ConexionBD;

public class crearEquiposPane extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTextField txtNombre, txtObservaciones;
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> comboEdificios, comboIdTipo;
    private java.util.Map<String, Integer> mapaEdificios, mapaTipo; 
    private JTextField textFieldCodigo;

    public crearEquiposPane() {
        setLayout(null);
        setBackground(new Color(255, 255, 206));

        JLabel lblTitulo = new JLabel("Registrar Equipos Audiovisuales");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBounds(20, 10, 400, 30);
        add(lblTitulo);

        // Campo: Código
        JLabel lblId = new JLabel("Codigo:");
        lblId.setBounds(20, 44, 100, 20);
        add(lblId);

        textFieldCodigo = new JTextField();
        textFieldCodigo.setBounds(120, 44, 200, 25);
        add(textFieldCodigo);

        // Campo: Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 80, 100, 20);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(120, 80, 200, 25);
        add(txtNombre);

        // Campo: Tipo de dispositivo
        JLabel lbldTipo = new JLabel("Tipo:");
        lbldTipo.setBounds(20, 113, 100, 20);
        add(lbldTipo);

        comboIdTipo = new JComboBox<>();
        comboIdTipo.setBounds(120, 113, 200, 25);
        add(comboIdTipo);

        // Inicializar mapaTipo antes de cargar
        mapaTipo = new java.util.HashMap<>();
        cargarTiposDesdeBD();

        // Campo: Observaciones
        JLabel lblObservaciones = new JLabel("Observaciones:");
        lblObservaciones.setBounds(20, 149, 100, 20);
        add(lblObservaciones);

        txtObservaciones = new JTextField();
        txtObservaciones.setBounds(120, 149, 200, 25);
        add(txtObservaciones);

        // Campo: Edificio
        JLabel lblIdEdificio = new JLabel("Edificio:");
        lblIdEdificio.setBounds(20, 184, 100, 20);
        add(lblIdEdificio);

        comboEdificios = new JComboBox<>();
        comboEdificios.setBounds(120, 184, 200, 25);
        add(comboEdificios);

        // Inicializar mapaEdificios antes de cargar
        mapaEdificios = new java.util.HashMap<>();
        cargarEdificiosDesdeBD();

        // Botones
        JButton btnAgregar = new JButton("Agregar Equipo");
        btnAgregar.setBounds(336, 179, 141, 30);
        add(btnAgregar);

        JButton btnModificar = new JButton("Modificar");
        btnModificar.setBounds(487, 179, 120, 30);
        add(btnModificar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(617, 179, 120, 30);
        add(btnEliminar);

        // Tabla
        String[] columnas = {"Código", "Nombre", "Observaciones", "Edificio", "Tipo"};
        tableModel = new DefaultTableModel(columnas, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 220, 600, 200);
        add(scrollPane);

        // Listeners
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = table.getSelectedRow();
                if (fila != -1) {
                    textFieldCodigo.setText(tableModel.getValueAt(fila, 0).toString());
                    txtNombre.setText(tableModel.getValueAt(fila, 1).toString());
                    txtObservaciones.setText(tableModel.getValueAt(fila, 2).toString());

                    comboEdificios.setSelectedItem(tableModel.getValueAt(fila, 3).toString());
                    comboIdTipo.setSelectedItem(tableModel.getValueAt(fila, 4).toString());
                }
            }
        });

        btnAgregar.addActionListener(e -> agregarEquipoBD());
        btnModificar.addActionListener(e -> modificarEquipoBD());
        btnEliminar.addActionListener(e -> eliminarEquipoBD());

        // Cargar datos a tabla
        cargarEquiposDesdeBD();
    }

    private void agregarEquipoBD() {
        try {
            int codigo = Integer.parseInt(textFieldCodigo.getText());
            String nombre = txtNombre.getText();
            String observaciones = txtObservaciones.getText();
            String nombreEdificio = (String) comboEdificios.getSelectedItem();
            String nombreTipo = (String) comboIdTipo.getSelectedItem();

            if (nombre.isEmpty() || observaciones.isEmpty() || nombreEdificio == null || nombreTipo == null) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idEdificio = mapaEdificios.get(nombreEdificio);
            int idTipo = mapaTipo.get(nombreTipo);

            Connection con = ConexionBD.obtenerConexionAdmin();
            String sql = "INSERT INTO equipo_audiovisual (codigo, nombre, observaciones, idEdificio, idTipo) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, codigo);
            ps.setString(2, nombre);
            ps.setString(3, observaciones);
            ps.setInt(4, idEdificio);
            ps.setInt(5, idTipo);
            ps.executeUpdate();

            ps.close();
            con.close();

            JOptionPane.showMessageDialog(this, "Equipo agregado correctamente.");
            limpiarCampos();
            cargarEquiposDesdeBD();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar equipo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarEquipoBD() {
        int filaSeleccionada = table.getSelectedRow();

        if (filaSeleccionada != -1) {
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el equipo?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                int idEquipo = Integer.parseInt(tableModel.getValueAt(filaSeleccionada, 0).toString());

                try (Connection conn = ConexionBD.obtenerConexionAdmin()) {
                    String sql = "DELETE FROM equipo_audiovisual WHERE codigo = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, idEquipo);

                    int filasAfectadas = stmt.executeUpdate();

                    if (filasAfectadas > 0) {
                        JOptionPane.showMessageDialog(this, "Equipo eliminado correctamente.");
                        cargarEquiposDesdeBD();
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo eliminar el equipo.");
                    }

                    stmt.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el equipo: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un equipo de la tabla para eliminar.");
        }
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
    
    private void limpiarCampos() {
        textFieldCodigo.setText("");
        txtNombre.setText("");
        txtObservaciones.setText("");
        comboEdificios.setSelectedIndex(-1);
        comboIdTipo.setSelectedIndex(-1);
    }

    private void modificarEquipoBD() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un equipo para modificar.");
            return;
        }

        try {
            int codigo = Integer.parseInt(tableModel.getValueAt(fila, 0).toString());
            String nombre = txtNombre.getText();
            String observaciones = txtObservaciones.getText();
            String nombreEdificio = (String) comboEdificios.getSelectedItem();
            String nombreTipo = (String) comboIdTipo.getSelectedItem();

            if (nombre.isEmpty() || observaciones.isEmpty() || nombreEdificio == null || nombreTipo == null) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idEdificio = mapaEdificios.get(nombreEdificio);
            int idTipo = mapaTipo.get(nombreTipo);

            Connection con = ConexionBD.obtenerConexionAdmin();
            String sql = "UPDATE equipo_audiovisual SET nombre = ?, observaciones = ?, idEdificio = ?, idTipo = ? WHERE codigo = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, observaciones);
            ps.setInt(3, idEdificio);
            ps.setInt(4, idTipo);
            ps.setInt(5, codigo);
            ps.executeUpdate();

            ps.close();
            con.close();

            JOptionPane.showMessageDialog(this, "Equipo modificado correctamente.");
            limpiarCampos();
            cargarEquiposDesdeBD();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar equipo: " + e.getMessage());
        }
    }

    private void cargarTiposDesdeBD() {
        try {
            Connection con = ConexionBD.obtenerConexionAdmin();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, nombre FROM tipo_dispositivo");

            comboIdTipo.removeAllItems();
            mapaTipo.clear();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                comboIdTipo.addItem(nombre);
                mapaTipo.put(nombre, id);
            }

            rs.close();
            st.close();
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar tipos: " + e.getMessage());
        }
    }


    private void cargarEquiposDesdeBD() {
        try {
            Connection con = ConexionBD.obtenerConexionAdmin();
            String sql = "SELECT e.codigo, e.nombre, e.observaciones, ed.nombre AS nombreEdificio, td.nombre AS nombreTipo " +
                         "FROM equipo_audiovisual e " +
                         "JOIN edificio ed ON e.idEdificio = ed.id " +
                         "JOIN tipo_dispositivo td ON e.idTipo = td.id";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            tableModel.setRowCount(0);
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("codigo"),
                    rs.getString("nombre"),
                    rs.getString("observaciones"),
                    rs.getString("nombreEdificio"),
                    rs.getString("nombreTipo")
                };
                tableModel.addRow(fila);
            }

            rs.close();
            st.close();
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar equipos: " + e.getMessage());
        }
    }


		
    private void modificarEquipoBD(int codigo, String nombre, String observaciones, int idEdificio, int idTipo) {
        try {
            Connection con = ConexionBD.obtenerConexionAdmin();
            String sql = "UPDATE equipo_audiovisual SET nombre = ?, observaciones = ?, idEdificio = ?, idTipo = ? WHERE codigo = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, observaciones);
            ps.setInt(3, idEdificio);
            ps.setInt(4, idTipo);
            ps.setInt(5, codigo);
            ps.executeUpdate();
            ps.close();
            con.close();
            JOptionPane.showMessageDialog(this, "Equipo modificado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar equipo: " + e.getMessage());
        }
    }

}

