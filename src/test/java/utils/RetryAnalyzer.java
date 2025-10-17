package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int maxRetryCount = 2; // will retry 2 times

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            System.out.println("Retrying test " + result.getName() + " again, attempt " + (retryCount+1));
            
            // Mark test as flaky if retry occurs
            result.setAttribute("isFlaky", true);
            result.setAttribute("retryCount", retryCount);
            
            
            return true;
        }
        return false;
    }
}
