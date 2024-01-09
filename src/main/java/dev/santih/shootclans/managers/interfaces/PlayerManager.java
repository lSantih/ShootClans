package dev.santih.shootclans.managers.interfaces;

import dev.santih.shootclans.clan.ClanPlayer;
import org.bukkit.entity.Player;

public interface PlayerManager {
	void loadPlayer(Player player);

	void unloadPlayer(Player player);

	ClanPlayer getClanPlayer(Player player);

	void cacheAllOnline();
}
