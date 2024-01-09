	package dev.santih.shootclans.commands;

	import co.aikar.commands.BaseCommand;
	import co.aikar.commands.annotation.*;
	import dev.santih.shootclans.clan.Clan;
	import dev.santih.shootclans.clan.ClanPlayer;
	import dev.santih.shootclans.clan.gui.TestGui;
	import dev.santih.shootclans.clan.rank.ClanRank;
	import dev.santih.shootclans.managers.factory.dependency.DependencyFactory;
	import dev.santih.shootclans.managers.interfaces.*;
	import dev.santih.shootclans.utils.MessageUtils;
	import org.bukkit.command.CommandSender;
	import org.bukkit.entity.Player;

	import java.util.Optional;

	@CommandAlias("clan")
	public class ClanAdminCommands extends BaseCommand {

		private final PlayerManager playerManager;
		private final EconomyManager economyManager;
		private final ClaimManager claimManager;
		private final ClanManager clanManager;
		private final RankManager rankManager;
		private final DependencyFactory dependencyFactory;

		public ClanAdminCommands(DependencyFactory dependencyFactory) {
			this.playerManager = dependencyFactory.getPlayerManager();
			this.economyManager = dependencyFactory.getEconomyManager();
			this.clanManager = dependencyFactory.getClanManager();
			this.claimManager = dependencyFactory.getClaimManager();
			this.rankManager = dependencyFactory.getRankManager();

			this.dependencyFactory = dependencyFactory;
		}

		@Subcommand("admin")
		public class AdminCommands extends BaseCommand {

			@Subcommand("addbalance")
			@Syntax("<clan> <amount>")
			@CommandCompletion("@clans")
			public void addBalance(CommandSender sender, String clanName, double amount) {
				if (amount <= 0) {
					MessageUtils.sendCoreMessage(sender, "&cInvalid amount.");
					return;
				}

				Optional<Clan> optionalClan = clanManager.getClanByName(clanName);
				if (optionalClan.isPresent()) {
					Clan clan = optionalClan.get();
					clan.addBalance(amount);
					MessageUtils.sendCoreMessage(sender, "&aAdded " + economyManager.getEconomy().format(amount) + " to the clan balance.");
				} else {
					MessageUtils.sendCoreMessage(sender, "&cClan not found.");
				}
			}

			@Subcommand("removebalance")
			@Syntax("<clan> <amount>")
			@CommandCompletion("@clans")
			public void removeBalance(CommandSender sender, String clanName, double amount) {
				if (amount <= 0) {
					MessageUtils.sendCoreMessage(sender, "&cInvalid amount.");
					return;
				}

				Optional<Clan> optionalClan = clanManager.getClanByName(clanName);
				if (optionalClan.isPresent()) {
					Clan clan = optionalClan.get();
					clan.removeBalance(amount);
					MessageUtils.sendCoreMessage(sender, "&aRemoved " + economyManager.getEconomy().format(amount) + " from the clan balance.");
				} else {
					MessageUtils.sendCoreMessage(sender, "&cClan not found.");
				}
			}

			@Subcommand("setbalance")
			@Syntax("<clan> <amount>")
			@CommandCompletion("@clans")
			public void setBalance(CommandSender sender, String clanName, double amount) {
				if (amount < 0) {
					MessageUtils.sendCoreMessage(sender, "&cInvalid amount.");
					return;
				}
	
				Optional<Clan> optionalClan = clanManager.getClanByName(clanName);
				if (optionalClan.isPresent()) {
					Clan clan = optionalClan.get();
					clan.setBalance(amount);
					MessageUtils.sendCoreMessage(sender, "&aSet the clan balance to " + economyManager.getEconomy().format(amount));
				} else {
					MessageUtils.sendCoreMessage(sender, "&cClan not found.");
				}
			}

			@Subcommand("forcejoin")
			@Syntax("<clan>")
			@CommandCompletion("@clans")
			public void forceJoin(Player player, String clanName) {
				ClanPlayer clanPlayer = playerManager.getClanPlayer(player);
				if (clanPlayer.getClan().isPresent()) {
					MessageUtils.sendCoreMessage(player, "&cYou are already in a clan.");
					return;
				}

				Optional<Clan> optionalClan = clanManager.getClanByName(clanName);
				if (optionalClan.isPresent()) {
					Clan clan = optionalClan.get();
					clan.getPlayers().add(clanPlayer);
					MessageUtils.sendCoreMessage(player, "&aYou have been added to the clan " + clanName + ".");
				} else {
					MessageUtils.sendCoreMessage(player, "&cClan not found.");
				}
			}
		}

		@Subcommand("admin showclaims")
		public void showClaims(Player player, @Default("100") int radius, @Default("on") String state) {
			if (!state.equalsIgnoreCase("on") && !state.equalsIgnoreCase("off")) {
				MessageUtils.sendCoreMessage(player, "&cUse a valid parameter: on or off");
				return;
			}

			if (state.equalsIgnoreCase("on")) {
				MessageUtils.sendCoreMessage(player, "&aShowing claims in a " + radius + " block radius.");
				claimManager.showNearbyClaims(player, radius);
			} else {
				claimManager.stopShowingNearbyClaims(player, radius);
				MessageUtils.sendCoreMessage(player, "&aNo longer showing claims in a " + radius + " block radius.");
			}
		}

		@Subcommand("admin overview")
		public void onTest(Player player, String rankName) {
			final ClanRank clanRank = rankManager.getRankByIdentifier(rankName).get();
			player.sendMessage("identifier: " + clanRank.getIdentifier());
			clanRank.getPermissions().forEach(perm -> player.sendMessage(perm.name()));
			player.sendMessage("is leader:" + String.valueOf(clanRank.isLeader()));
			player.sendMessage("display:" + clanRank.getDisplayName());
		}


		@Subcommand("admin testinv")
		public void onMsg(Player player) {
			new TestGui(player, playerManager.getClanPlayer(player).getClan().get(), dependencyFactory).open();
		}
	}