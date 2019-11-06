package datos;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "user")
public class User {
	private  int id;
	private String nick;
	private String name;
	private String surname;
	private String email;

	public User(String nick,String name, String surname, String email) {
		this.nick=nick;
		this.name = name;
		this.surname=surname;
		this.email=email;
	}
	
	public User() {
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
	        this.id = id;
    }
	
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
        this.nick = nick;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
        this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
        this.surname = surname;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
        this.email = email;
	}
	
}



