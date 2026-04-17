FROM maven:3.9.9-eclipse-temurin-21 AS backend-build
WORKDIR /build/backend

COPY backend/pom.xml ./pom.xml
COPY backend/src ./src
RUN mvn -q -DskipTests package

FROM node:20-alpine AS frontend-build
WORKDIR /build/frontend

ARG PUBLIC_API_BASE=/api
ENV PUBLIC_API_BASE=${PUBLIC_API_BASE}

COPY frontend/package*.json ./
RUN npm ci
COPY frontend/. ./
RUN npm run build

FROM eclipse-temurin:21-jre-jammy

RUN apt-get update \
  && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
     nginx \
    curl \
     ca-certificates \
  && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=backend-build /build/backend/target/*.jar /app/app.jar
COPY --from=frontend-build /build/frontend/dist /usr/share/nginx/html
COPY deploy/single/nginx.conf /etc/nginx/sites-available/default
COPY deploy/single/entrypoint.sh /usr/local/bin/entrypoint.sh

RUN chmod +x /usr/local/bin/entrypoint.sh \
  && mkdir -p /data/uploads

ENV APP_FRONTEND_URL=https://ufvshares.onrender.com
ENV APP_API_URL=https://ufvshares.onrender.com/api
ENV APP_UPLOAD_DIR=/data/uploads

VOLUME ["/data"]
EXPOSE 80

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
