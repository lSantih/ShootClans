package dev.santih.shootclans.managers.impl;

import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.clan.request.ClanRequestType;
import dev.santih.shootclans.clan.request.Request;
import dev.santih.shootclans.exception.AlreadyInClanException;
import dev.santih.shootclans.exception.AlreadyInThisClanException;
import dev.santih.shootclans.exception.NotInClanException;
import dev.santih.shootclans.managers.factory.RequestFactory;
import dev.santih.shootclans.managers.interfaces.RequestManager;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RequestManagerImpl implements RequestManager {
	private final Set<Request> requests = new HashSet<>();
	private final RequestFactory requestFactory;

	public RequestManagerImpl(RequestFactory requestFactory) {
		this.requestFactory = requestFactory;
	}

	@Override
	public void sendRequest(ClanPlayer sender, ClanPlayer receiver, ClanRequestType requestType) throws NotInClanException {
		validateClanPlayer(sender);
		Request request = requestFactory.createRequest(requestType, sender, receiver);
		requests.add(request);
	}

	@Override
	public <T extends Request> Optional<T> getRequest(ClanPlayer receiver, Class<T> requestClass) {
		return getRequest(receiver, null, requestClass);
	}

	@Override
	public <T extends Request> Optional<T> getRequest(ClanPlayer receiver, String clanName, Class<T> requestClass) {
		return requests.stream()
				.filter(requestClass::isInstance)
				.filter(request -> request.receiver().equals(receiver))
				.filter(request -> clanName == null || request.getSenderClan().getName().equals(clanName))
				.map(requestClass::cast)
				.findFirst();
	}

	@Override
	public void acceptRequest(ClanPlayer player, Request request) throws AlreadyInClanException, AlreadyInThisClanException, NotInClanException {
		if (requests.remove(request)) {
			request.processRequest(player);
		}
	}

	@Override
	public void rejectRequest(Request request) {
		requests.remove(request);
	}

	private void validateClanPlayer(ClanPlayer clanPlayer) throws NotInClanException {
		if (!clanPlayer.getClan().isPresent()) {
			throw new NotInClanException("The sender is not in a clan, we cannot send a request for a null clan.");
		}
	}
}