package core.simpleEntities;

import api.DiaEntityPool;
import api.DiaSystem;

import java.util.*;

/**
 * SimpleEntities
 * A simple and naive implementation of an entity architecture. Made for testing and benchmarking purposes
 *
 * @author Santiago Barreiro
 */
public class SimpleEntities implements DiaEntityPool {

    public static class Entity {
        // ATTRIBUTES
        protected final int id;
        protected Object[] components;
        protected ArrayList<Class<?>> archetype;

        // CONSTRUCTORS
        public Entity(int id, Class<?>[] archetype, Object[] components) {
            this.id = id;
            this.archetype = new ArrayList<>(Arrays.stream(archetype).toList());
            this.components = components;
        }

        public Object[] components() {
            return components;
        }
    }

    public static class QueryResult implements DiaQueryResult<Entity> {
        @Override
        public Iterator<Entity> iterator() {
            return null;
        }
    }

    public static class SimpleEntitiesIterator implements Iterator<Entity> {

        List<Entity> entities;
        private int iterated = 0;
        private final int count;

        public SimpleEntitiesIterator(Collection<Entity> entities) {
            this.entities = entities.stream().toList();
            this.count = entities.size();
        }

        @Override
        public boolean hasNext() {
            return iterated < count;
        }

        @Override
        public Entity next() {
            Entity e = entities.get(iterated);
            iterated++;
            return e;
        }
    }

    // ATTRIBUTES
    private int count;
    private final HashMap<Integer, Entity> entities;
    private final ArrayList<DiaSystem> systems;

    // CONSTRUCTORS
    public SimpleEntities() {
        this.entities = new HashMap<>();
        this.systems = new ArrayList<>();
    }

    // METHODS
    @Override
    public void init() {
        count = 0;
    }

    @Override
    public int createEntity(Object[] components) {
        Class<?>[] archetype = new Class[components.length];
        for (int i = 0; i < components.length; i++) archetype[i] = components[i].getClass();
        entities.put(count, new Entity(count, archetype, components));
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
    public Object[] retrieveEntity(int entity) {
        Entity e = entities.get(entity);
        if (e != null) return e.components;
        return null;
    }

    public SimpleEntitiesIterator queryEntitiesWith(Class<?>[] types) {
        return new SimpleEntitiesIterator(entities.values());
    }

    @Override
    public void scheduleSystem(DiaSystem system)     {
        systems.add(system);
    }

    @Override
    public void unregisterSystem(DiaSystem system) {
        systems.remove(system);
    }

    @Override
    public void dispatchSystems(float dt) {
        for (DiaSystem sys : systems) {
           sys.execute(this, dt);
        }
    }
}
