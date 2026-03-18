# Vulnerable Go Service

Test REST API service with intentional SCA vulnerabilities for testing the SCA Fix Agent.

## Vulnerabilities

1. **gin-gonic/gin v1.7.0** - Multiple CVEs, breaking changes in v1.9.0+
2. **golang.org/x/crypto** - Old version with known issues
3. **gopkg.in/yaml.v2 v2.4.0** - CVE-2022-28948

## Build

```bash
go mod download
go build -o server .
```

## Run

```bash
./server
```

## Test

```bash
curl http://localhost:8080/health
```
