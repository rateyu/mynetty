package g.b.m.netty.myim;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Scanner;

public class ImLineClient {
    Bootstrap b = new Bootstrap();
    Logger logger = LoggerFactory.getLogger(ImLineClient.class);
//    private String serverIp="127.0.0.1";
    private String serverIp="127.0.0.1";

    private int serverPort=7788;

    public static void main(String[] args) {
        new ImLineClient().runClient();
        
    }

    private void runClient() {
        //创建reactor 线程组
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();

        try {
            //1 设置reactor 线程组
            b.group(workerLoopGroup);
            //2 设置nio类型的channel
            b.channel(NioSocketChannel.class);
            //3 设置监听端口
            b.remoteAddress(serverIp, serverPort);
            //4 设置通道的参数
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            //5 装配子通道流水线
            b.handler(new ChannelInitializer<SocketChannel>() {
                //有连接到达时会创建一个channel
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // pipeline管理子通道channel中的Handler
                    // 向子channel流水线添加一个handler处理器
                    ch.pipeline().addLast(new LengthFieldPrepender(4));
                    ch.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));
                }
            });
            ChannelFuture f = b.connect();
            f.addListener((ChannelFuture futureListener) ->
            {
                if (futureListener.isSuccess()) {
                    logger.info("EchoClient客户端连接成功!");

                } else {
                    logger.info("EchoClient客户端连接失败!");
                }

            });

            // 阻塞,直到连接完成
            f.sync();
            Channel channel = f.channel();

            Scanner scanner = new Scanner(System.in);
            logger.info("请输入发送内容:");

//            while (scanner.hasNext()) {
//                //获取输入的内容
//                String next = scanner.next();
//                byte[] bytes = (" >>" + next).getBytes("UTF-8");
//                //发送ByteBuf
//                ByteBuf buffer = channel.alloc().buffer();
//                buffer.writeBytes(bytes);
//                channel.writeAndFlush(buffer);
//                logger.info("请输入发送内容:");
//
//            }

            //连续发送
            int aaa =1000000;
//            ByteBuf buffer
            while(aaa-->0) {
//                ByteBuf buffer = channel.alloc().buffer();
//                byte[] bytes = new byte[0];
//                try {
//                    bytes = (channel.id()+"_hello_world_"+aaa).getBytes("UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                buffer.writeBytes(bytes);
                channel.writeAndFlush(channel.id()+"_hello_world_"+aaa);
                logger.info("请输入发送内容: hello_world_"+aaa);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
