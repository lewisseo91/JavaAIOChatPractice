package servlet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;


public class AIOServer {
    public static int PORT = 10800;
    public static AsynchronousServerSocketChannel serverSocketChannel;
    public static void handleCompletionHandler(AsynchronousSocketChannel serverChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(12);
        serverChannel.read(buffer, null, new CompletionHandler<Integer, Void>() {
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
                int bufferSize = buffer.getInt(0);
                System.out.println(bufferSize);
                System.out.println("received message:" + Charset.forName("UTF-8").decode(buffer));
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
                } catch (IOException e) {
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