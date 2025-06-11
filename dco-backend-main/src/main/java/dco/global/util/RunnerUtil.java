package dco.global.util;

public class RunnerUtil {
    public static Long getMaxRuntime(long maxRuntime, long startTime, long endTime) {
        long runtime = endTime - startTime;
        if (runtime > maxRuntime) {
            maxRuntime = runtime;
        }
        return maxRuntime;
    }

    public static float calculateScore(int correctCount, int totalTestCases) {
        float score = (float) correctCount / totalTestCases * 100;
        return Math.round(score);
    }
}
