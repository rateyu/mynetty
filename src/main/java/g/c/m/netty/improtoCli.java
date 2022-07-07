package g.c.m.netty;

import g.b.m.netty.myim.ImClient;
import g.b.m.netty.myim.NettyEchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class improtoCli {
    Bootstrap b = new Bootstrap();
    Logger logger = LoggerFactory.getLogger(ImClient.class);
    private String serverIp="127.0.0.1";

    private int serverPort=7788;

    public static void main(String[] args) {
        new improtoCli().runClient();

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
//                    ch.pipeline().addLast(new LengthFieldPrepender(4));
//                    ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));

                    ch.pipeline().addLast("decoder", new SimpleProtobufDecoder());
                    ch.pipeline().addLast("encoder", new SimpleProtobufEncoder());
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
            int num =0;

            while (scanner.hasNext()) {
                //获取输入的内容
                String next = scanner.next();
                byte[] bytes = (" >>" + next).getBytes("UTF-8");
                //发送ByteBuf
//                ByteBuf buffer = channel.alloc().buffer();
//                buffer.writeBytes(bytes);
                MsgProtos.Msg.Builder builder = MsgProtos.Msg.newBuilder();
                builder.setId(num++);
                builder.setContent(next);
                channel.writeAndFlush(builder.build());
                logger.info("请输入发送内容:");

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
