# Ejemplo Completo: Historia de Usuario en Azure DevOps

## 🎯 HU-001: Registro de usuario con correo universitario

---

### 📋 Información General

| Campo | Valor |
|-------|-------|
| **ID** | HU-001 |
| **Work Item Type** | User Story |
| **Estado** | New → Active → Testing → Done |
| **Asignado a** | Gonzalo de Lorenzo Vaquero |
| **Epic** | Autenticación y Gestión de Usuarios |
| **Sprint** | Sprint 1 |
| **Prioridad** | 1 - Must-be (Crítico) |
| **Story Points** | 8 |
| **Tags** | MUST-BE, BACKEND, FRONTEND, AUTHENTICATION |

---

### 📝 Descripción (Description)

```
Como estudiante de la UFV
Quiero registrarme en la plataforma usando mi correo universitario
Para acceder a un entorno seguro y exclusivo de la comunidad universitaria

CONTEXTO:
El sistema debe permitir el registro únicamente con correos del dominio @ufv.es 
o @alumnos.ufv.es. Durante el registro se solicitará: correo universitario, 
contraseña (mínimo 8 caracteres, al menos una mayúscula, un número y un carácter 
especial), nombre completo y teléfono de contacto opcional. 

El sistema validará el formato del correo y enviará un email de verificación 
antes de activar la cuenta. La cuenta permanecerá en estado "no verificada" 
hasta que el usuario haga clic en el enlace del email.
```

---

### ✅ Criterios de Aceptación (Acceptance Criteria)

```gherkin
Escenario 1: Registro exitoso con correo universitario válido
  DADO que soy un estudiante con correo universitario
  CUANDO accedo a la página de registro
  Y completo el formulario con correo "@alumnos.ufv.es"
  Y proporciono una contraseña válida (Ejemplo123!)
  Y acepto los términos y condiciones
  ENTONCES el sistema crea mi cuenta
  Y envía un correo de verificación
  Y muestra mensaje "Registro exitoso. Revisa tu correo para verificar tu cuenta"
  Y me redirige a página informativa

Escenario 2: Rechazo de correo no universitario
  DADO que intento registrarme
  CUANDO ingreso correo "usuario@gmail.com"
  ENTONCES veo error "Debes usar tu correo universitario UFV (@ufv.es o @alumnos.ufv.es)"
  Y el botón "Registrarse" permanece deshabilitado
  Y no se crea ninguna cuenta

Escenario 3: Validación de contraseña débil
  DADO que estoy en el formulario de registro
  CUANDO ingreso contraseña "123456" (sin mayúsculas ni caracteres especiales)
  ENTONCES veo mensaje "La contraseña debe tener mínimo 8 caracteres, una mayúscula, un número y un carácter especial"
  Y el campo contraseña se marca en rojo
  Y no permite enviar el formulario

Escenario 4: Correo ya registrado
  DADO que existe cuenta con "alumno@alumnos.ufv.es"
  CUANDO intento registrarme con el mismo correo
  ENTONCES veo "Este correo ya está registrado"
  Y se muestra enlace "¿Olvidaste tu contraseña?"
  Y no se crea cuenta duplicada

Escenario 5: Campos obligatorios vacíos
  DADO que estoy en el formulario de registro
  CUANDO intento enviar sin completar correo o contraseña
  ENTONCES veo mensajes de error en campos vacíos
  Y el formulario no se envía
  Y puedo corregir y enviar nuevamente
```

---

### 🏗️ Tareas Técnicas (Child Tasks)

#### Backend (Gonzalo de Lorenzo)

- [ ] **T-005: Crear entidad User con JPA** [3h]
  - Atributos: id, email, passwordHash, nombre, telefono, fotoPerfil, biografia, fechaRegistro, verificado
  - Validaciones: @Email, @NotNull, @Size
  - Constructor, getters, setters
  - Test: Creación de entidad válida e inválida

- [ ] **T-006: Implementar UserRepository** [2h]
  - Extender JpaRepository<User, Long>
  - Métodos: findByEmail(), existsByEmail()
  - Test: Operaciones CRUD básicas

- [ ] **T-007: Crear AuthService - Registro** [4h]
  - Método: registrarUsuario(RegistroRequest)
  - Validar correo UFV con regex
  - Hash de contraseña con BCrypt
  - Generar token de verificación
  - Guardar usuario (verificado=false)
  - Test: Registro válido/inválido, correos duplicados

