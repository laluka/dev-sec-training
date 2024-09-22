# playbook-06.md & game.php

Welcome to this sixth playbook, here you'll learn about the dangers of code evaluation and unsafe uses of the include built php function.


## Included code

Goal: Fix the unsafe code inclusion.

This issue is mostly specific to php but can also be found in various others languages and frameworks that use a module system. What happens if you can write a module file and force the reload phase? or what if it includes all files within a directory? Well, code gets included and executed, and this is pretty bad!

Here, a hint file can be included, but due to the lack of input sanitization (again!), it's possible to include any file on the machine by using a path traversal attack. This also implies that any valid php file can also be included and executed!


```bash
# Test feature
curl -sSkgi 'http://127.0.0.1/game.php?regex=foo&hint=hint1' # Should output "[...] You lost this time... Let's try again!\nLetter 1 is a... m ! :D"
curl -sSkgi 'http://127.0.0.1/game.php?regex=foo'            # Should output "[...] You lost this time... Let's try again!"
# Test exploit
curl -sSkgi 'http://127.0.0.1/game.php?regex=<?php+system(id);?>&hint=../../../../../../tmp/regex' # Should output: "[...] uid=0(root) gid=0(root) groups=0(root)"
```


## Evaluated code

Goal: Get rid of this dirty eval statment!

Eval is a php builtin that.. Well.. Evaluates code. This implies that any attacker-controled content reaching such function is a real threat!\
Even though this has been known for years, this is still quite widely used, and is implemented in most of the languages. Oh god why...

```bash
# Test feature
curl -sSkgi 'http://127.0.0.1/game.php?regex=.*'        # Should output "Regex matches :) !"
curl -sSkgi 'http://127.0.0.1/game.php?regex=foo'       # Should output "Regex doesn't match :( !"
curl -sSkgi 'http://127.0.0.1/game.php?regex=m4gicword' # Should output "Regex matches :) !\nIt's an absolute win!"
# Test exploit
curl -sSkgi 'http://127.0.0.1/game.php?regex=.*/","foo",$foo);system("id");//' # Should output: "uid=0(root) gid=0(root) groups=0(root)\nYou won!"
```


