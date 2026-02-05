package tablas;

import java.util.Set;

/**
 * @author Jorge Alonso
 */
public class Clases {
	int cod_clase;
	String nombre;
	String dificultad;
	int durac_min;
	Set<Monitores>setmonitores;
	
	public Clases() {}
	public Clases(int cod_clase, String nombre, String dificultad, int durac_min, Set<Monitores> setmonitores) {
		super();
		this.cod_clase = cod_clase;
		this.nombre = nombre;
		this.dificultad = dificultad;
		this.durac_min = durac_min;
		this.setmonitores = setmonitores;
	}
	public Clases(int cod_clase, String nombre, String dificultad, int durac_min) {
		super();
		this.cod_clase = cod_clase;
		this.nombre = nombre;
		this.dificultad = dificultad;
		this.durac_min = durac_min;
	}
	
	public int getCod_clase() {
		return cod_clase;
	}
	public void setCod_clase(int cod_clase) {
		this.cod_clase = cod_clase;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDificultad() {
		return dificultad;
	}
	public void setDificultad(String dificultad) {
		this.dificultad = dificultad;
	}
	public int getDurac_min() {
		return durac_min;
	}
	public void setDurac_min(int durac_min) {
		this.durac_min = durac_min;
	}
	public Set<Monitores> getSetmonitores() {
		return setmonitores;
	}
	public void setSetmonitores(Set<Monitores> setmonitores) {
		this.setmonitores = setmonitores;
	}
	
	
}
