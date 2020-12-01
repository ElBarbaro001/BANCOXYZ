/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entrega2;

/**
 *
 * @author xroot
 */
//Librerias de flujo de datos
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Probar_ConexionDB {
    public static void main(String[] args){
        Connection baseDatos = null;
        String host = "localhost";
        String port = "5432";
        String db_name = "bancoxyz";
        String usuario = "xroot";
        String contrasena = "root";
        try{
            Class.forName("org.postgresql.Driver");
            baseDatos = DriverManager.getConnection("jdbc:postgresql://"+host+":"+port+"/"+db_name+"", ""+usuario+"", ""+contrasena+"");
            if(baseDatos != null){
                   System.out.println("Conexion exitosa!");
            }
        }catch(Exception e){e.printStackTrace();}
    }
}
