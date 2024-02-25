# GitHub Repositories Finder
GitHub Repository Finder is an application thats lets users find detailed information about GitHub repositories belonging to a specific user. With this application, users can retrieve repository names, owner logins, branch names, and the last commit SHA for each branch. It is written in Java using Spring Boot framework and it's WebFlux module to provide full reactivity.

To start server run in terminal: `mvn spring-boot:run`. Server should start on  `http://localhost:8080`.

To test application send POST requst to `http://localhost:8080/api/v1/github/repos` with `Content-Type: application/json` header and body like below:
```json
{
  "username": "example-username"
}
```
If GitHub user with given username exists application should return list with detalis regarding user's repositories.

Example:
```json
[
    {
        "name": "Todo list",
        "owner_login": "example-username",
        "branches": [
            {
                "name": "main",
                "last_commit_sha": "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad"
            },
            ...
        ]
    },
    ...
]
```

If user does not exists application returns not found message with status 404.

Example:

```json
{
  "status": "404",
  "message": "User with username: {example-username}, not found"
}
```
