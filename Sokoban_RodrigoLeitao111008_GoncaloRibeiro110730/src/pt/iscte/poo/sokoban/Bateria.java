package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Point2D;

public class Bateria extends Consumable {
	
	public Bateria(Point2D point2D){
		super(point2D, 1);
	}
	
	@Override
	public boolean consume(Empilhadora e) {
		e.consumeBattery();
		return super.consume(e);
	}
}
