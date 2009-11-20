<fb:if-is-app-user>

<?php
// Include the Facebook client library
require_once ('facebook/php/facebook.php');
require_once 'Services/Facebook.php';

// Set authentication variables
$appapikey = 'e7d3ed1695c713a1d57d015b654d1923';
$appsecret = '8a12e2d2fe95e99f1e9c6703d073f2d8';
Services_Facebook::$apiKey = $appapikey; // insert your application key instead of xxx 
Services_Facebook::$secret = $appsecret; // insert your application secret instead of yyy 
// Create the Facebook object
$facebook = new Facebook($appapikey, $appsecret);
$appcallbackurl = 'http://greentealatte.net/PacMan_ubc/index.php';
$pacmanmain = 'http://greentealatte.net/PacMan_ubc/PacMan.php';
$api = new Services_Facebook();

$info = $api->users->getInfo($uid); 
$pacmanid = $info->user->uid; 
$pacmanname = $info->user->first_name; 

echo '<APPLET CODE="PacMan.class" WIDTH=600 HEIGHT=500>';

echo '<PARAM NAME="username" VALUE='.$pacmanname.'>';
echo '<PARAM NAME="uid" VALUE='.$pacmanid.'>';
echo '</APPLET>';
?>