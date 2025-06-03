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
    private JComboBox<String> comboEdificios;
    private java.util.Map<String, Integer> mapaEdificios; // nombre -> id

    public crearEquiposPane() {
        setLayout(null);
        setBackground(new Color(255, 255, 206));

        JLabel lblTitulo = new JLabel("Registrar Equipos Audiovisuales");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBounds(20, 10, 400, 30);
        add(lblTitulo);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 60, 100, 20);
        add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setBounds(120, 60, 200, 25);
        add(txtNombre);

        JLabel lblObservaciones = new JLabel("Observaciones:");
        lblObservaciones.setBounds(20, 95, 100, 20);
        add(lblObservaciones);
        txtObservaciones = new JTextField();
        txtObservaciones.setBounds(120, 95, 200, 25);
        add(txtObservaciones);

        JLabel lblIdEdificio = new JLabel("Edificio:");
        lblIdEdificio.setBounds(20, 130, 100, 20);
        add(lblIdEdificio);

        comboEdificios = new JComboBox<>();
        comboEdificios.setBounds(120, 130, 200, 25);
        add(comboEdificios);

        mapaEdificios = new java.util.HashMap<>();

        // Cargar edificios en el combo
        cargarEdificiosDesdeBD();

        JButton btnAgregar = new JButton("Agregar Equipo");
        btnAgregar.setBounds(120, 170, 200, 30);
        add(btnAgregar);

        String[] columnas = {"Código", "Nombre", "Observaciones", "Edificio"};
        tableModel = new DefaultTableModel(columnas, 0);

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 220, 600, 200);
        add(scrollPane);
        
        JButton btnModificar = new JButton("Modificar");
        btnModificar.setBounds(340, 170, 120, 30);
        add(btnModificar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(470, 170, 120, 30);
        add(btnEliminar);
        
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = table.getSelectedRow();
                if (fila != -1) {
                    txtNombre.setText(tableModel.getValueAt(fila, 1).toString());
                    txtObservaciones.setText(tableModel.getValueAt(fila, 2).toString());

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
            }
        });
        btnModificar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un equipo para modificar.");
                return;
            }

            int codigo = (int) tableModel.getValueAt(fila, 0);
            String nombre = txtNombre.getText();
            String observaciones = txtObservaciones.getText();
            String nombreEdificio = (String) comboEdificios.getSelectedItem();

            if (nombreEdificio == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un edificio válido.");
                return;
            }

            int idEdificio = mapaEdificios.get(nombreEdificio);

            modificarEquipoBD(codigo, nombre, observaciones, idEdificio);
            cargarEquiposDesdeBD(); // refrescar tabla
        });

        
        btnAgregar.addActionListener(e -> {
            String nombre = txtNombre.getText();
            String observaciones = txtObservaciones.getText();
            String nombreEdificio = (String) comboEdificios.getSelectedItem();

            if (nombre.isEmpty() || observaciones.isEmpty() || nombreEdificio == null) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idEdificio = mapaEdificios.get(nombreEdificio);

            try {
                Connection con = util.ConexionBD.obtenerConexionAdmin();
                String sql = "INSERT INTO equipo_audiovisual (nombre, observaciones, idEdificio) VALUES (?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, nombre);
                ps.setString(2, observaciones);
                ps.setInt(3, idEdificio);
                ps.executeUpdate();
                ps.close();
                con.close();

                JOptionPane.showMessageDialog(this, "Equipo agregado correctamente.");
                cargarEquiposDesdeBD(); // refrescar la tabla
                txtNombre.setText("");
                txtObservaciones.setText("");
                comboEdificios.setSelectedIndex(-1);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al agregar equipo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = table.getSelectedRow();
                
                if (filaSeleccionada != -1) {
                    int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el equipo?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                    
                    if (confirmacion == JOptionPane.YES_OPTION) {
                        // Suponiendo que la primera columna contiene el ID del equipo
                        int idEquipo = Integer.parseInt(table.getValueAt(filaSeleccionada, 0).toString());

                        try (Connection conn = ConexionBD.obtenerConexionAdmin()) {
                            String sql = "DELETE FROM equipo_audiovisual WHERE id_equipo = ?";
                            PreparedStatement stmt = conn.prepareStatement(sql);
                            stmt.setInt(1, idEquipo);

                            int filasAfectadas = stmt.executeUpdate();

                            if (filasAfectadas > 0) {
                                JOptionPane.showMessageDialog(null, "Equipo eliminado correctamente.");
                                cargarEquiposDesdeBD(); // Método para recargar los datos en la tabla
                            } else {
                                JOptionPane.showMessageDialog(null, "No se pudo eliminar el equipo.");
                            }

                            stmt.close();
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Error al eliminar el equipo: " + ex.getMessage());
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un equipo de la tabla para eliminar.");
                }
            }
        });

        
        cargarEquiposDesdeBD();

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

    private void cargarEquiposDesdeBD() {
        try {
            Connection con = ConexionBD.obtenerConexionAdmin();

            // Consulta con JOIN para traer nombre del edificio junto con los equipos
            String sql = "SELECT e.codigo, e.nombre, e.observaciones, ed.nombre AS nombreEdificio " +
                         "FROM equipo_audiovisual e " +
                         "JOIN edificio ed ON e.idEdificio = ed.id";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            tableModel.setRowCount(0); // limpiar tabla

            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("codigo"),
                    rs.getString("nombre"),
                    rs.getString("observaciones"),
                    rs.getString("nombreEdificio")  // mostrar nombre del edificio, no id
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

		
		private void modificarEquipoBD(int codigo, String nombre, String observaciones, int idEdificio) {
			try {
				Connection con = util.ConexionBD.obtenerConexionAdmin();
				String sql = "UPDATE equipo_audiovisual SET nombre = ?, observaciones = ?, idEdificio = ? WHERE codigo = ?";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, nombre);
				ps.setString(2, observaciones);
				ps.setInt(3, idEdificio);
				ps.setInt(4, codigo);
				ps.executeUpdate();
				ps.close();
				con.close();
				JOptionPane.showMessageDialog(this, "Equipo modificado correctamente.");
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(this, "Error al modificar equipo: " + e.getMessage());
			}
		}
}

