package pw.owen.itemer.bean.attribute;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class Attribute implements Attributive {
	private String sendInfo = getInfo();
	@Override
	public int getPriority() {
		return 0;
	}


	@Override
	public void saveYml(YamlConfiguration yml) throws Exception {
		ConfigurationSection sc = yml.createSection(getName());
		// 反射保存所有静态被注册字段
		Map<String, String> map = getStaticParameters();
		Object[] ary = map.keySet().toArray();
		for (int i = 0; i < ary.length; i++)
			sc.set((String) ary[i], getStatic((String) ary[i]));
		saveElseYml(sc);
	}

	@Override
	public void loadYml(YamlConfiguration yml) throws Exception {
		ConfigurationSection sc = yml.getConfigurationSection(getName());
		if (sc == null)
			return;
		// 反射读取所有静态被注册字段
		Map<String, String> map = getStaticParameters();
		Object[] ary = map.keySet().toArray();
		for (int i = 0; i < ary.length; i++)
			setStatic((String) ary[i], sc.get((String) ary[i]) + "");
		loadElseYml(sc);
	}

	protected abstract void loadElseYml(ConfigurationSection sc);
	protected abstract void saveElseYml(ConfigurationSection sc);
	

	@Override
	public Map<String, String> getStaticParameters() {
		Map<String, String> maps = new HashMap<String, String>();
		maps = getElseStaticParameters(maps);
		if (maps == null)
			maps = new HashMap<String, String>();

		for (int i = 0; i < this.getClass().getFields().length; i++)
			if (Modifier
					.isStatic(this.getClass().getFields()[i]
					.getModifiers()))
				if (!hasValueInMap(maps,
						this.getClass().getFields()[i].getName()))
					maps.put(this.getClass().getFields()[i].getName(), this
							.getClass().getFields()[i].getName());
		return maps;
	}

	protected abstract Map<String, String> getElseStaticParameters(
			Map<String, String> maps);

	@Override
	public Map<String, String> getParameters() {
		Map<String, String> maps = new HashMap<String, String>();
		maps = getElseParameters(maps);
		if (maps == null)
			maps = new HashMap<String, String>();

		for (int i = 0; i < this.getClass().getFields().length; i++)
			if (!Modifier.isStatic(this.getClass().getFields()[i]
					.getModifiers()))
				if (!hasValueInMap(maps,
						this.getClass().getFields()[i].getName()))
					maps.put(this.getClass().getFields()[i].getName(), this
							.getClass().getFields()[i].getName());
		return maps;
	}

	private static boolean hasValueInMap(Map<String, String> maps, String name) {
		Object[] key = maps.keySet().toArray();
		for (int i = 0; i < key.length; i++)
			if (maps.get(key[i]).equals(name))
				return true;
		return false;
	}


	protected abstract Map<String, String> getElseParameters(
			Map<String, String> maps);

	/** 以下为反射调用参数 **/
	@Override
	public Object getStatic(String par) throws Exception {
		String str = this.getStaticParameters().get(par);
		if (str == null)
		return null;

		// 反射获取变量
		Field fl = this.getClass().getField(str);
		if (Modifier.isStatic(fl.getModifiers()))
			return fl.get(null);
		else
			return null;

	}

	@Override
	public boolean setStatic(String par, String value) throws Exception {
		String str = this.getStaticParameters().get(par);
		if (str == null)
		return false;

		// 反射获取变量

		Field fl = this.getClass().getField(str);
		if (Modifier.isStatic(fl.getModifiers()))
			return setFieldValue(fl, value);
		else
			return false;
	}

	private static boolean setFieldValue(Field fl, String value)
			throws IllegalAccessException {

			Object vs = null;
			try {
			if (fl.getType() == String.class)
				vs = value;
			else if (fl.getType() == Integer.class || fl.getType() == int.class)
				vs = Integer.parseInt(value);
			else if (fl.getType() == Boolean.class
					|| fl.getType() == boolean.class)
				vs = Boolean.parseBoolean(value);
			else if (fl.getType() == Long.class || fl.getType() == long.class)
				vs = Long.parseLong(value);
			else if (fl.getType() == Short.class || fl.getType() == short.class)
				vs = Short.parseShort(value);
			else if (fl.getType() == Character[].class
					|| fl.getType() == char[].class)
				vs = value.toCharArray();
			else if (fl.getType() == Double.class
					|| fl.getType() == double.class)
				vs = Double.parseDouble(value);
			else if (fl.getType() == Byte.class || fl.getType() == byte.class)
				vs = Byte.parseByte(value);
			else if (fl.getType() == Float.class || fl.getType() == float.class)
				vs = Float.parseFloat(value);
			else
				return false;
			} catch (NumberFormatException ex) {
				return false;
			}
			try {
			fl.set(null, vs);
			} catch (IllegalArgumentException ex) {
				return false;
			}
			return true;

	}


	@Override
	public Object get(String par) throws Exception {
		String str = this.getParameters().get(par);
		if (str == null)
		return null;

		// 反射获取变量
		Field fl = this.getClass().getField(str);
		if (!Modifier.isStatic(fl.getModifiers()))
			return fl.get(this);
		else
			return null;
	}
	@Override
	public boolean set(String par, String value) throws Exception {
		String str = this.getParameters().get(par);
		if (str == null)
		return false;

		// 反射获取变量
		Field fl = this.getClass().getField(str);
		if (!Modifier.isStatic(fl.getModifiers()))
			return setFieldValue(fl, value);
		else
			return false;
	}


}
