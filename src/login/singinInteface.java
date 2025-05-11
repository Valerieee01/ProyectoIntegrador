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
		textFieldPassword.setBounds(448, 307, 151, 32);
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
		textFieldCorreo.setBounds(260, 192, 151, 32);
		textFieldCorreo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(217, 244, 253)));
		contentPane.add(textFieldCorreo);
		
		textFieldEdad = new JTextField("Escribe Edad ..." , 20);
		textFieldEdad.setColumns(10);
		textFieldEdad.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(217, 244, 253)));
		textFieldEdad.setBounds(448, 248, 151, 32);
		contentPane.add(textFieldEdad);
		
		textFieldNombre = new JTextField("Escribe Nombre ..." , 20);
		textFieldNombre.setColumns(10);
		textFieldNombre.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(217, 244, 253)));
		textFieldNombre.setBounds(448, 192, 151, 32);
		contentPane.add(textFieldNombre);
		
		
		textFieldApellido = new JTextField("Escribe Apellidos..." , 20);
		textFieldApellido.setColumns(10);
		textFieldApellido.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(217, 244, 253)));
		textFieldApellido.setBounds(260, 248, 151, 32);
		contentPane.add(textFieldApellido);
		
		JComboBox<String> comboBoxCargo = new JComboBox<String>();
		comboBoxCargo.setBackground(new Color(255, 255, 255));
		comboBoxCargo.setModel(new DefaultComboBoxModel(new String[] {"Selecciona Cargo ...", "Administrador del Sistema", "Docente", "Administrativo"}));// Borde inferior: solo parte de abajo, 2 píxeles de grosor
        comboBoxCargo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(217, 244, 253)));
        comboBoxCargo.setOpaque(false);
		comboBoxCargo.setBounds(260, 306, 151, 32);
		contentPane.add(comboBoxCargo);
		
		JLabel labelregistro = new JLabel("Registro");
		labelregistro.setHorizontalAlignment(SwingConstants.CENTER);
		labelregistro.setFont(new Font("Sylfaen", Font.ITALIC, 17));
		labelregistro.setBounds(372, 138, 120, 32);
		contentPane.add(labelregistro);
		
		JLabel inicioSesion = new JLabel("Ya tienes cuenta?");
		inicioSesion.setBounds(546, 482, 158, 14);
		contentPane.add(inicioSesion);
		
		JButton btnirInicioSesion = new JButton("Inicia Sesion aquí");
		btnirInicioSesion.addActionListener(e -> {connectFrames.AbrirFrameLogin(this);});
		btnirInicioSesion.setBackground(new Color(217, 244, 253));
		btnirInicioSesion.setBounds(654, 478, 179, 23);
		contentPane.add(btnirInicioSesion);
	}
	
	public void cargarImagenEnLabel(JLabel label, String rutaInterna, int ancho, int alto) {
	    ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(rutaInterna));
	    Image imagenOriginal = iconoOriginal.getImage();
	    Image imagenEscalada = imagenOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
	    ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);
	    label.setIcon(iconoEscalado);
	}
}
