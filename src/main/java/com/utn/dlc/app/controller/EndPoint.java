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

    @PostMapping(path = "/agregar/{nombreDoc}")
    public @ResponseBody void Agregar(@PathParam("nombreDoc") String nombreDoc, File archivo) throws Exception{
        Class.forName(this.driver).newInstance();
        Connection connection = DriverManager.getConnection(this.url, "Lucas", null);
        File arch = new File("C:\\» Universidad\\DLC\\TP\\src\\main\\resources\\static\\Files\\" + nombreDoc);
        //if(arch.exists()){
        //  return @ResponseStatus(code = );
        //}
        Index index= new Index();
        index.PostearunDocumento(connection, arch);
        buscador = new Buscador(connection);

    }

}