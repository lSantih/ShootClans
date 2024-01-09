package dev.santih.shootclans.utils;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@SerializableAs("item-builder")
public class ItemBuilder {

	private static final ItemFlag[] EMPTY_ITEM_FLAG_ARRAY = new ItemFlag[0];

	private final ItemStack item;
	private final Map<Object, Object> customData;

	public ItemBuilder(ItemStack item, Map<Object, Object> customData, String tuputamadre) {
		this.item = item == null ? new ItemStack(Material.AIR) : item;
		this.customData = customData;
	}


	public ItemBuilder(final Material material) {
		this(new ItemStack(material), new HashMap<>(), null);
	}

	@SuppressWarnings("unchecked")
	public <M extends ItemMeta> ItemBuilder consumeMeta(Consumer<M> consumer) {
		M meta = (M) item.getItemMeta();
		if (meta == null)
			return this;

		consumer.accept(meta);
		item.setItemMeta(meta);

		return this;
	}

	@SuppressWarnings("unchecked")
	public <T extends ItemMeta> T getItemMeta() {
		return (T) item.getItemMeta();
	}

	@SuppressWarnings("unchecked")
	public <T, M extends ItemMeta> T getFromMeta(Function<M, T> supplier, T def) {
		M meta = (M) item.getItemMeta();
		if (meta == null)
			return def;

		T t = supplier.apply(meta);
		return t == null ? def : t;
	}

	public Material type() {
		return item.getType();
	}

	public ItemBuilder amount(int amount) {
		item.setAmount(amount);
		return this;
	}

	public ItemBuilder potionType(PotionType potionType) {
		if (item.getType() == Material.POTION || item.getType() == Material.SPLASH_POTION || item.getType() == Material.LINGERING_POTION) {
			PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
			PotionData potionData = new PotionData(potionType);
			potionMeta.setBasePotionData(potionData);
			item.setItemMeta(potionMeta);
		}
		return this;
	}

	public int amount() {
		return item.getAmount();
	}

	public ItemBuilder damage(int durability) {
		return consumeMeta(meta -> {
			if (meta instanceof Damageable damageable)
				damageable.setDamage(durability);
		});
	}

	public int damage() {
		return getFromMeta(meta -> {
			if (meta instanceof Damageable damageable)
				return damageable.getDamage();
			return null;
		}, 0);
	}

	public ItemBuilder lore(@Nullable String... lore) {
		return consumeMeta(meta -> meta.setLore(lore == null ? null : MessageUtils.color(Arrays.asList(lore))));
	}

	public ItemBuilder lore(@Nullable List<String> lore) {
		return consumeMeta(meta -> meta.setLore(MessageUtils.color(lore)));
	}

	public ItemBuilder itemFlags(@NotNull ItemFlag... itemFlags) {
		return consumeMeta(meta -> {
			meta.removeItemFlags(meta.getItemFlags().toArray(EMPTY_ITEM_FLAG_ARRAY));
			meta.addItemFlags(itemFlags);
		});
	}

	public ItemBuilder itemFlags(@NotNull Set<ItemFlag> itemFlags) {
		return consumeMeta(meta -> {
			meta.removeItemFlags(meta.getItemFlags().toArray(EMPTY_ITEM_FLAG_ARRAY));
			meta.addItemFlags(itemFlags.toArray(EMPTY_ITEM_FLAG_ARRAY));
		});
	}

	public ItemBuilder enchantment(Enchantment enchantment, int level) {
		return consumeMeta(meta -> meta.addEnchant(enchantment, level, true));
	}

	public ItemBuilder enchantments(Map<Enchantment, Integer> enchantments) {
		return consumeMeta(meta -> enchantments.forEach((enchantment, level) -> meta.addEnchant(enchantment, level, true)));
	}

	public Map<Enchantment, Integer> enchantments() {
		return getFromMeta(ItemMeta::getEnchants, new HashMap<>());
	}

	@NotNull
	public String displayName() {
		return getFromMeta(ItemMeta::getDisplayName, "");
	}

	public ItemBuilder displayName(@Nullable String displayName) {
		return consumeMeta(meta -> meta.setDisplayName(MessageUtils.color(displayName)));
	}

	@NotNull
	public List<String> lore() {
		return getFromMeta(ItemMeta::getLore, new ArrayList<>());
	}

	@NotNull
	public Set<ItemFlag> itemFlags() {
		return getFromMeta(ItemMeta::getItemFlags, new HashSet<>());
	}

	public ItemBuilder unbreakable(boolean unbreakable) {
		return consumeMeta(meta -> meta.setUnbreakable(unbreakable));
	}

	public boolean unbreakable() {
		return getFromMeta(ItemMeta::isUnbreakable, false);
	}

	public ItemBuilder customData(Object key, Object value) {
		this.customData.put(key, value);
		return this;
	}

	public ItemBuilder customData(Map<Object, Object> customData) {
		this.customData.clear();
		this.customData.putAll(customData);
		return this;
	}

	public Map<Object, Object> customData() {
		return this.customData;
	}


	public ItemBuilder mergeLore(ItemBuilder other) {
		if (other == this)
			return this;

		consumeMeta(meta -> {
			List<String> lore = meta.getLore();
			if (lore == null)
				lore = new ArrayList<>();

			lore.addAll(other.lore());
			meta.setLore(lore);
		});
		return this;
	}

	public ItemBuilder mergeItemFlags(ItemBuilder other) {
		if (other == this)
			return this;

		consumeMeta(meta -> meta.addItemFlags(other.itemFlags().toArray(EMPTY_ITEM_FLAG_ARRAY)));
		return this;
	}

	public ItemBuilder mergeEnchantments(ItemBuilder other) {
		if (other == this)
			return this;

		consumeMeta(meta -> {
			for (Map.Entry<Enchantment, Integer> entry : other.enchantments().entrySet()) {
				meta.addEnchant(entry.getKey(), entry.getValue(), true);
			}
		});
		return this;
	}

	public ItemStack build() {
		return item;
	}


	@Override
	public ItemBuilder clone() {
		return new ItemBuilder(item.clone(), new HashMap<>(customData), null);
	}

	/**
	 * Copies the data from the given ItemStack into a new builder.
	 * If the item is null, air or not have an ItemMeta, the builder will be empty
	 *
	 * @param item the item to copy from
	 * @return a new builder with the data from the given item
	 */
	public static ItemBuilder fromItemStack(@Nullable ItemStack item) {
		return new ItemBuilder(item, new HashMap<>(), null);
	}

	private static Color getColor(Object object) {
		if (object instanceof Color color)
			return color;

		if (object instanceof String string) {
			try {
				return Color.fromRGB(Integer.parseInt(string.substring(1), 16));
			} catch (NumberFormatException ignored) {
			}
		}

		return null;
	}
}