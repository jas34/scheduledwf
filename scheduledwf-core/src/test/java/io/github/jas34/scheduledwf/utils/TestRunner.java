package io.github.jas34.scheduledwf.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * @author Jasbir Singh
 */
public class TestRunner extends BlockJUnit4ClassRunner {

    private Injector injector;

    /**
     * Creates a BlockJUnit4ClassRunner to run {@code klass}
     *
     * @param klass
     * @throws InitializationError if the test class is malformed.
     */
    public TestRunner(Class<?> klass) throws InitializationError {
        super(klass);
        injector = Guice.createInjector(new TestModule());
    }

    @Override
    protected Object createTest() throws Exception {
        Object test = super.createTest();
        injector.injectMembers(test);
        return test;
    }

}
