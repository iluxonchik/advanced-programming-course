package pva.lab03;

import pva.lab03.annotations.Test;

public class Foo {
    @Test
    public static void m1() { /* empty  */ }

    @Test
    public static void m3() {
        throw new RuntimeException("Runtime Exception");
    }

    public static void m4() { /* empty  */ }

    @Test public static void m5() { /* empty  */  }
    
    public static void m6() { }

    @Test public static void m7() {
        throw new RuntimeException("Another exception");
    }

    public static void m8() { /* empty  */ }

}
