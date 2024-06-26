package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Point2D;

public class Chao extends GameElement {
	public Chao(Point2D point2D) {
		super(point2D, 0, true);
	}

	@Override
	public String getName() {
		if (GameEngine.getInstance().hasEasterEgg())
			return "Relva";
		return super.getName();
	}
}
