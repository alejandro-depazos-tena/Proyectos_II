 Metodología de Trabajo del Proyecto
======================================

> **Objetivo del documento:** definir de forma clara y común la metodología de desarrollo y la organización del trabajo del equipo, asegurando trazabilidad, calidad y control en todo el ciclo de vida del proyecto.

* * *

PARTE 1 · Metodología de Desarrollo
-----------------------------------

### 1.1 Repositorio y control de versiones

*   El proyecto se gestiona desde **un único repositorio Git** compartido por todo el equipo.
    
*   El control de versiones sigue estrictamente el modelo **Git Flow**.
    
*   Está prohibido trabajar directamente sobre la rama `main`.
    

### 1.2 Ramas del proyecto (Git Flow)

*   **main**
    *   Rama estable de producción.
        
    *   Solo contiene versiones validadas y desplegables.
        
*   **develop**
    *   Rama de integración del equipo.
        
*   **feature/***
    *   Desarrollo de nuevas funcionalidades.
        
    *   Una funcionalidad = una rama.
        
*   **bugfix/***
    *   Corrección de errores detectados.
        
*   **hotfix/***
    *   Correcciones urgentes en producción.
        

### 1.3 Flujo de desarrollo

1.  Creación de rama `feature/*` desde `develop`.
    
2.  Desarrollo de la funcionalidad con commits frecuentes y descriptivos.
    
3.  Ejecución local de tests antes de subir cambios.
    
4.  Pull Request hacia `develop`.
    
5.  Revisión por al menos un integrante del equipo.
    
6.  Resolución de conflictos y aprobación del PR.
    

### 1.4 Pipeline de integración y despliegue

*   Existe una **pipeline automática** asociada al repositorio.
    
*   La pipeline se ejecuta **al realizar commits en la rama `main`**.
    
*   La pipeline realiza, como mínimo:
    *   Compilación del proyecto.
        
    *   Ejecución de tests.
        
    *   Generación del artefacto desplegable.
        
    *   Despliegue automático en el entorno correspondiente.
        
*   No se permite subir código a `main` si la pipeline no finaliza correctamente.
    

* * *

PARTE 2 · Organización del Trabajo en Azure DevOps
--------------------------------------------------

### 2.1 Gestión de tareas

*   Todas las tareas del proyecto se gestionan mediante **Azure DevOps Boards**.
    
*   Cada tarea representa una unidad de trabajo clara y definida.
    
*   Toda tarea debe tener:
    *   Descripción clara
        
    *   Estimación inicial
        
    *   Responsable asignado
        

### 2.2 Estados de las tareas

Las tareas siguen el siguiente flujo de estados:
1.  **Activo**
    *   La tarea ha sido creada y está pendiente de comenzar.
        
    *   Se define la estimación inicial.
        
2.  **Doing**
    *   La tarea está en desarrollo.
        
    *   Se realizan commits asociados a la tarea.
        
    *   Se actualizan las **horas restantes**.
        
    *   Se comentan avances relevantes para exponer en la daily.
        
3.  **Pendiente PaT**
    *   El desarrollo ha finalizado.
        
    *   La tarea queda pendiente de **validación por el Team Leader (Mario)**.
        
    *   Se revisan cambios, enfoque y cumplimiento de requisitos.
        

        
5.  **Close**
    *   La tarea ha sido validada completamente.
        
    *   Se despliega en **producción**.
        
    *   La tarea se da por cerrada.
        

### 2.3 Normas de trabajo con tareas

*   Toda tarea debe tener commits asociados.
    
*   Los comentarios deben reflejar el progreso real del trabajo.
    
*   Las horas restantes deben actualizarse de forma honesta y continua.
    
*   No se puede pasar una tarea a **Pre** sin aprobación del Team Leader.
    
*   No se puede cerrar una tarea sin haber pasado por preproducción.
    

* * *

3. Compromiso del equipo
------------------------

> _Todos los integrantes del equipo se comprometen a seguir esta metodología de trabajo, respetando los flujos definidos tanto a nivel técnico como organizativo, con el objetivo de garantizar la calidad del producto final, la coordinación del equipo y el correcto cumplimiento de los plazos establecidos._