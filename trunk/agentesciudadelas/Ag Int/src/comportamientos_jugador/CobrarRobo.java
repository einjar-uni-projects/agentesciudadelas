package comportamientos_jugador;

import tablero.EstadoPartida;
import tablero.ResumenJugador;
import utils.Filtros;
import acciones.Monedas;
import acciones.Robar;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jugador.AgJugador;

public class CobrarRobo extends Behaviour {

	private final AgJugador _agj;
	private final Behaviour beh;
	private final AID raid;

	public CobrarRobo(AgJugador agj, Behaviour ft, AID aid) {
		_agj = agj;
		beh = ft;
		raid = aid;
	}
	
	@Override
	public void action(){
		
		ACLMessage msg = _agj.reciveBlockingMessageFrom(Filtros.COBRARROBO,raid, 100);
		
		if(msg!=null){
			Monedas m=new Monedas();;
			try {
				m=(Monedas) _agj.getContentManager().extractContent(msg);
			} catch (UngroundedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			_agj.setMonedas(m.getDinero()+_agj.getMonedas());
		}
	}

	@Override
	public boolean done() {
		
		return true;
	}
}