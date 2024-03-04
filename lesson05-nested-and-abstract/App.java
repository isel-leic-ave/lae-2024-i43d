public class App {
    public static void main(String[] args) {
        Person p = new Student("Maria");
        System.out.println(p.name);
        Student s = (Student) p;  // p as Student
        System.out.println(s.name);
        B b = new B();
        b.print();
        A a = b;
        a.print();
    }
}

class A {
    public void print() {
        System.out.println("I am A");
    }
}
class B extends A {
    public void print() {
        System.out.println("I am B");
    }
}

class Person {
    public String name = "empty";

    public Person() {
    }
    public Person(String name) {
        this.name = name;
    }
}

class Student extends Person {
    public String name;

    public Student(String name) {
        this.name = name;
    }
}