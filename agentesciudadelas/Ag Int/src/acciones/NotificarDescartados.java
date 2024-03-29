package acciones;

import jade.content.AgentAction;
import jade.util.leap.ArrayList;
import conceptos.Personaje;

public class NotificarDescartados implements AgentAction{
	
	private Personaje [] destapados;

	public void setDestapados(jade.util.leap.List l){
		destapados = new Personaje[l.size()];
		for (int i = 0; i < destapados.length; i++) {
			destapados[i] = (Personaje)l.get(i);
		}
	}

	public void setDestapados(Personaje[] d){
		destapados = d;
	}

	public jade.util.leap.List getDestapados(){
		jade.util.leap.List al = new ArrayList(destapados.length);
		for (int i = 0; i < destapados.length; i++) {
			al.add(i, destapados[i]);
		}
		return al;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NotificarDescartados: ");
		for (int i = 0; i < destapados.length; i++) {
			sb.append(destapados[i].getNombre());
			sb.append(" ");
		}
		return sb.toString();
	}
}
