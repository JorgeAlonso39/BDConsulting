package tablas;

import java.util.Set;

/**
 * @author Jorge Alonso
 */
public class Gimnasios {
	int cod_centro;
	String nombre;
	Monitores responsable;
	String direccion;
	String localidad;
	String provincia;
	Set<Monitores> setmonitores;
	
	public Gimnasios() {}
	public Gimnasios(int cod_cento, String nombre, Monitores responsable, String direccion, String localidad,
			String provincia, Set<Monitores> setmonitores) {
		super();
		this.cod_centro = cod_cento;
		this.nombre = nombre;
		this.responsable = responsable;
		this.direccion = direccion;
		this.localidad = localidad;
		this.provincia = provincia;
		this.setmonitores = setmonitores;
	}
	public Gimnasios(int cod_cento, String nombre, Monitores responsable, String direccion, String localidad,
			String provincia) {
		super();
		this.cod_centro = cod_cento;
		this.nombre = nombre;
		this.responsable = responsable;
		this.direccion = direccion;
		this.localidad = localidad;
		this.provincia = provincia;
	}
	public Gimnasios(int cod_cento, String nombre, String direccion, String localidad,
			String provincia) {
		super();
		this.cod_centro = cod_cento;
		this.nombre = nombre;
		this.direccion = direccion;
		this.localidad = localidad;
		this.provincia = provincia;
	}


	public int getCod_centro() {
		return cod_centro;
	}


	public void setCod_centro(int cod_cento) {
		this.cod_centro = cod_cento;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public Monitores getResponsable() {
		return responsable;
	}


	public void setResponsable(Monitores responsable) {
		this.responsable = responsable;
	}


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public String getLocalidad() {
		return localidad;
	}


	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}


	public String getProvincia() {
		return provincia;
	}


	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}


	public Set<Monitores> getSetmonitores() {
		return setmonitores;
	}


	public void setSetmonitores(Set<Monitores> setmonitores) {
		this.setmonitores = setmonitores;
	}
	
	
	
	
}
