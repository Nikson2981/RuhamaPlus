package blu3.ruhamaplus.module.modules.experimental;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.utils.friendutils.FriendManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Keyboard;

import javax.swing.text.JTextComponent;
import java.security.Key;
import java.util.Iterator;
import java.util.List;

public class Anti32kLog extends Module {


    public Anti32kLog() {
        super("Anti32kLog", Keyboard.KEY_NONE, Category.EXPERIMENTAL, "d dsxvgedgf gsfz", null);
    }

    @Override
    public void onUpdate() {

        Iterator entityIter;


        entityIter = this.mc.world.loadedEntityList.iterator();



        while (entityIter.hasNext())
        {
            Entity e =  (Entity) entityIter.next();
            if (e == this.mc.player) continue;
            if (e instanceof EntityPlayer) {
                if (mc.player.getDistance(e) < 10) {
                    check32k((EntityPlayer) e);
                    return;
                }
            }
        }
    }

    private boolean is32k(ItemStack stack) {
        if (stack.getItem() instanceof net.minecraft.item.ItemSword) {
            NBTTagList enchants = stack.getEnchantmentTagList();
            if (enchants != null)
                for (int i = 0; i < enchants.tagCount(); i++) {
                    if (enchants.getCompoundTagAt(i).getShort("lvl") >= Short.MAX_VALUE)
                        return true;
                }
        }
        return false ;
    }

    private void check32k(EntityPlayer e) {

        if (!FriendManager.Get().isFriend(e.getName().toLowerCase())) {
            if (is32k(e.getHeldItemMainhand())) {
                mc.player.sendChatMessage(e.getName() + "Tried to 32k me! Disconnecting...");
                logOut(e.getName() + " tried to 32k you!");
            }
        }
    }

    private void logOut(String reason) {
        this.mc.player.connection.getNetworkManager().closeChannel(new TextComponentString(reason));
        this.setToggled(false);
    }
}
