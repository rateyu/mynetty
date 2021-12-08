package a.c.ming.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class EchoHandle implements Runnable{
    Logger logger = LoggerFactory.getLogger(EchoHandle.class);

    SocketChannel channel;
    SelectionKey sk;
    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    final int RECEIVING = 0;
    final int SENDING =1;
    int state = RECEIVING;

    public EchoHandle(Selector selector, SocketChannel socketChannel) {
        System.out.println("world");
        channel = socketChannel;
        try {
            socketChannel.configureBlocking(false);
            sk = channel.register(selector, RECEIVING);
            sk.attach(this);
            //Fixme 以下两句不懂
            sk.interestOps(SelectionKey.OP_READ);
            selector.wakeup();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        System.out.println("hello");
        if (state==SENDING) {
            try {
                channel.write(byteBuffer);
                byteBuffer.clear();
                sk.interestOps(SelectionKey.OP_READ);
                state = RECEIVING;
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (state == RECEIVING) {
            int length =0;
            while (true) {
                try {
                    if (!((length = channel.read(byteBuffer)) > 0)) {
                        break;
                    }
                    logger.info(new String(byteBuffer.array(),0,length));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            byteBuffer.flip();
            sk.interestOps(SelectionKey.OP_WRITE);
            state = SENDING;
        }
        //处理结束 重复使用 不用关闭

    }
}
