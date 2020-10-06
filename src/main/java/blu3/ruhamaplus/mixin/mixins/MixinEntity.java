package blu3.ruhamaplus.mixin.mixins;

import blu3.ruhamaplus.module.ModuleManager;
import blu3.ruhamaplus.module.modules.combat.EnhancedMovement;
import blu3.ruhamaplus.module.modules.misc.StashFinder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(value = Entity.class)
public class MixinEntity {
    // Inject.
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

    @Shadow
    public void move(MoverType type, double x, double y, double z)
    {

    }

    @Shadow
    public double motionX;

    @Shadow
    public double motionY;

    @Shadow
    public double motionZ;

}
