# BACHAO
As name suggests, it is an emergency android application. Situations can arrive when one may need to let his family and friends know that he/she is in DANGER.
Though some mobile phones have a built-in app to tackle this situation, but most of them only calls to a emergency number only (999 or so).
So they lack some functionalities like MESSAGING the close ones to let them know that the victim is in danger or sending LOCATION of victim to them.
This app brings out just these.


<b>FEATURES:</b>

User can activate Danger Mode just TAPPING POWER BUTTON MORE THAN 4 TIMES (API level 28). Which will lead to 
1. Call emergency number (999 for BD)
2. Sending messages to the numbers user has set up (As much as user wants)
3. Sending their current location with message
4. User can also get numbers of AMBULANCE, POLICE, FIRE SERVICE of different divisions of Bangladesh and call these numbers straight.
5. User can sign in to his own account and save his information. So next time if he reinstall the app, there's no need to update information twice.


<b>UNDER THE HOOD:</b>

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
