# playbook-09.md & GraphQL*.java

Welcome to this 9th playbook, here you'll learn about Graphql introspection and Regular Expression Denial Of Service (ReDOS).


## Graphql introspection

Goal1: Fix the schema leakage by disabling the introspection.
Goal2: Can the schema still be recovered without introspection? If so, can this be mitigated as well?

Introspection is extremely useful while the app is still in developement, or for world-open APIs as it acts like a self-documented api, exposes the schema, the object definitions, the methods to read and mutate values, etc. But do we really want to expose our internal "documentation" to the world? Do we?? :<


```bash
# Test feature
curl -sSk 'http://127.0.0.1/graphql' -H 'Content-Type: application/json' --data-raw '{"query":"query MyQuery {\n  bookById(id: \"book-1\") {\n    id\n    author {\n      firstName\n    }\n  }\n}","variables":{},"operationName":"MyQuery"}' # Output: {"data":{"bookById":{"id":"book-1","author":{"firstName":"Joanne"}}}}
# Test exploit
curl -sSk 'http://127.0.0.1/graphql?query=query%20IntrospectionQuery%20%7B%0A%20%20%20%20__schema%20%7B%0A%20%20%20%20%20%20queryType%20%7B%20name%20%7D%0A%20%20%20%20%20%20mutationType%20%7B%20name%20%7D%0A%20%20%20%20%20%20subscriptionType%20%7B%20name%20%7D%0A%20%20%20%20%20%20types%20%7B%0A%20%20%20%20%20%20%20%20...FullType%0A%20%20%20%20%20%20%7D%0A%20%20%20%20%20%20directives%20%7B%0A%20%20%20%20%20%20%20%20name%0A%20%20%20%20%20%20%20%20description%0A%20%20%20%20%20%20%20%20args%20%7B%0A%20%20%20%20%20%20%20%20%20%20...InputValue%0A%20%20%20%20%20%20%20%20%7D%0A%20%20%20%20%20%20%20%20onOperation%0A%20%20%20%20%20%20%20%20onFragment%0A%20%20%20%20%20%20%20%20onField%0A%20%20%20%20%20%20%7D%0A%20%20%20%20%7D%0A%20%20%7D%0A%0A%20%20fragment%20FullType%20on%20__Type%20%7B%0A%20%20%20%20kind%0A%20%20%20%20name%0A%20%20%20%20description%0A%20%20%20%20fields(includeDeprecated:%20true)%20%7B%0A%20%20%20%20%20%20name%0A%20%20%20%20%20%20description%0A%20%20%20%20%20%20args%20%7B%0A%20%20%20%20%20%20%20%20...InputValue%0A%20%20%20%20%20%20%7D%0A%20%20%20%20%20%20type%20%7B%0A%20%20%20%20%20%20%20%20...TypeRef%0A%20%20%20%20%20%20%7D%0A%20%20%20%20%20%20isDeprecated%0A%20%20%20%20%20%20deprecationReason%0A%20%20%20%20%7D%0A%20%20%20%20inputFields%20%7B%0A%20%20%20%20%20%20...InputValue%0A%20%20%20%20%7D%0A%20%20%20%20interfaces%20%7B%0A%20%20%20%20%20%20...TypeRef%0A%20%20%20%20%7D%0A%20%20%20%20enumValues(includeDeprecated:%20true)%20%7B%0A%20%20%20%20%20%20name%0A%20%20%20%20%20%20description%0A%20%20%20%20%20%20isDeprecated%0A%20%20%20%20%20%20deprecationReason%0A%20%20%20%20%7D%0A%20%20%20%20possibleTypes%20%7B%0A%20%20%20%20%20%20...TypeRef%0A%20%20%20%20%7D%0A%20%20%7D%0A%0A%20%20fragment%20InputValue%20on%20__InputValue%20%7B%0A%20%20%20%20name%0A%20%20%20%20description%0A%20%20%20%20type%20%7B%20...TypeRef%20%7D%0A%20%20%20%20defaultValue%0A%20%20%7D%0A%0A%20%20fragment%20TypeRef%20on%20__Type%20%7B%0A%20%20%20%20kind%0A%20%20%20%20name%0A%20%20%20%20ofType%20%7B%0A%20%20%20%20%20%20kind%0A%20%20%20%20%20%20name%0A%20%20%20%20%20%20ofType%20%7B%0A%20%20%20%20%20%20%20%20kind%0A%20%20%20%20%20%20%20%20name%0A%20%20%20%20%20%20%20%20ofType%20%7B%0A%20%20%20%20%20%20%20%20%20%20kind%0A%20%20%20%20%20%20%20%20%20%20name%0A%20%20%20%20%20%20%20%20%7D%0A%20%20%20%20%20%20%7D%0A%20%20%20%20%7D%0A%20%20%7D&variables=%7B%7D&operationName=IntrospectionQuery' |  jq . # Outputs: 1100 lines of internal scheme, data, and queries definition
```


## Graphql - ReDos

Goal1: Fix the Regular Expression Denial Of Service so we can't DOS the service anymore.
Goal2: Brainstorm and propose two other ways to achieve ReDOS-prevention.

Regular Expressions are hard, really powerful, but hard. It gets even harder with lookups, and even worse with multiple lookups & lookahead. A user controled regex, or partially controled regex is often enough to cause DOS or data leakage. So let's harden our regex-fu!


```bash
# Test feature
curl -sSk 'http://127.0.0.1/graphql' -H 'Content-Type: application/json' --data-raw '{"query":"query MyQuery {\n  bookByPattern(pattern: \"[^H]+.*\") {\n    name\n    pageCount\n  }\n}","variables":{},"operationName":"MyQuery"}' # Output: {"data":{"bookByPattern":{"name":"Moby Dick","pageCount":635}}}
# Test exploit
curl -sSk 'http://127.0.0.1/graphql' -H 'Content-Type: application/json' --data-raw '{"query":"query MyQuery {\n  bookByPattern(pattern: \".*?a[a]+((.*[a]*)*)aa[a]+((.*[a]*)*)aa[a]+((.*[a]*)*)a\") {\n    name\n    pageCount\n  }\n}","variables":{},"operationName":"MyQuery"}' # Output: Nothing, hangs the backend, CPU DOS, run "htop" to check the resource consumption!
```
