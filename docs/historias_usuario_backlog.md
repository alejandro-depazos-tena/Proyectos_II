# Historias de Usuario - UFV Share
## Backlog del Producto para Azure DevOps

**Fecha de creación:** 11 de febrero de 2026  
**Proyecto:** UFV Share - Plataforma de préstamo, alquiler y compraventa universitaria  
**Metodología:** SCRUM con 4 sprints planificados  
**Equipo:** 5 personas  

---

## Resumen Ejecutivo

Este documento contiene el backlog completo de historias de usuario para el desarrollo de UFV Share, una aplicación web dirigida a la comunidad universitaria de la Universidad Francisco de Vitoria. El backlog está organizado en 7 épicas funcionales principales y distribuido en 4 sprints de desarrollo.

### Contexto del Producto

**¿Qué es UFV Share?**  
Una plataforma web que permite a estudiantes de la UFV compartir recursos mediante préstamo, alquiler o compraventa de objetos, fomentando la economía colaborativa y sostenibilidad dentro del campus.

**Usuarios principales:**  
Estudiantes universitarios de la UFV con correo institucional verificado.

**Problema que resuelve:**  
- Necesidad de materiales de uso temporal (libros, equipos tecnológicos, material académico)
- Alto coste de compra de artículos que se usan puntualmente
- Falta de canal centralizado y seguro para intercambios entre estudiantes
- Desperdicio de recursos y poco aprovechamiento de objetos en desuso

**Alcance inicial (MVP):**  
- Autenticación con correo universitario
- Publicación de objetos (préstamo/alquiler/compraventa)
- Búsqueda y filtrado de productos
- Sistema de solicitudes y gestión de transacciones
- Historial de transacciones
- Sistema básico de mensajería
- Interfaz accesible según WCAG

---

## Distribución por Sprints

| Sprint | Duración | Foco Principal | Historias |
|--------|----------|----------------|-----------|
| **Sprint 1** | Semanas 1-3 | Autenticación, usuarios y estructura base | HU-001 a HU-008 |
| **Sprint 2** | Semanas 4-6 | Gestión de productos y búsqueda | HU-009 a HU-018 |
| **Sprint 3** | Semanas 7-9 | Transacciones, solicitudes y mensajería | HU-019 a HU-028 |
| **Sprint 4** | Semanas 10-12 | Historial, reportes, refinamiento y accesibilidad | HU-029 a HU-038 |

---

## Épicas Funcionales

1. **ÉPICA 1: Autenticación y Gestión de Usuarios**
2. **ÉPICA 2: Gestión de Productos**
3. **ÉPICA 3: Búsqueda y Navegación**
4. **ÉPICA 4: Gestión de Transacciones**
5. **ÉPICA 5: Comunicación y Mensajería**
6. **ÉPICA 6: Historial y Seguimiento**
7. **ÉPICA 7: Moderación y Accesibilidad**

---

# ÉPICA 1: Autenticación y Gestión de Usuarios

## HU-001: Registro de usuario con correo universitario

**ID:** HU-001  
**Área Funcional:** Autenticación y Gestión de Usuarios  
**Prioridad:** Must-be (Requisito básico)  
**Sprint Recomendado:** Sprint 1  
**Dependencias:** Ninguna  

### Historia de Usuario
Como **estudiante de la UFV**  
Quiero **registrarme en la plataforma usando mi correo universitario**  
Para **acceder a un entorno seguro y exclusivo de la comunidad universitaria**

### Descripción Detallada
El sistema debe permitir el registro de nuevos usuarios únicamente con correos del dominio @ufv.es o @alumnos.ufv.es. Durante el registro se solicitará: correo universitario, contraseña (mínimo 8 caracteres, al menos una mayúscula, un número y un carácter especial), nombre completo y teléfono de contacto opcional. El sistema debe validar el formato del correo y enviar un email de verificación antes de activar la cuenta.

### Criterios de Aceptación

```gherkin
Escenario 1: Registro exitoso con correo universitario válido
  Dado que soy un estudiante con correo universitario
  Cuando accedo a la página de registro
  Y completo el formulario con correo "@alumnos.ufv.es"
  Y proporciono una contraseña válida
  Y acepto los términos y condiciones
  Entonces el sistema crea mi cuenta
  Y envía un correo de verificación
  Y muestra mensaje "Registro exitoso. Revisa tu correo para verificar tu cuenta"

Escenario 2: Rechazo de correo no universitario
  Dado que intento registrarme
  Cuando ingreso un correo con dominio diferente a "@ufv.es" o "@alumnos.ufv.es"
  Entonces el sistema muestra error "Debes usar tu correo universitario UFV"
  Y no permite completar el registro

Escenario 3: Validación de contraseña débil
  Dado que estoy en el formulario de registro
  Cuando ingreso una contraseña que no cumple los requisitos
  Entonces el sistema muestra "La contraseña debe tener mínimo 8 caracteres, una mayúscula, un número y un carácter especial"
  Y no permite enviar el formulario

Escenario 4: Correo ya registrado
  Dado que existe una cuenta con correo "alumno@alumnos.ufv.es"
  Cuando intento registrarme con el mismo correo
  Entonces el sistema muestra "Este correo ya está registrado"
  Y sugiere recuperar contraseña
```

---

## HU-002: Verificación de correo electrónico

**ID:** HU-002  
**Área Funcional:** Autenticación y Gestión de Usuarios  
**Prioridad:** Must-be (Requisito básico)  
**Sprint Recomendado:** Sprint 1  
**Dependencias:** HU-001  

### Historia de Usuario
Como **usuario registrado**  
Quiero **verificar mi correo electrónico mediante un enlace**  
Para **confirmar mi identidad y activar mi cuenta en la plataforma**

### Descripción Detallada
Tras el registro, el sistema debe enviar automáticamente un email al correo universitario del usuario con un enlace de verificación único y temporal (válido por 24 horas). Al hacer clic en el enlace, la cuenta se activa y el usuario puede iniciar sesión. Si el enlace expira, el usuario puede solicitar un nuevo envío desde la página de login.

### Criterios de Aceptación

```gherkin
Escenario 1: Verificación exitosa de email
  Dado que me he registrado con correo "alumno@alumnos.ufv.es"
  Cuando recibo el email de verificación
  Y hago clic en el enlace dentro de las 24 horas
  Entonces mi cuenta se activa
  Y soy redirigido a la página de login
  Y veo el mensaje "Cuenta verificada exitosamente. Ya puedes iniciar sesión"

Escenario 2: Enlace de verificación expirado
  Dado que recibí un email de verificación hace más de 24 horas
  Cuando hago clic en el enlace
  Entonces veo el mensaje "Este enlace ha expirado"
  Y se muestra opción "Reenviar correo de verificación"

Escenario 3: Reenvío de correo de verificación
  Dado que mi enlace ha expirado
  Cuando solicito reenviar el correo de verificación
  Y proporciono mi email
  Entonces recibo un nuevo correo con enlace válido
  Y veo mensaje "Correo reenviado. Revisa tu bandeja de entrada"
```

---

## HU-003: Inicio de sesión

**ID:** HU-003  
**Área Funcional:** Autenticación y Gestión de Usuarios  
**Prioridad:** Must-be (Requisito básico)  
**Sprint Recomendado:** Sprint 1  
**Dependencias:** HU-001, HU-002  

### Historia de Usuario
Como **usuario registrado y verificado**  
Quiero **iniciar sesión con mi correo y contraseña**  
Para **acceder a las funcionalidades de la plataforma**

### Descripción Detallada
El sistema debe proporcionar un formulario de login donde el usuario ingresa su correo universitario y contraseña. Tras validar las credenciales, el sistema genera un token de sesión (JWT) y redirige al usuario a la página principal. Se debe implementar protección contra intentos de login masivos (máximo 5 intentos fallidos en 15 minutos).

### Criterios de Aceptación

```gherkin
Escenario 1: Login exitoso con credenciales válidas
  Dado que tengo una cuenta verificada
  Cuando ingreso mi correo y contraseña correctos
  Y hago clic en "Iniciar sesión"
  Entonces el sistema me autentica
  Y soy redirigido a la página principal
  Y veo mi nombre de usuario en el menú

Escenario 2: Login fallido por credenciales incorrectas
  Dado que estoy en la página de login
  Cuando ingreso un correo válido pero contraseña incorrecta
  Entonces veo el mensaje "Credenciales incorrectas"
  Y permanezco en la página de login

Escenario 3: Bloqueo temporal por intentos fallidos
  Dado que he fallado 5 intentos de login consecutivos
  Cuando intento ingresar nuevamente
  Entonces veo "Cuenta temporalmente bloqueada. Intenta de nuevo en 15 minutos"
  Y no puedo intentar login hasta que pase el tiempo

Escenario 4: Intento de login sin verificar email
  Dado que me registré pero no verifiqué mi correo
  Cuando intento iniciar sesión con credenciales correctas
  Entonces veo "Debes verificar tu correo antes de iniciar sesión"
  Y se muestra opción para reenviar email de verificación
```

---

## HU-004: Recuperación de contraseña

**ID:** HU-004  
**Área Funcional:** Autenticación y Gestión de Usuarios  
**Prioridad:** Should (Importante)  
**Sprint Recomendado:** Sprint 1  
**Dependencias:** HU-001  

### Historia de Usuario
Como **usuario registrado que olvidó su contraseña**  
Quiero **solicitar el restablecimiento de mi contraseña**  
Para **recuperar el acceso a mi cuenta**

### Descripción Detallada
El sistema debe proporcionar un enlace "¿Olvidaste tu contraseña?" en la página de login. Al hacer clic, el usuario ingresa su correo universitario y recibe un email con un enlace temporal (válido 30 minutos) para crear una nueva contraseña. La nueva contraseña debe cumplir los mismos requisitos de seguridad que en el registro.

