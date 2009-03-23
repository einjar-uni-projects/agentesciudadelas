package comportamientos;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

import acciones.CobrarDistritosMercader;
import acciones.CobrarPorDistritos;
import acciones.DarDistritos;
import acciones.DarMonedas;
import conceptos.Jugador;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import tablero.AgTablero;
import tablero.EstadoPartida;

public class EleccionCartasODinero extends Behaviour {
	/*
	 * Lo mejor seria definir un mensaje para pedir cobrar el distrito del rey y que sea lo q este espera					
	 */
	
	private final AgTablero agt;

	public EleccionCartasODinero(AgTablero agTablero) {
		agt = agTablero;
	}

	@Override
	public void action() {
		EstadoPartida ep = EstadoPartida.getInstance();
		block();
		/*
		 * a la espera de q llege un mensaje del agente pidiendo construir el distrito
		 */
		MessageTemplate filtroIdentificador = MessageTemplate.MatchOntology("CobrarDistritosMercader");
		MessageTemplate filtroEmisor = MessageTemplate.MatchSender(ep.getResJugadorActual().getIdentificador());
		MessageTemplate plantilla = MessageTemplate.and(filtroEmisor, filtroIdentificador);
		ACLMessage msg = myAgent.receive(plantilla);
		if(msg!=null){
			
			ContentElement contenido = null;
			try {
				contenido=myAgent.getContentManager().extractContent(msg);
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
			// tengo el mensaje y su contenido, ahora a actualizar el estado actual
			
			if(contenido instanceof DarDistritos){
				DarDistritos dd=(DarDistritos)contenido;
				/*
				 * Preparar el mensaje de dar distritos
				 */
			}else if(contenido instanceof DarMonedas){
				DarMonedas dm=(DarMonedas)contenido;
				/*
				 * Preparar el mensaje de dar monedas
				 */
			}
			/*
			Jugador jg=mt.getJugador();
			
			CobrarPorDistritos cb=new CobrarPorDistritos();
			cb.setJugador(jg);
			int monedas=ep.getResJugadorActual().getColores()[0];
			cb.setCantidad(monedas);
			ep.getResJugadorActual().setDinero(ep.getResJugadorActual().getDinero()+monedas);

			 */
			
			/*
			 * asi puesto son los propios agentes quienes tienen q comprobar que no es un mensaje para ellos, 
			 * eso se hace comparando su jugador con el que enviamos
			 */
			/*
			ACLMessage msgEnviar = new ACLMessage(ACLMessage.REQUEST);
			msgEnviar.setOntology("CobrarPorDistritos");
			msg.setSender(agt.getAID());
			try {
				myAgent.getContentManager().fillContent(msgEnviar, cb);
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // contenido es el objeto que envia
			*/
		}
		
	}

	@Override
	public boolean done() {
		return true;// siempre termina
	}
}