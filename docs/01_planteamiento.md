# 📖 Planteamiento de la Aplicación

## UFV Shares - Plataforma Integral para Estudiantes

### Descripción General

**UFV Shares** es una aplicación web desarrollada con Java y Spring Boot que permite a estudiantes universitarios organizar quedadas, compartir recursos y comparar precios de productos en Amazon. La plataforma nace como solución a las necesidades cotidianas de la comunidad estudiantil de la UFV, integrando múltiples servicios en un único ecosistema digital.

### Problemática a Resolver

Los estudiantes universitarios enfrentan diversos desafíos en su día a día:

1. **Descoordinación en encuentros**: Dificultad para organizar quedadas de estudio, deportivas o sociales en el campus
2. **Gastos elevados**: Falta de herramientas para comparar precios y encontrar ofertas en material universitario
3. **Recursos dispersos**: No existe una plataforma centralizada para compraventa entre estudiantes
4. **Desorientación espacial**: Los nuevos estudiantes tienen dificultades para ubicarse en el campus

### Solución Propuesta

UFV Shares integra cuatro módulos principales:

#### 1. 🗺️ Sistema de Quedadas con Geolocalización
- Creación de eventos con ubicación precisa en el campus
- Visualización en mapa interactivo de todas las quedadas activas
- Confirmación de asistencia y notificaciones automáticas
- Cálculo de rutas a pie entre ubicaciones

#### 2. 💰 Comparador de Precios Amazon
- Búsqueda inteligente de productos en Amazon
- Gráficas históricas de precios (powered by Keepa)
- Detección automática de ofertas
- Sistema de alertas cuando un producto alcanza precio objetivo
- Análisis de tendencias para determinar el mejor momento de compra

#### 3. 🛒 Marketplace Estudiantil
- Plataforma de compraventa entre estudiantes
- Categorización de productos (libros, tecnología, muebles, etc.)
- Sistema de mensajería integrado
- Ubicación opcional para facilitar entregas en campus

#### 4. 📍 Mapa Interactivo del Campus
- Geolocalización de edificios, aulas, cafeterías, bibliotecas
- Búsqueda de ubicaciones por nombre
- Cálculo de rutas optimizadas a pie
- Visualización de quedadas cercanas

### Propuesta de Valor

| Característica | Beneficio para el Usuario |
|----------------|---------------------------|
| **Todo en Uno** | Una sola plataforma para múltiples necesidades |
| **Ahorro Económico** | Compras informadas + marketplace sin intermediarios |
| **Comunidad** | Fomenta interacción y sentido de pertenencia |
| **Navegación Fácil** | Nunca más perderse en el campus |
| **Notificaciones Inteligentes** | Alertas relevantes sin spam |

### Modelo de Negocio

#### Fase 1: MVP (Mínimo Producto Viable)
- Plataforma gratuita para todos los estudiantes UFV
- Financiación mediante proyecto académico

#### Fase 2: Expansión (Futuro)
- Comisiones en marketplace (2-5% por transacción)
- Programa de afiliados Amazon (ingresos por clicks)
- Premium features:
  - Alertas ilimitadas de precios
  - Promoción de publicaciones en marketplace
  - Analytics personalizados
- Expansión a otras universidades

### Tecnologías Clave

#### Backend
- **Java 17** + **Spring Boot 3.2**: Framework robusto y escalable
- **PostgreSQL**: Base de datos relacional
- **Redis**: Sistema de caché para optimizar APIs externas

#### Frontend
- **Thymeleaf**: Templates server-side
- **Bootstrap 5**: Diseño responsive
- **Leaflet.js**: Mapas interactivos
- **Chart.js**: Gráficas de precios

#### APIs Externas
- **Google Maps API / OpenStreetMap**: Geolocalización
- **Amazon Product Advertising API**: Datos de productos
- **Keepa API**: Histórico de precios

#### DevOps
- **Azure App Service**: Hosting y despliegue
- **Azure DevOps**: CI/CD y gestión de proyecto
- **Git Flow**: Control de versiones