### Criterios de Aceptación

```gherkin
Escenario 1: Solicitud de restablecimiento exitosa
  Dado que olvidé mi contraseña
  Cuando accedo a "¿Olvidaste tu contraseña?"
  Y ingreso mi correo "alumno@alumnos.ufv.es"
  Entonces recibo un email con enlace de restablecimiento
  Y veo mensaje "Revisa tu correo para restablecer tu contraseña"

Escenario 2: Restablecimiento de contraseña exitoso
  Dado que recibí el email de restablecimiento
  Cuando hago clic en el enlace dentro de 30 minutos
  Y ingreso mi nueva contraseña (cumpliendo requisitos)
  Y confirmo la nueva contraseña
  Entonces la contraseña se actualiza
  Y veo "Contraseña actualizada exitosamente"
  Y puedo iniciar sesión con la nueva contraseña

Escenario 3: Enlace de restablecimiento expirado
  Dado que recibí un enlace hace más de 30 minutos
  Cuando intento usarlo
  Entonces veo "Este enlace ha expirado"
  Y debo solicitar un nuevo enlace
```

---

## HU-005: Ver y editar perfil de usuario

**ID:** HU-005  
**Área Funcional:** Autenticación y Gestión de Usuarios  
**Prioridad:** Should (Importante)  
**Sprint Recomendado:** Sprint 1  
**Dependencias:** HU-003  

### Historia de Usuario
Como **usuario autenticado**  
Quiero **ver y editar mi información de perfil**  
Para **mantener mis datos actualizados y gestionar mi presencia en la plataforma**

### Descripción Detallada
El usuario debe poder acceder a su perfil desde el menú principal, donde visualiza: nombre completo, correo universitario (no editable), teléfono, foto de perfil, fecha de registro y número de objetos publicados. Puede editar: nombre, teléfono, foto de perfil y agregar una breve biografía (máximo 200 caracteres).

### Criterios de Aceptación

```gherkin
Escenario 1: Visualización de perfil propio
  Dado que he iniciado sesión
  Cuando accedo a "Mi perfil"
  Entonces veo mi información personal
  Y veo estadísticas (objetos publicados, transacciones)
  Y veo botón "Editar perfil"

Escenario 2: Edición exitosa de perfil
  Dado que estoy en mi perfil
  Cuando hago clic en "Editar perfil"
  Y modifico mi nombre y teléfono
  Y agrego una biografía
  Y hago clic en "Guardar cambios"
  Entonces mis datos se actualizan
  Y veo "Perfil actualizado exitosamente"

Escenario 3: Carga de foto de perfil
  Dado que estoy editando mi perfil
  Cuando subo una imagen JPG o PNG menor a 5MB
  Entonces la imagen se guarda como mi foto de perfil
  Y se muestra en mi perfil y publicaciones

Escenario 4: Validación de tamaño de foto
  Dado que intento subir una foto de perfil
  Cuando selecciono un archivo mayor a 5MB
  Entonces veo "La imagen debe ser menor a 5MB"
  Y la foto no se carga
```

---

## HU-006: Cerrar sesión

**ID:** HU-006  
**Área Funcional:** Autenticación y Gestión de Usuarios  
**Prioridad:** Must-be (Requisito básico)  
**Sprint Recomendado:** Sprint 1  
**Dependencias:** HU-003  

### Historia de Usuario
Como **usuario autenticado**  
Quiero **cerrar mi sesión de forma segura**  
Para **proteger mi cuenta cuando no esté usando la plataforma**

### Descripción Detallada
El usuario debe poder cerrar sesión desde cualquier página mediante una opción visible en el menú de usuario. Al cerrar sesión, el token de autenticación se invalida y el usuario es redirigido a la página de login.

### Criterios de Aceptación

```gherkin
Escenario 1: Cierre de sesión exitoso
  Dado que he iniciado sesión
  Cuando hago clic en "Cerrar sesión" en el menú
  Entonces mi sesión se invalida
  Y soy redirigido a la página de login
  Y no puedo acceder a páginas protegidas sin autenticarme nuevamente

Escenario 2: Acceso denegado tras cerrar sesión
  Dado que cerré mi sesión
  Cuando intento acceder a una URL protegida directamente
  Entonces soy redirigido a la página de login
  Y veo mensaje "Debes iniciar sesión para acceder"
```

---

## HU-007: Eliminar cuenta de usuario

**ID:** HU-007  
**Área Funcional:** Autenticación y Gestión de Usuarios  
**Prioridad:** Could (Deseable)  
**Sprint Recomendado:** Sprint 4  
**Dependencias:** HU-005  

### Historia de Usuario
Como **usuario de la plataforma**  
Quiero **eliminar permanentemente mi cuenta**  
Para **ejercer mi derecho a borrar mis datos personales**

### Descripción Detallada
Desde la configuración de perfil, el usuario puede solicitar la eliminación de su cuenta. El sistema verifica que no tenga transacciones pendientes (préstamos activos, solicitudes sin resolver). Si las hay, debe resolverlas primero. Se muestra advertencia clara de que la acción es irreversible y se solicita confirmación escribiendo "ELIMINAR". Los datos se anonimizar tras 30 días.

### Criterios de Aceptación

```gherkin
Escenario 1: Eliminación exitosa de cuenta sin transacciones pendientes
  Dado que no tengo transacciones pendientes
  Cuando accedo a "Configuración" > "Eliminar cuenta"
  Y confirmo escribiendo "ELIMINAR"
  Y hago clic en "Confirmar eliminación"
  Entonces mi cuenta se marca para eliminación
  Y recibo email de confirmación
  Y soy redirigido al login

Escenario 2: Bloqueo de eliminación por transacciones pendientes
  Dado que tengo un préstamo activo
  Cuando intento eliminar mi cuenta
  Entonces veo "No puedes eliminar tu cuenta con transacciones pendientes"
  Y se muestra lista de transacciones que debo resolver
```

---

## HU-008: Ver perfil público de otros usuarios

**ID:** HU-008  
**Área Funcional:** Autenticación y Gestión de Usuarios  
**Prioridad:** Should (Importante)  
**Sprint Recomendado:** Sprint 2  
**Dependencias:** HU-005  

### Historia de Usuario
Como **usuario autenticado**  
Quiero **ver el perfil público de otros usuarios**  
Para **conocer más información antes de realizar una transacción**

### Descripción Detallada
Al hacer clic en el nombre del propietario de un producto, se debe mostrar su perfil público que incluye: nombre, foto, biografía, fecha de registro, número de objetos publicados, y valoración promedio (si se implementa sistema de valoraciones). No se muestra información sensible como correo o teléfono completo.

### Criterios de Aceptación

```gherkin
Escenario 1: Visualización de perfil público
  Dado que estoy viendo un producto
  Cuando hago clic en el nombre del propietario
  Entonces veo su perfil público con: nombre, foto, biografía
  Y veo sus estadísticas públicas
  Y veo botón "Contactar"
  Pero no veo su correo completo ni teléfono completo

Escenario 2: Acceso a productos del usuario desde su perfil
  Dado que estoy en el perfil público de un usuario
  Cuando reviso la sección "Objetos disponibles"
  Entonces veo los productos activos que tiene publicados
  Y puedo hacer clic en ellos para ver detalles
```

---

# ÉPICA 2: Gestión de Productos

## HU-009: Publicar un nuevo producto

**ID:** HU-009  
**Área Funcional:** Gestión de Productos  
**Prioridad:** Must-be (Requisito básico)  
**Sprint Recomendado:** Sprint 2  
**Dependencias:** HU-003  

### Historia de Usuario
Como **usuario autenticado**  
Quiero **publicar un objeto que deseo prestar, alquilar o vender**  
Para **ponerlo a disposición de otros estudiantes**

### Descripción Detallada
El usuario accede a "Publicar objeto" desde el menú principal. Debe completar un formulario con: título (obligatorio, máx. 100 caracteres), descripción (obligatorio, máx. 500 caracteres), categoría (selección de lista: Electrónica, Libros, Material académico, Deporte, Otros), tipo de transacción (Préstamo gratuito, Alquiler, Venta), precio (si aplica), estado del producto (Nuevo, Como nuevo, Buen estado, Usado), y hasta 5 fotos. Campos opcionales: duración máxima del préstamo, condiciones especiales.

### Criterios de Aceptación

```gherkin
Escenario 1: Publicación exitosa de producto para préstamo
  Dado que soy usuario autenticado
  Cuando accedo a "Publicar objeto"
  Y completo título "Cámara Canon EOS"
  Y selecciono categoría "Electrónica"
  Y selecciono tipo "Préstamo gratuito"
  Y agrego descripción y 3 fotos
  Y hago clic en "Publicar"
  Entonces el producto se crea con estado "Disponible"
  Y veo mensaje "Producto publicado exitosamente"
  Y soy redirigido a la vista del producto

Escenario 2: Publicación de producto para alquiler con precio
  Dado que estoy creando una publicación
  Cuando selecciono tipo "Alquiler"
  Y ingreso precio "5€/día"
  Y completo los demás campos obligatorios
  Entonces el producto se publica con el precio visible
  Y aparece marcado como "Alquiler"

Escenario 3: Validación de campos obligatorios
  Dado que estoy en el formulario de publicación
  Cuando intento publicar sin completar título o descripción
  Entonces veo mensajes de error en campos faltantes
  Y la publicación no se crea

Escenario 4: Límite de fotos por publicación
  Dado que estoy agregando fotos
  Cuando intento subir más de 5 imágenes
  Entonces el sistema permite solo las primeras 5
  Y muestra "Máximo 5 fotos por publicación"
```

---

## HU-010: Ver detalle de un producto

