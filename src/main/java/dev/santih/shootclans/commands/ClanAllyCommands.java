package dev.santih.shootclans.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.annotation.Syntax;
import dev.santih.shootclans.clan.Clan;
import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.clan.Permission;
import dev.santih.shootclans.clan.request.AllyRequest;
import dev.santih.shootclans.clan.request.ClanRequestType;
import dev.santih.shootclans.exception.AlreadyInClanException;
import dev.santih.shootclans.exception.AlreadyInThisClanException;
import dev.santih.shootclans.exception.NotInClanException;
import dev.santih.shootclans.managers.factory.dependency.DependencyFactory;
import dev.santih.shootclans.managers.interfaces.ClanManager;
import dev.santih.shootclans.managers.interfaces.PlayerManager;
import dev.santih.shootclans.managers.interfaces.RequestManager;
import dev.santih.shootclans.utils.MessageUtils;
import org.bukkit.entity.Player;

@CommandAlias("clan")
public class ClanAllyCommands extends BaseCommand {

	private final RequestManager requestManager;

	private final PlayerManager playerManager;

	private final ClanManager clanManager;

	public ClanAllyCommands(DependencyFactory dependencyFactory) {
		this.requestManager = dependencyFactory.getRequestManager();
		this.playerManager = dependencyFactory.getPlayerManager();
		this.clanManager = dependencyFactory.getClanManager();
	}

	@CommandAlias("ally send")
	@Description("Send an ally request to another clan")
	@Syntax("<clanName>")
	public void sendAllyRequest(Player player, @Single String clanName) {
		ClanPlayer sender = playerManager.getClanPlayer(player);
		Clan senderClan = sender.getClan().orElse(null);

		if (!sender.hasPermission(Permission.SEND_ALLY)) {
			MessageUtils.sendCoreMessage(player, "cYou don't have permissions");
			return;
		}

		if (senderClan == null) {
			MessageUtils.sendCoreMessage(player, "&cYou are not in a clan!");
			return;
		}

		Clan receiverClan = clanManager.getClanByName(clanName).orElse(null);

		if (receiverClan == null) {
			MessageUtils.sendCoreMessage(player, "&cThe specified clan does not exist!");
			return;
		}

		if (senderClan.isAlly(receiverClan.getName())) {
			MessageUtils.sendCoreMessage(player, "&eYou are already allies with that clan!");
			return;
		}

		try {
			if (!receiverClan.getLeader().isPresent()) {
				return;
			}
			requestManager.sendRequest(sender, receiverClan.getLeader().get(), ClanRequestType.ALLIANCE);
			MessageUtils.sendCoreMessage(player, "&aAlly request sent to " + receiverClan.getName());
		} catch (NotInClanException e) {
			MessageUtils.sendCoreMessage(player, "&cYou need to be in a clan to send ally requests!");
		}
	}


	@CommandAlias("ally accept")
	@Description("Accept an ally request from another clan")
	@Syntax("<clanName>")
	public void acceptAllyRequest(Player player, @Single String clanName) {
		ClanPlayer receiver = playerManager.getClanPlayer(player);
		Clan clan = receiver.getClan().orElse(null);
		if (clan == null) {
			MessageUtils.sendCoreMessage(player, "&cYou are not in a clan!");
			return;
		}

		if (!receiver.hasPermission(Permission.ACCEPT_ALLY)) {
			MessageUtils.sendCoreMessage(player, "&cYou don't have permissions");
			return;
		}

		AllyRequest request = requestManager.getRequest(receiver, clanName, AllyRequest.class).orElse(null);

		if (request == null) {
			MessageUtils.sendCoreMessage(player, "&cThere is no ally request from the specified clan!");
			return;
		}

		try {
			requestManager.acceptRequest(receiver, request);
			MessageUtils.sendCoreMessage(player, "&aYou are now allies with " + request.sender().getClan().get().getName());
		} catch (AlreadyInClanException | AlreadyInThisClanException | NotInClanException e) {
			// Handle exceptions
		}
	}


	@CommandAlias("ally remove")
	@Description("Remove an ally from your clan")
	@Syntax("<clanName>")
	public void removeAlly(Player player, @Single String clanName) {
		ClanPlayer sender = playerManager.getClanPlayer(player);

		Clan clan = sender.getClan().orElse(null);
		if (clan == null) {
			MessageUtils.sendCoreMessage(player, "&cYou are not in a clan!");
			return;
		}

		if (!sender.hasPermission(Permission.REMOVE_ALLY)) {
			MessageUtils.sendCoreMessage(player, "&cYou don't have permissions");
			return;
		}

		if (!clan.isAlly(clanName)) {
			MessageUtils.sendCoreMessage(player, "&cThe specified clan is not your ally!");
			return;
		}

		clan.removeAlly(clanName);
		MessageUtils.sendCoreMessage(player, "&aAlly removed with " + clanName);
	}

}
