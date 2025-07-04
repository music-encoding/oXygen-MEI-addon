package org.music_encoding.oxygen.plugin;

import org.music_encoding.oxygen.plugin.SvgViewerPanel;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
import ro.sync.exml.workspace.api.editor.WSEditor;

import java.io.File;
import java.io.IOException;

/**
 * Controller for keeping the SVG viewer in sync with the current MEI file.
 */
public class ImageController {
    private final SvgViewerPanel svgViewerPanel;
    private StandalonePluginWorkspace pluginWorkspaceAccess;

    public ImageController(SvgViewerPanel svgViewerPanel) {
        this.svgViewerPanel = svgViewerPanel;
    }

    public void init(StandalonePluginWorkspace pluginWorkspaceAccess) {
        this.pluginWorkspaceAccess = pluginWorkspaceAccess;
    }

    /**
     * Render the current MEI file as SVG and display it.
     * This is a stub: replace with your actual MEI-to-SVG rendering logic.
     */
    public void renderCurrentFile() throws IOException {
        if (pluginWorkspaceAccess == null) return;

        WSEditor editor = pluginWorkspaceAccess.getCurrentEditorAccess(StandalonePluginWorkspace.MAIN_EDITING_AREA);
        if (editor != null) {
            String currentFilePath = editor.getEditorLocation().toString();
            // test if the current file is a MEI file
            // by checking the xml namespace
            if (currentFilePath != null) {
                // Read the file and check for MEI XML namespace
                try (java.io.FileInputStream fis = new java.io.FileInputStream(currentFilePath)) {
                    javax.xml.parsers.DocumentBuilderFactory dbFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
                    dbFactory.setNamespaceAware(true);
                    javax.xml.parsers.DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    org.w3c.dom.Document doc = dBuilder.parse(fis);
                    String rootNamespace = doc.getDocumentElement().getNamespaceURI();
                    if ("http://www.music-encoding.org/ns/mei".equals(rootNamespace)) {
                        File meiFile = new File(currentFilePath);
                        // TODO: Replace this with actual Verovio rendering logic
                        // For demonstration, assume SVG is at the same path with .svg extension
                        File svgFile = new File(meiFile.getParentFile(), meiFile.getName().replaceAll("\\.mei$", ".svg"));
                        // Load SVG into the viewer panel
                        svgViewerPanel.loadSvg(svgFile);
                    } else {
                        throw new IOException("The current file is not a valid MEI file.");
                    }
            } catch (Exception e) {
                throw new IOException("Error rendering MEI file: " + e.getMessage(), e);
            }
            } else {
                throw new IOException("No current file to render.");
        }
        } else {
            throw new IOException("No editor is currently open.");
        }
    }
}