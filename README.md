## *Material Meteo*
>Il progetto prevede lo sviluppo di un’applicazione meteo che sia in
>grado di interfacciarsi con i servizi offerti da Yahoo tramite un proprio set di
>API, in modo di permettere la ricerca di luoghi ed ottenere per essi
>informazioni sulle condizioni meteorologiche.

## Yahoo API e linguaggio YQL

Yahoo Developer Network mediante un proprio servizio Web mette a disposizione
diverse risorse categorizzate in dei database, che possono essere interrogati
tramite delle API con l’ausilio del linguaggio YQL. YQL è un linguaggio
proprietario sviluppato da Yahoo, mantenendo comunque una sintassi riconducibile
alla famiglia dei linguaggi SQL.

L’accesso alle informazioni avviene mediante richieste HTTP GET opportunamente
formattate, indirizzate alla API
‘*http://query.yahooapis.com/v1/[POINT_ACCESS]/yql?q=’*, che producono a loro
volta delle risposte in formato JSON oppure XML, a seconda delle necessità.

Yahoo implementa due differenti punti di accesso ai propri database: un punto di
accesso pubblico ed uno privato con autenticazione mediante una API KEY.
Entrambe le modalità consentono l’accesso alle stesse medesime risorse, tuttavia
con delle restrizioni differenti:

|                        | **Pubblico**              | **Oauth con API KEY**            |
|------------------------|---------------------------|----------------------------------|
| **YQL Endpoint**       | /v1/public/\*             | /v1/yql/\*                       |
| **Limite orario**      | 2000 richieste/ora per IP | 20000 richieste/ora per IP       |
| **Limite giornaliero** | Nessuno                   | 100000 richieste/ora per API KEY |

In fase di progettazione dell’applicazione in via generale è pertanto necessario
tener conto delle rispettive restrizioni applicate, in modo da adottare la
soluzione che più rispecchia le necessità ed il contesto in cui l’applicazione
sarà utilizzata.

Per la fattispecie, nella realizzazione della nostra applicazione meteo abbiamo
valutato che il canale pubblico offre prestazioni sufficienti a soddisfare i
requisiti richiesti.

Entrambe le modalità di accesso consentono l’utilizzo della piattaforma Yahoo
Developer Network per fini commerciali a seguito dell’approvazione della
specifica richiesta sottomessa al reparto dedicato, e per entrambe le modalità
di accesso Yahoo garantisce la stessa continuità di servizio, sia in termini di
performance (99.5% delle richieste soddisfatte in media), sia in termini
temporali.

Infatti Yahoo sia per il canale pubblico che per il canale tramite
autenticazione con chiave privata garantisce di avvisare gli sviluppatori di
eventuali modifiche o cessazioni del servizio con un arco temporale di preavviso
pari ad almeno 6 mesi, un aspetto che risulta essere fondamentale e da tenere in
considerazione in fase progettuale.

Le informazioni geografiche sono memorizzate nella tabella Geo.places

| **Nome chiave** | **Descrizione**                                   |
|-----------------|---------------------------------------------------|
| woeid           | Where On Earth ID                                 |
| text            | Nome del luogo                                    |
| lang            | Lingua desiderata per il nome del luogo (ISO 639) |

L’identificazione dei luoghi avviene tramite la chiave WOEID, Where On Earth ID,
un identificatore a 32 bit univoco e quindi non ripetitivo, che è associato a
tutte le entità presenti nel sistema.

Un codice WOIED una volta assegnato non viene mai cambiato oppure riutilizzato;
se per qualsiasi motivo (es: geografico, politico, strutturale) un WOID diventa
deprecato, esso viene automaticamente mappato al suo successore od erede,
cercando quindi di garantire la continuità di servizio per le richieste
formulate con codici deprecati.

Il parametro opzionale lang permette di localizzare nella lingua espressa,
codificata nello standard ISO 639, i risultati geografici forniti. Pur essendo
un parametro opzionale in realtà si tratta di una funzionalità molto importante
per l’esperienza d’uso dell’utente e che quindi è un aspetto che va tenuto
altamente in considerazione per poi poterlo effettivamente implementare.

