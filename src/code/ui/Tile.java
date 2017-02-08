package code.ui;

import java.util.ArrayList;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Tile extends JButton{
	
	private int _xCoord;
	private int _yCoord;
	
	private Tile _above = null;
	private Tile _below = null;
	private Tile _left = null;
	private Tile _right = null;

	public Tile(Tile tile) {
		_xCoord = tile.getXCoord();
		_yCoord = tile.getYCoord();
		this.setIcon(tile.getIcon());
	}
	
	public Tile(int x, int y){	
		_xCoord = x;
		_yCoord = y;
	}

	public void setNeighbors(ArrayList<ArrayList<Tile>> buttonArray){
		if(_xCoord > 0){
			_left = buttonArray.get(_yCoord).get(_xCoord - 1);
		}if(_xCoord < buttonArray.size() - 1){
			_right = buttonArray.get(_yCoord).get(_xCoord + 1);
		}if(_yCoord > 0){
			_above = buttonArray.get(_yCoord - 1).get(_xCoord);
		}if(_yCoord < buttonArray.size() - 1){
			_below = buttonArray.get(_yCoord + 1).get(_xCoord);
		}
	}
	
	public Tile getAbove(){
		return _above;
	}
	
	public Tile getBelow(){
		return _below;
	}
	
	public Tile getLeft(){
		return _left;
	}
	
	public Tile getRight(){
		return _right;
	}
	
	public int getXCoord(){
		return _xCoord;
	}
	
	public int getYCoord(){
		return _yCoord;
	}
}
