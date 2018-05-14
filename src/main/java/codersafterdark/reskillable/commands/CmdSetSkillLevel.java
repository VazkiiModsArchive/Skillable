package codersafterdark.reskillable.commands;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.event.LevelUpEvent;
import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CmdSetSkillLevel extends CommandBase {
    @Nonnull
    @Override
    public String getName() {
        return "setskilllevel";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "reskillable.command.setskilllevel.usage";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length == 0) {
            throw new CommandException("reskillable.command.invalid.missing.playerskilllevel");
        }
        if (args.length == 1) {
            throw new CommandException("reskillable.command.invalid.missing.skilllevel");
        }
        if (args.length == 2) {
            throw new CommandException("reskillable.command.invalid.missing.level");
        }
        EntityPlayerMP player = getPlayer(server, sender, args[0]);
        String[] parts = args[1].split("\\.");
        ResourceLocation skillName = parts.length > 1 ? new ResourceLocation(parts[0], args[1].substring(parts[0].length() + 1)) : new ResourceLocation(args[1]);
        if (!ReskillableRegistries.SKILLS.containsKey(skillName)) {
            throw new CommandException("reskillable.command.invalid.skill", skillName);
        }
        int level;
        try {
            level = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            throw new CommandException("reskillable.command.invalid.level", args[2]);
        }
        if (level <= 0) {
            throw new CommandException("reskillable.command.invalid.belowmin", level);
        }
        Skill skill = ReskillableRegistries.SKILLS.getValue(skillName);
        if (level > skill.getCap()) {
            throw new CommandException("reskillable.command.invalid.pastcap", level, skill.getCap());
        }
        PlayerData data = PlayerDataHandler.get(player);
        PlayerSkillInfo skillInfo = data.getSkillInfo(skill);
        int oldLevel = skillInfo.getLevel();
        if (!MinecraftForge.EVENT_BUS.post(new LevelUpEvent.Pre(player, skill, level, oldLevel))) {
            skillInfo.setLevel(level);
            MinecraftForge.EVENT_BUS.post(new LevelUpEvent.Post(player, skill, level, oldLevel));
            sender.sendMessage(new TextComponentTranslation("reskillable.command.success.setskilllevel", skillName, level, player.getDisplayName()));
        } else {
            sender.sendMessage(new TextComponentTranslation("reskillable.command.fail.setskilllevel", skillName, level, player.getDisplayName()));
        }
        data.saveAndSync();
    }

    @Nonnull
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 0) {
            return Arrays.asList(server.getPlayerList().getOnlinePlayerNames());
        }
        if (args.length == 1) {
            String partialName = args[0];
            return Arrays.stream(server.getPlayerList().getOnlinePlayerNames()).filter(name -> name.startsWith(partialName)).collect(Collectors.toList());
        }
        if (args.length == 2) {
            String partial = args[1];
            return ReskillableRegistries.SKILLS.getValuesCollection().stream().map(Skill::getKey).filter(skillName -> skillName.startsWith(partial)).collect(Collectors.toList());
        }
        if (args.length == 3) {
            String[] parts = args[1].split("\\.");
            ResourceLocation skillName = parts.length > 1 ? new ResourceLocation(parts[0], args[1].substring(parts[0].length() + 1)) : new ResourceLocation(args[1]);
            if (ReskillableRegistries.SKILLS.containsKey(skillName)) {
                Skill skill = ReskillableRegistries.SKILLS.getValue(skillName);
                String level = args[2];
                return IntStream.rangeClosed(1, skill.getCap()).mapToObj(Integer::toString).filter(iString -> iString.startsWith(level)).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }
}