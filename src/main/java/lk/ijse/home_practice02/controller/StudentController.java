package lk.ijse.home_practice02.controller;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.home_practice02.dto.StudentDto;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(value = "/student")
public class StudentController extends HttpServlet {

    Connection connection;
    static String SAVE_STUDENT ="INSERT INTO student(id,name,city,telephone) VALUES(?,?,?,?)";

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
            PreparedStatement ps = connection.prepareStatement(SAVE_STUDENT);
            ps.setString(1,studentDto.getId());
            ps.setString(2,studentDto.getName());
            ps.setString(3,studentDto.getCity());
            ps.setString(4,studentDto.getTelephone());

            if (ps.executeUpdate()>0){
                resp.getWriter().write("Student Saved");
            }else {
                resp.getWriter().write("Not saved");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("hi");
    }
}