**ID:** HU-010  
**Área Funcional:** Gestión de Productos  
**Prioridad:** Must-be (Requisito básico)  
**Sprint Recomendado:** Sprint 2  
**Dependencias:** HU-009  

### Historia de Usuario
Como **usuario autenticado**  
Quiero **ver todos los detalles de un producto publicado**  
Para **decidir si me interesa solicitarlo**

### Descripción Detallada
Al hacer clic en un producto desde cualquier listado, se debe mostrar una página de detalle que incluya: galería de fotos (con navegación), título, descripción completa, categoría, tipo de transacción, precio (si aplica), estado del producto, información del propietario (nombre, foto), fecha de publicación, disponibilidad actual, y botón "Solicitar" o "Contactar". Si el producto es del usuario actual, se muestran botones "Editar" y "Eliminar".

### Criterios de Aceptación

```gherkin
Escenario 1: Visualización completa de producto disponible
  Dado que navego por el catálogo
  Cuando hago clic en un producto
  Entonces veo página de detalle con toda la información
  Y veo galería de fotos navegable
  Y veo información del propietario
  Y veo estado "Disponible" en verde
  Y veo botón "Solicitar"

Escenario 2: Visualización de producto propio
  Dado que veo el detalle de un producto que publiqué
  Entonces no veo botón "Solicitar"
  Pero veo botones "Editar" y "Eliminar"
  Y veo lista de solicitudes recibidas

Escenario 3: Producto no disponible
  Dado que un producto está prestado actualmente
  Cuando accedo a su detalle
  Entonces veo estado "No disponible" en rojo
  Y el botón "Solicitar" está deshabilitado
  Y veo mensaje "Este objeto está actualmente prestado"
```

---

## HU-011: Editar producto publicado

**ID:** HU-011  
**Área Funcional:** Gestión de Productos  
**Prioridad:** Should (Importante)  
**Sprint Recomendado:** Sprint 2  
**Dependencias:** HU-009, HU-010  

### Historia de Usuario
Como **propietario de un producto publicado**  
Quiero **editar la información de mi publicación**  
Para **actualizar detalles, corregir errores o cambiar disponibilidad**

### Descripción Detallada
Desde la vista de detalle de su propio producto, el usuario puede hacer clic en "Editar" y modificar cualquier campo excepto el tipo de transacción (para evitar confusiones en solicitudes existentes). Puede agregar/eliminar fotos, actualizar precio, modificar descripción o marcar temporalmente como "No disponible".

### Criterios de Aceptación

```gherkin
Escenario 1: Edición exitosa de información
  Dado que publiqué un producto
  Cuando accedo a su detalle y hago clic en "Editar"
  Y modifico la descripción y el precio
  Y hago clic en "Guardar cambios"
  Entonces los cambios se reflejan inmediatamente
  Y veo "Producto actualizado exitosamente"

Escenario 2: Marcado temporal como no disponible
  Dado que estoy editando mi producto
  Cuando marco opción "Temporalmente no disponible"
  Y guardo cambios
  Entonces el producto se oculta de búsquedas
  Pero permanece en mi perfil
  Y puedo reactivarlo cuando quiera

Escenario 3: Restricción de cambio de tipo de transacción
  Dado que mi producto tiene solicitudes o transacciones activas
  Cuando intento editar el tipo de transacción
  Entonces ese campo aparece deshabilitado
  Y veo tooltip "No puedes cambiar el tipo con solicitudes activas"
```

---

## HU-012: Eliminar producto publicado

**ID:** HU-012  
**Área Funcional:** Gestión de Productos  
**Prioridad:** Should (Importante)  
**Sprint Recomendado:** Sprint 2  
**Dependencias:** HU-009, HU-010  

### Historia de Usuario
Como **propietario de un producto publicado**  
Quiero **eliminar mi publicación**  
Para **retirar el objeto de la plataforma cuando ya no esté disponible**

### Descripción Detallada
Desde la vista de detalle de su producto, el usuario puede hacer clic en "Eliminar". El sistema verifica que no existan transacciones activas. Se muestra diálogo de confirmación explicando que la acción es irreversible y se perderán las solicitudes pendientes. Si se confirma, el producto se elimina permanentemente.

### Criterios de Aceptación

```gherkin
Escenario 1: Eliminación exitosa sin transacciones activas
  Dado que publiqué un producto sin solicitudes pendientes
  Cuando hago clic en "Eliminar"
  Y confirmo en el diálogo "¿Estás seguro?"
  Entonces el producto se elimina permanentemente
  Y veo "Producto eliminado exitosamente"
  Y soy redirigido a "Mis publicaciones"

Escenario 2: Bloqueo de eliminación con transacción activa
  Dado que mi producto está actualmente prestado
  Cuando intento eliminarlo
  Entonces veo "No puedes eliminar un producto con transacción activa"
  Y el producto no se elimina

Escenario 3: Cancelación de eliminación
  Dado que hago clic en "Eliminar"
  Cuando veo el diálogo de confirmación
  Y hago clic en "Cancelar"
  Entonces el producto permanece sin cambios
  Y regreso a la vista de detalle
```

---

## HU-013: Ver mis productos publicados

**ID:** HU-013  
**Área Funcional:** Gestión de Productos  
**Prioridad:** Should (Importante)  
**Sprint Recomendado:** Sprint 2  
**Dependencias:** HU-009  

### Historia de Usuario
Como **usuario que ha publicado objetos**  
Quiero **ver todos mis productos en un solo lugar**  
Para **gestionar fácilmente mis publicaciones**

### Descripción Detallada
En "Mi perfil" existe una sección "Mis publicaciones" que muestra todos los productos del usuario organizados por estado: Disponibles, Prestados actualmente, No disponibles, y Vendidos/Cerrados. Para cada producto se muestra miniatura, título, categoría, estado y número de solicitudes pendientes. Se puede filtrar y ordenar.

### Criterios de Aceptación

```gherkin
Escenario 1: Visualización de mis publicaciones activas
  Dado que he publicado 3 productos disponibles
  Cuando accedo a "Mi perfil" > "Mis publicaciones"
  Entonces veo los 3 productos en la pestaña "Disponibles"
  Y cada uno muestra foto, título, estado y solicitudes

Escenario 2: Filtrado por estado de publicación
  Dado que tengo productos en diferentes estados
  Cuando selecciono pestaña "Prestados actualmente"
  Entonces veo solo productos con transacciones activas
  Y puedo hacer clic en ellos para ver detalles de la transacción

Escenario 3: Acceso rápido a edición desde listado
  Dado que estoy viendo mis publicaciones
  Cuando hago clic en "Editar" en un producto
  Entonces accedo directamente al formulario de edición
  Sin tener que abrir primero los detalles
```

---

## HU-014: Marcar producto como vendido/cerrado

**ID:** HU-014  
**Área Funcional:** Gestión de Productos  
**Prioridad:** Should (Importante)  
**Sprint Recomendado:** Sprint 3  
**Dependencias:** HU-009, HU-013  

### Historia de Usuario
Como **propietario de un producto de venta**  
Quiero **marcarlo como vendido**  
Para **que no aparezca más en búsquedas y quede registrado en mi historial**

### Descripción Detallada
Cuando un producto de venta ya fue vendido (dentro o fuera de la plataforma), el propietario puede marcarlo como "Vendido" desde su listado de publicaciones. El producto se archiva, desaparece de búsquedas pero permanece en el historial del usuario. Este cambio es irreversible.

### Criterios de Aceptación

```gherkin
Escenario 1: Marcar producto como vendido
  Dado que publiqué un objeto para venta
  Cuando accedo a "Mis publicaciones"
  Y hago clic en "Marcar como vendido"
  Y confirmo la acción
  Entonces el producto cambia a estado "Vendido"
  Y desaparece de búsquedas públicas
  Y se mueve a la sección "Vendidos/Cerrados"

Escenario 2: Irreversibilidad de marcar como vendido
  Dado que marqué un producto como vendido
  Cuando accedo a sus detalles
  Entonces no veo opción para "Reactivar"
  Y veo mensaje "Este producto fue marcado como vendido permanentemente"
```

---

## HU-015: Categorización de productos

**ID:** HU-015  
**Área Funcional:** Gestión de Productos  
**Prioridad:** Must-be (Requisito básico)  
**Sprint Recomendado:** Sprint 2  
**Dependencias:** HU-009  

### Historia de Usuario
Como **usuario que publica productos**  
Quiero **asignar una categoría a mis objetos**  
Para **que otros usuarios puedan encontrarlos más fácilmente**

### Descripción Detallada
Al crear o editar un producto, el usuario debe seleccionar obligatoriamente una categoría de una lista predefinida: Electrónica, Libros y Apuntes, Material Académico, Deportes y Fitness, Hogar y Decoración, Ropa y Accesorios, Otros. Estas categorías se usan para filtrado y navegación.

### Criterios de Aceptación

```gherkin
Escenario 1: Selección obligatoria de categoría
  Dado que estoy publicando un producto
  Cuando llego al campo "Categoría"
  Entonces veo un selector con las 7 categorías predefinidas
  Y debo seleccionar una antes de poder publicar

Escenario 2: Cambio de categoría al editar
  Dado que publiqué un producto en categoría "Otros"
  Cuando edito el producto
  Y cambio la categoría a "Electrónica"
  Y guardo cambios
  Entonces el producto aparece correctamente en búsquedas de Electrónica
  Y ya no aparece en "Otros"
```

---

# ÉPICA 3: Búsqueda y Navegación

## HU-016: Buscar productos por texto

**ID:** HU-016  
**Área Funcional:** Búsqueda y Navegación  
**Prioridad:** Must-be (Requisito básico)  
**Sprint Recomendado:** Sprint 2  
**Dependencias:** HU-009  

### Historia de Usuario
Como **usuario autenticado**  
Quiero **buscar productos mediante texto libre**  
Para **encontrar rápidamente objetos específicos que necesito**

