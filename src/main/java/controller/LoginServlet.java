package controller;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.hibernate.Session;
import model.User;
import util.HibernateUtil;
import java.io.*;
import util.PasswordUtil;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.createQuery("FROM User WHERE username = :username AND password = :password", User.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();

            if (user != null) {
                HttpSession httpSession = req.getSession();
                httpSession.setAttribute("user", user);
                resp.setStatus(HttpServletResponse.SC_OK);
                out.write("{\"status\":\"success\", \"message\":\"Login successful.\", \"username\":\"" + user.getUsername() + "\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.write("{\"status\":\"error\", \"message\":\"Invalid username or password.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"status\":\"error\", \"message\":\"Login failed.\"}");
        }
    }
}
