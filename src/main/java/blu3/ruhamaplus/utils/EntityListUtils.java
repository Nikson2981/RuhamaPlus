package blu3.ruhamaplus.utils;

import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class EntityListUtils implements Util{

    public static List<Entity> getEntitiesOfType(Class entityBruh){
        List<Entity> entities = new ArrayList<>();
        for (Entity e : mc.world.loadedEntityList){
            if (entityBruh.isInstance(e)){
                entities.add(e);
            }
        }
        return entities;
    }

}
