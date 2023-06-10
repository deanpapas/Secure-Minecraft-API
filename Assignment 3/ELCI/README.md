# ELCI

A Bukkit plugin which implements the [MCPI](https://github.com/martinohanlon/mcpi) API with some modifications, 
including new commands and removal of Raspberry Pi specific functionality. It is
designed to run with [this modified version](https://github.com/rozukke/mcpi-elci) of MCPI.

## Commands

### Commands supported

This release supports all commands from the [root repository](https://github.com/zhuowei/RaspberryJuice)
excluding block hits, projectiles and events. It also supports these additional performant
commands:
- `getBlocksWithData` to avoid multiple sequential `getBlockWithData` calls
- `getHeights` to avoid sequential `getHeight` calls
- `doCommand` (requires player on server) performs an ingame command such as `/tp 0 0 0 ` (`/` is not required)

## Config

Modify config.yml:

 - hostname: - ip address or hostname to allow connections from, default is "0.0.0.0" (any). "localhost" would prevent remote clients from connecting.
 - port: 4711 - the default tcp port can be changed in config.yml

## Build

To build ELCI, [download and install Maven](https://maven.apache.org/install.html), clone the repository, run `mvn package':

```
git clone https://github.com/rozukke/ELCI
cd ELCI
mvn package
```

## Version history

 - 1.12.1 - hostname specified in config.yml
 - 1.12 - getEntities, removeEntities, pollProjectileHits, events calls by player and entity
 - 1.11 - spawnEntity, setDirection, setRotation, setPitch
 - 1.10.1 - bug fixes
 - 1.10 - left, right, both hit clicks added to config.yml & fixed minor hit events bug
 - 1.9.1 - minor change to improve connection reset
 - 1.9 - relative and absolute positions added to config.yml
 - 1.8 - minecraft version 1.9.2 compatibility
 - 1.7 - added pollChatPosts() & block update performance improvements
 - 1.6 - added getPlayerId(playerName), getDirection, getRotation, getPitch
 - 1.5 - entity functions
 - 1.4.2 - bug fixes
 - 1.4 - bug fixes, port specified in config.yml
 - 1.3 - getHeight, multiplayer, getBlocks
 - 1.2 - added world.getBlockWithData
 - 1.1.1 - block hit events
 - 1.1 - Initial release

## Contributors

- [rozukke](https://github.com/zhuowei/RaspberryJuice) (edited  version)


 - [zhuowei](https://github.com/zhuowei)
 - [martinohanlon](https://github.com/martinohanlon)
 - [jclaggett](https://github.com/jclaggett)
 - [opticyclic](https://github.com/opticyclic)
 - [timcu](https://www.triptera.com.au/wordpress/)
 - [pxai](https://github.com/pxai)
 - [RonTang](https://github.com/RonTang)
 - [Marcinosoft](https://github.com/Marcinosoft)
 - [neuhaus](https://github.com/neuhaus)
