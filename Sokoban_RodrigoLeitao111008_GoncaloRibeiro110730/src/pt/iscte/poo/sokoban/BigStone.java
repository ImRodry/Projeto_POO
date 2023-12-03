package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Point2D;

public class BigStone extends Movable {
    public BigStone(Point2D position) {
        super(position, 0);
    }

    @Override
    public void interactWithHole(Buraco hole) {
        GameEngine.getInstance().remove(hole);
    }
}