### Descripción Detallada
En la página principal existe una barra de búsqueda visible donde el usuario puede escribir términos. El sistema busca coincidencias en título, descripción y categoría de productos. Los resultados se muestran en una cuadrícula con foto, título, categoría, tipo y precio (si aplica). Si no hay resultados, se muestra un mensaje claro con sugerencias.

### Criterios de Aceptación

```gherkin
Escenario 1: Búsqueda exitosa con resultados
  Dado que existen productos con "portátil" en su título o descripción
  Cuando escribo "portátil" en la barra de búsqueda
  Y presiono Enter o hago clic en buscar
  Entonces veo todos los productos que coinciden
  Y cada resultado muestra foto, título, tipo y propietario

Escenario 2: Búsqueda sin resultados
  Dado que busco "helicóptero"
  Y no existe ningún producto con ese término
  Cuando ejecuto la búsqueda
  Entonces veo mensaje "No se encontraron resultados para 'helicóptero'"
  Y veo sugerencias "Intenta con otros términos o explora por categorías"

Escenario 3: Búsqueda en tiempo real (opcional)
  Dado que estoy escribiendo en la barra de búsqueda
  Cuando escribo al menos 3 caracteres
  Entonces veo sugerencias de productos que coinciden
  Y puedo hacer clic en una sugerencia para ir directamente
```

---

## HU-017: Filtrar productos por categoría y tipo

**ID:** HU-017  
**Área Funcional:** Búsqueda y Navegación  
**Prioridad:** Should (Importante)  
**Sprint Recomendado:** Sprint 2  
**Dependencias:** HU-015, HU-016  

### Historia de Usuario
Como **usuario que busca productos**  
Quiero **aplicar filtros por categoría y tipo de transacción**  
Para **refinar mis resultados de búsqueda**

### Descripción Detallada
En la página de resultados de búsqueda o catálogo principal, existe un panel lateral con filtros: Categorías (checkboxes múltiples), Tipo de transacción (Préstamo/Alquiler/Venta, checkboxes múltiples), Rango de precio (para alquiler y venta), Disponibilidad (Solo disponibles). Los filtros se aplican en tiempo real sin recargar la página. El número de resultados actuales se muestra en todo momento.

### Criterios de Aceptación

```gherkin
Escenario 1: Filtrado por categoría única
  Dado que estoy en el catálogo de productos
  Cuando selecciono filtro "Electrónica"
  Entonces veo solo productos de esa categoría
  Y veo "23 resultados encontrados"

Escenario 2: Filtrado múltiple por categoría y tipo
  Dado que tengo resultados de búsqueda
  Cuando selecciono "Libros y Apuntes" y "Préstamo gratuito"
  Entonces veo solo productos de préstamo en esa categoría
  Y los demás productos se ocultan

Escenario 3: Limpiar filtros
  Dado que he aplicado varios filtros
  Cuando hago clic en "Limpiar filtros"
  Entonces se quitan todos los filtros activos
  Y veo nuevamente todos los resultados disponibles

Escenario 4: Filtrado por rango de precio
  Dado que filtro por tipo "Alquiler"
  Cuando ajusto el rango de precio a "0-10€"
  Entonces veo solo productos con precio en ese rango
```

---

## HU-018: Ordenar resultados de búsqueda

**ID:** HU-018  
**Área Funcional:** Búsqueda y Navegación  
**Prioridad:** Could (Deseable)  
**Sprint Recomendado:** Sprint 2  
**Dependencias:** HU-016  

### Historia de Usuario
Como **usuario navegando el catálogo**  
Quiero **ordenar los resultados**  
Para **ver primero los más relevantes según mis preferencias**

### Descripción Detallada
En la página de resultados existe un selector "Ordenar por" con opciones: Más recientes, Más antiguos, Precio: menor a mayor, Precio: mayor a menor, Más solicitados. El ordenamiento se aplica inmediatamente y se combina con los filtros activos.

### Criterios de Aceptación

```gherkin
Escenario 1: Ordenar por más recientes
  Dado que estoy viendo resultados
  Cuando selecciono "Ordenar por: Más recientes"
  Entonces los productos se reordenan
  Y veo primero los publicados recientemente

Escenario 2: Ordenar por precio ascendente
  Dado que filtro por tipo "Venta"
  Cuando selecciono "Ordenar por: Precio menor a mayor"
  Entonces los productos se ordenan de menor a mayor precio
  Y los productos sin precio (préstamos) aparecen al final
```

---

# ÉPICA 4: Gestión de Transacciones

## HU-019: Solicitar un producto

**ID:** HU-019  
**Área Funcional:** Gestión de Transacciones  
**Prioridad:** Must-be (Requisito básico)  
**Sprint Recomendado:** Sprint 3  
**Dependencias:** HU-010  

### Historia de Usuario
Como **usuario interesado en un producto**  
Quiero **enviar una solicitud al propietario**  
Para **iniciar el proceso de préstamo, alquiler o compra**

### Descripción Detallada
Desde la vista de detalle de un producto disponible, el usuario hace clic en "Solicitar". Se abre un formulario donde debe indicar: fechas deseadas (si es préstamo/alquiler), mensaje opcional para el propietario (máx. 300 caracteres), y confirmación de aceptar términos de uso responsable. La solicitud se envía al propietario quien puede aceptarla o rechazarla.

### Criterios de Aceptación

```gherkin
Escenario 1: Envío exitoso de solicitud de préstamo
  Dado que estoy viendo un producto de préstamo disponible
  Cuando hago clic en "Solicitar"
  Y especifico fechas "15/02/2026 - 20/02/2026"
  Y escribo mensaje "Lo necesito para un proyecto de clase"
  Y hago clic en "Enviar solicitud"
  Entonces la solicitud se envía al propietario
  Y veo "Solicitud enviada. El propietario recibirá una notificación"
  Y la solicitud aparece en "Mis solicitudes" con estado "Pendiente"

Escenario 2: Bloqueo de solicitud a producto propio
  Dado que estoy viendo un producto que yo publiqué
  Entonces no veo botón "Solicitar"
  Y veo mensaje "Este es tu producto"

Escenario 3: Validación de fechas para préstamo/alquiler
  Dado que estoy solicitando un producto de alquiler
  Cuando intento enviar sin especificar fechas
  Entonces veo "Debes especificar las fechas para este tipo de transacción"
  Y la solicitud no se envía

Escenario 4: Solicitud de compra sin fechas
  Dado que estoy solicitando un producto de venta
  Cuando completo el formulario sin fechas
  Entonces la solicitud se envía correctamente
  Porque no se requieren fechas para compra
```

---

## HU-020: Ver mis solicitudes enviadas

**ID:** HU-020  
**Área Funcional:** Gestión de Transacciones  
**Prioridad:** Must-be (Requisito básico)  
**Sprint Recomendado:** Sprint 3  
**Dependencias:** HU-019  

### Historia de Usuario
Como **usuario que ha enviado solicitudes**  
Quiero **ver el estado de todas mis solicitudes**  
Para **hacer seguimiento y saber cuáles fueron aceptadas o rechazadas**

### Descripción Detallada
En "Mi perfil" existe sección "Mis solicitudes" que lista todas las solicitudes enviadas con: foto del producto, título, propietario, fechas solicitadas, estado (Pendiente/Aceptada/Rechazada/Completada), fecha de solicitud. Se puede filtrar por estado y cancelar solicitudes pendientes.

### Criterios de Aceptación

```gherkin
Escenario 1: Visualización de solicitudes enviadas
  Dado que he enviado 3 solicitudes
  Cuando accedo a "Mi perfil" > "Mis solicitudes"
  Entonces veo las 3 solicitudes con su información
  Y cada una muestra el estado actual con código de color
  Y puedo hacer clic en cada una para ver detalles

Escenario 2: Cancelación de solicitud pendiente
  Dado que tengo una solicitud con estado "Pendiente"
  Cuando hago clic en "Cancelar solicitud"
  Y confirmo la acción
  Entonces la solicitud se cancela
  Y el propietario recibe notificación
  Y el estado cambia a "Cancelada"

Escenario 3: Restricción para cancelar solicitudes aceptadas
  Dado que tengo una solicitud ya "Aceptada"
  Cuando veo sus detalles
  Entonces no veo opción "Cancelar"
  Y veo mensaje "Contacta con el propietario si necesitas cancelar"
```

---

## HU-021: Gestionar solicitudes recibidas

**ID:** HU-021  
**Área Funcional:** Gestión de Transacciones  
**Prioridad:** Must-be (Requisito básico)  
**Sprint Recomendado:** Sprint 3  
**Dependencias:** HU-019  

### Historia de Usuario
Como **propietario de un producto**  
Quiero **revisar y gestionar las solicitudes que recibo**  
Para **decidir a quién prestar, alquilar o vender mis objetos**

### Descripción Detallada
En "Mis publicaciones", cada producto muestra un contador de solicitudes pendientes. Al hacer clic, se abre una lista con todas las solicitudes recibidas mostrando: solicitante (con enlace a perfil), fechas solicitadas, mensaje, fecha de solicitud. El propietario puede ver detalles del solicitante y elegir "Aceptar" o "Rechazar" con opción de agregar un mensaje.

### Criterios de Aceptación

