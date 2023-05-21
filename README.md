# music-league-analyser



## Setup / Prerequisites
1. Install Docker Desktop & make sure it's running. https://www.docker.com/products/docker-desktop/
2. `docker` is on your path.
3. Install IntelliJ (Easiest way to run this is to open it in IntelliJ and run `src/main/kotlin/Main.kt`)

## Usage
1. Before running, grab the cookie your browser uses when it visits musicleague.com and paste it into Main.kt's `token`
2. Open the league you want to analise and grab the league id from the url e.g. `https://app.musicleague.com/l/fced82519afd4d80bc2e274790c08acf/` -> your music league id is `fced82519afd4d80bc2e274790c08acf`
3. Navigate to the repo directory: `cd music-league-analyser`
4. Spin up the mysql instance: `docker compose up`
5. Run `setup.sql`
6. Run `Main.kt`
7. Connect the database locally via a workbench (e.g. DBeaver)
8. Perform your own analysis
