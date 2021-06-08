# Sticky Hopper

> Minecraft Fabric mod allowing hoppers to be sticky!

*Based on an original idea by [Aurelien_Sama (FR)](https://www.youtube.com/watch?v=3dE8PJsWcLo&ab_channel=Aurelien_SamaAurelien_Sama), and its prototype coded by ShoukaSeikyo.* 


## Features

**Sticky hopper** — which is obviously a new block added by this mod — simply never empty completely, but retain a single item in each of its inventory slots, except for not stackable items.  
It has been designed to be used as a filter. Simple, small, stuckable, redstone-less filter!

**Comparators** checking for sticky hopper content ignore all stackable items which are in a single copy per inventory slot.  
But as soon as there is a second item in a slot, they are counted as 2/64 (or 2/16, or whatever the maximum stack is for this type of item), not as 1/63 (nor 1/15).
Other unique items are still ignored though.

Please note that the time to transfer a stack of items through a line of sticky hoppers is the same as for normal hoppers (8 game ticks per actions).  
But since sticky hoppers keep one item each, they do not actually transfer anything until they have a second item in one of their inventory slots.


## Craft

A honey bottle on top of a normal hopper. That's it! ¯\_(ツ)_/¯  
Of course, it gives the empty bottle back!


## Installation

This mod is for both clients and servers!  
As it adds a new block, if you want to use it on a server, clients should have it too, so they can see and craft the sticky hopper.

All you need is, of course, the [Fabric](https://fabricmc.net/use/) launcher, the [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api), and to copy the ".jar" file into your mods folder.  
(For default Windows installations, after running the game with the Fabric launcher at least once, just copy / paste this into the path bar of any Explorer window:
`%appdata%\.minecraft\mods` - and press *Enter*.)

This mod is not customizable.
