package net.jas34.scheduledwf.concurrent;

import java.util.Optional;

/**
 * @author Jasbir Singh
 */
public interface PermitDAO {

	void insertOrUpdate(Permit permit);

	Optional<Permit> fetchByName(String name);
}
