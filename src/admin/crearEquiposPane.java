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

    private JTextField txtNombre, txtObservaciones, txtIdEdificio;
    private DefaultTableModel tableModel;
    private JTable table;

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

        JLabel lblIdEdificio = new JLabel("ID Edificio:");
        lblIdEdificio.setBounds(20, 130, 100, 20);
        add(lblIdEdificio);
        txtIdEdificio = new JTextField();
        txtIdEdificio.setBounds(120, 130, 200, 25);
        add(txtIdEdificio);

        JButton btnAgregar = new JButton("Agregar Equipo");
        btnAgregar.setBounds(120, 170, 200, 30);
        add(btnAgregar);

        String[] columnas = {"Código", "Nombre", "Observaciones", "ID Edificio"};
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
                    txtIdEdificio.setText(tableModel.getValueAt(fila, 3).toString());
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
            int idEdificio = Integer.parseInt(txtIdEdificio.getText());

            modificarEquipoBD(codigo, nombre, observaciones, idEdificio);
            cargarEquiposDesdeBD(); // refrescar tabla
        });
        

        btnAgregar.addActionListener(e -> {
            String nombre = txtNombre.getText();
            String observaciones = txtObservaciones.getText();
            String idEdificio = txtIdEdificio.getText();

            if (nombre.isEmpty() || observaciones.isEmpty() || idEdificio.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Simula inserción en tabla
            Vector<String> fila = new Vector<>();
            fila.add(nombre);
            fila.add(observaciones);
            fila.add(idEdificio);
            tableModel.addRow(fila);

            // Limpiar campos
            txtNombre.setText("");
            txtObservaciones.setText("");
            txtIdEdificio.setText("");
        });
    }

	private void cargarEquiposDesdeBD() {
		try {
			Connection con = util.ConexionBD.obtenerConexion();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM equipo_audiovisual");
			
			tableModel.setRowCount(0); // limpiar tabla
			
			while (rs.next()) {
				Object[] fila = {
						rs.getInt("codigo"),
						rs.getString("nombre"),
						rs.getString("observaciones"),
						rs.getInt("idEdificio")
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
				Connection con = util.ConexionBD.obtenerConexion();
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

