package comportamientos;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import tablero.AgTablero;
import tablero.EstadoPartida;
import tablero.ResumenJugador;
import utils.Filtros;
import utils.TipoDistrito;
import acciones.CobrarDistritosObispo;
import acciones.CobrarPorDistritos;
import acciones.DarMonedas;
import conceptos.Jugador;

public class CobrarObispo extends Behaviour {
	/*
	 * Lo mejor seria definir un mensaje para pedir cobrar el distrito del rey y que sea lo q este espera					
	 */
	
	private final AgTablero agt;
	private boolean fin = false;

	public CobrarObispo(AgTablero agTablero) {
		agt = agTablero;
	}

	@Override
	public void action() {
		EstadoPartida ep = EstadoPartida.getInstance();
		ResumenJugador jugador = ep.getJugActual();
		
		ACLMessage msg = agt.reciveBlockingMessageFrom(Filtros.COBRARDISTRITOSOBISPO,jugador, 100);
		
		if(msg!=null){
			try {
				fin = true;
				CobrarDistritosObispo contenido = (CobrarDistritosObispo)myAgent.getContentManager().extractContent(msg);
				
				// tengo el mensaje y su contenido, ahora a actualizar el estado actual
				
				Jugador jg=contenido.getJugador();

				CobrarPorDistritos cb=new CobrarPorDistritos();
				cb.setJugador(jg);
				int numCartasReligiosos = ep.getJugActual().getNumCartasColor(TipoDistrito.RELIGIOSO);
				cb.setCantidad(numCartasReligiosos);
				
				ep.getJugActual().setDinero(ep.getJugActual().getDinero()+numCartasReligiosos);

				DarMonedas obj=new DarMonedas();
				obj.setMonedas(numCartasReligiosos);
				agt.sendMSG(ACLMessage.REQUEST, jugador, obj, Filtros.DARMONEDAS);
				
				agt.sendMSG(ACLMessage.REQUEST, null, contenido, Filtros.COBRARPORDISTRITOS);
				
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
		}
	}

	@Override
	public boolean done() {
		return fin;// siempre termina
	}
}
