package b.b.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyThread{
    Logger logger = LoggerFactory.getLogger(MyThread.class);
    public static void main(String[] args) {
        new MyThread();
    }
    public MyThread() {
        new Thread(() -> logger.info("i am a thread")).start();
    }
}