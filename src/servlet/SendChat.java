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

@WebServlet("/SendChat")
public class SendChat extends HttpServlet {

    public SendChat() {
        super();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            String chatText = req.getParameter("chat_text");
            AIOClient.write(chatText);
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
