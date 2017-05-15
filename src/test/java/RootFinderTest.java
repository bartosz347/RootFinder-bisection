import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class RootFinderTest {
    @Test
    public void findRootsTest() throws Exception {
        ArrayList<Double> coefficients = new ArrayList<>(Arrays.asList(-6d, 11d, -6d, 1d));
        double epsilon = 0.0001d;

        RootFinder testRootFinder = new RootFinder(coefficients, epsilon, 1000);
        assertEquals(1d, testRootFinder.findRoots(0, 1.5d), epsilon);
    }

    @Test
    public void rootIsGivenInEndpoint() throws Exception {
        ArrayList<Double> coefficients = new ArrayList<>(Arrays.asList(0d,1d));
        double epsilon = 0.0001d;

        RootFinder testRootFinder = new RootFinder(coefficients, epsilon, 1000);
        assertEquals(0d, testRootFinder.findRoots(0, 6), epsilon);
    }


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void whenGivenEndpointsThatEvaluateToSameSignAnExceptionIsThrown() throws RuntimeException {
        ArrayList<Double> coefficients = new ArrayList<>(Arrays.asList(0d, 1d));
        RootFinder testRootFinder = new RootFinder(coefficients,0.01,100);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("wrong endpoints, function value has same sign for both endpoints");
        testRootFinder.findRoots(1,2);
    }

    @Test
    public void whenMaxAttemptsNumberExceededAnExceptionIsThrown() throws RuntimeException {
        ArrayList<Double> coefficients = new ArrayList<>(Arrays.asList(0d, 1d));
        RootFinder testRootFinder = new RootFinder(coefficients,0.01,10);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("no root found, max number of attempts exceeded");
        testRootFinder.findRoots(-110,150);
    }

    @Test
    public void whenGivenLeftEndpointIsGreaterThenRightEndpointAnExceptionIsThrown() throws RuntimeException {
        ArrayList<Double> coefficients = new ArrayList<>(Arrays.asList(0d, 1d));
        RootFinder testRootFinder = new RootFinder(coefficients,0.01,10);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage("left endpoint greater than right endpoint");
        testRootFinder.findRoots(100,-100);
    }

}