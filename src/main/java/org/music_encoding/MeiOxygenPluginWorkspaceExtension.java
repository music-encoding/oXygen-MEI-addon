package org.music_encoding.oxygen.plugin;

import org.music_encoding.oxygen.plugin.MeiOxygenPlugin;
import org.music_encoding.oxygen.plugin.SvgViewerPanel;
import org.music_encoding.oxygen.plugin.ImageController;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import ro.sync.exml.plugin.PluginDescriptor;
import ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension;
import ro.sync.exml.workspace.api.editor.page.text.xml.XPathException;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
import ro.sync.exml.workspace.api.standalone.ToolbarComponentsCustomizer;
import ro.sync.exml.workspace.api.standalone.ToolbarInfo;
import ro.sync.exml.workspace.api.standalone.ViewComponentCustomizer;
import ro.sync.exml.workspace.api.standalone.ViewInfo;
import ro.sync.exml.workspace.api.standalone.ui.ToolbarButton;
import ro.sync.util.URLUtil;
import ro.sync.exml.workspace.api.editor.WSEditor;
/**
 * A oXygen workspace access extension that adds a custom view.
 * This view can render the current MEI file as verovio SVG.
 *
 * @author Benjamin W. Bohl
 */
public class MeiOxygenPluginWorkspaceExtension implements WorkspaceAccessPluginExtension {

    /**
     * A toolbar that adds a button to render the current file with verovio.
     * This button will be visible when the current file is an MEI file.
     * The button will trigger the rendering of the MEI file as verovio SVG.
     * The rendered SVG will be displayed in the custom view.
     * The button will be disabled if the current file is not an MEI file.
     * The button will be enabled if the current file is an MEI file.
     * The button will be added to the toolbar with the ID "mei.oxygen.toolbar".
     * The button will have the label "Render with verovio".
     * The button will have the icon "mei-verovio-icon.png" located in the plugin's resources.
     * The button will have a tooltip "Render the current MEI file with verovio."
     * The button will be registered in the oXygen plugin workspace.
     * This toolbar is registered in the oXygen plugin workspace.
     * The toolbar can be customized to add the button to the desired location.
     * The toolbar can be used to render the current MEI file as verovio SVG.
     * The toolbar can be used to display the rendered SVG in the custom view.
     */
    private static final String TOOLBAR_ID = "mei.oxygen.toolbar";

    /**
     * The panel that will be used to display the verovio SVG.
     * This panel will be registered in the oXygen plugin workspace.
     * The panel will be used to display the rendered SVG of the current MEI file.
     * The panel will be used to display the SVG in the custom view.
     * The panel will be used to render the current MEI file as verovio SVG.
     * The panel will be used to display the rendered SVG in the custom view.
     * The panel will be used to display the SVG in the custom view.
     */
    private SvgViewerPanel verovioViewPanel = new SvgViewerPanel();

    /**
     * Controller for keeping the verovio image viewer in sync with the current MEI file.
     */
    private ImageController verovioImageViewerController = new ImageController(verovioViewPanel);

  /**
   * @see ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension#applicationStarted(ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace)
   */
  @Override
  public void applicationStarted(final StandalonePluginWorkspace pluginWorkspaceAccess) {
    verovioImageViewerController.init(pluginWorkspaceAccess);
    // add custom view to the workspace
    pluginWorkspaceAccess.addViewComponentCustomizer(new ViewComponentCustomizer() {
      /**
       * @see ro.sync.exml.workspace.api.standalone.ViewComponentCustomizer#customizeView(ro.sync.exml.workspace.api.standalone.ViewInfo)
       */
      @Override
      public void customizeView(ViewInfo viewInfo) {
        if (viewInfo.getViewID().equals(SvgViewerPanel.IMAGE_VIEWER_ID)) {
          // Set the custom view panel as the content of the view
          JPanel jPanel = new JPanel(new BorderLayout());

          JPanel northPanel = new JPanel(new GridBagLayout());

          /* JButton jButton = new JButton(new AbstractAction("Render with verovio");
          jButton.setToolTipText("Render the current MEI file with verovio.");
          jButton.setIcon(new ImageIcon(URLUtil.getResourceURL(PluginDescriptor.class, "mei-verovio-icon.png")));
          jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              try {
                verovioImageViewerController.renderCurrentFile();
              } catch (IOException | XPathException ex) {
                pluginWorkspaceAccess.showErrorMessage("Error rendering MEI file: " + ex.getMessage(), ex);
              }
            }
          });
          northPanel.add(jButton); */

            jPanel.add(northPanel, BorderLayout.NORTH);
            jPanel.add(verovioViewPanel, BorderLayout.CENTER);
            viewInfo.setComponent(jPanel);
        }
      }
    });

    pluginWorkspaceAccess.addToolbarComponentsCustomizer(new ToolbarComponentsCustomizer() {

      @Override
      public void customizeToolbar(ToolbarInfo toolbarInfo) {
        if (toolbarInfo.getToolbarID().equals(TOOLBAR_ID)) {
          Action openCurrentAction = new AbstractAction() {
            private static final long serialVersionUID = -3361572441178434523L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
              //PluginDescriptor descriptor = MeiOxygenPlugin.getInstance().getDescriptor();

              WSEditor editorAccess = pluginWorkspaceAccess.getCurrentEditorAccess(StandalonePluginWorkspace.MAIN_EDITING_AREA);
              if (editorAccess != null) {
                // Get the current file path from the editor access
                String currentFilePath = editorAccess.getEditorLocation() != null ? editorAccess.getEditorLocation().toString() : null;

                if (currentFilePath != null) {

                  // Render the current MEI file as verovio SVG
                  //verovioImageViewerController.renderCurrentFile(currentFilePath);
                  // echo currentFilePath in dialog
                  pluginWorkspaceAccess.showInformationMessage("Current MEI file path: " + currentFilePath);

                }
              }
            }
          };
          ToolbarButton verovioButton = new ToolbarButton(openCurrentAction, true);
          verovioButton.setText("MEI Verovio View");
          toolbarInfo.setComponents(new JComponent[] {verovioButton});
        }
      }
    });
  }

  /**
   * Constructor.
   */
  /* public MeiOxygenPluginWorkspaceExtension() {
    super();
    // Register the custom view
    registerView(new org.music_encoding.oxygen.plugin.view.MeiverovioView());
  } */

  @Override
  public boolean applicationClosing() {
    return true;
  }
}