package map.utils.events;

import map.domain.Entity;
import map.domain.Prietenie;
import map.domain.Utilizator;

public class EntityChangeEvent implements Event {
    private ChangeEventType type;
    private Entity<Long> data;

    public EntityChangeEvent(ChangeEventType type, Prietenie data) {
        this.type = type;
        this.data = data;
    }
    public EntityChangeEvent(ChangeEventType type, Utilizator data) {
        this.type = type;
        this.data = data;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Entity<Long> getData() {
        return data;
    }

}
