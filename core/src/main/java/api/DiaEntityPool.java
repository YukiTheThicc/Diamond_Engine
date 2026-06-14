package api;

import core.simpleEntities.SimpleEntities;

import java.util.Iterator;

/**
 * DiaEntityArchitecture
 *
 * @author Santiago Barreiro
 */
public interface DiaEntityPool {

    interface DiaQueryResult<T> extends Iterable<T>{
        Iterator<T> iterator();
    }

    void init();

    int createEntity(Object[] components);

    boolean deleteEntity(int entity);

    Object[] retrieveEntity(int entity);

    SimpleEntities.SimpleEntitiesIterator queryEntitiesWith(Class<?>[] components);

    void scheduleSystem(DiaSystem system);

    void unregisterSystem(DiaSystem system);

    void dispatchSystems(float dt);
}
