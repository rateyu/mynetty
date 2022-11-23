package i.g.g.g;

public class SingletonEnumB {
    private SingletonEnumB() {
    }
    public static enum SingletonMyEnum {
        SINGLETON;
        private SingletonEnumB instance = null;
        private SingletonMyEnum(){
            instance = new SingletonEnumB();
        }
        public SingletonEnumB getInstance(){
            return instance;
        }
    }

    public static void main(String[] args) {
        System.out.println(SingletonMyEnum.SINGLETON.getInstance());
    }
}
