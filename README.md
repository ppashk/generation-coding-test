# RatedPower backend challenge

With this challenge we want to see your skills and the quality of the code you write. We will consider the use of SOLID principles. You can use any tools or library that you consider relevant.

## Required tools
- Java 17

## Objectives

Using a fork of this repository, develop the necessary functionalities to have an application that allows the management of a single JSON file, kept in local storage. The JSON file represents an addition operation.

It will allow the following requests:

- **POST** http request that allows to upload a .json file, with the format of the `sample.json` provided (on the project root) and saves it.
- **PUT** http request that modifies the content of the stored .json file, changing the “valueX” and/or the “valueY” fields.
- **GET** http request that allows to download the stored document, including a “result” field.
- **DELETE** http request that deletes the document.

## JSON examples

### Sample POST file

```json
{
  "valueX": 8,
  "valueY": 5
}
```

### Sample GET file

```json
{
  "valueX": 8,
  "valueY": 5,
  "result": 13
} 
```