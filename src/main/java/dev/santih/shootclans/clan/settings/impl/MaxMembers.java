package dev.santih.shootclans.clan.settings.impl;

import dev.santih.shootclans.clan.settings.ClanSettingType;
import dev.santih.shootclans.clan.settings.ClanSettings;

public class MaxMembers implements ClanSettings<Integer> {
	private int maxMembersValue = 30;

	@Override
	public String getName() {
		return "Max Members";
	}

	@Override
	public ClanSettingType getType() {
		return ClanSettingType.MEMBERS;
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
