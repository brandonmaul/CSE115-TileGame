package code.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import code.ui.*;

public class Model{
	
	private UI _ui; //UI reference
	private int _level; //this was going to be used to implement a leveling system. But ran out of time having to study for other finals.. maybe in Version 2 ;) 
						//because the game's already built around using this variable i've decided to keep it.
	
	private ArrayList<String> _imageFileNames; //array list of image file names from /images
	private ArrayList<ImageIcon> _imageIcons; //the file names are made into image icons and added to this array 
	private ArrayList<Tile> _deathRowArray = new ArrayList<Tile>(); //the array of the tiles that will be 'removed' from the board. Excuse the name ;) 
	
	private ArrayList<ArrayList<Tile>> _modelBoard; //essentially a copy of the UI board that the model holds internally to run checks. Important for 'hasPossibleMoves'
	
	private Tile _swapButton1 = null; //swap buttons that the user will select
	private Tile _swapButton2 = null;
	
	private double _score = 0.0; //its over 9000! ... no it's not
	
	
	public Model(UI ui, int level){ //initializes the model
		_ui = ui;
		_level = level;
		_modelBoard = _ui.getBoard();
		_imageFileNames = new ArrayList<String>();
		_imageIcons = new ArrayList<ImageIcon>();
		
		initModel();
	}
	
	private void initModel(){ //adds the image icons to the array and sets them
		for(int i=0; i<6; i++){
			_imageFileNames.add("Images/Tile-"+ i +".png");
		}
		Iterator<String> iter = _imageFileNames.iterator();
		while(iter.hasNext()){
			_imageIcons.add(new ImageIcon(iter.next()));
		}
		
		_ui.initUIButtons(new TileListener(this), _level);
		
		setImageIcons();
		
		while(winCheck(_ui.getBoard()) || !hasPossibleMove(false)){
			setImageIcons();
		}
	}
	
	private void setImageIcons(){ //randomly sets the image icons of tiles in the model board
		for(ArrayList<Tile> array : _modelBoard){
			for(Tile button : array){
				button.setIcon(_imageIcons.get(new Random().nextInt(_imageIcons.size())));
			}
		}
	}
	
	public void removeTilesAndUpdateBoard() { //calls methods to remove tiles and checks to see if theres an additional winner after the board has updated
		removeWinningTiles();
		dropDown();
		
		if(winCheck(_ui.getBoard())){
			System.out.println("Combo!");
			System.out.println();
			removeTilesAndUpdateBoard();
		}
		
		if(!hasPossibleMove(false)){	//also checks if any possible moves exist. if not - end game.
			_ui.deactivateBoard();
		}
	}
	
	public void setSwapButtons(Tile button){ //controls the references in Model to match the user-selected buttons
		if(_swapButton1 == null & _swapButton2 == null){
			_swapButton1 = _ui.selectButton(button);
		}else if(_swapButton1 != null & _swapButton2 == null){
			_swapButton2 = _ui.selectButton(button);
			if(_swapButton1 == _swapButton2){
				System.out.println("You selected the same tile twice.");
				_swapButton1 = _ui.deselectButton(_swapButton1);
				_swapButton2 = _ui.deselectButton(_swapButton2);
			}else if(isAdjacent(_swapButton1, _swapButton2)){
				swap(_swapButton1, _swapButton2);
				swapCheckMatchAndExecute();
			}else{
				System.out.println("Those tiles are not adjacent.");
				_swapButton1 = _ui.deselectButton(_swapButton1);
				_swapButton2 = _ui.deselectButton(_swapButton2);
			}
		}
	}
	
	private void swapCheckMatchAndExecute(){ //calls 'swap' 'wincheck' and 'removeTilesAndUpdateBoard' to determine if the swapped tiles result in a match
		if(_swapButton1 == _swapButton2){
			System.out.println("You selected the same tile twice.");
		}else{
			if(winCheck(_ui.getBoard())){
				removeTilesAndUpdateBoard();
			}else{
				swap(_swapButton1, _swapButton2);
				System.out.println("Those tile are adjacent but will not result in a match.");
			}
		}
		_swapButton1 = _ui.deselectButton(_swapButton1);
		_swapButton2 = _ui.deselectButton(_swapButton2);
		hasPossibleMove(false);
	}
	
	public boolean isAdjacent(Tile b1, Tile b2){ //checks to see if the two selected tiles are next to each other or not
		if(b1 == b2.getAbove() | b1 == b2.getBelow() | b1 == b2.getLeft() | b1 == b2.getRight()){
			return true;
		}else{
			return false;
		}
	}
	
