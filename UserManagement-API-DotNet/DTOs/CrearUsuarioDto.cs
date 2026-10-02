using System.ComponentModel.DataAnnotations;

namespace UsuariosApi.DTOs;

public class CrearUsuarioDto
{
    [Required, MaxLength(100)]
    public string Nombre { get; set; } = string.Empty;

    [Required, MaxLength(100)]
    public string Apellido { get; set; } = string.Empty;

    [Required, EmailAddress, MaxLength(150)]
    public string Correo { get; set; } = string.Empty;

    [Required, MinLength(6)]
    public string Password { get; set; } = string.Empty;

    [Range(1, 120)]
    public int Edad { get; set; }

    [Range(1, int.MaxValue)]
    public int RolId { get; set; }
}