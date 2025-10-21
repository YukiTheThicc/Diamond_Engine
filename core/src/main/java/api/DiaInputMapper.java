package api;

import core.InputController;

/**
 * DiaInputMapper
 * @author Santiago Barreiro
 */
public abstract class DiaInputMapper {

    protected InputController inputController;

    public DiaInputMapper(InputController inputController) {
        this.inputController = inputController;
    }

    public void registerKeyAction(int key, int action) {

    }

    public void registerMouseAction() {

    }
}
