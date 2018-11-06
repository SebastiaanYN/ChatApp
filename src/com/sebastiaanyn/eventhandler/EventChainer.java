package com.sebastiaanyn.eventhandler;

import java.util.function.Consumer;

/**
 * An easy way to chain event listener assignment
 *
 * @param <T> An event class
 * @author SebastiaanYN
 */
public class EventChainer<T extends Event> {

    private final EventHandler eventHandler;
    private final Class<T> eventClass;

    /**
     * Create a new {@link EventChainer}
     *
     * @param eventHandler The {@link EventHandler} parent class
     * @param eventClass   The class of the event to use for the chain
     */
    public EventChainer(EventHandler eventHandler, Class<T> eventClass) {
        this.eventHandler = eventHandler;
        this.eventClass = eventClass;
    }

    /**
     * Add a new listener to the event with {@link EventPriority#NORMAL} as the handler priority
     *
     * @param handler The handler for the event
     * @return The {@code EventChainer} instance to allow chaining
     */
    public EventChainer<T> run(Consumer<T> handler) {
        return run(EventPriority.NORMAL, handler);
    }

    /**
     * Add a new listener to the event
     *
     * @param priority The priority of the handler
     * @param handler  The handler for the event
     * @return The {@code EventChainer} instance to allow chaining
     */
    public EventChainer<T> run(EventPriority priority, Consumer<T> handler) {
        eventHandler.on(eventClass, priority, handler);
        return this;
    }

    /**
     * Create a new {@link EventChainer} instance with a different {@code event class}
     *
     * @param eventClass The class of the event to use for the chain
     * @param <O>        An event class
     * @return A new {@link EventChainer} instance
     */
    public <O extends Event> EventChainer<O> and(Class<O> eventClass) {
        return eventHandler.listen(eventClass);
    }
}
