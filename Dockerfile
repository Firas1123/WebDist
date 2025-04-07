# Utilise l'image de base Java 17
FROM openjdk:17

# Création du répertoire de travail
WORKDIR /app
COPY .env .env

# Copie du fichier JAR généré
ADD target/Stage-0.0.1-SNAPSHOT.jar Stage.jar
# Exposition du port (adapter si besoin)
EXPOSE 8888

# Commande d’exécution du JAR
ENTRYPOINT ["java", "-jar", "Stage.jar"]
