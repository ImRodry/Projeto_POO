package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Point2D;

public class Alvo extends GameElement {
	private boolean filled = false;
	
	public Alvo(Point2D point2D){
		super(point2D, 1, true);
	}

	public boolean isFilled() {
		return filled;
	}

	public void setFilled(boolean filled) {
		this.filled = filled;
		if (filled)
			GameEngine.getInstance().checkEnd();
	}
}
