package controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Question;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/question")
public class QuestionServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        resp.setContentType("application/json");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Question> questions = session.createQuery("FROM Question", Question.class).list();
            String json = gson.toJson(questions);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        Question question = gson.fromJson(reader, Question.class);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(question);
            tx.commit();
            resp.setStatus(HttpServletResponse.SC_OK);
            out.write("{\"message\":\"Question added successfully.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"message\":\"Failed to add question.\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        Question updatedQuestion = gson.fromJson(reader, Question.class);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Question existing = session.get(Question.class, updatedQuestion.getId());
            if (existing != null) {
                existing.setQuestionText(updatedQuestion.getQuestionText());
                existing.setOptionA(updatedQuestion.getOptionA());
                existing.setOptionB(updatedQuestion.getOptionB());
                existing.setOptionC(updatedQuestion.getOptionC());
                existing.setOptionD(updatedQuestion.getOptionD());
                existing.setCorrectOption(updatedQuestion.getCorrectOption());
                session.update(existing);
                tx.commit();
                resp.setStatus(HttpServletResponse.SC_OK);
                out.write("{\"message\":\"Question updated successfully.\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"message\":\"Question not found.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"message\":\"Failed to update question.\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Question question = session.get(Question.class, id);
            if (question != null) {
                session.delete(question);
                tx.commit();
                resp.setStatus(HttpServletResponse.SC_OK);
                out.write("{\"message\":\"Question deleted successfully.\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"message\":\"Question not found.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"message\":\"Failed to delete question.\"}");
        }
    }
}

