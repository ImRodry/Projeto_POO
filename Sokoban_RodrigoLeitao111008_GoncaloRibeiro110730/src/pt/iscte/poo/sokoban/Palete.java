package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Point2D;

public class Palete extends Movable {
	public Palete(Point2D point2D) {
		super(point2D, 1);
	}

	@Override
	public void interactWithHole(Buraco hole) {
		hole.setCovered(true);
		setTransposable(true);
	}
}
