package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public abstract class Movable extends GameElement {

	public Movable(Point2D position, int layer) {
		super(position, layer, false);
	}

	public boolean move(Direction dir) {
		Point2D newPosition = getPosition().plus(dir.asVector());
		if (GameEngine.getInstance().isWithinBounds(newPosition) && canMoveTo(dir)) {
			setPosition(newPosition);
			return true;
		}
		return false;
	}

	public boolean canMoveTo(Direction d) {
		for (GameElement g : GameEngine.getInstance().getElementsIn(getPosition().plus(d.asVector()))) {
			if (!g.isTransposable())
				return false;
		}
		return true;
	}
}
