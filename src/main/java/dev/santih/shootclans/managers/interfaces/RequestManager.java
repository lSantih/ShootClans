package dev.santih.shootclans.managers.interfaces;

import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.clan.request.ClanRequestType;
import dev.santih.shootclans.clan.request.Request;
import dev.santih.shootclans.exception.AlreadyInClanException;
import dev.santih.shootclans.exception.AlreadyInThisClanException;
import dev.santih.shootclans.exception.NotInClanException;

import java.util.Optional;

public interface RequestManager {
	void sendRequest(ClanPlayer sender, ClanPlayer receiver, ClanRequestType requestType) throws NotInClanException;

	<T extends Request> Optional<T> getRequest(ClanPlayer receiver, Class<T> requestClass);

	<T extends Request> Optional<T> getRequest(ClanPlayer receiver, String clanName, Class<T> requestClass);

	void acceptRequest(ClanPlayer player, Request request) throws AlreadyInClanException, AlreadyInThisClanException, NotInClanException;

	void rejectRequest(Request request);
}
