
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
<img style="float: center;" src="https://i.imgur.com/xXTGtY0.png">
>
>Nella stessa barra si trovano le funzionalità chiave dell’applicazione,
>ovverosia l’icona per poter eseguire una nuova ricerca e l’icona rappresentante
>un cuore per poter impostare la città selezionata come preferita, in modo che,
>al contrario del primo avvio, questa sia direttamente mostrata senza doverla
>reinserire nuovamente tutte le volte successive.
>
<img style="float: center;" src="https://i.imgur.com/o2Etncy.png">
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
><img style="float: center;" src="https://i.imgur.com/p5Ugztm.png">
>
>In alternativa, per aggiornare le informazioni meteorologiche della città
>selezionata è sufficiente effettuare uno swipe verso il basso, richiamando la
>funzionalità SwipeToRefresh. In caso di errori nel reperire i dati o nel
>rappresentarli vengono mostrati degli avvisi all’utente mediante l’utilizzo di
>una Snackbar.
>
>![](media/6a065bc99fba52cb10e8a9e094825c6d.png)
>
>![](media/610dec45341bd3e2ea2615bd249ed5fd.png)
>
>![](media/ef899d4ca0941ab7bf2f5045ac59c2d7.png)