### Audiencia Objetivo

#### Perfil de Usuario Principal
- **Edad**: 18-25 años
- **Ocupación**: Estudiantes universitarios UFV
- **Necesidades**: 
  - Socialización en campus
  - Optimización de gastos
  - Navegación en campus
- **Comportamiento**: 
  - Uso intensivo de smartphone
  - Compras online frecuentes
  - Buscan comunidad estudiantil activa

#### Segmentos de Usuarios

1. **Nuevos Estudiantes (1º curso)**
   - Necesitan orientación en campus
   - Buscan hacer amigos
   - Primeras compras de material universitario

2. **Estudiantes Avanzados (2º-3º curso)**
   - Organizan grupos de estudio
   - Venden libros usados
   - Conocen el campus pero buscan ofertas

3. **Estudiantes Finales (4º curso)**
   - Venden todo antes de graduarse
   - Organizan eventos de despedida
   - Buscan maximizar valor de reventa

### Roadmap de Desarrollo

#### Sprint 1-2: Fundamentos (Semanas 1-4)
- ✅ Autenticación y gestión de usuarios
- ✅ Modelo de datos completo
- ✅ Integración con PostgreSQL

#### Sprint 3-4: Geolocalización (Semanas 5-8)
- ✅ Integración Google Maps API
- ✅ CRUD de quedadas
- ✅ Sistema de asistencias
- ✅ Mapa interactivo con Leaflet

#### Sprint 5-6: Comparador Amazon (Semanas 9-12)
- ✅ Integración Amazon API
- ✅ Integración Keepa API
- ✅ Gráficas de precios con Chart.js
- ✅ Sistema de alertas automáticas

#### Sprint 7-8: Marketplace (Semanas 13-16)
- 🔄 CRUD de productos
- 🔄 Sistema de categorías
- 🔄 Chat entre usuarios
- 🔄 Upload de imágenes

#### Sprint 9-10: Refinamiento (Semanas 17-20)
- 🔄 Optimización de rendimiento
- 🔄 Testing exhaustivo (coverage > 75%)
- 🔄 Documentación completa
- 🔄 Despliegue a producción

### Métricas de Éxito

#### KPIs Técnicos
- **Uptime**: > 99%
- **Tiempo de carga**: < 2 segundos
- **Cobertura de tests**: > 75%
- **Errores críticos**: 0

#### KPIs de Usuario
- **Usuarios activos**: > 100 en primer mes
- **Quedadas creadas**: > 50/semana
- **Productos publicados**: > 200 en marketplace
- **Alertas configuradas**: > 150
- **Tasa de retención**: > 60% mensual

### Riesgos y Mitigaciones

| Riesgo | Probabilidad | Impacto | Mitigación |
|--------|--------------|---------|------------|
| APIs externas caídas | Media | Alto | Sistema de caché robusto |
| Baja adopción inicial | Media | Alto | Campaña de marketing en campus |
| Costos de APIs | Baja | Medio | Límites de uso y optimización |
| Datos personales | Baja | Alto | Cumplimiento RGPD estricto |
| Escalabilidad | Media | Medio | Arquitectura cloud-native |

### Visión a Futuro

**Corto Plazo (6 meses)**
- Consolidar base de usuarios UFV
- Alcanzar 500+ usuarios activos
- Generar comunidad activa

**Medio Plazo (1 año)**
- Expandir a 3 universidades más en Madrid
- Introducir modelo freemium
- Desarrollar app móvil nativa

**Largo Plazo (2-3 años)**
- Presencia en 10+ universidades españolas
- 50,000+ usuarios activos
- Monetización sostenible
- Spin-off como startup

---

> **Nota**: Este planteamiento está alineado con el proyecto académico de Proyectos 2 (UFV), enfocándose en demostrar competencias técnicas en desarrollo full-stack, integración de APIs, arquitectura de software y metodologías ágiles.
