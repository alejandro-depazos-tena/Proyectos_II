# Guía de Transformación del Backlog

## 📊 Comparativa: Antes vs. Después

### ❌ ANTES (Backlog actual)

```
ID: 57
Type: Product Backlog Item
Title: "Creacion del proyecto base Spring Boot"
```

**Problemas:**
- No describe valor de usuario
- Demasiado técnico
- No sigue formato "Como...quiero...para..."
- Es una tarea, no una historia

### ✅ DESPUÉS (Backlog mejorado)

```
ID: HU-001
Type: User Story
Title: "Registro de usuario con correo universitario"
Description: "Como estudiante de la UFV
             Quiero registrarme usando mi correo universitario
             Para acceder a un entorno seguro de la comunidad"
Acceptance Criteria: 
    - Solo acepta correos @ufv.es o @alumnos.ufv.es
    - Valida contraseña (mín 8 chars, mayúscula, número, especial)
    - Envía email de verificación
    - Muestra mensaje de confirmación
```

**Mejoras:**
- ✅ Describe valor claro para el usuario
- ✅ Sigue formato estándar
- ✅ Tiene criterios de aceptación testables
- ✅ Las tareas técnicas están separadas

---

## 🔄 Mapeo de Transformación

He reorganizado tus PBIs actuales en **3 niveles jerárquicos**:

### Nivel 1: ÉPICAS (Features)
Agrupan historias relacionadas por área funcional

```
- Autenticación
- Gestión de Productos
- Búsqueda
- Transacciones
- Mensajería
- Historial
- Moderación
- Accesibilidad
```

### Nivel 2: HISTORIAS DE USUARIO (User Stories)
Representan valor de negocio completo

**Ejemplo de transformación:**

| Tus PBIs actuales (tareas) | → | Historia de Usuario |
|----------------------------|---|---------------------|
| 57: Creación proyecto Spring Boot | | |
| 60: Modelado entidad Usuario | → | **HU-001: Registro de usuario** |
| 61: Implementación repositorio Usuario | | |
| 63: Implementación servicio Usuario | | |
| 71: Implementación controlador Usuario | | |

**Todas esas tareas técnicas ahora son sub-tareas de HU-001**

### Nivel 3: TAREAS (Tasks)
Trabajo técnico necesario para completar la historia

```
HU-001: Registro de usuario
├── T-001: Inicializar proyecto Spring Boot
├── T-005: Crear entidad User con JPA
├── T-006: Implementar UserRepository
├── T-007: Crear AuthService
├── T-008: Implementar AuthController
├── T-011: Tests unitarios UserService
└── T-016: Crear formulario registro frontend
```

---

## 📋 Nueva Estructura del CSV

### Campos importantes:

| Campo | Descripción | Ejemplo |
|-------|-------------|---------|
| **ID** | HU-XXX para historias, T-XXX para tareas | HU-001, T-005 |
| **Work Item Type** | User Story o Task | User Story |
| **Title** | Título corto orientado a valor | "Registro de usuario con correo universitario" |
| **Description** | Formato "Como...quiero...para..." | Ver ejemplos abajo |
| **Acceptance Criteria** | Lista de criterios verificables | - Solo correos @ufv.es<br>- Valida contraseña |
| **Priority** | 1=Must-be, 2=Should, 3=Could | 1 |
| **Sprint** | Sprint asignado | Sprint 1 |
| **Epic** | Épica/área funcional | Autenticación |
| **Story Points** | Estimación de esfuerzo (Fibonacci) | 8 |
| **Tags** | Etiquetas útiles | MUST-BE, TEST, TECHNICAL |

---

## 🎯 Formato "Como...Quiero...Para..."

### Plantilla:

```
Como [tipo de usuario]
Quiero [acción que desea realizar]
Para [beneficio o valor que obtiene]
```

### Ejemplos de tus historias:

**✅ Bueno:**
```
Como estudiante de la UFV
Quiero registrarme usando mi correo universitario
Para acceder a un entorno seguro de la comunidad
```

**✅ Bueno:**
```
Como usuario autenticado
Quiero publicar objetos para prestar/alquilar/vender
Para ponerlos a disposición de otros estudiantes
```

**✅ Bueno:**
```
Como usuario interesado en un producto
Quiero enviar una solicitud al propietario
Para iniciar el proceso de préstamo/alquiler/compra
```

**❌ Malo (muy técnico):**
```
Como desarrollador
Quiero crear el repositorio JPA
Para persistir usuarios en la BD
```
☝️ Esto es una TAREA, no una historia de usuario

---

## 📑 Cómo Importar a Azure DevOps

### Opción 1: Importación CSV directa

1. En Azure DevOps → Boards → Backlogs
2. Clic en "⋮" (más opciones) → Import Work Items
3. Selecciona `backlog_mejorado.csv`
4. Mapea los campos según corresponda
5. Importar

### Opción 2: Creación manual estructurada

Para cada Sprint:

