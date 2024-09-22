<?php

if (!isset($_GET["feature"])) {
    header("Location: /curl.php?feature=1");
    die();
}

$feature = intval($_GET["feature"]);
if ($feature === 42) {
    if (!isset($_GET["domain"])) {
        header("Location: /curl.php?feature=42&domain=google.com");
        die();
    }
    $domain = $_GET["domain"];
    $cmd = "curl -sS --head -o /dev/null -w '%{http_code}:%{size_download}:%{url_effective}\n' $domain 2>&1";
    echo "Will execute: $cmd </br>";
    echo "Output: ";
    system($cmd);
} else {
    echo "Unauthenticated used detected, you're only allowed to use features below grade 10 :)\n";
    echo "Hello, I'm feature $feature!";
}
