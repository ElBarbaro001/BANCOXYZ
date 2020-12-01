/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.datos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author xroot
 */
public class servidor {
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
	// Declarar variables entre el cliente y el servidor: mensaje=msg
	String msgUsuario, msgContrasena, msgOpciones, msgRetirar, msgConsignar, msgResultado;
	// Menu Opciones Cliente
	String opcion1 = "1 Insertar Cliente -> $";
	String opcion2 = "0 Salir -> $";
	// Variables Cliente
	String nombre;
	String apellido;
	int idCliente ;
	int numCuenta;
	float saldo;
	String tipo;
	

	// Clase para inciar el servidor
	public void iniciarServidor() {
		try {
			// Iniciamos el servidor
			server = new ServerSocket(PUERTO);
			sc = new Socket();
			System.out.println("--------------------------------> Servidor iniciado <--------------------------------");
			sc = server.accept();
			System.out.println("--------------------------------> Cliente conectado <--------------------------------");
			entrada = new DataInputStream(sc.getInputStream());
			salida = new DataOutputStream(sc.getOutputStream());

			// Inicializar variables Menu
			String msn = "";
			String usuario = "";
			String contrasena = "";
			String opcion = "";

			while (msn.equals("")) {
				// Validar usuario del sistema
				//Primera validacion de usuario
				if (usuario.equals("")) {
					msgUsuario = entrada.readUTF();// Leer datos que llegan
					System.out.println("--------------------------------> Cliente " + msgUsuario +" ingresando contraseña <--------------");
					salida.writeUTF("Bienvenido " + msgUsuario + " Ingresar contrasena de la cuenta ->");// recibir contraseña desde el cliente
					usuario = msgUsuario.toString();
				} else {
					usuario = msgUsuario.toString();
				}
				//segunda validacion de contraseña
				if (!usuario.equals("") && contrasena.equals("")) {
					msgContrasena = entrada.readUTF();
					System.out.println("Sesion iniciada");
					salida.writeUTF("Seleccionar una de las siguientes opciones\n" + opcion1 + "\n " + opcion2 + " \n"
							+ opcion3);
					contrasena = msgContrasena.toString();
				} else {
					contrasena = msgContrasena.toString();
				}
				//tercera validacion usuario y contraseña
				if (!usuario.equals("") && !contrasena.equals("")) {
					msgOpciones = entrada.readUTF();
					opcion = msgOpciones.toString();
					//Caso
					switch (opcion) {
						case "1":
							consultarpsql(usuario);
							//Imprimir por pantalla
							salida.writeUTF("Cliente nuevo " + id + " " + apellido + " cuenta tipo " + tipo + " numero " + numCuenta + " saldo actual $ "+ saldo);
							break;
						case "2":
							salida.writeUTF("Valor a retirar \n" + retirar1 + "\n" + retirar2 + "\n"+  retirar3 +"\n" +retirar4);
							msgRetirar = entrada.readUTF();//Leer datos que llegan al servidor
							String opcionRetiro = "";
							opcionRetiro = msgRetirar.toString();
							//retirarpsql(usuario, opcionRetiro);
							salida.writeUTF(msgResultado);
							break;
						case "3":
							salida.writeUTF("Valor a depositar $ -> ");
							msgConsignar = entrada.readUTF();
							int valorDeposito = Integer.parseInt(msgConsignar);
							//depositarDB(usuario, valorDeposito);
							break;
					}// switch
					msn = usuario + ", " + contrasena;
				} else {
					opcion = msgOpciones.toString();
				}
			} // while
			sc.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}// Iniciar servidor
	
	//Clase para consultar datos 
	public void insertarDB(String usuarioAlias, String) {
		final String usuario = usuarioAlias;
		try {
			Class.forName(controlador);//conector
			java.sql.Connection con = DriverManager.getConnection(dbURL,dbusuario,dbcontrasena);

			// Crear sentencias sql con el objeto Statement
			// https://docs.oracle.com/javase/8/docs/api/java/beans/Statement.html
			Statement statement = con.createStatement();//crear metodo para consulta
			ResultSet resultSet = statement.executeQuery("select cd_cte,n_cte,a_cte from cte where usuario='" + usuario + "'");//enviar consulta a db

			if (resultSet.next() == true) {
				nombre = resultSet.getString("n_cte");//almacenar dato en variable nombre
				apellido = resultSet.getString("a_cte");//almacenar dato en variable apellido
				idCliente = resultSet.getInt("cd_cte");//almacenar dato en variable idCliente
				ResultSet resultSet2 = statement.executeQuery("select cd_cta,tipo,saldo from cta where cd_cte='"+ idCliente +"'");
				//Almacenar datos				
				if (resultSet2.next() == true) {
					numCuenta = resultSet2.getInt("cd_cta");//convertir a tipo de datos Integer
					tipo = resultSet2.getString("tipo");//Convertir a tipo de datos money
                                        saldo = resultSet2.getFloat("saldo");
				}
			}
			con.close();
		} catch (Exception e) {
                    e.printStackTrace();
                    }

	}// Consultarpsql
    public static void main(String[] args) {
	servidor ser = new servidor();
	ser.iniciarServidor();
    }
}
