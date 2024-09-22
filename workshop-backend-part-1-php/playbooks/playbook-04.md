# playbook-04.md & login.php

Welcome to this fourth playbook, here you'll learn about basic SQL injections, and type-juggling issues!


## SQL Injection

Goal: Fix the SQL injection to protect the authentication mechanism from suck issue.

SQL injections occur when user-controler data gets reflected within an SQL query without strict sanitization or without using "prepared statements".

Minimalistic example:

- Clean SQL request: `SELECT * FROM users WHERE uname='$USER_NAME' AND pwd='super_secret_pass' LIMIT 1`
- Good (`USER_NAME == admin`): `SELECT * FROM users WHERE uname='admin' AND pwd='super_secret_pass' LIMIT 1`
- Evil (`USER_NAME == admin';--`): `SELECT * FROM users WHERE uname='admin';--' AND pwd='super_secret_pass' LIMIT 1`
- Which is exactly like `SELECT * FROM users WHERE uname='admin'`, authentication entirely bypassed!

```bash
# Test feature
curl -sSkgi 'http://127.0.0.1/login.php' -d "uname=user&pwd=user" # Should output "Welcome back, user!"
curl -sSkgi 'http://127.0.0.1/login.php' -d "uname=admin&pwd=letmein" # Should output "Welcome back, admin!"
curl -sSkgi 'http://127.0.0.1/login.php' -d "uname=user&pwd=badpass" # Should output "Forbidden"
# Test exploit
curl -sSkgi 'http://127.0.0.1/login.php' -d "uname=admin';--&pwd=badpass" # Should output "Welcome back, admin!"
```


## Type juggling

Goal: Tweak the code to fix this issue!\
Hint: It can be fixed with a single keypress!\
Note: Once the type juggling is fixed, the feature won't be reachable anymore, but it's OK.

Php tries to be really flexible, but due to this ease of access, many issues are (sadly) introduced by design.. Here, type juggling can be seen as "loose conversions errors".\
All loose-typed language will try to convert values in a way that make sense, but here, `042aaaa` is parsed as `42`. Is this legitimate? Is this helpful? Is this dangerous?\
What happens for arrays such as `http://domain/?param[key]=dummy`? How is param treated if it gets passed to a function? Well, I'll tell you. Bad stuff happens.

More information on (php) type-juggling issues here: https://owasp.org/www-pdf-archive/PHPMagicTricks-TypeJuggling.pdf

```bash
# Test feature
curl -sSkgi 'http://127.0.0.1/login.php' -d "uname=user&pwd=user" # Should output "Welcome back, user!"
curl -sSkgi 'http://127.0.0.1/login.php' -d "uname=user&pwd=user&magic=42" # Should output "Welcome back, user! It would be too easy... Too short!"
# Test exploit
curl -sSkgi 'http://127.0.0.1/login.php' -d "uname=user&pwd=user&magic=042aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
# Should output "Welcome back, user! Uh oh, sorry, I meant: Welcome back, 'admin'!"
```