#### A. Crear las Épicas (Features)
```
- Autenticación
- Gestión de Productos
- Búsqueda
- Transacciones
- etc.
```

#### B. Crear User Stories bajo cada Épica

**Ejemplo para Sprint 1:**
1. Crear Feature "Autenticación"
2. Dentro, crear User Story "HU-001: Registro de usuario"
   - Completar descripción con formato "Como...quiero...para..."
   - Añadir criterios de aceptación
   - Asignar a: Gonzalo
   - Priority: 1
   - Story Points: 8
   - Sprint: Sprint 1
   
3. Dentro de HU-001, crear Tasks:
   - T-001: Inicializar proyecto Spring Boot
   - T-005: Crear entidad User
   - T-006: Implementar UserRepository
   - etc.

---

## 🏷️ Sistema de Tags Recomendado

Usa tags para clasificación rápida:

| Tag | Uso |
|-----|-----|
| **MUST-BE** | Historia crítica para MVP |
| **SHOULD** | Importante pero no crítica |
| **COULD** | Deseable pero optional |
| **TECHNICAL** | Tareas de infraestructura |
| **TEST** | Tareas de testing |
| **DOCUMENTATION** | Documentación |
| **SCRUM** | Ceremonias y gestión |
| **FRONTEND** | Trabajo frontend |
| **BACKEND** | Trabajo backend |
| **ACCESIBILIDAD** | Relacionado con WCAG |

---

## ✅ Definición de "Done" para User Stories

Para considerar una historia completada:

### Código
- ✅ Implementación backend completa
- ✅ Implementación frontend completa
- ✅ Code review por peer
- ✅ Sin code smells críticos

### Testing
- ✅ Tests unitarios (TDD) con >80% cobertura
- ✅ Tests de integración para API
- ✅ Todos los criterios de aceptación verificados

### Funcionalidad
- ✅ Todos los AC cumplidos
- ✅ Validaciones implementadas
- ✅ Manejo de errores
- ✅ Mensajes usuario claros

### Accesibilidad (desde Sprint 1)
- ✅ Navegable por teclado
- ✅ Contraste validado
- ✅ Alt text en imágenes

### Integración
- ✅ Código mergeado a main
- ✅ CI/CD pasando
- ✅ Desplegado en entorno testing

### Validación
- ✅ Demo en Sprint Review
- ✅ Aceptado por PO

---

## 📊 Estimación de Story Points

Usa escala Fibonacci:

| Points | Complejidad | Tiempo estimado | Ejemplo |
|--------|-------------|-----------------|---------|
| **1** | Trivial | 1-2 horas | Cambio de texto simple |
| **2** | Muy simple | 2-4 horas | Añadir validación simple |
| **3** | Simple | Medio día | Cerrar sesión |
| **5** | Moderada | 1 día | Ver y editar perfil |
| **8** | Compleja | 1-2 días | Registro de usuario, Búsqueda |
| **13** | Muy compleja | 2-3 días | Publicar producto, Gestionar solicitudes |
| **21** | Extrema | >3 días | ⚠️ Dividir en historias más pequeñas |

**Regla de oro:** Si una historia es >13 puntos, divídela en varias historias más pequeñas.

---

## 🔥 Priorización: Modelo MoSCoW

He priorizado las historias según:

### Must-be (Priority 1) ⭐
**Sin esto, el producto NO funciona**
- HU-001, 002, 003, 006: Autenticación básica
- HU-009, 010, 015, 016: Productos y búsqueda básica
- HU-019, 020, 021, 022, 023: Flujo de transacciones
- HU-033, 034, 035: Accesibilidad crítica (WCAG)

### Should (Priority 2) 📌
**Importante, deberían estar en MVP**
- HU-004, 005: Recuperación contraseña, perfil
- HU-011, 012, 013: Gestión avanzada productos
- HU-017: Filtros avanzados
- HU-024, 025, 026, 027: Mensajería y notificaciones
- HU-029: Historial

### Could (Priority 3) 💡
**Mejora que se puede posponer**
- HU-030: Estadísticas personales
- HU-007: Eliminar cuenta
- HU-028: Notificaciones por email
- HU-038: Documentación accesibilidad

---

## 📅 Distribución por Sprints

### Sprint 1 (6 historias - 34 points) - AUTENTICACIÓN
**Objetivo:** Base sólida de usuarios y seguridad

- HU-001: Registro (8 pts) 🔴
- HU-002: Verificación email (5 pts) 🔴
- HU-003: Login (8 pts) 🔴
- HU-004: Recuperación contraseña (5 pts) 🟡
- HU-005: Ver/editar perfil (5 pts) 🟡
- HU-006: Cerrar sesión (3 pts) 🔴

**+ 21 tareas técnicas** (T-001 a T-031)

### Sprint 2 (10 historias - 42 points) - PRODUCTOS
**Objetivo:** Catálogo funcional y búsqueda

