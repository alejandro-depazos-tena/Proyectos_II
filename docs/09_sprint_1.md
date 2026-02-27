# 5. Sprint 1 — Estado de avance

## 5.1 INCORPORACIONES

Durante el Sprint 1 se han incorporado los elementos base del producto para disponer de una primera versión funcional (MVP técnico):

- **Estructura de proyecto fullstack** separada en `backend` (Spring Boot + Maven) y `frontend` (Astro).
- **Modelo de datos inicial** implementado en SQL y reflejado en entidades JPA:
  - `usuario`
  - `producto`
  - `solicitud`
  - `transaccion`
  - `foto_producto`
  - `reporte_usuario`
  - `reporte_producto`
- **Autenticación básica** con endpoints de registro e inicio de sesión:
  - `POST /api/auth/register`
  - `POST /api/auth/login`
- **Gestión de sesión básica** mediante token de sesión para identificar al usuario autenticado.
- **Módulo de perfil (`/api/me`)** para consultar datos del usuario autenticado y gestionar sus productos iniciales:
  - `GET /api/me`
  - `GET /api/me/productos`
  - `POST /api/me/productos`
- **CRUDs de base del dominio** en backend para usuarios, productos, solicitudes, transacciones y reportes.
- **Interfaz inicial en frontend** con páginas de autenticación y perfil:
  - Registro/login (`/auth`, `/login`)
  - Perfil y publicación básica de productos (`/profile`)
- **Carga de datos semilla para desarrollo** en entorno local (H2) para facilitar pruebas funcionales rápidas.

> Resultado del Sprint 1: plataforma con base técnica operativa, autenticación funcional y primer flujo end-to-end de usuario autenticado + gestión inicial de productos.

## 5.2 METODOLOGIA DE TRABAJO

La metodología aplicada en el Sprint 1 se ha basado en **SCRUM + Git Flow + Azure DevOps**:

- **Planificación por sprints** con backlog priorizado por historias de usuario.
- **Gestión del trabajo en Azure DevOps Boards**, con trazabilidad de tareas, responsables y estimaciones.
- **Flujo Git Flow**:
  - Rama estable: `main`
  - Rama de integración: `develop`
  - Ramas de trabajo: `feature/*`, `bugfix/*`, `hotfix/*`
- **Trabajo con Pull Requests** para revisión de código antes de integrar cambios.
- **Validación continua** con ejecución local de pruebas y pipeline de integración para asegurar compilación y calidad mínima.
- **Seguimiento de avance** mediante estados de tarea y actualización de horas/estado durante el sprint.

Este enfoque ha permitido mantener control del alcance del Sprint 1, coordinación del equipo y una base sólida para los sprints siguientes.

## 5.3 PLANTILLA

Para la definición del backlog se ha utilizado una **plantilla estándar de Historia de Usuario** orientada a valor de negocio y verificabilidad:

### Estructura de Historia de Usuario

- **Formato narrativo**:
  - Como `[tipo de usuario]`
  - Quiero `[acción]`
  - Para `[beneficio]`
- **Metadatos mínimos**:
  - ID (`HU-XXX`)
  - Tipo de Work Item (`User Story`)
  - Épica
  - Sprint
  - Prioridad
  - Story Points
  - Responsable
  - Tags
- **Criterios de aceptación** en formato verificable (estilo Gherkin).
- **Subtareas técnicas** asociadas (`T-XXX`) para backend, frontend, testing y calidad.
- **Dependencias** (qué bloquea y de qué depende).
- **Definición de Done** (código, testing, validación funcional e integración).

### Plantilla base utilizada

```text
ID: HU-XXX
Título: [Historia orientada a valor]

Como [rol]
Quiero [necesidad]
Para [beneficio]

Criterios de aceptación:
- Escenario 1...
- Escenario 2...

Tareas técnicas hijas:
- T-XXX ...
- T-YYY ...

Dependencias:
- ...

Definition of Done:
- Código implementado
- Pruebas ejecutadas
- PR aprobado
```

## 5.4 DIAGRAMAS

Como parte del Sprint 1, se han consolidado los diagramas de soporte técnico para alinear diseño, implementación y base de datos.

### 5.4.1 Diagrama MER

