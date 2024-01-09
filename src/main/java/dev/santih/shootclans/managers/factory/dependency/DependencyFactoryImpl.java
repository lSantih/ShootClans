package dev.santih.shootclans.managers.factory.dependency;

import dev.santih.shootclans.ShootClans;
import dev.santih.shootclans.managers.factory.DefaultRequestFactory;
import dev.santih.shootclans.managers.impl.*;
import dev.santih.shootclans.managers.interfaces.*;

import java.util.function.Supplier;

public class DependencyFactoryImpl implements DependencyFactory {
	private final ShootClans shootClans;
	private final DefaultRequestFactory factory;
	private final PlayerManager playerManager;
	private final EconomyManager economyManager;
	private final ClaimManager claimManager;
	private final ClanManager clanManager;
	private final RankManager rankManager;
	private final RequestManager requestManager;
	private final DependencyManager dependencyManager;

	public DependencyFactoryImpl(final ShootClans shootClans) {
		this.shootClans = shootClans;
		this.factory = new DefaultRequestFactory();
		this.playerManager = createPlayerManager();
		this.economyManager = createEconomyManager();
		this.clanManager = createClanManager();
		this.rankManager = createRankManager();
		this.claimManager = createClaimManager();
		this.requestManager = createRequestManager();
		this.dependencyManager = createDependencyManager();
	}

	private synchronized <T> T initializeIfNull(T instance, Supplier<T> supplier) {
		if (instance == null) {
			instance = supplier.get();
		}
		return instance;
	}

	@Override
	public PlayerManager createPlayerManager() {
		return initializeIfNull(playerManager, PlayerManagerImpl::new);
	}

	@Override
	public EconomyManager createEconomyManager() {
		return initializeIfNull(economyManager, EconomyManagerImpl::new);
	}

	@Override
	public DependencyManager createDependencyManager() {
		return initializeIfNull(dependencyManager, () -> new DependencyManagerImpl(shootClans, "plugins/ShootClans/lib/"));
	}

	@Override
	public ClaimManager createClaimManager() {
		return initializeIfNull(claimManager, () -> new ClaimManagerImpl(shootClans));
	}

	@Override
	public ClanManager createClanManager() {
		return initializeIfNull(clanManager, ClanManagerImpl::new);
	}

	@Override
	public RankManager createRankManager() {
		return initializeIfNull(rankManager, RankManagerImpl::new);
	}


	@Override
	public RequestManager createRequestManager() {
		return initializeIfNull(requestManager, () -> new RequestManagerImpl(factory));
	}

	@Override
	public RankManager getRankManager() {
		return rankManager;
	}

	@Override
	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	@Override
	public EconomyManager getEconomyManager() {
		return economyManager;
	}

	@Override
	public ClanManager getClanManager() {
		return clanManager;
	}

	@Override
	public ClaimManager getClaimManager() {
		return claimManager;
	}


	@Override
	public RequestManager getRequestManager() {
		return requestManager;
	}

	@Override
	public DependencyManager getDependencyManager() {
		return dependencyManager;
	}
}