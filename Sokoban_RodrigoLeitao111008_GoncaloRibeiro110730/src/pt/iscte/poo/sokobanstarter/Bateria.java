package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Bateria extends Consumable {
	
	public Bateria(Point2D point2D){
		super(point2D, 1);
	}
	
	@Override
	public void consume(Empilhadora e) {
		super.consume(e);
		e.consumeBattery();	
	}
}
