# Changelog

## 1.9.2
- Fixed an exception being thrown to clients when things get auto unlocked on a server.
- Increased event priority to `HIGH` to ensure our checks are called before other default level listeners.
  - This fixes some items/blocks being able to open gui's even if the requirements for the item is not met.
- Moved things around slightly to hopefully fix the rare cases where the RequirementCache does not invalidate properly.
  - Unfortunately this is hard to test for as there is no easy way to reproduce it. 


## 1.9.1
- Fixed crash on server startup
- Added API methods to send skill and unlockable toasts from server side


## 1.9.0
- Changed chorus transmutation trait to a more generic transmutation trait. It is now possible to edit what things get transmuted and add your own via the API. (CompatSkills exposes this so that scripts can modify the transmutation map.)
- The Skill GUIs are now also closeable with the inventory key. 
- Added an API method and system to allow for easy addition/displaying of toast notifications.
- Display a toast notification when a trait is auto unlocked.



## 1.8.3
- Fixed Skill icon for rank 1 not being found if it is not set but one is set for rank 0.
- Fixed a bug with Logic Requirements not being properly invalidated
- Fixed support for blocks with "NBT" not checking the data while placed. Example: Mekanism factories with locks on them to have requirements for using installers.


## 1.8.2
- Fixed tooltips sometimes not being recalculated when requirements are invalidated.


## 1.8.1
- Hopefully finally fixed the CME with RequirementCache. It is hard to track down and fix a bug when you are unable to reproduce it.


## 1.8.0
- Made it so that the skill gui can be closed with the same key that opens it.
- Fixed a desync with requirement cache not invalidating properly on servers.
- Lots of internal API additions
  - Created a method to allow addons to inject more tooltip information into Reskillable's tooltips.
  - Made it so that addons are able to edit skill/trait configs. This mainly is for CompatSkills wrappers.
  - Added a method to be able to hide skills from the GUI.
  - Added CacheInvalidatedEvent thrown when part of the cache is invalidated.
  - Added the ability to set custom skill `rank` icons.



## 1.7.1
- Fixed the requirement cache not being properly invalidated on both the client and the server.


## 1.7.0
- Added "none" as a requirement type that basically removes all locks from an item. This allows for locking an entire mod and then unlocking specific items. The config previously stated that this would work and now it works again.
- Some minor performance improvements and code cleanup.
- Fixed the scroll buttons in the skills gui being slightly off center.
- Fixed the permission levels of all the commands so that they can be run from command blocks. (Or from functions like Triumph uses)
- Created a way to cache requirements so that they only have to be recalculated when something has changed. This provides a substantial performance increase.
- Better tooltip caching for hovering and rewrote how tooltips are handled internally so that they are more efficient for calculating and for inverting the color for NOT requirements.
- Created RequirementExceptions so that more detailed error messages can be given when a requirement string is invalid. This also makes logic requirements provide a simple "stacktrace" to which part of the requirement is formatted wrong.
- Made it so that if Unlockables/Traits have a cost of 0 then they will automatically unlock when all the requirements are met. This allows the GameStageUnlockables in CompatSkills to automatically unlock a gamestage when the requirements are met. This all is done utilizing the new requirement caching so that it only has to recheck the traits when something changed. (What that means is it will not negatively impact performance!)



## 1.6.0
- Added ru_RU localization
  - Thanks goes out to: Pirog17000
- Implemented an Unobtainable Requirement
  - Yup, this is a thing now, just use "unobtainable" as the requirement string
- Added Logical Requirements. Format followed by an example can be found below.
  - NOT
    - not|requirement
    - not|dim|0
  - AND
    - and|[requirement]~[requirement]
    - and|[reskillable:building|7]~[reskillable:building|5]
  - NAND
    - nand|[requirement]~[requirement]
    - nand|[dim|1]~[dim|-1]
  - OR
    - or|[requirement]~[requirement]
    - or|[reskillable:mining|5]~[reskillable:building|5]
  - NOR
    - nor|[requirement]~[requirement]
    - nor|[reskillable:farming|5]~[reskillable:gathering|5]
  - XOR
    - xor|[requirement]~[requirement]
    - xor|[dim|0]~[reskillable:building|5]
  - XNOR
    - xnor|[requirement]~[requirement]
    - xnor|[reskillable:agility|5]~[reskillable:farming|5]
