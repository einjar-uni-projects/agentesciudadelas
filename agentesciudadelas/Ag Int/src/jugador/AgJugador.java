package jugador;

import jade.content.AgentAction;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.leap.LEAPCodec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.List;

import java.util.Iterator;
import java.util.LinkedList;

import onto.OntologiaCiudadelas;
import utils.Personajes;
import utils.TipoDistrito;
import acciones.DestruirDistrito;
import acciones.InfoPartida;
import acciones.NotificarFinTurnoJugador;
import acciones.OfertarPersonajes;
import acciones.PedirDistritoJugadores;

import comportamientos_jugador.RecibirIniciarJugador;

import conceptos.Distrito;
import conceptos.Jugador;
import conceptos.Personaje;

public abstract class AgJugador extends jade.core.Agent {
	
	//Informacion del estado del jugador
	protected Personaje pj_actual = null;
	protected int monedas = 0;
	protected final LinkedList<Distrito> mano = new LinkedList<Distrito>();
	protected final LinkedList<Distrito> construidas = new LinkedList<Distrito>();
	protected AID msg_sender;
	protected Personajes [] destapados;
	protected Behaviour cambiarMano;
	protected int turno = 0;
	
	
	public int getTurno() {
		return turno;
	}
	
	public void addTurno() {
		this.turno++;
	}
	
	public Behaviour getCambiarMano() {
		return cambiarMano;
	}
	public void setCambiarMano(Behaviour cambiarMano) {
		this.cambiarMano = cambiarMano;
	}
	//private Codec codec = new SLCodec();
	private Codec codec2 = new LEAPCodec();
	private final OntologiaCiudadelas onto = OntologiaCiudadelas.getInstance();
	
	private InfoPartidaPropia infoPartida = new InfoPartidaPropia();
	
	// almacena el personaje muerto en este turno
	protected Personaje _muerto;
	protected Personaje _robado;
	
	
	public Codec getCodec() {
		return codec2;
	}
	public OntologiaCiudadelas getOnto() {
		return onto;
	}
	
	/*
	 * Codigo relacionado con la inicializacion del agente
	 * @see jade.core.Agent#setup()
	 */
	public void setup(){
		//Saludo del agente
		System.out.println("Soy el agente "+ getAID()+" y represento a un Jugador.");
		
		//Se registra la ontologia
		getContentManager().registerLanguage(codec2);
		getContentManager().registerOntology(onto);
		
		//Se registra el agente en el servicio del directorio
		///Se describen los servicios del agente
		ServiceDescription sd= new ServiceDescription(); 
		sd.setType("jugador");
		sd.setName("jugador1");
		
		///Se crea una descripción del agente
		DFAgentDescription dfd= new DFAgentDescription(); 
		dfd.setName(getAID());
		dfd.addServices(sd);
		///Se registra
		try { 
			DFService.register(this,dfd); 
		}catch (FIPAException fe) {
			fe.printStackTrace(); 
		}
		
		RecibirIniciarJugador comp = new RecibirIniciarJugador(this);
		addBehaviour(comp);
	}

	public void cambiarMano(List manoNueva){
		this.mano.clear();
		for (int i = 0; i < manoNueva.size(); i++){
			this.mano.add((Distrito)manoNueva.get(i));
		}
	}
	
	/*
	 * Se liberan los ercursos
	 * @see jade.core.Agent#takeDown()
	 */
	@Override
	protected void takeDown() { // Aqui se ponen las operaciones de limpieza de recursos
	    try {
	    	DFService.deregister(this); // Intenta darse de baja del resgitro de las paginas amarillas
	    }catch (FIPAException fe) {
	    	fe.printStackTrace();
	    }
	    System.out.println("Seller-agent "+getAID().getName()+" terminating."); // Esribe en pantalla un mensaje de terminaci�n
	}
	public InfoPartidaPropia getInfoPartida() {
		return infoPartida;
	}
	public void setInfoPartida(InfoPartidaPropia infoPartida) {
		this.infoPartida = infoPartida;
	}
	
	public Jugador getJugador(){
		Jugador ret = new Jugador();
		ret.setMano(infoPartida.getMano());
		ret.setMonedas(infoPartida.getMonedas());
		ret.setPuntos(infoPartida.getPuntos());
		ret.setNombre(getName());
		return ret;
	}
	
