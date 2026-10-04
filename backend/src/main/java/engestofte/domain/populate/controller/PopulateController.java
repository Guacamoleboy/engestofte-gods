package engestofte.domain.populate.controller;

import engestofte.domain.populate.PopulateDB;
import engestofte.util.TryCatchHelper;
import io.javalin.http.Context;
import java.util.Map;

public class PopulateController {

	// Attributes
	private final PopulateDB populateDB;

	// _________________________________________________________________________________________________________________

	public PopulateController(PopulateDB populateDB) {
		this.populateDB = populateDB;
	}

	// _________________________________________________________________________________________________________________

	public void populateRoles(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			populateDB.populateRoles();
			return Map.of("roles", new String[]{"CUSTOMER", "STAFF", "OWNER"});
		}, "Roles populated");
	}

	// _________________________________________________________________________________________________________________

	public void restart(Context context) {
		TryCatchHelper.tryCatchHelper(context, populateDB::restart, "Database restarted");
	}

	// _________________________________________________________________________________________________________________

	public void createOwners(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			populateDB.createOwners();
			return Map.of("emails", new String[]{"johan@johan.dk", "lise@lise.dk", "mette@mette.dk"});
		}, "Owner accounts populated");
	}
}
