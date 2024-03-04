public class App {  
    public static void main(String[] args) {
        foo(7);
    }

    public static void foo(Object obj) {}

    public static Integer incSeven(Integer n) {
        // Unboxing of n to int => intValue()
        // At the end Boxing the result to Integer 
        // according to the Return type => Integer.valueOf()
        return n + 7;
    }
}
