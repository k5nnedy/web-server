# Simple Java WebServer

A lightweight HTTP server built in Java that serves static files over the web.

## Overview

This project implements a simple HTTP server from scratch in Java. It concurrently listens for incoming HTTP requests, parses them, and serves static files (HTML, CSS, images, etc.) from a configurable web root directory. This project was also multithreaded to handle many requests at once.

## Project Structure

```
web-server/
├── src/               # Java source files
├── WebRoot/           # Static files served by the server (HTML, CSS, etc.)
├── pom.xml            # Maven build configuration
└── Request.txt        # Sample HTTP request for testing
```

## Requirements

- Java 8 or higher
- Maven 3.x

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/k5nnedy/web-server.git
cd web-server
```

### Build

```bash
mvn clean package
```

### Run

```bash
mvn exec:java
```

Or run the compiled JAR directly:

```bash
java -jar target/web-server-*.jar
```

### Test

Once the server is running, open your browser and navigate to:

```
http://localhost:8080
```

You can also use the included `Request.txt` as a reference for valid HTTP request format, or send a request manually with `curl`:

```bash
curl http://localhost:8080
```

## How It Works

1. The server opens a `ServerSocket` on a configured port (default: `8080`).
2. For each incoming connection, it reads and parses the HTTP request line and headers.
3. It maps the requested path to a file in the `WebRoot/` directory.
4. If the file exists, it responds with `200 OK` and the file contents. Otherwise, it returns a `404 Not Found` response.

## Configuration

To change the port or web root directory, update the relevant constants in the server's main source file inside `src/`.

## License

This project is open source. Feel free to use and modify it for your own learning or projects.
