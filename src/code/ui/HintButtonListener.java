package code.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import code.model.Model;

public class HintButtonListener implements ActionListener {

	private Model _model;
	
	public HintButtonListener(Model m){
		_model = m;
	}
	
	public void actionPerformed(ActionEvent e) {
		_model.hasPossibleMove(true);
		
	}

	

}