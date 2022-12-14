package f.a.ming.nio.discard;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class MyDiscardClient {
    public static void main(String[] args) {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 7777);
//        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 7778);
        try {
            SocketChannel socketChannel = SocketChannel.open(address);
            socketChannel.configureBlocking(false);
            while (!socketChannel.finishConnect()){
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put("hello世界".getBytes());
            byteBuffer.flip();

            socketChannel.write(byteBuffer);
            socketChannel.shutdownOutput();
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
