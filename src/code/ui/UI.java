package code.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.*;

import code.model.Model;

public class UI implements Runnable {
	private Model _model;

	private JFrame _frame;
	
	private JButton _hintButton;
	private JButton _startButton;
	
	private JPanel _container;
	private JPanel _upperPanel;
	private JPanel _lowerPanel;
	
	private ArrayList<ArrayList<Tile>> _board;

	public void run() {
		_board= new ArrayList<ArrayList<Tile>>();
		_frame = new JFrame("Brandon Maul's Lab 11");
		
		
		_startButton = new JButton("      Start      ");
		_startButton.setFont(new Font("American Typewriter", Font.PLAIN, 28));
		_hintButton = new JButton("Hint");
		_hintButton.setFont(new Font("American Typewriter", Font.PLAIN, 28));
		
		_container = new JPanel();
		_upperPanel = new JPanel();
		_lowerPanel = new JPanel();
		
		_container.setLayout(new BoxLayout(_container, BoxLayout.PAGE_AXIS));
		_lowerPanel.setLayout(new FlowLayout());
		
		_lowerPanel.add(_startButton);
		_startButton.addActionListener(new StartButtonListener(this, 1));
		
		
		_container.add(_upperPanel);
		_container.add(_lowerPanel);
		_frame.add(_container);
		
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.pack();
		_frame.setResizable(false);
		_frame.setVisible(true);
		
	}
	
	public void generateLevel(int x){
		_upperPanel.setLayout(new GridLayout(x+4,x+4));
		_model = new Model(this, x+4);
		_lowerPanel.removeAll();
		_lowerPanel.add(_hintButton);
		_hintButton.addActionListener(new HintButtonListener(_model));
		_frame.pack();
	}
	
	public void initUIButtons(TileListener s, int size) {
		for(int y=0; y<size; y++){
			ArrayList<Tile> temp = new ArrayList<Tile>();
			for (int x=0; x<size; x++) {
				Tile button = new Tile(x, y);
				button.addActionListener(s);
				temp.add(button);
			}
			_board.add(new ArrayList<Tile>(temp));
		}
		
		for(ArrayList<Tile> row : _board){
			for(Tile button : row){
				button.setNeighbors(_board);
				button.setOpaque(true);
				_upperPanel.add(button);
			}
		}
	}
	
	public Tile selectButton(Tile button){
		button.setBackground(Color.RED);
		return button;
	}
	
	public Tile deselectButton(Tile button){
		button.setBackground(Color.WHITE);
		return null;
	}
	
	public Tile highlightButton(int i, int j){
		_board.get(i).get(j).setBackground(Color.ORANGE);
		return null;
	}

	
	public ArrayList<ArrayList<Tile>> getBoard() {
		return _board;
	}
	
	public void updateBoard(ArrayList<ArrayList<Tile>> input) {
		_board = input;
	}
	
	public ArrayList<ArrayList<Tile>> copyBoard(){
		ArrayList<ArrayList<Tile>> copy = new ArrayList<ArrayList<Tile>>();
		for(int y = 0; y < _board.size(); y++) {
	        ArrayList<Tile> row = new ArrayList<Tile>();
	        for(int x = 0; x < _board.get(y).size(); x++) {
	        	row.add(new Tile(_board.get(y).get(x)));
	        }
	        copy.add(row);
	    }
		for(ArrayList<Tile> row : copy){
			for(Tile tile : row){
				tile.setNeighbors(copy);
			}
		}
	    return copy;
	}
	
	public void removeTiles(ArrayList<Tile> array){
		for(Tile tile : array){
			tile.setIcon(null);
			tile.setBackground(Color.WHITE);
		}
	}

	public void printScore(int i, double _score) {
		System.out.println("You got " + i + " tiles!");
		System.out.println("Your Score is: " + _score);
		System.out.println();
		
	}

	public void deactivateBoard() {
		for(ArrayList<Tile> row : _board){
			for(Tile button : row){
				button.setEnabled(false);
			}
		}
		
		System.out.println();
		System.out.println("No more matches are available!");
		System.out.println("Game Over!");
		System.out.println();
		
		_hintButton.setText("Game Over!");
	}
	
}
