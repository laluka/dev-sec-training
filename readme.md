# dev-sec-training-vuln-workshop

The purpose of this repository is to offer a local vulnerable sandbox so developers can be trained to exploit and patch bugs!


# How-To-Play

During the Developer Security Training (10 folks), 2 groups of 5 will be made. \
Each group will have to create their own branch and work on a security fix:

1. Create your branch & go: `git checkout -b team-name`
1. Read the N-th playbook, brainstorm, ask questions if needed
1. Test the feature & exploit with the `curl` requests
1. Implement the patch & test it, if needed, rebuild the env with `How-To - Docker101` below
1. Once you're confident in your fix: commit, push, come back in the main zoom room
1. We'll try to bypass the patch together, it's time to win points by finding bypasses before us!
1. A valid fix first try gives 2 points, second try 1 point, then none!

Rewards at the end on the session! ;)


# How-To - Docker101

Not everybody is (yet?) familiar with docker, so here are a few useful commands

```bash
cd "workshop-backend-part-1-php" # or workshop-backend-part-2-java or workshop-frontend
docker-compose -f docker-compose.yml rm -f
docker-compose -f docker-compose.yml up --force-recreate --remove-orphans
```


# Content of this repo

- Front
    - playbook-01.md - XSS
    - playbook-02.md - Open Redirect
    - playbook-03.md - Secret Leakage
    - playbook-04.md - Script Gadgets
    - playbook-05.md - Outdated Components
    - playbook-06.md - Prototype Pollution
    - playbook-07.md - ClickJacking to XSS
- Backend-part-1-php
    - playbook-01.md & list.php
        - Bad Regular Expression
        - Path traversal - arbitrary listing
        - Mixing filesystem & code logic - backup file
    - playbook-02.md & curl.php
        - IDOR
        - Command injection
    - playbook-03.md & gallery.php
        - File upload/write
    - playbook-04.md & login.php
        - SQL Injection
        - Type juggling
    - playbook-05.md & intake.php
        - Unserialize
        - Reflected XSS
    - playbook-06.md & game.php
        - Included code
        - Evaluated code
    - playbook-07.md & money.php
        - Open Redirect
        - CSRF
- Backend-part-2-java
    - playbook-08.md & Api.java
        - IDOR
        - SSRF
        - Unserialize
        - XXE
    - playbook-09.md & GraphQL*.java
        - Graphql - Introspection
        - Graphql - ReDos
    - playbook-10.md & Api.java
        - Path Traversal
        - Command Injection
        - SQL Injection
        - Race Condition
- Both
    - Information disclosure & verbose errors


# Future Improvements

- Backend
    - Broken Authentication
    - Prototype Pollution
    - Template Injection
    - Outdated software
    - Weak hash algo & lack of salt
    - JDBC evil uri java
    - Graphql reflected XSS
- Frontend
    - Client-side validation
    - Template Injection
    - PostMessage Based XSS


# Structure

```
tree -L 2
.
├── readme.md
├── workshop-backend-part-1-php
│   ├── docker-compose.yml
│   ├── playbooks
│   └── sources
├── workshop-backend-part-2-java
│   ├── docker-compose.yml
│   ├── Dockerfile.backend-java
│   ├── playbooks
│   ├── sources
│   └── ysoserial.jar
└── workshop-frontend
    ├── docker-compose.yml
    ├── exploits
    ├── playbooks
    └── sources

10 directories, 6 files
```
