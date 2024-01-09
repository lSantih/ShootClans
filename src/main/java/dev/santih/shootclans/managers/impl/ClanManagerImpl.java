package dev.santih.shootclans.managers.impl;

import dev.santih.shootclans.clan.Clan;
import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.exception.AlreadyInClanException;
import dev.santih.shootclans.managers.interfaces.ClanManager;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ClanManagerImpl implements ClanManager {
	private Set<Clan> clans = new HashSet<>();

	@Override
	public void createClan(String name, ClanPlayer owner) throws AlreadyInClanException {
		if (owner.getClan().isPresent()) {
			throw new AlreadyInClanException("Player is already in a clan - he can not create another.");
		}
		Clan clan = new Clan(name);
		clan.addPlayer(owner);
		owner.setLeader(true);
		owner.setClan(Optional.of(clan));
		clans.add(clan);
	}

	@Override
	public void deleteClan(Clan clan) {
		clans.remove(clan);
		for (ClanPlayer player : clan.getPlayers()) {
			player.setClan(Optional.empty());
			player.setLeader(false);
		}
	}

	@Override
	public Optional<Clan> getClanByName(String name) {
		return clans.stream()
				.filter(clan -> clan.getName().equals(name))
				.findFirst();
	}

	@Override
	public Optional<Clan> getClanByPlayerName(String playerName) {
		return clans.stream()
				.filter(clan -> clan.getPlayers().stream().anyMatch(player -> player.getName().equals(playerName)))
				.findFirst();
	}

	@Override
	public Set<Clan> getClans() {
		return clans;
	}
}
