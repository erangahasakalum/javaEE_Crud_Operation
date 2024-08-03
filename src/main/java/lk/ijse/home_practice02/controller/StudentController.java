package lk.ijse.home_practice02.controller;

import jakarta.json.JsonException;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.home_practice02.dao.StudentProcess;
import lk.ijse.home_practice02.dto.StudentDto;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.Writer;
import java.sql.*;

@WebServlet(value = "/student")
public class StudentController extends HttpServlet {

    StudentDto studentDto = new StudentDto();

    StudentProcess studentProcess = new StudentProcess();
    Connection connection;


    @Override
    public void init() {
        try {
            var driverClass = getServletContext().getInitParameter("driver-class");
            var dbUrl = getServletContext().getInitParameter("dbURL");
            var userName = getServletContext().getInitParameter("dbUserName");
            var password = getServletContext().getInitParameter("dbPassword");

            Class.forName(driverClass);
            this.connection = DriverManager.getConnection(dbUrl, userName, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getContentType().toLowerCase().startsWith("application/json") || req.getContentType() == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        Jsonb jsonb = JsonbBuilder.create();
        StudentDto studentDto = jsonb.fromJson(req.getReader(), StudentDto.class);

        try {
            boolean saveStudent = studentProcess.saveStudent(studentDto, connection);
            if (saveStudent) {
                resp.sendError(HttpServletResponse.SC_CREATED);
                resp.getWriter().write("Saved");
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Not Saved");
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String student_id = req.getParameter("id");

        try {
            boolean deleteStudent = studentProcess.deleteStudent(student_id, connection);
            if (deleteStudent) {
                resp.getWriter().write("Deleted");
                resp.sendError(HttpServletResponse.SC_NO_CONTENT);
            } else {
                resp.getWriter().write("Not Deleted");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getContentType().toLowerCase().startsWith("application/json") || req.getContentType() == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        var studentID = req.getParameter("id");
        Jsonb jsonb = JsonbBuilder.create();
        var st1 = jsonb.fromJson(req.getReader(), StudentDto.class);

        try (var writer = resp.getWriter()){
            boolean updateStudent = studentProcess.updateStudent(studentID, st1, connection);
            if (updateStudent){
                resp.sendError(HttpServletResponse.SC_NO_CONTENT);
            }else {
                writer.write("Not updated");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String stu_id = req.getParameter("id");

        try (var writer = resp.getWriter()) {
            StudentDto allStudent = studentProcess.getAll(stu_id, connection);
            resp.setContentType("application/json");
            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(allStudent, writer);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
