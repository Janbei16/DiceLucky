Mobile Glücksspiel-App README

Überblick:
Diese Mobile Glücksspiel-App wurde entwickelt, um ein unterhaltsames Spielerlebnis auf Samsung Galaxy S10e und anderen kompatiblen Geräten zu bieten. Mit dieser App können Benutzer mit virtuellen Coins spielen und ihre Glücksspiel-Fähigkeiten testen.

Autor: Jannis Beichler

Funktionen:

Für die Funktion onSensorChanged habe ich diese Seite verwendet und dann alles zusammen geklickt https://gist.github.com/iewnait/2138807
Teilweise kopiert von dieser Seite https://developer.android.com/develop/sensors-and-location/sensors/sensors_overview#java in der Klasse MainActivity.java Zeile 44-52
![alt text](image-1.png)

- Coins und Dicen: Zu Beginn erhält der Benutzer 100 Coins, mit denen er spielen kann. Er kann diese Coins verwenden, um zu Dicen und möglicherweise mehr Coins zu gewinnen.

- Zwei Dicen-Methoden: Der Benutzer kann entweder das Handy schütteln oder einen Button drücken, um zu Dicen. Beim Schütteln des Handys kann er jede Sekunde ein Dice machen. Beim Drücken des Buttons kann er so oft er möchte in der Sekunde Dicen.

- Gewinnvibration: Wenn der Benutzer gewinnt, wird das Handy vibrieren, um den Erfolg zu signalisieren.

- Automatische Aufladung: Jede minute wird automatisch 1 zusätzlicher Coin dem Guthaben hinzugefügt,wenn der Benuter auf der App ist, damit der Benutzer weiterspielen kann, wenn das Geld ausgeht.

- Benutzerdefiniertes Logo: Die App verfügt über ein eigenes Logo, das sie einzigartig macht.

- Datenpersistenz: Die Spielerdaten werden zuverlässig in einem SharedPreferences gespeichert, sodass der Benutzer die App pausieren oder schließen kann, ohne seine Fortschritte zu verlieren.

Installation und Verwendung:
Um die App zu installieren bitte diesen Schritten Folgen:

- Ordner entzipen um alles zu sehen!!

Lade die APK-Datei auf deinem Samsung Galaxy S10e oder ein kompatibles Android-Gerät herunter.

Installiere die APK-Datei auf deinem Gerät, indem man den Anweisungen auf dem Bildschirm folgen.

Starten Sie die App und beginnen Sie mit dem Spielen, indem Sie die Anweisungen auf dem Bildschirm befolgen.

Linter nutzen:

- Um Linter zu nutzen muss man "gradlew lint" im Terminal eingeben
- Bei mir wird das aber nicht gehen, da lint Java Version 17 verlangt und ich bin auf Version 8.1
