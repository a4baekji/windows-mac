FROM node:22-slim

ENV USER dreamhack
ENV PORT 3000

RUN adduser --disabled-password -u 1337 $USER

WORKDIR /app
COPY . .

RUN apt-get update && \
    apt-get install -y gcc curl && \
    rm -rf /var/lib/apt/lists/*

RUN gcc -o /flag flag.c && \
    rm flag.c && \
    chmod 4111 /flag

RUN mv ./flag.txt /flag.txt && \
    chmod 400 /flag.txt
    
RUN npm install --production

EXPOSE $PORT
USER $USER
CMD ["node", "server.js"]