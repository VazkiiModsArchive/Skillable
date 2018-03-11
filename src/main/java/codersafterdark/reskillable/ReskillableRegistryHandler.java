package codersafterdark.reskillable;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.skill.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.RegistryBuilder;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class ReskillableRegistryHandler {
    @SubscribeEvent
    public static void buildRegistry(RegistryEvent.NewRegistry newRegistryEvent) {
        new RegistryBuilder<Skill>()
                .setName(new ResourceLocation(MOD_ID, "skill"))
                .setType(Skill.class)
                .create();
    }

    @SubscribeEvent
    public static void registerSkills(RegistryEvent.Register<Skill> skillRegister) {
        skillRegister.getRegistry().registerAll(
                new SkillMining(),
                new SkillGathering(),
                new SkillAttack(),
                new SkillDefense(),
                new SkillBuilding(),
                new SkillFarming(),
                new SkillAgility(),
                new SkillMagic()
        );
    }
}
