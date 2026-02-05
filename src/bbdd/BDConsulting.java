package bbdd;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.ODBRuntimeException;
import org.neodatis.odb.ObjectValues;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Values;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.IValuesQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.impl.core.query.values.ValuesCriteriaQuery;

import tablas.Clases;
import tablas.Gimnasios;
import tablas.Monitores;

/**
 * @author Jorge Alonso
 * @version 1.0
 * Clase BDConsulting
 */
public class BDConsulting {

	/*
	 * Método principal de la clase
	 */
	public static void main(String[] args) {
		
		/*
		 * Menú principal
		 */
		ODB odb = ODBFactory.open("neodatis.test");
		Scanner sn = new Scanner(System.in);
		try {
			boolean salir = false;
			int opcion = 0;
			do {
				menu();
				System.out.println("Introduce la opcion: ");
				opcion = sn.nextInt();
				switch(opcion) {
				case 1:
					System.out.println("Insertar datos a la base de datos");
					insertarBBDD(odb);
					break;
				case 2:
					consulta1(odb);
					break;
				case 3:
					consulta2(odb);
					break;
				case 4:
					consulta3(odb);
					break;
				case 5:
					consulta4(odb);
					break;
				case 6:
					consulta5(odb);
					break;
				case 7:
					salir = true;
					break;
				}
			}while(!salir);
			
			
		} catch (SQLException e) {
			System.out.println("Error al crear la conexion");
		}catch(NoSuchElementException e) {
			System.out.println("Valor erroneo" + e.getMessage());
		}
		odb.commit();
		odb.close();
		sn.close();
		System.out.println("Terminado.");
		
		
	}
	
	/*
	 * Metodo que muestra el menú principal
	 */
	public static void menu() {
		System.out.println("Menú principal");
		System.out.println("1. Traspasar los datos de MySQL a neodatis");
		System.out.println("2. Primera consulta");
		System.out.println("3. Segunda consulta");
		System.out.println("4. Tercera consulta");
		System.out.println("5. Cuarta consulta");
		System.out.println("6. Quinta consulta");
		System.out.println("7. Salir");
	}
	
	/*
	 * Metodo para insetar los datos a neodatis
	 * Orden de inserccion:
	 * 	-Clases
	 * 	-Gimnasios
	 * 	-Monitores
	 * 	-Responsable a cada gimnasio
	 * 	-SetMonitores a cada gimnasio
	 * 	-SetMonitores a cada clase
	 */
	public static void insertarBBDD(ODB odb) throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bdrel_clases", "root", "jorge");
		List<Clases> clases = ConnectorMySQL.obtenerClases(conn);
		System.out.println("Insertando clases...");
		for(Clases clase : clases) {
			boolean existe1 = existeClase(odb,clase);
			if(existe1) {
				odb.store(clase);
			}else {
				System.out.println("Clase duplicada, no insertada. Codigo clase: " + clase.getCod_clase());
			}
			
		}
		System.out.println("Insertando gimnasios...");
		List<Gimnasios> gym = ConnectorMySQL.obtenerGimnasios(conn);
		for(Gimnasios gim : gym) {
			boolean existe2 = existeGym(odb,gim);
			if(existe2) {
				odb.store(gim);
			}else {
				System.out.println("Gimnasio duplicado, no insertado. Codigo gimnasio: " + gim.getCod_centro());
			}
		}
		System.out.println("Insertando monitores...");
		List<Monitores> monitores = ConnectorMySQL.obtenerMonitores(conn,odb);
		for(Monitores monitor : monitores) {
			boolean existe3 = existeMonitor(odb,monitor);
			if(existe3) {
				odb.store(monitor);
			}else {
				System.out.println("Monitor duplicado, no insertado. Codigo monitor: " + monitor.getCod_monitor());
			}
		}
		

		insertarResponsable(conn,odb);
		
