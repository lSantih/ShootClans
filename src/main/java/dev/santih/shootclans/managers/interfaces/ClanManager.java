package dev.santih.shootclans.managers.interfaces;

import dev.santih.shootclans.clan.Clan;
import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.exception.AlreadyInClanException;

import java.util.Optional;
import java.util.Set;

public interface ClanManager {
	void createClan(String name, ClanPlayer owner) throws AlreadyInClanException;

	void deleteClan(Clan clan);

	Optional<Clan> getClanByName(String name);

	Optional<Clan> getClanByPlayerName(String playerName);

	Set<Clan> getClans();
}
