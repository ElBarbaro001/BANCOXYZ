/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CBD;

/**
 *
 * @author xroot
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import javax.swing.JOptionPane;
public class Banco {//insertar datos a la tabla
    private static final String insertar = "INSERT INTO cliente (cod_cliente,nombres,apellidos,correo,telefono,id_cuenta,usuario,direccion) VALUES (?,?,?,?,?,?,?,?)";
    Integer codigo;
    String nombres;
    String apellidos;
    String correo;
    String telefono;
    String cuenta;
    String usuario;
    String direccion;
    public static void main(String[] args){
        try(Connection conectar = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bancoxyz", "xroot", "root");
                PreparedStatement ps = conectar.prepareStatement(insertar)){
            ps.setInt(1, 204);
            ps.setString(2, "Juan");
            ps.setString(3, "Escobar");
            ps.setString(4, "aa@fff.com");
            ps.setString(5, "5644");
            ps.setInt(6, 12);
            ps.setString(7, "army");
            ps.setString(8, "calle 97");
            int row = ps.executeUpdate();
            System.out.println(row);
        }catch (SQLException e){
            System.err.format("SQL estado: %s \n%s",e.getSQLState() ,e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
