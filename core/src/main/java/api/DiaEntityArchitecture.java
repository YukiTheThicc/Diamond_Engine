package api;

import java.util.function.Consumer;

/**
 * DiaEntityArchitecture
 *
 * @author Santiago Barreiro
 */
public interface DiaEntityArchitecture {

    public interface IDiaEntity {

    }

    public interface IDiaComponent {
        IDiaComponent copy();
    }

    public interface IDiaQueryResult {

        IDiaQueryResult forEachEntity(Consumer<IDiaEntity> action);
    }

    void init();

    void createEntity(IDiaComponent[] components);


}