```gherkin
Escenario 1: Visualización de solicitudes recibidas
  Dado que mi producto tiene 2 solicitudes pendientes
  Cuando accedo a "Mis publicaciones"
  Y hago clic en el producto (veo badge "2 solicitudes")
  Entonces veo lista de las 2 solicitudes
  Y cada una muestra información del solicitante

Escenario 2: Aceptación de solicitud
  Dado que estoy revisando una solicitud
  Cuando hago clic en "Ver perfil" del solicitante
  Y reviso su información
  Y hago clic en "Aceptar solicitud"
  Y opcionalmente agrego mensaje "Perfecto, nos vemos mañana"
  Entonces la solicitud cambia a "Aceptada"
  Y el solicitante recibe notificación
  Y se crea la transacción
  Y las demás solicitudes pendientes se rechazan automáticamente

Escenario 3: Rechazo de solicitud con mensaje
  Dado que estoy revisando una solicitud
  Cuando hago clic en "Rechazar"
  Y escribo motivo "Ya acepté otra solicitud, lo siento"
  Y confirmo
  Entonces la solicitud cambia a "Rechazada"
  Y el solicitante recibe notificación con el mensaje

Escenario 4: Rechazo automático de solicitudes al aceptar una
  Dado que tengo 3 solicitudes para el mismo producto
  Cuando acepto una de ellas
  Entonces las otras 2 se rechazan automáticamente
  Y los solicitantes reciben notificación "El propietario aceptó otra solicitud"
```

---

## HU-022: Confirmar inicio de transacción

**ID:** HU-022  
**Área Funcional:** Gestión de Transacciones  
**Prioridad:** Must-be (Requisito básico)  
**Sprint Recomendado:** Sprint 3  
**Dependencias:** HU-021  

### Historia de Usuario
Como **usuario que participó en una solicitud aceptada**  
Quiero **confirmar que la entrega del objeto se realizó**  
Para **registrar formalmente el inicio de la transacción**

### Descripción Detallada
Cuando una solicitud es aceptada, tanto propietario como solicitante reciben notificación de que la transacción está "Pendiente de entrega". Una vez se encuentran y entregan el objeto, ambos deben confirmar la entrega en la app. Solo cuando ambos confirman, la transacción pasa a estado "En curso" y se activan plazos de devolución.

### Criterios de Aceptación

```gherkin
Escenario 1: Confirmación de entrega por propietario
  Dado que acepté una solicitud de préstamo
  Y nos encontramos con el solicitante
  Cuando accedo a "Mis transacciones"
  Y selecciono la transacción "Pendiente de entrega"
  Y hago clic en "Confirmar que entregué el objeto"
  Entonces mi confirmación queda registrada
  Y veo "Esperando confirmación del receptor"

Escenario 2: Confirmación completa por ambas partes
  Dado que el propietario ya confirmó la entrega
  Cuando yo (solicitante) confirmo "Confirmo que recibí el objeto"
  Entonces la transacción pasa a estado "En curso"
  Y se activa el contador de tiempo hasta devolución
  Y ambos reciben notificación de confirmación

Escenario 3: Reporte de problema en entrega
  Dado que quedamos en encontrarnos pero no se presentó
  Cuando accedo a la transacción pendiente
  Y hago clic en "Reportar problema"
  Y describo la situación
  Entonces se genera un reporte
  Y se notifica al equipo de soporte (si existe)
```

---

## HU-023: Confirmar finalización de transacción

**ID:** HU-023  
**Área Funcional:** Gestión de Transacciones  
**Prioridad:** Must-be (Requisito básico)  
**Sprint Recomendado:** Sprint 3  
**Dependencias:** HU-022  

### Historia de Usuario
Como **usuario participante en una transacción activa**  
Quiero **confirmar que el objeto fue devuelto (préstamo/alquiler) o que se completó la compra**  
Para **cerrar formalmente la transacción**

### Descripción Detallada
Para préstamos y alquileres, al acercarse la fecha de devolución, ambas partes reciben recordatorio. Tras la devolución física, ambos deben confirmar en la app. Para ventas, el comprador confirma que recibió el objeto satisfactoriamente. Al completarse, el producto vuelve a estar disponible (préstamo/alquiler) o se marca como vendido.

### Criterios de Aceptación

```gherkin
Escenario 1: Devolución exitosa de préstamo
  Dado que tengo un préstamo activo que finaliza hoy
  Cuando devuelvo el objeto al propietario
  Y ambos confirmamos la devolución en la app
  Entonces la transacción cambia a "Completada"
  Y el objeto vuelve a estado "Disponible"
  Y ambos pueden evaluarse mutuamente (si existe sistema de valoración)

Escenario 2: Recordatorio automático de devolución próxima
  Dado que tengo un préstamo que vence en 1 día
  Cuando llega la fecha límite menos 24 horas
  Entonces recibo notificación "Recuerda devolver [producto] mañana"

Escenario 3: Retraso en devolución
  Dado que la fecha de devolución ya pasó
  Y no he confirmado la devolución
  Cuando accede el propietario a la transacción
  Entonces ve opción "Enviar recordatorio" o "Reportar retraso"

Escenario 4: Finalización de venta
  Dado que compré un producto
  Cuando confirmo "Recibí el producto y está conforme"
  Entonces la transacción se completa
  Y el producto se marca como "Vendido"
  Y ya no aparece en búsquedas
```

---

## HU-024: Ver mis transacciones activas

**ID:** HU-024  
**Área Funcional:** Gestión de Transacciones  
**Prioridad:** Should (Importante)  
**Sprint Recomendado:** Sprint 3  
**Dependencias:** HU-022  

### Historia de Usuario
Como **usuario con transacciones en curso**  
Quiero **ver todas las transacciones activas**  
Para **hacer seguimiento de objetos que presté o que tengo prestados**

### Descripción Detallada
En "Mi perfil" > "Transacciones activas" se muestran dos pestañas: "Prestado/Alquilado a mí" (objetos que tengo en mi poder) y "Prestado/Alquilado por mí" (objetos que presté a otros). Para cada transacción se muestra: foto del objeto, título, la otra persona involucrada, fechas, días restantes, estado, y botones de acción.

### Criterios de Aceptación

```gherkin
Escenario 1: Visualización de objetos que tengo prestados
  Dado que tengo un portátil prestado de otro usuario hasta el 20/02
  Cuando accedo a "Transacciones activas" > "Prestado a mí"
  Entonces veo el portátil con "Días restantes: 5"
  Y veo información del propietario
  Y veo botón "Contactar propietario"

Escenario 2: Visualización de objetos que presté a otros
  Dado que presté mi cámara a otro estudiante
  Cuando accedo a "Transacciones activas" > "Prestado por mí"
  Entonces veo la transacción de la cámara
  Y veo información del prestatario
  Y veo opción "Enviar recordatorio" si está próxima la devolución
```

---

# ÉPICA 5: Comunicación y Mensajería

## HU-025: Enviar mensaje a propietario de producto

**ID:** HU-025  
**Área Funcional:** Comunicación y Mensajería  
**Prioridad:** Should (Importante)  
**Sprint Recomendado:** Sprint 3  
**Dependencias:** HU-010  

### Historia de Usuario
Como **usuario interesado en un producto**  
Quiero **enviar un mensaje al propietario**  
Para **hacer preguntas antes de enviar una solicitud formal**

### Descripción Detallada
Desde la vista de detalle de un producto, existe botón "Contactar al propietario" que abre un formulario simple de mensaje. El mensaje se envía al propietario quien puede responder. Los mensajes quedan registrados en una bandeja de entrada básica. Se limita a 500 caracteres por mensaje.

### Criterios de Aceptación

```gherkin
Escenario 1: Envío exitoso de mensaje a propietario
  Dado que estoy viendo un producto de otro usuario
  Cuando hago clic en "Contactar al propietario"
  Y escribo "¿Está disponible este fin de semana?"
  Y hago clic en "Enviar"
  Entonces el mensaje se envía
  Y veo confirmación "Mensaje enviado"
  Y el propietario recibe notificación

Escenario 2: Respuesta del propietario
  Dado que recibí un mensaje sobre mi producto
  Cuando accedo a "Mensajes"
  Y veo el mensaje recibido
  Y hago clic en "Responder"
  Y escribo mi respuesta
  Entonces el solicitante recibe mi mensaje
  Y se crea un hilo de conversación

Escenario 3: Validación de longitud de mensaje
  Dado que estoy escribiendo un mensaje
  Cuando escribo más de 500 caracteres
  Entonces veo contador "500/500 caracteres"
  Y no puedo escribir más
```

---

## HU-026: Ver bandeja de mensajes

**ID:** HU-026  
**Área Funcional:** Comunicación y Mensajería  
**Prioridad:** Should (Importante)  
**Sprint Recomendado:** Sprint 3  
**Dependencias:** HU-025  

### Historia de Usuario
Como **usuario de la plataforma**  
Quiero **acceder a todos mis mensajes en un solo lugar**  
Para **gestionar mis conversaciones con otros usuarios**

### Descripción Detallada
Existe sección "Mensajes" accesible desde el menú principal. Muestra lista de conversaciones (cada una asociada a un producto específico) ordenadas por más reciente. Se indica si hay mensajes no leídos con badge numérico. Al hacer clic en una conversación, se abre el historial completo de mensajes y se puede responder.

### Criterios de Aceptación

```gherkin
Escenario 1: Visualización de conversaciones activas
  Dado que tengo conversaciones sobre 2 productos diferentes
  Cuando accedo a "Mensajes"
  Entonces veo lista de 2 conversaciones
  Y cada una muestra: foto del producto, otro usuario, último mensaje, fecha
  Y las no leídas tienen badge "Nuevo"

Escenario 2: Marcado automático de leído
  Dado que tengo mensajes no leídos
  Cuando abro una conversación
  Entonces los mensajes se marcan como leídos
  Y el badge desaparece

Escenario 3: Notificación de nuevo mensaje
  Dado que estoy navegando la plataforma
  Cuando recibo un nuevo mensaje
  Entonces veo badge en el icono de mensajes del menú
  Y opcionalmente recibo notificación en navegador (si está habilitado)
```

---

## HU-027: Notificaciones en la aplicación

