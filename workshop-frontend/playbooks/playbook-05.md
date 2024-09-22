# playbook-05.md - Outdated Components

Welcome to this fifth playbook, here you'll learn about bypassing a mitigation of XSS using Outdated Components.


## Outdated Components

### Goal

Fix the issue to not allow the execution of arbitrary Javascript. However, the user should still be able to write safe HTML in the page.

### Test

```bash
# Test feature
http://localhost:3000/dompurify?html=%3Ca+href%3D%2Fhello%3EHello%3C%2Fa%3E

# Test Exploit
http://localhost:3000/dompurify?html=%3Cmath%3E%3Cmtext%3E%3Ctable%3E%3Cmglyph%3E%3Cstyle%3E%3C%21--%3C%2Fstyle%3E%3Cimg%20title%3D%22--%26gt%3B%26lt%3B%2Fmglyph%26gt%3B%26lt%3Bimg%26Tab%3Bsrc%3D1%26Tab%3Bonerror%3Dalert%281%29%26gt%3B%22%3E%0A 
```

### Understand the exploit



Outdated components can introduce vulnerabilities for a number of reasons. They may no longer be supported by the vendor, meaning that there are no security updates or patches available for them. Additionally, outdated components may not be compatible with newer versions of software or operating systems, which can also introduce vulnerabilities. Finally, outdated components may simply be less secure than newer versions, due to advances in security technology.