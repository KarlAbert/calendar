#language:de
#noinspection SpellCheckingInspection
Funktionalität: Kalendarereignisse mit Zeitangabe
  Szenariogrundriss: Ereignis mit Zeitangabe erstellen
    Wenn TestUser ein Ereigniss mit dem Titel "<titel>" von "<von>"Uhr bis "<bis>"Uhr erstellt
    Dann steht das Ereignis "<titel>" von "<von_ergebnis>"Uhr bis "<bis_ergebnis>"Uhr in dem Kalender von TestUser

    Beispiele:
      | titel            | von              | bis              | von_ergebnis     | bis_ergebnis     |
      | Karls Geburtstag | 21.01.2017 20:15 | 21.01.2017 23:45 | 21.01.2017 20:15 | 21.01.2017 23:45 |
      | Karls Geburtstag | 21.01.2017 20:15 |                  | 21.01.2017 20:15 | 21.01.2017 21:15 |
      | Karls Geburtstag |                  | 21.01.2017 23:45 | 21.01.2017 22:45 | 21.01.2017 23:45 |

  Szenariogrundriss: Ereignis mit Zeitangabe bearbeiten
    Gegeben sei das Ereignis "<titel>"  von "<von>"Uhr bis "<bis>"Uhr in dem Kalendar von TestUser
    Wenn TestUser das Ereignis "<titel>" von "<von>"Uhr bis "<bis>"Uhr zu "<neuer_titel>" zwischen "<neu_von>"Uhr und "<neu_bis>"Uhr ändert
    Dann steht das Ereignis "<neuer_titel>" von "<von_ergebnis>"Uhr bis "<bis_ergebnis>"Uhr in dem Kalender von TestUser

    Beispiele:
      | titel            | von              | bis              | neuer_titel       | neu_von          | neu_bis          | von_ergebnis     | bis_ergebnis     |
      | Karls Geburtstag | 21.01.2016 20:15 | 21.01.2016 23:45 | Marens Geburtstag | 23.10.2017 15:00 | 23.10.2017 18:00 | 23.10.2017 15:00 | 23.10.2017 18:00 |
      | Karls Geburtstag | 21.01.2016 20:15 | 21.01.2016 23:45 | Marens Geburtstag | 23.10.2017 15:00 |                  | 23.10.2017 15:00 | 23.10.2017 16:00 |
      | Karls Geburtstag | 21.01.2016 20:15 | 21.01.2016 23:45 | Marens Geburtstag |                  | 23.10.2017 18:00 | 23.10.2017 17:00 | 23.10.2017 18:00 |