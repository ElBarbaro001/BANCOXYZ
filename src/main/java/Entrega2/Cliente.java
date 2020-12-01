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
//Importar libreria de transferencia de datos Stream
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.*;
import java.net.Socket;
//leer por pantalla
import java.util.Scanner;
public class Cliente {
    final String HOST ="127.0.0.1";//Direccion del localhost
    final int PUERTO = 5000;//Puerto de comunicacion 
    //Funcion e interfaz de manejo de flujo de datos
    DataInputStream ingresar;//https://docs.oracle.com/javase/8/docs/api/java/io/DataInputStream.html
    DataOutputStream salida;//https://docs.oracle.com/javase/8/docs/api/java/io/DataOutputStream.html
    String mensaje;//variable de control de lectura y escritura
    public void iniciarCliente(){
        Scanner leer = new Scanner(System.in);//https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html
        try{
            //Crear copia de la clase Socket - > https://docs.oracle.com/javase/8/docs/api/java/net/Socket.html 
            Socket sc = new Socket(HOST,PUERTO);
            ingresar = new DataInputStream(sc.getInputStream());//Creamos una copia de la clase a la variable ingresar
            salida = new DataOutputStream(sc.getOutputStream());
			String teclado = "";
			System.out.println("----------------------- > BANCOXYZ ->  Digita usuario Administrador ->");//Leer datos del usuario
                        
			while(!teclado.equals("x")){
					teclado = leer.nextLine();
					salida.writeUTF(teclado);
					mensaje = ingresar.readUTF();
					System.out.println(mensaje);
			}
			sc.close();//Cerrar socket cliente a servidor
        } catch (IOException ex){}
    }
    public static void main(String[] args){
        Cliente cliente = new Cliente();//Creamos una copia de la clase Cliente
        cliente.iniciarCliente();
    }
}
