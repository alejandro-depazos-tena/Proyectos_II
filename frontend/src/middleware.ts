import { defineMiddleware } from 'astro:middleware';

export const onRequest = defineMiddleware((context, next) => {
  const pathname = context.url.pathname;
  const lowerPath = pathname.toLowerCase();

  // Normalize uppercase routes (e.g. /ADMIN -> /admin) to avoid 404 by casing.
  if (pathname !== lowerPath) {
    const redirectUrl = new URL(context.url.toString());
    redirectUrl.pathname = lowerPath;
    return Response.redirect(redirectUrl, 307);
  }

  return next();
});
