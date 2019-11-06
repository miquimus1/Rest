package datos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.function.Predicate;

import datos.Status;
import datos.User;


public class Factory {

    private static Factory myInstance;
    public static Factory getInstance() {
        if (myInstance == null)
            myInstance = new Factory();
        return myInstance;
    }
	
    private Hashtable<Integer, User> users;
    private  Hashtable<Integer, ArrayList<Status>> statusList;
    //ArrayList<User> list=new ArrayList<User>();
    private  Hashtable<Integer, ArrayList<User>> friendList;
    private int userID;
    private int statusID;
    
	

    private Factory() {
        this.users= new Hashtable<>();
        this.statusList= new Hashtable<>();
        this.friendList = new Hashtable<>();
        this.userID= 1;
        this.statusID= 1;
        addData();
        
    }
		
	private void addData() {
		addUser(new User("Juanito22","Juan","Garrido Ruiz","juante@hotmail.com"));
		addUser(new User("Regul4r","Javier","Lopez Alonso","javivi@gmail.com"));
		addUser(new User("Watapota","Dani","Color Amarillo","waterpolista@jerez.com"));
		addUser(new User("Jasviers","Javier","Roma Lopez","jasvisers@gmail.com"));
		addUser(new User("ChinoMortal","JJ","Li","correoxino@xina.com"));
		addStatus(1,new Status("Aqui hacemos bunas navajas","13-05-2018"));
		addStatus(1,new Status("Con la calma"));
		addStatus(2,new Status("Hola"));
		addStatus(2,new Status("Estoy fenomenal"));
		addStatus(3,new Status("Que siesta"));
		addStatus(3,new Status("Me voy a dormir"));
		addStatus(4,new Status("Necesito un monster"));
		addStatus(4,new Status("No voy a dormir"));
		addStatus(5,new Status("Quieres bolsa"));
		addStatus(5,new Status("hielo"));
		addFriend(1,3);
		addFriend(1,2);
		addFriend(1,4);
		addFriend(2,4);
		addFriend(3,2);
		
	}
	
	public int addUser(User usuario) {
		usuario.setId(userID);
		users.put(userID, usuario);
		ArrayList<User> amigos = new ArrayList<User>();
		friendList.put(userID, amigos);
		ArrayList<Status> estados = new ArrayList<Status>();
		statusList.put(userID, estados);
        return userID++;
	}
	
	public Hashtable<Integer, User> getUsers(){
		return users;
	}
	
	public User getUser(int id) {
		return users.get(id);
	}
	public void deleteUser(int id) {	
		users.remove(id);
		statusList.remove(id);
		friendList.remove(id);
		
	}
	public void modifyUser(User user, int id) {	
		User modificar = users.get(id);
		modificar.setEmail(user.getEmail());
		modificar.setName(user.getName());
		modificar.setSurname(user.getSurname());
		users.put(id, modificar);
		
	}
	
	public int addStatus(int usuarioID, Status estado) {
		estado.setUserID(usuarioID);
		estado.setId(statusID);
		ArrayList<Status> listaEstados = statusList.get(usuarioID);
		listaEstados.add(estado);
		statusList.put(usuarioID, listaEstados);
		return statusID++;
	}
	
	public ArrayList<Status> getStatuses(int usuarioID){
		return statusList.get(usuarioID);
	}
	public void deleteState(int id,int idState){
		ArrayList<Status> lista=statusList.get(id);
		Predicate<Status> res= p->p.getId()==idState;
		lista.removeIf(res);
		
	}
	
	public boolean addFriend(int  usuarioID, int amigoID) {
		if(users.containsKey(amigoID)){//existe usuario amigo
			User amigo = this.getUser(amigoID);
			ArrayList<User> listaAmigos = friendList.get(usuarioID);
			if(!listaAmigos.contains(amigo)) {
				listaAmigos.add(amigo);
				friendList.put(usuarioID, listaAmigos);
				addFriend(amigoID, usuarioID);
			}
			return true;
		}
		return false;
	}
	
	public ArrayList<User> getFriends(int user){
		return friendList.get(user);
	}
	
	public ArrayList<User> getFriendsName(String name,int user){
		ArrayList<User> listaAmigos = friendList.get(user);
		ArrayList<User> resultado = new ArrayList<User>();
		for(User usuario: listaAmigos) {
			if(usuario.getName().equals(name)) {
				resultado.add(usuario);
			}
		}
		return resultado;
	}
	
	
	
	public void deleteFriend(int id,int idFriend){
		ArrayList<User> lista=friendList.get(id);
		Predicate<User> res= p->p.getId()==idFriend;
		lista.removeIf(res);
	}
	
}
