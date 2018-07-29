# document-store-cli

A CLI for simple-document-store.

## How to use
Build a binary:
```
$ mvn clean package
```
Make an alias for execution of the built jar:
```
$ alias dscli="java -jar target/document-store-cli-*.jar"
```
See the usage reference by executing `dscli`:
```
Usage:
  -put <key> <value> - put a document
  -get <key> - get a document by key
  -search <token1> <token2> ... <tokenN> - search document IDs by tokens
```
### Examples of commands:
Put a document:
```
$ dscli -put 123 "I love apples"
```
Get a document by key:
```
$ dscli -get 123
```
Search documents by tokens:
```
$ dscli -search apples
```
