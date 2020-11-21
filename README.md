# PGR301-DevOps-Exam-H2020
Github Repository for H2020 DevOps Class PGR301 Exam, Application Github

**Travis CI Builder:**  
- [![Build Status](https://travis-ci.com/JonPus/PGR301-DevOps-Exam-H2020.svg?token=WNYDyxATS1ezQLqAT1RT&branch=master)](https://travis-ci.com/JonPus/PGR301-DevOps-Exam-H2020)

**StatusCake:**
- <a href="https://www.statuscake.com" title="Website Uptime Monitoring"><img src="https://app.statuscake.com/button/index.php?Track=5745939&Days=1&Design=2" /></a>

### Beskrivelse
Denne Github Repository inneholder en som inneholder en simpel applikasjon som man kan * `GET` og `POST` i API endpoints.

Dette er bare en simpel applikasjon som lister opp monstre og gi verdier til monstre som `minVal`, `maxVal` og `Rarity`. Hovedpeonget er å bruke [**"The Twelve-Factor App"**](https://12factor.net/) metodologien til å deploye til Cloud løsning, metrikker og gi logger i andre løsninger på internett. 

### API Endpoints
API Endpoints er som følger:

**Monster:**
- `GET`: https://cloudrun-srv2-nmg2ikrwja-uc.a.run.app/monsters - Lister opp alle monstre som fins.
- `POST`: https://cloudrun-srv2-nmg2ikrwja-uc.a.run.app/monsters - Lager monstre med en random generert Unik ID (UUID).

**Rarities - Sjeldenhent basert på monstre:**
- `GET`: https://cloudrun-srv2-nmg2ikrwja-uc.a.run.app/monsters/{monsterId}/rarities - Henter ut rarities via monsterId
- `POST`: https://cloudrun-srv2-nmg2ikrwja-uc.a.run.app/monsters/{monsterId}/rarities - Legger til rarities via monsterId

### Google Cloud 

For at dette prosjektet skal fungere på **Google Cloud: Cloud Computing Services** med **Travis CI**.

1. En **IAM Service account bruker** på **Google Cloud IAM** med følgende permissions:
  - `Container Registry Agent`
  - `Storage Admin`
  - `Cloud Run Admin`
  
2. Laste ned **nøkkelfilen** til **IAM Service** accounten du opprettet med de rikte permissionn og navngi den `google-key.json` og lagre den i rot strukturen i prosjektet.

3. Kjøre `travis encrypt-file google-key.json -add` i prosjekt directoryen i terminalen og ``.travis.yml`` filen vil bli oppdatert med en OpenSSL script som travis vil kjøre når den skal bygge på **travisci.com**.

### Metrcis via InfluxDB og Grafana

- `creationCounter`: counter.metersCreated
- `notFoundCreation`: counter.metersNotFound
- `monsterRaritySummsary` = "distribution.monsterRarity
- `meterRegistry.com` = "Monster Rarity Calculation"
