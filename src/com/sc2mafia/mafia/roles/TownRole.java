package com.sc2mafia.mafia.roles;

import com.sc2mafia.mafia.Role;

public class TownRole extends Role {

    @Override
    public Alignment getAlignment() {
	return Role.Alignment.TOWN;
    }

}
