package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public abstract class Movable extends GameElement {
	public Movable(Point2D position, int layer) {
		super(position, layer, false, true);
	}

	public boolean move(Direction dir) {
		Point2D newPosition = getPosition().plus(dir.asVector());
		GameEngine engine = GameEngine.getInstance();
		if (engine.isWithinBounds(newPosition) && canMoveTo(dir)) {
			GameElement special = engine.getSpecialIn(newPosition);
			// Will move regardless of implementation
			setPosition(newPosition);
			if (special instanceof Interactable)
				((Interactable) special).interact(this);
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

	public abstract void interactWithHole(Buraco hole);
}
