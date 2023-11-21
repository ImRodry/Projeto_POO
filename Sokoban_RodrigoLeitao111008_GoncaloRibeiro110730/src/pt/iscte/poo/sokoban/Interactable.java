package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Point2D;

public abstract class Interactable extends GameElement {
    private boolean covered = false;

    public Interactable(Point2D position, int layer) {
        super(position, layer, true, true);
    }

    public boolean isCovered() {
        return covered;
    }

    public void setCovered(boolean covered) {
        this.covered = covered;
    }
}
