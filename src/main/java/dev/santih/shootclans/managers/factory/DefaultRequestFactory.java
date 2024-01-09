package dev.santih.shootclans.managers.factory;

import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.clan.request.AllyRequest;
import dev.santih.shootclans.clan.request.ClanRequestType;
import dev.santih.shootclans.clan.request.InviteRequest;
import dev.santih.shootclans.clan.request.Request;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;

public class DefaultRequestFactory implements RequestFactory {
	private final Map<ClanRequestType, BiFunction<ClanPlayer, ClanPlayer, Request>> creators;

	public DefaultRequestFactory() {
		creators = new EnumMap<>(ClanRequestType.class);
		creators.put(ClanRequestType.INVITE, InviteRequest::new);
		creators.put(ClanRequestType.ALLIANCE, AllyRequest::new);
	}

	@Override
	public Request createRequest(ClanRequestType requestType, ClanPlayer sender, ClanPlayer receiver) {
		BiFunction<ClanPlayer, ClanPlayer, Request> requestCreator = creators.get(requestType);
		if (requestCreator == null) {
			throw new IllegalArgumentException("Unsupported request type: " + requestType);
		}

		return requestCreator.apply(sender, receiver);
	}
}
