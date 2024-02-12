 Base de code pour l'UE : initiation au génie logiciel 2023-2024

Nom de l'équipe : E 

Membres de l'équipe :

Team 1
- VU Ngoc Linh
- BACHIR ABDOU Nana Hadiza 

Team 2
- EL HAMDAOUI Omar
- EL DERSHABY Mohamed

Liste des Commandes :

Pour voir tous les commandes :./gradlew run --args="AllMovies --help"

--genreIds=<genreIds>
Search by genre IDs
--maxVoteAverage=<maxVoteAverage>
Search by maximum vote average
--minVoteAverage=<minVoteAverage>
Search by minimum vote average
--outputFile=<outputFile>
Specify the output file for results
--partialTitle=<partialTitle>
Search by partial title
--releaseDate=<releaseDate>
Search by release date
--releaseDateAfter=<releaseDateAfter>
Search for movies released after a certain date
--releaseDateBefore=<releaseDateBefore>
Search for movies released before a certain date
--title=<title>   Search by title
--voteAverage=<voteAverage>
Search by exact vote average


Exemples de commandes
Tous les Movies :./gradlew run --args="list"
Mettre tous les Movies dans le fichier output.txt :./gradlew run --args="AllMovies --outputFile=output.txt"
Tous les Movies avec titre "Monkeyshines" et genre ='99'./gradlew run --args="AllMovies --partialTitle=Monkeyshines --genreIds=99"

for interactive mode in terminal:
we start by ./gradlew run --args="AllMovies"
And then you are just getting questions about what to do with the results












