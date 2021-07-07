package config;

import java.io.IOException;

import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import org.lwjgl.util.vector.Vector4f;

public class ShaderConfig {

	private Ini config;

	public ShaderConfig() {
		config = createConfig();
	}

	private Ini createConfig() {
		Ini ini = new Ini();
		try {
			ini = new Ini(Class.class.getResourceAsStream("/config.ini"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ini;
	}

	public Vector4f getColour() {
		Section section = this.config.get("ColourSwitch");
		if (section != null) {
			if (section.containsKey("RGBA")) {
				String rgba = section.get("RGBA").trim().toLowerCase();
				if (rgba.length() == 4) {
					if (rgba.matches("^[rgba]+$")) {
						String output = rgba.replace('r', '0').replace('g', '1').replace('b', '2').replace('a', '3');
						Vector4f rgbaVec = new Vector4f(Float.parseFloat(String.valueOf(output.charAt(0))),
								Float.parseFloat(String.valueOf(output.charAt(1))),
								Float.parseFloat(String.valueOf(output.charAt(2))),
								Float.parseFloat(String.valueOf(output.charAt(3))));
						return rgbaVec;
					}
				}
			}
		}
		return new Vector4f(0.0f, 1.0f, 2.0f, 3.0f);
	}

	public float getContrast() {
		Section section = this.config.get("ColourSwitch");
		if (section != null) {
			if (section.containsKey("Contrast")) {
				String contrast = section.get("Contrast").trim().toLowerCase();
				try {
					return Float.parseFloat(contrast);
				} catch (NumberFormatException e) {

				}
			}
		}
		return 0.0f;
	}

	public int getBlurFactor() {
		Section section = this.config.get("Blur");
		if (section != null) {
			if (section.containsKey("BlurFactor")) {
				String blurFactor = section.get("BlurFactor").trim().toLowerCase();
				try {
					return Integer.parseInt(blurFactor);
				} catch (NumberFormatException e) {

				}
			}
		}
		return 1;
	}
	
	public boolean shouldUseBloom() {
		Section section = this.config.get("Bloom");
		if(section != null) {
			if(section.containsKey("UseBloom")) {
				String bloom = section.get("UseBloom").trim().toLowerCase();
				try {
					return Boolean.parseBoolean(bloom);
				} catch(NumberFormatException e) {
					
				}
			}
		}
		return false;
	}
}