- Made some internal API changes to allow for some of the new features in CompatSkills



## 1.5.1
- Fixed a crash on servers


## 1.5.0
- Implemented better support for Negative-Stagger Values
- Implemented Commands for the following things:
  - Incrementing a Skill Level by 1
    - /reskillable incrementskill <player> <skill>
  - Setting a Specific Skill to a Specific Level
    - /reskillable setskilllevel <player> <skill> <level>
  - Reset a specific Skill to 1
    - /reskillable resetskill <player> <skill>
  - Resetting all Skills to 1
    - /reskillable resetall <player>
  - Adding/Removing a specific Trait
    - /reskillable toggletrait <player> <trait>
- Added a new Event called 'LockUnlockableEvent'
- Added an Config option to disable the level-up button and using exp for skills.
- Added a safety check for Null PlayerData which defaults the tooltip info to 'un-earned'
- Added a config option to show requirements without having to press-shift.
- Implemented improvements to the Tooltip Code
- Improved the Level-Up event to provide more info to check against.
  - This modifies LevelUpEvent to support what the previous level is.
  - Because the commands bypass whatever locks may be on the skills or traits directly, this was chosen over manually running levelup multiple times as the new level may be lower than the old level.
  - This does not fully address #75 as it manually fires the events so that it can let the user know if the command was cancelled.
 


## 1.4.0
- Added zh_cn.lang
  - Thanks goes out to SihenZhang on Github for the PR
  - Please do not since the PR has happened we've done some updates internally and will poke him about it.
- Added Support for "Fuzzy" locks
  - (internal addition)
