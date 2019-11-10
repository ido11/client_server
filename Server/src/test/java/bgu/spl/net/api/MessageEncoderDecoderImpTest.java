package bgu.spl.net.api;

import bgu.spl.net.protocol.messages.*;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

public class MessageEncoderDecoderImpTest<T> {
    private final MessageEncoderDecoderImp encdec = new MessageEncoderDecoderImp();
    private Queue<String> q=new LinkedList();
    private ByteBuffer b=ByteBuffer.allocateDirect(1 << 13);
    private int counter;

    @Before
    public void initialize() {
        //q.add("\0\1Name\0Password\0\0\2Name\0Password\0\0\3\0\4\0\3Aviv\0Matan\0Gal\0\0\5Message\0\0\6Username\0Content\0\0\7");
        q.add("\0\1Name\0Password\0");
        q.add("\0\2Name\0Password\0");
        q.add("\0\3");
        q.add("\0\4\0\3Aviv\0Matan\0Gal\0");
        q.add("\0\5Message\0");
        q.add("\0\6UserName\0Content\0");
        q.add("\0\7");

        counter=1;
        System.out.println("OP : Class");
        System.out.println("------------------");
        while (!q.isEmpty()){
            b.put(encode(q.poll()));
            read();
        }

    }

    private byte[] encode(String message) {
        return (message).getBytes();
    }

    @Test
    public void read() {
        ByteBuffer buf = b;
        buf.flip();

        try {
            while (buf.hasRemaining()) {
                absMessage nextMessage = encdec.decodeNextByte(buf.get());
                if (nextMessage != null) {
                    System.out.println("0"+counter+" : "+nextMessage.getClass().getSimpleName());
                    counter++;
                }
            }
        } finally {
            b.clear();
        }
    }
}
