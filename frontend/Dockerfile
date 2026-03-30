FROM node:20-alpine AS build
WORKDIR /app

ARG PUBLIC_API_BASE=http://localhost:8080/api
ENV PUBLIC_API_BASE=$PUBLIC_API_BASE

COPY package*.json ./
RUN npm ci

COPY . .
RUN npm run build

FROM nginx:1.27-alpine
WORKDIR /usr/share/nginx/html

COPY --from=build /app/dist ./

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
