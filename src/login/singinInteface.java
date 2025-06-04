package login;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import connectionFrames.connectFrames;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class singinInteface extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldPassword;
	private JLabel labelImg;
	private JTextField textFieldCorreo;
	private JTextField textFieldEdad;
	private JTextField textFieldNombre;
	private JTextField textFieldApellido;
	private JTextField textFieldIdentificacion;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					singinInteface frame = new singinInteface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public singinInteface() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 859, 551);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textFieldPassword = new JTextField("Escribe Contraseña ..." , 20);
		textFieldPassword.setBounds(451, 349, 151, 32);
		textFieldPassword.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(217, 244, 253)));
		contentPane.add(textFieldPassword);
		textFieldPassword.setColumns(10);
		
		JButton btnRegistro = new JButton("Registrarse");
		btnRegistro.setBackground(new Color(217, 244, 253));
		btnRegistro.setBounds(372, 404, 120, 23);
		contentPane.add(btnRegistro);
		
		JLabel titulo = new JLabel("Gestor de Prestamos \r\n");
		titulo.setFont(new Font("Sylfaen", Font.ITALIC, 19));
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		titulo.setBounds(307, 24, 248, 50);
		contentPane.add(titulo);
		
		labelImg = new JLabel("");
		labelImg.setBounds(385, 55, 95, 80);
		cargarImagenEnLabel(labelImg,"/img/logo.png", 95, 80);
		contentPane.add(labelImg);
		
		textFieldCorreo = new JTextField("Escribe Correo ..." , 20);
		textFieldCorreo.setColumns(10);
		textFieldCorreo.setBounds(263, 234, 151, 32);
		textFieldCorreo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(217, 244, 253)));
		contentPane.add(textFieldCorreo);
		
		textFieldEdad = new JTextField("Escribe Edad ..." , 20);
		textFieldEdad.setColumns(10);
		textFieldEdad.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(217, 244, 253)));
		textFieldEdad.setBounds(451, 290, 151, 32);
		contentPane.add(textFieldEdad);
		
		textFieldNombre = new JTextField("Escribe Nombre ..." , 20);
		textFieldNombre.setColumns(10);
		textFieldNombre.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(217, 244, 253)));
		textFieldNombre.setBounds(451, 234, 151, 32);
		contentPane.add(textFieldNombre);
		
		
		textFieldApellido = new JTextField("Escribe Apellidos..." , 20);
		textFieldApellido.setColumns(10);
		textFieldApellido.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(217, 244, 253)));
		textFieldApellido.setBounds(263, 290, 151, 32);
		contentPane.add(textFieldApellido);
		
		JComboBox<String> comboBoxCargo = new JComboBox<String>();
		comboBoxCargo.setBackground(new Color(255, 255, 255));
		comboBoxCargo.setModel(new DefaultComboBoxModel(new String[] {"Selecciona Cargo ...", "Administrador del Sistema", "Docente", "Administrativo"}));// Borde inferior: solo parte de abajo, 2 píxeles de grosor
        comboBoxCargo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(217, 244, 253)));
        comboBoxCargo.setOpaque(false);
		comboBoxCargo.setBounds(263, 348, 151, 32);
		contentPane.add(comboBoxCargo);
		
		JLabel labelregistro = new JLabel("Registro");
		labelregistro.setHorizontalAlignment(SwingConstants.CENTER);
		labelregistro.setFont(new Font("Sylfaen", Font.ITALIC, 17));
		labelregistro.setBounds(372, 138, 120, 32);
		contentPane.add(labelregistro);
		
		textFieldIdentificacion = new JTextField("escribe tu documento...", 10);
		textFieldIdentificacion.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(217, 244, 253)));
		textFieldIdentificacion.setBounds(355, 181, 151, 32);
		contentPane.add(textFieldIdentificacion);
		
		JLabel inicioSesion = new JLabel("Ya tienes cuenta?");
		inicioSesion.setBounds(546, 482, 158, 14);
		contentPane.add(inicioSesion);
		
		JButton btnirInicioSesion = new JButton("Inicia Sesion aquí");
		btnirInicioSesion.addActionListener(e -> {connectFrames.AbrirFrameLogin(this);});
		btnirInicioSesion.setBackground(new Color(217, 244, 253));
		btnirInicioSesion.setBounds(654, 478, 179, 23);
		contentPane.add(btnirInicioSesion);
		
		btnRegistro.addActionListener(e -> {
		    String getIdentificacion = textFieldIdentificacion.getText().trim();
		    int identificacion = Integer.parseInt(getIdentificacion);
		    String correo = textFieldCorreo.getText().trim();
		    String contrasenia = textFieldPassword.getText().trim();
		    String nombre = textFieldNombre.getText().trim();
		    String apellido = textFieldApellido.getText().trim();
		    String edadStr = textFieldEdad.getText().trim();
		    String cargo = (String) comboBoxCargo.getSelectedItem();

		    // Validación básica
		    if (getIdentificacion.isEmpty()||correo.isEmpty() || contrasenia.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || edadStr.isEmpty() || "Selecciona Cargo ...".equals(cargo)) {
		        javax.swing.JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.");
		        return;
		    }

		    int edad;
		    try {
		        edad = Integer.parseInt(edadStr);
		    } catch (NumberFormatException ex) {
		        javax.swing.JOptionPane.showMessageDialog(this, "La edad debe ser un número válido.");
		        return;
		    }

		    try {
		        java.sql.Connection conn = util.ConexionBD.obtenerConexionAdmin(); 
		        String sql;

		        if ("Administrador del Sistema".equals(cargo)) {
		            sql = "INSERT INTO AdministradorSoftware (identificacion, correo, contrasenia, nombre, apellido, edad) " +
		                  "VALUES (?, ?, ?, ?, ?, ?)";
		        } else {
		            sql = "INSERT INTO solicitante (identificacion, correo, contrasenia, nombre, apellido, edad, cargo) " +
		                  "VALUES (?, ?, ?, ?, ?, ?, ?)";
		        }

		        java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
		        stmt.setInt(1, identificacion);
		        stmt.setString(2, correo);
		        stmt.setString(3, contrasenia);
		        stmt.setString(4, nombre);
		        stmt.setString(5, apellido);
		        stmt.setInt(6, edad);

		        if (!"Administrador del Sistema".equals(cargo)) {
		            stmt.setString(7, cargo); // índice correcto aquí
		        }


		        int filas = stmt.executeUpdate();
		        if (filas > 0) {
		            javax.swing.JOptionPane.showMessageDialog(this, "Registro exitoso.");
		            // Limpiar campos
		            textFieldIdentificacion.setText("");
		            textFieldCorreo.setText("");
		            textFieldPassword.setText("");
		            textFieldNombre.setText("");
		            textFieldApellido.setText("");
		            textFieldEdad.setText("");
		            comboBoxCargo.setSelectedIndex(0);
		            
		        }

		        stmt.close();
		        conn.close();
		    } catch (Exception ex) {
		        ex.printStackTrace();
		        javax.swing.JOptionPane.showMessageDialog(this, "Error al registrar el usuario:\n" + ex.getMessage());
		    }
		});

	}
	
	public void cargarImagenEnLabel(JLabel label, String rutaInterna, int ancho, int alto) {
	    ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(rutaInterna));
	    Image imagenOriginal = iconoOriginal.getImage();
	    Image imagenEscalada = imagenOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
	    ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);
	    label.setIcon(iconoEscalado);
	}
}
