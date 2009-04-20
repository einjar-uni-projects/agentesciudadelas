package comportamientos_jugador;

import conceptos.Personaje;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jugador.AgJugador;
import utils.Filtros;
import acciones.Monedas;

public class HabilidadLadron extends Behaviour{
	private final AgJugador _agj;
	private final Behaviour beh;
	private final AID raid;

	public HabilidadLadron(AgJugador agj, Behaviour ft, AID aid) {
		_agj = agj;
		beh = ft;
		raid = aid;
	}
	
	@Override
	public void action(){
		
		Personaje p=_agj.seleccionarPersonajeRobo(); 
		
		
		
		_agj.sendMSG(ACLMessage.REQUEST, null, m, Filtros.PAGARROBO);
	}

	@Override
	public boolean done() {
		_agj.addBehaviour(beh);
		return true;
	}