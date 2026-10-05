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
			return Map.of("created", true);
		}, "Owner accounts populated");
	}

	// _________________________________________________________________________________________________________________

	public void staff(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			populateDB.staff();
			return Map.of("created", true, "accounts", new String[]{"kok@kok.dk", "clean@clean.dk"});
		}, "Staff accounts populated");
	}
}
