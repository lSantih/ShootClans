package dev.santih.shootclans.clan.request;

import dev.santih.shootclans.clan.Clan;
import dev.santih.shootclans.clan.ClanPlayer;
import dev.santih.shootclans.exception.AlreadyInClanException;
import dev.santih.shootclans.exception.AlreadyInThisClanException;
import dev.santih.shootclans.exception.NotInClanException;

public interface Request {
	ClanPlayer sender();

	ClanPlayer receiver();

	ClanRequestType getRequestType();

	default Clan getSenderClan() {
		return sender().getClan().orElse(null);
	}

	default Clan getReceiverClan() {
		return receiver().getClan().orElse(null);
	}

	void processRequest(ClanPlayer player) throws AlreadyInClanException, AlreadyInThisClanException, NotInClanException;
}
