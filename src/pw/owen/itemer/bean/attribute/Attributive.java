package pw.owen.itemer.bean.attribute;

import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_ARRAY)
@JsonSubTypes({ @JsonSubTypes.Type(value = Attack.class, name = "Attack"),
		@JsonSubTypes.Type(value = Crit.class, name = "Crit")
})
public interface Attributive {

	public void saveYml(YamlConfiguration yml) throws Exception;

	public void loadYml(YamlConfiguration yml) throws Exception;
	public void runEvent(Event event, LivingEntity e);

	public Map<String, String> getParameters();

	public boolean set(String par, String value) throws Exception;

	public Object get(String par) throws Exception;

	public Map<String, String> getStaticParameters();

	public boolean setStatic(String par, String value) throws Exception;

	public Object getStatic(String par) throws Exception;

	public String getInfo();

	public int getPriority();

	public String getName();
}
