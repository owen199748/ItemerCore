package pw.owen.itemer.bean.attribute;

import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Attack extends Attribute {
	private double attack = 0;
	@Override
	public void runEvent(Event event, LivingEntity e) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent eve = (EntityDamageByEntityEvent) event;
			if (eve.getDamager().getUniqueId().toString()
					.equals(e.getUniqueId().toString())) {
				eve.setDamage(eve.getDamage() + attack);
			}
		}

	}


	@Override
	public String getInfo() {
		return "¹¥»÷Á¦: " + attack;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public String getName() {

		return "¹¥»÷Á¦";
	}


	@Override
	protected void loadElseYml(ConfigurationSection sc) {

	}


	@Override
	protected void saveElseYml(ConfigurationSection sc) {

	}


	@Override
	protected Map<String, String> getElseStaticParameters(
			Map<String, String> maps) {
		return maps;
	}



	@Override
	protected Map<String, String> getElseParameters(Map<String, String> maps) {
		maps.put("¹¥»÷Á¦", "attack");
		return maps;
	}

}
