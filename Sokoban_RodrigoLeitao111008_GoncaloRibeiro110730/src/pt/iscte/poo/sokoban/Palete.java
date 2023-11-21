package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Point2D;

public class Palete extends Movable {

	public Palete(Point2D point2D) {
		super(point2D, 1);
	}

	@Override
	public void interactWithHole(Point2D newPosition) {
		if (isHole(newPosition) != null) {
			isHole(newPosition).setProtected(true);
			setLayer(1);
			setTransposable(true);
		}
	}
}
