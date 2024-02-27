package pt.isel;

public class Receivers {
    public static void main(String[] args) {
        perTypeMethod(); // <=> App.perTypeMethod();
 
        final Receivers receiver = new Receivers();
        receiver.perInstanceMethod();
        receiver.perInstanceMethod();
        receiver.perInstanceMethod();
        receiver.perInstanceMethod();
    }
    public void otherInstanceMethod() {
        perTypeMethod(); // <=> App.perTypeMethod();

        perInstanceMethod(); // <=> this.perTypeMethod();
    }

    public void perInstanceMethod() {  }
    public static void perTypeMethod() {  }
}