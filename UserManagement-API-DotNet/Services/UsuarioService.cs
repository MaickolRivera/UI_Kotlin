using UsuariosApi.DTOs;
using UsuariosApi.Entities;
using UsuariosApi.Interfaces;

namespace UsuariosApi.Services;

public class UsuarioService : IUsuarioService
{
    private readonly IUsuarioRepository _repo;

    public UsuarioService(IUsuarioRepository repo)
    {
        _repo = repo;
    }

    // ==========================================
    // OBTENER TODOS
    // ==========================================
    public async Task<List<UsuarioDto>> ObtenerTodosAsync()
    {
        var usuarios = await _repo.ObtenerTodosAsync();

        return usuarios
            .Select(Map)
            .ToList();
    }

    // ==========================================
    // OBTENER POR ID
    // ==========================================
    public async Task<UsuarioDto?> ObtenerPorIdAsync(int id)
    {
        var usuario = await _repo.ObtenerPorIdAsync(id);

        return usuario is null
            ? null
            : Map(usuario);
    }

    // ==========================================
    // CREAR USUARIO
    // ==========================================
    public async Task<UsuarioDto> CrearAsync(CrearUsuarioDto usuarioDto)
    {
        var correo = usuarioDto.Correo
            .Trim()
            .ToLowerInvariant();

        // Validar que el correo no esté registrado
        if (await _repo.ObtenerPorCorreoAsync(correo) is not null)
        {
            throw new InvalidOperationException(
                "Ya existe un usuario con ese correo."
            );
        }

        // Convertir contraseña en HASH
        var passwordHash =
            BCrypt.Net.BCrypt.HashPassword(usuarioDto.Password);

        var usuario = new Usuario
        {
            Nombre = usuarioDto.Nombre.Trim(),
            Apellido = usuarioDto.Apellido.Trim(),
            Correo = correo,
            PasswordHash = passwordHash,
            FechaNacimiento = usuarioDto.FechaNacimiento,
            Universidad = usuarioDto.Universidad,
            Semestre = usuarioDto.Semestre
        };

        await _repo.CrearAsync(usuario);

        return Map(usuario);
    }

    // ==========================================
    // LOGIN
    // ==========================================
    public async Task<UsuarioDto?> LoginAsync(LoginDto dto)
    {
        var correo = dto.Correo
            .Trim()
            .ToLowerInvariant();

        // Buscar usuario por correo
        var usuario =
            await _repo.ObtenerPorCorreoAsync(correo);

        // Usuario no encontrado
        if (usuario is null)
        {
            return null;
        }

        // Validar que esté activo
        if (!usuario.Activo)
        {
            return null;
        }

        // Validar que tenga contraseña registrada
        if (string.IsNullOrWhiteSpace(usuario.PasswordHash))
        {
            return null;
        }

        // Comparar la contraseña ingresada
        // con el HASH almacenado en la BD
        var passwordCorrecto =
            BCrypt.Net.BCrypt.Verify(
                dto.Password,
                usuario.PasswordHash
            );

        // Contraseña incorrecta
        if (!passwordCorrecto)
        {
            return null;
        }

        // LOGIN CORRECTO
        return Map(usuario);
    }

    // ==========================================
    // ACTUALIZAR
    // ==========================================
    public async Task<bool> ActualizarAsync(
        int id,
        ActualizarUsuarioDto actualizarUsuarioDto)
    {
        var usuario =
            await _repo.ObtenerPorIdAsync(id);

        if (usuario is null)
        {
            return false;
        }

        var correo = actualizarUsuarioDto.Correo
            .Trim()
            .ToLowerInvariant();

        var otro =
            await _repo.ObtenerPorCorreoAsync(correo);

        if (otro is not null &&
            otro.Id != id)
        {
            throw new InvalidOperationException(
                "El correo pertenece a otro usuario."
            );
        }

        usuario.Nombre = actualizarUsuarioDto.Nombre.Trim();
        usuario.Apellido = actualizarUsuarioDto.Apellido.Trim();
        usuario.Correo = correo;
        usuario.FechaNacimiento = actualizarUsuarioDto.FechaNacimiento;
        usuario.Activo = actualizarUsuarioDto.Activo;
        usuario.Universidad = actualizarUsuarioDto.Universidad;
        usuario.Semestre = actualizarUsuarioDto.Semestre;

        await _repo.ActualizarAsync(usuario);

        return true;
    }

    // ==========================================
    // ELIMINAR
    // ==========================================
    public async Task<bool> EliminarAsync(int id)
    {
        var usuario =
            await _repo.ObtenerPorIdAsync(id);

        if (usuario is null)
        {
            return false;
        }

        await _repo.EliminarAsync(usuario);

        return true;
    }

    // ==========================================
    // MAPEO ENTITY -> DTO
    // ==========================================
    private static UsuarioDto Map(Usuario usuario)
    {
        return new UsuarioDto
        {
            Id = usuario.Id,
            Nombre = usuario.Nombre,
            Apellido = usuario.Apellido,
            Correo = usuario.Correo,
            FechaNacimiento = usuario.FechaNacimiento,
            Activo = usuario.Activo,
            Universidad = usuario.Universidad,
            Semestre = usuario.Semestre,
            FechaCreacion = usuario.FechaCreacion
        };
    }
}