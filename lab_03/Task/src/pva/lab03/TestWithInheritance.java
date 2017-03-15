package pva.lab03;

import pva.lab03.annotations.Setup;
import pva.lab03.annotations.Test;

/**
     $ java RunTests TestInheritance
     s2
     s1
     Test public static void TestWithSetup.m5() failed
     s2
     s1
     m6
     Test private static void TestWithSetup.m6() OK!
     s1
     s2
     Test private static void TestWithSetup.m7() failed
     s1
     m8
     Test private static void TestWithSetup.m8() OK!
     s2
     s1
     Test public static void TestInheritance.m10() failed
     s1
     m12
     Test public static void TestInheritance.m12() OK!
     s1
     s4
     m14
     Test public static void TestInheritance.m14() OK!
     Passed: 4, Failed 3
 */
public class TestWithInheritance {
    @Setup("s4") protected static void s5() { System.out.println("s4"); }

    @Test("*") public static void m10() { System.out.println("m10"); }
    @Test("s2") public void m11() { System.out.println("m11"); }
    @Test("s1") public static void m12() { System.out.println("m12"); }
    public static void m13() { System.out.println("m13"); }
    @Test({"s1", "s4"}) public static void m14() { System.out.println("m14"); }
}
