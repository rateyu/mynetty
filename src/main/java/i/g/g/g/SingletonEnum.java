package i.g.g.g;

public enum SingletonEnum {
    INSTANCE;
    public long doSomething () {
        System.out.println("我是一个单例！");
        return 0;
    }

    public static void main(String[] args) {
        int a = 50;
        while (a-->0) {
            new Thread(()->{
                System.out.println(SingletonEnum.INSTANCE);
            }).start();
        }
    }
}
