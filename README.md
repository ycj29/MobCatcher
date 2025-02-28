# MobCatcher - Custom Mob Capture Plugin

## Overview
**MobCatcher** is a **custom-made plugin** that allows players to capture specific mobs using **Mob Catcher Eggs** and convert them into **spawn eggs**.  
This plugin includes **reusable capture eggs**, **world restrictions**, **cooldowns**, and **protection for named mobs**.  

⚠ **This is a custom plugin with some hardcoded configurations. If you want to use it, you must compile it yourself.**

---

## Installation
1. Download the plugin source code.  
2. Compile it using **Maven** or **Gradle**.  
3. Upload the compiled `.jar` file to your server’s `/plugins` folder.  
4. Restart the server.  
5. The plugin is now active!  

---

## Features
✔ **Capture and convert specific mobs into spawn eggs**  
✔ **Supports both one-time and reusable Mob Catcher Eggs**  
✔ **Lore descriptions guide players on usage**  
✔ **Prevents named mobs from being captured**  
✔ **Configurable world restrictions** (only works in allowed worlds)  
✔ **Cooldown for reusable capture eggs to prevent spam**  

---

## Usage
### Getting a Mob Catcher Egg
- **One-time egg:** `/mobcatcher`  
- **Reusable egg:** `/mobcatcher reusable`  

### Capturing Mobs
1. **Right-click to throw the Mob Catcher Egg.**  
2. **If it hits a compatible mob, the mob is converted into a spawn egg.**  
3. **Reusable eggs automatically replenish but have a 5-second cooldown.**  

🚫 **Named mobs are protected and cannot be captured.**  
🌍 **Capture eggs only work in allowed worlds (e.g., `survival`, `survival_nether`).**  

---

## Supported Mobs
- 🏡 **Villager**
- 🐑 **Sheep**
- 🐺 **Wolf**
- 🐷 **Pig**
- 🐄 **Cow**
- 🐱 **Cat**
- 🐎 **Horse**
- 🫏 **Donkey**
- 🐴 **Mule**

---

## Configuration
⚠ **Some settings are hardcoded and require modifying the source code before compilation.**  
🔧 **Current settings include:**  
- Allowed worlds: **`survival`, `survival_nether`, `survival_the_end`**  
- Cooldown: **`5 seconds for reusable eggs`**  
- Lore instructions: **Built into the item metadata**  

---

## License & Usage
🚫 **This is a custom private plugin. Do not redistribute or sell it.**  
🔧 **Modify the code as needed, but ensure compatibility before deploying.**  
