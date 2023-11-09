package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Vazio extends GameElement {
	
	public Vazio(Point2D point2D){
		super(point2D);
	}

	@Override
	public int getLayer() {
		return 0;
	}

}
