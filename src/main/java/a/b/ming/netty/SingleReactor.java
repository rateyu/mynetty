package a.b.ming.netty;

import ch.qos.logback.classic.net.SimpleSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SingleReactor implements Runnable {

    Selector selector;
    ServerSocketChannel serverSocketChannel;
    public SingleReactor() {

        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 7778));

            selector = Selector.open();
            SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            selectionKey.attach(new AcceptHandle());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        SingleReactor singleReactor = new SingleReactor();
        Thread thread = new Thread(singleReactor);
        thread.setDaemon(false);
        //所有线程都为守护线程，Java 虚拟机退出
        thread.start();

    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
//        while (true) {
            try {
                selector.select();
                Set <SelectionKey> selected = selector.selectedKeys();
                Iterator iterator =  selected.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = (SelectionKey) iterator.next();
                    dispatch(selectionKey);
                }
                selected.clear();
                //Fixme clear后还有值 为什么 待确定
//                selector.selectedKeys().clear();
//                iterator.remove();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void dispatch(SelectionKey selectionKey) {
        Runnable runnable = (Runnable) selectionKey.attachment();
        if (runnable!=null) {
            runnable.run();
        }
    }
}