		insertarSetGimnasios(conn,odb);
		insertarSetClases(conn,odb);

	}
	
	
	/*
	 * Metodo para insetar un responsable a un gimnasio
	 */
	public static void insertarResponsable(Connection conex, ODB odb) {
		IQuery query1 = new CriteriaQuery(Gimnasios.class);
		Objects<Gimnasios> gimnasios = odb.getObjects(query1);
		for (Gimnasios gym : gimnasios) {
			int codRes = ConnectorMySQL.obtenerResponsable(conex, odb, gym);
			IQuery query = new CriteriaQuery(Monitores.class, Where.equal("cod_monitor", codRes));
			Objects<Monitores> monitores = odb.getObjects(query);
			Monitores monitor = monitores.getFirst();
			gym.setResponsable(monitor);
			odb.store(gym);
		}

		odb.commit();
	}
	
	/*
	 * MEtodo que inserta el setMonitores a los gimnasios
	 */
	public static void insertarSetGimnasios(Connection conn, ODB odb) {
		IQuery query = new CriteriaQuery(Gimnasios.class);
		Objects<Gimnasios> gimnasios = odb.getObjects(query);
		for(Gimnasios gim : gimnasios) {
			List<Integer> ids = ConnectorMySQL.obtenerSetMonitoresGimnasio(conn, odb, gim);
			Set<Monitores> Setmonitores = devolverMonitores(odb,ids);
			gim.setSetmonitores(Setmonitores);
			boolean existe2 = existeGym(odb,gim);
			odb.store(gim);
			
		}
		odb.commit();
	}
	
	/*
	 * MEtodo que inserta el setMonitores a las clases
	 */
	public static void insertarSetClases(Connection conn, ODB odb) {
		IQuery query = new CriteriaQuery(Clases.class);
		Objects<Clases> clases = odb.getObjects(query);
		for(Clases clase : clases) {
			List<Integer> ids = ConnectorMySQL.obtenerSetMonitoresClases(conn, odb, clase);
			Set<Monitores> Setmonitores = devolverMonitores(odb,ids);
			clase.setSetmonitores(Setmonitores);
			boolean existe1 = existeClase(odb,clase);
				odb.store(clase);
			
		}
		odb.commit();
	}
	
	/*
	 * Los 3 siguientes metodos validan si existe ese campo ya en la base de datos de neodatis
	 */
	public static boolean existeClase(ODB odb,Clases clase) {
		 IQuery query = new CriteriaQuery(Clases.class, Where.equal("cod_clase", clase.getCod_clase()));
	        Objects<Clases> clases = odb.getObjects(query); // Recupera lista de objetos
	        return clases.isEmpty();
	}
	
	public static boolean existeGym(ODB odb,Gimnasios gym) {
		 IQuery query = new CriteriaQuery(Gimnasios.class, Where.equal("cod_centro", gym.getCod_centro()));
	        Objects<Gimnasios> gimnasios = odb.getObjects(query); // Recupera lista de objetos
	        return gimnasios.isEmpty();
	}
	
	public static boolean existeMonitor(ODB odb,Monitores monitor) {
		 IQuery query = new CriteriaQuery(Monitores.class, Where.equal("cod_monitor", monitor.getCod_monitor()));
	     Objects<Monitores> monitores = odb.getObjects(query); // Recupera lista de objetos
	     return monitores.isEmpty();
	}
	
	
	/*
	 * Metodo que devuelve el setMonitores a las clases o a los gimnasios
	 */
	public static Set<Monitores> devolverMonitores(ODB odb, List<Integer> idMonitor) {
	    Set<Monitores> monitores = new HashSet<>();

	    for (int id : idMonitor) {
	        IQuery query = new CriteriaQuery(Monitores.class, Where.equal("cod_monitor", id));
	        
	        Objects<Monitores> monitoress = odb.getObjects(query);
	        
	        if (!monitoress.isEmpty()) {
	            monitores.add(monitoress.getFirst());
	        }
	    }

	    return monitores;
	}
	
	/*
	 * Primera consulta: Muestra el nombre del centro, el nombre del responsable y la localidad del centro
	 * ordenado por localidad para una provincia específica.
	 */
	public static void consulta1(ODB odb) {
		Scanner sc = new Scanner(System.in);
		try {
			System.out.println("Mostrar el nombre del centro, el nombre del responsable, la localidad del centro ordenado por localidad para una provincia: ");
			String provincia = sc.nextLine();
			try {
				// Crear la consulta con filtro por provincia
				Values valores = odb.getValues(
					    (IValuesQuery) new ValuesCriteriaQuery(Gimnasios.class, Where.equal("provincia", provincia))
					        .field("nombre")
					        .field("responsable.nombre")
					        .field("localidad")
					        .orderByAsc("localidad")
					);
			
			
				 // Mostrar los resultados
		        while (valores.hasNext()) {
		        	ObjectValues ov = (ObjectValues) valores.next();
		        	 System.out.printf("%s %s %s%n", ov.getByIndex(0), ov.getByIndex(1), ov.getByIndex(2));
		        }
			} catch (ClassCastException e) {
                System.out.println("Error al procesar un resultado: " + e.getMessage());
            } catch (NoSuchElementException e) {
                System.out.println("No hay más elementos para leer en la consulta.");
            }
	       
		} catch (ODBRuntimeException e) {
	        System.out.println("Error en la base de datos: " + e.getMessage());
	    } catch (InputMismatchException e) {
	        System.out.println("Entrada inválida. Intente de nuevo.");
	    } catch (Exception e) {
	        System.out.println("Ocurrió un error inesperado: " + e.getMessage());
	    } finally {
	        sc.close();
	    }
		
	}
	
	/*
	 * Consulta que devuelve la duracion media de una clase segun el nivel de dificultad introducido
	 */
	public static void consulta2(ODB odb) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Introduce el nivel de dificultad: ");
            String dificultad = sc.nextLine().toUpperCase();

            //Consulta
            Values valores = odb.getValues(new ValuesCriteriaQuery(Clases.class, Where.equal("dificultad", dificultad))
                    .avg("durac_min"));

            //Mostrar los datos
            if (valores.hasNext()) {
                ObjectValues ov = valores.nextValues();
                BigDecimal promedio = (BigDecimal) ov.getByAlias("durac_min");
                System.out.printf("Duración media: %.2f minutos%n", promedio.floatValue());
            } else {
                System.out.println("No se encontraron clases con esa dificultad.");
            }
        } catch (Exception e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }
	
	/*
	 * Consulta que muestra los datos de mujeres cuyo nombre no termina en a
	 */
	public static void consulta3(ODB odb) {
        try {
            System.out.println("Monitores mujeres cuyo nombre no termina en 'A':");
            //Consulta
            Objects<Monitores> objetos = odb.getObjects(new CriteriaQuery(Monitores.class, Where.and()
                    .add(Where.equal("Sexo", "M"))
                    .add(Where.not(Where.like("nombre", "%a")))));

            while (objetos.hasNext()) {
                System.out.println(objetos.next());
            }
        } catch (Exception e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }
	
	/*
	 * Consulta que muestra la duracion total de las clases ordenadas por monitor
	 */
	 public static void consulta4(ODB odb) {
	        try {
	            System.out.println("Duración total de clases por monitor:");

	            Objects<Monitores> monitores = odb.getObjects(new CriteriaQuery(Monitores.class));

	            for (Monitores monitor : monitores) {
	                BigDecimal duracionTotal = BigDecimal.ZERO;

	                Objects<Clases> clases = odb.getObjects(new CriteriaQuery(Clases.class));

	                for (Clases clase : clases) {
	                    if (clase.getSetmonitores().contains(monitor)) {
	                        duracionTotal = duracionTotal.add(BigDecimal.valueOf(clase.getDurac_min()));
	                    }
	                }

	                System.out.printf("Monitor: %s - Duración total: %.2f minutos%n",
	                        monitor.getNombre(), duracionTotal.floatValue());
	            }
	        } catch (Exception e) {
	            System.out.println("Error en la consulta: " + e.getMessage());
	        }
	    }
	 
	 /*
	  * Consulta que muestra la provincia y el número de centros agrupados por provincia
	  */
	 public static void consulta5(ODB odb) {
		    try {
		        System.out.println("Mostrar la provincia y el número de centros agrupados por provincia:");

		        // Crear la consulta para agrupar por provincia y contar el número de centros
		        Values valores = odb.getValues(new ValuesCriteriaQuery(Gimnasios.class)
		                .field("provincia") 
		                .count("nombre") 
		                .groupBy("provincia")); 

		        // Mostrar los resultados
		        while (valores.hasNext()) {
		            ObjectValues ov = valores.next();
		            String provincia = (String) ov.getByIndex(0); 
		            BigInteger numCentrosBigInt = (BigInteger) ov.getByIndex(1); 
		            long numCentros = numCentrosBigInt.longValue(); 
		            System.out.printf("Provincia: %s, Número de centros: %d%n", provincia, numCentros);
		        }

		    } catch (ODBRuntimeException e) {
		        System.out.println("Error en la base de datos: " + e.getMessage());
		    } catch (Exception e) {
		        System.out.println("Ocurrió un error inesperado: " + e.getMessage());
		    }
		}


}
