package g.b.m.netty.myim;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

public class StringProcessHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String json = (String) msg;
        System.out.println("收到消息:"+json);
//        JsonMsg jsonMsg = JsonMsg.parseFromJson(json);
//        Logger.info("收到一个 Json 数据包 =》" + jsonMsg);

    }
}
