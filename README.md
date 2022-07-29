# Sticky Hopper

> Minecraft Fabric mod allowing hoppers to be sticky!

*Based on an original idea by [Aurelien_Sama (FR)](https://www.youtube.com/watch?v=3dE8PJsWcLo&ab_channel=Aurelien_SamaAurelien_Sama), and its prototype coded by ShoukaSeikyo.*


# Features

**Sticky Hopper** simply never empties completely, but retains a single item in each of its inventory slots instead.  
It has been designed to be used as a filter. Simple, small, sliceable, redstone-less filter!

**Comparators** checking for Sticky Hopper content ignore all items which are in a single copy per inventory slot.  
But as soon as there is a second item in a slot, they are counted as 2/64 (or 2/16, or whatever the maximum stack is for this type of item), not as 1/63 nor 1/64...
If there are single items in other slots, they are still ignored though.  
Non-stackable items are always ignored by Comparators as there can't be more than one in a slot, by all means.

Please note that the time to transfer a stack of items through a line of Sticky Hoppers is the same as for normal Hoppers (8 game ticks per transfer).  
But since Sticky Hoppers keep one item each, they do not actually transfer anything until the second (same) item, so they may seem slower at first.


# Craft

As this version is "server-side only" (SSO), there are obviously no craft available through the crafting table.  
[A "client / server" version exists](https://modrinth.com/mod/sticky-hopper), so you can choose which one is right for you. 

To craft a Sticky Hopper in this SSO version, you have to drop a Honey Bottle in a placed Hopper. Yes, your honey farms will be full of these, sorry! ¯\\\_(ツ)\_/¯  
The honey is consumed and the bottle may be absorbed by the hopper 8 ticks later!

Unless you give it a custom name, the hopper will appear as "Sticky Hopper" when opening its inventory, and its associated item will be renamed identically (with no italic font).  
I believe that it's sadly not possible to give it a different texture with a texture pack, but a few honey particles will fall from the exit of the Sticky Hopper, when placed.

To uncraft a Sticky Hopper, drop a Snowball on it. Do not try to throw it... or you may just open the Hopper inventory! ;)  
*Didn't you know that snow cleans honey much better than a water bucket or a water bottle? The legend says that it is because the latter are not stackable!*


# Installation

Players obviously don't have to install this "server-side only" version client-side, unless it is to be used offline.

The minimum requirements are, of course the [Fabric launcher](https://fabricmc.net/use/), and the [Fabric API](https://modrinth.com/mod/fabric-api), and to copy the ".jar" file into your mods folder.  
(For default Windows installations, after running the game with the Fabric launcher at least once, just copy / paste this into the path bar of any Explorer window to open the mod folder: `%appdata%\.minecraft\mods` — and press *Enter*.)


# Miscellaneous

Performance of Sticky Hoppers is similar to that of vanilla Hoppers (tested with Carpet Mod, on over 2500 idle normal and Sticky hoppers).

You are free to use this mod in any modpacks you'd make.

This mod works without conflict with my other mod "[Speedy Hopper](https://modrinth.com/mod/speedy-hopper)", but of course you won't be able to craft a "Fast Sticky Hopper", so you'll have to make choices and design your contraptions accordingly!

I don't plan to adapt it for another modloader than Fabric.  
Please take a look at the current [issues board](https://dev.cuicui.ovh/minecraft/sticky-hopper/-/boards), and also at [the interesting ideas and suggestions](https://dev.cuicui.ovh/minecraft/sticky-hopper/-/wikis/Ideas) I've received, to see what is likely to happen sooner or later!

*Hope you'll find this mod usefull! ♥ Also, feel free to take a look at [my other mods](https://modrinth.com/user/SoGCuicui)!*


## Versions

4.1 - Honey particles now randomly fall from the exit of Sticky Hoppers  
4.0 - As the new Allay mob can be used to sort non-stackable items, this feature has been removed from this mod, so it is more vanilla-friendly again!  
3.1 - NBTs are now used and the way to make or unmake a Hopper sticky has changed; Small changes on how a Sticky Hopper filtering non-stackable items works  
3.0a - Improving the functionality of the sorting book - This is also the first release of the server-side only version!  


## Known Incompatibilities

- _Lithium_:  
  Hoppers optimisations added in Lithium make Sticky Hoppers act like normal Hoppers!  
  I may work on this problem at some point... In the meantime, my mod disables these optimizations.
