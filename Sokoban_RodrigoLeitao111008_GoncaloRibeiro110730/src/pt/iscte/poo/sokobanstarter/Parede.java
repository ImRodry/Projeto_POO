package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Parede extends GameElement {
	
	public Parede(Point2D point2D){
		super(point2D);
	}

	@Override
	public int getLayer() {
		return 0;
	}

}
