# AmbientAddons
A collection of QOL features for Hypixel Skyblock in a Forge 1.8.9 + Essential mod, aiming to provide convenience without excessively cheaty features. Almost all features are off by default: choose what you like!

If you encounter any issues or have suggestions, please let me know on the [Discord](https://discord.gg/48bxrQj8Pz), or if unable DM me directly on Discord at appable#8347. Thanks!

## Commands
<i>Note: /aa or /ambientaddons can be used in place of /ambient.</i>
* /ambient: open main settings GUI
* /ambient salvage: edit salvage list
* /ambient buy: edit autobuy list
* /ambient move: edit display element locations
* /ping: view ping and TPS in chat
<img width="292" alt="Screenshot 2022-11-13 at 7 08 23 PM" src="https://user-images.githubusercontent.com/16139460/201566901-67ca79db-b3da-4b6e-a474-5fe03e938e6a.png">

## Chest QOL
Autobuy blocks rerolling and/or automatically opens dungeon chests containing user-specified items based on Skyblock ID or enchantments. 

Autobuy comes with a number of profitable drops preconfigured, but is fully customizable through commands. The maximum chest price of an item can be specified to limit certain drops like recombobulators and soul eater books.
* /ambient buy list: show current autobuy list.
* /ambient buy add <Skyblock ID> [maximum allowable chest price]: add an item to the list.
* /ambient buy buy remove <Skyblock ID>: remove an item from the list.
<img width="300" alt="Screenshot 2022-11-13 at 7 25 57 PM" src="https://user-images.githubusercontent.com/16139460/201568786-5b3dc817-648f-4063-8e2d-63c16707510c.png">

Chest QOL also blocks rerolling non-bedrock chests (except on M4), and prevents opening free chest with a key after opening another chest in a run.

## Salvaging
Salvaging features can highlight salvagable items, block clicks on non-salvagable items, or automatically salvage. Additionally, legit autosalvage allows you to click once on items in inventory to salvage them. Salvaging features are preconfigured and are further customizable through commands.
* /ambient salvage auto <Skyblock ID>: allow automatically salvaging item
* /ambient salvage allow <Skyblock ID>: allow salvaging item, but do not do so automatically
* /ambient salvage block <Skyblock ID>: explicitly block salvaging an item (e.g. ice spray wand, or glitched chest drops)
* /ambient salvage remove <Skyblock ID>: remove from list and use default salvaging rules 
<img width="250" alt="Screenshot 2022-11-13 at 7 11 53 PM" src="https://user-images.githubusercontent.com/16139460/201567273-7b7f8e12-9eaf-4589-a34c-adddc3f133a2.png">


## Additional features
### Keybinds
Editable in the vanilla Minecraft control settings.
* Re-send last message in party chat 
* Option to separate default camera perspectives 

### Notifications
* Bonzo mask notification when a bonzo or spirit mask activates.
* Thunder bottle notification warns when a thunder bottle is filled or is in inventory after switching worlds

### Displays
* Cat display: draws catplague's wonderful cat upgrade.
* Ping and TPS display: A display of the current (estimated) ping and TPS, using the Skytils method of determining ping. Ping requires sending certain packets, which are safe on Hypixel and most servers. 
* Wither shield display: Draws the current time remaining with the wither shield ability below the crosshair.
<img width="400" alt="Screenshot 2022-11-13 at 7 11 53 PM" src="https://user-images.githubusercontent.com/16139460/201568216-94d951fa-d9ee-4537-b03c-600feca1c2b8.png">

### Dungeon features
* Shortbow autoclick: Clicks repeatedly when use-item button is held while holding a shortbow. Adjustable from 0 to 50 CPS.
* Melody terminal helper: Blocks clicks on Melody terminal when not aligned.
* Melody terminal announcements: Customizable party chat message when Melody terminal is opened and/or throttled.
* Carpet QOL: Edits the hitboxes of carpets to reduce lagback (credit Floppa Client).
* Auto-close chests: Blocks opening secret chest GUIs inside dungeons (credit Floppa Client).
* Ignore hopper interactions: Allows using items with abilities when looking at a hopper (credit Floppa Client).

### Dungeon highlights
All dungeon highlights can be off, highlight-only, or ESP.
* Bat highlight: shows secret bats.
* Starred mob highlight: shows starred mobs.
* Shadow assassin highlight: highlights shadow assassins (as these are inconsistent with normal starred mob highlight).
* Bestiary highlight: shows Snipers and Cellar Spiders (longer-range than armor stand detection). Disabled when idkmansry is in run.
<img width="400" alt="Screenshot 2022-11-13 at 7 11 53 PM" src="https://user-images.githubusercontent.com/16139460/201568547-1e30e334-7f19-4a61-a675-0c5d2ffd5275.png">
### Pre/Post dungeon features
* Automatically start dungeon: Can be enabled always, or only when 5 players are in dungeon.
* Show extra stats: automatically shows extra stats at the end of a dungeon.

### Kuudra features
* Automatically start Kuudra: Automatically readies in Kuudra fights
* Dropship alert: notifies a few seconds before a dropship drops TNT
* Kuudra HP: shows Kuudra health in boss bar

### Skills
* Smart crop misclick prevention: checks whether items has replenish (which works correctly with laggy servers and counter updating), and blocks only source mushrooms for the two common styles of mushroom farms.
* Crimson rare sea creature alert: sends a message in chat when a rare sea creature is fished within render distance.
* Highlight Thunder, Jawbus, and Sparks: highlights rare sea creatures in the Crimson Isles within render distance.
* Trapper auto-start quest: automatically starts trapper quests.
* Trapper ESP: Renders position of trapper mobs from reasonably large distance.
<img width="400" alt="Screenshot 2022-11-13 at 7 11 53 PM" src="https://user-images.githubusercontent.com/16139460/193432076-da692f74-dcc5-4b6f-9604-f04ed03a2fcd.png">
