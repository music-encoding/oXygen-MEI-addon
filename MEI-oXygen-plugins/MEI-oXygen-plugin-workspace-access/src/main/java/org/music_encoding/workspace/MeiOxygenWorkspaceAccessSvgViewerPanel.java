package org.music_encoding.workspace;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JPanel;
import org.apache.batik.swing.JSVGCanvas;

/**
 * A simple MeiOxygenWorkspaceAccess viewer panel for use in oXygen XML plugins.
 */
public class MeiOxygenWorkspaceAccessSvgViewerPanel extends JPanel {
    private final JSVGCanvas MeiOxygenWorkspaceAccessCanvas;

    public static final String IMAGE_VIEWER_ID = "mei.verovio.MeiOxygenWorkspaceAccess.viewer";

    public MeiOxygenWorkspaceAccessSvgViewerPanel() {
        super(new BorderLayout());
        MeiOxygenWorkspaceAccessCanvas = new JSVGCanvas();
        this.add(MeiOxygenWorkspaceAccessCanvas, BorderLayout.CENTER);
    }

    /**
     * Loads and displays the given SVG file.
     * @param svgFile The SVG file to display.
     */
    public void loadSvg(File svgFile) {
        if (svgFile != null && svgFile.exists()) {
            MeiOxygenWorkspaceAccessCanvas.setURI(svgFile.toURI().toString());
        }
    }


    /**
     * Clears the current MeiOxygenWorkspaceAccess display.
     */
    public void clear() {
        MeiOxygenWorkspaceAccessCanvas.setURI(null);
    }

    /**
     * Returns the underlying JSVGCanvas for further customization if needed.
     * @return The JSVGCanvas instance.
     */
    public JSVGCanvas getMeiOxygenWorkspaceAccessCanvas() {
        return MeiOxygenWorkspaceAccessCanvas;
    }
}