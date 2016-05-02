# Mongo et Java (Licence Professionnelle SIL)

## Organisation du projet

Le but de ce projet est de vous permettre de prendre en main une librairie java de mapping de mongodb.
MongoDB fourni un driver java qui permet déjà de manipuler des bases [MongoDB](http://mongodb.github.io/mongo-java-driver/3.2/driver/getting-started/quick-tour/), mais le mapping objets Java en objets Json reste laborieux :
```java
// On se connecte à la base mongoDb
MongoClient mongoClient = new MongoClient();
// On récupère la db "mydb" (équivalent à 'use mydb' depuis le mongoShell)
MongoDatabase database = mongoClient.getDatabase("mydb");
// On récupère la collection "test" 
MongoCollection<Document> collection = database.getCollection("test");

Document doc = new Document("firstName", "Bobby")
               .append("lastName", "WOMACK")
               .append("address", 
                    new Document("city", "Los Angeles")
                    .append("street", "110th street")
                    .append("number", 90));
                    
collection.insertOne(doc);
```

Pour palier à ce problème de [nombreuses librairies](https://docs.mongodb.org/ecosystem/drivers/java/#pojo-mappers) ont vu le jour, permettant de faciliter le mapping des [POJO](https://fr.wikipedia.org/wiki/Plain_old_Java_object).
Toutes ses librairies se basent sur le driver java de mongo en apportant des facilités de manipulation des objets.
Nous allons nous concentrer sur la librairie [Jongo](http://jongo.org/).

### Les tests

Prérequis:
Java 8, MongoD (démarré)

Afin de vous guider, une série de tests a été écrit, nous les retrouvons dans la classe MovieRepositoryTest.
Lorsque vous lancez les tests, ils doivent tous échouer. 
Le but étant de faire passer tous les tests, vous devrez donc implémenter les méthodes de la classe MovieRepository nécessaires au bon fonctionnement des tests.
:warning: ne pas modifier le code des tests!

Pour lancer les tests, un clic droit sur le test vous permet d'accéder à l'action :

* "Run" sous IntelliJ,
* "Run as/ Junit test" sous eclipse.

#### Quelques explications

Voyons les premiers tests :

```java
@Test
public void itShouldFindAllMovies() throws Exception {
    //GIVEN
    movieRepository.loadFile("movies-db.json");
    
    //WHEN
    final Iterable<Movie> movies = movieRepository.find();
    
    //THEN
    assertThat(movies).hasSize(40);
}
```
Tous les tests sont séparés en 3 parties : 
 
* GIVEN : Il s'agit de la préparation des tests.
* WHEN : Il s'agit de l'action que l'on teste.
* THEN : Il s'agit de ce qui valide le test.

Dans ce cas, nous préparons le test en chargeant des données en base.
Afin de faciliter les choses, une méthode de chargement à partir fichier json a déjà été développé : ```java public void loadFile(String filePath) ```.
Cette méthode permet de charger des données en base depuis un fichier. Le fichier chargé se trouve dans les resources de test (```src/test/resources```).
Ensuite nous essayons de charger les données en utilisant la méthode ```java public Iterable<Movie> find() ```.
Et nous vérifions que nous avons bien 40 éléments dans notre base (nombre d'enregistrement dans le fichier movies-db.json).

Pour faire passer le test il suffit d'implémenter la méthode ```java public Iterable<Movie> find() ``` de la classe MovieRepository en utilisant la librairie Jongo.

#### Les assertions

Pour les tests, nous utilisons la librairie [assertJ](http://joel-costigliola.github.io/assertj/), qui permet d'avoir des assertion plus lisibles.
Nous utilisons notamment ```java extracting ```. Cette méthode permet d'extraire des champs d'une liste d'objet :

```java
assertThat(fellowshipOfTheRing).extracting("name")
                               .contains("Boromir", "Gandalf", "Frodo", "Legolas")
                               .doesNotContain("Sauron", "Elrond");
```

Lorsque nous extrayons plus d'un champ, assertJ manipule des Tuple :

```java
assertThat(fellowshipOfTheRing).extracting("name", "age", "race.name")
                               .contains(tuple("Boromir", 37, "Man"),
                                         tuple("Sam", 38, "Hobbit"),
                                         tuple("Legolas", 1000, "Elf"));
```