```mermaid
erDiagram
    USUARIO ||--o{ PRODUCTO : publica
    PRODUCTO ||--o{ SOLICITUD : recibe
    USUARIO ||--o{ SOLICITUD : solicita
    SOLICITUD ||--|| TRANSACCION : genera
    PRODUCTO ||--o{ FOTO_PRODUCTO : tiene
    USUARIO ||--o{ REPORTE_USUARIO : reporta
    USUARIO ||--o{ REPORTE_USUARIO : es_reportado
    USUARIO ||--o{ REPORTE_PRODUCTO : reporta
    PRODUCTO ||--o{ REPORTE_PRODUCTO : es_reportado

    USUARIO {
        int id_usuario PK
        string nombre
        string apellidos
        string correo UK
        string telefono UK
        string dni UK
        string password
    }

    PRODUCTO {
        int id_producto PK
        string titulo
        string descripcion
        string categoria
        string tipo_transaccion
        string estado_producto
        float precio
        int id_propietario FK
    }

    SOLICITUD {
        int id_solicitud PK
        int id_producto FK
        int id_solicitante FK
        string tipo_transaccion
        date fecha_solicitud
        date fecha_inicio
        date fecha_fin
        string estado_solicitud
    }

    TRANSACCION {
        int id_transaccion PK
        int id_solicitud FK
        date fecha_creacion
        date fecha_inicio_real
        date fecha_fin_real
        string estado_transaccion
    }

    FOTO_PRODUCTO {
        int id_foto PK
        int id_producto FK
        string url_foto
        boolean es_principal
    }

    REPORTE_USUARIO {
        int id_reporte PK
        int id_usuario_reportante FK
        int id_usuario_reportado FK
        string motivo
        string comentario
        date fecha_reporte
        string estado_reporte
    }

    REPORTE_PRODUCTO {
        int id_reporte PK
        int id_usuario_reportante FK
        int id_producto_reportado FK
        string motivo
        string comentario
        date fecha_reporte
        string estado_reporte
    }
```

Este diagrama MER representa la estructura lógica de datos del Sprint 1. Identifica las entidades principales del dominio (usuarios, productos, solicitudes, transacciones, fotos y reportes), sus atributos clave y las cardinalidades entre ellas. Su función es asegurar la coherencia entre requisitos funcionales y persistencia, definiendo con claridad qué información se almacena, cómo se relaciona y qué restricciones de integridad deben cumplirse.

> Nota: en el modelo SQL real existen restricciones de unicidad adicionales (correo, teléfono, dni y `id_solicitud` en transacción).

### 5.4.2 Diagrama de clases

```mermaid
classDiagram
    direction LR

    class Usuario {
        +Long idUsuario
        +String nombre
        +String apellidos
        +String correo
        +String telefono
        +String dni
        +String passwordHash
    }

    class Producto {
        +Long idProducto
        +String titulo
        +String descripcion
        +CategoriaProducto categoria
        +TipoTransaccion tipoTransaccion
        +EstadoProducto estadoProducto
        +BigDecimal precio
        +Long idPropietario
    }

    class Solicitud {
        +Long idSolicitud
        +Long idProducto
        +Long idSolicitante
        +TipoTransaccion tipoTransaccion
        +LocalDateTime fechaSolicitud
        +LocalDateTime fechaInicio
        +LocalDateTime fechaFin
        +EstadoSolicitud estadoSolicitud
    }

    class Transaccion {
        +Long idTransaccion
        +Long idSolicitud
        +LocalDateTime fechaCreacion
        +LocalDateTime fechaInicioReal
        +LocalDateTime fechaFinReal
        +EstadoTransaccion estadoTransaccion
    }

    class FotoProducto {
        +Long idFoto
        +Long idProducto
        +String urlFoto
        +Boolean esPrincipal
    }

    class ReporteUsuario {
        +Long idReporte
        +Long idUsuarioReportante
        +Long idUsuarioReportado
        +String motivo
        +String comentario
        +LocalDateTime fechaReporte
        +EstadoReporte estadoReporte
    }

    class ReporteProducto {
        +Long idReporte
        +Long idUsuarioReportante
        +Long idProductoReportado
        +String motivo
        +String comentario
        +LocalDateTime fechaReporte
        +EstadoReporte estadoReporte
    }

    Usuario "1" --> "N" Producto : publica
    Producto "1" --> "N" Solicitud : recibe
    Usuario "1" --> "N" Solicitud : solicita
    Solicitud "1" --> "1" Transaccion : genera
    Producto "1" --> "N" FotoProducto : tiene
    Usuario "1" --> "N" ReporteUsuario : reporta
    Usuario "1" --> "N" ReporteUsuario : es_reportado
    Usuario "1" --> "N" ReporteProducto : reporta
    Producto "1" --> "N" ReporteProducto : es_reportado
```

