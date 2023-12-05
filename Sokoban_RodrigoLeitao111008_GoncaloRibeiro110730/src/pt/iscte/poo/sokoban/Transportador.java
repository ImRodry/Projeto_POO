package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Point2D;

public class Transportador extends Interactable {
	public Transportador(Point2D position) {
		super(position, 1);
	}

	@Override
	public void interact(Movable m) {
		Point2D pos = new Point2D((int) Math.floor(Math.random() * 10), (int) Math.floor(Math.random() * 10));
		while (!m.canMoveTo(pos)) {
			pos = new Point2D((int) Math.floor(Math.random() * 10), (int) Math.floor(Math.random() * 10));
		}
		m.setPosition(pos);
	}
}
