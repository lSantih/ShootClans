package dev.santih.shootclans.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.santih.shootclans.clan.Clan;
import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.clan.Permission;
import dev.santih.shootclans.conditions.Conditions;
import dev.santih.shootclans.managers.factory.dependency.DependencyFactory;
import dev.santih.shootclans.managers.interfaces.PlayerManager;
import dev.santih.shootclans.utils.MessageUtils;
import org.bukkit.entity.Player;

@CommandAlias("clan")
public class ClanPermissionCommands extends BaseCommand {
	private final PlayerManager playerManager;

	public ClanPermissionCommands(DependencyFactory dependencyFactory) {
		this.playerManager = dependencyFactory.getPlayerManager();
	}

	@Subcommand("permission add")
	@CommandCompletion("@clanplayers @clanpermissions")
	@Syntax("<player> <permission>")
	@Description("Adds the given permission to the given player")
	@CommandPermission("clan.permission.add")
	public void onPermissionAdd(Player player, ClanPlayer target, Permission permission) {
		ClanPlayer sender = playerManager.getClanPlayer(player);
		if (!sender.getClan().isPresent()) {
			MessageUtils.sendCoreMessage(player, "&cYou are not in a clan!");
			return;
		}

		if (!sender.getClan().get().isLeader(sender)) {
			MessageUtils.sendCoreMessage(player, "&cOnly leaders can add permissions!");
			return;
		}

		if (target == null) {
			MessageUtils.sendCoreMessage(player, "&cPlayer not found!");
			return;
		}

		if (!Conditions.isInSameClan(sender, target)) {
			MessageUtils.sendCoreMessage(player, "&cThe player is not in your clan!");
			return;
		}

		if (target.hasPermission(permission)) {
			MessageUtils.sendCoreMessage(player, "&cThe player already has this permission!");
			return;
		}

		target.addPermission(permission);
		MessageUtils.sendCoreMessage(player, "&aPermission added successfully!");
	}

	@Subcommand("permission remove")
	@CommandCompletion("@clanplayers @clanpermissions")
	@Syntax("<player> <permission>")
	@Description("Removes the given permission from the given player")
	@CommandPermission("clan.permission.remove")
	public void onPermissionRemove(Player player, ClanPlayer target, Permission permission) {
		ClanPlayer sender = playerManager.getClanPlayer(player);
		if (!sender.getClan().isPresent()) {
			MessageUtils.sendCoreMessage(player, "&cYou are not in a clan!");
			return;
		}

		if (!sender.getClan().get().isLeader(sender)) {
			MessageUtils.sendCoreMessage(player, "&cOnly leaders can remove permissions!");
			return;
		}

		if (target == null) {
			MessageUtils.sendCoreMessage(player, "&cPlayer not found!");
			return;
		}

		if (!Conditions.isInSameClan(sender, target)) {
			MessageUtils.sendCoreMessage(player, "&cThe player is not in your clan!");
			return;
		}

		if (!target.hasPermission(permission)) {
			MessageUtils.sendCoreMessage(player, "&cThe player does not have this permission!");
			return;
		}

		target.removePermission(permission);
		MessageUtils.sendCoreMessage(player, "&aPermission removed successfully!");
	}

	@Subcommand("permission view")
	@CommandCompletion("@clanplayers")
	@Syntax("<player>")
	@Description("Displays the permissions of the given player")
	@CommandPermission("clan.permission.view")
	public void onPermissionView(Player player, ClanPlayer target) {
		ClanPlayer sender = playerManager.getClanPlayer(player);
		if (!sender.getClan().isPresent()) {
			MessageUtils.sendCoreMessage(player, "&cYou are not in a clan!");
			return;
		}

		if (target == null) {
			MessageUtils.sendCoreMessage(player, "&cPlayer not found!");
			return;
		}

		if (!Conditions.isInSameClan(sender, target)) {
			MessageUtils.sendCoreMessage(player, "&cThe player is not in your clan!");
			return;
		}

		MessageUtils.sendCoreMessage(player, "&b" + target.getBase().getName() + "'s Permissions:");
		for (Permission permission : Permission.values()) {
			if (target.hasPermission(permission)) {
				MessageUtils.sendCoreMessage(player, "&a- " + permission.name());
			} else {
				MessageUtils.sendCoreMessage(player, "&c- " + permission.name());
			}
		}
	}

	@Subcommand("roster")
	@Description("Displays a list of every member in the clan and their permissions")
	@CommandPermission("clan.roster")
	public void onRoster(Player player) {
		ClanPlayer clanPlayer = playerManager.getClanPlayer(player);
		if (clanPlayer == null || !clanPlayer.getClan().isPresent()) {
			MessageUtils.sendCoreMessage(player, "&cYou are not in a clan.");
			return;
		}
		Clan clan = clanPlayer.getClan().get();
		if (!clanPlayer.hasPermission(Permission.INVITE)) {
			MessageUtils.sendCoreMessage(player, "&cYou do not have permission to use this command.");
			return;
		}
		MessageUtils.sendCoreMessage(player, "&bClan Roster:");
		for (ClanPlayer member : clan.getPlayers()) {
			int permissionCount = 0;
			for (Permission permission : Permission.values()) {
				if (member.hasPermission(permission)) {
					permissionCount++;
				}
			}
			MessageUtils.sendCoreMessage(player, "&a" + member.getBase().getName() + ": &e" + permissionCount + " permissions");
		}
	}

}