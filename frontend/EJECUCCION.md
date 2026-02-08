# Frontend (Astro)

Frontend en Astro, separado del backend Java.

## Ejecutar en desarrollo

```bash
cd frontend
npm install
npm run dev
```

Astro levanta por defecto en `http://localhost:4321`.

## Logo UFV (oficial)

Por derechos de marca/copyright, este repo incluye un **placeholder**.

Para usar el logo oficial:

- Coloca un archivo llamado `ufv-logo.svg` (o un SVG equivalente) en `frontend/public/ufv-logo.svg`.
- La UI lo intentará cargar y, si no existe, hará fallback automático a `ufv-badge.svg`.

## Rutas

- `/` landing
- `/auth` login/registro
- `/app` inicio: listado + filtros + publicar

## Conexión con el backend

El frontend llama al backend en `http://localhost:8080/api`.

Endpoints usados:

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/products?type=sale|rent`
- `POST /api/products` (requiere `Authorization: Bearer <token>`)
