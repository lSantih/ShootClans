package dev.santih.shootclans.managers.impl;

import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.managers.interfaces.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManagerImpl implements PlayerManager {
	private final Map<UUID, ClanPlayer> players = new HashMap<>();

	@Override
	public void loadPlayer(Player player) {
		UUID uuid = player.getUniqueId();
		players.put(uuid, new ClanPlayer(uuid));

	}

	@Override
	public void unloadPlayer(Player player) {
		players.remove(player.getUniqueId());
	}

	@Override
	public ClanPlayer getClanPlayer(Player player) {
		return players.get(player.getUniqueId());
	}

	@Override
	public void cacheAllOnline() {
		Bukkit.getOnlinePlayers().forEach(this::loadPlayer);
	}


}
