package pt.iscte.poo.sokoban;

import java.util.ArrayList;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public abstract class Movable extends GameElement {

	public Movable(Point2D position, int layer, boolean transposable) {
		super(position, layer, transposable);
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

	public Buraco isHole(Point2D newPosition) {
		ArrayList<GameElement> list = GameEngine.getInstance().getElementsIn(newPosition);
		for (GameElement e : list) {
			if (e instanceof Buraco && !((Buraco)e).isProtected())
				return (Buraco) e;
		}
		return null;
	}

	public abstract void interactWithHole(Point2D newPosition);
}
