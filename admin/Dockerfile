FROM node:22-alpine AS build
WORKDIR /workspace

COPY package*.json ./
RUN npm ci

COPY angular.json tsconfig*.json ./
COPY src ./src
RUN npm run build

FROM nginx:1.27-alpine
COPY docker/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /workspace/dist/admin /usr/share/nginx/html

EXPOSE 80
