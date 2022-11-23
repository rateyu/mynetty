package i.g.g.g;

public class SingletonInner {
    private SingletonInner() {
    }
    private static class Inner {
        private static final SingletonInner instance = new SingletonInner();
    }
    public static SingletonInner getInstance() {
        return Inner.instance;
    }

    public static void main(String[] args) {
        int a = 50;
        while (a-- > 0) {
            new Thread(() -> {
                System.out.println(SingletonInner.getInstance());
            }).start();
        }
    }
}
