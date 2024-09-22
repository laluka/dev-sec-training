<?php

if (!isset($_GET["search"])) {
    header("Location: /gallery.php?search=manomano");
    die();
}
$search = $_GET["search"];
$uri = "https://www.google.com/search?q=" . $search;
$content = file_get_contents($uri);
echo "\n\n<br>Content: " . $content . "<br>";

$outdir = "history";
$outfile = str_replace("/", "_", $search);
$outfile = $outfile . ".search";
$outfile = $outdir . "/" . substr($outfile, 0, 200); // Prevent overlong names that would crash

if (!file_exists($outdir)) {
    mkdir($outdir, 0777, true);
}
echo "\n\n<br>Writting search history to $outfile<br>";
file_put_contents($outfile, $search . "\n\n" . $content);
