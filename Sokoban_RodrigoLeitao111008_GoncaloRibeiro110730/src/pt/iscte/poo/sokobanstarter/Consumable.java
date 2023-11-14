package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public abstract class Consumable extends GameElement {

	public Consumable(Point2D position, int layer) {
		super(position, layer, false);
	}
	
	public boolean consume(Empilhadora e) {
		GameEngine.getInstance().remove(this);
		return true;
	}
}