- [ ] **T-008: Implementar AuthController - Endpoint registro** [3h]
  - POST /api/auth/register
  - DTO: RegistroRequest (email, password, nombre, telefono)
  - Validar entrada con @Valid
  - Respuesta: 201 Created con mensaje
  - Manejo errores: 400 (validación), 409 (conflicto)
  - Test: MockMvc para registro exitoso y errores

- [ ] **T-010: Servicio envío emails verificación** [4h]
  - Configurar JavaMailSender
  - Template HTML para email verificación
  - Enlace con token: /api/auth/verify?token=xxx
  - Logging de emails enviados
  - Test: Mock de envío

#### Frontend (Alejandro de Pazos)

- [ ] **T-016: Crear formulario registro (register.astro)** [4h]
  - Campos: email, password, confirmPassword, nombre, telefono (opcional)
  - Checkbox términos y condiciones
  - Validación frontend en tiempo real
  - Mostrar requisitos contraseña
  - Estados: idle, loading, success, error

- [ ] **T-019: Servicio API - authService.register()** [2h]
  - Función: register(userData)
  - POST a /api/auth/register
  - Manejo respuestas: 201, 400, 409
  - Mensajes error localizados

- [ ] **T-018: Estilos formulario registro** [2h]
  - Diseño responsive mobile-first
  - Estados visuales: focus, error, success
  - Accesibilidad: labels, aria-describedby
  - Validación contraste colores

#### Testing (Jesús de Andrés)

- [ ] **T-011: Tests unitarios AuthService** [3h]
  - Test: Registro usuario válido crea cuenta
  - Test: Correo no-UFV lanza excepción
  - Test: Contraseña débil lanza excepción
  - Test: Email duplicado lanza ConflictException
  - Test: Hash de contraseña no almacena texto plano
  - Cobertura objetivo: >90%

- [ ] **T-012: Tests integración AuthController** [3h]
  - Test E2E: POST registro válido → 201 + email enviado
  - Test: POST correo inválido → 400 + mensaje error
  - Test: POST contraseña débil → 400 + mensaje específico
  - Test: POST email duplicado → 409 + mensaje conflicto
  - Verificar BD con TestEntityManager

#### Accesibilidad (Nicolás Sanchidrián)

- [ ] **T-022: Validar navegación teclado formulario** [1h]
  - Tab recorre todos los campos en orden lógico
  - Enter envía formulario
  - Focus visible en todos los elementos
  - Mensajes error anunciados por lector pantalla

- [ ] **T-023: Validar contraste formulario** [1h]
  - Textos labels: ratio >4.5:1
  - Botón registrarse: ratio >4.5:1
  - Estados error: color + icono (no solo color)
  - Placeholder con contraste suficiente

---

### 🔗 Dependencias

#### Bloquea a:
- HU-002: Verificación de correo (necesita flujo registro)
- HU-003: Inicio de sesión (necesita usuarios registrados)
- HU-005: Ver y editar perfil (necesita autenticación)

#### Depende de:
- T-001: Inicializar proyecto Spring Boot (infraestructura)
- T-013: Inicializar proyecto Astro (infraestructura)
- T-021: Configurar CORS (comunicación frontend-backend)

---

### 📊 Estimación Detallada

| Componente | Estimación | Complejidad |
|------------|------------|-------------|
| Backend API | 16h | Media-Alta |
| Frontend Form | 8h | Media |
| Testing | 6h | Media |
| Validaciones | 4h | Baja-Media |
| Accesibilidad | 2h | Baja |
| **TOTAL** | **36h** | **8 Story Points** |

**Nota:** 1 Story Point ≈ 4-5 horas de trabajo productivo

---

### 🧪 Plan de Testing

