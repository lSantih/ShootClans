package dev.santih.shootclans.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import dev.santih.shootclans.clan.Clan;
import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.clan.Permission;
import dev.santih.shootclans.managers.factory.dependency.DependencyFactory;
import dev.santih.shootclans.managers.interfaces.EconomyManager;
import dev.santih.shootclans.managers.interfaces.PlayerManager;
import dev.santih.shootclans.utils.MessageUtils;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

@CommandAlias("clan")
public class ClanBankCommands extends BaseCommand {
	private final PlayerManager playerManager;
	private final EconomyManager economyManager;

	public ClanBankCommands(DependencyFactory dependencyFactory) {
		this.playerManager = dependencyFactory.getPlayerManager();
		this.economyManager = dependencyFactory.getEconomyManager();
	}

	@Subcommand("balance")
	@CommandPermission("clan.balance")
	public void onBalance(Player player) {
		ClanPlayer clanPlayer = playerManager.getClanPlayer(player);
		Clan clan = clanPlayer.getClan().orElse(null);
		if (clan == null) {
			MessageUtils.sendCoreMessage(player, "&cYou are not in a clan.");
			return;
		}

		if (!clanPlayer.hasPermission(Permission.VIEW_BALANCE)) {
			MessageUtils.sendCoreMessage(player, "&cYou do not have permission to use this command.");
			return;
		}

		double balance = clan.getBalance();
		MessageUtils.sendCoreMessage(player, "&aClan balance: &6" + economyManager.getEconomy().format(balance));
	}

	@Subcommand("deposit")
	@CommandPermission("clan.deposit")
	public void onDeposit(Player player, double amount) {
		if (amount <= 0) {
			MessageUtils.sendCoreMessage(player, "&cInvalid amount.");
			return;
		}

		ClanPlayer clanPlayer = playerManager.getClanPlayer(player);
		Clan clan = clanPlayer.getClan().orElse(null);
		if (clan == null) {
			MessageUtils.sendCoreMessage(player, "&cYou are not in a clan.");
			return;
		}

		if (!clanPlayer.hasPermission(Permission.DEPOSIT_BALANCE)) {
			MessageUtils.sendCoreMessage(player, "&cYou do not have permission to use this command.");
			return;
		}

		double playerBalance = economyManager.getEconomy().getBalance(player);
		if (playerBalance < amount) {
			MessageUtils.sendCoreMessage(player, "&cInsufficient funds.");
			return;
		}

		EconomyResponse response = economyManager.getEconomy().withdrawPlayer(player, amount);
		if (response.transactionSuccess()) {
			clan.addBalance(amount);
			MessageUtils.sendCoreMessage(player, "&aSuccessfully deposited &6" + economyManager.getEconomy().format(amount) + " &ainto the clan balance.");
		} else {
			MessageUtils.sendCoreMessage(player, "&cFailed to deposit money: " + response.errorMessage);
		}
	}

	@Subcommand("withdraw")
	@CommandPermission("clan.withdraw")
	public void onWithdraw(Player player, double amount) {
		if (amount <= 0) {
			MessageUtils.sendCoreMessage(player, "&cInvalid amount.");
			return;
		}

		ClanPlayer clanPlayer = playerManager.getClanPlayer(player);
		Clan clan = clanPlayer.getClan().orElse(null);
		if (clan == null) {
			MessageUtils.sendCoreMessage(player, "&cYou are not in a clan.");
			return;
		}

		if (!clanPlayer.hasPermission(Permission.WITHDRAW_BALANCE)) {
			MessageUtils.sendCoreMessage(player, "&cYou do not have permission to use this command.");
			return;
		}

		double clanBalance = clan.getBalance();
		if (amount > clanBalance) {
			MessageUtils.sendCoreMessage(player, "&cInsufficient funds.");
			return;
		}

		EconomyResponse response = economyManager.getEconomy().depositPlayer(player, amount);
		if (response.transactionSuccess()) {
			clan.removeBalance(amount);
			MessageUtils.sendCoreMessage(player, "&aSuccessfully withdrew &6" + economyManager.getEconomy().format(amount) + " &afrom the clan balance.");
		} else {
			MessageUtils.sendCoreMessage(player, "&cFailed to withdraw money: " + response.errorMessage);
		}
	}
}