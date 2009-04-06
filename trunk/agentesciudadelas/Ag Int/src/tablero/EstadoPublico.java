package tablero;

import jade.core.AID;

import java.util.Random;


import conceptos.Distrito;
import conceptos.Jugador;
import jade.core.AID;

public class EstadoPublico {

	
	private int turno;
//	private EnumFase fase;
	private final int numJugador;
	private int jugActual;
	private int pjActual;
	private int corona;
	
	// cada jugador un agente, sin complicaciones el 0 es el ag0, el 1 es el ag1, etc...
	private ResumenJugadorPublico[] resJugadoresPublico;
	/*
	 * A�adido por Pablo
	 */
	private String nombreMuerto;
	private String nombreRobado;
	private int numJugHanJugado;
	private int destapado;
	
	
	// Protected constructor is sufficient to suppress unauthorized calls to the constructor
	private EstadoPublico() {
		EstadoPartida ep= EstadoPartida.getInstance();
		numJugador=ep.getNumJugador();
		turno=ep.getTurno();
		jugActual = ep.getJugActual();
		pjActual = ep.getPjActual();
		resJugadoresPublico=new ResumenJugadorPublico[numJugador];
		for(int i=0;i<resJugadoresPublico.length;i++){
			resJugadoresPublico[i]=new ResumenJugadorPublico(ep.getResJugadores()[i]);
		}
	
		corona = ep.getCorona();
		nombreMuerto=ep.getNombreMuerto();
		numJugHanJugado=ep.getNumJugHanJugado();
		destapado=ep.getDestapado();
		
	}
	


	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private final static EstadoPublico INSTANCE = new EstadoPublico();
	}

	public static EstadoPublico getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	public int nextJugador(){
		jugActual = (++jugActual)%numJugador;
		return jugActual;
	}

	public int getJugActual() {
		return jugActual;
	}

/*
	public int getPjActual() {
		return pjActual;
	}
*/

	public int getTurno() {
		return turno;
	}
	public int getDestapado() {
		return destapado;
	}

	public void setDestapado(int destapado) {
		this.destapado = destapado;
	}

	public void setTurno(int turno) {
		this.turno = turno;
	}

	public int getNumJugador() {
		return numJugador;
	}

	public int getCorona() {
		return corona;
	}

	public void setCorona(int corona) {
		this.corona = corona;
	}


	public String getNombreMuerto() {
		return nombreMuerto;
	}

	public void setNombreMuerto(String nombreMuerto) {
		this.nombreMuerto = nombreMuerto;
	}

	public void setJugActual(int jugActual) {
		this.jugActual = jugActual;
	}
/*
	public void setPjActual(int pjActual) {
		this.pjActual = pjActual;
	}
*/
	public int getNumJugHanJugado() {
		return numJugHanJugado;
	}

	public void setNumJugHanJugado(int numJugHanJugado) {
		this.numJugHanJugado = numJugHanJugado;
	}
	
	public String getNombreRobado() {
		return nombreRobado;
	}

	public void setNombreRobado(String nombreRobado) {
		this.nombreRobado = nombreRobado;
	}
}
