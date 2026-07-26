# OSRS Chat Companion (RuneLite Plugin)

Side-panel plugin for natural-language OSRS help. **Read/advise only** — no game automation.

## Status

**Phase 1:** sidebar chat panel + local player-state JSON dump to the client log. No network calls.

## Requirements

- JDK **11** (RuneLite plugin target). On this machine:

```bash
export JAVA_HOME="$(/usr/libexec/java_home -v 11)"
# or: export JAVA_HOME=/opt/homebrew/opt/openjdk@11/libexec/openjdk.jdk/Contents/Home
```

## Run (dev client with this plugin side-loaded)

```bash
cd /path/to/osrs-chat-companion-plugin
export JAVA_HOME="$(/usr/libexec/java_home -v 11)"
./gradlew run
```

Log in with a Jagex account per [Using Jagex Accounts](https://github.com/runelite/runelite/wiki/Using-Jagex-Accounts).

### Phase 1 acceptance check

1. Enable **OSRS Chat Companion** in the plugin list if needed.
2. Click the sidebar icon (chat bubble).
3. Type a message and press Enter.
4. Confirm the panel shows your message and a system note.
5. In the client log, find `Chat companion message:` and `Chat companion player state JSON:`.

## Disclosure

When third-party chat is enabled (Phase 4+), messages and a snapshot of inventory, equipment, skills, and quest progress are sent to the configured backend. Config includes the Plugin Hub warning. Phase 1 only logs locally.

## Related

- Backend: https://github.com/reed-jackson/osrs-chat-companion-backend
- Brief: [osrs-chat-companion-brief.md](./osrs-chat-companion-brief.md)