Le informazioni meteorologiche sono invece memorizzate nella tabella
Weather.forecast, che può essere interrogata specificando come parametri di
ricerca le chiavi:

| Parametro | Tipologia    | Descrizione                                            |
|-----------|--------------|--------------------------------------------------------|
| woeid     | Obbligatorio | Woeid del luogo per il quale si cercano i dati meteo   |
| u         | Facoltativo  | Set di unità di misura in cui esprimere i dati forniti |

Il parametro u permette di scegliere il sistema di unità di misura con il quale
si vuole che i dati vengano espressi. Il suo valore *u=*‘*c*‘ abilita la scelta
del sistema Metrico, fornendo la velocità espressa in Km/h, la pressione in
mbar, la distanza in Km e la temperatura in °C, mentre il valore *u=*‘*f*‘
imposta il sistema Imperiale che esprime la velocità in miglia orarie mph, la
distanza in miglia e la temperatura in °F.

Di default, non inserendo il parametro facoltativo, viene adottato il sistema
Imperiale.

I dati meteorologici ottenuti invece come risultato della interrogazione
riguardano dati sulla condizione attuale e futura, fornendo: temperatura,
direzione e velocità del vento, valori di umidità, visibilità, pressione, orario
di alba e tramonto, set di unità di misura utilizzato, e codice numerico
rappresentante la condizione attuale/futura tramite un pacchetto di immagini
integrato.

Tale pacchetto di immagini risulta essere esterno alle API stesse, ed è reso
disponibile ed accessibile all’indirizzo
*http://l.yimg.com/a/i/us/we/52/[CODICE_CONDIZIONE].gif*, dove il parametro
[CODICE_CONDIZIONE] è proprio quello fornito come risposta dall’interrogazione
alla tabella Weather.forecast

Segue tabella con spiegazione del significato associato a ciascun codice:

| *0*    | *tornado*              | *16* | *snow*                  | *32* | *sunny*                   |
|--------|------------------------|------|-------------------------|------|---------------------------|
| *1*    | *tropical storm*       | *17* | *hail*                  | *33* | *fair (night)*            |
| *2*    | *hurricane*            | *18* | *sleet*                 | *34* | *fair (day)*              |
| *3*    | *severe thunderstorms* | *19* | *dust*                  | *35* | *mixed rain and hail*     |
| *4*    | *thunderstorms*        | *20* | *foggy*                 | *36* | *hot*                     |
| *5*    | *mixed rain and snow*  | *21* | *haze*                  | *37* | *isolated thunderstorms*  |
| *6*    | *mixed rain and sleet* | *22* | *smoky*                 | *38* | *scattered thunderstorms* |
| *7*    | *mixed snow and sleet* | *23* | *blustery*              | *39* | *scattered thunderstorms* |
| *8*    | *freezing drizzle*     | *24* | *windy*                 | *40* | *scattered showers*       |
| *9*    | *drizzle*              | *25* | *cold*                  | *41* | *heavy snow*              |
| *10*   | *freezing rain*        | *26* | *cloudy*                | *42* | *scattered snow showers*  |
| *11*   | *showers*              | *27* | *mostly cloudy (night)* | *43* | *heavy snow*              |
| *12*   | *showers*              | *28* | *mostly cloudy (day)*   | *44* | *partly cloudy*           |
| *13*   | *snow flurries*        | *29* | *partly cloudy (night)* | *45* | *thundershowers*          |
| *14*   | *light snow showers*   | *30* | *partly cloudy (day)*   | *46* | *snow showers*            |
| *15*   | *blowing snow*         | *31* | *clear (night)*         | *47* | *isolated thundershowers* |
| *3200* | *not available*        |      |                         |      |                           |

Pur non facendo parte integrante delle API, l’indirizzo web con il quale vengono
fornite le immagini che rappresentano la condizione meteo gode della stessa
garanzia di continuità di servizio offerta alle risorse del Yahoo Develpoment
Network, e in conseguenza cessazioni o modifiche del servizio avverranno con un
preavviso agli sviluppatori di almeno 6 mesi, al fine di poter rendere
compatibile le proprie applicazione con le novità introdotte.

