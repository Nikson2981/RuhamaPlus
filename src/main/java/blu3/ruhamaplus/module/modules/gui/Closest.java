package blu3.ruhamaplus.module.modules.gui;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;
import blu3.ruhamaplus.utils.ClientChat;
import blu3.ruhamaplus.utils.friendutils.FriendManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Closest extends Module {

    private static final List<SettingBase> settings = Arrays.asList(
            new SettingSlider(0.0D, 1920.0D, 400.0D, 10, "X"),
            new SettingSlider(0.0D, 1080.0D, 400.0D, 10, "Y"),
            new SettingToggle(false, "Background"));

    public Closest()
    {
        super("Closest", 0, Category.GUI, "bruhhh", settings);
    }


    List<Entity> knownPlayers = new ArrayList<>();;
    List<Entity> players;

    private HashMap<String, Integer> popList = new HashMap();

    public void onUpdate(){
        if(mc.player == null) return;
        players = mc.world.loadedEntityList.stream().filter(e-> e instanceof EntityPlayer).collect(Collectors.toList());
        try {
            for (Entity e : players) {
                if (e instanceof EntityPlayer && !e.getName().equalsIgnoreCase(mc.player.getName())) {
                    if (!knownPlayers.contains(e)) {
                        knownPlayers.add(e);
                    }
                }
            }
        } catch(Exception e){}
        try {
            for (Entity e : knownPlayers) {
                if (e instanceof EntityPlayer && !e.getName().equalsIgnoreCase(mc.player.getName())) {
                    if (!players.contains(e)) {
                        knownPlayers.remove(e);;
                    }
                }
            }
        } catch(Exception e){}

        if (!(this.mc.player == null) && !(this.mc.world == null)) {
            for (EntityPlayer player : mc.world.playerEntities) {
                if (player.getHealth() <= 0) {
                    if (popList.containsKey(player.getName())) {
                        popList.remove(player.getName(), popList.get(player.getName()));
                    }
                }
            }
        }
    }


    public void onOverlay() {



        int x = (int) this.getSetting(0).asSlider().getValue();
        int y = (int) this.getSetting(1).asSlider().getValue();
        if (!knownPlayers.isEmpty()) {

            EntityPlayer l_Player = null;

            try {
                List<Entity> players = new ArrayList<>(this.mc.world.playerEntities);
                players.remove(this.mc.player);
                players.sort((a, b) -> Float.compare(a.getDistance(this.mc.player), b.getDistance(this.mc.player)));

                if (players.get(0).getDistance(this.mc.player) < this.getSetting(0).asSlider().getValue()) {
                    l_Player = (EntityPlayer) players.get(0);
                }
            } catch (Exception ignored) {
            }

            //l_Player = this.mc.player;
            int p = -1;
            if (this.mc.getConnection() == null || this.mc.getConnection().getPlayerInfo(l_Player.getName()) == null) {
                p = -1;
            } else {
                p = this.mc.getConnection().getPlayerInfo(l_Player.getName()).getResponseTime();
            }
            final String Name = l_Player.getName();
            final int Healt = Math.round(l_Player.getHealth() + l_Player.getAbsorptionAmount());
            final String Health = "" + Healt;
            final int Distanc = (int) this.mc.player.getDistance((Entity) l_Player);
            final InventoryPlayer items = l_Player.inventory;
            final ItemStack inHand = l_Player.getHeldItemMainhand();
            final ItemStack boots = items.armorItemInSlot(0);
            final ItemStack leggings = items.armorItemInSlot(1);
            final ItemStack body = items.armorItemInSlot(2);
            final ItemStack helm = items.armorItemInSlot(3);
            final ItemStack offHand = l_Player.getHeldItemOffhand();
            final String Distance = "" + Distanc;
            final String Ping = "" + p;
            final String helm2 = helm.getItem().getItemStackDisplayName(helm);
            final String body2 = body.getItem().getItemStackDisplayName(body);
            final String leggings2 = leggings.getItem().getItemStackDisplayName(leggings);
            final String boots2 = boots.getItem().getItemStackDisplayName(boots);

            if(this.getSetting(2).asToggle().state) GuiScreen.drawRect(x - 20, y - 65, x + 135, y + 25, 1879048192);

            mc.fontRenderer.drawStringWithShadow(ChatFormatting.AQUA + Name, x + 20, y - 55, 0);
            if (FriendManager.Get().isFriend(l_Player.getName().toLowerCase())) {
                mc.fontRenderer.drawStringWithShadow(ChatFormatting.AQUA + "Verified Cool" + ChatFormatting.WHITE + " | " + Ping + "ms" + " | " + Distance + "m", x + 20, y - 45, 0);
            } else if (helm2.equals("Diamond Helmet") && body2.equals("Diamond Chestplate") && leggings2.equals("Diamond Leggings") && boots2.equals("Diamond Boots")) {
                mc.fontRenderer.drawStringWithShadow(ChatFormatting.RED + "Potential Threat" + ChatFormatting.WHITE + " | " + Ping + "ms" + " | " + Distance + "m", x + 20, y - 45, 0);
            } else if (helm2.equals("Chain Helmet") && body2.equals("Chain Chestplate") && leggings2.equals("Chain Leggings") && boots2.equals("Chain Boots") && this.mc.getCurrentServerData().serverIP.contains("endcrystal")) {
                mc.fontRenderer.drawStringWithShadow(ChatFormatting.RED + "Netherite Retard" + ChatFormatting.WHITE + " | " + Ping + "ms" + " | " + Distance + "m", x + 20, y - 45, 0);
            } else if (helm2.equals("Diamond Helmet") && body2.equals("Elytra") && leggings2.equals("Diamond Leggings") && boots2.equals("Diamond Boots")) {
                mc.fontRenderer.drawStringWithShadow(ChatFormatting.YELLOW + "Wasp" + ChatFormatting.WHITE + " | " + Ping + "ms" + " | " + Distance + "m", x + 20, y - 45, 0);
            } else if (helm2.equals("Air") && body2.equals("Air") && leggings2.equals("Air") && boots2.equals("Air")) {
                mc.fontRenderer.drawStringWithShadow(ChatFormatting.GREEN + "Naked" + ChatFormatting.WHITE + " | " + Ping + "ms" + " | " + Distance + "m", x + 20, y - 45, 0);
            } else {
                mc.fontRenderer.drawStringWithShadow(ChatFormatting.LIGHT_PURPLE + "Newfag" + ChatFormatting.WHITE + " | " + Ping + "ms" + " | " + Distance + "m", x + 20, y - 45, 0);
            }
            int colour = 0;
            try {
                colour = l_Player.getHealth() + l_Player.getAbsorptionAmount() > 20.0F ? 2158832 : MathHelper.hsvToRGB((l_Player.getHealth() + l_Player.getAbsorptionAmount()) / 20.0F / 3.0F, 1.0F, 1.0F);
            } catch (Exception ignored) {
            }
            mc.fontRenderer.drawStringWithShadow(ChatFormatting.AQUA + "Health: " + ChatFormatting.RESET + Health, x + 20, y - 15, colour);


        if(popList.get(l_Player.getName()) == null) {
            mc.fontRenderer.drawStringWithShadow("Pops: 0" , x + 80, y - 15, colour);
        } else if(!(popList.get(l_Player.getName()) == null)) {
            mc.fontRenderer.drawStringWithShadow(ChatFormatting.WHITE + "Pops: " + ChatFormatting.RED + popList.get(l_Player.getName()), x + 80, y - 15, 0);
        }




            int i = 0;
            final List<ItemStack> armor = new ArrayList<ItemStack>();
            for (final ItemStack is : l_Player.getArmorInventoryList()) {
                armor.add(is);
            }
            Collections.reverse(armor);
            for (final ItemStack is : armor) {
                final int yy = (int) (y - 35.0);
                final int xx = (int) (x + i + 16.0);
                RenderHelper.enableGUIStandardItemLighting();
                this.mc.getRenderItem().renderItemAndEffectIntoGUI(is, xx, yy);
                this.mc.getRenderItem().renderItemOverlays(this.mc.fontRenderer, is, xx, yy);
                RenderHelper.disableStandardItemLighting();
                i += 18;
            }
            final int yy2 = (int) (y - 35.0);
            final int xx2 = (int) (x + 90.0);
            RenderHelper.enableGUIStandardItemLighting();
            this.mc.getRenderItem().renderItemAndEffectIntoGUI(inHand, xx2, yy2);
            this.mc.getRenderItem().renderItemOverlays(this.mc.fontRenderer, inHand, xx2, yy2);
            RenderHelper.disableStandardItemLighting();
            final ItemStack inOffHand = l_Player.getHeldItemOffhand();
            final int yyy = (int) (y + -35.0);
            final int xxx = (int) (x + 110.0);
            RenderHelper.enableGUIStandardItemLighting();
            this.mc.getRenderItem().renderItemAndEffectIntoGUI(inOffHand, xxx, yyy);
            this.mc.getRenderItem().renderItemOverlays(this.mc.fontRenderer, inOffHand, xxx, yyy);
            RenderHelper.disableStandardItemLighting();
            final EnchantEntry[] enchants = {new EnchantEntry(Enchantments.PROTECTION, "Pro"), new EnchantEntry(Enchantments.BLAST_PROTECTION, "Bla"), new EnchantEntry(Enchantments.FIRE_PROTECTION, "Fpr"), new EnchantEntry(Enchantments.PROJECTILE_PROTECTION, "Ppr"), new EnchantEntry(Enchantments.UNBREAKING, "Unb"), new EnchantEntry(Enchantments.MENDING, "Men"), new EnchantEntry(Enchantments.AQUA_AFFINITY, "Aqu"), new EnchantEntry(Enchantments.RESPIRATION, "Res"), new EnchantEntry(Enchantments.FEATHER_FALLING, "Fea"), new EnchantEntry(Enchantments.DEPTH_STRIDER, "Dep"), new EnchantEntry(Enchantments.FROST_WALKER, "Fro"), new EnchantEntry(Enchantments.THORNS, "Thr"), new EnchantEntry(Enchantments.SHARPNESS, "Sha"), new EnchantEntry(Enchantments.FIRE_ASPECT, "Fia"), new EnchantEntry(Enchantments.KNOCKBACK, "Knb"), new EnchantEntry(Enchantments.POWER, "Pow"), new EnchantEntry(Enchantments.BINDING_CURSE, "Bin"), new EnchantEntry(Enchantments.SMITE, "Smi"), new EnchantEntry(Enchantments.BANE_OF_ARTHROPODS, "Ban"), new EnchantEntry(Enchantments.LOOTING, "Loo"), new EnchantEntry(Enchantments.SWEEPING, "Swe"), new EnchantEntry(Enchantments.EFFICIENCY, "Eff"), new EnchantEntry(Enchantments.SILK_TOUCH, "Sil"), new EnchantEntry(Enchantments.FORTUNE, "For"), new EnchantEntry(Enchantments.FLAME, "Fla"), new EnchantEntry(Enchantments.LUCK_OF_THE_SEA, "Luc"), new EnchantEntry(Enchantments.LURE, "Lur"), new EnchantEntry(Enchantments.PUNCH, "Pun"), new EnchantEntry(Enchantments.VANISHING_CURSE, "Van")};
            int lolok = 0;
            int lolok2 = 0;
            int lolok3 = 0;
            int lolok4 = 0;
            int lolok5 = 0;
            int lolok6 = 0;
            EnchantEntry[] array;
            for (int length = (array = enchants).length, lolokq = 0; lolokq < length; ++lolokq) {
                final EnchantEntry enchant = array[lolokq];
                final int level = EnchantmentHelper.getEnchantmentLevel(enchant.getEnchant(), helm);
                String levelDisplay = "" + level;
                if (level > 10) {
                    levelDisplay = "10+";
                }
                if (level > 0) {

                    if (enchant.getName().equals("Van")) {
                        //this.mc.fontRenderer.drawStringWithShadow(ChatFormatting.AQUA + enchant.getName() + " " + levelDisplay, x, y, 0);
                    } else {
                        //this.mc.fontRenderer.drawStringWithShadow(ChatFormatting.WHITE + enchant.getName() + " " + levelDisplay, x, y, 0);
                    }
                    lolok += 5;

                }
            }
            for (int length2 = (array = enchants).length, lolok2q = 0; lolok2q < length2; ++lolok2q) {
                final EnchantEntry enchant2 = array[lolok2q];
                final int level2 = EnchantmentHelper.getEnchantmentLevel(enchant2.getEnchant(), body);
                String levelDisplay2 = "" + level2;
                if (level2 > 10) {
                    levelDisplay2 = "10+";
                }
                if (level2 > 0) {
                    if (enchant2.getName().equals("Van")) {
                        //this.mc.fontRenderer.drawStringWithShadow(ChatFormatting.RED + enchant2.getName() + " " + levelDisplay2, x, y, 0);
                    } else {
                        //this.mc.fontRenderer.drawStringWithShadow(ChatFormatting.WHITE + enchant2.getName() + " " + levelDisplay2, x, y, 0);
                    }
                    lolok2 += 5;
                }
            }
            for (int length3 = (array = enchants).length, lolok3q = 0; lolok3q < length3; ++lolok3q) {
                final EnchantEntry enchant3 = array[lolok3q];
                final int level3 = EnchantmentHelper.getEnchantmentLevel(enchant3.getEnchant(), leggings);
                String levelDisplay3 = "" + level3;
                if (level3 > 10) {
                    levelDisplay3 = "10+";
                }

                if (level3 > 0) {
                    if (enchant3.getName().equals("Van")) {
                        //this.mc.fontRenderer.drawStringWithShadow(ChatFormatting.RED + enchant3.getName() + " " + levelDisplay3, 0.0f, (float)(-Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT), 0);
                    } else {
                        //this.mc.fontRenderer.drawStringWithShadow(ChatFormatting.WHITE + enchant3.getName() + " " + levelDisplay3, 0.0f, (float)(-Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT), 0);
                    }
                    lolok3 += 5;
                }
            }
            for (int length4 = (array = enchants).length, lolok4q = 0; lolok4q < length4; ++lolok4q) {
                final EnchantEntry enchant4 = array[lolok4q];
                final int level4 = EnchantmentHelper.getEnchantmentLevel(enchant4.getEnchant(), boots);
                String levelDisplay4 = "" + level4;
                if (level4 > 10) {
                    levelDisplay4 = "10+";
                }
                if (level4 > 0) {
                    if (enchant4.getName().equals("Van")) {
                        //this.mc.fontRenderer.drawStringWithShadow(ChatFormatting.RED + enchant4.getName() + " " + levelDisplay4, 0.0f, (float)(-Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT), 0);
                    } else {
                        //this.mc.fontRenderer.drawStringWithShadow(ChatFormatting.WHITE + enchant4.getName() + " " + levelDisplay4, 0.0f, (float)(-Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT), 0);
                    }
                    lolok4 += 5;
                }
            }
            for (int length5 = (array = enchants).length, lolok5q = 0; lolok5q < length5; ++lolok5q) {
                final EnchantEntry enchant5 = array[lolok5q];
                final int level5 = EnchantmentHelper.getEnchantmentLevel(enchant5.getEnchant(), inHand);
                String levelDisplay5 = "" + level5;
                if (level5 > 10) {
                    levelDisplay5 = "10+";
                }
                if (level5 > 0) {
                    if (enchant5.getName().equals("Van")) {
                        this.mc.fontRenderer.drawStringWithShadow(ChatFormatting.RED + enchant5.getName() + " " + levelDisplay5, 0.0f, (float) (-Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT), 0);
                    } else {
                        this.mc.fontRenderer.drawStringWithShadow(ChatFormatting.WHITE + enchant5.getName() + " " + levelDisplay5, 0.0f, (float) (-Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT), 0);
                    }
                    lolok5 += 5;
                }
            }
            for (int length6 = (array = enchants).length, lolok6q = 0; lolok6q < length6; ++lolok6q) {
                final EnchantEntry enchant6 = array[lolok6q];
                final int level6 = EnchantmentHelper.getEnchantmentLevel(enchant6.getEnchant(), offHand);
                String levelDisplay6 = "" + level6;
                if (level6 > 10) {
                    levelDisplay6 = "10+";
                }
                if (level6 > 0) {

                    if (enchant6.getName().equals("Van")) {
                        this.mc.fontRenderer.drawStringWithShadow(ChatFormatting.RED + enchant6.getName() + " " + levelDisplay6, 0.0f, (float) (-Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT), 0);
                    } else {
                        this.mc.fontRenderer.drawStringWithShadow(ChatFormatting.WHITE + enchant6.getName() + " " + levelDisplay6, 0.0f, (float) (-Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT), 0);
                    }
                    lolok6 += 5;
                }
            }

            if (l_Player.isPotionActive(MobEffects.STRENGTH)) {
                final DecimalFormat format1 = new DecimalFormat("0");
                final DecimalFormat format2 = new DecimalFormat("00");
                final int duration = l_Player.getActivePotionEffect(MobEffects.STRENGTH).getDuration() / 20;
                final int amplifier = l_Player.getActivePotionEffect(MobEffects.STRENGTH).getAmplifier() + 1;
                final double p2 = duration % 60;
                final double p3 = duration / 60;
                final double p4 = p3 % 60.0;
                final String minutes = format1.format(p4);
                final String seconds = format2.format(p2);
                this.mc.fontRenderer.drawStringWithShadow(TextFormatting.RED + "Strength " + amplifier + TextFormatting.WHITE + " " + minutes + ":" + seconds, x + 20, y, 0);
            } else {
                this.mc.fontRenderer.drawStringWithShadow(TextFormatting.RED + "Strength" + TextFormatting.WHITE + " None", x + 20, y, 0);
            }

            if (l_Player.isPotionActive(MobEffects.WEAKNESS)) {
                final DecimalFormat format1 = new DecimalFormat("0");
                final DecimalFormat format2 = new DecimalFormat("00");
                final int duration = l_Player.getActivePotionEffect(MobEffects.WEAKNESS).getDuration() / 20;
                final int amplifier = l_Player.getActivePotionEffect(MobEffects.WEAKNESS).getAmplifier() + 1;
                final double p2 = duration % 60;
                final double p3 = duration / 60;
                final double p4 = p3 % 60.0;
                final String minutes = format1.format(p4);
                final String seconds = format2.format(p2);
                this.mc.fontRenderer.drawStringWithShadow(TextFormatting.GRAY + "Weakness " + amplifier + TextFormatting.WHITE + " " + minutes + ":" + seconds, x + 20, y + 10, 0);
            } else {
                this.mc.fontRenderer.drawStringWithShadow(TextFormatting.GRAY + "Weakness" + TextFormatting.WHITE + " None", x + 20, y + 10, 0);
            }
        }
    }


    public static class EnchantEntry
    {
        private Enchantment enchant;
        private String name;

        public EnchantEntry(final Enchantment enchant, final String name) {
            this.enchant = enchant;
            this.name = name;
        }

        public Enchantment getEnchant() {
            return this.enchant;
        }

        public String getName() {
            return this.name;
        }
    }

    public void newPoppedTotem(Entity e) {
        if(popList == null) {
            popList = new HashMap<>();
        }

        if(popList.get(e.getName()) == null) {

            popList.put(e.getName(), 1);
            ClientChat.log(e.getName() + " popped " + 1 + " totem");

        } else if(!(popList.get(e.getName()) == null)) {
            int popCounter = popList.get(e.getName());
            int newPopCounter = popCounter += 1;

            popList.put(e.getName(), newPopCounter);
            ClientChat.log(e.getName() + " popped " + newPopCounter + " totems");
        }
    }

    public boolean onPacketRead(Packet<?> packet)
    {
        if (!(this.mc.player == null) && !(this.mc.world == null)){
            if (packet instanceof SPacketEntityStatus){
                SPacketEntityStatus racket = (SPacketEntityStatus) packet; // had to change the name LMAO
                if (racket.getOpCode() == 35) {
                    Entity entity = racket.getEntity(mc.world);
                    this.newPoppedTotem(entity);
                    return false;
                }
            }
        }
        return false;
    }


}