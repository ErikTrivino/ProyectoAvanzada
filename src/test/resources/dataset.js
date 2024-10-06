
db = connect( 'mongodb://localhost:27017/proyecto' );
// Datos de Carrito
db.carrito.insertMany([
    {
        "fecha": ISODate("2023-10-01T10:00:00Z"),
        "items": [
            { "productoId": "abc123", "cantidad": 2, "precioUnitario": 15000 },
            { "productoId": "def456", "cantidad": 1, "precioUnitario": 30000 }
        ],
        "id": "carrito001",
        "idUsuario": ObjectId("651f3a9d6f1b2b3c4f5d1b1c")
    },
    {
        "fecha": ISODate("2023-10-02T11:00:00Z"),
        "items": [
            { "productoId": "ghi789", "cantidad": 3, "precioUnitario": 10000 }
        ],
        "id": "carrito002",
        "idUsuario": ObjectId("651f3a9d6f1b2b3c4f5d1b1c")
    },
    {
        "fecha": ISODate("2023-10-03T12:30:00Z"),
        "items": [
            { "productoId": "xyz111", "cantidad": 1, "precioUnitario": 50000 }
        ],
        "id": "carrito003",
        "idUsuario": ObjectId("651f3a9d6f1b2b3c4f5d1b2c")
    },
    {
        "fecha": ISODate("2023-10-04T13:00:00Z"),
        "items": [
            { "productoId": "opq222", "cantidad": 5, "precioUnitario": 8000 }
        ],
        "id": "carrito004",
        "idUsuario": ObjectId("651f3a9d6f1b2b3c4f5d1b3c")
    },
    {
        "fecha": ISODate("2023-10-05T14:30:00Z"),
        "items": [
            { "productoId": "rst333", "cantidad": 4, "precioUnitario": 12000 }
        ],
        "id": "carrito005",
        "idUsuario": ObjectId("651f3a9d6f1b2b3c4f5d1b4c")
    }
]);

