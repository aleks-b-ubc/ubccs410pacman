<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:fb="http://www.facebook.com/2008/fbml"> 
<head>
<title></title>
<?php
/* include the PHP Facebook Client Library to help
  with the API calls and make life easy */
require_once('facebook/php/facebook.php');
/* initialize the facebook API with your application API Key
  and Secret */
$facebook = new Facebook('e7d3ed1695c713a1d57d015b654d1923','8a12e2d2fe95e99f1e9c6703d073f2d8');

/* require the user to be logged into Facebook before
  using the application. If they are not logged in they
  will first be directed to a Facebook login page and then
  back to the application's page. require_login() returns
  the user's unique ID which we will store in fb_user */
$fb_user = $facebook->require_login();

/* now we will say:
  Hello USER_NAME! Welcome to my first application! */

$user_details = $facebook->api_client->users_getInfo($uid, 'name'); 
$data['name'] = $user_details['name']; 
$data['last_name'] = $user_details['last_name']; 
$wtf = 'wtf';
?>

Hello <?php echo $data['name']; ?>! Welcome to my Pacman M.D.!

<APPLET CODE="PacMan.class" WIDTH=600 HEIGHT=500>
<param name=username value='<?php echo $wtf; ?>' useyou='false' possessive='true' />>
<PARAM NAME=uid VALUE=<?php echo($pacmanid);?>>
</APPLET>

</body>