#### Tests Automatizados (JUnit + MockMvc)
```java
@Test
void registroExitoso_DeberiCrearUsuarioYEnviarEmail() {
    // Given
    RegistroRequest request = new RegistroRequest(
        "nuevo@alumnos.ufv.es", 
        "Segura123!", 
        "Juan Pérez",
        "600123456"
    );
    
    // When
    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
    
    // Then
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("Registro exitoso"));
    
    // Verificar usuario en BD
    User user = userRepository.findByEmail("nuevo@alumnos.ufv.es").get();
    assertFalse(user.isVerificado());
    assertTrue(passwordEncoder.matches("Segura123!", user.getPasswordHash()));
    
    // Verificar email enviado
    verify(emailService, times(1)).enviarEmailVerificacion(anyString(), anyString());
}

@Test
void registroConCorreoGmail_DeberiaRetornar400() {
    RegistroRequest request = new RegistroRequest(
        "usuario@gmail.com", 
        "Segura123!", 
        "Juan",
        null
    );
    
    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value(containsString("correo universitario UFV")));
}
```

#### Tests Manuales (QA)
- [ ] Probar registro con correo @ufv.es
- [ ] Probar registro con correo @alumnos.ufv.es
- [ ] Intentar registro con gmail, yahoo, hotmail
- [ ] Validar todas las combinaciones de contraseña débil
- [ ] Verificar email llega correctamente a bandeja
- [ ] Probar en navegadores: Chrome, Firefox, Safari, Edge
- [ ] Responsive: Mobile, Tablet, Desktop

---

### 📸 Referencias Visuales

#### Wireframe - Formulario de Registro
```
┌─────────────────────────────────────────┐
│          🎓 UFV Share                   │
│                                         │
│    Únete a la comunidad UFV             │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │ Correo universitario *          │   │
│  │ ejemplo@alumnos.ufv.es          │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │ Contraseña *                    │   │
│  │ ••••••••                        │   │
│  └─────────────────────────────────┘   │
│  ℹ️ Mín. 8 caracteres, mayúscula,      │
│     número y carácter especial          │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │ Confirmar contraseña *          │   │
│  │ ••••••••                        │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │ Nombre completo *               │   │
│  │ Juan Pérez García               │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │ Teléfono (opcional)             │   │
│  │ 600 123 456                     │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ☐ Acepto términos y condiciones       │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │      REGISTRARSE                 │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ¿Ya tienes cuenta? Inicia sesión      │
└─────────────────────────────────────────┘
```

#### Flujo de Proceso
```
Usuario                    Frontend               Backend               Email Service
   |                          |                      |                        |
   |--[Completa formulario]-->|                      |                        |
   |                          |                      |                        |
   |                          |--[Valida datos]      |                        |
   |                          |                      |                        |
   |                          |--[POST /register]--->|                        |
   |                          |                      |                        |
   |                          |                      |--[Valida correo UFV]   |
   |                          |                      |                        |
   |                          |                      |--[Hash password]       |
   |                          |                      |                        |
   |                          |                      |--[Guarda usuario BD]   |
   |                          |                      |                        |
   |                          |                      |--[Genera token]------->|
   |                          |                      |                        |
   |                          |                      |                        |--[Envía email]
   |                          |<--[201 Created]------|                        |
   |                          |                      |                        |
   |<--[Mensaje confirmación]-|                      |                        |
   |                          |                      |                        |
   |                                                 |                        |
   |<-----------------[Email con link verificación]-------------------------|
```

---

### 🚦 Definición de "Done" (DoD)

Esta historia se considera COMPLETA cuando:

#### Código
- ✅ Entidad User creada y mapeada a BD
- ✅ UserRepository implementado con métodos necesarios
- ✅ AuthService con lógica registro funcional
- ✅ AuthController con endpoint POST /register
- ✅ Formulario frontend con validaciones
- ✅ Servicio authService.register() implementado
- ✅ Servicio email configurado y probado
- ✅ Code review aprobado por 1+ compañero
- ✅ Sin code smells en SonarQube

#### Testing
- ✅ Tests unitarios AuthService: >90% cobertura
- ✅ Tests integración AuthController: todos AC cubiertos
- ✅ Tests frontend: validaciones formulario
- ✅ Tests manuales QA: 0 bugs críticos
- ✅ Todos los tests CI/CD pasando ✅

#### Funcionalidad
- ✅ Todos los 5 criterios de aceptación verificados
- ✅ Validación correos UFV funciona correctamente
- ✅ Validación contraseñas cumple requisitos
- ✅ Email de verificación llega en <1 minuto
- ✅ Mensajes de error claros y útiles
- ✅ No se puede registrar email duplicado

