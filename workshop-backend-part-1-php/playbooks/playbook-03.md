# playbook-03.md & gallery.php

Welcome to this third playbook, here you'll learn about file-upload & file-write issues, as well as a more generic "mixed filesystem & routing" deadly design-pattern!


## File upload/write

Goal: Fix the file-write vulnerability, the exploit should not permit to write php files nor bad chars in the controles content. \
Hint: Bad chars encoding, file name & extention allow-list

This kind of vulnerability is really frequent for filesystem-based storage (opposite to database storage). It allows an attacker to control some content within a file, being it with an absolute known path or not.

Some examples of this are the following:

- Upload a "profile picture" ending by `.php` with a php web-shell embeded
- Poison logs with a asp/php/jsp and include/eval the logs with another bug
- Overwrite a crontab or cronscript to execute a task (code execution) on the victim
- Overwrite a sensitive file such as `~/.ssh/authorized_keys` to takeover this machine's authentication system

```bash
# Test feature
curl -sSkig 'http://127.0.0.1/gallery.php?search=manomano'
curl -sSkig -o /dev/null 'http://127.0.0.1/history/manomano.search' -w "%{http_code}:%{url_effective}\n" # Should output "200:http://127.0.0.1/history/manomano.search"
# Test exploit
curl -sSkig 'http://127.0.0.1/gallery.php?search=%3c%3f%70%68%70%20%70%68%70%69%6e%66%6f%28%29%3b%20%3f%3efoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo.php'
curl -sSkig 'http://127.0.0.1/history/%3c%3f%70%68%70%20%70%68%70%69%6e%66%6f%28%29%3b%20%3f%3efoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo.php' | grep -iF 'phpinfo()<'
# Should output phpinfo page: "<title>PHP 7.4.26 - phpinfo()</title><meta name="ROBOTS" content="NOINDEX,NOFOLLOW,NOARCHIVE" /></head>"
```
