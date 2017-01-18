#language:de
#noinspection SpellCheckingInspection
Funktionalität: Registerieren

  Szenario: Registrieren mit richtigen Eingaben
    Wenn man sich mit folgenden Benutzerdaten registriert:
      | Vorname | Nachname | Benutzername | E-Mail Adresse   | Passwort |
      | Karl    | Abert    | kaabert      | karl@example.com | passwort |
    Dann ist das Registrieren erfolgreich


  Szenario: Registrieren mit vorhandenem Benutzernamen
    Gegeben sei der Benutzer mit folgenden Benutzerdaten:
      | Vorname | Nachname | Benutzername | E-Mail Adresse   | Passwort |
      | Arne    | Stulken  | arstulke     | arne@example.com | passwort |
    Wenn man sich mit folgenden Benutzerdaten registriert:
      | Vorname | Nachname | Benutzername | E-Mail Adresse   | Passwort |
      | Karl    | Abert    | arstulke     | karl@example.com | passwort |
    Dann wird das Registrieren verweigert


  Szenario: Registrieren mit belegter EMail-Adresse
    Gegeben sei der Benutzer mit folgenden Benutzerdaten:
      | Vorname | Nachname | Benutzername | E-Mail Adresse   | Passwort |
      | Arne    | Stulken  | arstulke     | arne@example.com | passwort |
    Wenn man sich mit folgenden Benutzerdaten registriert:
      | Vorname | Nachname | Benutzername | E-Mail Adresse   | Passwort |
      | Arne    | Stulken  | kaabert      | arne@example.com | passwort |
    Dann wird das Registrieren verweigert


  Szenario: Registrieren mit zu kurzem Benutzernamen
    Wenn man sich mit folgenden Benutzerdaten registriert:
      | Vorname | Nachname | Benutzername | E-Mail Adresse   | Passwort |
      | Karl    | Abert    | to           | karl@example.com | passwort |
    Dann wird das Registrieren verweigert


  Szenario: Registrieren ohen Vornamen
    Wenn man sich mit folgenden Benutzerdaten registriert:
      | Vorname | Nachname | Benutzername | E-Mail Adresse   | Passwort |
      |         | Abert    | kaabert      | karl@example.com | passwort |
    Dann wird das Registrieren verweigert


  Szenario: Registrieren ohne Nachnamen
    Wenn man sich mit folgenden Benutzerdaten registriert:
      | Vorname | Nachname | Benutzername | E-Mail Adresse   | Passwort |
      | Karl    |          | kaabert      | karl@example.com | passwort |
    Dann wird das Registrieren verweigert


  Szenario: Registrieren ohne EMail-Adresse
    Wenn man sich mit folgenden Benutzerdaten registriert:
      | Vorname | Nachname | Benutzername | E-Mail Adresse | Passwort |
      | Karl    | Abert    | kaabert      |                | passwort |
    Dann wird das Registrieren verweigert


  Szenario: Registrieren mit zu langem Benutzername
    Wenn man sich mit folgenden Benutzerdaten registriert:
      | Vorname | Nachname | Benutzername                          | E-Mail Adresse   | Passwort |
      | Karl    | Abert    | DieserBenutzernameHatMehrAls20Stellen | karl@example.com | passwort |
    Dann wird das Registrieren verweigert


  Szenario: Registrieren mit zu kurzem Passwort
    Wenn man sich mit folgenden Benutzerdaten registriert:
      | Vorname | Nachname | Benutzername | E-Mail Adresse   | Passwort |
      | Karl    | Abert    | kaabert      | karl@example.com | zukurz   |
    Dann wird das Registrieren verweigert