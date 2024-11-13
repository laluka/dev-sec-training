# playbook-10.md & Api.java

Welcome to this 10th playbook, here you'll learn about Path Traversal, Command Injection, SQL Injection and Race condition in Java programming language.

## Path traversal

Goal: Fix the path traversal.
Loose regex are a real threat. They are the cause of many issues in the software industry, and they are an even bigger issue in applicative firewalls!
In a waf, a loose regex means that an evil payload wouldn't be blocked, and a too strict regex would break the expected feature's behavior. It's a real pain!

- Example for UUID
  - Good: `^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$`
  - Bad : `^[\S]{34}`   # Think about the dashes, or the Infamous log4shell jndi:ldap://X.X.X.X/evil.class
- Example for PATH
  - Good: `^/some/[\d]+/safe/path$`
  - Bad : `^/some/.*   # Think about ../../../admin`

```bash
# Test feature
curl -sSkig 'http://127.0.0.1/api/path-traversal?dir=img' | grep -ioP 'cat[\d]+\.jpeg'  # Should list all images (cat1.jpeg,cat2.jpeg,cat3.jpeg)
# Test exploit
curl -sSkig 'http://127.0.0.1/api/path-traversal?dir=img/../../etc/passwd'  # Should output content of etc/passwd
```

## Command Injection

Goal: Fix the command injection by adding filtering or using the right java features/functions.
This kind of vulnerability occurs when commands are executed with non-sanitized user input. It may sound trivial, but there are MANY ways to sneak an evil payload.
Here are a few cool examples:

```bash
# Good
ping "foo.bar"
# Evil
ping "foo.bar$(id)"
ping "foo.bar`id`"
ping "foo.bar";id;"""
ping "foo.bar"&&id;#"
ping "foo.bar
id
"
# Should also be considered shell separators such as $IFS or even argument injections like
# The tar infamous checkpoint: https://gtfobins.github.io/gtfobins/tar/#shell
```

```bash
# Test feature
curl -sSkig 'http://127.0.0.1/api/command-injection-ls?path=../' # Should content of parent directory"
# Test exploit
curl -sSki 'http://127.0.0.1/api/command-injection-ls?path=./;id;'
```

## SQL Injection

Goal: Fix the SQL injection to protect the authentication mechanism from suck issue.
SQL injections occur when user-controler data gets reflected within an SQL query without strict sanitization or without using "prepared statements".
Minimalistic example:

Clean/Intended SQL request: `SELECT * FROM users WHERE uname='$USER_NAME' AND pwd='super_secret_pass' LIMIT 1`

- Good: `(USER_NAME == admin)`: `SELECT * FROM users WHERE uname='admin' AND pwd='super_secret_pass' LIMIT 1`
- Evil: `(USER_NAME == admin';-- )`: `SELECT * FROM users WHERE uname='admin';--' AND pwd='super_secret_pass' LIMIT 1`

Which is exactly like `SELECT * FROM users WHERE uname='admin';`, authentication entirely bypassed!\
The -- is to comment the rest of the request thus no SQL Error and your request is proceeded without issue.

```bash
# Test feature
curl -sSkgi 'http://127.0.0.1/api/sql-injection?username=user&password=user' # Should output "Welcome back, user!"
curl -sSkgi 'http://127.0.0.1/api/sql-injection?username=admin&password=admin' # Should output "Welcome back, admin!"
curl -sSkgi 'http://127.0.0.1/api/sql-injection?username=user&password=badpass' # Should output "Forbidden"
# Test exploit
curl -sSkgi "http://127.0.0.1/api/sql-injection?username=admin';--%20&password=badpass" # Should output "Welcome back, admin!"
```

## Race condition

Goal1: Fix the Race Condition External Entity while preserving the current feature of coupons with a max of 5 per user

A race condition is an undesirable situation that occurs when a device or system attempts to perform two or more operations at the same time, but because of the nature of the device or system, the operations must be done in the proper sequence to be done correctly.  Race conditions are most commonly associated with computer science and programming. They occur when two computer program processes, or threads, attempt to access the same resource at the same time and cause problems in the system. Race conditions are considered a common issue for multithreaded applications.

```bash
# Test feature
curl -sSkg "http://127.0.0.1/api/race-condition?username=admin" # Should display 3 coupons
# Test exploit
# Send the same request multiple times in a short time
for i in {1..10}; do curl "http://127.0.0.1/api/race-condition?username=admin" &; done # Should display more than 5 coupons
```
