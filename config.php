<?php
// Include the Facebook client library
require_once ('facebook/php/facebook.php');

// Set authentication variables
$appapikey = 'e7d3ed1695c713a1d57d015b654d1923';
$appsecret = '8a12e2d2fe95e99f1e9c6703d073f2d8';

// Create the Facebook object
$facebook = new Facebook($appapikey, $appsecret);
$appcallbackurl = 'http://greentealatte.net/PacMan_ubc/index.php';
$pacmanmain = 'http://greentealatte.net/PacMan_ubc/PacMan.php';

$user = $facebook->require_login();

//catch the exception that gets thrown if the cookie has an invalid session_key in it
$facebook->redirect($appcallbackurl);

?>