IF OBJECT_ID(N'dbo.AuditoriaRol', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.AuditoriaRol (
        AuditoriaRolID INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_AuditoriaRol PRIMARY KEY,
        AdministradorID INT NOT NULL,
        UsuarioAfectadoID INT NOT NULL,
        RolAnterior NVARCHAR(20) NOT NULL,
        RolNuevo NVARCHAR(20) NOT NULL,
        Fecha DATETIME2 NOT NULL CONSTRAINT DF_AuditoriaRol_Fecha DEFAULT SYSUTCDATETIME(),
        Resultado NVARCHAR(30) NOT NULL,
        CONSTRAINT FK_AuditoriaRol_Administrador FOREIGN KEY (AdministradorID) REFERENCES dbo.Usuario(UsuarioID),
        CONSTRAINT FK_AuditoriaRol_Usuario FOREIGN KEY (UsuarioAfectadoID) REFERENCES dbo.Usuario(UsuarioID)
    );
    CREATE INDEX IX_AuditoriaRol_Fecha ON dbo.AuditoriaRol(Fecha DESC);
END;
