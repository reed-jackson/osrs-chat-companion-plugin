# Project Brief: OSRS Chat Companion (RuneLite Plugin)

## Goal
A RuneLite side-panel plugin that lets a player chat in natural language with an LLM agent that has tool access to:
1. The player's current in-game state (inventory, equipment, skills, quest progress) — read locally via the RuneLite Client API, no network call.
2. The OSRS Wiki (drop rates, quest requirements, monster stats, item info) — via the MediaWiki API.
3. Grand Exchange prices — via the OSRS Wiki Real-time Prices API.

## Non-negotiable constraints
- **No automation.** Read/advise only. Never send input events to the game client.
- **Disclosure requirement.** Visible warning explaining exactly what game data is sent to a third-party server when chat is enabled.
- **No embedded API keys.** All LLM calls go through a backend proxy you control.
- **Be a good API citizen.** Cache wiki/price lookups aggressively and respect rate limits.

## Architecture
```
RuneLite Client (Java plugin)
  ├─ reads local game state (Client API) — no network
  ├─ chat panel UI (PluginPanel)
  └─ HTTPS ──> Backend proxy (holds LLM API key, does tool-calling loop)
                 ├─ calls LLM API (tool use / function calling)
                 ├─ tool: wiki_search / wiki_page   -> OSRS Wiki MediaWiki API
                 ├─ tool: ge_price(item)             -> OSRS Wiki Prices API
                 └─ tool: get_player_state           -> data passed up from plugin in the request
```

The LLM tool-calling loop lives in the backend, not in the Java plugin.

## Data sources
- OSRS Wiki: `https://oldschool.runescape.wiki/api.php`
- GE prices: `https://prices.runescape.wiki/api/v1/osrs/`
- Player state: local `net.runelite.api.Client` only

## Phases
1. Plugin skeleton — panel + PlayerStateProvider, log message + JSON (no network)
2. Wiki + prices clients in backend (standalone, cached)
3. Backend proxy + agent loop (`POST /chat`, SSE/chunked)
4. Wire plugin to backend + disclosure toggle
5. Polish + Plugin Hub submission

## Repos
- Plugin: this repo (Java 11, Gradle, example-plugin template)
- Backend: `osrs-chat-companion-backend` (Node/TypeScript) — separate workspace

## This repo phase focus
**Phase 1** until explicitly moving on. No HTTP from the plugin yet.
