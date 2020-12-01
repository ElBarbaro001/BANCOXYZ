/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.datos;

/**
 *
 * @author xroot
 */
//Librerias de flujo de datos
import java.io.DataInputStream;
import java.io.DataOutputStream;
//Librerias de sockets
import java.net.ServerSocket;
import java.net.Socket;
//Librerias para sql
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author xroot
 */
public class Servidor_ {
    static int PUERTO = 5000;
    ServerSocket server;
    Socket sc;
    final String dbURL = "jdbc:postgresql://localhost:5432/banco";
    final String dbusuario = "xroot";
    final String dbcontrasena = "root";
    final String controlador = "org.postgresql.Driver";
    // Declarar clases para la transmision de flujo de datos
    // Enviar datos
    DataOutputStream salida;
    // Recibir datos
    DataInputStream entrada;
    // Declarar variables que se envian desde el cliente a el servidor: mensaje=msg
    //Variables de logueo de usuario administrador	
    String msgUsuario, msgContrasena, msgOpciones;
    //Variables de control Menu y mensajes de resultados
    String msgInsertar, msgResultado;
    //Variables de insercion - Nuevo Cliente
    //Variables de almacenamiento
    Integer idc; //idcliente
    String nom; //nombres
    String ape; //apellidos
    String dir; //direccion
    String tel; //telefono
    // Menu Opciones Cliente
    String opcion1 = "1 - Ingresar NUEVO Cliente ->";//Menu
    String opcion2 = "2 - Consultar CLIENTE ->";//Menu
    String opcion3 = "0 - Salir";
	
