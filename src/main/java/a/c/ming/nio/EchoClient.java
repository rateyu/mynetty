package a.c.ming.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class EchoClient {
    Logger logger = LoggerFactory.getLogger(EchoClient.class);

    public EchoClient() {
        try {
            InetSocketAddress address = new InetSocketAddress("127.0.0.1", 7778);
            SocketChannel channel = SocketChannel.open(address);
            channel.configureBlocking(false);

            while (!channel.finishConnect()) {

            }
            logger.info("client ok");
            Thread thread = new Thread(new MyClient(channel));
            thread.setDaemon(false);
            thread.start();
//            (new Thread(new MyClient(channel)).setDaemon(false)).start();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class MyClient implements Runnable {

        ByteBuffer send = ByteBuffer.allocate(1024);
        ByteBuffer rece = ByteBuffer.allocate(1024);
        Selector selector;
        SocketChannel cChannel;
        int state = 0;

        public MyClient(SocketChannel channel) throws IOException {
            cChannel = channel;
            selector = Selector.open();
            cChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        }

        @Override
        public void run() {

            while (!Thread.interrupted()) {
                try {
                    selector.select();
                    Set<SelectionKey> set = selector.selectedKeys();
                    Iterator<SelectionKey> sks = set.iterator();
                    while (sks.hasNext()) {
                        SelectionKey sk = sks.next();
                        if (sk.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) sk.channel();
                            int len = 0;
                            while ((len = socketChannel.read(rece)) > 0) {
                                rece.flip();
                                logger.info(new String(rece.array(), 0, len));
                                rece.clear();
                            }


                        } else if (sk.isWritable()) {
                            SocketChannel socketChannel = (SocketChannel) sk.channel();
                            send.put("hello".getBytes());
                            send.flip();
                            socketChannel.write(send);
                            send.clear();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    public static void main(String[] args) {
        new EchoClient();
    }


}
