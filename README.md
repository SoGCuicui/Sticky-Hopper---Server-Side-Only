# Sticky Hopper

> Minecraft Fabric mod allowing hoppers to be sticky!

*Based on an original idea by [Aurelien_Sama (FR)](https://www.youtube.com/watch?v=3dE8PJsWcLo&ab_channel=Aurelien_SamaAurelien_Sama), and its prototype coded by ShoukaSeikyo.*


## Features

### Basic

**Sticky Hopper** — which is obviously a new block added by this mod — simply never empties completely, but retains a single item in each of its inventory slots instead.  
It has been designed to be used as a filter. Simple, small, sliceable, redstone-less filter!

**Comparators** checking for Sticky Hopper content ignore all items which are in a single copy per inventory slot.  
But as soon as there is a second item in a slot, they are counted as 2/64 (or 2/16, or whatever the maximum stack is for this type of item), not as 1/63 nor 1/64...
If there are single items in other slots, they are still ignored though.  
Non-stackable items are always ignored by Comparators as there can't be more than one in a slot, by all means.

Please note that the time to transfer a stack of items through a line of Sticky Hoppers is the same as for normal Hoppers (8 game ticks per transfer).  
But since Sticky Hoppers keep one item each, they do not actually transfer anything until the second (same) item, so they may seem slower at first.


### Advanced

**Since version 3.0** for Minecraft ≥ 1.17.1, it is possible to filter non-stackable items!  
Though, you'll need some extra items to make kind of a recipe inside the hopper (see the image below), and a single Sticky Hopper will be able to sort only one non-stackable item type!  
Moreover, this is a customizable option that is disabled by default!  
Indeed, the initial idea behind this hopper is to have a vanilla-friendly behavior, and adding the ability of sorting non-stackable items is quite overkill!

To activate it, you have to edit the "config/sticky_hopper.toml" file and set `nonstackable_filter` to `true`. On a single player session, you can also use [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu) in-game settings screen.  
Then, you have to configure your hopper by placing the item you want to filter like so:  
... img ...  
Use only one Tripwire Hook and one String on both sides of your item!  
If you do so, incoming Hooks and Strings will be ignored, and non-stackable items identical to the one in the center will be transferred, regardless of their durability, at Hopper speed!


******************** Noter qu'il faut que le sticky hopper pointe un inventaire avec une place de libre pour que l'item puisse sortir,
******************** un hopper en dessous (dans lequel le sticky hopper ne pointe pas) ne suffit pas !



## Craft

A Honey Bottle on top of a normal Hopper. That's it! ¯\\\_(ツ)\_/¯  
![](https://media.forgecdn.net/attachments/373/562/sticky_hopper_craft.png)  
Of course, it gives the empty Glass Bottle back!

It is also possible to clean up a Sticky Hopper, just place a Snowball on top of it.  
![](https://media.forgecdn.net/attachments/374/102/hopper_craft.png)  
*Didn't you know that snow cleans honey much better than a water bucket? The legend says that it is because the latter is not stackable!*


## Installation

This mod is for both clients and servers!  
As it adds a new block, if you want to use it on a server, clients should have it too, so they can see and craft the Sticky Hopper.

All you need is, of course, the [Fabric launcher](https://fabricmc.net/use/), the [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api), and to copy the ".jar" file into your mods folder.  
(For default Windows installations, after running the game with the Fabric launcher at least once, just copy / paste this into the path bar of any Explorer window to open the mod folder: `%appdata%\.minecraft\mods` — and press *Enter*.)


## Miscellaneous

This mod is translated in English and French.  
You are free to use this mod in any modpacks you'd make.

This mod works without conflict with my other mod "[Speedy Hopper](https://www.curseforge.com/minecraft/mc-mods/speedy-hopper)", but of course you won't be able to craft a "Fast Sticky Hopper", so you'll have to make choices and design your contraptions accordingly!

I don't plan to adapt it for another modloader than Fabric.  
Please take a look at the current [issues board](https://dev.cuicui.ovh/minecraft/sticky-hopper/-/boards), and also at [the interesting ideas and suggestions](https://dev.cuicui.ovh/minecraft/sticky-hopper/-/wikis/Ideas) I've received, to see what is likely to happen sooner or later!

*Hope you'll find this mod usefull! ♥ Also, feel free to take a look at [my other mods](https://www.curseforge.com/members/cuicui_off/projects)!*


### Versions

3.0 - (≥ 1.17.1) Adding the ability to filter non-stackable items  
2.3 - (≥ 1.17) Bugfix: Pickaxe tag added, so it's possible to mine Sticky Hoppers  
2.2 - Cleaning craft simplified  
2.1 - Non-stackable items can finally be retained (but not filtered); Clean up craft added  
2.0 - Comparators management; Transfer cooldown bugfix  
1.0 - Initial project with operational Sticky Hoppers


### Known Incompatibilities

- _Lithium (≥ 1.17 only)_:
  Hoppers optimisations added in Lithium make Sticky Hoppers act like normal Hoppers!  
  I intend to work on this problem sooner or later. In the meantime, my mod the deactivation of these optimizations for Minecraft 1.17.1 (if you are using it for 1.17, just add `mixin.block.hopper=false` in "config/lithium.properties" yourself).
- _Vanilla Tweaks - Resource Packs_:
  I've found two [Vanilla Tweaks resource packs](https://vanillatweaks.net/picker/resource-packs/) that have small compatibility issues, due to the change in texture of the Sticky Hoppers.  
  => _Directional Hoppers_ normally displays an arrow inside the hopper if it is not pointing down. It does not appear in Sticky Hoppers because of the texture of the honey.  
  => _3D Items_, more annoying, changes the model used for items (in the hotbar, in the hand, and also for items dropped on the ground), and unfortunately the honey texture does not appear at all.  
  Here are screenshots to illustrate those issues (without resource packs, with _Directional Hoppers_, and with _3D Items_):  
  ![](https://media.forgecdn.net/attachments/401/587/resource_pack_no.png)
  ![](https://media.forgecdn.net/attachments/401/589/resource_pack_directional_hoppers.png)
  ![](https://media.forgecdn.net/attachments/401/591/resource_pack_3d_items.png)
