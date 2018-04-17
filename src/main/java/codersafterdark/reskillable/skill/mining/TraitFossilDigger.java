package codersafterdark.reskillable.skill.mining;

import codersafterdark.reskillable.api.unlockable.Trait;
import codersafterdark.reskillable.base.ConditionHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TraitFossilDigger extends Trait {

    public TraitFossilDigger() {
        super(new ResourceLocation(MOD_ID, "fossil_digger"), 2, 1, new ResourceLocation(MOD_ID, "mining"),
                2, "reskillable:mining|6");
    }

    @Override
    public void onBlockDrops(HarvestDropsEvent event) {
        EntityPlayer player = event.getHarvester();
        IBlockState state = event.getState();

        if (state.getBlock() == Blocks.COAL_ORE && ConditionHelper.hasRightTool(player, state, "pickaxe", ToolMaterial.IRON.getHarvestLevel())
                && player.world.rand.nextInt(10) == 0 && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.getHeldItemMainhand()) == 0) {
            event.getDrops().add(new ItemStack(Items.COAL));
        }
    }


}
