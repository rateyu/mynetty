package g.b.m.netty.myim;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyLineClientHandler extends ChannelInboundHandlerAdapter {
    public static final NettyLineClientHandler INSTANCE = new NettyLineClientHandler();
    Logger logger = LoggerFactory.getLogger(NettyEchoServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        int len = in.readableBytes();
        byte[] arr = new byte[len];
        in.getBytes(0, arr);
        logger.info("client received: " + new String(arr, "UTF-8"));
        in.release();
    }
}
