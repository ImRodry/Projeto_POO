package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public abstract class Movable extends GameElement {

	public Movable(Point2D position, int layer) {
		super(position, layer, false, true);
	}

	public boolean move(Direction dir) {
		Point2D newPosition = getPosition().plus(dir.asVector());
		if (GameEngine.getInstance().isWithinBounds(newPosition) && canMoveTo(dir)) {
			GameElement special = GameEngine.getInstance().getSpecialIn(newPosition);
			// Will move regardless of implementation
			setPosition(newPosition);
			if (special instanceof Buraco)
				interactWithHole((Buraco) special);
			else if (special instanceof Teleporte){
				Point2D p = ((Teleporte) special).getTeleportPair(GameEngine.getInstance().getTeleportes());
				if (p != null)
					moveTo(p);
			}
			return true;
		}
		return false;
	}

	public void moveTo(Point2D p) {
		this.setPosition(p);
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
