package dev.santih.shootclans.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.santih.shootclans.clan.Clan;
import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.clan.request.InviteRequest;
import dev.santih.shootclans.clan.request.Request;
import dev.santih.shootclans.exception.AlreadyInClanException;
import dev.santih.shootclans.exception.AlreadyInThisClanException;
import dev.santih.shootclans.exception.NotInClanException;
import dev.santih.shootclans.managers.factory.dependency.DependencyFactory;
import dev.santih.shootclans.managers.interfaces.ClanManager;
import dev.santih.shootclans.managers.interfaces.PlayerManager;
import dev.santih.shootclans.managers.interfaces.RequestManager;
import dev.santih.shootclans.utils.MessageUtils;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandAlias("clan")
public class ClanJoinLeaveCommands extends BaseCommand {

	private final ClanManager clanManager;
	private final PlayerManager playerManager;
	private final RequestManager requestManager;

	public ClanJoinLeaveCommands(DependencyFactory dependencyFactory) {
		this.clanManager = dependencyFactory.getClanManager();
		this.playerManager = dependencyFactory.getPlayerManager();
		this.requestManager = dependencyFactory.getRequestManager();
	}

	@Subcommand("join")
	@CommandCompletion("@nothing")
	@Syntax("<name>")
	@Description("Joins the clan with the given name")
	@CommandPermission("clan.join")
	public void onJoin(Player player, String name) {
		ClanPlayer clanPlayer = playerManager.getClanPlayer(player);
		Optional<InviteRequest> optionalRequest = requestManager.getRequest(clanPlayer, name, InviteRequest.class);
		if (!optionalRequest.isPresent()) {
			MessageUtils.sendCoreMessage(player, "&cYou do not have an invitation to join this clan!");
			return;
		}

		Request request = optionalRequest.get();
		try {
			requestManager.acceptRequest(clanPlayer, request);
			MessageUtils.sendCoreMessage(player, "&aYou have joined the clan!");
		} catch (Exception e) {
			if (e instanceof AlreadyInClanException) {
				MessageUtils.sendCoreMessage(player, "&cYou are already in a clan!");
			} else if (e instanceof AlreadyInThisClanException) {
				MessageUtils.sendCoreMessage(player, "&cYou are already in this clan!");
			} else if (e instanceof NotInClanException) {
				MessageUtils.sendCoreMessage(player, "&cYou are not in a clan!");
			}
		}
	}

	@Subcommand("leave")
	@CommandCompletion("@nothing")
	@Description("Leaves your current clan")
	@CommandPermission("clan.leave")
	public void onLeave(Player player) {
		ClanPlayer clanPlayer = playerManager.getClanPlayer(player);
		Optional<Clan> optionalClan = clanPlayer.getClan();
		if (!optionalClan.isPresent()) {
			MessageUtils.sendCoreMessage(player, "&cYou are not in a clan!");
			return;
		}

		Clan clan = optionalClan.get();
		if (clan.isLeader(clanPlayer)) {
			MessageUtils.sendCoreMessage(player, "&cYou cannot leave the clan because you are the leader! Use /clan delete to delete the clan.");
			return;
		}

		clan.removePlayer(clanPlayer);
		MessageUtils.sendCoreMessage(player, "&aYou have left the clan!");
	}
}