Pertanto ai fini del progetto per ottenere le immagini meteorologiche basterà
effettuare una connessione http utilizzando l’indirizzo web attualmente in uso,
parametrizzato dal codice della condizione ottenuta dall’interrogazione.

>Yahoo Developer Network mediante un proprio servizio Web mette a disposizione
>diverse risorse categorizzate in dei database, che possono essere interrogati
>tramite delle API con l’ausilio del linguaggio YQL. YQL è un linguaggio
>proprietario sviluppato da Yahoo, mantenendo comunque una sintassi riconducibile
>alla famiglia dei linguaggi SQL.
>
>L’accesso alle informazioni avviene mediante richieste HTTP GET opportunamente
>formattate, indirizzate alla API
>‘*http://query.yahooapis.com/v1/[POINT_ACCESS]/yql?q=’*, che producono a loro
>volta delle risposte in formato JSON oppure XML, a seconda delle necessità.
>
>Yahoo implementa due differenti punti di accesso ai propri database: un punto di
>accesso pubblico ed uno privato con autenticazione mediante una API KEY.
>Entrambe le modalità consentono l’accesso alle stesse medesime risorse, tuttavia
>con delle restrizioni differenti:
>
>|                        | **Pubblico**              | **Oauth con API KEY**            |
>|------------------------|---------------------------|----------------------------------|
>| **YQL Endpoint**       | /v1/public/\*             | /v1/yql/\*                       |
| **Limite orario**      | 2000 richieste/ora per IP | 20000 richieste/ora per IP       |
>| **Limite giornaliero** | Nessuno                   | 100000 richieste/ora per API KEY |
>
>In fase di progettazione dell’applicazione in via generale è pertanto necessario
>tener conto delle rispettive restrizioni applicate, in modo da adottare la
>soluzione che più rispecchia le necessità ed il contesto in cui l’applicazione
>sarà utilizzata.
>
>Entrambe le modalità di accesso consentono l’utilizzo della piattaforma Yahoo
>Developer Network per fini commerciali a seguito dell’approvazione della
>specifica richiesta sottomessa al reparto dedicato, e per entrambe le modalità
>di accesso Yahoo garantisce la stessa continuità di servizio, sia in termini di
>performance (99.5% delle richieste soddisfatte in media), sia in termini
>temporali.
>
>Infatti Yahoo sia per il canale pubblico che per il canale tramite
>autenticazione con chiave privata garantisce di avvisare gli sviluppatori di
>eventuali modifiche o cessazioni del servizio con un arco temporale di preavviso
>pari ad almeno 6 mesi, un aspetto che risulta essere fondamentale e da tenere in
considerazione in fase progettuale.
>
>Le informazioni geografiche sono memorizzate nella tabella Geo.places
>
>| **Nome chiave** | **Descrizione**                                   |
>|-----------------|---------------------------------------------------|
>| woeid           | Where On Earth ID                                 |
>| text            | Nome del luogo                                    |
>| lang            | Lingua desiderata per il nome del luogo (ISO 639) |
>
>L’identificazione dei luoghi avviene tramite la chiave WOEID, Where On Earth ID,
>un identificatore a 32 bit univoco e quindi non ripetitivo, che è associato a
>tutte le entità presenti nel sistema.
>
>Un codice WOIED una volta assegnato non viene mai cambiato oppure riutilizzato;
>se per qualsiasi motivo (es: geografico, politico, strutturale) un WOID diventa
>deprecato, esso viene automaticamente mappato al suo successore od erede,
>cercando quindi di garantire la continuità di servizio per le richieste
>formulate con codici deprecati.
>
>Il parametro opzionale lang permette di localizzare nella lingua espressa,
>codificata nello standard ISO 639, i risultati geografici forniti. Pur essendo
>un parametro opzionale in realtà si tratta di una funzionalità molto importante
>per l’esperienza d’uso dell’utente e che quindi è un aspetto che va tenuto
>altamente in considerazione per poi poterlo effettivamente implementare.
>
>Le informazioni meteorologiche sono invece memorizzate nella tabella
>Weather.forecast, che può essere interrogata specificando come parametri di
>ricerca le chiavi:
>
>| Parametro | Tipologia    | Descrizione                                            |
>|-----------|--------------|--------------------------------------------------------|
>| woeid     | Obbligatorio | Woeid del luogo per il quale si cercano i dati meteo   |
>| u         | Facoltativo  | Set di unità di misura in cui esprimere i dati forniti |
>
>Il parametro u permette di scegliere il sistema di unità di misura con il quale
>si vuole che i dati vengano espressi. Il suo valore *u=*‘*c*‘ abilita la scelta
>del sistema Metrico, fornendo la velocità espressa in Km/h, la pressione in
>mbar, la distanza in Km e la temperatura in °C, mentre il valore *u=*‘*f*‘
>imposta il sistema Imperiale che esprime la velocità in miglia orarie mph, la
>distanza in miglia e la temperatura in °F.
>
>Di default, non inserendo il parametro facoltativo, viene adottato il sistema
>Imperiale.
>
>I dati meteorologici ottenuti invece come risultato della interrogazione
>riguardano dati sulla condizione attuale e futura, fornendo: temperatura,
>direzione e velocità del vento, valori di umidità, visibilità, pressione, orario
>di alba e tramonto, set di unità di misura utilizzato, e codice numerico
>rappresentante la condizione attuale/futura tramite un pacchetto di immagini
>integrato.
>
>Tale pacchetto di immagini risulta essere esterno alle API stesse, ed è reso
>disponibile ed accessibile all’indirizzo
>*http://l.yimg.com/a/i/us/we/52/[CODICE_CONDIZIONE].gif*, dove il parametro
>[CODICE_CONDIZIONE] è proprio quello fornito come risposta dall’interrogazione
>alla tabella Weather.forecast
>
Segue tabella con spiegazione del significato associato a ciascun codice:

