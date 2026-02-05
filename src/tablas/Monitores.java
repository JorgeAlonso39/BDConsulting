package tablas;

import java.sql.Date;

/**
 * @author Jorge Alonso
 */
public class Monitores {
	int cod_monitor;
	String nombre;
	String apellido;
	Date Fecha_nac;
	String Sexo;
	Gimnasios Gimnasio;
	
	public Monitores() {}
	public Monitores(int cod_monitor, String nombre, String apellido, Date fecha_nac, String sexo, Gimnasios gimnasio) {
		super();
		this.cod_monitor = cod_monitor;
		this.nombre = nombre;
		this.apellido = apellido;
		Fecha_nac = fecha_nac;
		Sexo = sexo;
		Gimnasio = gimnasio;
	}
	
	public Monitores(int cod_monitor, String nombre, String apellido, Date fecha_nac, String sexo) {
		super();
		this.cod_monitor = cod_monitor;
		this.nombre = nombre;
		this.apellido = apellido;
		Fecha_nac = fecha_nac;
		Sexo = sexo;
	}
	
	
	public int getCod_monitor() {
		return cod_monitor;
	}
	public void setCod_monitor(int cod_monitor) {
		this.cod_monitor = cod_monitor;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public Date getFecha_nac() {
		return Fecha_nac;
	}
	public void setFecha_nac(Date fecha_nac) {
		Fecha_nac = fecha_nac;
	}
	public String getSexo() {
		return Sexo;
	}
	public void setSexo(String sexo) {
		Sexo = sexo;
	}
	public Gimnasios getGimnasio() {
		return Gimnasio;
	}
	public void setGimnasio(Gimnasios gimnasio) {
		Gimnasio = gimnasio;
	}
	
	@Override
	public String toString() {
		return "Codigo monitor: " + this.cod_monitor +
				"\nNombre: " + this.nombre + 
				"\nApellido: " + this.apellido +
				"\nFecha nacimiento: " + this.Fecha_nac + 
				"\nSexo: " + this.Sexo + 
				"\nNombre Gimnasio: " + this.Gimnasio.nombre + 
				"\n***********************";
	}
	
}
