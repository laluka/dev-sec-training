# playbook-07.md - Impacting Click Jacking

Welcome to this seventh playbook, here you'll learn about ClickJacking.

## Outdated Components

### Goal

Fix the exploit where we can delete the database in one click.

### Test

### Test Feature

- Go to http://localhost:3000/hijacking
- Click on the button to delete the database

### Test Exploit

1. Open `exploits/clickjacked.html` with your browser
2. Click on the `Click` button
3. Notice that you managed to Delete the database

### Understand the exploit

Clickjacking, also known as UI redress attack, is a type of exploit where malicious code is injected into a page or email causing users to perform unintended actions. The attacker tricks the user into clicking on something that they would not normally click on, such as a link that opens up a harmful website or downloads malware.

An iframe trick is used to create a transparent iframe that covers the entire page. When the user clicks on the iframe, they are actually clicking on the invisible button or link underneath. This tricks the user into thinking they are clicking on something else, when in reality they are performing an action that they may not have intended to do.

While clickjacking is not considered as a security issue by itself, sometimes it can lead to critical exploits such deleting arbitrary data in one click.
