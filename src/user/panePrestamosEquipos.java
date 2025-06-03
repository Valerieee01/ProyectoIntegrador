package user;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import util.ConexionBD;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;

public class panePrestamosEquipos extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tableDispositivos;
    private JTable tableReferencias;
    private JButton btnReservar;

    public panePrestamosEquipos() {
        setBackground(new Color(255, 255, 198));
        setLayout(null);
        
        JLabel labelDispositivo = new JLabel("SISTEMA DE PRÉSTAMO DE EQUIPOS");
        labelDispositivo.setHorizontalAlignment(SwingConstants.CENTER);
        labelDispositivo.setFont(new Font("Tahoma", Font.BOLD, 16));
        labelDispositivo.setBounds(10, 10, 580, 30);
        add(labelDispositivo);
        
        JLabel labelSeleccion = new JLabel("Seleccione un dispositivo:");
        labelSeleccion.setFont(new Font("Tahoma", Font.PLAIN, 12));
        labelSeleccion.setBounds(81, 50, 150, 20);
        add(labelSeleccion);
        
        // Combo box para selección de dispositivos
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setModel(new DefaultComboBoxModel<>(new String[] {"Selecciona un dispositivo..."}));
        comboBox.setBounds(81, 75, 238, 30);
        add(comboBox);
        
        // Imagen del dispositivo
        JLabel labelImgDispositivo = new JLabel("");
        labelImgDispositivo.setHorizontalAlignment(SwingConstants.CENTER);
        labelImgDispositivo.setBounds(81, 115, 238, 150);
        labelImgDispositivo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(labelImgDispositivo);
        
        // Tabla de dispositivos
        tableDispositivos = new JTable();
        tableDispositivos.setModel(new DefaultTableModel(
            new Object[][] {
                {"1", "Computador Portátil", "PC"},
                {"2", "Proyector", "PY"},
                {"3", "Cámara", "CAM"},
                {"4", "Televisor", "TV"}
            },
            new String[] {"Código", "Nombre", "Abreviación"}
        ));
        JScrollPane scrollDispositivos = new JScrollPane(tableDispositivos);
        scrollDispositivos.setBounds(350, 75, 250, 120);
        add(scrollDispositivos);
        
     // Tabla para mostrar referencias
        tableReferencias = new JTable();
        tableReferencias.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"Referencia", "Modelo", "Estado", "Disponibilidad"}
        ));
        JScrollPane scrollReferencias = new JScrollPane(tableReferencias);
        scrollReferencias.setBounds(81, 280, 520, 150);
        add(scrollReferencias);
        
        // Botón de reserva
        btnReservar = new JButton("RESERVAR EQUIPO");
        btnReservar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnReservar.setBounds(543, 214, 250, 40);
        btnReservar.setEnabled(false);
        add(btnReservar);
        
        // Llenar el comboBox con los nombres de dispositivos
        DefaultTableModel modelDispositivos = (DefaultTableModel) tableDispositivos.getModel();
        for (int i = 0; i < modelDispositivos.getRowCount(); i++) {
            String nombre = modelDispositivos.getValueAt(i, 1).toString();
            comboBox.addItem(nombre);
        }
        
        // ActionListener para el comboBox
        comboBox.addActionListener(e -> {
            String dispositivoSeleccionado = (String) comboBox.getSelectedItem();
            if (dispositivoSeleccionado != null && !dispositivoSeleccionado.equals("Selecciona un dispositivo...")) {
                String abreviatura = obtenerAbreviatura(dispositivoSeleccionado);
                cargarImagenDispositivo(abreviatura, labelImgDispositivo);
                cargarReferenciasDesdeBD(abreviatura); 
                btnReservar.setEnabled(false);
            }
        });

        
        // Listener para selección en tabla de referencias
        tableReferencias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                btnReservar.setEnabled(tableReferencias.getSelectedRow() >= 0);
            }
        });
        
        // Tabla de dispositivos
        tableDispositivos = new JTable();
        tableDispositivos.setModel(new DefaultTableModel(
        		  new Object[][] {
                      {"1", "Computador Portátil", "PORT"},
                      {"2", "Proyector", "PROY"},
                      {"3", "Cámara", "CAM"},
                      {"4", "Televisor", "TV"}
                  },
                  new String[] {"Código", "Nombre", "Abreviación"}
        ));
        
     
        // Tabla para mostrar referencias
        tableReferencias = new JTable();
        tableReferencias.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"Id", "Modelo", "Estado", "Disponibilidad"}
        ));
     
        
        // Botón de reserva
        btnReservar = new JButton("RESERVAR EQUIPO");
        btnReservar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnReservar.setBounds(543, 214, 250, 40);
        btnReservar.setEnabled(false);
        add(btnReservar);
        
      
       
        
        // Listener para selección en tabla de referencias
        tableReferencias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                btnReservar.setEnabled(tableReferencias.getSelectedRow() >= 0);
            }
        });
        
        // ActionListener para el botón de reserva
        btnReservar.addActionListener(e -> realizarReserva());
    }
    
    private void cargarReferenciasDesdeBD(String abreviacion) {
        DefaultTableModel modelo = (DefaultTableModel) tableReferencias.getModel();
        modelo.setRowCount(0); // Limpiar la tabla

        try (Connection conn = util.ConexionBDSoli.obtenerConexionSolicitante()) {
            String sql = """
                SELECT ea.codigo, ea.nombre, ea.observaciones
                FROM AdminGestrorPrestamos.equipo_audiovisual ea
                JOIN AdminGestrorPrestamos.tipo_dispositivo td ON ea.idTipo = td.id
                WHERE UPPER(td.abreviacion) = ?
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, abreviacion.toUpperCase());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("codigo");
                String nombre = rs.getString("nombre");
                String observaciones = rs.getString("observaciones");

                modelo.addRow(new Object[]{id, nombre, "Bueno", "Disponible"}); // Simulando campos
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar referencias: " + e.getMessage());
        }
    }

    private void guardarReservaEnBD(int idPrestamo, int codigoEquipo) {
        try (Connection conn = util.ConexionBDSoli.obtenerConexionSolicitante()) {
    

            String sql = "INSERT INTO AdminGestrorPrestamos.PrestamoEquipo (idPrestamo, codigoEquipo) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idPrestamo); 
            ps.setInt(2, codigoEquipo);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Reserva registrada en la base de datos.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar reserva: " + e.getMessage());
        }
    }
    
    private void realizarReserva() {
        int filaSeleccionada = tableReferencias.getSelectedRow();
        if (filaSeleccionada >= 0) {
            String referencia = (String) tableReferencias.getValueAt(filaSeleccionada, 0);
            String modelo = (String) tableReferencias.getValueAt(filaSeleccionada, 1);
            String disponibilidad = (String) tableReferencias.getValueAt(filaSeleccionada, 3);

            if ("Disponible".equals(disponibilidad)) {
                int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Confirmar reserva para:\n" + modelo + " (" + referencia + ")?",
                    "Confirmar Reserva",
                    JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    int codigoEquipo = Integer.parseInt(referencia); // Asegúrate que esta columna es el código
                    int idPrestamo = 1; // Este valor deberías traerlo del módulo de préstamos

                    guardarReservaEnBD(idPrestamo, codigoEquipo);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Este equipo no está disponible para préstamo",
                    "No Disponible",
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private String obtenerAbreviatura(String nombreDispositivo) {
        DefaultTableModel model = (DefaultTableModel) tableDispositivos.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 1).equals(nombreDispositivo)) {
                return model.getValueAt(i, 2).toString();
            }
        }
        return "";
    }
    
    private void cargarImagenDispositivo(String abreviatura, JLabel label) {
        String rutaImagen = "";
        switch (abreviatura) {
            case "PORT": rutaImagen = "/img/pc.jpg"; break;
            case "PROY": rutaImagen = "/img/proyector.jpg"; break;
            case "CAM": rutaImagen = "/img/camara.jpg"; break;
            case "TV": rutaImagen = "/img/tv.jpg"; break;
            default: rutaImagen = "/img/default.jpg";
        }
        cargarImagenEnLabel(label, rutaImagen, 238, 150);
    }
    
    
    public void cargarImagenEnLabel(JLabel label, String rutaInterna, int ancho, int alto) {
        try {
            ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(rutaInterna));
            if (iconoOriginal.getImage() != null) {
                Image imagenOriginal = iconoOriginal.getImage();
                Image imagenEscalada = imagenOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(imagenEscalada));
            }
        } catch (Exception e) {
            label.setIcon(null);
            label.setText("Imagen no disponible");
        }
    }
    
    
}