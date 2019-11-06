package datos;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "summary")
public class summary {
	private User usuario;
	private Status ultimoEstado;
	private int numAmigos;
	private  ArrayList<Status> estados;
	
	public User getUsuario() {
		return usuario;
	}
	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}
	public Status getUltimoEstado() {
		return ultimoEstado;
	}
	public void setUltimoEstado(Status ultimoEstado) {
		this.ultimoEstado = ultimoEstado;
	}
	public int getNumAmigos() {
		return numAmigos;
	}
	public void setNumAmigos(int numAmigos) {
		this.numAmigos = numAmigos;
	}
	public ArrayList<Status> getEstados() {
		return estados;
	}
	public void setEstados(ArrayList<Status> estados) {
		this.estados = estados;
	}
	public summary(User usuario, Status state, int numAmigos, ArrayList<Status> estados) {
		this.usuario=usuario;		
		this.numAmigos=numAmigos;
		this.ultimoEstado=state;
		this.estados=estados;
	}
	public summary() {
		
	}
	
	
	
}
