# music-league-analyser



## Setup
Easiest way to run this is to open it in IntelliJ and run `src/main/kotlin/Main.kt`.

## Usage
1. Before running, grab the cookie your browser uses when it visits musicleague.com and paste it into Main.kt's `token`
2. Open the league you want to analise and grab the league id from the url e.g. `https://app.musicleague.com/l/fced82519afd4d80bc2e274790c08acf/` -> your music league id is `fced82519afd4d80bc2e274790c08acf`
3. Grab your analysis output from std-out

## Conduct your own analysis / Future work
Right now all this does is some naive comparisons of the constituent voters. You could, however, print out all the fetched data to a csv and perform analysis with something more sophisticated like linear regression to find player clusters.

You could also connect to the Spotify API to see who in your league has biases to which genres.
