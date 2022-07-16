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
The honey is consumed and the bottle may be absorbed by the Hopper 8 ticks later!

It will change its NBTs so the game can differentiate it from normal Hoppers. This NBTs is kept by the item if you break the Hopper, that means it won't stack with normal Hoppers, and more importantly it won't lose its Sticky power.  
Unless you give it a custom name, the Hopper would appear as "Sticky Hopper" when opening its inventory, but sadly the corresponding item can't be renamed. It's also not possible to give it a different texture with a texture pack.

To uncraft a Sticky Hopper, drop a Snowball on it. Do not try to throw it... or you may just open the Hopper inventory! ;)  
*Didn't you know that snow cleans honey much better than a water bucket or a water bottle? The legend says that it is because the latter are not stackable!*


# Installation

Players obviously don't have to install this "server-side only" version client-side, unless it is to be used offline.

The minimum requirements are, of course the [Fabric launcher](https://fabricmc.net/use/), and the [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api), and to copy the ".jar" file into your mods folder.  
(For default Windows installations, after running the game with the Fabric launcher at least once, just copy / paste this into the path bar of any Explorer window to open the mod folder: `%appdata%\.minecraft\mods` — and press *Enter*.)


# Miscellaneous

This mod is translated in English and French.  
You are free to use this mod in any modpacks you'd make.

This mod works without conflict with my other mod "[Speedy Hopper](https://modrinth.com/mod/speedy-hopper)", but of course you won't be able to craft a "Fast Sticky Hopper", so you'll have to make choices and design your contraptions accordingly!

I don't plan to adapt it for another modloader than Fabric.  
Please take a look at the current [issues board](https://dev.cuicui.ovh/minecraft/sticky-hopper/-/boards), and also at [the interesting ideas and suggestions](https://dev.cuicui.ovh/minecraft/sticky-hopper/-/wikis/Ideas) I've received, to see what is likely to happen sooner or later!

*Hope you'll find this mod usefull! ♥ Also, feel free to take a look at [my other mods](https://modrinth.com/user/SoGCuicui)!*


## Versions

4.0 - (≥ 1.19) As the new Allay mob can be used to sort non-stackable items, this feature has been removed from this mod, so it is more vanilla-friendly again!  
3.1 - Server Side Only version: NBTs are now used and the way to make or unmake a Hopper sticky has changed; Both versions: small change on how a Sticky Hopper filtering non-stackable items works  
3.0b - Correcting texts for Mod Menu (Client/Server version)  
3.0a - (≥ 1.17.1) Improving the functionality of the sorting book - This is also the first release of the server-side only version!  
3.0 - (≥ 1.17.1) Adding the ability to filter non-stackable items  
2.3 - (≥ 1.17) Bugfix: Pickaxe tag added, so it's possible to mine Sticky Hoppers!  
2.2 - Cleaning craft simplified  
2.1 - Non-stackable items can finally be retained (but not filtered); Clean up craft added  
2.0 - Comparators management; Transfer cooldown bugfix  
1.0 - Initial project with operational Sticky Hoppers


## Known Incompatibilities

- _Lithium (≥ 1.17 only)_:  
  Hoppers optimisations added in Lithium make Sticky Hoppers act like normal Hoppers!  
  I may work on this problem at some point... In the meantime, my mod disables these optimizations for Minecraft 1.17.1 (if you are using it for 1.17, just add `mixin.block.hopper=false` in "config/lithium.properties" yourself).
