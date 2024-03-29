package conceptos;

import jade.content.Concept;

public class Personaje implements Concept, Comparable<Personaje> {
	
	private String nombre;
	private Integer turno;
	private String color;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Integer getTurno() {
		return turno;
	}
	public void setTurno(Integer turno) {
		this.turno = turno;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int compareTo(Personaje o) {
		if(o != null)
			return turno.compareTo(o.turno);
		return -1;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Personaje)
			if(turno.compareTo(((Personaje)obj).turno) == 0)
				return true;
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		return nombre;
	}

}
