#language:de
#noinspection SpellCheckingInspection
Funktionalität: Login

  Szenario: Einloggen mit validen Daten
    Gegeben sei der Benutzer mit folgenden Benutzerdaten:
      | Vorname | Nachname | Benutzername | E-Mail Adresse    | Passwort  |
      | Peter   | Busch    | pebusch      | peter@example.com | passwort  |
    Wenn man sich mit dem Benutzername "pebusch" und dem Passwort "passwort" anmeldet
    Dann wird der Zugriff gewährt


  Szenario: Einloggen ohne Benutzernamen
    Wenn man sich mit dem Benutzername "" und dem Passwort "passwort" anmeldet
    Dann wird der Zugriff verweigert


  Szenario: Einloggen ohne Passwort
    Wenn man sich mit dem Benutzername "pebusch" und dem Passwort "" anmeldet
    Dann wird der Zugriff verweigert


  Szenario: Einloggen mit unbekantem Benutzer
    Wenn man sich mit dem Benutzername "unbekannt" und dem Passwort "passwort" anmeldet
    Dann wird der Zugriff verweigert


  Szenario: Einloggen mit dem falschen Passwort
    Gegeben sei der Benutzer mit folgenden Benutzerdaten:
      | Vorname | Nachname | Benutzername | E-Mail Adresse    | Passwort  |
      | Olaf    | Jäger    | oljaeger     | olaf@example.com  | passwort  |
    Wenn man sich mit dem Benutzername "oljaeger" und dem Passwort "passwort2" anmeldet
    Dann wird der Zugriff verweigert