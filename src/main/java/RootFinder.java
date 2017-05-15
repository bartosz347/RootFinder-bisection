import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import static java.lang.Math.signum;

public class RootFinder {

    private ArrayList<Double> coefficients;
    private double epsilon;
    private int maxAttempts;

    RootFinder(ArrayList<Double> coefficients, double epsilon, int maxAttempts) {
        this.coefficients = coefficients;
        this.epsilon = epsilon;
        this.maxAttempts = maxAttempts;
    }

    public double findRoot(double leftEndpoint, double rightEndpoint)
    {
        int attemptNo = 1;
        if(leftEndpoint > rightEndpoint)
            throw new RuntimeException("left endpoint greater than right endpoint");

        if (getPolynomialValueFor(leftEndpoint) * getPolynomialValueFor(rightEndpoint) < 0) {
            while (attemptNo <= maxAttempts) {
                double middleValue = (leftEndpoint + rightEndpoint) / 2;

                // demanded accuracy achieved or exact root found
                if ((rightEndpoint - leftEndpoint) / 2 < epsilon || isRoot(middleValue))
                    return middleValue;

                // choose left or right subinterval
                if (signum(getPolynomialValueFor(middleValue)) == signum(getPolynomialValueFor(leftEndpoint)))
                    leftEndpoint = middleValue;
                else
                    rightEndpoint = middleValue;

                attemptNo++;
            }
        }
        else if(isRoot(leftEndpoint))
            return leftEndpoint;
        else if(isRoot(rightEndpoint))
            return rightEndpoint;
        else
            throw new RuntimeException("wrong endpoints, function value has same sign for both endpoints");

        throw new RuntimeException("no root found, max number of attempts exceeded");
    }

    private boolean isRoot(double x)
    {
        return getPolynomialValueFor(x) == 0;
    }

    private double getPolynomialValueFor(double x) {
        double sourceX = x;
        double result = coefficients.get(0);
        for (int i = 1; i < coefficients.size(); i++) {
            result += x * coefficients.get(i);
            x *= sourceX;
        }
        return result;
    }

    public static void main(String... args) throws Exception
    {
        // usage: RootFinder <k> <l> <eps> <max> <a0> <a1> ... <an>
        if(args.length < 5) {
            printUsage();
            return;
        }
        double leftEndpoint=0, rightEndpoint=0, epsilon=0;
        int maxAttempts=0;
        ArrayList<Double> coefficients = new ArrayList<>();
        try {
            leftEndpoint = Double.parseDouble(args[0]);
            rightEndpoint = Double.parseDouble(args[1]);
            epsilon = Double.parseDouble(args[2]);
            maxAttempts = Integer.parseInt(args[3]);

            for(int i = 4; i < args.length; i++)
            {
                coefficients.add(Double.parseDouble(args[i]));
            }
        } catch (NumberFormatException e) {
            printUsage();
            return;
        }

        // calculations
        RootFinder runRootFinder = new RootFinder(coefficients, epsilon,maxAttempts);
        double result = runRootFinder.findRoot(leftEndpoint, rightEndpoint);

        // rounding to print proper decimal places
        StringBuilder pattern = new StringBuilder("#.");
        while(epsilon < 1) {
            epsilon *= 10;
            pattern.append("0");
        }
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat)nf;
        df.applyPattern(pattern.toString());
        df.setRoundingMode(RoundingMode.HALF_UP);
        System.out.println("found root is " + df.format(result));
    }

    public static void printUsage()
    {
        System.out.println("wrong arguments");
        System.out.println("usage: RootFinder <k> <l> <eps> <max> <a0> <a1> ... <an>");
        System.out.println("polynomial: a0 + a1*x + a2*x^2 + ... + an*x^n");
        System.out.printf("e.g. parameters: -2 4 0.0001 1000 -1 0 7 1 -1 -1");
    }
}
