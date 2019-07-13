# Hierarchy management

You can run it by command:
```
./gradlew bootRun
```

How to make requests by curl
```
curl -i --user user:pass http://localhost:8080/employees/Jonas
curl --user user:pass -d '{"Pete":"Nick", "Barbara":"Nick", "Nick":"Sophie", "Sophie":"Jonas"}' -H "Content-Type: application/json" -X POST http://localhost:8080/relationships
```