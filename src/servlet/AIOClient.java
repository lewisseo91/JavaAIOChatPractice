package servlet;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.Date;

public class AIOClient implements Runnable {
    public static int PORT = 10800;
    private static String IP_ADDRESS = "localhost";
    private static AsynchronousSocketChannel clientSocketChannel;

    public static void main(String[] args) throws Exception {
        clientSocketChannel = AsynchronousSocketChannel.open();
        clientSocketChannel.connect(new InetSocketAddress(IP_ADDRESS, PORT));
        while (true) {
            Thread.sleep(1000);
            String date = new Date().toString();
            System.out.println(date.length() + 4);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.putInt(date.length() + 4);
            buffer.put(date.getBytes());
            buffer.flip();
            clientSocketChannel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    if( result < 0) {
                        try {
                            clientSocketChannel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    System.err.println(exc);
                    try {
                        clientSocketChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void run() {

    }
}