# BACHAO
As name suggests, it is an emergency android application. Situations can arrive when one may need to let his family and friends know that he/she is in DANGER.
Though some mobile phones have a built-in app to tackle this situation, but most of them only calls to a emergency number only (999 or so).
So they lack some functionalities like MESSAGING the close ones to let them know that the victim is in danger or sending LOCATION of victim to them.
This app brings out just these.


## FEATURES:

User can activate Danger Mode just TAPPING POWER BUTTON MORE THAN 4 TIMES (API level 28 or 29). Which will lead to 
1. Call emergency number (999 for BD)
2. Sending messages to the numbers user has set up (As much as user wants)
3. Sending their current location with message
4. User can also get numbers of AMBULANCE, POLICE, FIRE SERVICE of different divisions of Bangladesh and call these numbers straight.
5. User can sign in to his own account and save his information. So next time if he reinstall the app, there's no need to update information twice.


## App demo

<table >
  
  <tr>
    <th>Signup</th>
    <th>Login</th>
  </tr>
  <tr>
    <td width=33%>
      <img src="https://github.com/FarhanSadaf/Emergency-app/blob/master/tutorials/1-create%20account.jpg">
    </td>
    <td width=33%>
      <img src="https://github.com/FarhanSadaf/Emergency-app/blob/master/tutorials/2-login.jpg">
    </td>
  </tr>
  
  <tr>
    <th>Welcome window after signup</th>
    <th>Home screen</th>
  </tr>
  <tr>
    <td width=33%>
      <img src="https://github.com/FarhanSadaf/Emergency-app/blob/master/tutorials/3-welcome%20screen.jpg">
    </td>
    <td width=33%>
      <img src="https://github.com/FarhanSadaf/Emergency-app/blob/master/tutorials/4-home%20screen.jpg">
    </td>
  </tr>
  
  <tr>
    <th>Settings window</th>
    <th>Update / Delete contacts</th>
    <th>Advanced settings window</th>
  </tr>
  <tr>
    <td width=33%>
      <img src="https://github.com/FarhanSadaf/Emergency-app/blob/master/tutorials/5-settings%20screen.jpg">
    </td>
    <td width=33%>
      <img src="https://github.com/FarhanSadaf/Emergency-app/blob/master/tutorials/6-%20settings%20update%20numbers.jpg">
    </td>
    <td width=33%>
      <img src="https://github.com/FarhanSadaf/Emergency-app/blob/master/tutorials/7-advanced%20settings%20screen.jpg">
    </td>
  </tr>
  
  <tr>
    <th>Emergency mode initiated</th>
    <th>After 4+ tap on power button</th>
  </tr>
  <tr>
    <td width=33%>
      <img src="https://github.com/FarhanSadaf/Emergency-app/blob/master/tutorials/8-emergency.jpg">
    </td>
    <td width=33%>
      <img src="https://github.com/FarhanSadaf/Emergency-app/blob/master/tutorials/9-emergency%20after%20pressing%20power%20button.jpg">
    </td>
  </tr>
  
  <tr>
    <th>Emergency contacts window</th>
    <th>Detailed emergency contacts</th>
  </tr>
  <tr>
    <td width=33%>
      <img src="https://github.com/FarhanSadaf/Emergency-app/blob/master/tutorials/10-emergency%20contacts.jpg">
    </td>
    <td width=33%>
      <img src="https://github.com/FarhanSadaf/Emergency-app/blob/master/tutorials/11-emergency%20contacts%20(2).jpg">
    </td>
  </tr>
  
  <tr>
    <th>Sidebar</th>
  </tr>
  <tr>
    <td width=33%>
      <img src="https://github.com/FarhanSadaf/Emergency-app/blob/master/tutorials/12-side%20bar.jpg">
    </td>
  </tr>
</table>

## UNDER THE HOOD:

I used FireBase Authentication to sign the users into the app. 

FireBase Realtime Database is also used to store Numbers whom user want to send message in case of emergency and Name of the user.

Map API is used to get current location from GPS, to be precised, latitude and longitude.

JSON format is also used to parse Emergency Ambulance, Fire Service or Police numbers of different divisions.

I also used SharedPreference to store some additional detailed settings like 
- Setting up time counter
- Functionalities that should be used in Danger Mode
- Saving previous location if somehow phone can't parse current location from GPS.

As UI design elements, I used Custom TOOLBAR, CARDVIEW, LISTVIEW, EXPANDABLE LISTVIEW, NAVIGATION DRAWER, ALERT DIALOGUE, POP-UP DIALOGUE, SPINNER, CHECKBOX and so on.

As the app needs to be opened in background, i had to use Custom NOTIFICATION CHANNEL and BroadcastReceiver.

I also had to implement functionalities to make a CALL, send MESSAGE, send custom EMAIL or using VIBRATOR etc.

<strong><br/>Project - CSE 2200 (Advanced Programming)</strong>

## Licensing
Licensed under the [MIT License](LICENSE).
