package pva.lab03;

import pva.lab03.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        ArrayList<String> passedMeth = new ArrayList<>();
        ArrayList<String> failedMeth = new ArrayList<>();

        int passed = 0, failed = 0;
        Method[] methods = Class.forName(args[0]).getDeclaredMethods();
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
}
