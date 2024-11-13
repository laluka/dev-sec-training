# playbook-07.md & money.php

Welcome to this seventh playbook, here you'll learn about the Open Redirections and Cross Site Request Forgeries.

## Open Redirect

Goal1: Fix the open redirect to either block it totally, or only allow redirection on the website.\
Goal2: Find a few (2 or 3) ways this could be used by an attacker, what would be the impact?

Open Redirections can be used in many way, as it's one of the goals of this section to find them, they won't be detailed here..\
That being said, they occur when an attacker is able to inject content in the "Location" http header or specific html tags or javascript code. They allow to redirect the victim to an attacker-controled page.

```bash
# Test feature
curl -sSkgiL 'http://127.0.0.1/money.php?on_success=/money.php?success=1' | grep -F Money # Should output "Money sent."
# Test exploit
curl -sSkgi 'http://127.0.0.1/money.php?on_success=http://google.com/' | grep -F Location # Should output "Location: http://google.com/"
# Opening this link in a browser should automatically redirect the user to google.com
```

## Cross Site Request Forgery - CSRF

Goal: Find a way to mitigate this CSRF. As it can be hard due to the fact that no database is currently linked to the project, you can limit this task to a detailed explanation on how it could be mitigated.

CSRF take place when a user can take an action without any "confirmation token". Long story short, no sensitive action should be allowed with a predictible url, the url or post body should contain an unique token verified for sensitive actions.

```bash
# Test feature
# The feature should still allow to send money, but in two steps, "clicking a link" should not be enough for loosing money, right?
# Test exploit
curl -sSkgi 'http://127.0.0.1/money.php?from=lalu&to=roni&amount=111' # Should output "All good, 111$ have been sent from lalu to roni!"
```
