package g.c.m.netty;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * create by 尼恩 @
 * <p>
 * 编码器
 */

//@Slf4j
public class SimpleProtobufEncoder extends MessageToByteEncoder<MsgProtos.Msg> {

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          MsgProtos.Msg msg, ByteBuf out)
            throws Exception {
        encode0(msg, out);
    }

    public static void encode0(
            MsgProtos.Msg msg, ByteBuf out) {
        out.writeShort(ProtoInstant.MAGIC_CODE);
        out.writeShort(ProtoInstant.VERSION_CODE);

        byte[] bytes = msg.toByteArray();// 将对象转换为byte

        // 加密消息体
        /*ThreeDES des = channel.channel().attr(Constants.ENCRYPT).get();
        byte[] encryptByte = des.encrypt(bytes);*/
        int length = bytes.length;// 读取消息的长度

//        Logger.cfo("encoder length=" + length);

        // 先将消息长度写入，也就是消息头
        out.writeInt(length);
        // 消息体中包含我们要发送的数据
        out.writeBytes(bytes);

/*        log.debug("send "
                + "[remote ip:" + ctx.channel().remoteAddress()
                + "][total length:" + length
                + "][bare length:" + msg.getSerializedSize() + "]");*/


    }

}
