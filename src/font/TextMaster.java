package font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import font.fontMeshCreator.FontType;
import font.fontMeshCreator.GUIText;
import font.fontMeshCreator.TextMeshData;
import font.fontRendering.FontRenderer;
import renderEngine.ModelLoader;

public class TextMaster {

	private static ModelLoader loader;
	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static FontRenderer renderer;
	
	public static void init(ModelLoader loaderIn) {
		renderer = new FontRenderer();
		loader = loaderIn;
	}
	
	public static void render() {
		renderer.render(texts);
	}
	
	public static void loadText(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		if(textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public static void removeText(GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if(textBatch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}
	
	public static void cleanUp() {
		renderer.cleanUp();
	}
}
