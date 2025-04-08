# <center>Exercise 2: To Do App</center>


### <center>Franc Tauste González</center>

<hr>

Aplicación para la gestión de tareas por usuario. Incluye aspectos como son el login,
permisos, fragments, conexión con la base de datos, gestión de incidencias con github y trello,
además del uso de controladores y servicios con Spring Boot.

### Links
 - GitHub: https://github.com/FrancTG/p2-todolist-app
 - Docker: https://hub.docker.com/repository/docker/franctg/p2-todolistapp/general
 - Trello: https://trello.com/b/B3sgwNQz/p2-to-do-list-app

En los siguientes puntos se describiran los cambios añadidos durante el proceso del ejercicio.
Estos cambios se han gestionado con Trello usando la metodología de __Kanban__, y en GitHub
se han introducido como incidencias a resolver para poder usar una nueva rama por cambio, que 
posteriormente finalizara con un PullRequest. Estas ramas van ligadas a un nuevo hito para 
desplegar la versión 1.1.0 cuando hayan sido resueltas.

# Menu bar

Menu bar es la barra que aparece en la cabecera de la página, contiene enlaces para acceder a 
las páginas de about y tareas, además de links para la gestión de usuario y cerrar sesión cuando
el usuario ha iniciado sesión o para iniciar sesión y registrarse cuando no lo está.

Esta función está implmentada mediante una nueva página llamada "menu-bar.html" y el uso de fragments con thymeleaf, que nos
facilitarán su implementación en cualquier otra página del proyecto

```html
// Ejemplo de creación
<header th:fragment="menu-bar (id,nombre,userIsLoggedIn)">

// Ejemplo de uso en otra página
<header th:replace="menu-bar :: menu-bar(id = ${user?.id},nombre = ${user?.nombre},userIsLoggedIn=${userIsLoggedIn})"></header>
```

Además, como se puede observar, permite el paso de variables como el id y el nombre de usuario para 
mostrarlos en el menú y saber donde redirigir los links.

```html
<header th:fragment="menu-bar (id,nombre)">
<a class="nav-link" th:href="@{/usuarios/{id}/tareas (id=${id})}">Tasks</a>
```

Estas variables las reciben desde la función del controlador que las ejecuta.

```java
@GetMapping("about")
public String about(Model model) {
    Long userId = managerUserSession.usuarioLogeado();
    UsuarioData user = null;
    boolean userIsLoggedIn = userId != null;
    if (userId != null) {
        user = usuarioService.findById(userId);
    }
    --> model.addAttribute("userIsLoggedIn", userIsLoggedIn);
    --> model.addAttribute("user",user);
    return "about";
}
```

# User List

User List muestra una nueva página con la lista de usuarios registrados. 

Para mostrar esta nueva página, se carga desde el controlador de Home, con un GetMapping
cuando introducimos la URL /registered. La función carga la nueva página y además recibe 
los datos de los usuario registrados desde el servicio de usuario por la función getAllUsers.

```java
//HomeController.java
@GetMapping("registered")
public String registered(Model model) {
    model.addAttribute("users",usuarioService.getAllUsers());
    return "registered";
}

//UsuarioService.java
public Iterable<Usuario> getAllUsers() {
    return usuarioRepository.findAll();
}
```

La nueva página realiza una iteración sobre la lista de datos recibida con el foreach de thymealeaf
que imprime en una tabla.

```html
<tr th:each="user: ${users}">
    <td th:text="${user.id}"></td>
    <td th:text="${user.email}"></td>
</tr>
```

# User description

De forma complementaria a la página de usuarios está la de descripción, que nos permite ver algunos de los datos
adicionales del usuario.

Esta nueva página la desplegamos mediante un link llamado detalles por cada usuario de la lista que debe contener 
en el enlace el id del usuario.

```html
<td><a th:href="@{/registered/{id}(id=${user.id})}">Detalles</a></td>
```

El link necesita una nueva función que la controle, en este caso está junto a la función de la lista de usuario en
el HomeController, que necesita el id del link para solicitar al servicio los datos del usuario para reenviarlos 
a la nueva página.

```java
@GetMapping("/registered/{id}")
public String registeredUserDetails(@PathVariable(value="id") Long idUsuario,Model model) {
    model.addAttribute("user",usuarioService.findById(idUsuario));
    return "registeredUserDetails";
}
```
La nueva página carga los datos en una tabla usando el elemento object de thymeleaf.

```html
<tr th:object="${user}">
    <td th:text="${user.id}"></td>
    <td th:text="${user.email}"></td>
    <td th:text="${user.nombre}"></td>
    <td th:text="${user.fechaNacimiento}"></td>
</tr>
```

# Admin User

El usuario administrador es un usuario que solo puede existir una vez de entre todos los usuarios registrados. 
Al registrarnos podremos encontrar un nuevo checkbox en el caso de que no exista ningún administrador ya registrado,
que nos permite identificar este nuevo usuario como administrador.

Este cambio requiere añadir un nuevo atributo llamado __isAdmin__ en el objeto usuario, de forma que sabemos con true
si es admin o false si no lo es.

```java
private boolean isAdmin;
public boolean getIsAdmin() {
    return isAdmin;
}
public void setAdmin(boolean admin) {
    isAdmin = admin;
}
```

Además de una función que recorre toda la lista de usuarios evaluando el atributo de admin y cuando encuentra que es verdadero 
se detiene y lo devuelve.
```java
public boolean adminUserAlreadyExists() {
    for (Usuario user : usuarioRepository.findAll()) {
        if (user.getIsAdmin()) {
            return true;
        }
    }
    return false;
}
```

Esta función y atributo se usan en la página de registro para decirle al checkbox del formulario si tiene que mostrarse y
que su valor se relaciona con el atributo de isAdmin

```html
<input th:disabled="${adminUserExists}" type="checkbox" id="admin" class="form-control" name="admin" th:checked="*{isAdmin}"/>
```

Y por último, en el controlador del usuario que obtenemos tras el registro, se evalúa cuando es admin para redireccionar a la página de interés.

```java
if (registroData.getIsAdmin()) {
    return "redirect:/registered";
} else {
    return "redirect:/login";
}
```