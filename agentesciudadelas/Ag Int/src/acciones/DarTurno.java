package acciones;

import jade.content.AgentAction;
import conceptos.Jugador;
import conceptos.Personaje;

public class DarTurno implements AgentAction {

	private Jugador jugador;
	private Boolean muerto;
	private Boolean robado;
	private Personaje personaje;
	private Boolean haymuerto;
	
	public Jugador getJugador() {
		return jugador;
	}
	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}
	public Boolean getMuerto() {
		return muerto;
	}
	public void setMuerto(Boolean muerto) {
		this.muerto = muerto;
	}
	public Boolean getRobado() {
		return robado;
	}
	public void setRobado(Boolean robado) {
		this.robado = robado;
	}
	public Personaje getPersonaje() {
		return personaje;
	}
	public void setPersonaje(Personaje personaje) {
		this.personaje = personaje;
	}
	public Boolean getHaymuerto() {
		return haymuerto;
	}
	public void setHaymuerto(Boolean haymuerto) {
		this.haymuerto = haymuerto;
	}

}

