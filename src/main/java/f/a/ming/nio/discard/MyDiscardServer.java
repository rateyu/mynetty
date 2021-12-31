package f.a.ming.nio.discard;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
//import java.util.logging.Logger;

/**
 * 接收到数据 抛弃掉
 */
public class MyDiscardServer {

    //    private static final Logger log = LoggerFactory.getLogger(MyDiscardServer.class);
//    Logger logger = L;
//    Logger log = Logger.getLogger("mingsoft");
    private  static Logger log = LoggerFactory.getLogger(MyDiscardServer.class);

    public static void main(String[] args) {
        try {
            /**
             *
             * 获取通道
             * 设置非阻塞
             * 绑定连接
             *
             * 获取选择器
             * 通道注册io事件,注册到选择器上
             *
             * 轮询感兴趣的io事件
             * 获取选择键集合
             *  获取单个选择键
             *      获取客户端连接
             *      设置非阻塞
             *      可读注册到选择器
             *
             *      若可读, 读数据
             *
             *
             *      总结: 通道打开 选择器打开  循环监听
             *
             */

            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 7777));

            log.info("8888");
//            log.info();

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (selector.select() > 0) {

                Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey selectionKey = selectionKeyIterator.next();
                    if (selectionKey.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int len = 0;
                        while ((len = socketChannel.read(byteBuffer)) > 0) {
                            byteBuffer.flip();
                            log.info(new String(byteBuffer.array(), 0, len));
                            byteBuffer.clear();
                        }
                        socketChannel.close();
                    }

                    selectionKeyIterator.remove();
                }
            }
            serverSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
