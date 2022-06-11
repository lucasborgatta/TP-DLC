package com.utn.dlc.app.controller;


import com.utn.dlc.app.Index;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Connection;
import java.sql.DriverManager;

@RequestMapping(path = "/endpoint")
public class EndPoint {
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://127.0.0.1:3306/sys?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires";

    @PostMapping(path = "/index")
    public @ResponseBody void index() throws Exception {
        Class.forName(this.driver).newInstance();
        Connection connection = DriverManager.getConnection(this.url, "Lucas", null);
        Index index = new Index();
        index.index(connection);
    }

}