- Implemented Keybind for Opening the Skill GUI
  - Fixes [#74](https://github.com/Coders-After-Dark/Reskillable/issues/74) & [#92](https://github.com/Coders-After-Dark/Reskillable/issues/92)
- Implemented the "Hill-Walker" trait
  - This Trait adds Step-Assist!
  - Fixes [#88](https://github.com/Coders-After-Dark/Reskillable/issues/88)
- Made it so the scroll feature is disabled if there is nothing that needs scrolling.
  - Fixed [#72](https://github.com/Coders-After-Dark/Reskillable/issues/72)
- Fixed Skills not being able to be disabled!
  - Fixed [#56](https://github.com/Coders-After-Dark/Reskillable/issues/56)
- Started work on new API End-Points
  - Finished the first 2 points in [#86](https://github.com/Coders-After-Dark/Reskillable/issues/86)
- Made some performance improvements to our existing event handlers to deal with locking.
- Made some changes to the Skill and Trait code to allow for more powerful Custom Skills/Traits features.
- Added Generic-Partial Lock Matching!


## 1.3.0
- Large Improvements to the NBT-Handling Internally
- Large Improvements to the Lock System
  - Made it overall more Generic



## 1.2.2
- Added mcmod.info file


## 1.2.1
- Fix being able to place blocks that are locked


## 1.2.0
- Added Mod-Based Gating
  - This only works in Configs
  - This is implemented as:
    - Example:
      - "minecraft=reskillable:defense|5,reskillable:magic|5"
- Improved NBT Matching A LOT more powerful!
  - Added Partial NBT Matches
  - Added Lock-Merging
  - Added improved NBT Matching.
- So what does all of this mean?
  - Well for example:
    - NBT Locks Override Base-locks!
      - This means that if you have a base lock of 16
      - And a NBT lock with the same value of 8
      - Once you add the NBT Lock it'll lower the value to 8!
    - By adding a script like this:
      - https://gist.github.com/Lanse505/b19fe16f3c177ff7e0162e5761e4d1ca
        - Locks are for:
          - Efficienty 5
          - Silk Touch
          - Unbreaking 3
        - You'd get a result like this with all enchantments applied: 
          - https://i.imgur.com/gCfETAh.png
- Fixed NPE crashes with Null Skills and Traits!



## 1.1.0
- Added Config Option for X-Y Position for Traits in the GUI
- Added Better NBT Handling (Credit to pupnewfster for the PR) 
    - [Requires: CompatSkills's CrT Support]
- Added Metadata Handling for config (Credit to pupnewfster for the PR) 
    - Syntax is now: 
        - Modid:item:meta 
            - `minecraft:skull:1`
        - Also now supports wildcard metadata 
            - minecraft:diamond_sword:* 
        - If no Metadata is added, it defaults to 0
- Changed "Green Thumbs" now to apply a bone meal tick to a Growable block instead of adding a random tick to it. 
    - Might be rebalanced as time goes on!




## 1.0.0
General:
- Removed reliance on AutoRegLib

Non-Modders:
Additions:
- Added support for individual Skill level max values.
- Added a scrolling feature to the Skill Info GUI.
- Added Support for Level-Based Staggering Amounts! (See Below)
- Rewrote the config backend to support more powerful options for Skills and Traits.

- Added Trait Requirements!
    - You can now gate items/blocks/traits behind other Traits!
        - Syntax:
            - "trait|reskillable:roadwalk"
            - "trait|reskillable:sidestep"
            - "trait|reskillable:neutralissse"

- The new config system supports:
    - Supports values on a per-skill basis!
    - Setting/Changing requirements on Traits
    - Enable/Disable Skills & Traits
        - Examples:
            - Skills: https://i.imgur.com/N6Cs26N.png
            - Traits: https://i.imgur.com/4My9soP.png

- Added a new Cost Staggering system for Skills
	- Base + (currentLevel + stagger)
	- Example
		- Base = 1
		- Level = 1
		- Stagger [{
			1|1
			10|2
		}]

		- This means the equation for level 1-9 would be:
			- 1 + (currentLevel + 1)
		- While anything past 9 would be:
			- 1 + (currentLevel + 2)

Fixes:
- Fixed [Vaskii#23](https://github.com/Vazkii/Skillable/issues/23) - You should now be able to properly lock TE Machine Upgrades
- Fixed [Vaskii#18](https://github.com/Vazkii/Skillable/issues/18) - Implemented a fix for the Perfect Recovery Trait
- Fixed [Vaskii#20](https://github.com/Vazkii/Skillable/issues/20) and [Reskillable#5](https://github.com/Vazkii/Skillable/issues/5) making it so Reskillable now works on Servers.
- Implemented a potential fix for [Reskillable#14](https://github.com/Coders-After-Dark/Reskillable/issues/14) - Tools that can break multiple blocks should now not be able to cheese Reskillable.
- Implemented an improved behavior to the level-up button to make it more obvious where to click to invest experience for points.
    - Fixes [Reskillable#11](https://github.com/Coders-After-Dark/Reskillable/issues/11)


Modders:
- Implemented a large-scale API rewrite!
    - You can now as a modder add new skills, traits, and requirements to Reskillable through an addon!
- Skill icons are now based on the name of the skill.
    - See: https://github.com/Coders-After-Dark/Reskillable/blob/feature/api/src/main/java/codersafterdark/reskillable/api/skill/Skill.java#L26
    - This means for example if your mod id is XYZ and you make a skill called "ninja", you'll need to add the skill icon texture to:
        - "xyz:textures/skills/ninja"
- Implemented it so the Skill Constructor now takes a resource location for the background texture for the Skill Info GUI instead of a Block instance.
- Skills and Unlockables now use Forge Registries which can be found under: ReskillableRegistries.SKILLS.register or ReskillableRegistries.UNLOCKABLES.register
- Requirements are added in their own non-forge registry which can be found under: ReskillableAPI.getInstance().getRequirementRegistry().addRequirementHandler