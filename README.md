# Shapeless Portals – Highly Configurable Nether Portal Plugin

**Shapeless Portals** lets players ignite custom-shaped Nether portals made from any materials configured in the config.

**Join our** [**Discord**](https://discord.gg/YGzA4UxzFB) — discover other cool plugins, suggest new features, or get help with configurations.

### Features ✨

- **Unlimited Portal Shapes**  
  Create portals in any geometry: squares, triangles, hearts, or abstract shapes made of straight lines.

- **Configurable Frame Materials**  
  Want CRYING_OBSIDIAN instead of classic OBSIDIAN? Just add it to the list in the config.

- **Permission & Player Controls**  
  Enable `shapelessPortals.ignite` or allow everyone to ignite.

- **Size Limits**  
  Set max/min portal opening size: from 1×1 up to 21×21 (or any values you choose).

- **Hot Reload**  
  No restarts needed! Change the config and run `/shapelessportals reload`.

### Main config (`config.yml`) ⚙️

<details>
  <summary><strong>config.yml</strong></summary>

  ```yaml
# Join our discord - https://discord.gg/YGzA4UxzFB you can find other cool plugins there.
# "shapelessPortals.reload" - permission required to use "/shapelessportals reload" command

# if true, only players can ignite shapeless portals
onlyPlayerCanIgnite: false

# if true, players must have permission to ignite shapeless portals
isPermissionRequired: false
permission: "shapelessPortals.ignite"

# max width/height of portal opening (portal blocks)
maxWidth: 21
maxHeight: 21

# min size of portal opening (portal blocks)
minPortalSize: 1

# allowed frame materials (Obsidian, Crying Obsidian, etc.)
portalFrameMaterials:
  - OBSIDIAN
  - CRYING_OBSIDIAN

# reload messages, supports MiniMessage
noPermissionToReload: "<red>✘ <white>You don't have permission!"
successfullyReloaded: "<green>✔ <white>Config reloaded!"
````

</details>

### Permissions 🔐

| Permission Node           | Description                          |
| ------------------------- | ------------------------------------ |
| `shapelessPortals.ignite` | Allows ignition of shapeless portals |
| `shapelessPortals.reload` | Allows reloading the plugin config   |

### Commands 📟

| Command                    | Description                   |
| -------------------------- | ----------------------------- |
| `/shapelessportals reload` | Reload plugin config          |
