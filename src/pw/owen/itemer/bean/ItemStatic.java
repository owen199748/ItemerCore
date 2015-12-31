package pw.owen.itemer.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import pw.owen.itemer.utils.RangeInt;
import pw.owen.itemer.utils.Send;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ItemStatic{
	public static final String START = "===装备属性===";
	@JsonProperty
	private RangeInt damage = null;
	// 增加的伤害
	@JsonProperty
	private RangeInt hp = null;
	// 增加的血量
	@JsonProperty
	private Crit crit = null;
	// 暴击几率
	@JsonProperty
	private Reflect reflect = null;
	// 反伤
	@JsonProperty
	private LifeSteal lifeSteal = null;
	// 生命偷取

	private ItemStatic() {
	}

	public double getCritDamage() {
		return critDamage;
	}

	public void setCritDamage(double critDamage) {
		this.critDamage = critDamage;
	}

	public double getCrit() {
		return crit;
	}

	public void setCrit(double crit) {
		this.crit = crit;
	}

	public RangeInt getDamage() {
		return damage;
	}

	public RangeInt getHp() {
		return hp;
	}

	public double getSpeed() {
		return speed;
	}

	public void setDamage(RangeInt damage) {
		this.damage = damage;
	}

	public void setHp(RangeInt hp) {
		this.hp = hp;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public ItemStack putItemStatic(ItemStack it) {
		net.minecraft.server.v1_8_R3.ItemStack it2 = CraftItemStack
				.asNMSCopy(it);
		NBTTagCompound tag = it2.getTag();
		if (tag == null)
			tag = new NBTTagCompound();
		ObjectMapper mapper = new ObjectMapper();

		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

		try {
			tag.setString("ItemerNBT_owen", mapper.writeValueAsString(this));
		} catch (JsonProcessingException e) {
			Send.sendConsole("NBT序列化失败");
			e.printStackTrace();
		}
		it2.setTag(tag);
		ItemStack it3 = CraftItemStack.asBukkitCopy(it2);
		return updateTag(it3);
	}
	
	

	public static ItemStatic getItemStatic(ItemStack it) {
		net.minecraft.server.v1_8_R3.ItemStack it2 = CraftItemStack
				.asNMSCopy(it);
		if (it2.getTag() == null)
			it2.setTag(new NBTTagCompound());
		String nbt = it2.getTag().getString("ItemerNBT_owen");
		if (nbt != null && nbt.trim() != "") {
			// 加载NBT反序列化为ItemStatic实例.
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			mapper.configure(
					DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
					true);
			mapper.configure(
					DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING,
					true);
			mapper.configure(
					DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL,
					true);
			mapper.configure(
					DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
			mapper.configure(DeserializationFeature.WRAP_EXCEPTIONS, true);

			try {
				return mapper.readValue(nbt, ItemStatic.class);
			} catch (IOException e) {
				Send.sendConsole("NBT反序列化失败");
				e.printStackTrace();
				return null;

			}
		} else
			return null;

	}

	public static ItemStatic getNewItemStatic() {
		return new ItemStatic();
	}

	public static double getDamageMarkon(double dmg, List<ItemStatic> item) {
		// TODO 计算这些物品的伤害对伤害的加成
		return 0;
	}

	public static double getArmorMarkon(double dmg, List<ItemStatic> item) {
		// TODO 计算这些物品的护甲对伤害的加成
		return 0;
	}

	private ItemStack updateTag(ItemStack it3) {
		ItemMeta im = it3.getItemMeta();
		Object[] tag = im.getLore().toArray();
		for (int i = 0; i < tag.length; i++)
			if (((String) tag[i]).equals(START))
				tag = Arrays.copyOf(tag, i);

		List<String> ls = new ArrayList<String>();
		for (int i = 0; i < tag.length; i++)
			ls.add(tag[i].toString());
		ls.add(START);
		ls.addAll(Arrays.asList(toString().split("\n")));
		im.setLore(ls);
		it3.setItemMeta(im);
		return it3;
	}

	@Override
	public String toString() {
		// 返回期待的属性列表
		return "";
	}
}
