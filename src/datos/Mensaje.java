package datos;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;


public class Mensaje {
	@XmlElement(name = "idFriend")
	public int friendId;
	
	public Mensaje(int friendId) {
		this.friendId=friendId;
	}
	public Mensaje() {		
	}
	public int getFriendId() {
		return this.friendId;
	}
	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}
	
}