    public void iniciarServidor(){
        try{
            //Asignacion de variable
            server = new ServerSocket(PUERTO);
            sc = new Socket();
            System.out.println("--------------------------------> Servidor iniciado <--------------------------------");
            //Aceptar conecion entre cliente servidor
            sc = server.accept();
            System.out.println("--------------------------------> Cliente conectado <--------------------------------");
            // Creamos una copia del metodo primitivo DataInputStream, se encargara de los flujos de entrada
            entrada = new DataInputStream(sc.getInputStream());
            // Creamos una copia de la intefaz  DataOutputStream, se encargara de los flujos de salida 
            salida = new DataOutputStream(sc.getOutputStream());
            String msg = ""; //variable de control del ciclo while
            //Variables usuario del sistema
            String usuario = "";//usuario del sistema
            String contrasena = "" ;//contrasena
            String opcion = "";//control del ciclo case

            while(msg.equals("")){
                //Validar usuario administrador
                if(usuario.equals("")){
                    msgUsuario = entrada.readUTF();//Leer datos que se envian desde el cliente
                    System.out.println("Cliente -> " + msgUsuario + " <- esta ingresando contraseña");//Imprimir por pantalla mensaje
                    salida.writeUTF("Bienvenido " + msgUsuario + "\n" + "ingresa contraseña ->");//Imprimir mensaje al cliente
                    usuario = msgUsuario.toString();//asignar valor a la variable usuario
                }else{
                    usuario = msgUsuario.toString();//asignar valor a la variable usuario
                }//fin if
                if(!usuario.equals("") && contrasena.equals("")){
                    msgContrasena = entrada.readUTF();
                    System.out.println("Sesion inciada correctamente!");
                    salida.writeUTF("MENU seleccionar opciones\n" + opcion1 + "\n" + opcion2 + "\n"+ opcion3);//Menu para cliente
                    contrasena = msgContrasena.toString();
                }else{
                    contrasena = msgContrasena.toString();
                }//fin if
                if(!usuario.equals("") && !contrasena.equals("")){
                    msgOpciones = entrada.readUTF();
                    opcion = msgOpciones.toString();
                    //caso
                    switch (opcion) {
                        case "1":
                            //codigo
                            salida.writeUTF("Ingresar Codigo Cliente -> ");
                            msgInsertar = entrada.readUTF();
                            idc = Integer.parseInt(msgInsertar);
                            System.out.println("Codigo -> "+idc);
                            //nombres
                            salida.writeUTF("Ingresar Nombres -> ");
                            msgInsertar = entrada.readUTF();
                            nom = msgInsertar.toString();
                            System.out.println("Nombres -> "+nom);
                            //apellidos
                            salida.writeUTF("Ingresar Apellidos ->");
                            msgInsertar = entrada.readUTF();
                            ape = msgInsertar.toString();
                            System.out.println("Apellidos -> "+ape);
                            //direccion
                            salida.writeUTF("Ingresar Direccion -> ");
                            msgInsertar = entrada.readUTF();
                            dir = msgInsertar.toString();
                            System.out.println("Direccion -> "+dir);
                            //telefono
                            salida.writeUTF("Ingesar Telefono -> ");
                            msgInsertar = entrada.readUTF();
                            tel = msgInsertar.toString();
                            System.out.println("Telefono -> "+tel);

                            //invocar funcion insertar y pasarle las variables para la consulta insertar
                            insertarDB(idc, nom, ape, dir, tel);
                            salida.writeUTF("Presione ENTER para terminar!");
                            break;
                        case "2":
                            //codigo
                            salida.writeUTF("Consultar cliente por codigo -> ");
                            msgInsertar = entrada.readUTF();
                            idc = Integer.parseInt(msgInsertar);
                            System.out.println("Codigo consultado -> "+idc);
                            //Invocar funcion select
                            consultarDB(idc);
                            salida.writeUTF("Codigo Cliente "+ idc + "\nNombres " + nom + "\nApellidos " + ape + "\nDireccion " + dir + "\nTelefono " + tel +"\nPresione Enter para Salir!");
                            break;
                        default:
                            salida.close();
                            break;
                    }//fin case
                    msg = usuario + ", " + contrasena;
                }else{
                    opcion = msgOpciones.toString();
                }//fin if
            }
            sc.close();//cerrar comunicacion
        }catch (Exception e) {
			// TODO: handle exception
		}
    }
    public void insertarDB(Integer cod, String n, String a, String d, String t){
        try {
            Class.forName(controlador);//controlador psql
            java.sql.Connection conectar = DriverManager.getConnection(dbURL,dbusuario,dbcontrasena);
            Statement st = conectar.createStatement();
            //ejecutar consulta con insertar datos
            ResultSet rs = st.executeQuery("INSERT INTO cliente (idcliente,nombres,apellidos,direccion,telefono) VALUES ("+idc+",'"+nom+"','"+ape+"','"+dir+"','"+tel+"')");
            conectar.close();//cerrar conexion a base de datos
        } catch (SQLException e) {
            //TODO: handle exception
            System.out.println("SQL Exception: "+ e.toString());
        } catch(ClassNotFoundException cE){
            System.out.println("Class Not Found Exception: "+ cE.toString());
        }
    }//fin insertar
    public void consultarDB(Integer code){
         try {
            Class.forName(controlador);//controlador psql
            java.sql.Connection conectar = DriverManager.getConnection(dbURL,dbusuario,dbcontrasena);
            Statement st = conectar.createStatement();
            //ejecutar consulta con insertar datos
            ResultSet rs = st.executeQuery("select nombres,apellidos,direccion,telefono from cliente where idcliente ="+idc);
            //Almacenar datos en variables
            if(rs.next() == true){
                nom = rs.getString("nombres");
                ape = rs.getString("apellidos");
                dir = rs.getString("direccion");
                tel = rs.getString("telefono");
            }
            conectar.close();//cerrar conexion a base de datos
        } catch (SQLException e) {
            //TODO: handle exception
            System.out.println("SQL Exception: "+ e.toString());
        } catch(ClassNotFoundException cE){
            System.out.println("Class Not Found Exception: "+ cE.toString());
        }
    }//Fin consultar
    //Ejecutar programa desde el main
    public static void main(String[] args) {
	Servidor_ ser = new Servidor_();
	ser.iniciarServidor();
    }
}