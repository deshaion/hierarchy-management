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

### The task:

This JSON represents an Employee -> Supervisor relationship that looks like this:
```json
{
  "Pete": "Nick",
  "Barbara": "Nick",
  "Nick": "Sophie",
  "Sophie": "Jonas"
}
```

In this case, Nick is a supervisor of Pete and Barbara, Sophie supervises Nick. The supervisor list is
not always in order.

As a response to querying the endpoint, I would like to have a properly formatted JSON which
reflects the employee hierarchy in a way, where the most senior employee is at the top of the JSON
nested dictionary. For instance, previous input would result in:
```json
{
  "Jonas": {
    "Sophie": {
      "Nick": {
        "Pete": {},
        "Barbara": {}
      }
    }
  }
}
```
Sometimes there are nonsense hierarchies in the request that contain loops or multiple roots. I would be
grateful if the endpoint could handle the mistakes and tell what went wrong. The more
detailed the error messages are, the better!

I would really like if the hierarchy could be stored in a relational database (e.g. SQLite) and queried
to get the supervisor and the supervisor’s supervisor of a given employee. I want to send the name
of an employee, and receive the name of the supervisor and the name of the supervisor’s
supervisor in return.

I would like the API to be secure, so that only I can use it. Please implement some kind of
authentication.
