package connectionFrames;

import java.awt.Frame;

import login.loginInterface;
import login.singinInteface;
import user.mainInterfaceUser;

public class connectFrames {
	

	public static void AbrirFrameLogin(Frame frame) {
		new loginInterface().setVisible(true);
		frame.setVisible(false);
	}
	
	public static void AbrirFrameSingin(Frame frame) {
		new singinInteface().setVisible(true);
		frame.setVisible(false);
	}
	
	public static void AbrirFrameInitUser(Frame frame) {
		new mainInterfaceUser().setVisible(true);
		frame.setVisible(false);
	}

}
