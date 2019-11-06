Se trata de diseñar e implementar una API REST y un prototipo funcional de un servicio sencillo para una red social a la que llamaremos UPMSocial. En este servicio los usuarios publican mensajes (estados), y siguen a otros usuarios (amigos) para poder ver los estados de éstos. El servicio debe al menos soportar a través de esa API las siguientes operaciones:

+ Obtener una lista de todos los usuarios de la red
+ Añadir un usuario nuevo
+ Ver los datos básicos de un usuario
+ Publicar un nuevo estado2
+ Eliminar un estado
+ Obtener una lista de todos los estados de un usuarioy filtrar esa lista por fecha o limitar la cantidad de información obtenida por número +de estados (e.g. los 10 primeros elementos, los elementos entre el 11 y el 20, etc.)
+ Obtener el número de estados publicados por mí en la red social en un determinado periodo (fecha inicio y fin) -Buscar posibles amigos en la red por nombre (patrón)
+ Añadir un nuevo amigo
+ Eliminar un amigo
+ Obtener una lista de todos nuestros amigos y filtrar esa lista por nombre o limitar la cantidad de información obtenida por número de amigos (e.g. los 10 primeros amigos, los amigos entre el 11 y el 20, etc.) -Cambiar datos de nuestro perfil (excepto el nombre de usuario)
+ Borrar nuestro perfil de la red social
+ Consultar los últimos estados de nuestros amigos: obtener una lista de los últimos estados de todos mis amigos, pudiendo filtrar estos estados por fecha (último estado antes de cierta fecha). Poder limitar la cantidad de información obtenida por número de estados (e.g. los + 10 primeros elementos, los elementos entre el 11 y el 20, etc.)
+ Buscar en todos los estados de nuestros amigos por contenido –contiene un determinado texto-. Poder limitar la cantidad de información obtenida por número de estados (e.g. los 10 primeros elementos, los elementos entre el 11 y el 20, etc.).
+ Consultar fácilmente la descripción necesaria para una aplicación móvil que queremos realizar, que muestre los datos básicos de un usuario, su último estado, el número de amigos y los estados de sus 10 últimos amigos que se han actualizado
