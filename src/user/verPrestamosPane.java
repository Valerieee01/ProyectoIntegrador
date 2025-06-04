package user;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import util.UsuarioSesion;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class verPrestamosPane extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtEspecialidad, txtInicio, txtFin, txtSolicitante, txtAdmin;
    private JButton btnRegistrar;
    private JLabel lbltituloPrestamos;
    private JComboBox<String> comboObjeto;
    private JComboBox<String> comboAdmin;
    private Map<String, Integer> mapaAdmins = new HashMap<>();
    private JLabel lablTituloPrestamos;
    private long idSolicitante = UsuarioSesion.getIdentificacion();
    private String nombre = UsuarioSesion.getNombre();


    public verPrestamosPane() {
        setLayout(null);
        setBackground(new Color(255, 255, 206));

        tableModel = new DefaultTableModel(new String[]{"ID", "Objeto", "Especialidad", "Inicio", "Fin", "Solicitante", "Admin"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 60, 800, 300);
        add(scrollPane);

        lbltituloPrestamos = new JLabel("Prestamos de Salas  y Equipos");
        lbltituloPrestamos.setFont(new Font("Tahoma", Font.BOLD, 28));
        lbltituloPrestamos.setBounds(30, 490, 438, 40);
        add(lbltituloPrestamos);
        
        
        // resgistro del prestamo
        JLabel lblObjeto = new JLabel("Objeto:");
        lblObjeto.setBounds(20, 370, 60, 25);
        add(lblObjeto);

        comboObjeto = new JComboBox<>();
        comboObjeto.setBounds(71, 371, 250, 25); 
        add(comboObjeto);
;
        JLabel lblEsp = new JLabel("Especialidad:");
        lblEsp.setBounds(350, 371, 100, 25);
        add(lblEsp);
        txtEspecialidad = new JTextField();
        txtEspecialidad.setBounds(430, 370, 100, 25);
        add(txtEspecialidad);

        JLabel lblInicio = new JLabel("Inicio:");
        lblInicio.setBounds(556, 370, 60, 25);
        add(lblInicio);
        txtInicio = new JTextField("YYYY-MM-DD HH:MM:SS");
        txtInicio.setBounds(610, 371, 150, 25);
        add(txtInicio);

        JLabel lblFin = new JLabel("Fin:");
        lblFin.setBounds(20, 410, 50, 25);
        add(lblFin);
        txtFin = new JTextField("YYYY-MM-DD HH:MM:SS");
        txtFin.setBounds(80, 410, 150, 25);
        add(txtFin);
    
        // Reemplaza txtAdmin
        comboAdmin = new JComboBox<>();
        comboAdmin.setBounds(360, 410, 256, 25);
        add(comboAdmin);

        JLabel lblAdmin = new JLabel("ID Admin:");
        lblAdmin.setBounds(292, 410, 100, 25);
        add(lblAdmin);

        btnRegistrar = new JButton("Registrar Préstamo");
        btnRegistrar.setBounds(643, 24, 150, 25);
        add(btnRegistrar);
        
        lablTituloPrestamos = new JLabel("Gestion de prestamos");
        lablTituloPrestamos.setFont(new Font("Tahoma", Font.BOLD, 21));
        lablTituloPrestamos.setBounds(30, 20, 420, 29);
        add(lablTituloPrestamos);

        cargarPrestamos();
        btnRegistrar.addActionListener(e -> registrarPrestamo());
        
        cargarOpcionesObjeto();
        cargarAdministradores();
        
        String seleccionado = (String) comboObjeto.getSelectedItem(); 
        String[] partes = seleccionado.split("[-:]");
        String tipo = partes[0].trim(); 
        int codigo = Integer.parseInt(partes[1].trim()); 
        
        
    
    }
    

    private void cargarAdministradores() {
        try {
            Connection con = util.ConexionBD.obtenerConexionAdmin();
            PreparedStatement ps = con.prepareStatement("SELECT identificacion , nombre FROM AdminGestrorPrestamos.AdministradorSoftware");
            ResultSet rs = ps.executeQuery();
            mapaAdmins.clear();
            comboAdmin.removeAllItems();

            while (rs.next()) {
                int id = rs.getInt("identificacion");
                String nombre = rs.getString("nombre");
                String entrada = id + " - " + nombre;
                comboAdmin.addItem(entrada);
                mapaAdmins.put(entrada, id);
            }

            rs.close(); ps.close(); con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar administradores: " + e.getMessage());
        }
    }

    private void cargarOpcionesObjeto() {
        try {
            Connection con = util.ConexionBD.obtenerConexionAdmin();
            comboObjeto.removeAllItems();

            // Salas Informáticas
            PreparedStatement psSala = con.prepareStatement("SELECT codigo, observaciones FROM AdminGestrorPrestamos.sala_informatica");
            ResultSet rsSala = psSala.executeQuery();
            while (rsSala.next()) {
                int codigo = rsSala.getInt("codigo");
                String descripcion = rsSala.getString("observaciones");
                comboObjeto.addItem("Sala-" + codigo + ": " + descripcion);
            }
            rsSala.close(); psSala.close();

            // Equipos Audiovisuales
            PreparedStatement psEq = con.prepareStatement("SELECT codigo, nombre FROM AdminGestrorPrestamos.equipo_audiovisual");
            ResultSet rsEq = psEq.executeQuery();
            while (rsEq.next()) {
                int codigo = rsEq.getInt("codigo");
                String nombre = rsEq.getString("nombre");
                comboObjeto.addItem("Equipo-" + codigo + ": " + nombre);
            }
            rsEq.close(); psEq.close();

            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar objetos: " + e.getMessage());
        }
    }

    private void cargarPrestamos() {
        try {
            Connection con = util.ConexionBD.obtenerConexionAdmin();
            tableModel.setRowCount(0); // Limpiar tabla

            String sql = "SELECT rp.id, rp.objetoSolicita, rp.especialidad, rp.FechaHoraInicio, rp.FechaHoraFin, " +
                         "s.nombre AS nombreSolicitante, a.nombre AS nombreAdmin " +
                         "FROM AdminGestrorPrestamos.RegistroPrestamo rp " +
                         "JOIN AdminGestrorPrestamos.Solicitante s ON rp.idSolicitante = s.identificacion " +
                         "JOIN AdminGestrorPrestamos.AdministradorSoftware a ON rp.idAdmin = a.identificacion";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[]{
                    rs.getInt("id"),
                    rs.getString("objetoSolicita"),
                    rs.getString("especialidad"),
                    rs.getTimestamp("FechaHoraInicio"),
                    rs.getTimestamp("FechaHoraFin"),
                    rs.getString("nombreSolicitante"),
                    rs.getString("nombreAdmin")
                };
                tableModel.addRow(fila);
            }

            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar préstamos: " + e.getMessage());
        }
    }
    private void registrarPrestamo() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = util.ConexionBD.obtenerConexionAdmin();

            // 1. Obtener el ID del nuevo préstamo
            String getIdSql = "SELECT SEQ_REGISTROPRESTAMO.NEXTVAL FROM dual";
            ps = con.prepareStatement(getIdSql);
            rs = ps.executeQuery();
            int nuevoIdPrestamo = -1;
            if (rs.next()) {
                nuevoIdPrestamo = rs.getInt(1);
            }
            rs.close();
            ps.close();

            if (nuevoIdPrestamo == -1) throw new SQLException("No se pudo obtener el ID del préstamo.");

            // Obtener ID del admin desde los mapas
            String adminSeleccionado = (String) comboAdmin.getSelectedItem();
            int idAdmin = mapaAdmins.get(adminSeleccionado);

            // 2. Insertar en RegistroPrestamo
            String insertRegSql = "INSERT INTO AdminGestrorPrestamos.RegistroPrestamo " +
                    "(id, objetoSolicita, especialidad, FechaHoraInicio, FechaHoraFin, idSolicitante, idAdmin) " +
                    "VALUES (?, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ?, ?)";
            ps = con.prepareStatement(insertRegSql);
            ps.setInt(1, nuevoIdPrestamo);
            ps.setString(2, (String) comboObjeto.getSelectedItem());
            ps.setString(3, txtEspecialidad.getText());
            ps.setString(4, txtInicio.getText());
            ps.setString(5, txtFin.getText());
            ps.setLong(6, idSolicitante);
            ps.setInt(7, idAdmin);
            ps.executeUpdate();
            ps.close();

            // 3. Determinar tipo y código del objeto
            String seleccionado = (String) comboObjeto.getSelectedItem(); // Ej: "Sala-101: Lab Redes"
            String[] partes = seleccionado.split("[-:]");
            String tipo = partes[0].trim(); // "Sala" o "Equipo"
            int codigo = Integer.parseInt(partes[1].trim());

            // 4. Insertar en tabla correspondiente
            if (tipo.equalsIgnoreCase("Sala")) {
                String insertSala = "INSERT INTO AdminGestrorPrestamos.PrestamoSala (id, idPrestamo, codigoSala) " +
                        "VALUES (SEQ_PRESTAMOSALA.NEXTVAL, ?, ?)";
                ps = con.prepareStatement(insertSala);
                ps.setInt(1, nuevoIdPrestamo);
                ps.setInt(2, codigo);
                ps.executeUpdate();
            } else if (tipo.equalsIgnoreCase("Equipo")) {
                String insertEquipo = "INSERT INTO AdminGestrorPrestamos.PrestamoEquipo (id, idPrestamo, codigoEquipo) " +
                        "VALUES (SEQ_PRESTAMOEQUIPO.NEXTVAL, ?, ?)";
                ps = con.prepareStatement(insertEquipo);
                ps.setInt(1, nuevoIdPrestamo);
                ps.setInt(2, codigo);
                ps.executeUpdate();
            }

            ps.close();
            con.close();

            JOptionPane.showMessageDialog(this, "Préstamo registrado correctamente.");
            cargarPrestamos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar préstamo: " + e.getMessage());
            try {
                if (con != null) con.rollback(); // Intentar rollback si algo falla
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}