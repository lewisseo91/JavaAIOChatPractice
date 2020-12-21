package servlet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.*;


public class AIOServer {
    private ExecutorService executorService;          // Thread Pool
    private AsynchronousChannelGroup threadGroup;      // Channel group
    public AsynchronousServerSocketChannel asynServerSocketChannel;  // server channel
    public void start(Integer port){
        try {
            // 1. Create a cache pool
            executorService = Executors.newCachedThreadPool();
            // 2. Create a channel group
            threadGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService, 1);
            // 3. Create a server channel
            asynServerSocketChannel = AsynchronousServerSocketChannel.open(threadGroup);
            // 4. Binding
            asynServerSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("server start , port : " + port);
            // 5. Wait for client request
            asynServerSocketChannel.accept(this, new AIOServerHandler());
            // Block all the time, don't let the server stop, the real environment is running under tomcat, so this line of code is not needed
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        AIOServer server = new AIOServer();
        server.start(8888);
    }
}