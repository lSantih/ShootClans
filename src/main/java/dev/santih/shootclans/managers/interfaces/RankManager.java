package dev.santih.shootclans.managers.interfaces;

import dev.santih.shootclans.clan.Clan;
import dev.santih.shootclans.clan.rank.ClanRank;
import dev.santih.shootclans.clan.rank.CustomRank;

import java.util.List;
import java.util.Optional;

public interface RankManager {
	Optional<ClanRank> getRankByIdentifier(String identifier);

	ClanRank getMemberRank();

	ClanRank getCaptainRank();

	ClanRank getCoLeaderRank();

	ClanRank getLeaderRank();

	CustomRank createCustomRank(Clan clan);

	List<ClanRank> getLoadedRanks();
}
