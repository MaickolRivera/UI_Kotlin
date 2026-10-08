using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Metadata;
using UsuariosApi.Data;

#nullable disable

namespace UsuariosApi.Migrations;

[DbContext(typeof(ApplicationDbContext))]
public partial class ApplicationDbContextModelSnapshot : ModelSnapshot
{
    protected override void BuildModel(ModelBuilder modelBuilder)
    {
        modelBuilder
            .HasAnnotation("ProductVersion", "8.0.20")
            .HasAnnotation("Relational:MaxIdentifierLength", 64);

        MySqlModelBuilderExtensions.AutoIncrementColumns(modelBuilder);

        modelBuilder.Entity("UsuariosApi.Entities.Usuario", b =>
        {
            b.Property<int>("Id")
                .ValueGeneratedOnAdd()
                .HasColumnType("int");
            MySqlPropertyBuilderExtensions.UseMySqlIdentityColumn(b.Property<int>("Id"));

            b.Property<bool>("Activo").HasColumnType("tinyint(1)");

            b.Property<string>("Apellido")
                .IsRequired()
                .HasMaxLength(100)
                .HasColumnType("varchar(100)");

            b.Property<string>("Correo")
                .IsRequired()
                .HasMaxLength(150)
                .HasColumnType("varchar(150)");

            b.Property<string>("Edad")
                .IsRequired()
                .HasColumnType("longtext");

            b.Property<DateTime>("FechaCreacion").HasColumnType("datetime(6)");
            b.Property<DateTime>("FechaNacimiento").HasColumnType("datetime(6)");

            b.Property<string>("Nombre")
                .IsRequired()
                .HasMaxLength(100)
                .HasColumnType("varchar(100)");

            b.Property<string>("PasswordHash")
                .IsRequired()
                .HasColumnType("longtext");

            b.Property<int>("Semestre").HasColumnType("int");

            b.Property<string>("Universidad")
                .IsRequired()
                .HasColumnType("longtext");

            b.HasKey("Id");
            b.HasIndex("Correo").IsUnique();
            b.ToTable("Usuarios", (string)null);
        });
    }
}
