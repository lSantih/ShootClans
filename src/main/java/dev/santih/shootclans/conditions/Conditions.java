package dev.santih.shootclans.conditions;

import dev.santih.shootclans.clan.ClanPlayer;

public class Conditions {
	public static boolean isInSameClan(ClanPlayer player1, ClanPlayer player2) {
		return player1.getClan().isPresent() && player2.getClan().isPresent() && player1.getClan().get().equals(player2.getClan().get());
	}
}
