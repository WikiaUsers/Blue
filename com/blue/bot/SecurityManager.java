package com.blue.bot;

import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;

public class PluginPolicy extends Policy {
	public PermissionCollection getPermissions(CodeSource codeSource) {
		Permissions permissions = new Permissions();
		permissions.add(new AllPermission());
		return permissions;
	}

    // Required by interface
	public void refresh() {}
}
