package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Martelo extends Consumable {

	public Martelo(Point2D point2D){
		super(point2D, 1);
	}
	
	@Override
	public boolean consume(Empilhadora e) {
		e.consumeHammer();
		return super.consume(e);
	}
}
