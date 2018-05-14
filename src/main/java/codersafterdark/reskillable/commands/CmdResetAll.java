package codersafterdark.reskillable.commands;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.event.LevelUpEvent;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CmdResetAll extends CommandBase {
    @Nonnull
    @Override
    public String getName() {
        return "resetall";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "reskillable.command.resetall.usage";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length == 0) {
            throw new CommandException("reskillable.command.invalid.missing.player");
        }
        EntityPlayerMP player = getPlayer(server, sender, args[0]);
        PlayerData data = PlayerDataHandler.get(player);
        Collection<PlayerSkillInfo> allSkils = data.getAllSkillInfo();
        StringBuilder failedSkills = new StringBuilder();
        for (PlayerSkillInfo skillInfo : allSkils) {
            int oldLevel = skillInfo.getLevel();
            if (!MinecraftForge.EVENT_BUS.post(new LevelUpEvent.Pre(player, skillInfo.skill, 1, oldLevel))) {
                skillInfo.setLevel(1);
                skillInfo.respec();
                MinecraftForge.EVENT_BUS.post(new LevelUpEvent.Post(player, skillInfo.skill, 1, oldLevel));
            } else {
                failedSkills.append(skillInfo.skill.getName()).append(", ");
            }
        }
        data.saveAndSync();
        if (failedSkills.length() == 0) {
            sender.sendMessage(new TextComponentTranslation("reskillable.command.success.resetall", player.getDisplayName()));
        } else {
            sender.sendMessage(new TextComponentTranslation("reskillable.command.fail.resetall", failedSkills.substring(0, failedSkills.length() - 2), player.getDisplayName()));
        }
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
        return new ArrayList<>();
    }
}