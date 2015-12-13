package amidst.gui.main.worldsurroundings.widget;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;

import amidst.documentation.AmidstThread;
import amidst.documentation.CalledOnlyBy;
import amidst.documentation.NotThreadSafe;
import amidst.fragment.Fragment;
import amidst.fragment.FragmentGraph;
import amidst.gui.main.worldsurroundings.FragmentGraphToScreenTranslator;
import amidst.mojangapi.world.Biome;
import amidst.mojangapi.world.CoordinatesInWorld;
import amidst.mojangapi.world.Resolution;

@NotThreadSafe
public class CursorInformationWidget extends Widget {
	private static final String UNKNOWN_BIOME_NAME = "Unknown";

	private final FragmentGraph graph;
	private final FragmentGraphToScreenTranslator translator;

	private String text = "";

	@CalledOnlyBy(AmidstThread.EDT)
	public CursorInformationWidget(CornerAnchorPoint anchor,
			FragmentGraph graph, FragmentGraphToScreenTranslator translator) {
		super(anchor);
		this.graph = graph;
		this.translator = translator;
		setWidth(20);
		setHeight(30);
		forceVisibility(false);
	}

	@CalledOnlyBy(AmidstThread.EDT)
	@Override
	protected void doUpdate(FontMetrics fontMetrics, float time) {
		String newText = getText();
		if (newText != null) {
			text = newText;
		}
		setWidth(fontMetrics.stringWidth(text) + 20);
	}

	@CalledOnlyBy(AmidstThread.EDT)
	private String getText() {
		Point mousePosition = getMousePosition();
		if (mousePosition != null) {
			CoordinatesInWorld coordinates = translator
					.screenToWorld(mousePosition);
			String biomeName = getBiomeNameAt(coordinates);
			return biomeName + " " + coordinates.toString();
		} else {
			return null;
		}
	}

	@CalledOnlyBy(AmidstThread.EDT)
	private String getBiomeNameAt(CoordinatesInWorld coordinates) {
		Fragment fragment = graph.getFragmentAt(coordinates);
		if (fragment != null && fragment.isLoaded()) {
			long x = coordinates.getXRelativeToFragmentAs(Resolution.QUARTER);
			long y = coordinates.getYRelativeToFragmentAs(Resolution.QUARTER);
			short biome = fragment.getBiomeDataAt((int) x, (int) y);
			return Biome.getByIndex(biome).getName();
		} else {
			return UNKNOWN_BIOME_NAME;
		}
	}

	@CalledOnlyBy(AmidstThread.EDT)
	@Override
	protected void doDraw(Graphics2D g2d) {
		g2d.drawString(text, getX() + 10, getY() + 20);
	}

	@CalledOnlyBy(AmidstThread.EDT)
	@Override
	protected boolean onVisibilityCheck() {
		return getMousePosition() != null;
	}
}
