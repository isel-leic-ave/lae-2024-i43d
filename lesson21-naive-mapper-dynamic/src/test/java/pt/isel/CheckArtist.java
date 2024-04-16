package pt.isel;

public class CheckArtist {

    static void foo(int a, Integer b, int c) {}
    static void foo(int a, Integer b) {
        foo(a, b, 11);
    }
    static void foo(int a) {
        foo(a, 7);
    }

    public void testArtist() {
        foo(3123);

        new Artist("Jose");
        new Artist("Jose", new State("pt", "pt"));

    }
}
