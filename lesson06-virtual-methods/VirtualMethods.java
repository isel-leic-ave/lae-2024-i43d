public class VirtualMethods {
    public static void main(String[] args) {
        
    }
}

class A {
    public void v() {
        System.out.println("I am A");
    }
    public final void nv() {
        System.out.println("I am A");
    }
}
class B extends A {
    /**
     * Implict Overriding method v
     */
    public void v() {
        System.out.println("I am B");
    }
    /**
     * nv() in B cannot override nv() in A
     * because nv() is not virtual (marked final)
     */
    public void nv() {
        System.out.println("I am A");
    }
}