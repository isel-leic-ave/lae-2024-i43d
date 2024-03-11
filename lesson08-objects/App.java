public class App {
    public static void main(String[] args) {
        // new B(); // Error => object has a private constructor
        // B.m(); // ERROR => non-static method m()
        B.INSTANCE.m();
    }
}