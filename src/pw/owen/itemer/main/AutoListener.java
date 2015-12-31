package pw.owen.itemer.main;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import pw.owen.itemer.bean.ItemStatic;



public class AutoListener implements Listener {

	@EventHandler
	public void ede(EntityDamageByEntityEvent e) {
		double damage = e.getDamage();
		if (e.getDamager() instanceof LivingEntity) {// 计算攻击力加成
			LivingEntity le = (LivingEntity) e.getDamager();
			List<ItemStatic> ls = new ArrayList<ItemStatic>();
			if (le.getEquipment() != null)
				for (int i = 0; i < le.getEquipment().getArmorContents().length; i++)
			{
			if ( le.getEquipment().getArmorContents()[i]  == null
					|| le.getEquipment().getArmorContents()[i] .getType() == Material.AIR)
						continue;

			ItemStatic s = ItemStatic.getItemStatic(le.getEquipment()
							.getArmorContents()[i]);
			if (s == null)
						continue;

					ls.add(s);
			}
			damage = ItemStatic.getDamageMarkon(damage, ls);

		}
		
		if (e.getEntity() instanceof LivingEntity) {

			LivingEntity le = (LivingEntity) e.getEntity();
			List<ItemStatic> ls = new ArrayList<ItemStatic>();
			if (le.getEquipment() != null)
				for (int i = 0; i < le.getEquipment().getArmorContents().length; i++) {
					if (le.getEquipment().getArmorContents()[i] == null
							|| le.getEquipment().getArmorContents()[i]
									.getType() == Material.AIR)
						continue;

					ItemStatic s = ItemStatic.getItemStatic(le.getEquipment()
							.getArmorContents()[i]);
					if (s == null)
						continue;
					ls.add(s);

				}
			damage = ItemStatic.getArmorMarkon(damage, ls);
		}
		e.setDamage(damage);

	}
	


}

