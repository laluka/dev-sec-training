# playbook-03.md - Secret Leakage

Welcome to this third playbook, here you'll learn about Secrets Leakage in the Front End


## All the secrets are leaking :/

### Goal

Fix the issue by removing all the secrets leaking in the front end.

### Test

- Open the application on your browser (Chrome or Firefox)
- Go to the Dev Tool 
- Search the keyword "password"
- Notice the password to rick-database leaking

### Understand the exploit


Imagine a website that contains sensitive data in its front-end code, such as API tokens. This data is accessible to anyone who views the source code of the website. Consequently, this data could be easily accessed and misused by malicious actors. API keys and other secrets should never be hard-coded into a front-end application. If a secret is compromised, it can be used to access sensitive data or perform other malicious actions. Secrets should be stored in a secure location, such as an environment variable, and retrieved when needed.