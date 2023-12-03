package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Point2D;

public class Parede extends GameElement {
	public Parede(Point2D point2D) {
		super(point2D, 0, false);
	}

	@Override
	public String getName() {
		if (GameEngine.getInstance().hasEasterEgg())
			return "ParedeRelva";
		return super.getName();
	}
}
