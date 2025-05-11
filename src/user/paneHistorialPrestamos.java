package user;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.CardLayout;

public class paneHistorialPrestamos extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public paneHistorialPrestamos() {
		setBackground(new Color(255, 255, 198));
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("historial");
		lblNewLabel.setBounds(173, 134, 46, 14);
		add(lblNewLabel);

	}

}
