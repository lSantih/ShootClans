package dev.santih.shootclans.managers.factory;

import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.clan.request.ClanRequestType;
import dev.santih.shootclans.clan.request.Request;

public interface RequestFactory {
	Request createRequest(ClanRequestType requestType, ClanPlayer sender, ClanPlayer receiver);

}
