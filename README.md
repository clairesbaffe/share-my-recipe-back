# share-my-recipe-back
## About the project
This project is actually the back-end of [this](https://github.com/clairesbaffe/share-my-recipe-front) project. Share My Recipe is an application in which you can share your favorite recipes with all the fellow users.

## Built with
- Kotlin
- Gradle

## Start app

Before starting, create a `.env` file in the root of the project with the following content:

```bash
PORT = <Your port>
# example: 8080

DB_URL = <your db url>
# example: "jdbc:postgresql://localhost:5432/database"

DB_USER = <your db user>
DB_PASSWORD = <your db password>
SESSION_SECRET = <your secret session key>
```

To start the app, run the following command in a terminal:
```bash
docker-compose up --build -d
```

Once the application has started, start the front, open your browser and go to: http://localhost:1003.
