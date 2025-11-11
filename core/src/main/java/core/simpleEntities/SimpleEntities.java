package core.simpleEntities;

import api.DiaEntityPool;
import api.DiaSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * SimpleEntities
 *
 * @author Santiago Barreiro
 */
public class SimpleEntities implements DiaEntityPool {

    private static class Entity implements IDiaEntity {
        // ATTRIBUTES
        protected final int id;
        protected IDiaComponent[] components;
        protected Class<?>[] archetype;

        // CONSTRUCTORS
        public Entity(int id, Class<?>[] archetype, IDiaComponent[] components) {
            this.id = id;
            this.archetype = archetype;
            this.components = components;
        }
    }

    // ATTRIBUTES
    private int count;
    private final HashMap<Integer, Entity> entities;
    private final HashMap<Class<?>[], ArrayList<Integer>> compositionTree;
    private final ArrayList<DiaSystem> systems;

    // CONSTRUCTORS
    public SimpleEntities() {
        this.entities = new HashMap<>();
        this.compositionTree = new HashMap<>();
        this.systems = new ArrayList<>();
    }

    // METHODS
    @Override
    public void init() {
        count = 0;
    }

    @Override
    public int createEntity(IDiaComponent[] components) {
        Class<?>[] archetype = new Class[components.length];
        entities.put(count, new Entity(count, archetype, components));
        for (int i = 0; i < components.length; i++) archetype[i] = components.getClass();
        ArrayList<Integer> tree = compositionTree.computeIfAbsent(archetype, k -> new ArrayList<>());
        tree.add(count);
        count++;
        return count - 1;
    }

    @Override
    public boolean deleteEntity(int entity) {
        Entity e = entities.get(entity);
        if (e == null) return false;
        else entities.remove(entity);
        return true;
    }

    @Override
    public IDiaComponent[] retrieveEntity(int entity) {
        Entity e = entities.get(entity);
        if (e != null) return e.components;
        return null;
    }

    @Override
    public void registerSystem(DiaSystem system) {
        systems.add(system);
    }

    @Override
    public void unregisterSystem(DiaSystem system) {
        systems.remove(system);
    }

    @Override
    public void dispatchSystems(float dt) {
        for (DiaSystem sys : systems) {
            Class<?>[] archetype = sys.getAffectedComponents();
            for (Entity entity : entities.values()) {
                if (entity.archetype == archetype) {
                    sys.execute(entity.components, dt);
                }
            }
        }
    }
}