| *0*    | *tornado*              | *16* | *snow*                  | *32* | *sunny*                   |
|--------|------------------------|------|-------------------------|------|---------------------------|
| *1*    | *tropical storm*       | *17* | *hail*                  | *33* | *fair (night)*            |
| *2*    | *hurricane*            | *18* | *sleet*                 | *34* | *fair (day)*              |
| *3*    | *severe thunderstorms* | *19* | *dust*                  | *35* | *mixed rain and hail*     |
| *4*    | *thunderstorms*        | *20* | *foggy*                 | *36* | *hot*                     |
| *5*    | *mixed rain and snow*  | *21* | *haze*                  | *37* | *isolated thunderstorms*  |
| *6*    | *mixed rain and sleet* | *22* | *smoky*                 | *38* | *scattered thunderstorms* |
| *7*    | *mixed snow and sleet* | *23* | *blustery*              | *39* | *scattered thunderstorms* |
| *8*    | *freezing drizzle*     | *24* | *windy*                 | *40* | *scattered showers*       |
| *9*    | *drizzle*              | *25* | *cold*                  | *41* | *heavy snow*              |
| *10*   | *freezing rain*        | *26* | *cloudy*                | *42* | *scattered snow showers*  |
| *11*   | *showers*              | *27* | *mostly cloudy (night)* | *43* | *heavy snow*              |
| *12*   | *showers*              | *28* | *mostly cloudy (day)*   | *44* | *partly cloudy*           |
| *13*   | *snow flurries*        | *29* | *partly cloudy (night)* | *45* | *thundershowers*          |
| *14*   | *light snow showers*   | *30* | *partly cloudy (day)*   | *46* | *snow showers*            |
| *15*   | *blowing snow*         | *31* | *clear (night)*         | *47* | *isolated thundershowers* |
| *3200* | *not available*        |      |                         |      |                           |

Pur non facendo parte integrante delle API, l’indirizzo web con il quale vengono
fornite le immagini che rappresentano la condizione meteo gode della stessa
garanzia di continuità di servizio offerta alle risorse del Yahoo Develpoment
Network, e in conseguenza cessazioni o modifiche del servizio avverranno con un
preavviso agli sviluppatori di almeno 6 mesi, al fine di poter rendere
compatibile le proprie applicazione con le novità introdotte.

Pertanto ai fini del progetto per ottenere le immagini meteorologiche basterà
effettuare una connessione http utilizzando l’indirizzo web attualmente in uso,
parametrizzato dal codice della condizione ottenuta dall’interrogazione.


# 1.  Avvio.

