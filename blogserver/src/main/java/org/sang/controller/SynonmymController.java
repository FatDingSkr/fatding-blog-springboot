package org.sang.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/synonym111")
public class SynonmymController {
    private String lastModified = new Date().toString();
    private String etag = String.valueOf(System.currentTimeMillis());
    @RequestMapping(value = "/word", method = {RequestMethod.GET,RequestMethod.HEAD}, produces="text/html;charset=UTF-8")
    public String getSynonymWord(HttpServletResponse response){
        response.setHeader("Last-Modified",lastModified);
        response.setHeader("ETag",etag);
        //response.setHeader("If-Modified-Since",lastModified);
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        StringBuilder words = new StringBuilder();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://1.15.62.156:3306/blogdatabase?useUnicode=true&characterEncoding=UTF-8",
                    "root",
                    "Root_123"
            );
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select rule from dynamic_synonym_rule");

            while(rs.next()) {
                String theWord = rs.getString("rule");
                System.out.println("hot word from mysql: " + theWord);
                words.append(theWord);
                words.append("\n");
            }

            return words.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public void updateModified(){
        lastModified = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        etag = String.valueOf(System.currentTimeMillis());
    }
}