Este diagrama de clases muestra la vista orientada a objetos del modelo implementado en backend. Refleja las clases de dominio y sus atributos según las entidades JPA, así como las asociaciones funcionales entre ellas. Se utiliza para mantener la trazabilidad diseño-código, comprobando que la estructura del software respeta el modelo de negocio definido en análisis y en base de datos.

### 5.4.3 Diagrama de componentes

```mermaid
flowchart LR
    U[Usuario UFV] --> FE[Frontend Astro]
    FE --> API[API REST Spring Boot]

    subgraph Backend
        API --> AUTH[AuthController/AuthService]
        API --> ME[MeController]
        API --> PROD[ProductoController/Service]
        API --> SOL[SolicitudController/Service]
        API --> TRA[TransaccionController/Service]
        API --> REP[ReporteController/Service]
        API --> AUD[Servicio de Trazabilidad]
    end

    AUTH --> SES[SessionRepository]
    AUTH --> UREP[(UsuarioRepository)]
    ME --> UREP
    PROD --> PREP[(ProductoRepository)]
    SOL --> SREP[(SolicitudRepository)]
    TRA --> TREP[(TransaccionRepository)]
    REP --> RUREP[(ReporteUsuarioRepository)]
    REP --> RPREP[(ReporteProductoRepository)]

    UREP --> DB[(MySQL/H2)]
    PREP --> DB
    SREP --> DB
    TREP --> DB
    RUREP --> DB
    RPREP --> DB
    AUD --> ADB[(Tabla AuditoriaEventos)]

    TRA -. notificaciones .-> NOTI[Email/Push]
```

Este diagrama de componentes describe la arquitectura funcional del sistema y la responsabilidad de cada bloque. Se observa el flujo principal desde el usuario (frontend Astro) hasta la API Spring Boot, la descomposición en controladores/servicios y la capa de repositorios conectada a la base de datos. También incorpora el componente de trazabilidad para registrar eventos operativos, lo que facilita auditoría técnica y seguimiento de incidencias.

### 5.4.4 Diagrama de estados (trazabilidad de acciones)

```mermaid
stateDiagram-v2
    [*] --> Publicado: crear_publicacion
    Publicado --> SolicitudPendiente: solicitar(alquiler/compra)
    SolicitudPendiente --> SolicitudRechazada: rechazar
    SolicitudPendiente --> SolicitudAceptada: aceptar
    SolicitudPendiente --> Cancelada: cancelar_solicitud

    SolicitudAceptada --> QuedadaPendiente: proponer_quedada_entrega
    QuedadaPendiente --> PagoPendiente: confirmar_quedada
    QuedadaPendiente --> Cancelada: no_show/cancelar

    PagoPendiente --> Pagado: registrar_pago
    PagoPendiente --> Cancelada: pago_fallido/cancelar

    Pagado --> Entregado: confirmar_entrega

    Entregado --> EnAlquiler: tipo=ALQUILER
    Entregado --> CompraCerrada: tipo=COMPRA

    EnAlquiler --> DevolucionPendiente: solicitar_devolucion
    DevolucionPendiente --> Devuelto: confirmar_devolucion
    Devuelto --> Cerrada

    CompraCerrada --> Cerrada
    SolicitudRechazada --> [*]
    Cancelada --> [*]
    Cerrada --> [*]

    note right of SolicitudPendiente
        Trazabilidad:
        - evento: SOLICITUD_CREADA
        - actor, fecha, idSolicitud
    end note

    note right of PagoPendiente
        Trazabilidad:
        - evento: PAGO_INICIADO/PAGO_OK/PAGO_ERROR
        - actor, fecha, idTransaccion
    end note

    note right of Devuelto
        Trazabilidad:
        - evento: DEVOLUCION_CONFIRMADA
        - actor, fecha, idTransaccion
    end note
```

