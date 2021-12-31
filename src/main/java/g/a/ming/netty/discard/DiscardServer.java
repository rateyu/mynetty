package g.a.ming.netty.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscardServer {

    ServerBootstrap b = new ServerBootstrap();
    int port;
    public DiscardServer(int i) {
        port = i;
    }


    public static void main(String[] args) {
        new DiscardServer(7778).runServer();
    }

    private void runServer() {
        /**
         * 创建反应器组 两个 一个boss 多个worker
         * 设置反应器组
         * 设置nio通道
         * 监听端口
         * 通道参数
         * 通道流水线
         */

        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        b.group(boss, worker);
        b.channel(NioServerSocketChannel.class);
        b.localAddress(port);

        //Fixme 通道参数
        b.option(ChannelOption.SO_KEEPALIVE,true);
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new NettyDiscardHandler());
            }
        });
        try {
            ChannelFuture channelFuture = b.bind().sync();

            //等待关闭通道
            ChannelFuture closeFutrue = channelFuture.channel().closeFuture();
            closeFutrue.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }


    }

    class NettyDiscardHandler extends ChannelInboundHandlerAdapter {

        Logger logger = LoggerFactory.getLogger(NettyDiscardHandler.class);
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//            super.channelRead(ctx, msg);
//            System.out.println(msg);

            ByteBuf in = (ByteBuf) msg;
            try {
                logger.info("收到消息,丢弃如下:");
                while (in.isReadable()) {
                    System.out.print((char) in.readByte());
                }
//                System.out.println();
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }
    }
}
