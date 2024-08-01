package lk.ijse.home_practice02.controller;

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
        if (!req.getContentType().toLowerCase().startsWith("application/json") || req.getContentType() == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        Jsonb jsonb = JsonbBuilder.create();
        StudentDto studentDto = jsonb.fromJson(req.getReader(), StudentDto.class);

        try {
            boolean saveStudent = studentProcess.saveStudent(studentDto, connection);
            if (saveStudent){
                resp.getWriter().write("Saved");
            }else {
                resp.getWriter().write("Not Saved");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String student_id = req.getParameter("id");

        try {
            boolean deleteStudent = studentProcess.deleteStudent(student_id, connection);
            if (deleteStudent){
                resp.getWriter().write("Deleted");
            }else {
                resp.getWriter().write("Not Deleted");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

//    @Override
//    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        if (!req.getContentType().toLowerCase().startsWith("application/json") || req.getContentType() == null){
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
//        }
//
//        String id = req.getParameter("id");
//        Jsonb jsonb = JsonbBuilder.create();
//        StudentDto studentDto = jsonb.fromJson(req.getReader(), StudentDto.class);
//
//        try {
//            PreparedStatement ps = connection.prepareStatement(UPDATE_STUDENT);
//            ps.setString(1,studentDto.getName());
//            ps.setString(2,studentDto.getCity());
//            ps.setString(3,studentDto.getTelephone());
//            ps.setString(4,id);
//
//            if (ps.executeUpdate()>0){
//                resp.getWriter().write("Updated");
//            }else {
//                resp.getWriter().write("Not Updated");
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String stu_id = req.getParameter("id");
//
//        try (var writer = resp.getWriter()){
//            PreparedStatement ps = connection.prepareStatement(GET_STUDENT);
//            ps.setString(1,stu_id);
//            ResultSet resultSet = ps.executeQuery();
//
//            while (resultSet.next()){
//                studentDto.setId(resultSet.getString("id"));
//                studentDto.setName(resultSet.getString("name"));
//                studentDto.setCity(resultSet.getString("city"));
//                studentDto.setTelephone(resultSet.getString("telephone"));
//
//            }
//
//            var jsonb = JsonbBuilder.create();
//            jsonb.toJson(studentDto,writer);
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
