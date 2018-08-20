# Java Client
Sample Java REST client for Relativity

## Description
This client demonstrates the use of the `java.net.HttpURLConnection` class to talk to Relativity's REST endpoints. It was written using
Java 8 and no external packages or libraries. As a consequence, it doesn't parse or construct JSON--I've put the JSON payloads in their
own separate files from which the client reads at run time. 

### Samples
* [Samples#queryIdentifier](Samples.java#L64) - queries for the Document identifier
* [Samples#queryAllFieldsOnDoc](Samples.java#L73) - queries for all fields on the Document object
* [Samples#createField](Samples.java#L86) - creates a fixed-length text field on the Document object

## Prerequisites
* JDK/JRE 8

## How to Use
After cloning the repository and pointing your shell to the downloaded directory, run

```bash
>> javac Main.java
>> java Main 'https://my-instance' 'admin.user@example.com' 'SecretPassword!000' '1234567'
```
where `1234567` refers to your workspace artifact ID.
