package g.b.m.netty.myim;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyLineServerHandler extends ChannelInboundHandlerAdapter {
    public static final NettyLineServerHandler INSTANCE = new NettyLineServerHandler();
    Logger logger = LoggerFactory.getLogger(NettyLineServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        logger.info("msg type: " + (in.hasArray()?"堆内存":"直接内存"));

        int len = in.readableBytes();
        byte[] arr = new byte[len];
        in.getBytes(0, arr);
        logger.info("server received: " + new String(arr, "UTF-8"));

        //写回数据，异步任务
        logger.info("写回前，msg.refCnt:" + ((ByteBuf) msg).refCnt());
        ChannelFuture f = ctx.writeAndFlush(msg);
        f.addListener((ChannelFuture futureListener) -> {
            logger.info("写回后，msg.refCnt:" + ((ByteBuf) msg).refCnt());
        });
    }
}
