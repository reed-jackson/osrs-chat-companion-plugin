package com.osrschatcompanion;

import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.Player;
import net.runelite.api.Quest;
import net.runelite.api.Skill;
import net.runelite.api.gameval.InventoryID;

/**
 * Reads inventory, equipment, skills, and quest state from the local Client API.
 * Must be called on the client thread.
 */
@Singleton
public class PlayerStateProvider
{
	private final Client client;

	@Inject
	PlayerStateProvider(Client client)
	{
		this.client = client;
	}

	public PlayerState snapshot()
	{
		PlayerState state = new PlayerState();

		if (client.getGameState() != GameState.LOGGED_IN)
		{
			state.setLoggedIn(false);
			return state;
		}

		state.setLoggedIn(true);

		Player localPlayer = client.getLocalPlayer();
		if (localPlayer != null)
		{
			state.setPlayerName(localPlayer.getName());
		}

		collectInventory(state);
		collectEquipment(state);
		collectSkills(state);
		collectQuests(state);

		return state;
	}

	private void collectInventory(PlayerState state)
	{
		ItemContainer container = client.getItemContainer(InventoryID.INV);
		if (container == null)
		{
			return;
		}

		for (Item item : container.getItems())
		{
			if (item == null || item.getId() <= 0)
			{
				continue;
			}
			state.getInventory().add(toItemStack(item));
		}
	}

	private void collectEquipment(PlayerState state)
	{
		ItemContainer container = client.getItemContainer(InventoryID.WORN);
		if (container == null)
		{
			return;
		}

		for (EquipmentInventorySlot slot : EquipmentInventorySlot.values())
		{
			Item item = container.getItem(slot.getSlotIdx());
			if (item == null || item.getId() <= 0)
			{
				continue;
			}
			state.getEquipment().put(slot.name(), toItemStack(item));
		}
	}

	private void collectSkills(PlayerState state)
	{
		for (Skill skill : Skill.values())
		{
			state.getSkills().put(
				skill.getName(),
				new PlayerState.SkillLevels(
					client.getRealSkillLevel(skill),
					client.getBoostedSkillLevel(skill)
				)
			);
		}
	}

	private void collectQuests(PlayerState state)
	{
		for (Quest quest : Quest.values())
		{
			state.getQuests().put(quest.getName(), quest.getState(client).name());
		}
	}

	private PlayerState.ItemStack toItemStack(Item item)
	{
		String name = "Unknown";
		ItemComposition composition = client.getItemDefinition(item.getId());
		if (composition != null)
		{
			name = composition.getName();
		}
		return new PlayerState.ItemStack(item.getId(), name, item.getQuantity());
	}
}
