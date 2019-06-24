package com.spacer.event.model;

public class EventSpaceTrending {
    public EventType event;
    public Space space;

    public EventType getEvent() {
        return event;
    }

    public void setEvent(EventType event) {
        this.event = event;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public EventSpaceTrending() {
    }

    public EventSpaceTrending(EventType event, Space space) {
        this.event = event;
        this.space = space;
    }
}
