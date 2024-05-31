package com.cibertec.bd;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.FileInputStream;


public class Server {
    
    private static final int PORT = 5000;

    public Server(){
    	System.out.println("1 >> [ini] Server constructor");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
        	System.out.println("2 >> waiting for client...");    
            while(true){
                Socket clienSocket = serverSocket.accept();
                
                DataInputStream entrada = new DataInputStream(clienSocket.getInputStream());

                // Recibe el nombre de la imagen
                String nombreImagen = entrada.readUTF();
                
                // Recibe el tamaño de la imagen
                int tamanoImagen = entrada.readInt();

                // Recibe el contenido de la imagen
                byte[] buffer = new byte[1024];
                File file = new File("C:\\server\\" + nombreImagen);
                FileOutputStream fos = new FileOutputStream(file);
                int count;
                while ((count = entrada.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                
                // Guarda la información en la base de datos
                guardarImagenEnBD(nombreImagen, tamanoImagen, file);
                
                clienSocket.close();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void guardarImagenEnBD(String nombreImagen, int tamanoImagen, File file) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/socket_bd", "root", "mysql")) {
            String sql = "INSERT INTO bd_imagen (nombre, tamaño, archivo) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nombreImagen);
                stmt.setInt(2, tamanoImagen);
                stmt.setBlob(3, new FileInputStream(file));
                stmt.executeUpdate();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        new Server();   
    }
}
