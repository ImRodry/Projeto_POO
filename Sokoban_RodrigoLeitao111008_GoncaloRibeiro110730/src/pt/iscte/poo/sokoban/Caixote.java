package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Point2D;

public class Caixote extends Movable {
	private boolean onTarget = false;
	
	public Caixote(Point2D point2D){
		super(point2D, 2);
	}

	public boolean isOnTarget() {
		return onTarget;
	}

	public void setOnTarget(boolean onTarget) {
		this.onTarget = onTarget;
	}

	@Override
	public String getName() {
		return super.getName() + (onTarget ? "_no_alvo" : "");
	}
}