Este diagrama de estados modela el ciclo de vida de una operación iniciada por el usuario, desde la publicación y solicitud hasta su cierre por compra o devolución. Define transiciones explícitas para aceptar/rechazar, quedar para entrega, confirmar entrega y finalizar la transacción, incluyendo cancelaciones. Como apoyo a trazabilidad, cada transición relevante se asocia a eventos registrables (actor, fecha e identificador de solicitud/transacción), permitiendo reconstruir qué ocurrió, cuándo y por quién.

### 5.4.5 Diagrama de secuencia (flujo principal de operación)

```mermaid
sequenceDiagram
    autonumber
    actor U as Usuario
    participant FE as Frontend (Astro)
    participant API as Backend API (Spring Boot)
    participant AUTH as AuthService
    participant SOL as SolicitudService
    participant TRA as TransaccionService
    participant DB as BD (MySQL/H2)
    participant AUD as ServicioTrazabilidad

    U->>FE: Inicia sesión (email/password)
    FE->>API: POST /api/auth/login
    API->>AUTH: validarCredenciales()
    AUTH->>DB: consulta usuario + validación hash
    DB-->>AUTH: credenciales válidas
    AUTH-->>API: token de sesión
    API-->>FE: 200 OK + token

    U->>FE: Solicita producto (alquiler/compra)
    FE->>API: POST /api/solicitudes
    API->>SOL: crearSolicitud()
    SOL->>DB: INSERT solicitud (PENDIENTE)
    DB-->>SOL: idSolicitud
    SOL->>AUD: registrarEvento(SOLICITUD_CREADA)
    SOL-->>API: solicitud creada
    API-->>FE: 201 Created

    U->>FE: Propietario acepta solicitud
    FE->>API: PATCH /api/solicitudes/{id}/aceptar
    API->>SOL: aceptarSolicitud(id)
    SOL->>DB: UPDATE solicitud = ACEPTADA
    SOL->>TRA: crearTransaccionDesdeSolicitud()
    TRA->>DB: INSERT transaccion (EN_CURSO)
    DB-->>TRA: idTransaccion
    TRA->>AUD: registrarEvento(TRANSACCION_CREADA)
    TRA-->>API: transacción creada
    API-->>FE: 200 OK

    U->>FE: Confirmar entrega
    FE->>API: POST /api/transacciones/{id}/entrega
    API->>TRA: confirmarEntrega()
    TRA->>DB: UPDATE estado y fechas
    TRA->>AUD: registrarEvento(ENTREGA_CONFIRMADA)
    TRA-->>API: entrega confirmada
    API-->>FE: 200 OK

    alt Tipo = ALQUILER/PRESTAMO
        U->>FE: Confirmar devolución
        FE->>API: POST /api/transacciones/{id}/devolucion
        API->>TRA: confirmarDevolucion()
        TRA->>DB: UPDATE transacción = COMPLETADA
        TRA->>DB: UPDATE producto = DISPONIBLE
        TRA->>AUD: registrarEvento(DEVOLUCION_CONFIRMADA)
        API-->>FE: 200 OK
    else Tipo = COMPRA
        API->>TRA: cerrarCompra()
        TRA->>DB: UPDATE transacción = COMPLETADA
        TRA->>DB: UPDATE producto = NO_DISPONIBLE
        TRA->>AUD: registrarEvento(COMPRA_CERRADA)
        API-->>FE: 200 OK
    end
```

Este diagrama de secuencia representa, en orden temporal, la interacción entre actor, interfaz, servicios de negocio y base de datos durante el flujo principal de una operación en UFV Share. El diagrama permite verificar la trazabilidad entre acciones del usuario y efectos técnicos (creación/actualización de solicitud y transacción), incorporando además el registro de eventos de auditoría en cada hito relevante. De este modo, se documenta no solo qué funcionalidad se ejecuta, sino también cómo se propaga internamente cada acción hasta su cierre por compra o devolución.

---

### Nota de alcance del Sprint 1

Este documento refleja el avance técnico ya implementado y validado en repositorio al cierre del Sprint 1. Los módulos avanzados previstos para siguientes sprints (mensajería, historial ampliado, refinamientos UX y hardening de seguridad) quedan fuera de este apartado.
