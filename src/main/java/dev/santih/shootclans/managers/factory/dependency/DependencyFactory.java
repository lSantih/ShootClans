package dev.santih.shootclans.managers.factory.dependency;

import dev.santih.shootclans.managers.interfaces.*;

public interface DependencyFactory {
	PlayerManager createPlayerManager();

	DependencyManager createDependencyManager();

	EconomyManager createEconomyManager();

	ClaimManager createClaimManager();

	ClanManager createClanManager();

	RankManager createRankManager();

	RequestManager createRequestManager();

	PlayerManager getPlayerManager();

	EconomyManager getEconomyManager();

	ClaimManager getClaimManager();

	ClanManager getClanManager();

	RankManager getRankManager();


	RequestManager getRequestManager();

	DependencyManager getDependencyManager();

}
