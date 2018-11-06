package com.sebastiaanyn.eventhandler;

/**
 * Represents an event
 *
 * @author SebastiaanYN
 */
public abstract class Event {

    private final String eventName;

    /**
     * Default constructor. Uses {@link Class#getSimpleName()} as the name
     */
    protected Event() {
        this.eventName = this.getClass().getSimpleName();
    }

    /**
     * Default constructor
     *
     * @param eventName The name of the event
     */
    protected Event(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Get the name of the event
     *
     * @return The name of the event
     */
    public String getEventName() {
        return eventName;
    }
}
