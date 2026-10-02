namespace UsuariosApi.Entities;

public class Usuario
{
    public int Id { get; set; }

    public string Nombre { get; set; } = string.Empty;

    public string Apellido { get; set; } = string.Empty;

    public string Correo { get; set; } = string.Empty;

    // La contraseña NO se guarda en texto plano
    public string PasswordHash { get; set; } = string.Empty;

    public int Edad { get; set; }

    public bool Activo { get; set; } = true;

    public int RolId { get; set; }

    public DateTime FechaCreacion { get; set; } = DateTime.Now;
}