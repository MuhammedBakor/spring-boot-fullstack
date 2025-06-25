#FROM node:24-alpine
#ARG api_base_url
#WORKDIR /app
#COPY package*.json .
#RUN npm i --silent
#COPY . .
#RUN echo "VITE_API_BASE_URL=${api_base_url}" > .env
#EXPOSE 5173
#CMD ["npm", "run", "dev"]
#
#to build the frontend image
#docker build . -t muhammedbakur/customer-management-react --build-arg api_base_url=https://customer-management-api-3y8d.onrender.com

FROM node:24-alpine

# 👇 نعرّف متغير الـ API Base URL
ARG api_base_url

WORKDIR /app

COPY package*.json ./
RUN npm install --silent

COPY . .

# 👇 نضع متغير البيئة في .env
RUN echo "VITE_API_BASE_URL=${api_base_url}" > .env

# 👇 نجهز نسخة إنتاج
RUN npm run build

# 👇 نثبت سيرفر لتشغيل build النهائي
RUN npm install -g serve

# 👇 نستخدم المنفذ 80 (المطلوب في Render)
EXPOSE 80

# 👇 نشغل السيرفر بدل npm run dev
CMD ["serve", "-s", "dist", "-l", "80"]
