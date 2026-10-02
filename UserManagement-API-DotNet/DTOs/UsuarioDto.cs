namespace UsuariosApi.DTOs;

public class UsuarioDto
{
    public int Id { get; set; }

    public string Nombre { get; set; } = string.Empty;

    public string Apellido { get; set; } = string.Empty;

    public string Correo { get; set; } = string.Empty;

    public int Edad { get; set; }

    public bool Activo { get; set; }

    public DateTime FechaCreacion { get; set; }
}