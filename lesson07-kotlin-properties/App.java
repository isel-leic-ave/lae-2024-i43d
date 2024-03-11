public class App {
    public static void main(String[] args) {
        Person p = new Person("Maria", 73264);
        System.out.println(p.getName());
        System.out.println(p.getNr());
        p.setName("Maria Silva");
        System.out.println(p.getName());
        
    }
}