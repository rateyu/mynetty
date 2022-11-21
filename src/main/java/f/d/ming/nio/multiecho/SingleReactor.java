package f.d.ming.nio.multiecho;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SingleReactor implements Runnable {

    Selector selector;
    ServerSocketChannel serverSocketChannel;
    SingleReactor() throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7777);
        serverSocketChannel.bind(inetSocketAddress);
        serverSocketChannel.configureBlocking(false);
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        selectionKey.attach(new AcceptHandle());

    }
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selected.iterator();
                while(iterator.hasNext()) {
                    SelectionKey sk =iterator.next();
                    dispatch(sk);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void dispatch(SelectionKey sk) {
        Runnable run = (Runnable) sk.attachment();
        if (run!=null) {
            run.run();
        }
    }

    class AcceptHandle implements Runnable {
        @Override
        public void run() {
            SocketChannel socketChannel = null;
            try {
                socketChannel = serverSocketChannel.accept();
                System.out.println("收到连接");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (socketChannel!=null) {
                new MyHandle(selector,socketChannel);
            }
        }
    }

    private class MyHandle implements Runnable{
        public MyHandle(Selector selector, SocketChannel socketChannel) {
            System.out.println("处理业务");
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1*1000*60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        try {
            new Thread(new SingleReactor()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
