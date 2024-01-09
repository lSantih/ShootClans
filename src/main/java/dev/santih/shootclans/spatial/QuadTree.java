package dev.santih.shootclans.spatial;

import dev.santih.shootclans.utils.Cuboid;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuadTree {
	private static final int MAX_OBJECTS = 10;
	private static final int MAX_LEVELS = 5;

	private final int level;
	private final List<Cuboid> objects;
	private final Cuboid bounds;
	private final QuadTree[] nodes;

	public QuadTree(int level, Cuboid bounds) {
		this.level = level;
		this.objects = new ArrayList<>();
		this.bounds = bounds;
		this.nodes = new QuadTree[4];
	}

	public void clear() {
		objects.clear();
		for (QuadTree node : nodes) {
			if (node != null) {
				node.clear();
				node = null;
			}
		}
	}

	private void split() {
		int subWidth = (int) (bounds.getXWidth() / 2);
		int subHeight = (int) (bounds.getHeight() / 2);
		int subDepth = (int) (bounds.getZWidth() / 2);
		int x = (int) bounds.getPoint1().getX();
		int y = (int) bounds.getPoint1().getY();
		int z = (int) bounds.getPoint1().getZ();
		World world = bounds.getPoint1().getWorld();

		nodes[0] = new QuadTree(level + 1, new Cuboid(new Location(world, x + subWidth, y, z), new Location(world, x + 2 * subWidth, y + subHeight, z + subDepth)));
		nodes[1] = new QuadTree(level + 1, new Cuboid(new Location(world, x, y, z), new Location(world, x + subWidth, y + subHeight, z + subDepth)));
		nodes[2] = new QuadTree(level + 1, new Cuboid(new Location(world, x, y + subHeight, z), new Location(world, x + subWidth, y + 2 * subHeight, z + subDepth)));
		nodes[3] = new QuadTree(level + 1, new Cuboid(new Location(world, x + subWidth, y + subHeight, z), new Location(world, x + 2 * subWidth, y + 2 * subHeight, z + subDepth)));
	}

	private enum Quadrant {
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT
	}

	private Quadrant getQuadrant(Cuboid cuboid) {
		double verticalMidpoint = bounds.getPoint1().getX() + (bounds.getXWidth() / 2);
		double horizontalMidpoint = bounds.getPoint1().getZ() + (bounds.getZWidth() / 2);

		boolean topQuadrant = cuboid.getPoint1().getZ() < horizontalMidpoint && cuboid.getPoint2().getZ() < horizontalMidpoint;
		boolean bottomQuadrant = cuboid.getPoint1().getZ() > horizontalMidpoint && cuboid.getPoint2().getZ() > horizontalMidpoint;

		if (cuboid.getPoint1().getX() < verticalMidpoint && cuboid.getPoint2().getX() < verticalMidpoint) {
			if (topQuadrant) {
				return Quadrant.TOP_LEFT;
			} else if (bottomQuadrant) {
				return Quadrant.BOTTOM_LEFT;
			}
		} else if (cuboid.getPoint1().getX() > verticalMidpoint && cuboid.getPoint2().getX() > verticalMidpoint) {
			if (topQuadrant) {
				return Quadrant.TOP_RIGHT;
			} else if (bottomQuadrant) {
				return Quadrant.BOTTOM_RIGHT;
			}
		}

		return null;
	}

	public void insert(Cuboid cuboid) {
		if (nodes[0] != null) {
			Quadrant quadrant = getQuadrant(cuboid);

			if (quadrant != null) {
				nodes[quadrant.ordinal()].insert(cuboid);
				return;
			}
		}

		objects.add(cuboid);

		if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
			if (nodes[0] == null) {
				split();
			}

			Iterator<Cuboid> iterator = objects.iterator();
			while (iterator.hasNext()) {
				Cuboid obj = iterator.next();
				Quadrant quadrant = getQuadrant(obj);
				if (quadrant != null) {
					nodes[quadrant.ordinal()].insert(obj);
					iterator.remove();
				}
			}
		}
	}

	public List<Cuboid> retrieve(List<Cuboid> returnObjects, Cuboid cuboid) {
		Quadrant quadrant = getQuadrant(cuboid);
		if (quadrant != null && nodes[0] != null) {
			nodes[quadrant.ordinal()].retrieve(returnObjects, cuboid);
		}

		for (Cuboid obj : objects) {
			if (cuboid.intersects(obj)) {
				returnObjects.add(obj);
			}
		}

		return returnObjects;
	}
}
