package pva.lab03.task;

import pva.lab03.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Task Runner which is responsible for running the tests and reporiting the results.
 */
public class TaskRunner {
    private String testClass;

    private int passed = 0;
    private int failed = 0;

    private ArrayList<String> passedMeth = new ArrayList<>();
    private ArrayList<String> failedMeth = new ArrayList<>();

    public TaskRunner(String testClass) {
        this.testClass = testClass;
    }

    private void printResults() {
        System.out.printf("Passed: %d, Failed: %d%n", passed, failed);

        System.out.println("Passed Methods:");
        for (String methName : passedMeth) {
            System.out.println("\t" + methName);
        }

        System.out.println("Failed Methods:");
        for (String methName : failedMeth) {
            System.out.println("\t" + methName);
        }
    }

    public void run() throws Exception {
        Method[] methods = Class.forName(testClass).getDeclaredMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(Test.class)) {
                try {
                    m.setAccessible(true); // suppress Java access checking (i.e. allow invoking private methods)
                    m.invoke(null); // null: invoking a static method (on a "null" object)
                    passed++;
                    passedMeth.add(m.getName());
                } catch (Throwable ex) {
                    System.out.printf("Test %s failed: %s %n", m, ex.getMessage());
                    failed++;
                    failedMeth.add(m.getName());
                }
            }
        }
        printResults();
    }
}
