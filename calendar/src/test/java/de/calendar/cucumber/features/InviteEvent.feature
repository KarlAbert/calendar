#language:de
#noinspection SpellCheckingInspection
Funktionalität: Einladen zu einem bestehenden Event
    Szenario: Erfolgreiches Hinzufügen eines Benutzers
        Gegeben sei das ganztägige Ereignis "Ueberraschungsparty" am "23.10.2017" in dem Kalendar von TestUser
        Wenn TestUser den Einladelink vom Ereignis "Ueberraschungsparty" am "23.10.2017" generiert
        Und "BenutzerNeu" den Einladelink aufruft
        Dann nimmt "BenutzerNeu" an dem Ereignis "Ueberraschungsparty" am "23.10.2017" von TestUser teil
        
    Szenario: Abgelaufener Einladelink
       Gegeben sei das ganztägige Ereignis "Ueberraschungsparty" am "23.10.2017" in dem Kalendar von TestUser
       Wenn TestUser den Einladelink vom Ereignis "Ueberraschungsparty" am "23.10.2017" generiert
       Und "BenutzerNeu" den Einladelink aufruft
       Dann nimmt "BenutzerNeu" an dem Ereignis "Ueberraschungsparty" am "23.10.2017" von TestUser teil
       Wenn "BenutzerAnders" den Einladelink aufruft
       Dann wird die Einladung abgelehnt


    Szenario: Unerlaubtes erstellen eines Einladelinks
        Gegeben sei das ganztägige Ereignis "Ueberraschungsparty von Arne" am "23.10.2017" in dem Kalendar von "Arne"
        Wenn TestUser den Einladelink vom Ereignis "Ueberraschungsparty von Arne" am "23.10.2017" generiert
        Dann wird die Anfrage abgelehnt

    Szenario: Erstellen eines Einladelinks von unbekanntem Ereignis
        Gegeben sei das ganztägige Ereignis "Ueberraschungsparty" am "23.10.2017" in dem Kalendar von TestUser
        Wenn TestUser den Einladelink vom Ereignis "Marens Ueberraschungsparty" am "23.10.2017" generiert
        Dann wird die Anfrage abgelehnt
