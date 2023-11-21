package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Point2D;

public class Martelo extends Consumable {

	public Martelo(Point2D point2D){
		super(point2D, 1);
	}
	
	@Override
	public void consume(Empilhadora e) {
		e.consumeHammer();
		super.consume(e);
	}
}
