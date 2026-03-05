# Integración con la API de Google Books

## Descripción general

UFV Shares utiliza la **API pública de Google Books** para enriquecer la experiencia de publicación de anuncios. Cuando un usuario publica un producto de la categoría **Libros**, aparece un panel de búsqueda que consulta la API y rellena automáticamente el título y la descripción del anuncio con datos reales del libro.

---

## Configuración de la API Key (necesaria)

Sin clave, Google Books limita a ~100-200 peticiones por día por IP. Con clave gratuita el límite sube a **1 000 peticiones por día** en el nivel gratuito.

### Pasos para obtener la clave

1. Ve a [Google Cloud Console](https://console.cloud.google.com/)
2. Crea un proyecto (o usa uno existente)
3. Activa la **Books API**: *APIs y servicios → Biblioteca → busca "Books API" → Activar*
4. Crea una credencial: *APIs y servicios → Credenciales → Crear credencial → Clave de API*
5. (Recomendado) Restringe la clave a la Books API y a tu dominio

### Añadir la clave al proyecto

Crea el fichero `frontend/.env` (si no existe) y añade:

```env
PUBLIC_GOOGLE_BOOKS_KEY=AIzaSy...
```

> El prefijo `PUBLIC_` es obligatorio en Astro para exponer la variable al navegador.

---

## ¿Por qué Google Books?

| Criterio | Valoración |
|---|---|
| Coste | **Gratuita** para uso básico (hasta ~1 000 req./día por IP sin clave) |
| Integración | Llamada HTTP directa desde el navegador, **sin backend** |
| Valor aportado | Datos ricos: título, autores, editorial, año, ISBN, sinopsis, portada |
| Encaje con el proyecto | La categoría `LIBROS` es nativa del modelo de datos de UFV Shares |

---

## Endpoint utilizado

```
GET https://www.googleapis.com/books/v1/volumes?q={query}&maxResults=6&printType=books
```

### Parámetros

| Parámetro | Descripción |
|---|---|
| `q` | Término de búsqueda. Se construye como `isbn:<número>` si el input contiene solo dígitos, o como `intitle:<texto>` para búsqueda por título |
| `maxResults` | Máximo de resultados devueltos (6 en nuestra implementación) |
| `printType=books` | Filtra solo libros, excluyendo revistas |

### Ejemplo de request por ISBN

```
GET https://www.googleapis.com/books/v1/volumes?q=isbn:9788420412337&maxResults=6&printType=books
```

### Fragmento de respuesta relevante

```json
{
  "items": [
    {
      "volumeInfo": {
        "title": "El señor de los anillos",
        "subtitle": "La Comunidad del Anillo",
        "authors": ["J.R.R. Tolkien"],
        "publisher": "Minotauro",
        "publishedDate": "2001",
        "description": "La historia de un hobbit llamado Frodo...",
        "pageCount": 576,
        "industryIdentifiers": [
          { "type": "ISBN_13", "identifier": "9788420412337" }
        ],
        "imageLinks": {
          "smallThumbnail": "http://books.google.com/books/content?id=...",
          "thumbnail": "http://books.google.com/books/content?id=..."
        }
      }
    }
  ]
}
```

---

## Flujo de usuario

```
1. El usuario abre el formulario "Nuevo anuncio" en /profile
   └── Selecciona la categoría LIBROS
       └── Aparece el panel "Autocompletar con Google Books"

2. El usuario escribe un ISBN o el título del libro
   └── Pulsa "Buscar" (o presiona Enter)
       └── Se llama a Google Books API
           └── Se muestran hasta 6 resultados con portada, autor y año

3. El usuario selecciona un resultado
   └── El formulario se rellena automáticamente:
       ├── Título  →  "Nombre del libro - Autor principal"
       ├── Descripción  →  Autores, editorial, año, ISBN-13, sinopsis
       └── Se muestra un badge con el libro seleccionado

4. El usuario completa el resto de campos (precio, condición, fotos)
   └── Publica el anuncio normalmente
```

---

## Ubicación en el código

| Archivo | Sección | Descripción |
|---|---|---|
| `frontend/src/pages/profile.astro` | HTML · línea ~240 | Panel `#books-lookup` con input, botón y contenedores de resultados |
| `frontend/src/pages/profile.astro` | `<script>` · función `searchGoogleBooks()` | Construye la URL y ejecuta el `fetch` |
| `frontend/src/pages/profile.astro` | `<script>` · función `fillFormWithBook()` | Mapea los campos de la respuesta al formulario |
| `frontend/src/pages/profile.astro` | `<script>` · listener `categoriaSelect` | Muestra u oculta el panel según la categoría elegida |

---

## Lógica de detección ISBN vs. título

```js
// Si el input contiene solo dígitos (y posibles guiones), se trata como ISBN
const isIsbn = /^[\d\-]{9,17}$/.test(query.replace(/\s/g, ''));
const q = isIsbn
  ? 'isbn:' + query.replace(/[^\d]/g, '')   // prefijo isbn:
  : 'intitle:' + query;                      // prefijo intitle:
```

Esto permite que el usuario pueda pegar directamente el código de barras del libro.

---

## Campos que se rellenan automáticamente

| Campo del formulario | Campo de la API (`volumeInfo`) |
|---|---|
| **Título** | `title` + `subtitle` (si existe) + `authors[0]` |
| **Descripción** | `authors`, `publisher`, `publishedDate`, `pageCount`, `ISBN-13`, `description` (hasta 500 caracteres) |

> Los campos solo se rellenan si están **vacíos** en el momento de selección, para no sobreescribir lo que el usuario haya escrito manualmente.

---

## Seguridad y privacidad

- La llamada se realiza **directamente desde el navegador del usuario** (client-side fetch). No se envían datos del usuario a Google.
- No se utiliza API Key. La API es pública. En caso de superar el límite de uso gratuito por IP, Google devuelve HTTP 429 y el panel muestra un mensaje de error sin interrumpir el flujo de publicación.
- No se almacena ningún dato de Google Books en la base de datos. Solo se usa para pre-rellenar campos que el usuario puede editar libremente antes de publicar.

---

## Posibles mejoras futuras

- Guardar el `ISBN-13` como campo adicional en el modelo `Producto` del backend para mejorar la búsqueda y evitar duplicados.
- Añadir la **imagen de portada** como foto inicial del anuncio (convertirla a `File` y añadirla a `selectedFiles`).
- Ampliar la búsqueda con `langRestrict=es` para priorizar ediciones en español.
- Cachear resultados en `sessionStorage` para reducir llamadas repetidas.
