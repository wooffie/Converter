# Converter Service

[![Tests](https://github.com/wooffie/converter/actions/workflows/gradle-tests.yml/badge.svg?branch=master)](https://github.com/wooffie/converter/actions/workflows/gradle-tests.yml)
(Develop
[![Tests](https://github.com/wooffie/converter/actions/workflows/gradle-tests.yml/badge.svg?branch=develop)](https://github.com/wooffie/converter/actions/workflows/gradle-tests.yml) )


Simple service which takes HTTP POST request with [XML, YAML or JSON] file and returns the response with [XML, YAML or JSON] translated format. Also you can beautify your file with this service!

## How to run

Clone repository:
``` console
$ git clone https://github.com/wooffie/Converter.git
```
Build with gradle:
``` console
$ ./gradlew build
```
Run jar file:
``` console
$ java -jar build/libs/converter-v1.0.jar
```

## How to run with docker
You can use [image from DockerHub](https://hub.docker.com/r/wooffie/converter)

Build docker image:
``` console
$ docker build -t wooffie/converter
```
Run docker image:
``` console
$ docker run -p 8080:8080 wooffie/converter
```
## How to use

When service is up, it can accept request.

GET `/api/help` - some bacis info about it

Data sends by HTTP-POST request using form-data. It have fields:
- string - string input to convert
- file - file to convert
- source - source format of your data
- target - the format we want to get, can list several separated by commas

For source/target formats use: `XML`, `YAML`, `JSON`

In normal case response structure:
- message = null
- result - json with key - format and value - converted data

In other cases message won't be null and say about your mistake.

## Examples
Using Postman choose POST-request with ip:8080/api/convert. Choose form-data and setup next:

key `string` : value `{"menu": {
  "id": "file",
  "value": "File",
  "popup": {
    "menuitem": [
      {"value": "New", "onclick": "CreateNewDoc()"},
      {"value": "Open", "onclick": "OpenDoc()"},
      {"value": "Close", "onclick": "CloseDoc()"}
    ]
  }
}}` 

key `source` : value `JSON`

key `target` : value `XML,YAML,JSON`

Result field in response will have:

JSON: 
``` JSON
{
  "menu" : {
    "id" : "file",
    "value" : "File",
    "popup" : {
      "menuitem" : [ {
        "value" : "New",
        "onclick" : "CreateNewDoc()"
      }, {
        "value" : "Open",
        "onclick" : "OpenDoc()"
      }, {
        "value" : "Close",
        "onclick" : "CloseDoc()"
      } ]
    }
  }
}
```
XML:
``` XML
<?xml version='1.1' encoding='UTF-8'?>
<menu>
  <id>file</id>
  <value>File</value>
  <popup>
    <menuitem>
      <value>New</value>
      <onclick>CreateNewDoc()</onclick>
    </menuitem>
    <menuitem>
      <value>Open</value>
      <onclick>OpenDoc()</onclick>
    </menuitem>
    <menuitem>
      <value>Close</value>
      <onclick>CloseDoc()</onclick>
    </menuitem>
  </popup>
</menu>
```
YAML:"
```YAML
---
menu:
  id: "file"
  value: "File"
  popup:
    menuitem:
    - value: "New"
      onclick: "CreateNewDoc()"
    - value: "Open"
      onclick: "OpenDoc()"
    - value: "Close"
      onclick: "CloseDoc()"

```

For files fill key `file` with your file and service will convert it to another format.

For file input result for this case similar.


## Lisense
We're [MIT](./LICENSE) licensed.
