# Sticky Hopper

> Minecraft Fabric mod allowing hoppers to be sticky!

*Based on an original idea by [Aurelien_Sama (FR)](https://www.youtube.com/watch?v=3dE8PJsWcLo&ab_channel=Aurelien_SamaAurelien_Sama), and its prototype coded by ShoukaSeikyo.* 


## Features

**Sticky Hopper** — which is obviously a new block added by this mod — simply never empties completely, but retains a single item in each of its inventory slots instead.  
It has been designed to be used as a filter. Simple, small, stuckable, redstone-less filter!

**Comparators** checking for Sticky Hopper content ignore all items which are in a single copy per inventory slot.  
But as soon as there is a second item in a slot, they are counted as 2/64 (or 2/16, or whatever the maximum stack is for this type of item), not as 1/63 (nor 1/15).
If there are single items in other slots, they are still ignored though.  
Non-stackable items are always ignored by Comparators as there can't be more than one in a slot, by all means.

Please note that the time to transfer a stack of items through a line of Sticky Hoppers is the same as for normal Hoppers (8 game ticks per transfer).  
But since Sticky Hoppers keep one item each, they do not actually transfer anything until they have a second item in one of their inventory slots.


## Craft

A Honey Bottle on top of a normal Hopper. That's it! ¯\\\_(ツ)\_/¯  
![](https://media.forgecdn.net/attachments/373/562/sticky_hopper_craft.png)  
Of course, it gives the empty Glass Bottle back!

It is also possible to clean up a Sticky Hopper, just place a Ghast Tear on top of it.  
![](https://media.forgecdn.net/attachments/373/563/hopper_craft.png)  
*Didn't you know that a Ghast tear cleans honey much better than a water bucket? The legend says that it is because the latter is not stackable!*


## Installation

This mod is for both clients and servers!  
As it adds a new block, if you want to use it on a server, clients should have it too, so they can see and craft the sticky hopper.

All you need is, of course, the [Fabric launcher](https://fabricmc.net/use/), the [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api), and to copy the ".jar" file into your mods folder.  
(For default Windows installations, after running the game with the Fabric launcher at least once, just copy / paste this into the path bar of any Explorer window to open the mod folder: `%appdata%\.minecraft\mods` — and press *Enter*.)

Sticky hoppers are not customizable.


## Miscellaneous

This mod is translated in English and French.  
You are free to use this mod in any modpacks you'd make.  
This mod works without conflict with my other mod "[Speedy Hopper](https://www.curseforge.com/minecraft/mc-mods/speedy-hopper)", but of course you won't be able to craft a "Fast Sticky Hopper", so you'll have to make choices and design your contraptions accordingly!

I don't plan to adapt it for another modloader than Fabric.

*Hope you'll find this mod usefull!* ♥

 
### Versions

2.1 - Non-stackable items can finally be retained (but not filtered); Clean up craft added  
2.0 - Comparators management; Transfer cooldown bugfix  
1.0 - Initial project with operational Sticky Hoppers
