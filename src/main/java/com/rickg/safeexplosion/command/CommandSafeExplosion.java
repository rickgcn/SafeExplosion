package com.rickg.safeexplosion.command;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;

import com.rickg.safeexplosion.Config;
import com.rickg.safeexplosion.SafeExplosionMod;

public final class CommandSafeExplosion extends CommandBase {

    private static final String COMMAND_NAME = "safeexplosion";
    private static final String SUBCOMMAND_RELOAD = "reload";
    private static final String SUBCOMMAND_LIST = "list";

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + COMMAND_NAME + " <reload|list>";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("safeexp", "se");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 1) {
            throw new WrongUsageException(getCommandUsage(sender));
        }

        String subcommand = args[0];
        if (SUBCOMMAND_RELOAD.equalsIgnoreCase(subcommand)) {
            Config.reloadFromDisk();
            SafeExplosionMod.LOG
                .info("{} configuration reloaded by {}.", SafeExplosionMod.MOD_NAME, sender.getCommandSenderName());
            sendMessage(sender, "SafeExplosion configuration reloaded.");
            sendCurrentConfiguration(sender);
            return;
        }

        if (SUBCOMMAND_LIST.equalsIgnoreCase(subcommand)) {
            sendCurrentConfiguration(sender);
            return;
        }

        throw new WrongUsageException(getCommandUsage(sender));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, SUBCOMMAND_RELOAD, SUBCOMMAND_LIST);
        }

        return null;
    }

    private static void sendCurrentConfiguration(ICommandSender sender) {
        sendMessage(sender, "SafeExplosion configuration:");
        sendMessage(sender, "  disableBlockDamage=" + Config.disableBlockDamage);
        sendMessage(sender, "  protectedExplosionTypes=" + Config.getProtectedExplosionTypesSummary());
        sendMessage(sender, "  logProtectedExplosions=" + Config.logProtectedExplosions);
        sendMessage(sender, "  logExplosionTypeDecisions=" + Config.logExplosionTypeDecisions);
    }

    private static void sendMessage(ICommandSender sender, String message) {
        sender.addChatMessage(new ChatComponentText(message));
    }
}
