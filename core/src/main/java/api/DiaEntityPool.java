package api;

import java.util.function.Consumer;

/**
 * DiaEntityArchitecture
 *
 * @author Santiago Barreiro
 */
public interface DiaEntityPool {

    interface IDiaEntity {

    }

    interface IDiaComponent {
        IDiaComponent copy();
    }

    interface IDiaQueryResult {

        IDiaQueryResult forEachEntity(Consumer<IDiaEntity> action);
    }

    void init();

    int createEntity(IDiaComponent[] components);

    boolean deleteEntity(int entity);

    IDiaComponent[] retrieveEntity(int entity);

    void registerSystem(DiaSystem system);

    void unregisterSystem(DiaSystem system);

    void dispatchSystems(float dt);
}
