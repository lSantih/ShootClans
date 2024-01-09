package dev.santih.shootclans.clan.settings.impl;

import dev.santih.shootclans.clan.settings.ClanSettingType;
import dev.santih.shootclans.clan.settings.ClanSettings;

public class MaxRanks implements ClanSettings<Integer> {
	private int maxMembersValue = 5;

	@Override
	public String getName() {
		return "Max Ranks";
	}

	@Override
	public ClanSettingType getType() {
		return ClanSettingType.MAX_RANKS;
	}

	@Override
	public Integer getCurrentValue() {
		return maxMembersValue;
	}

	@Override
	public void setCurrentValue(Integer newValue) {
		this.maxMembersValue = newValue;
	}
}
