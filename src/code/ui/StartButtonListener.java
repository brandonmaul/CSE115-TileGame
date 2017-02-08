package code.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartButtonListener implements ActionListener {

	private UI _ui;
	private int _level;
	
	public StartButtonListener(UI ui, int x){
		_ui = ui;
		_level = x;
	}
	
	public void actionPerformed(ActionEvent e) {
		_ui.generateLevel(_level);
		
	}

	

}