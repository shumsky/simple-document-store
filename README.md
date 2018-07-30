# simple-document-store

A simple key value store with term-based search capability over stored values (documents).  

## How to run
```
$ mvn clean spring-boot:run
```
## REST
Exposes a REST interface on port 8500 (overridable via `-Dserver.port=[port]`):
- `PUT /documents/{key}` - put a document
- `GET /documents/{key}` - get a document by key
- `GET /documents?tokens={token1},{token2},{tokenN}` - get all documents each of which contains all specified tokens

## CLI
A CLI is available in [document-store-cli](https://github.com/shumsky/simple-document-store/tree/master/document-store-cli) sub-project

## How to test
```
$ mvn clean test
```
