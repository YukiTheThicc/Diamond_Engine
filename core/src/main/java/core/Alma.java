package core;

import alma.AlmaPool;
import alma.api.IComponent;
import api.DiaEntityArchitecture;

/**
 * Alma
 *
 * @author Santiago Barreiro
 */
public class Alma implements DiaEntityArchitecture {

    AlmaPool alma = null;

    @Override
    public void init() {
        alma = AlmaPool.Factory.create();
    }

    @Override
    public void createEntity(IDiaComponent[] components) {
        alma.createEntity((IComponent[]) components);
    }
}
