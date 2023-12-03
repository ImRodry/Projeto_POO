package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Point2D;

public class Buraco extends Interactable {
	public Buraco(Point2D point2D) {
		super(point2D, 0);
	}

	@Override
	public String getName() {
		if (GameEngine.getInstance().hasEasterEgg())
			return "BuracoRelva";
		return super.getName();
	}

	@Override
	public void interact(Movable m) {
		m.interactWithHole(this);
	}
}
