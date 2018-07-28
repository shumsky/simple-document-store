# simple-document-store

A simple key value store with term-based search capability over stored values (documents).  

How to run:
```
$ mvn clean spring:run
```
Exposes a REST interface on port 8500 (overridable via `-Dserver.port=[port]`):
- `PUT /documents/{key}` - put a document
- `GET /documents/{key}` - get a document by key
- `GET /documents?tokens={token1},{token2}` - get all documents each of which contains all specified tokens

How to test:
```
$ mvn clean test
```
