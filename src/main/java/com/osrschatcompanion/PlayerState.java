package com.osrschatcompanion;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * Serializable snapshot of local player state for logging (Phase 1)
 * and later backend chat requests (Phase 4).
 */
@Data
public class PlayerState
{
	private boolean loggedIn;
	private String playerName;
	private List<ItemStack> inventory = new ArrayList<>();
	private Map<String, ItemStack> equipment = new LinkedHashMap<>();
	private Map<String, SkillLevels> skills = new LinkedHashMap<>();
	private Map<String, String> quests = new LinkedHashMap<>();

	@Data
	public static class ItemStack
	{
		private int id;
		private String name;
		private int quantity;

		public ItemStack()
		{
		}

		public ItemStack(int id, String name, int quantity)
		{
			this.id = id;
			this.name = name;
			this.quantity = quantity;
		}
	}

	@Data
	public static class SkillLevels
	{
		private int real;
		private int boosted;

		public SkillLevels()
		{
		}

		public SkillLevels(int real, int boosted)
		{
			this.real = real;
			this.boosted = boosted;
		}
	}
}
