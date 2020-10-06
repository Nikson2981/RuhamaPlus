package blu3.ruhamaplus.module.modules.chat;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.utils.ClientChat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityStatus;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TotemPopCounter extends Module {

    private static final List<SettingBase> settings = Collections.singletonList(new SettingMode("Mode: ","Client Side", "Public Chat"));

    public TotemPopCounter() { super ("TotemPopCounter", 0, Category.CHAT, "ezzz pops keep running ni-", settings); }

    private HashMap<String, Integer> popList = new HashMap();

    public void throwNewPoppedTotem(Entity e) {
        if(popList == null) {
            popList = new HashMap<>();
        }

        if(popList.get(e.getName()) == null) {
            popList.put(e.getName(), 1);

            if (this.getSettings().get(0).asMode().mode == 0){
                ClientChat.log(e.getName() + " popped " + 1 + " totem");
            } else {
                this.mc.player.sendChatMessage(e.getName() + " popped " + 1 + " totem");
            }
        } else if(!(popList.get(e.getName()) == null)) {
            int popCounter = popList.get(e.getName());
            int newPopCounter = popCounter += 1;
            popList.put(e.getName(), newPopCounter);
            if (this.getSettings().get(0).asMode().mode == 0) {
                ClientChat.log(e.getName() + " popped " + newPopCounter + " totems");
            } else {
                this.mc.player.sendChatMessage(e.getName() + " popped " + newPopCounter + " totems");
            }
        }
    }

    public void onUpdate() {
        if (!(this.mc.player == null) && !(this.mc.world == null)) {
            for (EntityPlayer player : mc.world.playerEntities) {
                if (player.getHealth() <= 0) {
                    if (popList.containsKey(player.getName())) {
                        if (this.getSettings().get(0).asMode().mode == 0) {
                            ClientChat.log(player.getName() + " died after popping " + popList.get(player.getName()) + " totems");
                        } else {
                            this.mc.player.sendChatMessage(player.getName() + " died after popping " + popList.get(player.getName()) + " totems");
                        }
                        popList.remove(player.getName(), popList.get(player.getName()));
                    }
                }
            }
        }
    }

    @Override
    public boolean onPacketRead(Packet<?> packet)
    {
        if (!(this.mc.player == null) && !(this.mc.world == null)){
            if (packet instanceof SPacketEntityStatus){
                SPacketEntityStatus theOneAndOnlyPacket = (SPacketEntityStatus) packet; // had to change the name LMFAO
                if (theOneAndOnlyPacket.getOpCode() == 35) { //anyone care to explain what the other opcodes are?
                    Entity entity = theOneAndOnlyPacket.getEntity(mc.world);
                    this.throwNewPoppedTotem(entity);
                    return false;
                }
            }
        }
        return false;
    }



}
