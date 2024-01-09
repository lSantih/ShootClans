package dev.santih.shootclans.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import dev.santih.shootclans.clan.Clan;
import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.clan.Permission;
import dev.santih.shootclans.exception.AlreadyInClanException;
import dev.santih.shootclans.managers.factory.dependency.DependencyFactory;
import dev.santih.shootclans.managers.interfaces.ClanManager;
import dev.santih.shootclans.managers.interfaces.PlayerManager;
import dev.santih.shootclans.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;

@CommandAlias("clan")
public class ClanAdminMemberCommands extends BaseCommand {

	private final PlayerManager playerManager;
	private final ClanManager clanManager;

	public ClanAdminMemberCommands(final DependencyFactory dependencyFactory) {
		this.playerManager = dependencyFactory.getPlayerManager();
		this.clanManager = dependencyFactory.getClanManager();
	}

	@Subcommand("admin member add")
	@Syntax("<playerName> <clan>")
	@CommandCompletion("@players @clans @nothing")
	public void onAdd(CommandSender sender, String playerName, String clanName) {
		Player player = Bukkit.getPlayer(playerName);
		if (player == null) {
			MessageUtils.sendCoreMessage(sender, "&cPlayer not found.");
			return;
		}
		ClanPlayer clanPlayer = playerManager.getClanPlayer(player);
		if (clanPlayer.getClan().isPresent()) {
			MessageUtils.sendCoreMessage(sender, "&cPlayer is already in a clan.");
			return;
		}
		Optional<Clan> optionalClan = clanManager.getClanByName(clanName);
		if (!optionalClan.isPresent()) {
			MessageUtils.sendCoreMessage(sender, "&cClan not found.");
			return;
		}
		Clan clan = optionalClan.get();
		try {
			clan.addPlayer(clanPlayer);
		} catch (AlreadyInClanException e) {
			e.printStackTrace();
		}
		MessageUtils.sendCoreMessage(sender, "&aPlayer &6" + playerName + "&a has been added to the clan &6" + clanName + "&a.");
		MessageUtils.sendCoreMessage(player, "&aYou have been added to the clan &6" + clanName + "&a.");
	}

	@Subcommand("admin member remove")
	@Syntax("<clan> <playerName>")
	@CommandCompletion("@clans @clanplayers @nothing")
	public void onRemove(CommandSender sender, String clanName, String playerName) {
		Player player = Bukkit.getPlayer(playerName);
		if (player == null) {
			MessageUtils.sendCoreMessage(sender, "&cPlayer not found.");
			return;
		}
		ClanPlayer clanPlayer = playerManager.getClanPlayer(player);
		if (!clanPlayer.getClan().isPresent()) {
			MessageUtils.sendCoreMessage(sender, "&cPlayer is not in a clan.");
			return;
		}

		String texto = "Esto es un texto con un valor!";
		texto = "ad";

		Optional<Clan> optionalClan = clanManager.getClanByName(clanName);
		if (!optionalClan.isPresent()) {
			MessageUtils.sendCoreMessage(sender, "&cClan not found.");
			return;
		}
		Clan clan = optionalClan.get();
		if (!clan.getPlayers().contains(clanPlayer)) {
			MessageUtils.sendCoreMessage(sender, "&cPlayer is not in this clan.");
			return;
		}
		clan.removePlayer(clanPlayer);
		MessageUtils.sendCoreMessage(sender, "&aPlayer &6" + playerName + "&a has been removed from the clan &6" + clanName + "&a.");
		MessageUtils.sendCoreMessage(player, "&aYou have been removed from the clan &6" + clanName + "&a.");
	}

	@Subcommand("admin member setpermission")
	@CommandCompletion("@players @clanpermissions grant|revoke @nothing")
	public void onSetPermission(Player sender, String memberName, Permission permission, String action) {
		if (!action.equalsIgnoreCase("grant") && !action.equalsIgnoreCase("revoke")) {
			MessageUtils.sendCoreMessage(sender, "&cInvalid action. Must be 'grant' or 'revoke'.");
			return;
		}

		Player player = Bukkit.getPlayerExact(memberName);

		if (player == null) {
			MessageUtils.sendCoreMessage(sender, "&cCould not find player with name " + memberName);
			return;
		}
		ClanPlayer member = playerManager.getClanPlayer(player);

		if (!member.getClan().isPresent()) {
			MessageUtils.sendCoreMessage(sender, "&cPlayer " + memberName + " is not in a clan");
			return;
		}

		if (action.equalsIgnoreCase("grant")) {
			if (member.hasPermission(permission)) {
				MessageUtils.sendCoreMessage(sender, "&cPlayer " + memberName + " already has permission " + permission.name());
				return;
			}

			member.addPermission(permission);
			MessageUtils.sendCoreMessage(sender, "&aPermission " + permission.name() + " added to player " + memberName);
			return;
		}

		if (!member.hasPermission(permission)) {
			MessageUtils.sendCoreMessage(sender, "&cPlayer " + memberName + " does not have permission " + permission.name());
			return;
		}
		member.removePermission(permission);
		MessageUtils.sendCoreMessage(sender, "&aPermission " + permission.name() + " removed from player " + memberName);
	}

	@Subcommand("admin member list")
	@CommandCompletion("@clans")
	public void onListMembers(Player sender, String clanName) {
		Optional<Clan> clanOpt = clanManager.getClanByName(clanName);
		if (!clanOpt.isPresent()) {
			MessageUtils.sendCoreMessage(sender, "&cCould not find clan with name " + clanName);
			return;
		}
		
		Clan clan = clanOpt.get();
		Set<ClanPlayer> members = clan.getPlayers();

		StringBuilder sb = new StringBuilder();
		sb.append("&6Members of &e").append(clanName).append("&6:");
		for (ClanPlayer member : members) {
			sb.append("\n&7- &f").append(member.getName());
		}
		MessageUtils.sendCoreMessage(sender, sb.toString());
	}


}


