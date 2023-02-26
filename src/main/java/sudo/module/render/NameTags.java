package sudo.module.render;

import net.minecraft.client.util.math.MatrixStack;
import sudo.events.EventReceivePacket;
import sudo.module.Mod;
import com.google.common.eventbus.Subscribe;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ModeSetting;
import sudo.module.settings.NumberSetting;
import sudo.utils.render.RenderUtils;

import java.text.DecimalFormat;
import java.util.UUID;

public class NameTags extends Mod {

    BooleanSetting background  = new BooleanSetting("Background", true);
    BooleanSetting health  = new BooleanSetting("Health", true);
    BooleanSetting pops  = new BooleanSetting("Pops", true);
    BooleanSetting ping  = new BooleanSetting("Ping", true);
    ModeSetting formatting = new ModeSetting("Format", "Bold", "Bold", "Italic", "Both");
    NumberSetting scale = new NumberSetting("Scale", 0.5, 2, 1.1, .1);

    public NameTags() {
        super("NameTags", "Nametags through walls", Category.RENDER, 0);
        addSettings(background,health,pops,ping,formatting,scale);
        get = this;
    }


    private final Object2IntMap<UUID> totemPopMap = new Object2IntOpenHashMap<>();
    int popped;

    @Override
    public void onEnable() {
        totemPopMap.clear();
        popped = 0;
    }

    @SuppressWarnings("unused")
	@Subscribe
    public void onWorldRender(MatrixStack matrices) {
        for (Entity entity : mc.world.getEntities()) {
            Vec3d rPos = entity.getPos().subtract(RenderUtils.getInterpolationOffset(entity)).add(0, entity.getHeight() + 0.50, 0);
            double size = Math.max(2 * (mc.cameraEntity.distanceTo(entity) / 20), 1);

            if (entity instanceof PlayerEntity) {
                if (entity == mc.player || ((PlayerEntity) entity).isDead()) continue;

//                RenderUtils.drawText(Text.of(" "+getPlayerInfo((PlayerEntity) entity)+" "), rPos.x, rPos.y, rPos.z, scale.getValue() * size, background.isEnabled());
//                String text = Text.of(" "+getPlayerInfo((PlayerEntity) entity)+" ");
                RenderUtils.drawWorldText(getPlayerInfo((PlayerEntity) entity)+"", entity.getX(), entity.getY() + entity.getHeight() + 0.5f, entity.getZ(), scale.getValueFloat(), -1, true);
                
            }
        }
    }

    @Subscribe
    private void onPop(EventReceivePacket event) {
        if (!pops.isEnabled()) return;
        if (!(event.getPacket() instanceof EntityStatusS2CPacket p)) return;
        if (p.getStatus() != 35) return;
        Entity entity = p.getEntity(mc.world);
        if (!(entity instanceof PlayerEntity)) return;
        if ((entity.equals(mc.player))) return;

        synchronized (totemPopMap) {
            popped = totemPopMap.getOrDefault(entity.getUuid(), 0);
            totemPopMap.put(entity.getUuid(), ++popped);
        }
    }

    private String getPlayerInfo(PlayerEntity player) {
        StringBuilder sb = new StringBuilder();

        sb.append(formattingByName(player) + (ping.isEnabled() ? " " + getPing(player)+"ms" : ""));
        sb.append(health.isEnabled() ? formattingByHealth(player) : "");
        sb.append(pops.isEnabled() ? formattingByPops(getPops(player)) : "");

        return sb.toString();
    }

    private int getPops(PlayerEntity p) {
        if (!totemPopMap.containsKey(p.getUuid())) return 0;
        return totemPopMap.getOrDefault(p.getUuid(), 0);
    }

    public int getPing(PlayerEntity player) {
        if (mc.getNetworkHandler() == null) return 0;

        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        if (playerListEntry == null) return 0;
        return playerListEntry.getLatency();
    }

    private String formattingByHealth(PlayerEntity player) {
        DecimalFormat df = new DecimalFormat("##");
        double hp = Math.round(player.getHealth() + player.getAbsorptionAmount());
        String health = df.format(Math.round(hp));

        if (hp >= 19) return shortForm() + Formatting.GREEN + " " + health;
        if (hp >= 13 && hp <= 18) return shortForm() + Formatting.YELLOW + " " + health;
        if (hp >= 8 && hp <= 12) return shortForm() + Formatting.GOLD + " " + health;
        if (hp >= 6 && hp <= 7) return  shortForm() + Formatting.RED + " " + health;
        if (hp <= 5) return  shortForm() + Formatting.DARK_RED + " " + health;

        return shortForm() + Formatting.GREEN + " unexpected";
    }

    private String shortForm() {
        return switch (formatting.getMode()) {
            case "Italic" -> "" + Formatting.ITALIC;
            case "Bold" -> "" + Formatting.BOLD ;
            case "Both" -> "" + Formatting.ITALIC + Formatting.BOLD;
            default -> "";
        };
    }

    private String formattingByName(PlayerEntity player) {
        if (player.isInSneakingPose()) return shortForm() + Formatting.GOLD + player.getEntityName();

        return shortForm() + Formatting.WHITE + player.getEntityName();
    }

    private String formattingByPops(int pops) {
        switch (pops){
            case 0: return "";
            case 1: return shortForm() + Formatting.GREEN + " -" + pops;
            case 2: return shortForm() + Formatting.DARK_GREEN + " -" + pops;
            case 3: return shortForm() + Formatting.YELLOW + " -" + pops;
            case 4: return shortForm() + Formatting.GOLD + " -" + pops;
            case 5: return shortForm() + Formatting.RED + " -" + pops;
            default: return shortForm() + Formatting.DARK_RED + " -" + pops;

        }
    }

    public static NameTags get;

}
