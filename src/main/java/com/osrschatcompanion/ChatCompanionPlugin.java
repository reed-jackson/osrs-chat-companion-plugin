package com.osrschatcompanion;

import com.google.gson.Gson;
import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

@Slf4j
@PluginDescriptor(
	name = "OSRS Chat Companion",
	description = "Natural-language chat helper (read/advise only). When third-party chat is enabled, "
		+ "messages and a local game-state snapshot (inventory, equipment, skills, quests) are sent "
		+ "to a backend you configure — never automate gameplay.",
	tags = {"chat", "helper", "wiki", "panel"}
)
public class ChatCompanionPlugin extends Plugin
{
	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ClientThread clientThread;

	@Inject
	private PlayerStateProvider playerStateProvider;

	@Inject
	private Gson gson;

	private ChatCompanionPanel panel;
	private NavigationButton navButton;

	@Override
	protected void startUp()
	{
		panel = injector.getInstance(ChatCompanionPanel.class);
		panel.init(this::onMessageSubmitted);

		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "icon.png");

		navButton = NavigationButton.builder()
			.tooltip("OSRS Chat Companion")
			.icon(icon)
			.priority(5)
			.panel(panel)
			.build();

		clientToolbar.addNavigation(navButton);
		log.info("OSRS Chat Companion started (Phase 1: local state dump only)");
	}

	@Override
	protected void shutDown()
	{
		clientToolbar.removeNavigation(navButton);
		log.info("OSRS Chat Companion stopped");
	}

	private void onMessageSubmitted(String message)
	{
		clientThread.invoke(() ->
		{
			PlayerState state = playerStateProvider.snapshot();
			String json = gson.toJson(state);
			log.info("Chat companion message: {}", message);
			log.info("Chat companion player state JSON: {}", json);
			panel.addSystemMessage("Logged message + player state JSON to client log (Phase 1, no network).");
		});
	}

	@Provides
	ChatCompanionConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ChatCompanionConfig.class);
	}
}
