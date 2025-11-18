package alma.architecture;

import alma.Entity;
import alma.archetypes.Archetype;
import alma.archetypes.ArchetypeHash;

import java.util.*;
import java.util.function.Consumer;

/**
 * QueryResult
 *
 * @author Santiago Barreiro
 */
public final class QueryResult {

    // ATTRIBUTES
    private final int[] componentIndex;
    private final Map<ArchetypeHash, Archetype> queriedCompositions;

    // CONSTRUCTORS
    public QueryResult(int[] query, Map<ArchetypeHash, Archetype> queriedCompositions) {
        this.componentIndex = query;
        this.queriedCompositions = queriedCompositions;
    }

    // METHODS
    public Iterator<Entity> getResults() {
        for (Archetype c : queriedCompositions.values()) {
            return c.getPartition().iterator(componentIndex);
        }
        return null;
    }

    public void withState(Enum<?> state) {

    }
}
