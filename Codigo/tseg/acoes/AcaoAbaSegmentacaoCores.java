package tseg.acoes;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextPane;

public class AcaoAbaSegmentacaoCores {
    
	public KeyListener txpSegmentacaoKeyListener() {
		KeyListener keyListener = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent evt) {
				((JTextPane) evt.getSource()).setEditable(false);
			}

			@Override
			public void keyReleased(KeyEvent evt) {
				((JTextPane) evt.getSource()).setEditable(true);
			}

			@Override
			public void keyPressed(KeyEvent evt) {
				((JTextPane) evt.getSource()).setEditable(false);
			}
		};
		return keyListener;
	}
    
}
