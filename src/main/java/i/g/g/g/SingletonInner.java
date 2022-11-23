package i.g.g.g;

public class Singleton {
    private Singleton() {
    }
    private static class Inner {
        private static final Singleton instance = new Singleton();
    }
    public static Singleton getInstance() {
        return Inner.instance;
    }

    public static void main(String[] args) {
        System.out.println(Singleton.getInstance());
    }
}
