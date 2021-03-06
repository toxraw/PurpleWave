# PurpleWave
### A tournament-winning AI player of *StarCraft: Brood War*

## About
[PurpleWave](https://github.com/dgant/PurpleWave) is a *StarCraft: Brood War* AI written in Scala. It can play all three races and a large variety of professional-style strategies.

PurpleWave has won:
 * :trophy: 1st Place in the [2019-20 SSCAIT](https://sscaitournament.com/index.php?action=2019)
 * :trophy: 1st Place in the [2019 IEEE CoG StarCraft AI Competition](https://cilab.gist.ac.kr/sc_competition2019/?cat=17)
 * :trophy: 1st Place in the [2018-19 SSCAIT](https://sscaitournament.com/index.php?action=2018)
 * :trophy: 1st Place in the [2018 AIST1](https://sites.google.com/view/aistarcrafttournament/aist-s1)
 * :2nd_place_medal: 2nd Place in the [2020 AIST3](https://sites.google.com/view/aistarcrafttournament/aist-s3)
 * :2nd_place_medal: 2nd Place in the [2019 AIIDE StarCraft AI Competition](https://www.cs.mun.ca/~dchurchill/starcraftaicomp/2019/)
 * :2nd_place_medal: 2nd Place in the [2017 AIIDE StarCraft AI Competition](https://www.cs.mun.ca/~dchurchill/starcraftaicomp/2017/)
 * :2nd_place_medal: 2nd Place in the [2018 IEEE CIG StarCraft AI Competition](https://cilab.gist.ac.kr/sc_competition2018/?cat=17)
 * :3rd_place_medal: 3rd Place in the [2017 IEEE CIG StarCraft AI Competition](https://cilab.gist.ac.kr/sc_competition2017/?cat=17)
 
PurpleWave has also ranked #1 on the [BASIL](https://basil.bytekeeper.org/ranking.html), SSCAIT, and SAIL ladders.

PurpleWave vs. Iron, AIIDE 2017:

[![PurpleWave vs. Iron, AIIDE 2017](https://img.youtube.com/vi/g33PIqDdTqs/0.jpg)](https://www.youtube.com/watch?v=g33PIqDdTqs)

## Credits
Thanks to:
* Nathan Roth (Antiga/Iruian) for strategy advice and consulting -- so much of the polish in PurpleWave's strategies comes from his wisdom and replay analysis
* @jaj22/JohnJ for lots of advice navigating Brood War mechanics
* @IMP42 @AdakiteSystems and @tscmoo for helping me get BWAPI up and running when I was getting started
* @davechurchill @certicky @krasi0 @Bytekeeper @bgweber Nathan Roth and the Cognition & Intelligence Lab at Sejong University for hosting Brood War competitions
* @kovarex and @heinermann for [BWAPI](https://github.com/bwapi/bwapi), @JasperGeurtz @Bytekeeper and @N00byEdge for [JBWAPI](https://github.com/JavaBWAPI/JBWAPI), @vjurenka for [BWMirror](https://github.com/vjurenka/BWMirror), @tscmoo for [OpenBW](https://github.com/OpenBW/openbw/), Luke Perkins for BWTA, and @CMcCrave for [MCRS/Horizon](https://github.com/Cmccrave/Horizon) 

## How to build PurpleWave
[See build instructions in install.md](install/install.md)

Steps: 
* Clone or download this repository (I keep it in c:\p\pw but it should work from anywhere)
* If you cloned the repository `git submodule sync; git submodule update --init --recursive` to clone JBWAPI 
* Open IntelliJ IDEA
* In IntelliJ IDEA: File -> Settings -> Plugins -> Check off Scala
* In IntelliJ IDEA: File -> Open -> Select the PurpleWave directory
* In IntelliJ IDEA: File -> Project Structure -> Select the Java Development Kit directory (like c:\Program Files\Java\jdk\1.8.0_121)
* In IntelliJ IDEA: File -> Project Structure -> Modules -> The green "+" -> Scala -> Create... -> Download... -> 2.12.6... -> OK
* In IntelliJ IDEA: File -> Project Structure -> Modules -> Dependencies -> Under "Export" check scala-sdk-2.12.6
* In IntelliJ IDEA: Build -> Build Artifacts... -> Build

This will produce PurpleWave.jar. See below for "How to run PurpleWave"

## How to run PurpleWave
* From IntelliJ IDEA: Run -> Run 'PurpleWave' or Debug 'PurpleWave'
* As a JAR:
  - `cd` to the StarCraft directory
  - `mkdir -p bwapi-data/AI; mkdir -p bwapi-data/read; mkdir -p bwapi-data/write` to create the standard directories for BWAPI bot data
  - Copy PurpleWave.jar to `bwapi-data/AI`
  - `java.exe -jar bwapi-data/AI/PurpleWave.jar` <-- Run this from the StarCraft directory  

## Questions and feedback
Say hi! Post an issue here on Github or email dsgant at gmail

## License
PurpleWave is published under the MIT License. I encourage you to use PurpleWave as a starting point for your own creation!
