package pva.lab03;

import pva.lab03.annotations.Test;

/**
     $ java RunTests TestSimple
     m1
     Test public static void TestSimple.m1() OK!
     m2
     Test private static void TestSimple.m2() OK!
     Test private static void TestSimple.m3() failed
     Passed: 2, Failed 1
 */
public class TestSimple {
    @Test public static void m1() { System.out.println("m1"); }
    @Test private static void m2() { System.out.println("m2"); }
    @Test private static void m3() { throw new RuntimeException("problem"); }
    public static void m4() { System.out.println("m4"); }
}
