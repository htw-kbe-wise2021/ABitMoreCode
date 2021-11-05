# Beleg 2

Bearbeitsungszeit: 3 Wochen

## Kontext

In Beleg 1 haben sie ihre ersten Schritte mit Spring Boot gemacht. Dabei haben sie einen Dienst entwickelt, der die Informationen aus einer Datei lädt. 

In diesem Beleg bauen sie darauf auf und nutzen eine Datenbank. Zudem erweitern sie ihren Dienst um weitere Funktionalitäten und implementieren Tests.



## Aufgaben

### Von lokaler Datei zur Datenbank

1. Erstellen Sie eine relationale Datenbank. Sie können Cloud-Anbieter wie Google Cloud, Amazon Web Sevices oder Herku oder HTW-Dienste wie studiserver oder db1 verwenden. Nutzen sie die Datenbank um ihre Songs zu persistieren. ⭐ 

2. In der Datenbank muss jedes Lied einen Titel besitzen. Passen Sie Ihre Datenbank und Endpunkte entsprechend an. Verwenden Sie bei Bedarf einen Schema-Validator. Achten Sie darauf, dass Ihr Dienst keine 500 Statuscodes zurückgibt. ⭐

3. (Bonus) Das Repository enthält nicht die Anmeldeinformationen Ihrer Datenbank. Sie können das Passwort aus der Umgebungsvariable lösen. 

   - (Optional) Um einen weiteren Sicherheitsschicht zu besitzen, können sie beispielsweise `git-crypt` nutzen um Dateien zu verschlüsseln oder `Jasypt` um das Passwort zu verschlüsseln. 

   Dokumentieren sie zusätzlich in der `README.md` wie ihr Service korrekt gestartet werden kann. ⭐

4. (Optional/Empfehlung) Nutzen sie zum lokalen Testen ihrer Anwendung eine In-Memory-Datenbank, welche beim Start mit Dummy-Daten initialisiert wird.⭐

### XML als Ausgabeformat

- Beide GET-Anfragen sollten in der Lage sein, je nach `Accept`-Header JSON oder XML auszugeben.⭐ Tipps: Es gibt Mapper, die in der Lage sind, ein Objekt in XML zu serialisieren. Sie müssen dies als weitere Abhängigkeit hinzufügen.

### Integrationstests für Song Endpoint

- Implementieren Sie Tests für alle Songs-Endpunkte. Sie können Ihre Postman-Tests als Vorlage verwenden und sie in Java-Code übersetzen.
  - Tests für GET all ⭐
  - Tests für GET by id ⭐
  - Tests für POST ⭐
  - Tests für DELETE ⭐
  - (Bonus) Tests für PUT ⭐
- In den Tests werden nicht die Daten in der "Production"-Datenbank verwendet.⭐

### Songlist Endpoint

1. Erstellen sie die Datenbank Tabelle und entsprechendes Java-Objekt für die Songliste. Eine Songlist hat folgende Eigenschaften⭐:

    ```
    id: (Entweder numerisch oder Hash-String)
    name: der Name
    songs: eine Liste von Songs
    ```
    
2. Implementieren sie folgende GET-Endpunkt⭐:

   ```http
   GET /TEAMNAME/rest/songLists/SONGID
   Accept: application/json oder application/xml
   ```

   welcher die Songliste mit der ID `SONGID ` zurückgibt. Der Endpoint kann JSON oder XML zurückgeben.

3. Implementieren sie folgende GET-Endpunkt⭐:

   ```http
   GET /TEAMNAME/rest/songLists/
   Accept: application/json oder application/xml
   ```
   
   welcher alle Songlisten in JSON oder XML zurückgibt.

4. Implementieren sie folgende POST-Endpunkt⭐:

   ```http
   POST /TEAMNAME/rest/songLists/
   Content-Type: application/json
   ```

   welcher folgenden Beispiel JSON-Payload erwartet:

   ```json
   {
    "name": "SongList123",
    "songs": [
       {
          "id": 5,
          "title": "We Built This City",
          "artist": "Starship",
          "label": "Grunt/RCA",
          "released": 1985
       },
       {
          "id": 4,
          "title": "Sussudio",
          "artist": "Phil Collins",
          "label": "Virgin",
          "released": 1985
       }
    ]
    }
   ```
   
   Alle Songs in der Payload der Anfrage müssen in der DB existieren. Falls einer der Songs nicht existiert, können Sie einfachheitshalber eine 400 (BAD_REQUEST) zurückschicken.
   
   Songlisten, die gepostet werden, enthalten keine SongList-Ids, da diese von Ihrem Service oder der DB vergeben werden. Wenn eine Songlist erfolgreich angelegt wurde, schickt Ihr Service die URL mit neuer Songlist-Id im Location-Header zurück.
   
5. Implementieren sie folgende DELETE-Endpunkt⭐:

   ```http
   POST /TEAMNAME/rest/songLists/SONG_ID
   Content-Type: application/json
   ```

   der die Songlist mit der ID `SONG_ID` löscht.

6. (Bonus) Implementieren sie folgende PUT-Endpunkt⭐:
    ```http
   POST /TEAMNAME/rest/songLists/SONG_ID
   Content-Type: application/json
   ```

    welcher folgenden Beispiel JSON-Payload erwartet:
   
    ```json
    {
     "id": SONG_ID
     "name": "SongList123",
     "songs": [
        {
           "id": 5,
           "title": "We Built This City",
           "artist": "Starship",
           "label": "Grunt/RCA",
           "released": 1985
        },
        {
           "id": 4,
           "title": "Sussudio",
           "artist": "Phil Collins",
           "label": "Virgin",
           "released": 1985
        }
     ]
     }
    ```
   
    Alle Lieder im Payload der Anfrage müssen in der DB vorhanden sein. Ebenso muss die ID der Songliste im Payload mit der ID in der URI übereinstimmen. Wenn eine der Bedingungen nicht erfüllt ist, können Sie der Einfachheit halber eine 400 (BAD_REQUEST) zurückgeben. 
   
    Wenn eine Songlist erfolgreich aktuallisiert wurde, schickt Ihr Service den entsprechenden Statuscode zurück.
   
7. Implementieren Sie Tests für alle Songlist-Endpunkte.

   - Tests für GET all ⭐
   - Tests für GET by id ⭐
   - Tests für POST ⭐
   - Tests für DELETE ⭐
   - (Bonus) Tests für PUT ⭐

8. Ihr Dienst behandelt inkorrekten Client-Requests entsprechend RFC 2616, Section 10.04: https://tools.ietf.org/html/rfc2616#section-10.4. Beispielsweise sollen HTTP-Methoden, die Ihr Service nicht anbietet mit dem Statuscode 405 beantwortet werden. ⭐

9. (Bonus) Wie Sie vielleicht bemerkt haben, gibt es für jedes POJO eine Menge Boilerplate-Code (Getter, Setter, Konstruktor, ....). Um dies zu reduzieren, integrieren Sie ein Werkzeug wie Lombok in Ihr Projekt. ⭐
