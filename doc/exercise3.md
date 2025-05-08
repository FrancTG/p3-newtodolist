# <center>Exercise 3: New To Do App</center>


### <center>Franc Tauste González</center>

<hr>
En este ejercicio seguimos a partir del trabajo anterior, en el que teníamos una aplicación 
de gestionar tareas por usuario y le añadimos nuevas funcionalidades usando los proyectos 
y incidencias de GitHub, para ahora continuar el desarrollo pero usando las acciones también de GitHub.
Además, estaremos desarrollando con el sistema TDD (Test Driven Design), de forma que se realizarán
pruebas unitarias de las funcionalidades a implementar antes de programar el código para la aplicación.



### Links
 - GitHub: https://github.com/FrancTG/p3-newtodolist
 - Docker: https://hub.docker.com/repository/docker/franctg/p3-newtodolist/general
 - Trello: https://trello.com/b/andhkI9h/p3-to-do-list-new-app

# 009 Gestión de afiliación en los equipos

Este apartado consiste en añadir una nueva funcionalidad en la aplicación en la que debemos permitir 
que el usuario tenga la posibilidad de ver y crear equipos al los que poder unirse o salir cuando 
prefiera. Por ello tenemos un total de 6 test implementados (en Github aparecen a partir del 10, es decir,
los tests del 11 al 16) con las acciones de GitHub que dividimos en 2 apartados, uno donde implementamos 
en el servicio las funcionalidades que vamos usar en el apartado y otro en el que implementamos la vista,
el controlador y los tests que se requieran.

## Servicios y tests

Como en el apartado anterior 008 ya se implementaron las funciones de crear equipos y añadir usuario, 
ahora tan solo nos quedaría implementar la de __borrar a los usuario__ de un equipo y comprobar con los test 
que todo está correcto. De esta forma añadimos una nueva función en __EquipoService__ llamada 
__borrarUsuarioDelEquipo__ y usamos la misma estructura que la función ya existente de __añadirUsuarioAEquipo__
pero cambiando el momento en el que se añade el usuario al equipo por la función de borrado.

```java
public void añadirUsuarioAEquipo(Long idEquipo, Long idUsuario) {
    ...
    equipo.addUsuario(usuario);
    ...
}

public void borrarUsuarioDelEquipo(Long idEquipo, Long idUsuario) {
    ...
    equipo.removeUsuario(usuario);
    ...
}
```

La función para borrar al usuario del equipo completa los servicios que necesitamos, por lo que pasamos a 
probar que todo esté correcto con los tests. En concreto son 2 los que realizamos:

### Test 11: Usuario se borra del equipo

Este test consiste en crear un equipo y dos usuarios, que añadimos dentro del equipo, para luego borrar  
uno de ambos y comprobar que solamente queda el otro y que es el que queremos.

### Test 12: Usuario se borra del equipo excepciones

En complemento al anterior test nos encontramos con este, en el que comprobamos las excepciones que
pueden saltar al realizar la funciones que tenemos previstas usar durante el borrado del usuario en 
un equipo. Los tipos de excepciones que comprobamos son:
* Ver los usuario de un equipo que no contiene ningún usuario
* Ver los equipo de un usuario que no tiene ningún equipo
* Borrar un usuario de un equipo al que no pertence 
* Borrar de un equipo un usuario que no existe
* Borrar un usuario de un equipo que no existe

## Vista y controlador

Teniendo el servicio ya preparado y testeado procedemos a implementar el apartado visual que hace uso
de estas funcionalidades.

### Test 13: Mostrar equipo

En este test implementamos la vista de __listaEquipos__ que muestra una tabla con todos los equipos 
disponibles. Para hacer el test añadimos un nuevo equipo llamado _Ejemplo 1_, para luego acceder a la
ruta correspondiente y comprobar que en el contenido aparece el equipo _Ejemplo1_.

### Test 14: Crear nuevos equipo

Tras añadir la posibilidad de ver los equipos, queremos que los usuario puedan añadir nuevos por ellos mismos, 
así que realizamos una nueva vista __formNuevoEquipo__ que permite rellenar un formulario con el que
añadir el nuevo equipo. Con el test comprobamos que al añadir un nuevo equipo aparece en la vista de equipos.

### Test 15: Vista de usuarios en el equipo

En este test se añade una nueva vista llamada __listarUsuarioEquipo__ para mostrar los usuarios pertenecientes a 
dicho equipo y un nuevo botón en la tabla de los equipos para acceder a esta vista. En el test se añade un usuario
a un equipo y se comprueba que al ejecutar la nueva vista aparecen sus datos.

### Test 16: Salir y unirse a un equipo

Y, en este último test, se añade a la vista de __listarUsuarioEquipo__ dos botones para que __el usuario pueda salir o 
entrar al equipo__, que aparecerán si este ya se encuentra dentro o no. Para hacer el test se crea un equipo y dos
usuarios, estos dos se añaden al equipo mediante el controlador y se comprueba si existe el primero, luego se elimina
el primer usuario del equipo y se comprueba que efectivamente solo queda el segundo.

## Base de datos 

![Base de datos](https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fdrive.google.com/uc?id=1ccwpipN1_oZz8nNpzLtsrOLD65Ms35BZ)
