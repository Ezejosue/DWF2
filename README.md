# DWF2 - Sistema de Gestión de cuentas bancarias 🤑

¡Bienvenido al proyecto **DWF2**! Este es un sistema de gestión de cuentas bancarias desarrollado en **Java 17** utilizando **Spring Boot**, **Jersey**, **JPA**, y **Spring Security**. La aplicación permite a los usuarios registrarse, iniciar sesión, y acceder a funcionalidades basadas en roles. 📋

## 📂 Estructura del Proyecto

La estructura de carpetas del proyecto es la siguiente:

```
DWF2/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/
│   │   │       └── fase2/
│   │   │           └── dwf2/
│   │   │               ├── controller/
│   │   │               │   └── UserController.java
│   │   │               ├── dto/
│   │   │               ├── service/
│   │   │               └── util/
│   │   │                   └── JerseyConfig.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
├── pom.xml
└── README.md
```

### Descripción de Directorios 📁

- **controller/**: Contiene los controladores REST, como `UserController.java`, que maneja las solicitudes relacionadas con usuarios (registro, login, etc.).
- **dto/**: Contiene los **Data Transfer Objects** (DTOs) utilizados para transferir datos entre las capas de la aplicación.
- **service/**: Contiene la lógica de negocio de la aplicación.
- **util/**: Contiene configuraciones y utilidades, como `JerseyConfig.java`, que se encarga de la configuración del framework Jersey para la aplicación.
- **resources/**: Contiene archivos de configuración y recursos, como `application.properties`.

## 🚀 Funcionalidades Principales

- **Registro de Usuarios** 📝: Los usuarios pueden registrarse proporcionando información básica como nombre, email, y contraseña.
- **Login** 🔑: Los usuarios pueden autenticarse utilizando email y contraseña.
- **Gestión Basada en Roles** 🛡️: Los usuarios tienen roles (como `CLIENT`, `ADMIN`), y las funcionalidades están restringidas según estos roles.

## ⚙️ Tecnologías Utilizadas

- **Java 17** ☕
- **Spring Boot 3** 🌱
- **Jersey** para crear APIs REST 🛠️
- **Spring Security** para autenticación y autorización 🔒
- **JPA / Hibernate** para el acceso a la base de datos 📊
- **MySQL** como base de datos relacional 🗄️

## 📦 Instalación y Configuración

1. **Clonar el Repositorio**:
   ```sh
   git clone https://github.com/tu-usuario/DWF2.git
   ```
2. **Configurar el archivo `application.properties`**: 
   Asegúrate de configurar correctamente las propiedades de la base de datos en `src/main/resources/application.properties`.

3. **Compilar y Ejecutar el Proyecto**:
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```

## 📝 Endpoints Principales

- **POST** `/api/users/register` - Registro de nuevos usuarios.
- **POST** `/api/users/login` - Inicio de sesión.
- **GET** `/api/users` - Obtener todos los usuarios (restringido a roles específicos).

## 🧪 Pruebas

Las pruebas se encuentran en el directorio `src/test/java/`. Para ejecutarlas, puedes usar:

```sh
mvn test
```

## 📄 Dependencias Clave

Algunas de las dependencias utilizadas en el proyecto (definidas en `pom.xml`) son:

- **spring-boot-starter-data-jpa**: Para la interacción con la base de datos.
- **spring-boot-starter-jersey**: Para la creación de APIs RESTful.
- **spring-boot-starter-security**: Para la autenticación y autorización.
- **lombok**: Para reducir el código boilerplate (getters, setters, etc.).

## 📜 Licencia

Este proyecto está bajo la Licencia MIT. Para más detalles, consulta el archivo `LICENSE`.

---

¡Gracias por tu interés en DWF2! 😊 No dudes en contribuir, enviar tus sugerencias, o abrir un issue si encuentras algún problema.

**¡Happy Code!** 🚀✨
