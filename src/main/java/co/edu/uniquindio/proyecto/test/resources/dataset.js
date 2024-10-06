// Datos de Cuenta
db.cuenta.insertMany([
    {
        "_id": ObjectId("6701ec61eb812956e91267d7") ,
        "email": "usuario1@example.com",
        "password": "password123",
        "rol": "ADMIN",
        "fechaRegistro": ISODate("2023-09-10T08:30:00Z"),
        "usuario": {
            "id": "6701ecb5522faa848df6771c",
            "telefono": "123456789",
            "direccion": "Calle 123",
            "cedula": "987654321",
            "nombre": "Juan Perez"
        },
        "estado": "ACTIVO",
        "codigoValidacionRegistro": {
            "codigo": "VAL001",
            "fechaCreacion": ISODate("2023-09-10T08:35:00Z"),
            "fechaExpiracion": ISODate("2023-09-11T08:35:00Z")
        },
        "codigoValidacionPassword": null,
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Cuenta'
    },
    {
        "_id": ObjectId("6701ed1fa81f609e1a5692fb"),
        "email": "usuario2@example.com",
        "password": "password1234",
        "rol": "USUARIO",
        "fechaRegistro": ISODate("2023-09-11T09:00:00Z"),
        "usuario": {
            "id": "6701ed2718dc56dfc847f9c7",
            "telefono": "987654321",
            "direccion": "Calle 456",
            "cedula": "123456789",
            "nombre": "Ana Gomez"
        },
        "estado": "INACTIVO",
        "codigoValidacionRegistro": {
            "codigo": "VAL002",
            "fechaCreacion": ISODate("2023-09-11T09:05:00Z"),
            "fechaExpiracion": ISODate("2023-09-12T09:05:00Z")
        },
        "codigoValidacionPassword": null,
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Cuenta'
    },
    {
        "_id": ObjectId("6701ed2718dc56dfc847f9c7"),
        "email": "usuario3@example.com",
        "password": "password12345",
        "rol": "ADMIN",
        "fechaRegistro": ISODate("2023-09-12T10:00:00Z"),
        "usuario": {
            "id": "6701ed5940fbd4b4320644f1",
            "telefono": "345678901",
            "direccion": "Calle 789",
            "cedula": "876543210",
            "nombre": "Carlos Lopez"
        },
        "estado": "ACTIVO",
        "codigoValidacionRegistro": null,
        "codigoValidacionPassword": null,
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Cuenta'
    },
    {
        "_id": ObjectId("6701edd7e490d979c91ccac0"),
        "email": "usuario4@example.com",
        "password": "password123456",
        "rol": "USUARIO",
        "fechaRegistro": ISODate("2023-09-13T11:30:00Z"),
        "usuario": {
            "id": "6701ede133cf27e08dd1e854",
            "telefono": "567890123",
            "direccion": "Calle 321",
            "cedula": "543216789",
            "nombre": "Marta Silva"
        },
        "estado": "ACTIVO",
        "codigoValidacionRegistro": null,
        "codigoValidacionPassword": {
            "codigo": "VAL003",
            "fechaCreacion": ISODate("2023-09-13T11:35:00Z"),
            "fechaExpiracion": ISODate("2023-09-14T11:35:00Z")
        },
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Cuenta'
    },
    {
        "_id": ObjectId("6701ee143c2efcda35bec4e6"),
        "email": "usuario5@example.com",
        "password": "password654321",
        "rol": "USUARIO",
        "fechaRegistro": ISODate("2023-09-14T12:00:00Z"),
        "usuario": {
            "id": "6701ee143c2efcda35bec4e6",
            "telefono": "789012345",
            "direccion": "Calle 654",
            "cedula": "654321987",
            "nombre": "Laura Rodriguez"
        },
        "estado": "INACTIVO",
        "codigoValidacionRegistro": null,
        "codigoValidacionPassword": null,
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Cuenta'
    }
]);


