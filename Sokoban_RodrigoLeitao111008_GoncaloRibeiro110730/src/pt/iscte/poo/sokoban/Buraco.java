package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Point2D;

public class Buraco extends GameElement {

	private boolean covered = false;
	
	public Buraco(Point2D point2D){
		super(point2D, 0, true, true);
	}

	public boolean isCovered() {
		return covered;
	}

	public void setCovered(boolean covered) {
		this.covered = covered;
	}
}
