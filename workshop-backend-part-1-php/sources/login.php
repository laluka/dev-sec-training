<?php

// Ensure a clean db state, super-dirty, workshop only

@unlink("sqlite.db");
$db = new SQLite3('sqlite.db');

$db->exec("CREATE TABLE users(id INTEGER PRIMARY KEY, uname TEXT, pwd TEXT)");
$db->exec("INSERT INTO users(uname, pwd) VALUES('user', 'ee11cbb19052e40b07aac0ca060c23ee')");
$db->exec("INSERT INTO users(uname, pwd) VALUES('test', '098f6bcd4621d373cade4e832627b4f6')");
$db->exec("INSERT INTO users(uname, pwd) VALUES('admin', '0d107d09f5bbe40cade3de5c71e9e9b7')");

define("AUTH_NONE", 0);
define("AUTH_ADMIN", 1);
define("AUTH_USER", 2);

$html_start = "<!DOCTYPE html>
<html>
  <head>
    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />
    <title>Hello!</title>
    <meta name=\"description\" content=\"description\"/>
    <meta name=\"author\" content=\"author\" />
    <meta name=\"keywords\" content=\"keywords\" />
    <style type=\"text/css\">.body { width: auto; }</style>
  </head>
  <body>";

$html_stop = "</body>
</html>";

$login_form = "<form method=\"post\" action=\"login.php\">
<div id=\"div_login\">
    <h1>Login</h1>
    <div>
        <input type=\"text\" class=\"textbox\" id=\"uname\" name=\"uname\" placeholder=\"Username\" />
    </div>
    <div>
        <input type=\"password\" class=\"textbox\" id=\"pwd\" name=\"pwd\" placeholder=\"Password\"/>
    </div>
    <div>
        <input type=\"submit\" value=\"Submit\" name=\"but_submit\" id=\"but_submit\" />
    </div>
</div>
</form>";

$auth_status = AUTH_NONE;

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    echo "$html_start";
    echo "$login_form";
    echo "$html_stop";
    die();
} else if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (!isset($_POST["pwd"]) || !isset($_POST["uname"]) || strlen($_POST["uname"]) === 0 || strlen($_POST["pwd"]) === 0) {
        header("Location: /login.php");
        die();
    }
    $uname = $_POST["uname"];
    $pwd = $_POST["pwd"];
    $md5_pwd = md5($pwd);

    $req = "SELECT * FROM users WHERE uname='$uname' AND pwd='$md5_pwd' LIMIT 1";
    // echo "SQL_REQ: $req\n";
    $result = $db->query($req);
    $row = $result->fetchArray();
    if (!isset($row["uname"])) {
        http_response_code(403);
        die("Forbidden");
    }
    if ($row["uname"] === "admin") {
        $auth_status = AUTH_ADMIN;
    } else {
        $auth_status = AUTH_USER;
    }

    if ($auth_status === AUTH_USER) {
        echo "Welcome back, user!\n";
        if (isset($_POST["magic"])) {
            if (strlen($_POST["magic"]) <= 10) {
                echo "It would be too easy... Too short!\n";
                die();
            }
            if ($_POST["magic"] == 42) {
                echo "Uh oh, sorry, I meant: Welcome back, 'admin'!\n";
            }
        }
    } else if ($auth_status === AUTH_ADMIN) {
        echo "Welcome back, admin!\n";
        die();
    } else {
        echo "Something, Somewhere, went terribly wrong!\n";
        die();
    }
}
