package dev.santih.shootclans.managers.impl;

import dev.santih.shootclans.clan.Clan;
import dev.santih.shootclans.clan.rank.ClanRank;
import dev.santih.shootclans.clan.rank.CustomRank;
import dev.santih.shootclans.clan.rank.impl.*;
import dev.santih.shootclans.managers.interfaces.RankManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RankManagerImpl implements RankManager {
	private final List<ClanRank> unmodifiableRanks;

	public RankManagerImpl() {
		final List<ClanRank> loadedRanks = Arrays.asList(
				new RankMember(),
				new RankCaptain(),
				new RankCoLeader(),
				new RankLeader()
		);

		unmodifiableRanks = Collections.unmodifiableList(loadedRanks);

	}

	@Override
	public Optional<ClanRank> getRankByIdentifier(final String identifier) {
		return unmodifiableRanks.stream().filter(rank -> rank.getIdentifier().equals(identifier)).findFirst();
	}

	@Override
	public ClanRank getMemberRank() {
		return getRankByIdentifier("Member").orElse(null);
	}

	@Override
	public ClanRank getCaptainRank() {
		return getRankByIdentifier("Captain").orElse(null);
	}

	@Override
	public ClanRank getCoLeaderRank() {
		return getRankByIdentifier("Co-Leader").orElse(null);
	}

	@Override
	public ClanRank getLeaderRank() {
		return getRankByIdentifier("Leader").orElse(null);
	}

	@Override
	public CustomRank createCustomRank(final Clan clan) {
		final RankCustom createdRank = new RankCustom();
		clan.addCustomRank(createdRank);

		return createdRank;
	}

	@Override
	public List<ClanRank> getLoadedRanks() {
		return unmodifiableRanks;
	}
}
