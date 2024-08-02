# B-RUB-MOTORSPORTS-02

## Important information for Websocket connection
In order to keep the login data for the websocket server secret it has to be added in individually.
For that two variables called USERNAME and PASSWORD have to be added to the local.properties file. 
These variables have to be set to the username and password which can be found in the dicussion forum. 
It is important to add these values **WITHOUT quotation marks**.


## App Usage
By starting the app you get directed to the overview. There you can see the mean value of the voltage
and temperature. Furthermore is a icon which can display exclamation marks or the word ok. If ok is 
seen there no error is currently there, but if one or more exclamation mark is seen there, that 
indicates one or more errors.

If using the bottom navigation bar on and pressing on the diagramm bar icon. You get directed to the
detailed View. There are all segments displayed and you can see the cells and their voltage there.
Above the bar chart is an icon where you can see if segement x has an error. By clicking on a 
certain fragment you get to the detailed view of it. There should be the cells displayed.
Inside the bar should be the cell and its Voltage. Below that you can see the temperature
also displayed by a bar chart. If you scroll down the next thing you should see is some
information about the cell balacing and below that the whole error log. On the top left of
the screen should an error displayed if you click on that you will be directed to the view 
again where you should see each segement

By pressing on the gear icon on the bottom navigation bar you will be directed to the settings.
The first section is a switch with a Password Protection if it is turned on you will be directed
to the overview and you can't use the bottom navigation bar until the right password is passed to
the pop up, which should appear by using the bottom navigation bar. The next section from the settings
is displayed as "Password" with an edit button. By pressing this button a pop up should appear which gets
two inputs. The first input is the old password. **If you set the password for the first time the old password**
**is an empty string.** Below that is the sentry mode which can be turned on and off with a switch. Below that
is the section "Email for Warning" there will be a list of the stored mail. You are also able to edit the list
by removing or adding some elements. The last two one is a tablet mode which should display all segements in a view
and the other one is a lightmode switch, where you can change between a light or darkmode.
