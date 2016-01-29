package pw.owen.itemer.main;


import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import pw.owen.itemer.bean.ItemStatic;



public class AutoListener implements Listener {

	@EventHandler
	public void edbee(EntityDamageByEntityEvent e) {
		if (e.getCause() == DamageCause.ENTITY_ATTACK)
		if (e.getDamager() instanceof LivingEntity)
			ItemStatic.runEventOfLivingEntity((LivingEntity) e.getDamager(), e);
		
		if (e.getCause() == DamageCause.PROJECTILE)
			if (e.getDamager() instanceof Arrow)
				if (((Arrow) e.getDamager()).getShooter() instanceof LivingEntity)
					ItemStatic.runEventOfLivingEntity((LivingEntity) ((Arrow) e
							.getDamager()).getShooter(), e);

		if (e.getEntity() instanceof LivingEntity)
			ItemStatic.runEventOfLivingEntity((LivingEntity) e.getEntity(), e);


	}
	@EventHandler
	public void ede(EntityDeathEvent e) {
		if (e.getEntity() instanceof LivingEntity) 
			ItemStatic.runEventOfLivingEntity((LivingEntity) e.getEntity(), e);
	}

	@EventHandler
	public void edecc(PlayerInteractEvent e) {
		ItemStatic.runEventOfLivingEntity((LivingEntity) e.getPlayer(), e);
	}


}

