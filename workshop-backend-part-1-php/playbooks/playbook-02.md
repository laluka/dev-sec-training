# playbook-02.md & curl.php

Welcome to this second playbook, here you'll learn about Insecure Direct Object Reference and Command Injections!

## IDOR

Goal: Explain how this specific IDOR should be fixed, what should we implement here?

Insecure Direct Object Reference consist in a user, authentified or not, being able to access data or features they shouldn't have access to. It often requires guessing the right id, keyword, or uuid to query it, as well as a lack of ownership/privilege checks.

```bash
# Test feature
curl -sSki 'http://127.0.0.1/curl.php?feature=1' # Should output "Unauthenticated used detected, you're only allowed to use features below grade 10 :)\nHello, I'm feature 1!"
# Test exploit
curl -sSki 'http://127.0.0.1/curl.php?feature=42' | grep -i location # Should output "Location: /curl.php?feature=42&domain=google.com"
```

## Command Injection

Goal: Fix the command injection by adding filtering or using the right php features/functions.

This kind of vulnerability occurs when commands are executed with insanitized user input. It may sound trivial, but there are MANY ways to sneak an evil payload.

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
curl -sSkig 'http://127.0.0.1/curl.php?feature=42&domain=www.google.com' # Should output "Output: 200:0:http://www.google.com/"
# Test exploit
curl -sSki 'http://127.0.0.1/curl.php?feature=42&domain=www.google.com;id;' | grep -ioP 'uid=.*' # uid=0(root) gid=0(root) groups=0(root)
```
