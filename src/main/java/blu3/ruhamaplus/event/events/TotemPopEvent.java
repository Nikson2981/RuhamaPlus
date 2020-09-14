package blu3.ruhamaplus.event.events;

import blu3.ruhamaplus.event.Event;
import net.minecraft.entity.Entity;

public class TotemPopEvent extends Event {

    private Entity entity;

    public TotemPopEvent(Entity entity) {
        super();
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

}