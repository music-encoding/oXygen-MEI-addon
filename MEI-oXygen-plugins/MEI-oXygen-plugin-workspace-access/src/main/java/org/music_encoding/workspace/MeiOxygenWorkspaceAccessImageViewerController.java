package org.music_encoding.workspace;

// import the correct package for MeiOxygenWorkspaceAccessSvgViewerPanel
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
import ro.sync.exml.workspace.api.editor.WSEditor;

import java.io.File;
import java.io.IOException;

/**
 * Controller for keeping the MeiOxygenWorkspaceAccess viewer in sync with the current MEI file.
 */
public class MeiOxygenWorkspaceAccessImageViewerController {
    private final MeiOxygenWorkspaceAccessSvgViewerPanel MeiOxygenWorkspaceAccessSvgViewerPanel;
    private StandalonePluginWorkspace pluginWorkspaceAccess;

    public MeiOxygenWorkspaceAccessImageViewerController(MeiOxygenWorkspaceAccessSvgViewerPanel MeiOxygenWorkspaceAccessSvgViewerPanel) {
        this.MeiOxygenWorkspaceAccessSvgViewerPanel = MeiOxygenWorkspaceAccessSvgViewerPanel;
    }

    public void init(StandalonePluginWorkspace pluginWorkspaceAccess) {
        this.pluginWorkspaceAccess = pluginWorkspaceAccess;
    }

    /**
     * Render the current MEI file as MeiOxygenWorkspaceAccess and display it.
     * This is a stub: replace with your actual MEI-to-MeiOxygenWorkspaceAccess rendering logic.
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
                        // For demonstration, assume MeiOxygenWorkspaceAccess is at the same path with .MeiOxygenWorkspaceAccess extension
                        File file = new File(meiFile.getParentFile(), meiFile.getName().replaceAll("\\.mei$", ".MeiOxygenWorkspaceAccess"));
                        // Load MeiOxygenWorkspaceAccess into the viewer panel
                        //MeiOxygenWorkspaceAccessSvgViewerPanel.loadFile(file);
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