<?php

if (isset($_REQUEST["success"])) {
    http_response_code(200);
    die("Money sent.");
}

if (isset($_REQUEST["on_success"])) {
    header("Location: " . $_REQUEST["on_success"]);
    http_response_code(301);
    die("Redirecting...");
}

if (!isset($_GET["from"]) or !isset($_GET["to"]) or !isset($_GET["amount"])) {
    http_response_code(500);
    die("Not using the right parameters, quitting.");
}
try {
    $from = $_GET["from"];
    $to = $_GET["to"];
    $amount = intval($_GET["amount"]);
} catch (\Throwable $th) {
    http_response_code(500);
    die("Not using the right parameters, quitting.");
}
// Placeholder, act like if money was really sent to someone here :)

http_response_code(302);
header("Location: /money.php?on_success=/money.php?success=1");
echo "All good, $amount$ have been sent from $from to $to!";
die();
