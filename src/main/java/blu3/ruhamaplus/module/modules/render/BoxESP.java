package blu3.ruhamaplus.module.modules.render;
import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;
import java.util.List;
public class BoxESP extends Module {
    public BoxESP() { super("BoxESP", 0, Category.RENDER, "now THAAAATS a lotta players", null); }
    public void onRender() {
            List<EntityPlayer> players = new ArrayList<>(this.mc.world.playerEntities);
            players.remove(this.mc.player);
            for (EntityPlayer e : players) RenderUtils.drawFilledBlockBox(e.getEntityBoundingBox(), 0.0F, 1.0F, 1.0F, 0.3F); }}