#### Accesibilidad
- ✅ Formulario navegable 100% por teclado
- ✅ Focus visible en todos los campos
- ✅ Labels asociados correctamente
- ✅ Mensajes error accesibles con aria-live
- ✅ Contraste validado (ratio >4.5:1)

#### Seguridad
- ✅ Contraseñas hasheadas con BCrypt
- ✅ Nunca se muestra contraseña en logs
- ✅ Tokens verificación únicos y seguros
- ✅ Validación server-side (no solo cliente)

#### Documentación
- ✅ API endpoint documentado en Swagger/OpenAPI
- ✅ Código comentado en partes complejas
- ✅ README actualizado con instrucciones
- ✅ Diagramas de flujo actualizados

#### Integración
- ✅ Código mergeado a rama `main`
- ✅ Pipeline CI/CD pasando (build + tests)
- ✅ Desplegado en entorno de testing
- ✅ Base de datos migrada correctamente

#### Validación
- ✅ Demo exitosa en Sprint Review
- ✅ Product Owner acepta la historia
- ✅ Feedback incorporado si hay ajustes menores

---

### 📝 Notas de Desarrollo

#### Decisiones Técnicas
- **Hash de contraseña:** BCryptPasswordEncoder con strength 12
- **Regex correo UFV:** `^[a-zA-Z0-9._%+-]+@(ufv\.es|alumnos\.ufv\.es)$`
- **Token verificación:** UUID aleatorio con 24h validez
- **BD desarrollo:** H2 in-memory para tests, MySQL para producción

#### Riesgos Identificados
- ⚠️ **Email podría ir a spam:** Configurar correctamente SPF/DKIM
- ⚠️ **Carga del servicio email:** Implementar retry y logging
- ⚠️ **Validación lado cliente puede saltarse:** Siempre validar en backend

#### Mejoras Futuras (Fuera de scope)
- 🔮 Registro con OAuth (SSO UFV) si disponible
- 🔮 Verificación por SMS alternativa
- 🔮 Foto de perfil durante registro
- 🔮 ReCAPTCHA para evitar bots

---

### 💬 Conversaciones (Comments)

**Gonzalo - 10/02/2026 10:30**
> He completado la entidad User y el repositorio. ¿Debería agregar índice único en el campo email?

**Jesús - 10/02/2026 11:15**
> Sí, definitivamente. También añade @Column(unique=true) en la entidad para que JPA lo cree automáticamente. Agregaré test específico para verificar que se lanza excepción con email duplicado.

**Alejandro - 11/02/2026 09:00**
> El formulario está listo en frontend. He implementado validación en tiempo real que muestra los requisitos de contraseña conforme se escribe. ¿Probamos la integración esta tarde?

**Nicolás - 11/02/2026 14:30**
> He validado accesibilidad del formulario. Todo correcto excepto el mensaje de error que no se anuncia automáticamente. Añadí aria-live="polite" y ahora funciona perfecto con NVDA.

**Mario - 12/02/2026 16:00**
> Historia completada y demo preparada para Sprint Review de mañana. Todos los AC verificados ✅. Gran trabajo equipo!

---

### 📊 Burndown de la Historia

```
Story Points Restantes
8 │ ●
  │  ╲
7 │   ●
  │    ╲
6 │     ●─●
  │        ╲
5 │         ●
  │          ╲╲
4 │            ●
  │             ╲
3 │              ●─●
  │                 ╲
2 │                  ●
  │                   ╲
1 │                    ●
  │                     ╲
0 │                      ●
  └────────────────────────
  L  M  X  J  V  L  M  X  (días)
```

**Observaciones:**
- Ligero retraso inicial por configuración de email service
- Recuperado gracias a trabajo en paralelo (backend + frontend)
- Completada 1 día antes del fin de sprint

---

### ✅ Estado Final

**Fecha de inicio:** 03/02/2026  
**Fecha de completado:** 12/02/2026  
**Duración real:** 8 días (dentro de estimación)  
**Estado:** **DONE** ✅  
**Aceptado por PO:** Sí ✅  
**Desplegado a Testing:** Sí ✅  

---

**💡 Lección aprendida:** La configuración del servicio de email tomó más tiempo del estimado. En próximos sprints, considerar tareas de infraestructura externa como spikes separados para mejor estimación.

---

Este ejemplo muestra el nivel de detalle esperado para cada User Story en Azure DevOps. Adapta según las necesidades específicas de tu equipo.
