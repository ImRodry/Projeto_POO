package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Point2D;

public class ParedeRachada extends Consumable {

	public ParedeRachada(Point2D point2D) {
		super(point2D, 1);
	}

	public void consume(Empilhadora e) {
		if (!canConsume(e))
			throw new IllegalStateException("Cannot consume without hammer");
		super.consume(e);
	}

	@Override
	public boolean canConsume(Empilhadora e) {
		return e.getHammer();
	}
}
