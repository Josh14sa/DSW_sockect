package com.cibertec.bd;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;


public class Client {
    
    private static final String HOST = "localhost";
    private static final int PORT = 5000;
    
    public Client(){
        System.out.println("1 >> [ini] Client constructor");
        try {
            System.out.println("2 >> connecting to server...");
            Socket socket = new Socket(HOST, PORT);
            System.out.println("3 >> connected to server...");

            File[] files = new File("C:\\client").listFiles();
            Random random = new Random();
            int index = random.nextInt(files.length);
            File file = files[index];
            System.out.println("Enviando archivo: " + file.getAbsolutePath());

            //Flujo de datos de entrada y salida
            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
            
            // Envía el nombre del archivo
            salida.writeUTF(file.getName());
            
            // Envía el tamaño del archivo
            salida.writeInt((int) file.length());

            // Envía el contenido del archivo
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int count;
            while ((count = fis.read(buffer)) > 0) {
                salida.write(buffer, 0, count);
            }
            fis.close();
            
            System.out.println("Archivo enviado: " + file.getAbsolutePath());
            System.out.println("4 >> final for client...");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
