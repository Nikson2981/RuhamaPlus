package blu3.ruhamaplus.mixin.mixins;

import blu3.ruhamaplus.module.*;
import blu3.ruhamaplus.module.modules.combat.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Objects;

@Mixin(value = Entity.class)
public abstract class MixinEntity {

    public MixinEntity() {
        super();
    }

    @Shadow public double motionX;
    @Shadow public double motionY;
    @Shadow public double motionZ;

    @Shadow public void move(MoverType type, double x, double y, double z) {}

    @Redirect(method = "applyEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void velocity(Entity entity, double x, double y, double z) {
       if (((EnhancedMovement) Objects.requireNonNull(ModuleManager.getModuleByName("EnhancedMovement"))).collision(entity)) {
            return;
        }

        entity.motionX += x;
        entity.motionY += y;
        entity.motionZ += z;

        entity.isAirBorne = true;
    }

}
