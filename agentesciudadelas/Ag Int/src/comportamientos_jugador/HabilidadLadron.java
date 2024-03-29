package comportamientos_jugador;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jugador.AgJugador;
import utils.Filtros;
import acciones.NotificarRobado;
import conceptos.Personaje;

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
		
		NotificarRobado nr=new NotificarRobado();
		nr.setPersonaje(p);
		
		_agj.sendMSG(ACLMessage.REQUEST, raid, nr, Filtros.ROBAR);
	}

	@Override
	public boolean done() {
		_agj.addBehaviour(beh);
		_agj.addBehaviour(new CobrarRobo(_agj, raid));
		return true;
	}
}