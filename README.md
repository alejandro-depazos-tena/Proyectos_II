# 🎓 UFV Shares - Sistema de Gestión 

<div align="center">

![UFV Shares Banner](https://img.shields.io/badge/UFV%20Shares-Sistema%20de%20Gesti%C3%B3n-blueviolet?style=for-the-badge&logo=rocket)
[![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![Azure](https://img.shields.io/badge/Azure-App%20Service-0078D4?style=for-the-badge&logo=microsoft-azure)](https://azure.microsoft.com/)

**Proyecto 2 - Trabajo Final**

*Aplicación web para la gestión de con integración continua y despliegue automatizado*

[Ver Demo](https://ufvsharesmarioh.azurewebsites.net/) · [Reportar Bug](../../issues) · [Solicitar Feature](../../issues)

</div>

---

## 👥 Equipo de Desarrollo

| Nombre | Rol | GitHub |
|--------|-----|--------|
| **Mario Hernández Santos** | Tech Leader | [GitHub](https://github.com/mario-hernandez-santos) |
| **Nicolás Sanchidrián Infante** | Product Owner | [GitHub](https://github.com/nicolas-sanchidrian-infante) |
| **Jesús de Andrés de las Heras** | Frontend Developer | [GitHub](https://github.com/jesus-deandres-delasheras) |
| **Alejandro de Pazos Tena** | Scrum Master | [GitHub](https://github.com/alejandro-depazos-tena) |
| **Gonzalo de Lorenzo Vaquero** | QA Analist | [GitHub](https://github.com/gonzalo-delorenzo-vaquero) |

---

## 🎓 Información Académica

| Rol | Nombre | Información |
|-----|--------|-------------|
| 🏫 **Universidad** | UFV | Grado en Ingeniería Informática |
| 📚 **Asignatura** | Proyectos 2 | 3º Curso - 2025/2026 |
| 👨‍🏫 **Profesor y Stakeholder** | Roberto Rodríguez Galán | EPS |
| 📅 **Periodo** | Enero - Mayo 2026 | Proyecto Final |

---

## 🌟 Sobre el Proyecto

### 📖 Descripción

**UFV Shares** es una aplicación web desarrollada con Java y Spring Boot que permite a estudiantes universitarios organizar quedadas, compartir recursos y comparar precios de productos en Amazon. La plataforma integra:
- **Sistema de localización** para coordinar encuentros en el campus
- **Comparador de precios inteligente** con análisis histórico de Amazon
- **Gestión de grupos** y eventos académicos
- **Marketplace estudiantil** para compra/venta entre alumnos

### 🎯 Objetivos del Proyecto

Este proyecto implementa prácticas profesionales de desarrollo colaborativo, aplicando:

- ✅ **Desarrollo Ágil** con Azure DevOps (Backlog, Sprints, PRs)
- ✅ **Test-Driven Development (TDD)** para garantizar calidad del código
- ✅ **Integración Continua (CI)** con pipelines automatizados
- ✅ **Entrega Continua (CD)** con despliegue automático a Azure
- ✅ **Control de Versiones** con Git Flow y Pull Requests revisados
- ✅ **Gestión de Proyecto** con Features, PBIs y criterios QAS
 
---

## 🛠️ Stack Tecnológico

<div align="center">

| Categoría | Tecnologías |
|-----------|-------------|
| **Backend** | ![Java](https://img.shields.io/badge/-Java-007396?style=flat-square&logo=openjdk&logoColor=white) ![Spring Boot](https://img.shields.io/badge/-Spring%20Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white) |
| **Frontend** | ![HTML5](https://img.shields.io/badge/-HTML5-E34F26?style=flat-square&logo=html5&logoColor=white) ![CSS3](https://img.shields.io/badge/-CSS3-1572B6?style=flat-square&logo=css3) ![Bootstrap](https://img.shields.io/badge/-Bootstrap-7952B3?style=flat-square&logo=bootstrap&logoColor=white) |
| **Base de Datos** | ![PostgreSQL](https://img.shields.io/badge/-PostgreSQL-336791?style=flat-square&logo=postgresql&logoColor=white) ![Azure](https://img.shields.io/badge/-Azure%20Database-0078D4?style=flat-square&logo=microsoft-azure) |
| **DevOps** | ![Azure DevOps](https://img.shields.io/badge/-Azure%20DevOps-0078D7?style=flat-square&logo=azure-devops) ![Git](https://img.shields.io/badge/-Git-F05032?style=flat-square&logo=git&logoColor=white) |
| **Testing** | ![Coverage](https://img.shields.io/badge/-Coverage-2E7D32?style=flat-square) ![TDD](https://img.shields.io/badge/-TDD-FF6B6B?style=flat-square) |
| **Deployment** | ![Azure App Service](https://img.shields.io/badge/-App%20Service-0089D6?style=flat-square&logo=microsoft-azure) ![Gunicorn](https://img.shields.io/badge/-Gunicorn-499848?style=flat-square&logo=gunicorn&logoColor=white) |

</div>

---

## 📁 Estructura del Proyecto

```
.
├─ backend/                  # Java + Maven (API REST)
│  ├─ src/main/java/com/ufvshares/backend/
│  │  ├─ auth/               # Login, registro, sesión
│  │  ├─ producto/           # Gestión de productos
│  │  ├─ solicitud/          # Solicitudes de alquiler/compra
│  │  ├─ transaccion/        # Ciclo de transacciones
│  │  ├─ reporteusuario/     # Reportes de usuarios
│  │  └─ reporteproducto/    # Reportes de productos
│  └─ src/main/resources/
├─ frontend/                 # Astro (UI)
│  ├─ src/pages/             # Rutas y pantallas
│  ├─ src/components/        # Componentes reutilizables
│  ├─ src/store/             # Estado cliente (carrito/favoritos)
│  └─ src/styles/            # Estilos globales
├─ docs/                     # Memoria y documentación técnica
├─ bbdd/                     # Scripts SQL (tablas y datos)
└─ run-dev.ps1               # Arranque local rápido
```

---

## 🧑‍💻 Desarrollo local

### Arranque rápido (Windows)

- Opción 1 (VS Code): Ejecuta la tarea `dev:fullstack`.
	- Menú: `Terminal` → `Run Task...` → `dev:fullstack`
- Opción 2 (PowerShell):

```powershell
./run-dev.ps1
```

Abre la app en `http://localhost:4321`.

### Requisitos

- **Java JDK 21** (el backend usa Spring Boot 3.x y el `pom.xml` define Java 21)
- **Maven** (3.9+)
- **Node.js LTS** (18/20+) y **npm**

### Ejecutar en local (dos terminales)

#### 1) Backend (Spring Boot)

En una terminal:

```bash
cd backend
mvn test
mvn spring-boot:run
```

El backend levanta por defecto en `http://localhost:8080`.

Endpoint de prueba:

- `GET http://localhost:8080/api/hello`

#### 2) Frontend (Astro)

En otra terminal:

```bash
cd frontend
npm install
npm run dev
```

Astro levanta por defecto en `http://localhost:4321`.

El backend ya está configurado con CORS para aceptar peticiones desde `http://localhost:4321`.

### Problemas típicos

- **Puerto ocupado**:
	- Backend: cambia `backend/src/main/resources/application.properties` (`server.port=8080`) o ejecuta `mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081`.
	- Frontend: ejecuta `npm run dev -- --port 4322`.
- **Comandos no encontrados**:
	- `mvn`: instala Maven y asegúrate de tenerlo en el `PATH`.
	- `npm`: instala Node.js LTS (incluye npm).

---

## 🔄 Metodología de Desarrollo

### Git Flow Implementado

```mermaid

```

### Proceso de Pull Request

1. 🔀 Crear rama feature desde `master`
2. 💻 Desarrollar con TDD (tests primero)
3. ✅ Validar que tests pasan
4. 📝 Crear PR con enlace a PBIs
5. 👁️ Code Review por compañeros
6. ✔️ Merge tras aprobación

---

## 📊 Calidad y Testing

### Cobertura de Tests

```

```

> **📋 Política de Calidad**: El proyecto requiere un mínimo de **75% de cobertura** para desplegar a producción.
> Actualmente: **%**

### Pipeline CI/CD

- ✅ Build automatizado en cada push
- ✅ Tests unitarios ejecutados automáticamente
- ✅ Análisis de cobertura de código (mínimo 70%)
- ✅ Generación de reportes HTML de coverage
- ✅ Despliegue automático a Azure (solo si coverage ≥ 70%)
- ✅ Health checks post-deployment

---

## 🌐 Despliegue

La aplicación está desplegada en ****:

🔗 **URL Producción**: 

---

## 📈 Gestión del Proyecto

### Backlog Jerarquizado

- **Épicas**: Autenticación, Gestión de Productos, Solicitudes/Transacciones, Reportes y Accesibilidad.
- **Historias de Usuario**: redactadas en formato _Como/Quiero/Para_ con criterios verificables.
- **Tareas técnicas**: desglosadas por backend, frontend, testing y documentación.

### 🗓️ Plan de trabajo

- **Metodología**: SCRUM (4 sprints) + TDD + Git Flow.
- **Sprint 1**: base técnica (auth, perfil, productos iniciales y modelo de datos).
- **Sprint 2**: consolidación de gestión de productos y búsqueda/filtros.
- **Sprint 3**: flujo de solicitudes y transacciones end-to-end.
- **Sprint 4**: reportes, accesibilidad, estabilización y cierre.

### Estructura de cada sprint

1. **Sprint Planning**: selección de historias y estimación.
2. **Desarrollo**: implementación incremental con Pull Requests.
3. **Testing continuo**: validación funcional y técnica durante el sprint.
4. **Sprint Review**: demostración del incremento y feedback.
5. **Retrospective**: mejoras del proceso para la siguiente iteración.


### Criterios de Aceptación (QAS)

Todos los PBIs incluyen Quality Attribute Scenarios con formato:
- **Agente**: Quién interactúa
- **Estímulo**: Qué acción realiza
- **Artefacto**: Componente afectado
- **Condiciones**: Contexto de ejecución
- **Resultado**: Comportamiento esperado
- **Métrica**: Medida objetiva de éxito

---

## 🏆 Logros del Equipo

- ✅ **100%** de PTs completados y funcionales
- 🔄 **74%** de cobertura de tests (objetivo: 75%)
- ✅ Pipeline CI/CD completamente automatizado
- ✅ Política de calidad: despliegue solo con coverage ≥ 75%
- ✅ Backlog completo con trazabilidad total
- ✅ Todos los PRs revisados y aprobados

---

## 📜 Licencia

Este proyecto es parte de un trabajo académico para la asignatura de Proyectos 2.

---

## 🙏 Agradecimientos

- 👨‍🏫 A nuestro profesor por la guía y feedback continuo
- 🤝 A UFV Shares por confiar en nuestro equipo
- 🎓 A la Universidad por proporcionar los recursos necesarios
- 💻 A la comunidad Java, Spring Boot y Azure por la documentación

---

<div align="center">

**⭐ Si te gusta este proyecto, ¡déjanos una estrella! ⭐**

Desarrollado con 💜 por el Equipo ROAR S.L.

[⬆ Volver arriba](#-ufv-shares---sistema-de-gestión)

</div>
