package codersafterdark.reskillable;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import codersafterdark.reskillable.skill.*;
import codersafterdark.reskillable.skill.agility.TraitHillWalker;
import codersafterdark.reskillable.skill.agility.TraitRoadWalk;
import codersafterdark.reskillable.skill.agility.TraitSidestep;
import codersafterdark.reskillable.skill.attack.TraitBattleSpirit;
import codersafterdark.reskillable.skill.attack.TraitNeutralissse;
import codersafterdark.reskillable.skill.building.TraitChorusTransmutation;
import codersafterdark.reskillable.skill.building.TraitPerfectRecover;
import codersafterdark.reskillable.skill.defense.TraitEffectTwist;
import codersafterdark.reskillable.skill.defense.TraitUndershirt;
import codersafterdark.reskillable.skill.farming.TraitGreenThumb;
import codersafterdark.reskillable.skill.farming.TraitMoreWheat;
import codersafterdark.reskillable.skill.gathering.TraitDropGuarantee;
import codersafterdark.reskillable.skill.gathering.TraitLuckyFisherman;
import codersafterdark.reskillable.skill.magic.TraitGoldenOsmosis;
import codersafterdark.reskillable.skill.magic.TraitSafePort;
import codersafterdark.reskillable.skill.mining.TraitFossilDigger;
import codersafterdark.reskillable.skill.mining.TraitObsidianSmasher;
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
        new RegistryBuilder<Unlockable>()
                .setName(new ResourceLocation(MOD_ID, "unlockable"))
                .setType(Unlockable.class)
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

    @SubscribeEvent
    public static void registerTraits(RegistryEvent.Register<Unlockable> unlockablesRegister) {
        unlockablesRegister.getRegistry().registerAll(
                new TraitHillWalker(),
                new TraitRoadWalk(),
                new TraitSidestep(),
                new TraitNeutralissse(),
                new TraitBattleSpirit(),
                new TraitChorusTransmutation(),
                new TraitPerfectRecover(),
                new TraitUndershirt(),
                new TraitEffectTwist(),
                new TraitMoreWheat(),
                new TraitGreenThumb(),
                new TraitLuckyFisherman(),
                new TraitDropGuarantee(),
                new TraitGoldenOsmosis(),
                new TraitSafePort(),
                new TraitFossilDigger(),
                new TraitObsidianSmasher()
        );
    }
}
