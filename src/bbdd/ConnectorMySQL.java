package bbdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

import java.sql.Date;

import tablas.Clases;
import tablas.Gimnasios;
import tablas.Monitores;

/**
 * @author Jorge Alonso
 * Clase para pasar los datos de mySQl a neodatis
 */
public class ConnectorMySQL {
	 private static final String URL = "jdbc:mysql://localhost/fitnessclub";
	    private static final String USER = "root";
	    private static final String PASSWORD = "jorge";
	    
	    
	    /*
	     * Metodo que recupera las clases de la base de datos y devuelve una lista de clases
	     */
	    public static List<Clases> obtenerClases(Connection conn) {
	        List<Clases> clases = new ArrayList<>();
	        String query = "SELECT * FROM Clases";
	        
	        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
	            while (rs.next()) {
	                int codClase = rs.getInt("cod_clase");
	                String nombre = rs.getString("nombre");
	                String dificultad = rs.getString("dificultad");
	                int duracion = rs.getInt("durac_min");
	                
	                clases.add(new Clases(codClase, nombre, dificultad, duracion));
	            }
	        } catch (SQLException e) {
	            System.err.println("Error al obtener clases: " + e.getMessage());
	        }
	        
	        return clases;
	    }

	    /*
	     * Metodo que recupera los gimnasios de la base de datos y devuelve una lista de gimnsios
	     */
	    public static List<Gimnasios> obtenerGimnasios(Connection conn) {
	        List<Gimnasios> gimnasios = new ArrayList<>();
	        String query = "SELECT * FROM Gimnasios";
	        
	        try (Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(query)) {
	            
	            while (rs.next()) {
	                int codCentro = rs.getInt("cod_centro");
	                String nombre = rs.getString("nombre");
	                String direccion = rs.getString("direccion");
	                String localidad = rs.getString("localidad");
	                String provincia = rs.getString("provincia");
	                
	                gimnasios.add(new Gimnasios(codCentro, nombre, direccion, localidad, provincia));
	            }
	        } catch (SQLException e) {
	            System.err.println("Error al obtener gimnasios: " + e.getMessage());
	        }
	        
	        return gimnasios;
	    }

	    /*
	     * Metodo que recupera los monitores de la base de datos y devuelve una lista de monitores
	     */
	    public static List<Monitores> obtenerMonitores(Connection conn, ODB conex) {
	        List<Monitores> monitores = new ArrayList<>();
	        String query = "SELECT * FROM Monitores";
	        
	        try (Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(query)) {
	            
	            while (rs.next()) {
	                int codMonitor = rs.getInt("cod_monitor");
	                String nombre = rs.getString("nombre");
	                String apellido = rs.getString("apellido");
	                Date fecha = rs.getDate("Fecha_nac");
	                String sexo = rs.getString("sexo");
	                int codGym = rs.getInt("cod_centro");

	                // Obtener el gimnasio correspondiente
	                Gimnasios gimnasio = obtenerGimnasioPorId(conex, codGym);
	                
	                // Crear el monitor con el objeto Gimnasios
	                monitores.add(new Monitores(codMonitor, nombre, apellido, fecha, sexo, gimnasio));
	            }
	        } catch (SQLException e) {
	            System.err.println("Error al obtener monitores: " + e.getMessage());
	            e.printStackTrace();
	        }
	        
	        return monitores;
	    }

	    /*
	     * Metodo que optiene el gimnasio por id
	     */
	    public static Gimnasios obtenerGimnasioPorId(ODB odb, int codGym) {
	        IQuery query = new CriteriaQuery(Gimnasios.class, Where.equal("cod_centro", codGym));
	        Objects<Gimnasios> gimnasios = odb.getObjects(query); // Recupera lista de objetos

	        if (!gimnasios.isEmpty()) {
	            return gimnasios.getFirst(); // Obtiene el primer gimnasio encontrado
	        }

	        return null; // Devuelve null si no se encuentra
	    }
	    
	    /*
	     * Metodo que obtiene el responsable de un gimnasio
	     */
	    public static int obtenerResponsable(Connection conn, ODB conex, Gimnasios gym) {
	        String query = "SELECT Responsable FROM Gimnasios WHERE Cod_centro = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            // Usar parámetros para evitar inyección SQL
	            stmt.setInt(1, gym.getCod_centro());
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                // Verificar si hay resultados
	                if (rs.next()) {
	                    int cod = rs.getInt("Responsable");
	                    return cod;
	                } else {
	                    System.out.println("No se encontró un responsable para el gimnasio con Cod_centro: " + gym.getCod_centro());
	                }
	            }
	        } catch (SQLException e) {
	            System.err.println("Error al obtener el responsable: " + e.getMessage());
	        }
	        return 0;
	    }
	    
	    /*
	     * Metodo que obtiene el SetMonitores de un gimnasio
	     * Estos son los monitores asociados a un gimnasio
	     */
	    public static List<Integer> obtenerSetMonitoresGimnasio(Connection conn, ODB conex,Gimnasios gym) {
	        List<Integer> idMonitores = new ArrayList<>();
	        String query = "SELECT * FROM Monitores WHERE Cod_centro = ? ";
	        
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	        	stmt.setInt(1, gym.getCod_centro());
	        	ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	                int codMonitor = rs.getInt("cod_monitor");
	               idMonitores.add(codMonitor);
	            }
	        } catch (SQLException e) {
	            System.err.println("Error al obtener monitores: " + e.getMessage());
	        }
	        
	        return idMonitores;
	    }
	    
	    
	    /*
	     * Metodo que obtiene el SetMonitores de una clase
	     * Estos son los monitores asociados a una clase
	     */
	    public static List<Integer> obtenerSetMonitoresClases(Connection conn, ODB conex,Clases clase) {
	        List<Integer> idMonitores = new ArrayList<>();
	        String query = "SELECT * FROM clasmon WHERE Cod_Clase = ? ";
	        
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	        	stmt.setInt(1, clase.getCod_clase());
	        	ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	                int codMonitor = rs.getInt("Cod_monitor");
	               idMonitores.add(codMonitor);
	            }
	        } catch (SQLException e) {
	            System.err.println("Error al obtener monitores: " + e.getMessage());
	        }
	        
	        return idMonitores;
	    }

	    


	    
	    
	    

}
