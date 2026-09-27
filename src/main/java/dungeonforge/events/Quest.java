package dungeonforge.events;

public class Quest {

    public String getId() { return id; }

    public String getDescription() { return description; }

    public EventType getTrigger() { return trigger; }

    public int getRequired() { return required; }

    public int getProgress() { return progress; }

    private final String id;
    private final String description;
    private final EventType trigger;
    private final int required;
    private int progress;

    public Quest(String id, String description, EventType trigger, int required) {
        this.id = id;
        this.description = description;
        this.trigger = trigger;
        this.required = required;
    }

    public boolean isComplete() {
        return progress >= required;
    }

    public boolean advance() {
        if(isComplete()) return  false;
        progress++;
        return isComplete();
    }

    @Override
    public String toString() {
        return (isComplete() ? "[X] " : "[ ] ") + description +
                " (" + Math.min(progress, required) + "/" + required + ")";
    }
}