// Datos de Evento
db.evento.insertMany([
    {
        "_id": ObjectId("6701eea02f877bfc0e9397cf"),
        "imagenPortada": "portada_evento1.jpg",
        "estado": "ACTIVO",
        "nombre": "Concierto Banda XYZ",
        "descripcion": "Un concierto inolvidable con Banda XYZ",
        "imagenLocalidades": "localidades_evento1.jpg",
        "tipo": "CONCIERTO",
        "fechaEvento": ISODate("2023-11-15T19:00:00Z"),
        "ciudad": "Bogotá",
        "localidades": [
            { "nombre": "General", "precio": 50000, "capacidadMaxima": 100 },
            { "nombre": "VIP", "precio": 100000, "capacidadMaxima": 50 }
        ],
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Evento'
    },
    {
        "_id": ObjectId("6701eea60f15f5a0bd60922f"),
        "imagenPortada": "portada_evento2.jpg",
        "estado": "ACTIVO",
        "nombre": "Festival de Jazz",
        "descripcion": "Disfruta del mejor jazz en vivo",
        "imagenLocalidades": "localidades_evento2.jpg",
        "tipo": "FESTIVAL",
        "fechaEvento": ISODate("2023-12-20T18:00:00Z"),
        "ciudad": "Medellín",
        "localidades": [
            { "nombre": "General", "precio": 40000, "capacidadMaxima": 150, "entradasVendidas": 0 },
            { "nombre": "VIP", "precio": 80000, "capacidadMaxima": 60, "entradasVendidas": 0 }
        ],
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Evento'
    },
    {
        "_id": ObjectId("6701eeb14d32c9a9e276392f"),
        "imagenPortada": "portada_evento3.jpg",
        "estado": "INACTIVO",
        "nombre": "Teatro Clásico",
        "descripcion": "Una obra clásica que te emocionará",
        "imagenLocalidades": "localidades_evento3.jpg",
        "tipo": "TEATRO",
        "fechaEvento": ISODate("2023-10-05T17:00:00Z"),
        "ciudad": "Cali",
        "localidades": [
            { "nombre": "Platea", "precio": 60000, "capacidadMaxima": 80, "entradasVendidas": 20 },
            { "nombre": "Balcón", "precio": 40000, "capacidadMaxima": 50, "entradasVendidas": 8 }
        ]
    },
    {
        "_id": ObjectId("6701eeb9fbc0310ac379122a"),
        "imagenPortada": "portada_evento4.jpg",
        "estado": "ACTIVO",
        "nombre": "Feria de Ciencia",
        "descripcion": "Exposición de los mejores proyectos científicos",
        "imagenLocalidades": "localidades_evento4.jpg",
        "tipo": "FERIA",
        "fechaEvento": ISODate("2023-11-30T09:00:00Z"),
        "ciudad": "Barranquilla",
        "localidades": [
            { "nombre": "General", "precio": 20000, "capacidadMaxima": 300, "entradasVendidas": 0 },
            { "nombre": "Estudiantes", "precio": 10000, "capacidadMaxima": 200, "entradasVendidas": 0 }
        ],
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Evento'
    },
    {
        "_id": ObjectId("6701eec35da1f43a2940c085"),
        "imagenPortada": "portada_evento5.jpg",
        "estado": "INACTIVO",
        "nombre": "Exposición de Arte",
        "descripcion": "Una colección impresionante de arte moderno",
        "imagenLocalidades": "localidades_evento5.jpg",
        "tipo": "EXPOSICION",
        "fechaEvento": ISODate("2024-01-10T10:00:00Z"),
        "ciudad": "Cartagena",
        "localidades": [
            { "nombre": "General", "precio": 70000, "capacidadMaxima": 120, "entradasVendidas": 0 },
            { "nombre": "VIP", "precio": 150000, "capacidadMaxima": 30, "entradasVendidas": 0 }
        ],
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Evento'
    }
]);



// Datos de Usuario
db.usuario.insertMany([
    {
        "_id": ObjectId("6701ecb5522faa848df6771c"),
        "telefono": "123456789",
        "direccion": "Calle 123",
        "cedula": "987654321",
        "nombre": "Juan Perez",
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Usuario'
    },
    {
        "_id": ObjectId("6701ed2718dc56dfc847f9c7"),
        "telefono": "987654321",
        "direccion": "Calle 456",
        "cedula": "123456789",
        "nombre": "Ana Gomez",
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Usuario'
    },
    {
        "_id": ObjectId("6701ed5940fbd4b4320644f1"),
        "telefono": "345678901",
        "direccion": "Calle 789",
        "cedula": "876543210",
        "nombre": "Carlos Lopez",
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Usuario'
    },
    {
        "_id": ObjectId("6701ede133cf27e08dd1e854"),
        "telefono": "567890123",
        "direccion": "Calle 321",
        "cedula": "543216789",
        "nombre": "Marta Silva",
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Usuario'
    },
    {
        "_id": ObjectId("6701ee143c2efcda35bec4e6"),
        "telefono": "789012345",
        "direccion": "Calle 654",
        "cedula": "654321987",
        "nombre": "Laura Rodriguez",
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Usuario'
    }
]);