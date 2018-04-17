package codersafterdark.reskillable.skill.agility;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.unlockable.Trait;
import codersafterdark.reskillable.client.base.ClientTickHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TraitSidestep extends Trait {

    public static final int MAX_CD = 20;
    private int leftDown, rightDown, cd;

    public TraitSidestep() {
        super(new ResourceLocation(MOD_ID, "sidestep"), 3, 1, new ResourceLocation(MOD_ID, "agility"),
                10, "reskillable:agility|26", "reskillable:defense|20");

        if (FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void clientTick(ClientTickEvent event) {
        if (event.phase == Phase.END && cd > 0) {
            cd--;
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onKeyDown(KeyInputEvent event) {
        if (cd > 0) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (!PlayerDataHandler.get(mc.player).getSkillInfo(getParentSkill()).isUnlocked(this) || mc.player.isSneaking()) {
            return;
        }

        int threshold = 4;
        if (mc.gameSettings.keyBindLeft.isKeyDown()) {
            int oldLeft = leftDown;
            leftDown = ClientTickHandler.ticksInGame;

            if (leftDown - oldLeft < threshold) {
                dodge(mc.player, true);
            }
        } else if (mc.gameSettings.keyBindRight.isKeyDown()) {
            int oldRight = rightDown;
            rightDown = ClientTickHandler.ticksInGame;

            if (rightDown - oldRight < threshold) {
                dodge(mc.player, false);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void dodge(EntityPlayer player, boolean left) {
        if (player.capabilities.isFlying || !player.onGround || player.moveForward >= 0 || !GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSprint)) {
            return;
        }

        float yaw = player.rotationYaw;
        float x = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float z = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        Vec3d lookVec = new Vec3d(x, 0, z);
        Vec3d sideVec = lookVec.crossProduct(new Vec3d(0, left ? 1 : -1, 0)).scale(1.25);

        player.motionX = sideVec.x;
        player.motionY = sideVec.y;
        player.motionZ = sideVec.z;
        cd = MAX_CD;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void renderHUD(RenderGameOverlayEvent.Post event) {
        if (event.getType() != ElementType.ALL) {
            return;
        }

        ScaledResolution res = event.getResolution();
        Minecraft mc = Minecraft.getMinecraft();
        int xo = res.getScaledWidth() / 2 - 20;
        int y = res.getScaledHeight() / 2 + 20;

        GlStateManager.enableBlend();
        if (!mc.player.capabilities.isFlying) {
            int width = Math.min((int) ((cd - event.getPartialTicks()) * 2), 40);
            GlStateManager.color(1F, 1F, 1F, 1F);
            if (width > 0) {
                Gui.drawRect(xo, y - 2, xo + 40, y - 1, 0x44000000);
                Gui.drawRect(xo, y - 2, xo + width, y - 1, 0xFFFFFFFF);
            }
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
    }


}
