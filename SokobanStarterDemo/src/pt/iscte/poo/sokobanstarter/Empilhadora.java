package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Empilhadora extends GameElement {

	private Point2D position;
	private String imageName;
	
	public Empilhadora(Point2D initialPosition){
	private int energy = 100;
		super(initialPosition);
		position = initialPosition;
		imageName = "Empilhadora_D";
	}
	
	@Override
	public String getName() {
		return imageName;
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public int getLayer() {
		return 2;
	}

	public int getEnergy() {
		return energy;
	}

	public void move(Direction dir) {
		// Move segundo a direcao gerada, mas so' se estiver dentro dos limites
		Point2D newPosition = position.plus(dir.asVector());
		if (GameEngine.getInstance().isWithinBounds(newPosition)) {
			position = newPosition;
			energy--;
		}
	}
}