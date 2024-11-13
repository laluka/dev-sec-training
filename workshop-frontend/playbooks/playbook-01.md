# playbook-01.md - XSS

Welcome to this first playbook, here you'll learn about Cross-Site-Scripting

## Cross-Site-Scripting (XSS)

### Goal

Fix the issue to not allow the execution of arbitrary Javascript.

### Test

```bash
# Test feature
http://localhost:3000/?secret=SomeSecret

# Test exploit
http://localhost:3000/?secret=<img src=x onerror=alert(location)>
```

### Understand the exploit

XSS is a type of security vulnerability that allows an attacker to inject malicious code into a web page. This code is then executed by the browser of any unsuspecting user who visits the page, resulting in the attacker being able to steal information or perform other malicious actions. XSS is the most common type of web application security vulnerability, making it a major security concern for businesses and individuals alike.

There are two main types of XSS attacks:

1. Reflected XSS: This type of attack occurs when a malicious script is injected into a web page and is then reflected back to the user's browser. The script is executed when the user views the page, resulting in the attacker being able to steal information or perform other malicious actions.

2. Persistent XSS: This type of attack occurs when a malicious script is injected into a web page and is then stored by the server. Any unsuspecting user who visits the page will then have the script executed, resulting in the attacker being able to steal information or perform other malicious actions.

Both types of attacks can be devastating, but persistent XSS attacks are particularly dangerous because they can affect a large number of users and are much harder to detect and fix.

XSS vulnerabilities can be found in a wide variety of web applications, including social networking sites, e-commerce sites, and even simple websites. Any site that allows users to input data is at risk, which is why it's so important for businesses and individuals to be aware of the dangers of XSS and to take steps to protect themselves.
