package pw.owen.itemer.bean.attribute;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import pw.owen.itemer.utils.Send;

public class Crit implements Attributive {
	private int crit = 0;
	private int critDamage = 200;
	private static String critInfo = "������˱���!";
	@Override
	public void runEvent(Event event, LivingEntity e) {
		if(crit<=0)
			return;
		
		if(event instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent eve = (EntityDamageByEntityEvent)event;
			if (eve.getDamager().getUniqueId().toString()
					.equals(e.getUniqueId().toString())) {
				if ((int) (Math.random() * 100) < crit)
 {
					eve.setDamage(eve.getDamage() * (critDamage / 100));
					if (e instanceof Player)
						Send.sendPluginMessage((Player) e, critInfo);
				}
			}
			
		}

	}

	@Override
	public Map<String, Class<?>> getParameters() {
		Map<String, Class<?>> mp = new HashMap<String, Class<?>>();
		mp.put("������", int.class);
		mp.put("�����˺�", int.class);
		return mp;
	}

	@Override
	public boolean set(String par, String value) {
		if (par.equalsIgnoreCase("������")) {
			crit = Integer.parseInt(value);
			return true;
		} else if (par.equalsIgnoreCase("�����˺�")) {
			critDamage = Integer.parseInt(value);
			return true;
		}
		return false;
	}

	@Override
	public Object get(String par) {
		if (par.equalsIgnoreCase("������")) {
			return crit;
		} else if (par.equalsIgnoreCase("�����˺�")) {
			return critDamage;
		}
		return null;
	}

	@Override
	public Map<String, Class<?>> getStaticParameters() {
	HashMap<String, Class<?>> mp = new HashMap<String, Class<?>>();
		mp.put("������ʾ", String.class);
		return mp;
	}

	@Override
	public boolean setStatic(String par, String value) {
		if (par.equalsIgnoreCase("������ʾ")) {
			critInfo = value;
			return true;
		}
		// TODO �Զ����ɵķ������
		return false;
	}

	@Override
	public Object getStatic(String par) {
		if (par.equalsIgnoreCase("������ʾ"))
			return critInfo;
		return null;
	}

	@Override
	public String getInfo() {
		return "������: " + crit + "% , �����˺�: " + critDamage + "%";
	}

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public String getName() {
		return "����";
	}

}
