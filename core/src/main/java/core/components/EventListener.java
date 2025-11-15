package core.components;

import api.DiaEntityPool;
import core.EventPool;

/**
 * EventListener
 *
 * @author Santiago Barreiro
 */
public class EventListener implements EventPool.EventObserver {

    public Enum<?> event;

    @Override
    public void onEvent(EventPool.Event event) {

    }
}
