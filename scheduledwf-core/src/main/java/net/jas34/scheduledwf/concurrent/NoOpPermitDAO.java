package net.jas34.scheduledwf.concurrent;

import java.util.Optional;

/**
 * @author Jasbir Singh
 */
public class NoOpPermitDAO implements PermitDAO {
	@Override
	public void insertOrUpdate(Permit permit) {
		//do nothing
	}

	@Override
	public Optional<Permit> fetchByName(String name) {
		return Optional.of(new Permit(name));
	}
}
