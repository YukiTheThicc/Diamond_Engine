package api;

/**
 * DiaSystem
 *
 * @author Santiago Barreiro
 */
public interface DiaSystem {

    Class<?>[] getAffectedComponents();

    void execute(DiaEntityPool.IDiaComponent[] components, float dt);
}
