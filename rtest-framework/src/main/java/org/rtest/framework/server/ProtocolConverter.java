package org.rtest.framework.server;

import org.junit.ComparisonFailure;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.rtest.framework.common.data.ComparisonFailureContainer;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Mark Bramnik on 21/09/2016.
 */
/* in spock 1.1-rc-2 there is a defect (https://github.com/spockframework/spock/issues/647) -
   the class that denotes a comparison failure
   is not serializable, so can't be transmitted through the network.
   so in this case we'll create our own failure object
*/

public class ProtocolConverter {

    private void substituteFailure(Failure destFailure, Failure sourceOfSubstitution) {
        try {
            Field f = destFailure.getClass().getDeclaredField("fThrownException");
            f.setAccessible(true);
            f.set(destFailure, sourceOfSubstitution.getException());
            f.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
    public Result convertResult(Result result) {
         List<Failure> failures = result.getFailures();
         for(Failure failure : failures) {
             if(shouldConvertFailure(failure)) {
                Failure convertedFailure = convertFailure(failure);
                substituteFailure(failure, convertedFailure);
             }
         }
         return result;
    }
    private boolean shouldConvertFailure(Failure failure) {
         return (failure.getException() instanceof ComparisonFailure);
    }
    public Failure convertFailure(Failure failure) {

        Failure currentFailure = failure;
        if(shouldConvertFailure(failure)) {
            Throwable failureException = failure.getException();
            ComparisonFailure comparisonFailure = (ComparisonFailure) failureException;
            ComparisonFailureContainer newComparisonFailureContainer =
                    new ComparisonFailureContainer(
                            comparisonFailure.getMessage(),
                            comparisonFailure.getExpected(),
                            comparisonFailure.getActual(),
                            currentFailure.getException().getCause(),
                            currentFailure.getException().getStackTrace());
            //StackTraceElement[] stackTrace = currentFailure.getStackTrace();
            currentFailure = new Failure(failure.getDescription(), newComparisonFailureContainer);
        }
        return currentFailure;
    }
}
