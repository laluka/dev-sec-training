<?php

$html_start = "<!DOCTYPE html>
<html>
  <head>
    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />
    <title>Hello Fellow Hacker!</title>
    <meta name=\"description\" content=\"description\"/>
    <meta name=\"author\" content=\"author\" />
    <meta name=\"keywords\" content=\"keywords\" />
    <style type=\"text/css\">.body { width: auto; }</style>
  </head>
  <body>
  <h1>Here are the various features and bugs to exploit & patch!</h1>
  <ul>";

$html_stop = "</ul>
</body>
</html>";

if (isset($_GET["debug"])) {
  phpinfo();
} else {
  echo "$html_start";
  foreach (glob("*.php") as $filename) {
    echo "<li><a href=\"$filename\">$filename</a></li>";
  }
  echo "$html_stop";
}
