<?php

if (!isset($_REQUEST["regex"])) {
    http_response_code(403);
    die("Try to guess the magic word with the parameter 'regex'");
}

$regex = $_REQUEST["regex"];
file_put_contents("/tmp/regex.txt", $regex);
echo "Current regex has been saved to /tmp/regex.txt for backup purpose\n";

$REG = '$win = preg_match("/^REGEX$/", "m4gicword", $matches);';
$REG = str_replace("REGEX", $regex, $REG);

eval($REG);


if (intval($win) === 1) {
    echo "Regex matches :) !\n";
} else {
    echo "Regex doesn't match :( !\n";
}

if (isset($_GET["hint"])) {
    include("./hints/" . $_GET["hint"] . ".txt");
}

if ($regex === "m4gicword") {
    echo "It's an absolute win!\n";
}
