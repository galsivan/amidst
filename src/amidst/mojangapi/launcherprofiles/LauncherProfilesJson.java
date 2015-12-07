package amidst.mojangapi.launcherprofiles;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import amidst.mojangapi.MojangAPI;
import amidst.mojangapi.dotminecraft.ProfileDirectory;

public class LauncherProfilesJson {
	private Map<String, LauncherProfileJson> profiles = Collections.emptyMap();

	public LauncherProfilesJson() {
		// no-argument constructor for gson
	}

	public LauncherProfileJson getProfile(String name) {
		return profiles.get(name);
	}

	public Collection<LauncherProfileJson> getProfiles() {
		return profiles.values();
	}

	public List<ProfileDirectory> createProfileDirectories() {
		return MojangAPI.createProfileDirectories(this);
	}
}
