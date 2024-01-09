package dev.santih.shootclans.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.santih.shootclans.clan.Clan;
import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.clan.Permission;
import dev.santih.shootclans.clan.request.ClanRequestType;
import dev.santih.shootclans.exception.AlreadyInClanException;
import dev.santih.shootclans.exception.NotInClanException;
import dev.santih.shootclans.managers.factory.dependency.DependencyFactory;
import dev.santih.shootclans.managers.interfaces.ClanManager;
import dev.santih.shootclans.managers.interfaces.PlayerManager;
import dev.santih.shootclans.managers.interfaces.RequestManager;
import dev.santih.shootclans.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandAlias("clan")
public class ClanManagementCommands extends BaseCommand {
	private static final int MIN_CLAN_NAME_LENGTH = 3;
	private static final int MAX_CLAN_NAME_LENGTH = 16;

	private final ClanManager clanManager;
	private final PlayerManager playerManager;
	private final RequestManager requestManager;

	public ClanManagementCommands(DependencyFactory dependencyFactory) {
		this.clanManager = dependencyFactory.getClanManager();
		this.playerManager = dependencyFactory.getPlayerManager();
		this.requestManager = dependencyFactory.getRequestManager();
	}

	@Subcommand("create")
	@CommandCompletion("@nothing")
	@Syntax("<name>")
	@Description("Creates a new clan with the given name")
	@CommandPermission("clan.create")
	public void onCreate(Player player, String name) {
		if (name.length() < MIN_CLAN_NAME_LENGTH || name.length() > MAX_CLAN_NAME_LENGTH) {
			MessageUtils.sendCoreMessage(player, "&cClan name must be between " + MIN_CLAN_NAME_LENGTH + " and " + MAX_CLAN_NAME_LENGTH + " characters!");
			return;
		}

		if (!name.matches("[a-zA-Z0-9]+")) {
			MessageUtils.sendCoreMessage(player, "&cClan name can only contain letters and numbers!");
			return;
		}

		ClanPlayer clanPlayer = playerManager.getClanPlayer(player);
		try {
			clanManager.createClan(name, clanPlayer);
			MessageUtils.sendCoreMessage(player, "&aClan created successfully!");
		} catch (AlreadyInClanException e) {
			MessageUtils.sendCoreMessage(player, "&cYou are already in a clan!");
		}
	}

	@Subcommand("delete")
	@CommandCompletion("@nothing")
	@Description("Deletes your current clan")
	@CommandPermission("clan.delete")
	public void onDelete(Player player) {
		ClanPlayer clanPlayer = playerManager.getClanPlayer(player);
		Optional<Clan> optionalClan = clanPlayer.getClan();
		if (!optionalClan.isPresent()) {
			MessageUtils.sendCoreMessage(player, "&cYou are not in a clan!");
			return;
		}

		Clan clan = optionalClan.get();
		if (!clan.isLeader(clanPlayer)) {
			MessageUtils.sendCoreMessage(player, "&cYou must be the leader of the clan to delete it!");
			return;
		}

		clanManager.deleteClan(clan);
		MessageUtils.sendCoreMessage(player, "&aClan deleted successfully!");
	}

	@Subcommand("invite")
	@CommandCompletion("@players")
	@Syntax("<player>")
	@Description("Invites the given player to your clan")
	@CommandPermission("clan.invite")
	public void onInvite(Player player, String targetName) {
		Player target = Bukkit.getPlayer(targetName);
		if (target == null) {
			MessageUtils.sendCoreMessage(player, "&cPlayer not found!");
			return;
		}

		if (player.equals(target)) {
			MessageUtils.sendCoreMessage(player, "&cYou cannot invite yourself!");
			return;
		}

		ClanPlayer sender = playerManager.getClanPlayer(player);
		ClanPlayer receiver = playerManager.getClanPlayer(target);

		if (!sender.getClan().isPresent()) {
			MessageUtils.sendCoreMessage(player, "&cYou are not in a clan!");
			return;
		}

		if(!sender.hasPermission(Permission.INVITE)){
			MessageUtils.sendCoreMessage(player, "&cYou don't have permissions");
			return;
		}

		if (receiver.getClan().isPresent()) {
			MessageUtils.sendCoreMessage(player, "&cThe player is already in a clan!");
			return;
		}

		try {
			requestManager.sendRequest(sender, receiver, ClanRequestType.INVITE);
			MessageUtils.sendCoreMessage(player, "&aInvitation sent successfully!");
			MessageUtils.sendCoreMessage(target, "&aYou have been invited to join " + sender.getClan().get().getName() + "!");

		} catch (NotInClanException e) {
			MessageUtils.sendCoreMessage(player, "&cYou are not in a clan!");
		}
	}


}