- HU-008: Ver perfil público (5 pts) 🟡
- HU-009: Publicar producto (13 pts) 🔴
- HU-010: Ver detalle (8 pts) 🔴
- HU-011: Editar producto (5 pts) 🟡
- HU-012: Eliminar producto (3 pts) 🟡
- HU-013: Mis productos (5 pts) 🟡
- HU-015: Categorización (3 pts) 🔴
- HU-016: Buscar por texto (8 pts) 🔴
- HU-017: Filtrar productos (8 pts) 🟡
- HU-018: Ordenar resultados (3 pts) 💡

### Sprint 3 (10 historias - 48 points) - TRANSACCIONES
**Objetivo:** Flujo completo de préstamo/alquiler/venta

- HU-014: Marcar como vendido (3 pts) 🟡
- HU-019: Solicitar producto (13 pts) 🔴
- HU-020: Ver mis solicitudes (5 pts) 🔴
- HU-021: Gestionar solicitudes (13 pts) 🔴
- HU-022: Confirmar inicio (8 pts) 🔴
- HU-023: Confirmar fin (8 pts) 🔴
- HU-024: Ver transacciones activas (5 pts) 🟡
- HU-025: Enviar mensaje (5 pts) 🟡
- HU-026: Bandeja mensajes (8 pts) 🟡
- HU-027: Notificaciones (8 pts) 🟡

### Sprint 4 (12 historias - 38 points) - CIERRE Y WCAG
**Objetivo:** MVP completo con accesibilidad

- HU-007: Eliminar cuenta (3 pts) 💡
- HU-028: Notif por email (5 pts) 💡
- HU-029: Historial (5 pts) 🟡
- HU-030: Estadísticas (5 pts) 💡
- HU-031: Reportar publicación (3 pts) 🟡
- HU-032: Reportar usuario (3 pts) 🟡
- HU-033: Navegación teclado (8 pts) 🔴
- HU-034: Textos alternativos (5 pts) 🔴
- HU-035: Contraste colores (5 pts) 🔴
- HU-036: Roles ARIA (5 pts) 🟡
- HU-037: Zoom responsive (5 pts) 🟡
- HU-038: Doc accesibilidad (3 pts) 💡

**Leyenda:** 🔴 Must-be | 🟡 Should | 💡 Could

---

## 🚀 Próximos Pasos

### 1. Inmediato (Hoy)
- [ ] Revisar el CSV mejorado con el equipo
- [ ] Decidir si importar o crear manual en Azure DevOps
- [ ] Asignar IDs finales en Azure

### 2. Esta semana
- [ ] Sprint Planning 1 detallado
- [ ] Estimar cada historia con Planning Poker
- [ ] Refinar criterios de aceptación con el equipo
- [ ] Definir DoD específico del equipo

### 3. Continuo
- [ ] Refinar backlog cada sprint
- [ ] Añadir nuevas historias descubiertas
- [ ] Actualizar prioridades según feedback
- [ ] Mantener historias en estado "Ready" para próximo sprint

---

## 💡 Tips de Buenas Prácticas

### ✅ Hacer:
- Escribir historias desde perspectiva del USUARIO, no del desarrollador
- Cada historia debe ser INDEPENDIENTE (puede desarrollarse sola)
- Mantener historias PEQUEÑAS (<13 points idealmente)
- Tener criterios de aceptación TESTABLES
- Refinar backlog CONSTANTEMENTE

### ❌ Evitar:
- Historias técnicas como "Crear base de datos"
- Historias que requieren todas las demás para probarse
- Historias gigantes de 21+ points
- Criterios vagos como "Debe funcionar bien"
- Backlog estático que nunca se revisa

---

## 📚 Referencias

- **Historias Usuario completas:** Ver `historias_usuario_backlog.md`
- **Guía INVEST:** Independent, Negotiable, Valuable, Estimable, Small, Testable
- **SCRUM Guide:** https://scrumguides.org/
- **Azure DevOps:** Boards → Import Work Items
- **WCAG 2.1:** https://www.w3.org/WAI/WCAG21/quickref/

---

## ❓ Preguntas Frecuentes

**P: ¿Debo eliminar todos mis PBIs actuales?**
R: No, puedes transformarlos progresivamente o mantenerlos como referencia. El CSV nuevo es una propuesta mejorada.

**P: ¿Las tareas técnicas desaparecen?**
R: No, ahora son Tasks hijas de User Stories, manteniendo jerarquía clara.

**P: ¿Puedo cambiar las prioridades?**
R: Sí, estas son sugerencias basadas en análisis del proyecto. Ajústalas con tu equipo.

**P: ¿Story points son días?**
R: No directamente. Son medida relativa de COMPLEJIDAD, no tiempo absoluto.

**P: ¿Necesito todas estas historias?**
R: Estas 38 historias forman el MVP completo. Puedes empezar con las Must-be (18 historias).

---

**Creado por:** GitHub Copilot  
**Fecha:** 11 de febrero de 2026  
**Para proyecto:** UFV Share  
**Versión:** 1.0
