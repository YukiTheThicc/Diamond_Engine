package core;

import core.exceptions.DiamondCriticalException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * EventPool self-contained class that implements a simple event system. It handles evens by types represented by abstract
 * enums. Events are cascaded until caught.
 *
 * @author Santiago Barreiro
 */
public class EventPool {

    /**
     * Secondary class that represents an event. Includes an abstract enum type and a payload
     */
    public static class Event {

        // ATTRIBUTES
        public Enum<?> type;
        public Object payload;
        public boolean caught = false;

        // CONSTRUCTORS
        public Event(Enum<?> type, Object payload) {
            this.type = type;
            this.payload = payload;
        }

        public Event(Enum<?> type) {
            this.type = type;
            this.payload = null;
        }

        public Event() {
            this.type = null;
            this.payload = null;
        }
    }

    /**
     * Interface for event observers
     */
    public interface EventObserver {
        void onEvent(Event event);
    }

    // ATTRIBUTES
    private static ArrayList<Event> eventStack;
    private static HashMap<Enum<?>, ArrayList<EventObserver>> observers;
    private static boolean initialized = false;

    // METHODS

    /**
     * Initializes the event system.
     */
    public static void init() {
        eventStack = new ArrayList<>();
        observers = new HashMap<>();
        initialized = true;
    }

    /**
     * Registers an observer for a specific event type
     * @param eventType Event type to register the observer to
     * @param observer The observer to register
     */
    public static void addObserver(Enum<?> eventType, EventObserver observer) {
        if (!initialized) throw new DiamondCriticalException(EventPool.class, "Tried to add event observer while EventPool is not initialized");
        ArrayList<EventObserver> eventObservers = observers.computeIfAbsent(eventType, k -> new ArrayList<>());
        eventObservers.add(observer);
    }

    /**
     * Un-registers an observer from the observer list so it stops being notified of events.
     * @param observer Observer to remove
     */
    public static void removeObserver(EventObserver observer) {
        if (!initialized) throw new DiamondCriticalException(EventPool.class, "Tried to remove event observer while EventPool is not initialized");
        for (ArrayList<EventObserver> eventObservers : observers.values()) {
            eventObservers.remove(observer);
        }
    }

    /**
     * Registers an event on the system
     * @param event New event to register
     */
    public static void throwEvent(Event event) {
        if (!initialized) throw new DiamondCriticalException(EventPool.class, "Tried to throw event while EventPool is not initialized");
        eventStack.add(event);
    }

    /**
     * Dispatches all currently stacked events. Unmanaged events are not kept between dispatches. Notifies registered observers
     * on the same type of event.
     */
    public static void dispatchEvents() {
        if (!initialized) throw new DiamondCriticalException(EventPool.class, "Tried to dispatch events while EventPool is not initialized");
        for (Event event : eventStack) {
            for (EventObserver observer : observers.get(event.type)) {
                observer.onEvent(event);
                if (event.caught) break;
            }
        }
        eventStack.clear();
    }
}
