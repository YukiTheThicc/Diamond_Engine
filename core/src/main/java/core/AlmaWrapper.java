package core;

import alma.AlmaPool;
import api.DiaEntityPool;
import api.DiaSystem;

import java.util.Iterator;

/**
 * Alma
 *
 * @author Santiago Barreiro
 */
public class AlmaWrapper implements DiaEntityPool {

    AlmaPool alma = null;

    @Override
    public void init() {
        alma =new AlmaPool();
    }

    @Override
    public int createEntity(Object[] components) {
        return alma.createEntity(components);
    }

    @Override
    public boolean deleteEntity(int entity) {
        return false;
    }

    @Override
    public Object[] retrieveEntity(int entity) {
        return new Object[0];
    }

    @Override
    public Iterator<Object> queryEntitiesWith(Class<?>[] components) {
        return null;
    }

    @Override
    public void scheduleSystem(DiaSystem system) {

    }

    @Override
    public void unregisterSystem(DiaSystem system) {

    }

    @Override
    public void dispatchSystems(float dt) {

    }
}
