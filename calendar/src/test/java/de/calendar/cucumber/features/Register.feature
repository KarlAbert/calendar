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
      | Enno    | Krämer   | arstulke     | enno@example.com | passwort |
    Dann wird das Registrieren verweigert


  Szenario: Registrieren mit belegter EMail-Adresse
    Gegeben sei der Benutzer mit folgenden Benutzerdaten:
      | Vorname | Nachname | Benutzername | E-Mail Adresse   | Passwort |
      | Hans    | Müller   | hamuelle     | hans@example.com | passwort |
    Wenn man sich mit folgenden Benutzerdaten registriert:
      | Vorname | Nachname | Benutzername | E-Mail Adresse   | Passwort |
      | Arne    | Stulken  | arstulke     | hans@example.com | passwort |
    Dann wird das Registrieren verweigert


  Szenario: Registrieren mit zu kurzem Benutzernamen
    Wenn man sich mit folgenden Benutzerdaten registriert:
      | Vorname | Nachname | Benutzername | E-Mail Adresse   | Passwort |
      | Egon    | Schmitt  | es           | egon@example.com | passwort |
    Dann wird das Registrieren verweigert


  Szenario: Registrieren ohne Vornamen
    Wenn man sich mit folgenden Benutzerdaten registriert:
      | Vorname | Nachname | Benutzername | E-Mail Adresse     | Passwort |
      |         | Becker   | becker       | becker@example.com | passwort |
    Dann wird das Registrieren verweigert


  Szenario: Registrieren ohne Nachnamen
    Wenn man sich mit folgenden Benutzerdaten registriert:
      | Vorname | Nachname | Benutzername | E-Mail Adresse    | Passwort |
      | Klaus   |          | klbaumann    | klaus@example.com | passwort |
    Dann wird das Registrieren verweigert


  Szenario: Registrieren ohne EMail-Adresse
    Wenn man sich mit folgenden Benutzerdaten registriert:
      | Vorname | Nachname | Benutzername | E-Mail Adresse | Passwort |
      | Dieter  | Schuster | dischust     |                | passwort |
    Dann wird das Registrieren verweigert


  Szenario: Registrieren mit zu langem Benutzername
    Wenn man sich mit folgenden Benutzerdaten registriert:
      | Vorname | Nachname | Benutzername                                                  | E-Mail Adresse      | Passwort |
      | Michael | Wolf     | DieserBenutzernameHatMehrAls20Stellen__iuehseiuhfsuwdwddweihfsjedrw | michael@example.com | passwort |
    Dann wird das Registrieren verweigert


  Szenario: Registrieren mit zu kurzem Passwort
    Wenn man sich mit folgenden Benutzerdaten registriert:
      | Vorname | Nachname | Benutzername | E-Mail Adresse   | Passwort |
      | Ingo    | Brandt   | inbrandt     | ingo@example.com | zukurz   |
    Dann wird das Registrieren verweigert