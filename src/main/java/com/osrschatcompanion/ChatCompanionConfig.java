package com.osrschatcompanion;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("osrsChatCompanion")
public interface ChatCompanionConfig extends Config
{
	String CHAT_DISCLOSURE =
		"When enabled (Phase 4+), chat messages and a snapshot of your local game state "
			+ "(inventory item ids/names/quantities, equipped items, skill levels, and quest progress) "
			+ "are sent to the configured backend proxy so an LLM can answer questions. "
			+ "Your IP address is also visible to that server. "
			+ "No game inputs are automated; this plugin is read/advise only. "
			+ "Phase 1 does not send any data over the network.";

	@ConfigItem(
		keyName = "chatEnabled",
		name = "Enable chat companion (third-party)",
		description = CHAT_DISCLOSURE,
		warning = "This feature submits your IP address to a 3rd-party server not controlled or verified by RuneLite developers. "
			+ "When chat is enabled, your messages and a snapshot of local game state "
			+ "(inventory, equipment, skills, quest progress) are sent to the configured backend proxy. "
			+ "Phase 1 only logs locally and does not send network requests.",
		position = 0
	)
	default boolean chatEnabled()
	{
		return false;
	}

	@ConfigItem(
		keyName = "backendUrl",
		name = "Backend URL",
		description = "HTTPS base URL of your chat companion backend (unused in Phase 1).",
		position = 1
	)
	default String backendUrl()
	{
		return "http://localhost:3000";
	}
}
