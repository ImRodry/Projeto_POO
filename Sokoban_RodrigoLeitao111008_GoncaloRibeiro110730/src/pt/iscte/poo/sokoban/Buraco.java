package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Point2D;

public class Buraco extends GameElement {

	private boolean isProtected;
	
	public Buraco(Point2D point2D){
		super(point2D, 0, true);
		this.isProtected = false;
	}

	public boolean isProtected() {
		return isProtected;
	}

	public void setProtected(boolean isProtected) {
		this.isProtected = isProtected;
	}
}