// Datos de Cuenta
db.cuenta.insertMany([
    {
        "_id": "cuenta001",
        "email": "usuario1@example.com",
        "password": "password123",
        "rol": "ADMIN",
        "fechaRegistro": ISODate("2023-09-10T08:30:00Z"),
        "usuario": {
            "id": "usuario001",
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
        "codigoValidacionPassword": null
    },
    {
        "_id": "cuenta002",
        "email": "usuario2@example.com",
        "password": "password1234",
        "rol": "USUARIO",
        "fechaRegistro": ISODate("2023-09-11T09:00:00Z"),
        "usuario": {
            "id": "usuario002",
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
        "codigoValidacionPassword": null
    },
    {
        "_id": "cuenta003",
        "email": "usuario3@example.com",
        "password": "password12345",
        "rol": "ADMIN",
        "fechaRegistro": ISODate("2023-09-12T10:00:00Z"),
        "usuario": {
            "id": "usuario003",
            "telefono": "345678901",
            "direccion": "Calle 789",
            "cedula": "876543210",
            "nombre": "Carlos Lopez"
        },
        "estado": "ACTIVO",
        "codigoValidacionRegistro": null,
        "codigoValidacionPassword": null
    },
    {
        "_id": "cuenta004",
        "email": "usuario4@example.com",
        "password": "password123456",
        "rol": "USUARIO",
        "fechaRegistro": ISODate("2023-09-13T11:30:00Z"),
        "usuario": {
            "id": "usuario004",
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
        }
    },
    {
        "_id": "cuenta005",
        "email": "usuario5@example.com",
        "password": "password654321",
        "rol": "USUARIO",
        "fechaRegistro": ISODate("2023-09-14T12:00:00Z"),
        "usuario": {
            "id": "usuario005",
            "telefono": "789012345",
            "direccion": "Calle 654",
            "cedula": "654321987",
            "nombre": "Laura Rodriguez"
        },
        "estado": "INACTIVO",
        "codigoValidacionRegistro": null,
        "codigoValidacionPassword": null
    }
]);

// Datos de Cupon
db.cupon.insertMany([
    {
        _id: ObjectId("652c95c6f0b56723d4638921"),
        descuento: 20.0,
        fechaVencimiento: ISODate("2024-12-31T23:59:00Z"),
        codigo: "CUPON2024",
        estado: "ACTIVO",
        tipo: "UNICO",
        nombre: "Descuento del 20%",
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Cupon'
    },
    {
        _id: ObjectId("652c95c6f0b56723d4638922"),
        descuento: 15.0,
        fechaVencimiento: ISODate("2024-11-30T23:59:00Z"),
        codigo: "CUPON2023",
        estado: "ACTIVO",
        tipo: "MULTIPLE",
        nombre: "Descuento del 15%",
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Cupon'

    },
    {
        _id: ObjectId("652c95c6f0b56723d4638923"),
        descuento: 10.0,
        fechaVencimiento: ISODate("2024-10-31T23:59:00Z"),
        codigo: "CUPON2022",
        estado: "EXPIRADO",
        tipo: "UNICO",
        nombre: "Descuento del 10%",
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Cupon'
    },
    {
        _id: ObjectId("652c95c6f0b56723d4638924"),
        descuento: 5.0,
        fechaVencimiento: ISODate("2024-09-30T23:59:00Z"),
        codigo: "CUPON2021",
        estado: "ACTIVO",
        tipo: "MULTIPLE",
        nombre: "Descuento del 5%",
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Cupon'
    },
    {
        _id: ObjectId("652c95c6f0b56723d4638925"),
        descuento: 25.0,
        fechaVencimiento: ISODate("2024-08-31T23:59:00Z"),
        codigo: "CUPON2020",
        estado: "REDIMIDO",
        tipo: "UNICO",
        nombre: "Descuento del 25%",
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Cupon'
    },

]);


// Datos de Evento
db.evento.insertMany([
    {
        "_id": "evento001",
        "imagenPortada": "portada_evento1.jpg",
        "estado": "ACTIVO",
        "nombre": "Concierto Banda XYZ",
        "descripcion": "Un concierto inolvidable con Banda XYZ",
        "imagenLocalidades": "localidades_evento1.jpg",
        "tipo": "CONCIERTO",
        "fechaEvento": ISODate("2023-11-15T19:00:00Z"),
        "ciudad": "Bogotá",
        "localidades": [
            { "nombre": "General", "precio": 50000, "disponibilidad": 100 },
            { "nombre": "VIP", "precio": 100000, "disponibilidad": 50 }
        ]
    },
    {
        "_id": "evento002",
        "imagenPortada": "portada_evento2.jpg",
        "estado": "ACTIVO",
        "nombre": "Festival de Jazz",
        "descripcion": "Disfruta del mejor jazz en vivo",
        "imagenLocalidades": "localidades_evento2.jpg",
        "tipo": "FESTIVAL",
        "fechaEvento": ISODate("2023-12-20T18:00:00Z"),
        "ciudad": "Medellín",
        "localidades": [
            { "nombre": "General", "precio": 40000, "disponibilidad": 150 },
            { "nombre": "VIP", "precio": 80000, "disponibilidad": 60 }
        ]
    },
    {
        "_id": "evento003",
        "imagenPortada": "portada_evento3.jpg",
        "estado": "INACTIVO",
        "nombre": "Teatro Clásico",
        "descripcion": "Una obra clásica que te emocionará",
        "imagenLocalidades": "localidades_evento3.jpg",
        "tipo": "TEATRO",
        "fechaEvento": ISODate("2023-10-05T17:00:00Z"),
        "ciudad": "Cali",
        "localidades": [
            { "nombre": "Platea", "precio": 60000, "disponibilidad": 80 },
            { "nombre": "Balcón", "precio": 40000, "disponibilidad": 50 }
        ]
    },
    {
        "_id": "evento004",
        "imagenPortada": "portada_evento4.jpg",
        "estado": "ACTIVO",
        "nombre": "Feria de Ciencia",
        "descripcion": "Exposición de los mejores proyectos científicos",
        "imagenLocalidades": "localidades_evento4.jpg",
        "tipo": "FERIA",
        "fechaEvento": ISODate("2023-11-30T09:00:00Z"),
        "ciudad": "Barranquilla",
        "localidades": [
            { "nombre": "General", "precio": 20000, "disponibilidad": 300 },
            { "nombre": "Estudiantes", "precio": 10000, "disponibilidad": 200 }
        ]
    },
    {
        "_id": "evento005",
        "imagenPortada": "portada_evento5.jpg",
        "estado": "INACTIVO",
        "nombre": "Exposición de Arte",
        "descripcion": "Una colección impresionante de arte moderno",
        "imagenLocalidades": "localidades_evento5.jpg",
        "tipo": "EXPOSICION",
        "fechaEvento": ISODate("2024-01-10T10:00:00Z"),
        "ciudad": "Cartagena",
        "localidades": [
            { "nombre": "General", "precio": 70000, "disponibilidad": 120 },
            { "nombre": "VIP", "precio": 150000, "disponibilidad": 30 }
        ]
    }
]);

// Datos de Orden
db.orden.insertMany([
    {
        _id: ObjectId("6701fcf0bdb88d4aa14f6547"),
        idCliente: ObjectId("652c95c6f0b56723d4638910"),
        fecha: ISODate("2024-01-01T15:30:00Z"),
        codigoPasarela: "CODIGO_PASARELA_123",
        items:  [
            {
                "id": "item001",
                "idEvento": "evento001",
                "precio": 20000.0,
                "nombreLocalidad": "VIP",
                "cantidad": 2
            },
            {
                "id": "item002",
                "idEvento": "evento002",
                "precio": 15000.0,
                "nombreLocalidad": "General",
                "cantidad": 1
            }
        ],
        pago: {
            moneda: "USD",
            tipoPago: "CREDIT_CARD",
            detalleEstado: "Pago exitoso",
            codigoAutorizacion: "AUTH123",
            fecha: ISODate("2024-01-01T15:35:00Z"),
            id: ObjectId(),
            valorTransaccion: 350.0,
            estado: "COMPLETADO"
        },
        total: 350.0,
        idCupon: ObjectId("652c95c6f0b56723d4638921")
        ,
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Orden'
    },
    {
        _id: ObjectId("6701fd0d44bf8e22611ddeaa"),
        idCliente: ObjectId("652c95c6f0b56723d4638911"),
        fecha: ISODate("2024-02-15T10:45:00Z"),
        codigoPasarela: "CODIGO_PASARELA_456",
        items:  [
            {
                "id": "item001",
                "idEvento": "evento001",
                "precio": 20000.0,
                "nombreLocalidad": "VIP",
                "cantidad": 2
            },
            {
                "id": "item002",
                "idEvento": "evento002",
                "precio": 15000.0,
                "nombreLocalidad": "General",
                "cantidad": 1
            }
        ],
        pago: {
            moneda: "COP",
            tipoPago: "DEBIT_CARD",
            detalleEstado: "Pago pendiente",
            codigoAutorizacion: "AUTH456",
            fecha: ISODate("2024-02-15T10:50:00Z"),
            id: ObjectId(),
            valorTransaccion: 550.0,
            estado: "PENDIENTE"
        },
        total: 550.0,
        idCupon: ObjectId("652c95c6f0b56723d4638922"),
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Orden'
    },
    {
        _id: ObjectId("6701fd148a8526257d0ed1f5"),
        idCliente: ObjectId("652c95c6f0b56723d4638912"),
        fecha: ISODate("2024-03-20T18:00:00Z"),
        codigoPasarela: "CODIGO_PASARELA_789",
        items:  [
            {
                "id": "item001",
                "idEvento": "evento001",
                "precio": 20000.0,
                "nombreLocalidad": "VIP",
                "cantidad": 2
            },
            {
                "id": "item002",
                "idEvento": "evento002",
                "precio": 15000.0,
                "nombreLocalidad": "General",
                "cantidad": 1
            }
        ],
        pago: {
            moneda: "USD",
            tipoPago: "PAYPAL",
            detalleEstado: "Pago rechazado",
            codigoAutorizacion: "AUTH789",
            fecha: ISODate("2024-03-20T18:05:00Z"),
            id: ObjectId(),
            valorTransaccion: 600.0,
            estado: "RECHAZADO"
        },
        total: 600.0,
        idCupon: ObjectId("652c95c6f0b56723d4638923"),
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Orden'
    },
    {
        _id: ObjectId("6701fd1b9b809cfdda0a5c4f"),
        idCliente: ObjectId("652c95c6f0b56723d4638913"),
        fecha: ISODate("2024-04-05T12:00:00Z"),
        codigoPasarela: "CODIGO_PASARELA_101",
        items:  [
            {
                "id": "item001",
                "idEvento": "evento001",
                "precio": 20000.0,
                "nombreLocalidad": "VIP",
                "cantidad": 2
            },

        ],
        pago: {
            moneda: "EUR",
            tipoPago: "BANK_TRANSFER",
            detalleEstado: "Pago exitoso",
            codigoAutorizacion: "AUTH101",
            fecha: ISODate("2024-04-05T12:05:00Z"),
            id: ObjectId(),
            valorTransaccion: 100.0,
            estado: "COMPLETADO"
        },
        total: 100.0,
        idCupon: ObjectId("652c95c6f0b56723d4638924"),
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Orden'
    },
    {
        _id: ObjectId("6701fd25aba7f616b42cbfb1"),
        idCliente: ObjectId("652c95c6f0b56723d4638914"),
        fecha: ISODate("2024-05-10T08:00:00Z"),
        codigoPasarela: "CODIGO_PASARELA_202",
        items:  [
            {
                "id": "item001",
                "idEvento": "evento001",
                "precio": 20000.0,
                "nombreLocalidad": "VIP",
                "cantidad": 2
            },
            {
                "id": "item002",
                "idEvento": "evento002",
                "precio": 15000.0,
                "nombreLocalidad": "General",
                "cantidad": 1
            }
        ],
        pago: {
            moneda: "COP",
            tipoPago: "CASH",
            detalleEstado: "Pago exitoso",
            codigoAutorizacion: "AUTH202",
            fecha: ISODate("2024-05-10T08:05:00Z"),
            id: ObjectId(),
            valorTransaccion: 540.0,
            estado: "COMPLETADO"
        },
        total: 540.0,
        idCupon: ObjectId("652c95c6f0b56723d4638925"),
        _class: 'co.edu.uniquindio.proyecto.modelo.documentos.Orden'
    }
]);


// Datos de Usuario
db.usuario.insertMany([
    {
        "_id": "usuario001",
        "telefono": "123456789",
        "direccion": "Calle 123",
        "cedula": "987654321",
        "nombre": "Juan Perez"
    },
    {
        "_id": "usuario002",
        "telefono": "987654321",
        "direccion": "Calle 456",
        "cedula": "123456789",
        "nombre": "Ana Gomez"
    },
    {
        "_id": "usuario003",
        "telefono": "345678901",
        "direccion": "Calle 789",
        "cedula": "876543210",
        "nombre": "Carlos Lopez"
    },
    {
        "_id": "usuario004",
        "telefono": "567890123",
        "direccion": "Calle 321",
        "cedula": "543216789",
        "nombre": "Marta Silva"
    },
    {
        "_id": "usuario005",
        "telefono": "789012345",
        "direccion": "Calle 654",
        "cedula": "654321987",
        "nombre": "Laura Rodriguez"
    }
]);
