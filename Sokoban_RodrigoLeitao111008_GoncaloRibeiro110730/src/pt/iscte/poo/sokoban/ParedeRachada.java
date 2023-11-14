package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Point2D;

public class ParedeRachada extends Consumable {

	public ParedeRachada(Point2D point2D) {
		super(point2D, 1);
	}

	public boolean consume(Empilhadora e) {
		if (e.getHammer())
			return super.consume(e);
		else
			return false;
	}
}
