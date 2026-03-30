# 6. Sprint 2 - Validacion inicial con usuarios

## 6.1 Objetivo

En este Sprint 2 se realiza una primera validacion de usabilidad para obtener una senal temprana sobre la aceptacion de la aplicacion por parte de usuarios del campus.

## 6.2 Alcance de esta iteracion

Para no mezclar demasiadas tecnicas en la misma entrega, en esta fase solo se ejecuta:

- Encuesta de usabilidad con **System Usability Scale (SUS)**.

Quedan planificadas para siguientes sprints:

- Entrevistas con usuarios de prueba.
- Encuesta de satisfaccion con escala Likert.
- Validacion por usuarios expertos (evaluacion heuristica).

## 6.3 Diseno de la prueba SUS

- **Muestra**: 10 estudiantes UFV (datos simulados realistas).
- **Perfiles**: 6 usuarios frecuentes de apps de compraventa y 4 usuarios ocasionales.
- **Duracion por sesion**: entre 14 y 22 minutos.
- **Tareas realizadas antes de contestar SUS**:
  - Iniciar sesion o registrarse.
  - Publicar un producto basico.
  - Abrir una conversacion desde un producto.
  - Revisar perfil y favoritos.
- **Instrumento**: SUS estandar de 10 preguntas con escala 1-5.

### Formula de puntuacion SUS

Para cada usuario:

- Preguntas impares: valor - 1.
- Preguntas pares: 5 - valor.
- Suma total x 2.5.

Resultado final en escala de 0 a 100.

## 6.4 Resultados (datos simulados)

### 6.4.1 Puntuacion por participante

| Participante | Perfil | Tiempo (min) | Errores bloqueantes | SUS |
|---|---|---:|---:|---:|
| U01 | Frecuente | 16 | 0 | 82.5 |
| U02 | Ocasional | 21 | 1 | 67.5 |
| U03 | Frecuente | 15 | 0 | 80.0 |
| U04 | Frecuente | 14 | 0 | 87.5 |
| U05 | Ocasional | 22 | 1 | 65.0 |
| U06 | Frecuente | 17 | 0 | 77.5 |
| U07 | Ocasional | 20 | 1 | 70.0 |
| U08 | Frecuente | 18 | 0 | 75.0 |
| U09 | Frecuente | 16 | 0 | 85.0 |
| U10 | Ocasional | 19 | 0 | 72.5 |

### 6.4.2 Resumen estadistico

- **Media SUS**: 76.3
- **Mediana SUS**: 76.3
- **Minimo - maximo**: 65.0 - 87.5
- **Usuarios con SUS >= 68**: 8 de 10 (80%)

Interpretacion:

- Un valor superior a 68 se considera aceptable en SUS.
- El resultado global (76.3) indica una usabilidad buena para una version en evolucion.

### 6.4.3 Promedio por item SUS

| Item SUS | Media (1-5) | Lectura rapida |
|---|---:|---|
| P1: Usaria el sistema con frecuencia | 4.0 | Alta predisposicion de uso |
| P2: El sistema es innecesariamente complejo | 2.1 | Complejidad percibida baja-moderada |
| P3: El sistema es facil de usar | 4.2 | Facilidad alta |
| P4: Necesitaria ayuda tecnica para usarlo | 2.4 | Algunos usuarios ocasionales requieren apoyo inicial |
| P5: Funciones bien integradas | 3.9 | Integracion generalmente correcta |
| P6: Hay demasiada inconsistencia | 2.6 | Existen pequenas inconsistencias visuales/flujo |
| P7: La mayoria aprenderia rapido | 4.1 | Curva de aprendizaje favorable |
| P8: El sistema es engorroso de usar | 2.3 | Friccion baja en tareas principales |
| P9: Me senti seguro al usarlo | 3.8 | Confianza adecuada, mejorable |
| P10: Necesite aprender demasiado antes de usarlo | 2.2 | Inicio relativamente sencillo |

## 6.5 Hallazgos principales de la encuesta

- La navegacion principal se entiende bien en primer contacto.
- El flujo de publicar producto se percibe util, pero puede simplificarse en campos y mensajes de ayuda.
- El acceso al chat desde producto se valora positivamente.
- Los usuarios ocasionales muestran mas dudas en validaciones de formularios y estados de carga.

## 6.6 Incidencias detectadas (priorizacion inicial)

| ID | Incidencia observada | Impacto | Prioridad |
|---|---|---|---|
| VAL-S2-01 | Mensajes de validacion poco especificos en algunos formularios | Medio | Alta |
| VAL-S2-02 | Falta de feedback inmediato en una parte del flujo de publicacion | Medio | Media |
| VAL-S2-03 | Inconsistencia menor en textos de botones entre vistas | Bajo | Media |

## 6.7 Acciones propuestas para Sprint 3

- Refinar copy de errores y validaciones de formularios.
- Unificar estados de carga, exito y error en frontend.
- Ejecutar segunda ronda SUS tras correcciones.
- Ampliar validacion con entrevistas y escala Likert.

## 6.8 Nota metodologica

Estos resultados son **datos inventados con perfil realista** para validacion inicial de Sprint 2. Se han usado para establecer linea base y preparar la validacion completa en proximas iteraciones.