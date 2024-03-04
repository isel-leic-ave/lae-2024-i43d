class Outer {
    public static void main(String[] args) {
        final Outer obj = new Outer("ISEL");
        final Xpto x = obj.new Xpto();
        x.print();
    }

    private final String name; 

    public Outer(String name) {
        this.name = name;
    }

    class Nested {
        
    }

    class Xpto {
        /**
         * An additional implicit Field of type Outer
         */
        public void print() {
            System.out.println("Outer name = " + Outer.this.name);
        }
    }
}