**ID:** HU-027  
**Área Funcional:** Comunicación y Mensajería  
**Prioridad:** Should (Importante)  
**Sprint Recomendado:** Sprint 3  
**Dependencias:** HU-019, HU-021, HU-025  

### Historia de Usuario
Como **usuario de la plataforma**  
Quiero **recibir notificaciones de eventos importantes**  
Para **estar al tanto de solicitudes, mensajes y cambios de estado**

### Descripción Detallada
El sistema genera notificaciones internas (visibles en ícono de campana del menú) para eventos clave: nueva solicitud recibida, solicitud aceptada/rechazada, nuevo mensaje, recordatorio de devolución, transacción completada. Las notificaciones muestran: ícono del tipo de evento, mensaje descriptivo, tiempo transcurrido, y enlace a la acción relevante. Se marcan como leídas al hacer clic.

### Criterios de Aceptación

```gherkin
Escenario 1: Notificación de nueva solicitud recibida
  Dado que publiqué un producto
  Cuando alguien envía una solicitud
  Entonces recibo notificación "Nueva solicitud para [producto]"
  Y veo badge con número en el ícono de notificaciones
  Y puedo hacer clic para ir directamente a la solicitud

Escenario 2: Notificación de solicitud aceptada
  Dado que envié una solicitud
  Cuando el propietario la acepta
  Entonces recibo notificación "Tu solicitud de [producto] fue aceptada"
  Y al hacer clic veo detalles de cómo proceder con la entrega

Escenario 3: Centro de notificaciones
  Dado que tengo múltiples notificaciones
  Cuando hago clic en el ícono de campana
  Entonces veo lista de todas las notificaciones
  Y puedo filtrar por "No leídas" o ver "Todas"
  Y puedo marcar todas como leídas

Escenario 4: Desactivación de notificaciones
  Dado que estoy en configuración
  Cuando desactivo ciertos tipos de notificaciones
  Entonces dejo de recibirlas
  Pero las notificaciones críticas (como vencimientos) siguen activas
```

---

## HU-028: Notificaciones por correo electrónico

**ID:** HU-028  
**Área Funcional:** Comunicación y Mensajería  
**Prioridad:** Could (Deseable)  
**Sprint Recomendado:** Sprint 4  
**Dependencias:** HU-027  

### Historia de Usuario
Como **usuario de la plataforma**  
Quiero **recibir notificaciones importantes por correo electrónico**  
Para **estar informado incluso cuando no esté conectado a la app**

### Descripción Detallada
Para eventos críticos (solicitud aceptada, recordatorio de devolución en 24h, retraso en devolución), el sistema envía emails automáticos al correo universitario además de la notificación interna. El usuario puede configurar en ajustes qué tipos de eventos desea recibir por email.

### Criterios de Aceptación

```gherkin
Escenario 1: Recepción de email por solicitud aceptada
  Dado que mi solicitud fue aceptada
  Entonces recibo email en mi correo universitario
  Con asunto "Tu solicitud en UFV Share fue aceptada"
  Y contenido con detalles básicos y link a la plataforma

Escenario 2: Configuración de preferencias de email
  Dado que estoy en "Configuración" > "Notificaciones"
  Cuando desactivo "Enviar email por nuevos mensajes"
  Pero mantengo activo "Enviar email por recordatorios"
  Entonces solo recibo emails de recordatorios
  Y los mensajes solo generan notificación interna
```

---

# ÉPICA 6: Historial y Seguimiento

## HU-029: Ver historial completo de transacciones

**ID:** HU-029  
**Área Funcional:** Historial y Seguimiento  
**Prioridad:** Should (Importante)  
**Sprint Recomendado:** Sprint 4  
**Dependencias:** HU-023  

### Historia de Usuario
Como **usuario de la plataforma**  
Quiero **acceder al historial de todas mis transacciones completadas**  
Para **llevar un registro de préstamos, alquileres y compras realizadas**

### Descripción Detallada
En "Mi perfil" > "Historial" se muestra listado completo de transacciones completadas, organizadas cronológicamente. Se puede filtrar por: tipo (prestadas a mí / prestadas por mí / compras / ventas), rango de fechas, categoría de producto. Para cada transacción se muestra: producto, la otra persona, fechas, tipo, y resultado (completada exitosamente / con incidencias).

### Criterios de Aceptación

```gherkin
Escenario 1: Visualización de historial completo
  Dado que he completado 5 transacciones
  Cuando accedo a "Mi perfil" > "Historial"
  Entonces veo las 5 transacciones ordenadas por fecha reciente
  Y cada una muestra información resumida
  Y puedo hacer clic para ver detalles

Escenario 2: Filtrado de historial por tipo
  Dado que estoy en mi historial
  Cuando selecciono filtro "Prestado por mí"
  Entonces veo solo transacciones donde fui el propietario
  Y se ocultan las demás

Escenario 3: Búsqueda en historial
  Dado que tengo muchas transacciones históricas
  Cuando busco "cámara"
  Entonces veo solo transacciones relacionadas con cámaras
```

---

## HU-030: Ver estadísticas personales

**ID:** HU-030  
**Área Funcional:** Historial y Seguimiento  
**Prioridad:** Could (Deseable - Delighter)  
**Sprint Recomendado:** Sprint 4  
**Dependencias:** HU-029  

### Historia de Usuario
Como **usuario activo de la plataforma**  
Quiero **ver estadísticas de mi participación**  
Para **visualizar mi contribución a la comunidad y mi historial de uso**

### Descripción Detallada
En el panel de perfil se muestra sección "Mis estadísticas" con métricas: total de objetos prestados a otros, total de objetos tomados prestados, objetos vendidos, objetos comprados, transacciones completadas, ahorro estimado (suma de valores de objetos usados sin comprar), impacto ambiental estimado (objetos reutilizados). Se presenta de forma visual con gráficos simples.

### Criterios de Aceptación

```gherkin
Escenario 1: Visualización de estadísticas básicas
  Dado que tengo transacciones completadas
  Cuando accedo a "Mi perfil"
  Entonces veo tarjetas con mis estadísticas:
  - "15 objetos prestados"
  - "8 objetos tomados prestados"
  - "3 compras realizadas"
  - "Ahorro estimado: 450€"

Escenario 2: Detalle de métrica al hacer clic
  Dado que veo mi estadística "15 objetos prestados"
  Cuando hago clic en ella
  Entonces veo desglose por categoría
  Y gráfico de evolución temporal
```

---

# ÉPICA 7: Moderación y Accesibilidad

## HU-031: Reportar publicación inapropiada

**ID:** HU-031  
**Área Funcional:** Moderación y Accesibilidad  
**Prioridad:** Should (Importante)  
**Sprint Recomendado:** Sprint 4  
**Dependencias:** HU-010  

### Historia de Usuario
Como **usuario que encuentra contenido inapropiado**  
Quiero **reportar una publicación**  
Para **mantener la calidad y seguridad de la plataforma**

### Descripción Detallada
En la vista de detalle de cualquier producto, existe opción "Reportar" que abre formulario con motivos predefinidos: Contenido inapropiado, Spam, Información falsa, Precio abusivo, Objeto prohibido, Otro. Se puede agregar descripción adicional. Los reportes se almacenan y en versiones futuras pueden ser revisados por moderadores.

### Criterios de Aceptación

```gherkin
Escenario 1: Reporte exitoso de publicación
  Dado que estoy viendo un producto sospechoso
  Cuando hago clic en "Reportar"
  Y selecciono motivo "Información falsa"
  Y opcionalmente agrego descripción
  Y hago clic en "Enviar reporte"
  Entonces el reporte se registra
  Y veo "Gracias por tu reporte. Lo revisaremos pronto"

Escenario 2: Prevención de reportes abusivos
  Dado que ya reporté este producto
  Cuando intento reportarlo nuevamente
  Entonces veo "Ya has reportado esta publicación"
  Y no puedo enviar otro reporte
```

---

## HU-032: Reportar comportamiento de usuario

**ID:** HU-032  
**Área Funcional:** Moderación y Accesibilidad  
**Prioridad:** Should (Importante)  
**Sprint Recomendado:** Sprint 4  
**Dependencias:** HU-008  

### Historia de Usuario
Como **usuario que experimenta mal comportamiento de otro usuario**  
Quiero **reportar su conducta**  
Para **contribuir a un entorno seguro y respetuoso**

### Descripción Detallada
Desde el perfil de otro usuario o desde una transacción activa, existe opción "Reportar usuario" con motivos: No devolvió objeto, Hostigamiento, Lenguaje ofensivo, Suplantación de identidad, Otro. El reporte se asocia a la transacción o conversación específica cuando sea posible.

### Criterios de Aceptación

```gherkin
Escenario 1: Reporte de no devolución de objeto
  Dado que un usuario no devolvió mi objeto en la fecha acordada
  Y han pasado más de 3 días
  Cuando acceso a la transacción
  Y hago clic en "Reportar usuario"
  Y selecciono "No devolvió objeto"
  Y describo la situación
  Entonces el reporte se crea vinculado a la transacción
  Y queda registrado en el sistema

Escenario 2: Bloqueo de usuario (funcionalidad futura)
  Dado que reporté a un usuario
  Cuando el sistema valida el reporte (versión futura)
  Entonces puedo optar por bloquear al usuario
  Y sus publicaciones ya no me aparecen
```

---

## HU-033: Navegación completa por teclado

**ID:** HU-033  
**Área Funcional:** Moderación y Accesibilidad  
**Prioridad:** Must-be (Requisito básico - WCAG)  
**Sprint Recomendado:** Sprint 4  
**Dependencias:** Todas las HU de interfaz  

### Historia de Usuario
Como **usuario que no puede usar ratón o pantalla táctil**  
Quiero **navegar completamente la aplicación usando solo el teclado**  
Para **acceder a todas las funcionalidades de forma independiente**

