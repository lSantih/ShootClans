package dev.santih.shootclans.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Cuboid {

	private final int xMin;
	private final int xMax;
	private final int yMin;
	private final int yMax;
	private final int zMin;
	private final int zMax;
	private final double xMinCentered;
	private final double xMaxCentered;
	private final double yMinCentered;
	private final double yMaxCentered;
	private final double zMinCentered;
	private final double zMaxCentered;
	private final World world;

	private final Location point1;
	private final Location point2;
	private final Random rand = new Random();

	public Cuboid(final Location point1, final Location point2) {
		if (point1 == null || point2 == null) {
			throw new IllegalArgumentException("Points cannot be null");
		}
		if (!point1.getWorld().equals(point2.getWorld())) {
			throw new IllegalArgumentException("Points must be in the same world");
		}

		this.xMin = Math.min(point1.getBlockX(), point2.getBlockX());
		this.xMax = Math.max(point1.getBlockX(), point2.getBlockX());
		this.yMin = Math.min(point1.getBlockY(), point2.getBlockY());
		this.yMax = Math.max(point1.getBlockY(), point2.getBlockY());
		this.zMin = Math.min(point1.getBlockZ(), point2.getBlockZ());
		this.zMax = Math.max(point1.getBlockZ(), point2.getBlockZ());
		this.world = point1.getWorld();
		this.xMinCentered = this.xMin + 0.5;
		this.xMaxCentered = this.xMax + 0.5;
		this.yMinCentered = this.yMin + 0.5;
		this.yMaxCentered = this.yMax + 0.5;
		this.zMinCentered = this.zMin + 0.5;
		this.zMaxCentered = this.zMax + 0.5;

		this.point1 = point1;
		this.point2 = point2;
	}

	public Iterator<Block> blockList() {
		List<Block> bL = new ArrayList<>(this.getTotalBlockSize());
		IntStream.rangeClosed(this.xMin, this.xMax)
				.forEach(x -> IntStream.rangeClosed(this.yMin, this.yMax)
						.forEach(y -> IntStream.rangeClosed(this.zMin, this.zMax)
								.forEach(z -> {
									Location location = new Location(this.world, x, y, z);
									Block b = this.world.getBlockAt(location);
									bL.add(b);
								})));
		return bL.iterator();
	}

	public Location getCenter() {
		return new Location(this.world, (this.xMax - this.xMin) / 2 + this.xMin, (this.yMax - this.yMin) / 2 + this.yMin, (this.zMax - this.zMin) / 2 + this.zMin);
	}

	public double getDistance() {
		return this.getPoint1().distance(this.getPoint2());
	}

	public double getDistanceSquared() {
		return this.getPoint1().distanceSquared(this.getPoint2());
	}

	public int getHeight() {
		return this.yMax - this.yMin + 1;
	}

	public Location getPoint1() {
		return new Location(this.world, this.xMin, this.yMin, this.zMin);
	}

	public Location getPoint2() {
		return new Location(this.world, this.xMax, this.yMax, this.zMax);
	}

	public Location getRandomLocation() {
		final int x = rand.nextInt(Math.abs(this.xMax - this.xMin) + 1) + this.xMin;
		final int y = rand.nextInt(Math.abs(this.yMax - this.yMin) + 1) + this.yMin;
		final int z = rand.nextInt(Math.abs(this.zMax - this.zMin) + 1) + this.zMin;
		return new Location(this.world, x, y, z);
	}

	public int getTotalBlockSize() {
		return this.getHeight() * this.getXWidth() * this.getZWidth();
	}

	public int getXWidth() {
		return this.xMax - this.xMin + 1;
	}

	public int getZWidth() {
		return this.zMax - this.zMin + 1;
	}

	public boolean isIn(final Location loc) {
		return loc.getWorld().equals(this.world) && loc.getBlockX() >= this.xMin && loc.getBlockX() <= this.xMax && loc.getBlockY() >= this.yMin && loc.getBlockY() <= this.yMax && loc
				.getBlockZ() >= this.zMin && loc.getBlockZ() <= this.zMax;
	}

	public boolean isIn(final Player player) {
		return this.isIn(player.getLocation());
	}

	public boolean isInWithMarge(final Location loc, final double marge) {
		return loc.getWorld().equals(this.world) && loc.getX() >= this.xMinCentered - marge && loc.getX() <= this.xMaxCentered + marge && loc.getY() >= this.yMinCentered - marge && loc
				.getY() <= this.yMaxCentered + marge && loc.getZ() >= this.zMinCentered - marge && loc.getZ() <= this.zMaxCentered + marge;
	}

	@Override
	public String toString() {
		return String.format("Cuboid[(%.2f, %.2f, %.2f), (%.2f, %.2f, %.2f)]", point1.getX(), point1.getY(), point1.getZ(), point2.getX(), point2.getY(), point2.getZ());
	}

	public void savePoint(final ConfigurationSection section, Location point) {
		section.set("x", point.getX());
		section.set("y", point.getY());
		section.set("z", point.getZ());
		section.set("yaw", point.getYaw());
		section.set("pitch", point.getPitch());
		section.set("worldName", point.getWorld().getName());
	}

	public void savePoint1(final ConfigurationSection section) {
		savePoint(section, point1);
	}

	public void savePoint2(final ConfigurationSection section) {
		savePoint(section, point2);
	}

	public boolean intersects(Cuboid other) {
		return (this.getPoint1().getX() <= other.getPoint2().getX() && this.getPoint2().getX() >= other.getPoint1().getX()) &&
				(this.getPoint1().getY() <= other.getPoint2().getY() && this.getPoint2().getY() >= other.getPoint1().getY()) &&
				(this.getPoint1().getZ() <= other.getPoint2().getZ() && this.getPoint2().getZ() >= other.getPoint1().getZ());
	}


}