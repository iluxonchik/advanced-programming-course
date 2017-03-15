package pva.lab03.task;

import pva.lab03.annotations.Setup;
import pva.lab03.annotations.Test;
import sun.java2d.pipe.AAShapePipe;

import javax.sound.midi.SysexMessage;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Task Runner which is responsible for running the tests and reporiting the results.
 */
public class TaskRunner {
    private String testClass;

    private int passed = 0;
    private int failed = 0;

    private ArrayList<String> passedMeth = new ArrayList<>();
    private ArrayList<String> failedMeth = new ArrayList<>();

    private HashMap<String, Method> setupMethods = new HashMap<>();
    private ArrayList<Method> testMethods = new ArrayList<>();

    private HashMap<Method, Collection<Method>> testMethodOrder = new HashMap<>();

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

    private void parseSetupMethods(Method[] methods) {
        Setup setupAnnotation;
        for (Method m : methods) {
            if (m.isAnnotationPresent(Setup.class)) {
                if (!Modifier.isStatic(m.getModifiers())) {
                    continue;
                }
                m.setAccessible(true);
                setupAnnotation = m.getAnnotation(Setup.class);
                setupMethods.put(setupAnnotation.value(), m);
            }
        }
    }

    private void parseTestMethods(Method[] methods) {
        for (Method m : methods) {
            if (m.isAnnotationPresent(Test.class)) {
                if (!Modifier.isStatic(m.getModifiers())) {
                    continue;
                }
                m.setAccessible(true); // suppress Java access checking (i.e. allow invoking private methods)
                testMethods.add(m);
            }
        }
    }

    private void parseTestMethodOrder() {
        Test testAnnotation;
        String[] setupNames;
        Collection<Method> methodList;
        for (Method m : testMethods) {
            testMethodOrder.putIfAbsent(m, new ArrayList<>()); // init if needed
            methodList = testMethodOrder.get(m);

            testAnnotation = m.getAnnotation(Test.class);
            setupNames = testAnnotation.value();
            for (String setupMethodName : setupNames) {
                if (setupMethodName.equals(Test.SETUP_ALL)) {
                    testMethodOrder.put(m, setupMethods.values());
                    break; // ignoring other values (even if present)
                }
                methodList.add(setupMethods.get(setupMethodName));
            }
        }
    }

    private void runTestMethods() {
        boolean failTest = false;
        for (Method m : testMethods) {
            for (Method setupMethod : testMethodOrder.get(m)) {
                try {
                    setupMethod.invoke(null);
                } catch (Throwable e) {
                    failTest = true;
                    System.out.printf("Setup method %s FAILED!%n", setupMethod.getName());
                    break;
                }
            }

            if (failTest) {
                failTest = false;
                System.out.printf("Test %s failed%n", m.getName());
                failed++;
                failedMeth.add(m.getName());
                continue;
            }

            try {
                m.invoke(null);
                passed++;
                passedMeth.add(m.getName());
                System.out.printf("%s PASSED!%n", m.getName());
            } catch (Throwable ex) {
                System.out.printf("Test %s failed: %s %n", m, ex);
                failed++;
                failedMeth.add(m.getName());
            }
        }
    }


    public void run() throws Exception {
        ArrayList<Class> classes = getClassList();
        for (Class c : classes) {
            System.out.println("DBG2: Running for class " + c.getName());
            Method[] methods = c.getDeclaredMethods();
            parseSetupMethods(methods);
            parseTestMethods(methods);
            parseTestMethodOrder();

            runTestMethods();
            testMethods.clear();
        }
        printResults();
    }

    private ArrayList<Class> getClassList() {
        ArrayList<Class> classes = new ArrayList<>();
        try {
            Class c = Class.forName(testClass);
            while (! (c.getName().equals(Object.class.getName()))) {
                System.out.println("DBG: Adding class " + c.getName());
                classes.add(0, c);
                c = c.getSuperclass();
            }
            return classes;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classes;
    }
}