### Descripción Detallada
Todos los elementos interactivos (botones, enlaces, formularios) deben ser accesibles mediante tabulación. El orden de tabulación debe ser lógico y visual. Se debe mostrar indicador claro de foco. Atajos de teclado para acciones comunes: Alt+B (buscar), Alt+P (publicar), Esc (cerrar modales).

### Criterios de Aceptación

```gherkin
Escenario 1: Navegación por todo el menú principal con Tab
  Dado que cargo la página principal
  Cuando presiono repetidamente Tab
  Entonces el foco se mueve secuencialmente por: logo, búsqueda, menú, productos
  Y el elemento con foco tiene borde visible claramente
  Y puedo activar cualquier elemento con Enter o Espacio

Escenario 2: Completar formulario solo con teclado
  Dado que accedo al formulario de publicación
  Cuando uso Tab para moverme entre campos
  Y uso flechas para seleccionar en dropdowns
  Y presiono Enter en "Publicar"
  Entonces el formulario se envía correctamente
  Sin necesidad de usar ratón en ningún momento

Escenario 3: Cerrar modales con Escape
  Dado que abrí un modal de detalles de producto
  Cuando presiono tecla Escape
  Entonces el modal se cierra
  Y el foco vuelve al elemento que lo abrió
```

---

## HU-034: Textos alternativos para imágenes

**ID:** HU-034  
**Área Funcional:** Moderación y Accesibilidad  
**Prioridad:** Must-be (Requisito básico - WCAG)  
**Sprint Recomendado:** Sprint 4  
**Dependencias:** HU de imágenes (009, 010, 011)  

### Historia de Usuario
Como **usuario con discapacidad visual que usa lector de pantalla**  
Quiero **que todas las imágenes tengan textos alternativos descriptivos**  
Para **comprender el contenido visual de la plataforma**

### Descripción Detallada
Todas las imágenes deben incluir atributo alt descriptivo. Para fotos de productos, se debe solicitar al usuario que agregue descripción breve al subir la imagen, o auto-generar basándose en el título y categoría. Imágenes decorativas usan alt="". Iconos informativos incluyen texto alternativo o uso de aria-label.

### Criterios de Aceptación

```gherkin
Escenario 1: Subida de imagen con descripción automática
  Dado que estoy publicando un producto "Portátil HP Pavilion"
  Cuando subo una foto del portátil
  Y no especifico descripción
  Entonces el sistema genera alt="Foto de Portátil HP Pavilion - Electrónica"
  Y el lector de pantalla lo anuncia correctamente

Escenario 2: Iconos con etiquetas accesibles
  Dado que uso lector de pantalla
  Cuando navego por los íconos de acciones
  Entonces cada ícono anuncia su función: "Editar publicación", "Eliminar", "Compartir"
  Y no solo "ícono" o "imagen"

Escenario 3: Validación de accesibilidad de imágenes
  Dado que he subido imágenes a mi publicación
  Cuando guardo la publicación
  Entonces el sistema valida que todas tienen texto alternativo
  Si alguna no lo tiene, sugiere agregarlo
```

---

## HU-035: Contraste de colores adecuado

**ID:** HU-035  
**Área Funcional:** Moderación y Accesibilidad  
**Prioridad:** Must-be (Requisito básico - WCAG)  
**Sprint Recomendado:** Sprint 4  
**Dependencias:** Todas las HU de interfaz  

### Historia de Usuario
Como **usuario con baja visión o daltonismo**  
Quiero **que todos los textos y elementos tengan suficiente contraste**  
Para **poder leer y usar la aplicación sin dificultad**

### Descripción Detallada
Todos los textos deben cumplir ratio de contraste mínimo 4.5:1 para texto normal y 3:1 para texto grande según WCAG AA. Los estados de error, éxito y advertencia no deben depender solo del color. Botones importantes deben tener contraste suficiente. Se debe validar con herramientas de contraste durante el desarrollo.

### Criterios de Aceptación

```gherkin
Escenario 1: Validación de contraste en textos
  Dado que analizo la página con herramienta de contraste
  Cuando reviso todos los textos visibles
  Entonces todos cumplen ratio mínimo 4.5:1
  Y ningún texto aparece con advertencia de contraste insuficiente

Escenario 2: Indicadores de estado no solo por color
  Dado que un producto está "Disponible" (verde)
  Entonces además del color verde
  Veo ícono ✓ y texto "Disponible"
  Para que usuarios con daltonismo también lo identifiquen

Escenario 3: Botones con contraste suficiente
  Dado que reviso todos los botones de la aplicación
  Entonces los botones primarios tienen contraste > 4.5:1
  Y los botones deshabilitados también son claramente distinguibles
```

---

## HU-036: Etiquetas y roles ARIA apropiados

**ID:** HU-036  
**Área Funcional:** Moderación y Accesibilidad  
**Prioridad:** Should (Importante - WCAG)  
**Sprint Recomendado:** Sprint 4  
**Dependencias:** Todas las HU de interfaz  

### Historia de Usuario
Como **usuario de tecnologías de asistencia**  
Quiero **que los componentes tengan roles y etiquetas semánticas correctas**  
Para **entender la estructura y función de cada elemento**

### Descripción Detallada
Usar elementos HTML semánticos correctos (<button>, <nav>, <main>, etc.). Cuando se usen divs interactivos, agregar roles ARIA apropiados. Los formularios deben tener <label> asociados. Regiones principales deben usar landmarks. Mensajes dinámicos (notificaciones) deben usar aria-live.

### Criterios de Aceptación

```gherkin
Escenario 1: Landmarks de navegación correctos
  Dado que uso lector de pantalla
  Cuando cargo cualquier página
  Entonces puedo navegar por landmarks: banner, navigation, main, complementary
  Y puedo saltar entre secciones rápidamente

Escenario 2: Formularios con labels asociados
  Dado que estoy en un formulario
  Cuando enfoco un campo con lector de pantalla
  Entonces escucho su etiqueta correcta
  "Título del producto, campo de texto"
  Y sé qué información debo ingresar

Escenario 3: Notificaciones dinámicas anunciadas
  Dado que envío un formulario exitosamente
  Cuando aparece mensaje "Producto publicado exitosamente"
  Entonces el lector de pantalla lo anuncia automáticamente
  Sin necesidad de navegar hasta el mensaje
```

---

## HU-037: Tamaños de fuente y zoom responsive

**ID:** HU-037  
**Área Funcional:** Moderación y Accesibilidad  
**Prioridad:** Should (Importante - WCAG)  
**Sprint Recomendado:** Sprint 4  
**Dependencias:** Todas las HU de interfaz  

### Historia de Usuario
Como **usuario con baja visión**  
Quiero **poder hacer zoom hasta 200% sin perder funcionalidad**  
Para **leer cómodamente todo el contenido**

### Descripción Detallada
La interfaz debe ser responsive y soportar zoom de navegador hasta 200% sin scroll horizontal. Usar unidades relativas (rem, em) en lugar de píxeles fijos. El contenido debe reflow correctamente. Tamaño de fuente base mínimo 16px. Permitir que usuarios cambien tamaño de fuente del navegador.

### Criterios de Aceptación

```gherkin
Escenario 1: Zoom al 200% sin pérdida de funcionalidad
  Dado que cargo la página principal
  Cuando hago zoom al 200% en el navegador
  Entonces todo el contenido permanece visible
  Y no aparece scroll horizontal
  Y todos los botones siguen siendo clicables
  Y puedo completar todas las tareas

Escenario 2: Rediseño responsive al aumentar fuente
  Dado que aumento el tamaño base de fuente en navegador
  Cuando recargo la página
  Entonces todos los textos se agrandan proporcionalmente
  Y el layout se adapta correctamente

Escenario 3: Controles táctiles suficientemente grandes
  Dado que uso la app en móvil
  Cuando veo botones y enlaces interactivos
  Entonces todos tienen mínimo 44x44px de área clicable
  Para facilitar el toque preciso
```

---

## HU-038: Documentación de accesibilidad

**ID:** HU-038  
**Área Funcional:** Moderación y Accesibilidad  
**Prioridad:** Could (Deseable)  
**Sprint Recomendado:** Sprint 4  
**Dependencias:** HU-033 a HU-037  

### Historia de Usuario
Como **usuario preocupado por la accesibilidad** o **como auditor**  
Quiero **acceder a documentación sobre las características de accesibilidad**  
Para **conocer el nivel de cumplimiento y cómo usar funciones accesibles**

### Descripción Detallada
Crear página "Accesibilidad" accesible desde el footer que documente: nivel de conformidad WCAG (objetivo: AA), funciones de accesibilidad implementadas, atajos de teclado disponibles, cómo reportar problemas de accesibilidad, y roadmap de mejoras futuras.

### Criterios de Aceptación

```gherkin
Escenario 1: Acceso a declaración de accesibilidad
  Dado que estoy en cualquier página
  Cuando hago scroll al footer
  Y hago clic en enlace "Accesibilidad"
  Entonces veo página con información detallada:
  - Nivel WCAG que cumplimos
  - Lista de funciones accesibles
  - Atajos de teclado
  - Contacto para reportar problemas

Escenario 2: Lista de atajos de teclado
  Dado que estoy en la página de accesibilidad
  Cuando reviso la sección "Atajos de teclado"
  Entonces veo tabla con todos los atajos:
  - Alt + B: Buscar
  - Alt + P: Publicar
  - Esc: Cerrar modal
  - Y puedo probarlos inmediatamente
```

---

# DISTRIBUCIÓN EN SPRINTS

## **Sprint 1: Fundamentos y Autenticación** (Semanas 1-3)

**Objetivo:** Establecer la arquitectura base y sistema completo de usuarios

