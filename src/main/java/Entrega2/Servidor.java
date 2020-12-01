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

public class Servidor {
    static int PUERTO = 5000;
    ServerSocket server;
    Socket sc;
    final String dbURL = "jdbc:postgresql://localhost:5432/bancoxyz";
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
    //Variables de almacenamiento referenciadas en la tabla cliente de la BD
    Integer codigo; //codigo cliente PK
    String nombres; //nombres
    String apellidos; //apellidos
    String correo;//correo
    String telefono; //telefono
    Integer cuenta;//numero cuenta FK
    String usuariosis;//usuario
    String direccion; //direccion
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
            //Aceptar conexion entre cliente servidor
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
                    System.out.println("Opcion seleccionada ----> "+opcion);
                    //caso
                    switch (opcion) {
                        case "1":
                            //codigo
                            salida.writeUTF("Ingresar Codigo Cliente -> ");
                            msgInsertar = entrada.readUTF();//Leer lo que llega desde el cliente
                            codigo = Integer.parseInt(msgInsertar);//asignar datos que llegaron desde el cliente
                            System.out.println("Codigo -> "+codigo);
                            //nombres
                            salida.writeUTF("Ingresar Nombres -> ");
                            msgInsertar = entrada.readUTF();
                            nombres = msgInsertar.toString();
                            System.out.println("Nombres -> "+nombres);
                            //apellidos
                            salida.writeUTF("Ingresar Apellidos ->");
                            msgInsertar = entrada.readUTF();
                            apellidos = msgInsertar.toString();
                            System.out.println("Apellidos -> "+apellidos);
                            //correo
                            salida.writeUTF("Ingresar Correo -> ");
                            msgInsertar = entrada.readUTF();
                            correo = msgInsertar.toString();
                            System.out.println("Direccion -> "+correo);
                            //telefono
                            salida.writeUTF("Ingesar Telefono -> ");
                            msgInsertar = entrada.readUTF();
                            telefono = msgInsertar.toString();
                            System.out.println("Telefono -> "+telefono);
                            //numero de cuenta
                            salida.writeUTF("Ingresa codigo  cuenta -> ");
                            msgInsertar = entrada.readUTF();
                            cuenta = Integer.parseInt(msgInsertar);
                            System.out.println("Codigo cuenta -> "+cuenta);
                            //usuario del sistema
                            salida.writeUTF("Ingresar usuario -> ");
                            msgInsertar = entrada.readUTF();
                            usuariosis = msgInsertar.toString();
                            System.out.println("Usuario ->"+usuariosis);
                            //Direccion
                            salida.writeUTF("Ingresar direccion -> ");
                            msgInsertar = entrada.readUTF();
                            direccion = msgInsertar.toString();
                            System.out.println("Direccion ->"+direccion);
                            

                            //invocar funcion insertar y pasarle las variables para la consulta insertar
                            insertarDB(codigo,nombres,apellidos,correo,telefono,cuenta,usuariosis,direccion);
                            salida.writeUTF("Presione ENTER para terminar!");
                            break;
                        case "2":
                            //codigo
                            salida.writeUTF("Consultar cliente por codigo -> ");
                            msgInsertar = entrada.readUTF();
                            codigo = Integer.parseInt(msgInsertar);
                            System.out.println("Codigo consultado -> "+codigo);
                            //Invocar funcion select
                            consultarDB(codigo);
                            salida.writeUTF("Codigo Cliente -> "+ codigo + "\nNombres -> " + nombres + "\nApellidos -> " + apellidos + "\nCorreo -> " + correo + "\nTelefono -> " + telefono + "\nNumero cuenta -> "+ cuenta + "\nUsuario del sistema -> " + usuariosis+ "\nDireccion -> "+ direccion + "\nPresione ENTER para salir!");
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
    }//fin iniciar servidor
    public void insertarDB(Integer cod, String n, String a, String c, String t, Integer cta, String us, String dir){
        try {
            Class.forName(controlador);//controlador psql
            java.sql.Connection conectar = DriverManager.getConnection(dbURL,dbusuario,dbcontrasena);
            Statement st = conectar.createStatement();
            //ejecutar consulta con insertar datos
            ResultSet rs = st.executeQuery("INSERT INTO cliente (cod_cliente,nombres,apellidos,correo,telefono,id_cuenta,usuario,direccion) VALUES ("+codigo+",'"+nombres+"','"+apellidos+"','"+correo+"','"+telefono+"',"+cuenta+",'"+usuariosis+"','"+direccion+"')");
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
            //ejecutar consulta con seleccionar  datos desde la base de datos
            ResultSet rs = st.executeQuery("select nombres,apellidos,correo,telefono,id_cuenta,usuario,direccion from cliente where cod_cliente ="+codigo);
            //Almacenar datos en variables provenientes de la base de datos
            if(rs.next() == true){
                nombres = rs.getString("nombres");
                apellidos = rs.getString("apellidos");
                correo = rs.getString("correo");
                telefono = rs.getString("telefono");
                cuenta = rs.getInt("id_cuenta");
                usuariosis = rs.getString("usuario");
                direccion = rs.getString("direccion");
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
    public static void main(String[] args){
        Servidor servidor = new Servidor();
        servidor.iniciarServidor();
    }//fin main
}
