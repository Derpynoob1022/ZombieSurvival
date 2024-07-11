**Game name TBD**

What the game is going to look like:
- defending the base or a pile of gold from hordes of zombies that progressively gets harder
- Bosses every few levels
- able to upgrade base defence and put down canons or smth
- able to get rare drops drop zombies
- want to be able to gain some sort of currency after each game depending on performace, which lets the user upgrade 
the defence or unlock new items.
- endless waves 
- different maps?

What type of classes do I need and what do they do:
Src folder
- UI package
  - sound
  - ui
  - GamePanel: handles all the updates
  - GameFrame
  - Main
  
- Persistence package
  - JSON reader and writer

- Model package
  - Collisionchecker
  - Levelhandler: knows whether a level is complete and spawns in next wave of mobs
  - Keyhandler
  - Player
  - Drawable (interface)
  - Enemy (interface)
  - Blocks (interface)
  - Items (interface)

Resource folder
- Character sprites
- Enemy sprites
- projectile sprites
- block sprites
