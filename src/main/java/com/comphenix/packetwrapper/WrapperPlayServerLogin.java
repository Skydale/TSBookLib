/*
 * PacketWrapper - ProtocolLib wrappers for Minecraft packets
 * Copyright (C) dmulloy2 <http://dmulloy2.net>
 * Copyright (C) Kristian S. Strangeland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.comphenix.packetwrapper;

import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.BukkitConverters;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import java.util.Set;

public class WrapperPlayServerLogin extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Server.LOGIN;

	public WrapperPlayServerLogin() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerLogin(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Entity ID.
	 * <p>
	 * Notes: entity's ID
	 *
	 * @return The current Entity ID
	 */
	public int getEntityID() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Set Entity ID.
	 *
	 * @param value - new value.
	 */
	public void setEntityID(int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve the entity of the painting that will be spawned.
	 *
	 * @param world - the current world of the entity.
	 * @return The spawned entity.
	 */
	public Entity getEntity(World world) {
		return handle.getEntityModifier(world).read(0);
	}

	/**
	 * Retrieve the entity of the painting that will be spawned.
	 *
	 * @param event - the packet event.
	 * @return The spawned entity.
	 */
	public Entity getEntity(PacketEvent event) {
		return getEntity(event.getPlayer().getWorld());
	}

	/**
	 * Retrieve Gamemode.
	 * <p>
	 * Notes: 0: survival, 1: creative, 2: adventure. Bit 3 (0x8) is the
	 * hardcore flag
	 *
	 * @return The current Gamemode
	 */
	public EnumWrappers.NativeGameMode getGamemode() { return handle.getGameModes().read(0); }

	/**
	 * Set Gamemode.
	 *
	 * @param value - new value.
	 */
	public void setGamemode(EnumWrappers.NativeGameMode value) {
		handle.getGameModes().write(0, value);
	}

	public void setHashedSeed(long value) {
		handle.getLongs().write(0, value);
	}

	public long getHashedSeed() {
		return handle.getLongs().read(0);
	}

	/**
	 * Retrieve Dimension.
	 * <p>
	 * Notes: -1: nether, 0: overworld, 1: end
	 *
	 * @return The current Dimension
	 */
	public int getDimension() {
		return handle.getDimensions().read(0);
	}

	/**
	 * Set Dimension.
	 *
	 * @param value - new value.
	 */
	public void setDimension(int value) {
		handle.getDimensions().write(0, value);
	}

	public void setResourceKey(World value) {
		handle.getWorldKeys().write(0, value);
	}

	public World getResourceKey() {
		return handle.getWorldKeys().read(0);
	}

	private StructureModifier<Set<World>> getWorldsModifier() {
		return handle.getSets(BukkitConverters.getWorldKeyConverter());
	}

	public void setWorlds(Set<World> value) {
		getWorldsModifier().write(0, value);
	}

	public Set<World> getWorlds() {
		return getWorldsModifier().read(0);
	}

	/**
	 * Retrieve Max Players.
	 * <p>
	 * Notes: used by the client to draw the player list
	 *
	 * @return The current Max Players
	 */
	public int getMaxPlayers() {
		return handle.getIntegers().read(1);
	}

	/**
	 * Set Max Players.
	 *
	 * @param value - new value.
	 */
	public void setMaxPlayers(int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve Level Type.
	 * <p>
	 * Notes: default, flat, largeBiomes, amplified, default_1_1
	 *
	 * @return The current Level Type
	 */
	public WorldType getLevelType() {
		return handle.getWorldTypeModifier().read(0);
	}

	/**
	 * Set Level Type.
	 *
	 * @param value - new value.
	 */
	public void setLevelType(WorldType value) {
		handle.getWorldTypeModifier().write(0, value);
	}

	/**
	 * Retrieve Reduced Debug Info.
	 *
	 * @return The current Reduced Debug Info
	 */
	public boolean getReducedDebugInfo() {
		return handle.getBooleans().read(0);
	}

	/**
	 * Set Reduced Debug Info.
	 *
	 * @param value - new value.
	 */
	public void setReducedDebugInfo(boolean value) { handle.getBooleans().write(0, value); }
}