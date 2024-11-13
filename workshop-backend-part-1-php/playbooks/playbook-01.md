# playbook-01.md & list.php

Welcome to this first playbook, here you'll learn about three vulnerabilities, regular expressions bypasses, path traversals, and mixed filesystem/routing!

## Regular Expression Bypass

Goal: Fix the path traversal, first with the appropriate regex (or any other method of your choice *wink wink*).

Loose regex are a real threat. They are the cause of many issues in the software industry, and they are an even bigger issue in firewalls! In a firewall, a loose regex means that an evil payload wouldn't be blocked, and a too strict regex would break the expected feature's behavior. It's a real pain!

- Example for UUID
  - Good: `^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$`
  - Bad : `^[\S]{34}`   # Think about the dashes, or the Infamous log4shell `jndi:ldap://X.X.X.X/evil.class`
- Example for PATH
  - Good: `^/some/[\d]+/safe/path$`
  - Bad : `^/some/.*`   # Think about `../../../admin`

```bash
# Test feature
curl -sSkig 'http://127.0.0.1/list.php?dir=img' | grep -ioP 'cat[\d]+\.jpeg' # Should list all images (cat1.jpeg,cat2.jpeg,cat3.jpeg)
# Test exploit
curl -sSkig 'http://127.0.0.1/list.php?dir=img/../' | grep -ioP 'unguessable[^ ]+\.bak' # Should output unguessable-backup-name-982738792473.bak
```

## Path traversal - arbitrary listing

Goal: Understand how directory traversal work! :)\
Note: As the first regex fix fixed the path traversal, nothing to do here!

A path traversal can occur in any kind of URI, including URLs and file paths. They are due to unsanitized & user-controled content used to construct such URI.

Examples:

- `http://foo.bar/profile/../../../../`
- `http://foo.bar/profile/..;/..;/..;/`
- `http://foo.bar/profile/.././.././../`
- `http://foo.bar/profile/..%09/..%09/`
- `http://foo.bar/profile/..\/..\/..\/`

## Mixing filesystem & code logic - backup file

Goal: Brainstorm with your teammates to propose one way this could be fixed. Do *not* implement it as this would probably take a lot of time.

This security issue is "by design" depending on the technology being used.\
In an ideal world, every route should be declated, whitelisted, and respect a strict parsing before reaching the backend.\
However, in real life, it's often much scarier, things are being exposed unwillingly, unknowingly, and moft of the time leak to huge security holes!

Example: Think about a wordpress website, with directory listing. Now realise you can download the backup files with random names as they're LISTED just for you. Once the backup is acquired, it's a matter of time to parse them, recoved the plaintext admin passwords, and completely takeover this website!

```bash
# Test feature
## Nope, this just souldn't exist, it's... Meh.
# Test exploit
curl -sSkig 'http://127.0.0.1/unguessable-backup-name-982738792473.bak'
```
