package com.sebastiaanyn.server.eventhandler;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A simple event handler class
 *
 * @author SebastiaanYN
 */
public class EventHandler {

    private final Map<Class<? extends Event>, Multimap<EventPriority, Consumer<Event>>> eventMap = new HashMap<>();
    private final String name;

    /**
     * Create a new event handler
     */
    public EventHandler() {
        this(null);
    }

    /**
     * Create a new event handler
     *
     * @param name The name of the handler
     */
    public EventHandler(String name) {
        this.name = name;
    }

    /**
     * Assign a new listener to an event with {@link EventPriority#NORMAL} as the handler priority
     *
     * @param eventClass The class of the event to listen to
     * @param handler    The handler for the event
     * @param <T>        An event class
     * @return The {@code EventHandler} instance to allow chaining
     */
    public <T extends Event> EventHandler on(Class<T> eventClass, Consumer<T> handler) {
        return on(eventClass, EventPriority.NORMAL, handler);
    }

    /**
     * Assign a new listener to an event
     *
     * @param eventClass The class of the event to listen to
     * @param priority   The priority of the handler
     * @param handler    The handler for the event
     * @param <T>        An event class
     * @return The {@code EventHandler} instance to allow chaining
     */
    @SuppressWarnings("unchecked")
    public <T extends Event> EventHandler on(Class<T> eventClass, EventPriority priority, Consumer<T> handler) {
        Multimap<EventPriority, Consumer<Event>> handlers = getConsumers(eventClass);
        handlers.put(priority, (Consumer<Event>) handler);
        eventMap.put(eventClass, handlers);
        return this;
    }

    /**
     * Assign a set of listeners to a specific event using {@link EventChainer}
     *
     * @param eventClass The class of the event to listen to
     * @param <T>        An event class
     * @return A new {@link EventChainer} instance
     */
    public <T extends Event> EventChainer<T> listen(Class<T> eventClass) {
        return new EventChainer<>(this, eventClass);
    }

    /**
     * Emit an event
     *
     * @param event The event to emit
     */
    public void emit(Event event) {
        Multimap<EventPriority, Consumer<Event>> handlers = eventMap.get(event.getClass());
        if (handlers == null) {
            return;
        }
        for (EventPriority priority : EventPriority.values()) {
            handlers.get(priority).forEach(c -> c.accept(event));
        }
    }

    /**
     * Get all consumers for an event, or create a new {@link Multimap} if not present
     *
     * @param eventClass The class of the event
     * @param <T>        An event class
     * @return A {@link Multimap} with the listeners
     */
    private <T extends Event> Multimap<EventPriority, Consumer<Event>> getConsumers(Class<T> eventClass) {
        Multimap<EventPriority, Consumer<Event>> handlers = eventMap.get(eventClass);
        if (handlers == null) {
            handlers = ArrayListMultimap.create();
        }
        return handlers;
    }

    /**
     * Get the name of the handler
     *
     * @return The name
     */
    public String getName() {
        return name;
    }
}
