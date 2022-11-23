package i.g.g.g;

import java.math.BigDecimal;

public class SingletonDB {
    private volatile static SingletonDB singletonDB;
    private SingletonDB () {}
    public static SingletonDB getInstance() {
        if (singletonDB == null) {
            synchronized (SingletonDB.class) {
                if (singletonDB == null) {
                    singletonDB = new SingletonDB();
                    System.out.println("SingletonDB");
                }
            }
        }
        return singletonDB;
    }

    public static void main(String[] args) {
        int a = 50;
        while (a-->0) {
            new Thread(()->{
                System.out.println(SingletonDB.getInstance());
            }).start();
        }
//        new BigDecimal();
    }
}
