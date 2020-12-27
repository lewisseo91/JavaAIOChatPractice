package servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;


public class AIOServer {
    public static int PORT = 10800;
    public static AsynchronousServerSocketChannel serverSocketChannel;
    public static void handleCompletionHandler(AsynchronousSocketChannel serverChannel) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(12);
        serverChannel.read(buffer, null, new CompletionHandler<Integer, Void>() {
            int currentBufferSize = 0;
            int currentRemainingBufferSize = 0;
            String currentString = "";
            @Override
            public void completed(Integer result, Void attachment) {

                if( result < 0) {
                    try {
                        serverChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                buffer.flip();
//                int bufferCapacity = buffer.capacity();
                if(buffer.hasRemaining() && currentRemainingBufferSize == 0) {
//                    System.out.println("remaining:" + ", " + buffer.remaining());
                    currentBufferSize = buffer.getInt(0);
                }

                currentRemainingBufferSize += buffer.remaining();

                System.out.println("currentBufferSize:" + ", " + currentBufferSize);
                System.out.println("currentRemainingBufferSize:" + ", " + currentRemainingBufferSize);
//                System.out.println("received message:" + Charset.forName("UTF-8").decode(buffer));
                try {
                    currentString += new String(buffer.array(), "UTF-8");
                    System.out.println("received message:" + currentString);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (currentBufferSize == currentRemainingBufferSize) {
                    // 세션 등록 해 놓고 세션에다가 currentString 을 보내주어야 함.
                    // 다 받았을 때 초기화
                    currentBufferSize = 0;
                    currentRemainingBufferSize = 0;
                    currentString = "";
                }
//                new String(buffer.array(), "UTF-8").length()
                buffer.position(0);
                buffer.limit(buffer.capacity());
                serverChannel.read(buffer, null, this);
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                System.err.println(exc);
                try {
                    serverChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(PORT));
        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel result, Void attachment) {
                System.out.println("Server Connected");
                try {
                    handleCompletionHandler(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                System.err.println(exc);
                try {
                    serverSocketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("Server opened : " + new InetSocketAddress(PORT).getAddress().toString());
        Thread.currentThread().join();
    }
}