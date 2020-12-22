package servlet;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

public class AIOServerHandler  implements CompletionHandler<AsynchronousSocketChannel, AIOServer> {
    private final Integer BUFFER_SIZE = 1024;
    @Override
    public void completed(AsynchronousSocketChannel asynSocketChannel, AIOServer attachment) {
        // Ensure that multiple clients can block
//        attachment.asynServerSocketChannel.accept(attachment, this);
        read(asynSocketChannel);
    }
    //Read data
    private void read(final AsynchronousSocketChannel asynSocketChannel) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        asynSocketChannel.read(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer resultSize, ByteBuffer attachment) {
                //After reading, reset the flag
                attachment.flip();
                //Get the number of bytes read
                System.out.println("Server -> " + "The length of the data received from the client is:" + resultSize);
                //Get the read data
                String resultData = new String(attachment.array()).trim();
                System.out.println("Server -> " + "The data information received from the client is:" + resultData);
                String response = "The server responded, and received the data from the client: " + resultData;
                write(asynSocketChannel, response);
            }
            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        });
    }
    // data input
    private void write(AsynchronousSocketChannel asynSocketChannel, String response) {
        try {
            // Write data to the buffer
            ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
            buf.put(response.getBytes());
            buf.flip();
            // Write from the buffer to the channel
            asynSocketChannel.write(buf).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void failed(Throwable exc, AIOServer attachment) {
        exc.printStackTrace();
    }
}