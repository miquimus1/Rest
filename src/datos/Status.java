package datos;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.glass.ui.Pixels.Format;

import java.text.SimpleDateFormat;
import java.util.Calendar;



public class Status {
	private int id;
	private int userID;
	private String text;
	private String date;
	
	
	public Status( String text, String date) {
		this.text=text;
		this.date=date;
	}
	public Status( String text) {
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		this.text=text;
		this.date=formato.format(Calendar.getInstance().getTime());
	}	
	public Status() {
		
	}	
	
	public void setUserID(int usuarioID) {
		this.userID=usuarioID;
	}
	
	public int getId() {
		return this.id;
	}
	private int getUserID() {
		return userID;
	}
	public void setId(int id) {
	        this.id = id;
	}
	
	public String getText() {
		return this.text;
	}
	public void setText(String text) {
	    this.text = text;
	}
	
	public String getDate(){
		return this.date;
	}
	public void setDate(String date ) {
        this.date = date;
	}

}
