package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public abstract class Movable extends GameElement {
	public Movable(Point2D position, int layer) {
		this(position, layer, true);
	}

	public Movable(Point2D position, int layer, boolean special) {
		super(position, layer, false, special);
	}

	public boolean move(Direction dir) {
		Point2D newPosition = getPosition().plus(dir.asVector());
		GameEngine engine = GameEngine.getInstance();
		if (engine.isWithinBounds(newPosition) && canMoveTo(newPosition)) {
			GameElement special = engine.getSpecialIn(newPosition);
			// Will move regardless of implementation
			setPosition(newPosition);
			if (special instanceof Interactable)
				((Interactable) special).interact(this);
			return true;
		}
		return false;
	}

	public boolean canMoveTo(Point2D position) {
		for (GameElement g : GameEngine.getInstance().getElementsIn(position)) {
			if (!g.isTransposable())
				return false;
		}
		return true;
	}

	public abstract void interactWithHole(Buraco hole);
}
