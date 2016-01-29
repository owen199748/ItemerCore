package pw.owen.itemer.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import pw.owen.itemer.bean.attribute.Attributive;
import pw.owen.itemer.main.Main;
import pw.owen.itemer.utils.JarLoad;
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
	private static List<Class<Attributive>> cls;
	@JsonProperty
	private ArrayList<Attributive> attributes = new ArrayList<Attributive>();
	static {
		try {
			cls = new ArrayList<Class<Attributive>>();
			List<Class<?>> clss = JarLoad.getJarClass(Main.getMyfile()
					.getAbsolutePath(), Attributive.class, Main.getClassloader());
			for (int i = 0; i < clss.size(); i++)
				if (clss.get(i).newInstance() instanceof Attributive)
					cls.add((Class<Attributive>) clss.get(i));


		} catch (ClassNotFoundException | IOException | InstantiationException
				| IllegalAccessException e) {
			cls = new ArrayList<Class<Attributive>>();
			e.printStackTrace();
		}

	}

	public static void save(YamlConfiguration yml) throws Exception {
		for (int i = 0; i < cls.size(); i++)
			if (cls.get(i) != null)
				cls.get(i).newInstance().saveYml(yml);

	}

	public static void load(YamlConfiguration yml) throws Exception {
		for (int i = 0; i < cls.size(); i++)
			if (cls.get(i) != null)
				cls.get(i).newInstance().loadYml(yml);
	}

	public boolean set(String name, String parName, String value)
			throws Exception {
		boolean b = false;
		for (int i = 0; i < attributes.size(); i++)
			if (attributes.get(i).getName().equalsIgnoreCase(name))
				b = attributes.get(i).set(parName, value);
		if (!b) {
		Attributive attr = ItemStatic.getAttribute(name);
		if (attr != null) 
			if (attr.set(parName, value))
 {
				attributes.add(attr);
				b = true;
			}
		}
		
		checkVoid();
		return b;
	}

	public static List<Attributive> bubbleSort(List<Attributive> list) {
		Attributive[] ls = new ArrayList<Attributive>(list)
				.toArray(new Attributive[list.size()]);

		Attributive temp;
		for (int i = 0; i < ls.length - 1; i++)
			for (int j = i + 1; j < ls.length; j++)
				if (ls[i].getPriority() > ls[j].getPriority()) {
					temp = ls[j];
					ls[j] = ls[i];
					ls[i] = temp;
		}

		return new ArrayList<Attributive>(Arrays.asList(ls));
	}

	public void checkVoid() throws Exception {
		ArrayList<Attributive> ls = new ArrayList<Attributive>(attributes);
		for(int i=0;i<ls.size();i++){
			Attributive ab = ls.get(i);
			Object[] as = ab.getParameters().keySet().toArray();
			boolean f = false;
			for (int r = 0; r < as.length; r++)
				if (ab.get((String) as[r]) != null)
 {
					f = true;
					break;
				}
			if (!f)
				attributes.remove(ab);
		}
	}


	public static Attributive getAttribute(String name) {
		for (int i = 0; i < cls.size(); i++) {
			try {
				Attributive at = (Attributive) cls.get(i).newInstance();
				if (at.getName().equalsIgnoreCase(name))
					return at;
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				return null;
			}

		}
		return null;
	}

	private ItemStatic() throws Exception {
		// this.attributes = new Attribute[cls.size()];

	}



	public ItemStack putItemStatic(ItemStack it) {
		net.minecraft.server.v1_8_R3.ItemStack it2 = CraftItemStack
				.asNMSCopy(it);
		if (it2 == null)
			return null;
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
		if (attributes.size() == 0)
			tag.remove("ItemerNBT_owen");

		NBTTagList am = new NBTTagList();
		tag.set("AttributeModifiers", am);
		it2.setTag(tag);
		ItemStack it3 = CraftItemStack.asBukkitCopy(it2);
		return updateTag(it3);
	}
	
	

	public static ItemStatic getItemStatic(ItemStack it) {
		if (it == null)
			return null;
		net.minecraft.server.v1_8_R3.ItemStack it2 = CraftItemStack
				.asNMSCopy(it);
		if (it2 == null)
			return null;
		if (it2.getTag() == null)
			it2.setTag(new NBTTagCompound());
		String nbt = it2.getTag().getString("ItemerNBT_owen");
		if (nbt != null && nbt.trim() != "") {
			// 加载NBT反序列化为ItemStatic实例.
			ObjectMapper mapper = new ObjectMapper();
			mapper.enableDefaultTyping();
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
		try {
			return new ItemStatic();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	private ItemStack updateTag(ItemStack it3) {
		ItemMeta im = it3.getItemMeta();
		Object[] tag = null;
		if (im.getLore() != null)
			tag = im.getLore().toArray();
		else
			tag = new String[] {};
		for (int i = 0; i < tag.length; i++)
			if (((String) tag[i]).equals(START))
				tag = Arrays.copyOf(tag, i);

		List<String> ls = new ArrayList<String>();
		for (int i = 0; i < tag.length; i++)
			ls.add(tag[i].toString());
		String str = toString();
		if (str != null) {
		ls.add(START);
		ls.addAll(Arrays.asList(str.split("\n")));
		}
		im.setLore(ls);
		it3.setItemMeta(im);
		return it3;
	}

	@Override
	public String toString() {
		if (attributes.size() == 0)
			return null;
		String str = "";
		for (int i = 0; i < attributes.size(); i++) {
			String ss = attributes.get(i).getInfo();
			if (ss == null)
				continue;

			if (i != 0)
				str += "\n";
			str += ss;
		}
		return str;
	}

	public static List<ItemStatic> getItemsOfLivingEntity(LivingEntity e) {
		List<ItemStatic> ls = new ArrayList<ItemStatic>();
		
		ItemStatic it;
		if ((it = ItemStatic.getItemStatic(e.getEquipment().getBoots())) != null)
			ls.add(it);
		if ((it = ItemStatic.getItemStatic(e.getEquipment().getChestplate())) != null)
			ls.add(it);
		if ((it = ItemStatic.getItemStatic(e.getEquipment().getHelmet())) != null)
			ls.add(it);
		if ((it = ItemStatic.getItemStatic(e.getEquipment().getLeggings())) != null)
			ls.add(it);
		if ((it = ItemStatic.getItemStatic(e.getEquipment().getItemInHand())) != null)
			ls.add(it);

		return ls;
	}

	public List<Attributive> getAttributes() {
		return attributes;
	}
	public static void runEventOfLivingEntity(LivingEntity entity, Event event) {
		List<ItemStatic> ls = ItemStatic.getItemsOfLivingEntity(entity);
		List<Attributive> lss = new ArrayList<Attributive>();
		for(int i=0;i<ls.size();i++)
			for (int r = 0; r < ls.get(i).getAttributes().size(); r++)
				lss.add(ls.get(i).getAttributes().get(r));

		lss = ItemStatic.bubbleSort(lss);

		for (int i = 0; i < lss.size(); i++)
			lss.get(i).runEvent(event, entity);

	}

	public static void help(Player p) {
	Send.sendPluginMessage(p, "///帮助///");
		Send.sendPluginMessage(p, "///Itemer属性:插件所有的属性,比如攻击,血量,防御.");
		Send.sendPluginMessage(p, "///子属性:Itemer属性中的子属性,存在装备之内,比如攻击属性中的攻击力");
		Send.sendPluginMessage(p,
				"///设置属性:Itemer属性中的设置属性,不存在装备之内,比如暴击属性中的暴击伤害(统一更改)");
	Send.sendPluginMessage(p, "/Itemer set [Itemer属性] [子属性/设置属性] [值] 为手上的装备设置一个值.");
		Send.sendPluginMessage(p, "/Itemer get 列出Itemer支持的所有属性.");
		Send.sendPluginMessage(p,
				"/Itemer get [Itemer属性] 列出这个Itemer属性拥有的所有子属性和设置属性");
		Send.sendPluginMessage(p,
				"/Itemer get [Itemer属性] [子属性/设置属性] 列出这个子属性或设置属性的值");
		Send.sendPluginMessage(p, "/Itemer help 帮助");
	}

	public static boolean setStatic(String str1, String str2, String str3)
			throws Exception {
		Attributive attr = getAttribute(str1);
		if(attr==null)
		return false;
		
		return attr.setStatic(str2, str3);
	}

	public static List<Class<Attributive>> getAttributeClassList() {
		return cls;
	}
}
