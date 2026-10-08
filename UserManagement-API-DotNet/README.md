# 📱 User Management Mobile App

Aplicación móvil desarrollada con Kotlin para Android que consume una
API REST desarrollada con ASP.NET Core y utiliza MySQL como sistema
de persistencia.

## 🚀 Tecnologías

### Mobile
- Kotlin
- Android Studio
- Retrofit
- Gson
- XML Layouts

### Backend
- C#
- ASP.NET Core Web API
- Entity Framework Core
- BCrypt
- Repository Pattern

### Database
- MySQL

## 🏗️ Arquitectura

Android App
    ↓
Retrofit
    ↓
ASP.NET Core REST API
    ↓
Controller
    ↓
Service
    ↓
Repository
    ↓
Entity Framework Core
    ↓
MySQL

## 🔐 Autenticación

Las contraseñas no se almacenan en texto plano.

Durante el registro:

Password → BCrypt Hash → MySQL

Durante el login:

Password → BCrypt Verify → PasswordHash

## 📌 Funcionalidades

- Registro de usuarios
- Inicio de sesión
- Validación de credenciales
- Hash seguro de contraseñas
- Consulta de usuarios
- Actualización de usuarios
- Eliminación de usuarios
- Manejo de roles
- Consumo de API REST desde Android

## 🔗 Endpoints

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /api/Usuarios | Consultar usuarios |
| GET | /api/Usuarios/{id} | Consultar usuario |
| POST | /api/Usuarios | Crear usuario |
| POST | /api/Usuarios/login | Iniciar sesión |
| PUT | /api/Usuarios/{id} | Actualizar usuario |
| DELETE | /api/Usuarios/{id} | Eliminar usuario |
## Crear la base de datos MySQL

La migración inicial incluida en `Migrations` crea la tabla `Usuarios`
con todos los campos de la entidad y un índice único para `Correo`.

1. Inicia MySQL y configura `ConnectionStrings:MySql` en `appsettings.json`
   con el nombre de la base de datos, usuario y contraseña reales.
   El usuario necesita permisos para crear la base de datos y sus tablas.
2. Si no tienes la herramienta de EF Core 8, instálala:

   ```powershell
   dotnet tool install --global dotnet-ef --version 8.0.20
   ```

3. Desde la carpeta del proyecto, aplica la migración:

   ```powershell
   dotnet ef database update
   ```

Este último comando compila el proyecto y crea la base de datos si no existe.
Si ya cuentas con una compilación actualizada que incluye la migración, puedes
evitar compilar usando `dotnet ef database update --no-build`.

No necesitas ejecutar `dotnet ef migrations add InitialCreate`: la migración
y el snapshot ya están incluidos. No se aplica automáticamente al iniciar la API.
