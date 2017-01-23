#language:de
#noinspection SpellCheckingInspection
Funktionalität: ganztägige Kalenderereignisse

  Szenario: ganztägiges Ereignis erstellen
    Wenn TestUser ein ganztägiges Ereigniss am "31.02.2017" mit dem Titel "Karls Geburtstag" erstellt
    Dann steht das ganztägige Ereignis "Karls Geburtstag" am "31.02.2017" in dem Kalender von TestUser


  Szenario: ganztägiges Ereignis bearbeiten
    Gegeben sei das ganztägige Ereignis "Karls Geburtstag" am "21.01.2017" in dem Kalendar von TestUser
    Wenn TestUser das ganztägige Ereignis "Karls Geburtstag" am "21.01.2017" zu "Marens Geburtstag" am "23.10.2017" ändert
    Dann steht das ganztägige Ereignis "Marens Geburtstag" am "23.10.2017" in dem Kalender von TestUser


  Szenario: Ereignis löschen
    Gegeben sei das ganztägige Ereignis "Karls Geburtstag" am "21.01.2017" in dem Kalendar von TestUser
    Wenn TestUser das ganztägige Ereignis "Karls Geburtstag" am "21.01.2017" löscht
    Dann existiert kein Ereignis am "21.01.2017" im Kalendar von TestUser


  Szenario: Ereignis in einem Zeitraum abrufen
    Gegeben sei das ganztägige Ereignis "Karls Geburtstag" am "21.01.2017" in dem Kalendar von TestUser
    Und das ganztägige Ereignis "Arnes Geburtstag" am "08.05.2017" in dem Kalendar von TestUser
    Und das ganztägige Ereignis "Marens Geburtstag" am "23.10.2017" in dem Kalendar von TestUser
    Wenn TestUser die Ereignisse zwischen dem "20.01.2017" und dem "10.05.2017" anzeigen lässt
    Dann werden TestUser folgende Ergebnisse zurückgegeben:
      | Karls Geburtstag | 21.01.2017 |
      | Arnes Geburtstag | 08.05.2017 |