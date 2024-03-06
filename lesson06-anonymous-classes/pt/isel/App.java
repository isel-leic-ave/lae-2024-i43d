package pt.isel;

public class App {
    public static void main(String[] args) {
        A x = new A() {
            void foo() {
            }
        };
        A y = new A() {
            void foo() {
            }
        };
        new Outer(7).zas().foo();
        new Outer(11).zas().foo();
    }
}

class Outer {
    private final int nr;

    public Outer(int nr) {
        this.nr = nr;
    }
    public A zas() {
        return new A() {
            void foo() {
                System.out.println("I have nr " + Outer.this.nr);
            }
        };
    }
}

class Dummy {
    public A bar() {
        return new A(){
            void foo() {
            }};
    }   
}