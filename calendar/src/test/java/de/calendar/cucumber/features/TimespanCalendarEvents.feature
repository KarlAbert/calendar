#language:de
#noinspection SpellCheckingInspection
Funktionalität: Kalendarereignisse mit Zeitangabe

  #Szenario: Ereignis mit Zeitangabe erstellen
   # Wenn TestUser ein Ereigniss am "21.01.2017" mit dem Titel "Karls Geburtstagparty" von "20:15"Uhr bis "23:45"Uhr erstellt
  #  Dann steht das Ereignis "Karls Geburtstagparty" am "21.01.2017" von "20:15"Uhr bis "23:45"Uhr in dem Kalender von TestUser


 # Szenario: Ereignis mit Zeitangabe bearbeiten
   # Gegeben sei das Ereignis "Karls Geburtstagsparty" am "21.01.2017" von "20:15"Uhr bis "23:45"Uhr in dem Kalendar von TestUser
   # Wenn TestUser das Ereignis "Karls Geburtstag" am "21.01.2017" von "20:15"Uhr bis "23:45"Uhr zu "Marens Geburtstag" am "23.10.2017" zwischen "15:00"Uhr und "18:30"Uhr ändert
   # Dann steht das Ereignis "Marens Geburtstag" am "23.10.2017" von "15:00"Uhr bis "18:30"Uhr in dem Kalender von TestUser
    #Dann wurde das Ereignis verschoben und umbenannt


  Szenariogrundriss: Ereignis mit unvollständiger Zeitangabe erstellen
    Wenn TestUser ein Ereigniss mit dem Titel "<titel>" von "<von>"Uhr bis "<bis>"Uhr erstellt
    Dann steht das Ereignis "<title>" von "<von_ergebnis>"Uhr bis "<bis_ergebnis>"Uhr in dem Kalender von TestUser

    Beispiele:
      | title            | von              | bis              | von_ergebnis     | bis_ergebnis      |
      | Karls Geburtstag | 21.01.2017 20:15 | 21.01.2017 23:45 | 21.01.2017 20:15 | 21.01.2017 23:45 |
      | Karls Geburtstag | 21.01.2017 20:15 |                  | 21.01.2017 20:15 | 21.01.2017 21:15 |
      | Karls Geburtstag |                  | 21.01.2017 23:45 | 21.01.2017 22:45 | 21.01.2017 23:45 |

  Szenariogrundriss: Ereignis mit unvollständiger Zeitangabe erstellen
    Gegeben sei das Ereignis "<titel>"  von "<von>"Uhr bis "<bis>"Uhr in dem Kalendar von TestUser
    Wenn TestUser das Ereignis "<titel>" von "<von>"Uhr bis "<bis>"Uhr zu "<neuer_titel>" am "<new_date>" zwischen "<neu_von>"Uhr und "<neu_bis>"Uhr ändert
    Dann steht das Ereignis "<neuer_titel>" von "<von_ergebnis>"Uhr bis "<bis_ergebnis>"Uhr in dem Kalender von TestUser

    Beispiele:
      | titel            | von              | bis              | neuer_titel       | neu_von          | neu_bis          | von_ergebnis     | bis_ergebnis     |
      | Karls Geburtstag | 21.01.2017 20:15 | 21.01.2017 23:45 | Marens Geburtstag | 23.10.2017 15:00 | 23.10.2017 18:00 | 23.10.2017 15:00 | 23.10.2017 18:00 |
      | Karls Geburtstag | 21.01.2017 20:15 | 21.01.2017 23:45 | Marens Geburtstag | 23.10.2017 15:00 |                  | 23.10.2017 15:00 | 23.10.2017 16:00 |
      | Karls Geburtstag | 21.01.2017 20:15 | 21.01.2017 23:45 | Marens Geburtstag |                  | 23.10.2017 18:00 | 23.10.2017 17:00 | 23.10.2017 18:00 |