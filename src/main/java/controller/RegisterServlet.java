package controller;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.hibernate.*;
import model.User;
import util.HibernateUtil;
import java.io.*;
import util.PasswordUtil;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword(password);

            session.persist(newUser);
            tx.commit();

            resp.setStatus(HttpServletResponse.SC_OK);
            out.write("{\"status\":\"success\", \"message\":\"User registered successfully.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"status\":\"error\", \"message\":\"Registration failed.\"}");
        }
    }
}