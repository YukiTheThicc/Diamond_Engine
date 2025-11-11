package core;

import alma.AlmaPool;
import alma.api.IComponent;
import api.DiaEntityPool;
import api.DiaSystem;

/**
 * Alma
 *
 * @author Santiago Barreiro
 */
public class Alma implements DiaEntityPool {

    AlmaPool alma = null;

    @Override
    public void init() {
        alma = AlmaPool.Factory.create();
    }

    @Override
    public int createEntity(IDiaComponent[] components) {
        return alma.createEntity((IComponent[]) components);
    }

    @Override
    public boolean deleteEntity(int entity) {
        return false;
    }

    @Override
    public IDiaComponent[] retrieveEntity(int entity) {
        return new IDiaComponent[0];
    }

    @Override
    public void registerSystem(DiaSystem system) {

    }

    @Override
    public void unregisterSystem(DiaSystem system) {

    }

    @Override
    public void dispatchSystems(float dt) {

    }
}
