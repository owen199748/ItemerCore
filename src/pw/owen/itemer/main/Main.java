package pw.owen.itemer.main;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import pw.owen.itemer.bean.ItemStatic;
import pw.owen.itemer.utils.RangeInt;

public class Main extends JavaPlugin{
	private static Main main;
	public static final java.text.DecimalFormat FORMAT = new java.text.DecimalFormat(
			"#.##");

	public static Main getMain() {
		return main;
	}

@Override
public void onEnable() {
		main = this;
		getServer().getPluginManager().registerEvents(new AutoListener(), this);
}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("本命令只能由玩家执行");
			return true;
		} else {
			if (cmd.getName().equalsIgnoreCase("itemer")) {
				if (args.length == 0)
					return false;
				Player p = (Player) sender;
				// 不是操作类命令写在这里

				if (p.getItemInHand() == null
						|| p.getItemInHand().getType() == Material.AIR) {

				}
				// 操作类命令写在这里
				ItemStack ph = p.getItemInHand();
				ItemStatic s = ItemStatic.getItemStatic(ph);
				if (s == null)
					s = ItemStatic.getNewItemStatic();
				if (args[0].equalsIgnoreCase("damage")) {
					if (args.length != 2)
						return false;
					s.setDamage(new RangeInt(Integer.parseInt(args[1])));
				} else
					return false;

				p.setItemInHand(s.putItemStatic(ph));

			}

		}

		return false;
	}
}

