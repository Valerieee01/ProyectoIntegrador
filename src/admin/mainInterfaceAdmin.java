package admin;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import user.verPrestamosPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class mainInterfaceAdmin extends JFrame {

	private static final long serialVersionUID = 1L;
	private CardLayout cardLayout;
	private JPanel contentPane;
	JPanel panelContenedor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainInterfaceAdmin frame = new mainInterfaceAdmin();
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
	public mainInterfaceAdmin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBounds(100, 100, 861, 551);
	    setLocationRelativeTo(null);

	    cardLayout = new CardLayout();
	    contentPane = new JPanel();
	    contentPane.setLayout(null); 
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    setContentPane(contentPane); 
	    
	    panelContenedor = new JPanel();
	    panelContenedor.setBackground(new Color(255, 255, 206));
	    panelContenedor.setBounds(0, 63, 843, 449);
	    panelContenedor.setLayout(cardLayout);
	    contentPane.add(panelContenedor);
	    
	    // Instanciar los paneles
	    crearEquiposPane crearEquiposPane = new crearEquiposPane();
	    crearSalasPane crearSalasPane = new crearSalasPane();
	    verPrestamosPane verPrestamosPane = new verPrestamosPane();
	    verUsuariosPane verUsuariosPane = new verUsuariosPane();
	    crearEdificios crearEdificios = new crearEdificios();
	    crearReportesPane crearReportesPane = new crearReportesPane();



	    // Crear panel de bienvenida
	    JPanel panelBienvenida = new JPanel();
	    panelBienvenida.setBackground(new Color(255, 255, 206));
	    JLabel bienvenidolabel = new JLabel("BIENVENIDO ADMINISTRADOR");
	    bienvenidolabel.setHorizontalAlignment(SwingConstants.CENTER);
	    bienvenidolabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
	    panelBienvenida.add(bienvenidolabel);

	    // Añadirlos al contenedor con una clave
	    panelContenedor.add(panelBienvenida, "panelBienvenida");
	    panelContenedor.add(verPrestamosPane, "namePaneVerPrestamos");
	    panelContenedor.add(crearSalasPane, "namePaneCrearSalas");
	    panelContenedor.add(crearEquiposPane, "namePaneCrearEquipos");
	    panelContenedor.add(verUsuariosPane, "namePaneVerUsuarios");
	    panelContenedor.add(crearEdificios, "namePanecrearEdificios");
	    panelContenedor.add(crearReportesPane, "namePanecrearReportesPane");



	    // Mostrar el panel de bienvenida al inicio
	    cardLayout.show(panelContenedor, "panelBienvenida");
	    

	    
	    JPanel panelMenuBar = new JPanel();
	    panelMenuBar.setLayout(null);
	    panelMenuBar.setBackground(new Color(199, 235, 252));
	    panelMenuBar.setBounds(0, 0, 843, 64);
	    contentPane.add(panelMenuBar);
	    
	    JLabel labelCrearEquipos = new JLabel("Equipos");
	    labelCrearEquipos.setBackground(new Color(199, 235, 252));
	    labelCrearEquipos.setHorizontalAlignment(SwingConstants.CENTER);
	    labelCrearEquipos.setOpaque(true); // ¡Esto es clave!
	    labelCrearEquipos.setBounds(25, 25, 134, 28);
	    panelMenuBar.add(labelCrearEquipos);
	    
	    JLabel labelCrearSalas = new JLabel("Salas");
	    labelCrearSalas.setBackground(new Color(199, 235, 252));
	    labelCrearSalas.setHorizontalAlignment(SwingConstants.CENTER);
	    labelCrearSalas.setBounds(509, 25, 169, 28);
	    labelCrearSalas.setOpaque(true); // ¡Esto es clave! 
	    panelMenuBar.add(labelCrearSalas);
	    
	    JLabel labelUsuarios = new JLabel("Usuarios");
	    labelUsuarios.setBackground(new Color(199, 235, 252));
	    labelUsuarios.setHorizontalAlignment(SwingConstants.CENTER);
	    labelUsuarios.setBounds(187, 25, 134, 28);
	    labelUsuarios.setOpaque(true); // ¡Esto es clave!
	    panelMenuBar.add(labelUsuarios);
	    
	    JLabel labelEdificios = new JLabel("Edificios");
	    labelEdificios.setOpaque(true);
	    labelEdificios.setHorizontalAlignment(SwingConstants.CENTER);
	    labelEdificios.setBackground(new Color(199, 235, 252));
	    labelUsuarios.setOpaque(true); // ¡Esto es clave!
	    labelEdificios.setBounds(365, 25, 134, 28);
	    panelMenuBar.add(labelEdificios);
	    
	    JLabel lblReportes = new JLabel("Reportes");
	    lblReportes.setBounds(692, 25, 105, 28);
	    lblReportes.setOpaque(true);
	    lblReportes.setHorizontalAlignment(SwingConstants.CENTER);
	    lblReportes.setBackground(new Color(199, 235, 252));
	    panelMenuBar.add(lblReportes);
	    
	    Color colorNormal = new Color(199, 235, 252); // Color inicial
	    Color colorHover = new Color(220, 220, 220);  // Color al pasar el mouse
	   // Acciones de los label 
	    
	    lblReportes.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {
	    		cardLayout.show(panelContenedor, "namePanecrearReportesPane");
	    	}
	    	@Override
	    	public void mouseEntered(MouseEvent e) {
	    		lblReportes.setBackground(colorHover);
	    		lblReportes.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    	}
	    	@Override
	    	public void mouseExited(MouseEvent e) {
	    		lblReportes.setBackground(colorNormal);
	    	}
	    });
	    
	    labelEdificios.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {
	    		cardLayout.show(panelContenedor, "namePanecrearEdificios");
	    	}
	    	@Override
	    	public void mouseEntered(MouseEvent e) {
	    		labelEdificios.setBackground(colorHover);
	    		labelEdificios.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    	}
	    	@Override
	    	public void mouseExited(MouseEvent e) {
	    		labelEdificios.setBackground(colorNormal);
	    	}
	    });
	    labelCrearSalas.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {
	    		cardLayout.show(panelContenedor, "namePaneCrearSalas");
	    	}
	    	@Override
	    	public void mouseEntered(MouseEvent e) {
	    		labelCrearSalas.setBackground(colorHover);
	    		labelCrearSalas.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    	}
	    	@Override
	    	public void mouseExited(MouseEvent e) {
	    		labelCrearSalas.setBackground(colorNormal);
	    	}
	    });
	    
	  
	    
	    
	    labelUsuarios.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {
	    		cardLayout.show(panelContenedor, "namePaneVerUsuarios");
	    	}
	    	@Override
	    	public void mouseEntered(MouseEvent e) {
	    		labelUsuarios.setBackground(colorHover);
	    		labelUsuarios.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    	}
	    	@Override
	    	public void mouseExited(MouseEvent e) {
	    		labelUsuarios.setBackground(colorNormal);
	    	}
	    });
	    
	    labelCrearEquipos.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {
	    		cardLayout.show(panelContenedor, "namePaneCrearEquipos");
	    	}
	    	@Override
	    	public void mouseEntered(MouseEvent e) {
	    		labelCrearEquipos.setBackground(colorHover);
	    		labelCrearEquipos.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    	}
	    	@Override
	    	public void mouseExited(MouseEvent e) {
	    		labelCrearEquipos.setBackground(colorNormal);
	    	}
	    });
	}
}
