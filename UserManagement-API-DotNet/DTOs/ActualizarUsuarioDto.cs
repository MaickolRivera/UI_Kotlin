using System.ComponentModel.DataAnnotations;

namespace UsuariosApi.DTOs;

public class ActualizarUsuarioDto
{
    [Required]
    [MaxLength(100)]
    public string Nombre { get; set; } = string.Empty;

    [Required]
    [MaxLength(100)]
    public string Apellido { get; set; } = string.Empty;

    [Required]
    [EmailAddress]
    [MaxLength(150)]
    public string Correo { get; set; } = string.Empty;

    [Range(1, 120)]
    public int Edad { get; set; }

    public bool Activo { get; set; }
}