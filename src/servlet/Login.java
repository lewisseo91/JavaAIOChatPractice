package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

@WebServlet("/call/Login")
public class Login extends HttpServlet {
    public Login() {
        super();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
            client.connect(new InetSocketAddress("localhost", 9888)).get();
            client.write(ByteBuffer.wrap("123456789sakgjlkadsjlgas".getBytes()));
            Thread.sleep(1111111);
        } catch (Exception e) {
            e.printStackTrace();
        }

        res.setContentType("text/html");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write("hi");
        res.getWriter().flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
