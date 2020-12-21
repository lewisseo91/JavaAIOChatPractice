package servlet;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class AIOClient  implements Runnable {
    private static Integer PORT = 8888;
    private static String IP_ADDRESS = "localhost";
    private AsynchronousSocketChannel asynSocketChannel;
    public AIOClient() throws Exception {
        asynSocketChannel = AsynchronousSocketChannel.open();  // Open the channel
    }
    public void connect(){
        asynSocketChannel.connect(new InetSocketAddress(IP_ADDRESS, PORT));  // Create connection Same as NIO
    }
    public void write(String request){
        try {
            asynSocketChannel.write(ByteBuffer.wrap(request.getBytes())).get();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            asynSocketChannel.read(byteBuffer).get();
            byteBuffer.flip();
            byte[] respByte = new byte[byteBuffer.remaining()];
            byteBuffer.get(respByte); // Put the data of the buffer into the byte array
            System.out.println(new String(respByte,"utf-8").trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        while(true){
        }
    }
    public static void main(String[] args) throws Exception {
//            for (int i=0 ; i< 10; i++) {
//                AIOClient myClient = new AIOClient();
//                myClient.connect();
//                new Thread(myClient, "myClient" + i).start();
//                myClient.write("aaaaaaaaaaaaaaaaaaaaaa");
//            }
//            AIOClient myClient = new AIOClient();
//            myClient.connect();
//            new Thread(myClient, "myClient").start();
//            myClient.write("aaaaaaaaaaaaaaaaaaaaaa");
              AIOClient c1 = new AIOClient();
              c1.connect();

              AIOClient c2 = new AIOClient();
              c2.connect();

              AIOClient c3 = new AIOClient();
              c3.connect();

              new Thread(c1, "c1").start();
              new Thread(c2, "c2").start();
              new Thread(c3, "c3").start();

              Thread.sleep(1000);

              c1.write("c1 aaa");
              c2.write("c2 bbbb");
              c3.write("c3 ccccc");
    }
}