	/*
	 * Esta funcion simplifica el envio de mensajes rellenando los campos necesarios
	 */
	public boolean sendMSG(int perf, AID reciver, AgentAction msgContent, String filtro) {
		boolean ret = false;
		
		ACLMessage msgEnviar = new ACLMessage(perf);
		msgEnviar.setSender(getAID());
		msgEnviar.setLanguage(getCodec().getName());
		msgEnviar.setOntology(getOnto().getName());
		if(filtro != null){
			msgEnviar.setConversationId(filtro);
		}
		if (reciver != null)
			msgEnviar.addReceiver(reciver);

		try {
			getContentManager().fillContent(msgEnviar, msgContent);
			System.out.println("$ Envio de mensaje:");
			if(reciver != null)
				System.out.println("| "+getAID().getName()+" manda un mensaje a "+ reciver.getName());
			else
				System.out.println("| "+getAID().getName()+" manda un mensaje a todos");
			if(filtro != null)
				System.out.println("| Conversacion: "+filtro);
			System.out.println("| Contenido: "+msgContent);
			System.out.println("$ Fin de mensaje\n");
			send(msgEnviar);
			ret = true;
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	/*
	 * Funcion que se bloquea esperando un mensaje con un determinado filtro
	 */
	public ACLMessage reciveBlockingMessage(String filtro, boolean global){
		ACLMessage ret = null;
		MessageTemplate filtroIdentificador = MessageTemplate.MatchConversationId(filtro);
		if(global){
			ret = blockingReceive(filtroIdentificador);
		}else{
			AID[] aids = new AID[]{getAID()};
			MessageTemplate filtroReceptor = MessageTemplate.MatchReceiver(aids);
			MessageTemplate plantilla = MessageTemplate.and(filtroReceptor, filtroIdentificador);
			ret = blockingReceive(plantilla);
		}		
		return ret;
	}
	
	
	
	public ACLMessage reciveBlockingMessage(String filtro, boolean global, long mills){
		ACLMessage ret = null;
		MessageTemplate filtroIdentificador = MessageTemplate.MatchConversationId(filtro);
		if(global){
			ret = blockingReceive(filtroIdentificador);
		}else{
			AID[] aids = new AID[]{getAID()};
			MessageTemplate filtroReceptor = MessageTemplate.MatchReceiver(aids);
			MessageTemplate plantilla = MessageTemplate.and(filtroReceptor, filtroIdentificador);
			ret = blockingReceive(plantilla, mills);
		}		
		return ret;
	}
	
	public ACLMessage reciveBlockingMessageFrom(String filtro, AID sender,  boolean global){
		ACLMessage ret = null;
		MessageTemplate filtroIdentificador = MessageTemplate.MatchConversationId(filtro);
		MessageTemplate filtroEmisor = MessageTemplate.MatchSender(sender);
		MessageTemplate plantilla = MessageTemplate.and(filtroEmisor, filtroIdentificador);
		if(global){
			ret = blockingReceive(plantilla);
		}else{
			AID[] aids = new AID[]{getAID()};
			MessageTemplate filtroReceptor = MessageTemplate.MatchReceiver(aids);
			MessageTemplate plantilla2 = MessageTemplate.and(filtroReceptor, filtroIdentificador);
			ret = blockingReceive(plantilla2);
		}		
		return ret;
	}
	
	public NotificarFinTurnoJugador getNotificarFinTurnoJugador() {
		NotificarFinTurnoJugador ret = new NotificarFinTurnoJugador();
		ret.setJugador(getJugador());
		ret.setPersonaje(pj_actual);
		return ret;
	}
	
	/*
	 * Funcion que se bloquea esperando un mensaje con un determinado filtro
	 */
	public ACLMessage reciveBlockingMessageFrom(String filtro, AID sender, long mills){
		ACLMessage ret = null;
		MessageTemplate filtroIdentificador = MessageTemplate.MatchConversationId(filtro);
		MessageTemplate filtroEmisor = MessageTemplate.MatchSender(sender);
		MessageTemplate plantilla = MessageTemplate.and(filtroEmisor, filtroIdentificador);
		//TODO 100 milisegundos para recibir un mensaje
		ret = blockingReceive(plantilla, mills);
		return ret;
	}
	
	public void addMonedas(int mas){
		monedas += mas;
	}
	
	public void addDistritos(List list){
		Iterator it = list.iterator();
		while(it.hasNext()){
			mano.add((Distrito)it.next());
		}
	}
	
	public void printEstado(){
		StringBuilder sb = new StringBuilder();
		sb.append("> Soy ");
		sb.append(getAID().getName());
		sb.append(" mi estado del turno ");
		sb.append(turno);
		sb.append(" es:\n");
		sb.append("> personaje: ");
		sb.append(pj_actual.getNombre());
		sb.append("\n> monedas: ");
		sb.append(monedas);
		sb.append("\n> mano: ");
		for (Distrito d : mano) {
			sb.append(d.toString());
			sb.append(" ");
		}
		sb.append("\n> construido: ");
		for (Distrito d : construidas) {
			sb.append(d.toString());
			sb.append(" ");
		}
		sb.append("\n");
		System.out.println(sb.toString());
	}
	
	protected Distrito[] getDistritosConstruibles(){
		LinkedList<Distrito> d = new LinkedList<Distrito>();
		for (Distrito distrito : mano) {
			if(distrito.getCoste() <= monedas && isNoConstruida(distrito)){
				d.add(distrito);
			}
		}
		return d.toArray(new Distrito[d.size()]);
	}
	
	protected boolean isNoConstruida(Distrito distrito) {
		boolean ret = true;
		for (Distrito dist : construidas) {
			if(dist.getNombre().compareTo(distrito.getNombre())==0){
				ret = false;
			}
		}
		return ret;
	}
	
	public Personaje getPj_actual() {
		return pj_actual;
	}
	
	/*
	 * Construye un distrito
	 */
	public void construir(Distrito dist) {
		mano.remove(dist);
		construidas.add(dist);
	}
	
	
	/*
	 * Indica el número de cartas que se pudeden construir
	 */
	protected int cartasManoNoConstruidas(){
		int distintos = 0;
		for (Distrito distrito : mano) {
			if(isNoConstruida(distrito)){
				distintos++;
			}
		}
		return distintos;
	}
	
	/**
	 * Indica si se dispone de alguna carta ya construida con el mismo color que el distrito
	 * @param d distrito que se desea comprobar
	 * @return true si ya se tiene una carta con el color del distrito d
	 */
	protected boolean tengoColor(Distrito d){
		boolean ret = false;
		for (Distrito construido : construidas) {
			if(d.getColor().compareTo(construido.getColor()) == 0)
				ret = true;
		}
		return ret;
	}
	
	public void setPersonajesDescartados(List destapados) {
		this.destapados = new Personajes[destapados.size()];
		for (int i = 0; i < destapados.size(); i++) {
			this.destapados[i] = Personajes.getPersonajeByPJ((Personaje)destapados.get(i));
		}
	}
	
	/**
	 * Cuenta los distritos del tipo indicado
	 * @param color tipo de distrito indicado
	 * @return número de distritos del tipo indicado
	 */
	protected int cuentaColor(TipoDistrito color){
		int ret = 0;
		for (Distrito construido : construidas) {
			if(color.getColor().compareTo(construido.getColor()) == 0)
				ret++;
		}
		return ret;
	}
	
	Distrito[] validos(List list){
		
		int din=this.monedas;
		int cont=0;
		for(int i=0;i<list.size();i++){
			if(din>=(((Distrito)(list.get(i))).getCoste()-1))
				cont++;
		}
		cont=0;
		Distrito [] sal= new Distrito[cont];
		for(int i=0;i<list.size();i++){
			if(din>=(((Distrito)(list.get(i))).getCoste()-1))
				sal[cont]=(Distrito)(list.get(i));
		}
		return sal;
	}
	
	
	public Personaje get_muerto() {
		return _muerto;
	}

	public void set_muerto(Personaje _muerto) {
		this._muerto = _muerto;
	}
	
	public int getMonedas() {
		return monedas;
	}
	
	public void setMonedas(int monedas) {
		this.monedas = monedas;
	}
	
	public Personaje get_robado() {
		return _robado;
	}

	public void set_robado(Personaje _robado) {
		this._robado = _robado;
	}
	
	public abstract Personaje selectPersonaje(OfertarPersonajes contenido);
	public abstract Behaviour jugarTurno(ACLMessage msg);
	public abstract Distrito getDistritoConstruir();
	public abstract Personaje getPersonajeMatar();
	public abstract Distrito[] descartaDistritos(List distritos);
	public abstract boolean seleccionarMonedasOCartas();
	public abstract Jugador seleccionarJugadorCambiarCartas(Jugador jug1, Jugador jug2, Jugador jug3);
	public abstract void getDistritoDestruir(PedirDistritoJugadores pd, DestruirDistrito dd);
	public abstract Personaje seleccionarPersonajeRobo();
	public abstract void setInfo(InfoPartida msgInfo);
}
