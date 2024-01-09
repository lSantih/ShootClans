package dev.santih.shootclans.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import dev.santih.shootclans.clan.Clan;
import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.clan.Permission;
import dev.santih.shootclans.managers.factory.dependency.DependencyFactory;
import dev.santih.shootclans.managers.interfaces.PlayerManager;
import dev.santih.shootclans.utils.Cuboid;
import dev.santih.shootclans.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("clan")
public class ClanHomeClaimCommands extends BaseCommand {
	private final PlayerManager playerManager;

	public ClanHomeClaimCommands(DependencyFactory dependencyFactory) {
		this.playerManager = dependencyFactory.getPlayerManager();
	}

	@Subcommand("home")
	public void onHome(Player player) {
		ClanPlayer clanPlayer = playerManager.getClanPlayer(player);
		Clan clan = clanPlayer.getClan().orElse(null);
		if (clan == null) {
			MessageUtils.sendCoreMessage(player, "&cYou are not in a clan.");
			return;
		}
		if (!clanPlayer.hasPermission(Permission.HOME)) {
			MessageUtils.sendCoreMessage(player, "&cYou don't have permission to use this command.");
			return;
		}
		if (clan.getClanHome() == null) {
			MessageUtils.sendCoreMessage(player, "&cYour clan does not have a home location set.");
			return;
		}
		// Teleport the player to the clan's home location
		clan.teleport(player);
		MessageUtils.sendCoreMessage(player, "&aTeleported to your clan's home location.");
	}

	@Subcommand("sethome")
	public void onSetHome(Player player) {
		ClanPlayer clanPlayer = playerManager.getClanPlayer(player);
		Clan clan = clanPlayer.getClan().orElse(null);
		if (clan == null) {
			MessageUtils.sendCoreMessage(player, "&cYou are not in a clan.");
			return;
		}
		if (!clanPlayer.hasPermission(Permission.SET_HOME)) {
			MessageUtils.sendCoreMessage(player, "&cYou don't have permission to use this command.");
			return;
		}
		if (clan.getClaim() == null) {
			MessageUtils.sendCoreMessage(player, "&cYour clan does not have a claim.");
			return;
		}
		Location location = player.getLocation();
		if (!clan.getClaim().isIn(location)) {
			MessageUtils.sendCoreMessage(player, "&cThe home location must be inside your clan's claim.");
			return;
		}
		// Set the clan's home location to the player's current location
		clan.setLocation(location);
		MessageUtils.sendCoreMessage(player, "&aClan home location set.");
	}

	@Subcommand("claim view")
	public void onClaimView(Player player) {
		ClanPlayer clanPlayer = playerManager.getClanPlayer(player);
		Clan clan = clanPlayer.getClan().orElse(null);
		if (clan == null) {
			MessageUtils.sendCoreMessage(player, "&cYou are not in a clan.");
			return;
		}
		Cuboid claim = clan.getClaim();
		if (claim == null) {
			MessageUtils.sendCoreMessage(player, "&cYour clan does not have a claim.");
			return;
		}
		int totalBlocks = claim.getTotalBlockSize();
		Location min = claim.getPoint1();
		Location max = claim.getPoint2();
		MessageUtils.sendCoreMessage(player, "&aYour clan's claim has a total of &e" + totalBlocks + " &ablocks.");
		MessageUtils.sendCoreMessage(player, "&aThe coordinates of your claim are: &e(" + min.getBlockX() + ", " + min.getBlockY() + ", " + min.getBlockZ() + ") &ato &e(" + max.getBlockX() + ", " + max.getBlockY() + ", " + max.getBlockZ() + ")");
	}

	@Subcommand("claim remove")
	public void onClaimRemove(Player player) {
		ClanPlayer clanPlayer = playerManager.getClanPlayer(player);
		Clan clan = clanPlayer.getClan().orElse(null);
		if (clan == null) {
			MessageUtils.sendCoreMessage(player, "&cYou are not in a clan.");
			return;
		}

		if(clanPlayer.hasPermission(Permission.UNCLAIM_WAND)){
			MessageUtils.sendCoreMessage(player, "&cYou not have permissions");
			return;
		}

		if (clan.getClaim() == null) {
			MessageUtils.sendCoreMessage(player, "&cYour clan does not have a claim.");
			return;
		}
		// Remove the clan's claim
		clan.setClaim(null);
		MessageUtils.sendCoreMessage(player, "&aYour clan's claim has been removed.");
	}
}