**Historias incluidas:**
- HU-001: Registro de usuario ★ Must-be
- HU-002: Verificación de correo ★ Must-be
- HU-003: Inicio de sesión ★ Must-be
- HU-004: Recuperación de contraseña - Should
- HU-005: Ver y editar perfil - Should
- HU-006: Cerrar sesión ★ Must-be

**Entregables:**
- Backend: Arquitectura Spring Boot, modelo de datos inicial, seguridad JWT
- Frontend: Páginas de auth, perfil básico
- BD: Schema de usuarios y sesiones
- Tests: TDD para auth y validaciones
- DevOps: CI/CD básico, deployment a entorno dev

**Story Points estimados:** 34

---

## **Sprint 2: Gestión de Productos y Búsqueda** (Semanas 4-6)

**Objetivo:** Permitir publicar, ver, buscar y filtrar productos

**Historias incluidas:**
- HU-008: Ver perfil público de otros - Should
- HU-009: Publicar producto ★ Must-be
- HU-010: Ver detalle de producto ★ Must-be
- HU-011: Editar producto - Should
- HU-012: Eliminar producto - Should
- HU-013: Ver mis productos - Should
- HU-015: Categorización ★ Must-be
- HU-016: Buscar por texto ★ Must-be
- HU-017: Filtrar productos - Should
- HU-018: Ordenar resultados - Could

**Entregables:**
- Backend: API completa de productos, búsqueda y filtros
- Frontend: Formularios de publicación, catálogo, búsqueda
- BD: Schema de products y categories
- Tests: TDD para CRUD de productos
- Integración: Subida de imágenes

**Story Points estimados:** 42

---

## **Sprint 3: Transacciones y Comunicación** (Semanas 7-9)

**Objetivo:** Habilitar el flujo completo de solicitud, transacción y mensajería

**Historias incluidas:**
- HU-014: Marcar como vendido - Should
- HU-019: Solicitar producto ★ Must-be
- HU-020: Ver mis solicitudes ★ Must-be
- HU-021: Gestionar solicitudes recibidas ★ Must-be
- HU-022: Confirmar inicio de transacción ★ Must-be
- HU-023: Confirmar finalización ★ Must-be
- HU-024: Ver transacciones activas - Should
- HU-025: Enviar mensaje a propietario - Should
- HU-026: Bandeja de mensajes - Should
- HU-027: Notificaciones en app - Should

**Entregables:**
- Backend: Lógica de solicitudes, transacciones, mensajería
- Frontend: Interfaces de solicitud, gestión, chat básico
- BD: Schema de requests, transactions, messages
- Tests: TDD para flujos de transacción
- Notificaciones: Sistema interno de notificaciones

**Story Points estimados:** 48

---

## **Sprint 4: Historial, Moderación y Accesibilidad** (Semanas 10-12)

**Objetivo:** Completar MVP con historial, reportes y cumplimiento WCAG

**Historias incluidas:**
- HU-007: Eliminar cuenta - Could
- HU-028: Notificaciones por email - Could
- HU-029: Historial de transacciones - Should
- HU-030: Estadísticas personales - Could (Delighter)
- HU-031: Reportar publicación - Should
- HU-032: Reportar usuario - Should
- HU-033: Navegación por teclado ★ Must-be (WCAG)
- HU-034: Textos alternativos ★ Must-be (WCAG)
- HU-035: Contraste de colores ★ Must-be (WCAG)
- HU-036: Roles ARIA - Should (WCAG)
- HU-037: Zoom responsive - Should (WCAG)
- HU-038: Documentación accesibilidad - Could

**Entregables:**
- Backend: Historial, reportes, emails
- Frontend: Refinamiento completo, accesibilidad WCAG AA
- Tests: Testing de accesibilidad automatizado
- Documentación: Manual de usuario, guía de accesibilidad
- DevOps: Deployment a producción

**Story Points estimados:** 38

---

# CRITERIOS DE PRIORIZACIÓN

## Modelo MoSCoW aplicado:

### **Must-be (Imprescindible):** 
Funcionalidades sin las cuales el producto no puede funcionar. Representan el núcleo del MVP.
- Total: 18 historias

### **Should (Importante):**
Funcionalidades importantes que agregan valor significativo y deben incluirse si es posible.
- Total: 15 historias

### **Could (Deseable):**
Mejoras que agregan valor pero pueden postergarse sin afectar funcionalidad core.
- Total: 4 historias

### **Delighter (Diferenciador):**
Funcionalidades sorpresa que exceden expectativas y deleitan usuarios.
- Total: 1 historia (HU-030: Estadísticas personales)

---

# DEFINICIÓN DE "DONE" (DoD)

Para que una historia de usuario se considere completada, debe cumplir:

✅ **Código:**
- Implementación completa backend y frontend
- Código revisado (code review) por al menos 1 compañero
- Sin code smells críticos
- Comentarios en código complejo

✅ **Tests:**
- Tests unitarios escritos antes (TDD)
- Cobertura mínima 80% en backend
- Tests de integración para endpoints API
- Tests E2E para flujos críticos (auth, transacciones)

✅ **Funcionalidad:**
- Todos los criterios de aceptación verificados
- Validaciones de inputs implementadas
- Manejo de errores apropiado
- Mensajes al usuario claros

✅ **Accesibilidad:**
- Navegable por teclado
- Contraste validado
- Textos alternativos en imágenes
- Tests de accesibilidad automatizados pasando

✅ **Documentación:**
- API documentada (si aplica)
- README actualizado
- Cambios en modelo de datos documentados

✅ **Integración:**
- Código mergeado a branch principal
- CI/CD pasando (build + tests)
- Desplegado en entorno de testing
- Sin conflictos de merge

✅ **Validación:**
- Demo en Sprint Review
- Aceptación del Product Owner
- Feedback incorporado

---

# DEPENDENCIAS TÉCNICAS CLAVE

## Infraestructura inicial (Sprint 1):
- Configuración Spring Boot + Maven
- Base de datos H2 (dev) / MySQL (prod)
- Configuración Astro + estructura frontend
- Azure DevOps configurado
- GitHub repositorio y ramas

## Integraciones (Sprints 2-3):
- Servicio de email (verificación, notificaciones)
- Almacenamiento de imágenes (local o cloud)
- Tokens JWT para autenticación
- WebSockets para notificaciones en tiempo real (opcional)

## Accesibilidad (Sprint 4):
- Herramientas de testing: axe, WAVE
- Librería de componentes accesibles
- Validación automática en CI

---

# RIESGOS Y MITIGACIONES

| Riesgo | Probabilidad | Impacto | Mitigación |
|--------|--------------|---------|------------|
| Complejidad del sistema de transacciones | Media | Alto | Diseño detallado en Sprint Planning 3, spikes técnicos |
| Sobrecarga en Sprint 3 | Alta | Medio | Mover HU-028 (emails) a Sprint 4 si es necesario |
| Cumplimiento WCAG más complejo de lo esperado | Media | Alto | Iniciar tareas de accesibilidad desde Sprint 1 |
| Tiempo insuficiente para todas las Could | Media | Bajo | Están marcadas como opcionales, priorizables post-MVP |
| Integración frontend-backend | Baja | Medio | Definir contratos API al inicio de cada sprint |

---

# MÉTRICAS DE ÉXITO DEL PRODUCTO

**Al finalizar los 4 sprints, el MVP debe permitir:**

✅ Que cualquier estudiante UFV se registre y autentique  
✅ Publicar al menos 50 productos en diferentes categorías  
✅ Buscar y filtrar productos efectivamente  
✅ Completar el flujo completo de préstamo: solicitud → aceptación → entrega → devolución  
✅ Enviar y recibir mensajes entre usuarios  
✅ Cumplir WCAG 2.1 nivel AA en navegación por teclado y contraste  
✅ Tener 0 bugs críticos en producción  
✅ Tiempo de carga < 3 segundos en página principal  
✅ Cobertura de tests > 80% en backend  

**Métricas de adopción (post-lanzamiento):**
- 100+ usuarios registrados en primer mes
- 10+ transacciones completadas semanalmente
- NPS (Net Promoter Score) > 40

---

# NOTAS FINALES

## Principios INVEST aplicados:

**I - Independent (Independiente):** Cada historia puede desarrollarse y probarse de forma independiente aunque hay dependencias técnicas claramente indicadas.

**N - Negotiable (Negociable):** Los detalles de implementación son flexibles, estos son acuerdos iniciales que pueden refinarse con el equipo.

**V - Valuable (Valiosa):** Cada historia aporta valor visible al usuario final o cumple requisito crítico (WCAG).

**E - Estimable (Estimable):** Historias tienen alcance claro que permite estimación en planning.

**S - Small (Pequeña):** Cada historia es completable en 1-3 días por desarrollador, permitiendo entregas incrementales.

**T - Testable (Testable):** Criterios de aceptación en Gherkin permiten crear tests automatizados directamente.

---

## Próximos pasos recomendados:

1. **Importar a Azure DevOps:** Crear Epics y Work Items para cada historia
2. **Sprint Planning 1:** Refinar estimaciones, asignar tareas, definir DoD específico del equipo
3. **Spike técnico inicial:** Validar arquitectura Spring Boot + Astro (2-3 días)
4. **Diseño de BD:** Esquema completo antes de iniciar Sprint 1
5. **Configuración CI/CD:** Pipelines automatizados desde día 1
6. **Daily Standups:** 15 min diarios, mismo horario
7. **Revisión semanal de progreso:** Ajustar backlog si es necesario

---

**Documento preparado por:** GitHub Copilot - AI Product Owner  
**Para proyecto:** UFV Share  
**Fecha:** 11 de febrero de 2026  
**Versión:** 1.0  

---

*Este backlog es un documento vivo que evolucionará con el proyecto. Se recomienda revisión y refinamiento continuo en cada sprint.*
