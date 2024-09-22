# playbook-04.md - Script Gadgets

Welcome to this fourth playbook, here you'll learn about bypassing a mitigation of XSS using Script Gadgets.


## Script Gadgets

### Goal

Fix the issue to not allow the execution of arbitrary Javascript. However, the user should still be able to write safe HTML in the page.

### Test

```bash
# Test feature
http://localhost:3000/dompurify?html=%3Ca+href%3D%2Fhello%3EHello%3C%2Fa%3E

# Test Exploit
http://localhost:3000/dompurify?html=%3Cdiv%20data-toggle=tooltip%20data-html=true%20title=%27%3Cscript%3Ealert(1)%3C/script%3E%27%3Ehover%20for%20xss%3C/div%3E
```

### Understand the exploit


Cross Site Scripting (XSS) is a type of attack that allows an attacker to inject malicious code into a web page. This code is then executed by the browser, resulting in the attacker being able to access the victim's sensitive information.

However, XSS can be bypassed by using script gadgets from other libraries. This is because the script gadget will execute the malicious code in the context of the library, rather than in the context of the web page. As a result, the attacker will not be able to access the victim's sensitive information.