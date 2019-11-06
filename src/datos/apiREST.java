package datos;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import datos.Factory;

@Path("upmsocial.com")
public class apiREST {

	@Context
	private UriInfo uriInfo;

	private Factory factoria;
	public apiREST() {
		this.factoria = Factory.getInstance();
	}

	@GET
	@Produces({MediaType.TEXT_HTML})
	public String htmlHello() {
		return "<html>" + "<h1>" + "waddup" + "</h1>" + "<html>";
	}

	@GET
	@Path("/users")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getUsers() {
		ArrayList<User> res = Collections.list(factoria.getUsers().elements());
		return Response.status(Response.Status.OK).entity(res).build();		
	
	}
	
	@GET
	@Path("/user/{userID}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getDataUser(@PathParam("userID") String id) {
		int id_num;
		try {
			id_num = Integer.parseInt(id);
		}
		catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("El identificador utilizado ("+id+") no tiene formato de número entero").build();
		}
		if(factoria.getUsers().containsKey(id_num)) {
			return Response.status(Response.Status.OK).entity(factoria.getUser(id_num)).header("Content-Location", uriInfo.getAbsolutePath()).build();
			
		}else {
			return Response.status(Response.Status.NOT_FOUND).entity("No existe ningún usuario en la base de datos con id:"+id).build();	
			}
	}
	
	@DELETE
	@Path("/user/{userID}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response deteleUser(@PathParam("userID") String id) {
		int id_num;
		try {
			id_num = Integer.parseInt(id);
		}
		catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("El identificador utilizado ("+id+") no tiene formato de número entero").build();
		}
		
		if(factoria.getUsers().containsKey(id_num)) {
			factoria.deleteUser(id_num);
			return Response.status(Response.Status.OK).entity(factoria.getUser(id_num)).header("Content-Location", uriInfo.getAbsolutePath()).build();
			
		}else {
			return Response.status(Response.Status.NOT_FOUND).entity("No existe ningún usuario en la base de datos con id:"+id).build();	
			}
	}
	
	@POST
	@Path("/user")
	@Produces({MediaType.APPLICATION_JSON})
	public Response addUser(User usuario) {
		int id =factoria.addUser(usuario);
		return Response.status(Response.Status.CREATED).header("Location", uriInfo.getPath().toString()+"/"+id).build();			
	}
	
	@GET
	@Path("/user/{userID}/status")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getStatus(@PathParam("userID") String id, @QueryParam("desde") @DefaultValue("") String num,@QueryParam("hasta") @DefaultValue("") String nume,
			@QueryParam("fecha") @DefaultValue("") String fecha) {
		int id_num;
		int numberDesde;
		int numberHasta;
		/*Compruebo que los parametros desde y hasta existen. Si no devuelven "" y los valores por defecto seran 0 y 10.*/
		if(num.equals("") && nume.equals("")) {
			numberDesde=0;
			numberHasta=10;			
		}else {//si existen los parametros hago el parseo para sacar sus valores.
			try {
				numberDesde = Integer.parseInt(num);
				numberHasta = Integer.parseInt(nume);				
			} catch (NumberFormatException e) {
				return Response.status(Response.Status.BAD_REQUEST).entity("El identificador utilizado ("+id+") no tiene formato de número entero").build();
			}
		}
		/* Compruebo si existe el parametro fecha. Si no existe devolvera "".*/
		if(!fecha.equals("")) {
			SimpleDateFormat formateo = new SimpleDateFormat("dd-MM-yyyyy");//Compruebo si el formato de la fecha es correcto
			try {
				formateo.parse(fecha);
			}catch(ParseException | java.text.ParseException e){
				return Response.status(Response.Status.BAD_REQUEST).entity("La fecha utilizada no tiene el formato correcto dd/MM/yyyy").build();
			}			
		}
		/*Parsear id usuario*/
		try {
			id_num = Integer.parseInt(id);			
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("El identificador utilizado ("+id+") no tiene formato de número entero").build();
		}
		/*Compruebo que exista el usuario para ese id.*/
		if(factoria.getUsers().containsKey(id_num)) {
				if(!factoria.getStatuses(id_num).isEmpty()) {//Si no esta vacia ---> OK	
					
					if(!fecha.equals("")) {
						ArrayList<Status> res = new ArrayList<Status>();
						for(Status estado: factoria.getStatuses(id_num)) {
							if(estado.getDate().equals(fecha)) {
								res.add(estado);
							}
						}
						int tam = res.size();
						if(tam>numberHasta && tam>=numberDesde ) {							
							List<Status> filtrado = res.subList(numberDesde, numberHasta);
							return Response.status(Response.Status.OK).entity(filtrado).header("Content-Location", uriInfo.getAbsolutePath()).build();
						}else {
							return Response.status(Response.Status.OK).entity(res).header("Content-Location", uriInfo.getAbsolutePath()).build();
						}					
					}//fin !fecha.equals("")
					int tam = factoria.getStatuses(id_num).size();
					if(tam>numberHasta && tam>numberDesde ) {						
						List<Status> res= factoria.getStatuses(id_num).subList(numberDesde, numberHasta);
						return Response.status(Response.Status.OK).entity(res).header("Content-Location", uriInfo.getAbsolutePath()).build();
					}else {
						ArrayList<Status> res = factoria.getStatuses(id_num);
						return Response.status(Response.Status.OK).entity(res).header("Content-Location", uriInfo.getAbsolutePath()).build();
					
					}
				}else {//si esta vacia ---->NOTFOUD
					return Response.status(Response.Status.NOT_FOUND).entity("No existe ningún estado en la base de datos con id:"+id).build();	
				}
			}
		
		else {
		return Response.status(Response.Status.NOT_FOUND).entity("No existe ningún usuario en la base de datos con id:"+id).build();	
		}
	}
	
	@POST
	@Path("/user/{userID}/status")
	@Produces({MediaType.APPLICATION_JSON})
	public Response addStatus(@PathParam("userID") String id,Status estado) {
		int id_num;
		try {
			id_num = Integer.parseInt(id);
		}
		catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("El identificador utilizado ("+id+") no tiene formato de número entero").build();
		}
		User usuario=factoria.getUser(id_num);
		if(usuario!=null) {
		int j=factoria.addStatus(id_num,estado);
		return Response.status(Response.Status.CREATED).header("Location", uriInfo.getPath().toString()+"/"+j).build();	
		}else{
			return Response.status(Response.Status.NOT_FOUND).entity("El usuario con id  "+ id_num+" no existe en la base de datos").build();
		}
	}
	
	@GET
	@Path("/user/{userID}/statusNumber")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getStatusNumber(@PathParam("userID") String id, @QueryParam("fechaInicio") @DefaultValue("") String num,@QueryParam("fechaFin") @DefaultValue("") String nume) {
		int id_num;
		try {
			id_num = Integer.parseInt(id);
		}
		catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("El identificador utilizado ("+id+") no tiene formato de número entero").build();
		}//parsear idNumber
		
		if(factoria.getUsers().isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).entity("No existe ningún usuario en la base de datos con id:"+id).build();
		}else {
		if(!factoria.getStatuses(id_num).isEmpty()) {//Si no esta vacia ---> OK
			int res = factoria.getStatuses(id_num).size();
			return Response.status(Response.Status.OK).entity(res).header("Content-Location", uriInfo.getAbsolutePath()).build();
			
		}else {//si esta vacia ---->NOTFOUD
			return Response.status(Response.Status.NOT_FOUND).entity(0).build();	
			}
		}
	}
	
	@DELETE
	@Path("/user/{userID}/status/{idStatus}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response deteleStatus(@PathParam("userID") String id, @PathParam("idStatus") String idState) {
		int id_num;
		int idStat;
		try {
			id_num = Integer.parseInt(id);
			idStat = Integer.parseInt(idState);
		}
		catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("El identificador utilizado ("+id+") no tiene formato de número entero").build();
		}
		
		if(factoria.getUsers().containsKey(id_num)) {
			factoria.deleteState(id_num,idStat);
			return Response.status(Response.Status.OK).header("Content-Location", uriInfo.getAbsolutePath()).build();
			
		}else {
			return Response.status(Response.Status.NOT_FOUND).entity("No existe ningún estado en la base de datos con id: "+idStat+" para el usuario: "+id).build();	
			}
	}
	
	@GET
	@Path("/user/{userID}/friends")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getFriends(@PathParam("userID") String id,@QueryParam("name") @DefaultValue("") String nombre, @QueryParam("desde") @DefaultValue("") String num,@QueryParam("hasta") @DefaultValue("") String nume) {
		int id_num;
		int numberHasta;
		int numberDesde;
		if(num.equals("") && nume.equals("")) {
			numberDesde=0;
			numberHasta=10;			
		}else {//si existen los parametros hago el parseo para sacar sus valores.
			try {
				numberDesde = Integer.parseInt(num);
				numberHasta = Integer.parseInt(nume);				
			} catch (NumberFormatException e) {
				return Response.status(Response.Status.BAD_REQUEST).entity("El identificador utilizado ("+id+") no tiene formato de número entero").build();
			}
		}
		
		try {
			id_num = Integer.parseInt(id);
		}
		catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("El identificador utilizado ("+id+") no tiene formato de número entero").build();
		}//parsear idNumber
		
		
		if(factoria.getUsers().isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).entity("No existe ningún usuario en la base de datos con id:"+id).build();
		}else {
		if(!factoria.getFriends(id_num).isEmpty()) {//Si no esta vacia ---> OK
			ArrayList<User> res = new ArrayList<User>();
			ArrayList<User> user = factoria.getFriends(id_num);			
			for(User usuario : user) {
				if(usuario.getName().equals(nombre)) {
					res.add(usuario);
				}
			}
			int tam = res.size();
			if(tam>numberHasta && tam>=numberDesde ) {							
				List<User> filtrado = res.subList(numberDesde, numberHasta);
				return Response.status(Response.Status.OK).entity(filtrado).header("Content-Location", uriInfo.getAbsolutePath()).build();
			}else {
				return Response.status(Response.Status.OK).entity(res).header("Content-Location", uriInfo.getAbsolutePath()).build();
			}
		}else {//si esta vacia ---->NOTFOUD
			return Response.status(Response.Status.NOT_FOUND).entity("No hay amigos en la base de datos para este id"+id).build();	
			}
		}
	}
	
	
	@PUT
	@Path("/user/{userID}/friends")
	@Produces({MediaType.APPLICATION_JSON})
	public Response addFriend(@PathParam("userID") String id, Mensaje idFriend) {

		int id_num;
		try {
			id_num = Integer.parseInt(id);
		}
		catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("El identificador utilizado ("+id+") no tiene formato de número entero").build();
		}
		User usuario=factoria.getUser(id_num);
		if(usuario!=null) {
			
			if(factoria.addFriend(id_num, idFriend.getFriendId())){
				return Response.status(Response.Status.CREATED).header("Location", uriInfo.getPath().toString()+"/").build();
			}else {
				return Response.status(Response.Status.NOT_FOUND).entity("El amigo con id  "+ idFriend+" que quieres anadir no existe en la base de datos").build();
			}
		}else{
			return Response.status(Response.Status.NOT_FOUND).entity("El usuario con id  "+ id_num+" no existe en la base de datos").build();
		}
	}
	@DELETE
	@Path("/user/{userID}/friends/{friendID}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response deteleFriend(@PathParam("userID") String id, @PathParam("friendID") String friendId) {
		int id_num;
		int idFriend;
		try {
			id_num = Integer.parseInt(id);
			idFriend=Integer.parseInt(friendId);
			
		}
		catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("El identificador utilizado ("+id+") no tiene formato de número entero").build();
		}
		
		if(factoria.getUsers().containsKey(id_num)) {
			factoria.deleteFriend(id_num,idFriend);
			return Response.status(Response.Status.OK).header("Content-Location", uriInfo.getAbsolutePath()).build();
			
		}else {
			return Response.status(Response.Status.NOT_FOUND).entity("No existe ningún usuario en la base de datos con id:"+id).build();	
			}
	}
	
	@PUT
	@Path("/user/{userID}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response modifyUser(@PathParam("userID") String id,User user) {
		int id_num;
		try {
			id_num = Integer.parseInt(id);
		}
		catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("El identificador utilizado ("+id+") no tiene formato de número entero").build();
		}
		
		User usuario=factoria.getUser(id_num);
		if(usuario!=null) {
			factoria.modifyUser(user,id_num);
			return Response.status(Response.Status.OK).entity(factoria.getUser(id_num)).header("Content-Location", uriInfo.getAbsolutePath()).build();
		}else{
			return Response.status(Response.Status.NOT_FOUND).entity("El usuario con id  "+ id_num+" no existe en la base de datos").build();
		}
	}
	@GET
	@Path("/user/{userID}/findFriends")
	@Produces({MediaType.APPLICATION_JSON})
	public Response findFriends(@PathParam("userID") String id,@QueryParam("nombre") @DefaultValue("") String name) {
		int id_num;
		try {
			id_num = Integer.parseInt(id);
		}
		catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("El identificador utilizado ("+id+") no tiene formato de número entero").build();
		}//parsear idNumber
		
			if(factoria.getUsers().containsKey(id_num)) {
				ArrayList<User> usuarios = Collections.list(factoria.getUsers().elements());
				ArrayList<User> usuarioQuitar = new ArrayList<User>();
				usuarioQuitar.add(factoria.getUser(id_num));
				usuarios.removeAll(usuarioQuitar);
				usuarios.removeAll(factoria.getFriends(id_num));
				
				ArrayList<User> res = new ArrayList<User>();
				for(User nombre : usuarios) {
					if(nombre.getName().equals(name)) {
						res.add(nombre);
					}						
				}
				return Response.status(Response.Status.OK).entity(res).header("Content-Location", uriInfo.getAbsolutePath()).build();
			}else {
				return Response.status(Response.Status.NOT_FOUND).entity("No existe ningún usuario en la base de datos con id:"+id).build();
			}	
	}
	
	@GET
	@Path("/user/{userID}/friends/latestStatus")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getFriendsStatus(@PathParam("userID") String id, @QueryParam("desde") @DefaultValue("") String num,@QueryParam("hasta") @DefaultValue("") String nume,
			@QueryParam("fecha") @DefaultValue("") String fecha) {
		int id_num;
		int numberDesde;
		int numberHasta;
		/*Compruebo que los parametros desde y hasta existen. Si no devuelven "" y los valores por defecto seran 0 y 10.*/
		if(num.equals("") && nume.equals("")) {
			numberDesde=0;
			numberHasta=10;			
		}else {//si existen los parametros hago el parseo para sacar sus valores.
			try {
				numberDesde = Integer.parseInt(num);
				numberHasta = Integer.parseInt(nume);				
			} catch (NumberFormatException e) {
				return Response.status(Response.Status.BAD_REQUEST).entity("El identificador utilizado ("+id+") no tiene formato de número entero").build();
			}
		}
		/* Compruebo si existe el parametro fecha. Si no existe devolvera "".*/
		if(!fecha.equals("")) {
			SimpleDateFormat formateo = new SimpleDateFormat("dd-MM-yyyyy");//Compruebo si el formato de la fecha es correcto
			try {
				formateo.parse(fecha);
			}catch(ParseException | java.text.ParseException e){
				return Response.status(Response.Status.BAD_REQUEST).entity("La fecha utilizada no tiene el formato correcto dd/MM/yyyy").build();
			}			
		}
		/*Parsear id usuario*/
		try {
			id_num = Integer.parseInt(id);			
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("El identificador utilizado ("+id+") no tiene formato de número entero").build();
		}
		/*Compruebo que exista el usuario para ese id.*/
		if(factoria.getUsers().containsKey(id_num)) {
				if(!factoria.getFriends(id_num).isEmpty()) {//Si no esta vacia ---> OK	
					ArrayList<User> amigos= factoria.getFriends(id_num);
					ArrayList<Status> estadosAmigos = new ArrayList<Status>();
					for(User usuario :amigos) {
						estadosAmigos.addAll(factoria.getStatuses(usuario.getId()));
					}
					
					if(!fecha.equals("")) {
						ArrayList<Status> res = new ArrayList<Status>();
						try {
						Date fechaParam = new SimpleDateFormat("dd-MM-yyyy").parse(fecha);
						for(Status estado: estadosAmigos) {
							Date fechaEstado = new SimpleDateFormat("dd-MM-yyyy").parse(estado.getDate());
							if(fechaEstado.before(fechaParam)) {
								res.add(estado);
							}
						}
						}catch(ParseException | java.text.ParseException e){
							return Response.status(Response.Status.BAD_REQUEST).entity("La fecha utilizada no tiene el formato correcto dd/MM/yyyy").build();
						}	
						int tam = res.size();
						if(tam>numberHasta && tam>=numberDesde ) {							
							List<Status> filtrado = res.subList(numberDesde, numberHasta);
							return Response.status(Response.Status.OK).entity(filtrado).header("Content-Location", uriInfo.getAbsolutePath()).build();
						}else {
							return Response.status(Response.Status.OK).entity(res).header("Content-Location", uriInfo.getAbsolutePath()).build();
						}					
					}//fin !fecha.equals("")
					int tam = estadosAmigos.size();
					if(tam>numberHasta && tam>numberDesde ) {						
						List<Status> res= estadosAmigos.subList(numberDesde, numberHasta);
						return Response.status(Response.Status.OK).entity(res).header("Content-Location", uriInfo.getAbsolutePath()).build();
					}else {
						
						return Response.status(Response.Status.OK).entity(estadosAmigos).header("Content-Location", uriInfo.getAbsolutePath()).build();
					
					}
				}else {//si esta vacia ---->NOTFOUD
					return Response.status(Response.Status.NOT_FOUND).entity("No existe ningún amigo en la base de datos para el id:"+id + ":(").build();	
				}
			}
		
		else {
		return Response.status(Response.Status.NOT_FOUND).entity("No existe ningún usuario en la base de datos con id:"+id).build();	
		}
	}
	
	@GET
	@Path("/user/{userID}/friends/status")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getfindFriendsContent(@PathParam("userID") String id, @QueryParam("desde") @DefaultValue("") String num,@QueryParam("hasta") @DefaultValue("") String nume,
			@QueryParam("contenido") @DefaultValue("") String content) {
		int id_num;
		int numberDesde;
		int numberHasta;
		/*Compruebo que los parametros desde y hasta existen. Si no devuelven "" y los valores por defecto seran 0 y 10.*/
		if(num.equals("") && nume.equals("")) {
			numberDesde=0;
			numberHasta=10;			
		}else {//si existen los parametros hago el parseo para sacar sus valores.
			try {
				numberDesde = Integer.parseInt(num);
				numberHasta = Integer.parseInt(nume);				
			} catch (NumberFormatException e) {
				return Response.status(Response.Status.BAD_REQUEST).entity("El identificador utilizado ("+id+") no tiene formato de número entero").build();
			}
		}
		/*Parsear id usuario*/
		try {
			id_num = Integer.parseInt(id);			
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("El identificador utilizado ("+id+") no tiene formato de número entero").build();
		}
		/*Compruebo que exista el usuario para ese id.*/
		if(factoria.getUsers().containsKey(id_num)) {
				if(!factoria.getFriends(id_num).isEmpty()) {//Si no esta vacia ---> OK	
					ArrayList<User> amigos= factoria.getFriends(id_num);
					ArrayList<Status> estadosAmigos = new ArrayList<Status>();
					for(User usuario :amigos) {
						estadosAmigos.addAll(factoria.getStatuses(usuario.getId()));
					}
					
					if(!content.equals("")) {
						ArrayList<Status> res = new ArrayList<Status>();

						for(Status estado: estadosAmigos) {
							if(estado.getText().contains(content)) {
								res.add(estado);
							}
						}						
						int tam = res.size();
						if(tam>numberHasta && tam>=numberDesde ) {							
							List<Status> filtrado = res.subList(numberDesde, numberHasta);
							return Response.status(Response.Status.OK).entity(filtrado).header("Content-Location", uriInfo.getAbsolutePath()).build();
						}else {
							return Response.status(Response.Status.OK).entity(res).header("Content-Location", uriInfo.getAbsolutePath()).build();
						}					
					}//fin !fecha.equals("")
					int tam = estadosAmigos.size();
					if(tam>numberHasta && tam>numberDesde ) {						
						List<Status> res= estadosAmigos.subList(numberDesde, numberHasta);
						return Response.status(Response.Status.OK).entity(res).header("Content-Location", uriInfo.getAbsolutePath()).build();
					}else {
						
						return Response.status(Response.Status.OK).entity(estadosAmigos).header("Content-Location", uriInfo.getAbsolutePath()).build();
					
					}
				}else {//si esta vacia ---->NOTFOUD
					return Response.status(Response.Status.NOT_FOUND).entity("No existe ningún amigo en la base de datos para el id:"+id + ":(").build();	
				}
			}
		
		else {
		return Response.status(Response.Status.NOT_FOUND).entity("No existe ningún usuario en la base de datos con id:"+id).build();	
		}
	}
	@GET
	@Path("/user/{userID}/summary")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSummary(@PathParam("userID") String id) {
		int id_num;
		try {
			id_num = Integer.parseInt(id);
		}
		catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("El identificador utilizado ("+id+") no tiene formato de número entero").build();
		}//parsear idNumber
		if(factoria.getUsers().containsKey(id_num)) {
			User usuario = factoria.getUser(id_num);
			Status estado = factoria.getStatuses(id_num).get(factoria.getStatuses(id_num).size()-1);
			int numAmigos = factoria.getFriends(id_num).size();
			ArrayList<User> amigos= factoria.getFriends(id_num);
			ArrayList<Status> estadosAmigos = new ArrayList<Status>();
			for(User usuarios :amigos) {
					estadosAmigos.addAll(factoria.getStatuses(usuarios.getId()));
			}
			ArrayList<Status> estados10 = new ArrayList<Status>();
			for(int i = 0; i<10 && i<estadosAmigos.size(); i++) {
				estados10.add(estadosAmigos.get(i));
			}
			summary res = new summary(usuario,estado,numAmigos,estados10);
			return Response.status(Response.Status.OK).entity(res).header("Content-Location", uriInfo.getAbsolutePath()).build();			
								
		}else {
			return Response.status(Response.Status.NOT_FOUND).entity("No existe ningún usuario en la base de datos con id:"+id).build();	
		}
	}
	
}
