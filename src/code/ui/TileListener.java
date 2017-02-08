package code.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import code.model.Model;

public class TileListener implements ActionListener {

	private Model _model;
	
	public TileListener(Model m){
		_model = m;
	}
	
	public void actionPerformed(ActionEvent e) {
		Tile button = (Tile) e.getSource();
		_model.setSwapButtons(button);
		
	}

	

}