	public void swap(Tile button1, Tile button2){ //simply swaps the icons of two tiles
		Icon temp;
		temp = button1.getIcon();
		button1.setIcon(button2.getIcon());
		button2.setIcon(temp);
		
	}
	
	private boolean winCheck(ArrayList<ArrayList<Tile>> board){ //returns true if the board has a match
		boolean bool = false;
		for(ArrayList<Tile> array : board){
			for(Tile tile : array){
				if(matchingY(tile) | matchingX(tile)){
					bool = true;
				}
			}
		}
		return bool;
	}
	
	private void removeWinningTiles(){ //removes tiles that match from the grid, and puts them on death row
		for(ArrayList<Tile> array : _ui.getBoard()){
			for(Tile tile : array){
				if(matchingY(tile) == true){
					putOnDeathRow(tile.getAbove(), tile, tile.getBelow());
				}if(matchingX(tile) == true){
					putOnDeathRow(tile.getLeft(), tile, tile.getRight());
				}
			}
		}
		scorer();
		_ui.removeTiles(_deathRowArray);
		_deathRowArray.clear();
	}
	
	private void putOnDeathRow(Tile a, Tile b, Tile c){ //adds the winning tiles to the death row array
		ArrayList<Tile> temp = new ArrayList<Tile>();
		temp.add(a);
		temp.add(b);
		temp.add(c);
		for(Tile tile : temp){
			if(!_deathRowArray.contains(tile)) {
				_deathRowArray.add(tile);
			}
		}
		temp = null;
	}
	
	private void dropDown(){ //causes tiles to 'drop' down or generate new tiles from the top
		outerloop:
		for(ArrayList<Tile> array : _ui.getBoard()){
			for(Tile tile : array){
				if(tile.getIcon() == null && tile.getYCoord() == 0){	
					tile.setIcon(_imageIcons.get(new Random().nextInt(_imageIcons.size())));
					dropDown();
					break outerloop;
				}else if(tile.getIcon() == null && tile.getYCoord() != 0){
					tile.setIcon(tile.getAbove().getIcon());
					tile.getAbove().setIcon(null);
					dropDown();
					break outerloop;
				}
			}
		}
	}
	
	private void scorer(){ //keeps track of score in the Model
		_score = _score + _deathRowArray.size();
		_ui.printScore(_deathRowArray.size(), _score);
		
		
	}

	public boolean hasPossibleMove(boolean input){
		
		_modelBoard = _ui.copyBoard();
		ArrayList<ArrayList<Tile>> backup = _ui.copyBoard();
		
		int y = 0;
		int x = 0;
		boolean bool = false;
		
		outerForLoop:
		for(int i = 0; i < _modelBoard.size(); i++){
			for(int j = 0; j < _modelBoard.get(i).size(); j++){
				_modelBoard = backup;
				Tile tile = _modelBoard.get(i).get(j);
				
				if(tile.getAbove() != null){
					swap(tile, tile.getAbove());
					if(winCheck(_modelBoard)){
						bool = true;
						y = i;
						x = j;
						break outerForLoop;
					}else{
						swap(tile, tile.getAbove());
						_modelBoard = backup;
					}
				}
				
				if(tile.getBelow() != null){
					swap(tile, tile.getBelow());
					if(winCheck(_modelBoard)){
						bool = true;
						y = i;
						x = j;
						break outerForLoop;
					}else{
						swap(tile, tile.getBelow());
						_modelBoard = backup;
					}
				}
				
				if(tile.getLeft() != null){
					swap(tile, tile.getLeft());
					if(winCheck(_modelBoard)){
						bool = true;
						y = i;
						x = j;
						break outerForLoop;
					}else{
						swap(tile, tile.getLeft());
						_modelBoard = backup;
					}
				}
				
				if(tile.getRight() != null){
					swap(tile, tile.getRight());
					if(winCheck(_modelBoard)){
						bool = true;
						y = i;
						x = j;
						break outerForLoop;
					}else{
						swap(tile, tile.getRight());
						_modelBoard = backup;
					}
				}
			}
		}
		
		if(input & bool){
			System.out.println();
			System.out.println("A match is possible!");
			System.out.println();
			
			_ui.highlightButton(y, x);
				
		}
		
		return bool;
	}
	
	private boolean matchingY(Tile t){
		if(t.getAbove() != null & t.getBelow() != null){
			if(t.getIcon() == t.getAbove().getIcon() & t.getIcon() == t.getBelow().getIcon()){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	private boolean matchingX(Tile t){
		if(t.getLeft() != null & t.getRight() != null){
			if(t.getIcon() == t.getLeft().getIcon() & t.getIcon() == t.getRight().getIcon()){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
}
