<?php

if (!isset($_GET["dir"])) {
    header("Location: /list.php?dir=img");
    die();
}
$path = $_GET["dir"];
// Ensure we're only displaying img content and its sub-directories
if (!preg_match("/^img.*/i", $path)) {
    echo "Trying to list a forbidden directory, blocked!";
    http_response_code(403);
    die();
}
if ($dir = opendir($path)) {
    echo "<h1>Listing : $path</h1></br><ul>";
    while (($file = readdir($dir)) !== false) {
        echo "<li>$file</li>";
    }
    echo "</ul>";
}