>L’applicazione all’avvio presenta un AlerDialog che invita l’utente ad
>effettuare due scelte: inserire il nome della città di cui disidera conoscere le
>condizioni meteorologiche e specificare con quale sistema di unità di misura
>internazionale esprimere e rappresentare le informazioni ottenute.
>
><img style="float: left;" src="https://i.imgur.com/ZWkxJxt.png">
>
><img style="float: left;" src="https://i.imgur.com/6Kar99N.png">
>
>
>Per i dispositivi che lo supportano, è possibile anche inserire
>il nome della città pronunciandolo a voce utilizzando la
>modalità Voice to Speech, che si attiva cliccando
>sull’icona a forma di microfono dedicata della tastiera.
>Affinché tale funzionalità possa essere utilizzata è
>indispensabile che sul dispositivo vi sia installato e sia
>attivo almeno il motore di sintesi vocale Google,
>normalmente presente come componente nativo di
>sistema.


## 1.  Presentazione dei risultati

>Una volta che l’utente ha inserito la città nel campo di ricerca e dato
>conferma, in caso vengano trovati più risultati conformi questi vengono mostrati
>tramite un AlerDialog parametrizzato con una ListView, dando la possibilità
>all’utente medesimo di scegliere la corrispondenza consona alle iniziali
>intenzioni di ricerca.
>
><img style="float: left;" src="https://i.imgur.com/jB1hPNh.png">
>
><img style="float: left;" src="https://i.imgur.com/0LrxULi.png">
>
>In caso invece per la città inserita venga rilevata una sola corrispondenza,
>questa viene automaticamente selezionata e mostrata nella grafica principale
>dell’applicazione.


## 1.  La Toolbar

>
>In alto a sinistra è riportato il nome della città
>selezionata, seguito dall’eventuale
>Regione/Distretto e Stato.
>
><img style="float: center;" src="https://i.imgur.com/xXTGtY0.png">
>
>
>Nella stessa barra si trovano le funzionalità chiave dell’applicazione,
>ovverosia l’icona per poter eseguire una nuova ricerca e l’icona rappresentante
>un cuore per poter impostare la città selezionata come preferita, in modo che,
>al contrario del primo avvio, questa sia direttamente mostrata senza doverla
>reinserire nuovamente tutte le volte successive.
>
><img style="align: center;" src="https://i.imgur.com/o2Etncy.png">
>
>Cliccando sull’icona si ricerca il menù si espande facendo comparire una casella
>di input dove l’utente può inserire la nuova interrogazione da sottomettere.
>
>Come funzionalità aggiuntiva anche in questo caso è possibile inserire la città
>semplicemente pronunciandola cliccando l’icona a forma di microfono per far
>partire la funzionalità. Cliccando l’icona a forma di cuore, prima di impostare
>la città come preferita, viene mostrato un AlterDialog di conferma.
>
><img style="float: left;" src="https://i.imgur.com/59AxtRp.png">
>
><img style="float: left;" src="https://i.imgur.com/RdpUTeB.png">
>

## 1.  Azioni aggiuntive

>Cliccando sull’ultimo pulsante a destra presente nella Toolbar è possibile
>richiamare un menù che offre la possibilità di aggiornare le informazioni
>meteorologiche della città selezionata, cancellare ogni tipologia di preferenza
>dell’applicazione salvata sul dispositivo sia per la città preferita che per il
>set di unità di misura, ed infine la possibilità di modificare quest’ultimo on
>the fly senza dover riavviare l’applicazione, aggiornando contestualmente quindi
>i dati rappresentati.
>
><img style="float: center;" src="https://i.imgur.com/p5Ugztm.png">
>
>In alternativa, per aggiornare le informazioni meteorologiche della città
>selezionata è sufficiente effettuare uno swipe verso il basso, richiamando la
>funzionalità SwipeToRefresh. In caso di errori nel reperire i dati o nel
>rappresentarli vengono mostrati degli avvisi all’utente mediante l’utilizzo di
>una Snackbar.
>
><img style="float: left;" src="https://i.imgur.com/UJRrPz2.png">
>
><img style="float: left;" src="https://i.imgur.com/Yur1BDh.png">
>
