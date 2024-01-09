package dev.santih.shootclans.managers.impl;

import dev.santih.shootclans.managers.interfaces.EconomyManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManagerImpl implements EconomyManager {

	private Economy economy;

	public EconomyManagerImpl() {
		final RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp != null) {
			economy = rsp.getProvider();
		}
	}

	@Override
	public Economy getEconomy() {
		return economy;
	}
}