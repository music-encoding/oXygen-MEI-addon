package org.music_encoding.workspace.extension;

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

/**
 * A oXygen workspace access extension that adds a custom view.
 * This view can render the current MEI file as verovio SVG.
 *
 * @author Benjamin W. Bohl
 */
public class MeiOxygenPluginWorkspaceExtension implements WorkspaceAccessPluginExtension {

    private static final long serialVersionUID = 1L;

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
    private ImageViewerPanel verovioViewPanel = new ImageViewerPanel();

    /**
     * Controller for keeping the verovio image viewer in sync with the current MEI file.
     */
    private VerovioImageViewerController verovioImageViewerController = new VerovioImageViewerController(verovioViewPanel);

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
        if (viewInfo.getViewID().equals(org.music_encoding.workspace.view.MeiVerovioView.VIEW_ID)) {
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

    pluginWorkspaceAccess.addToolbarComponentCustomizer(new ToolbarComponentsCustomizer() {
      @Override
      public void customizeToolbarComponents(ToolbarInfo toolbarInfo) {
        if (toolbarInfo.getId().equals(TOOLBAR_ID)) {
          // Add the custom view to the toolbar
          Action openCurrentAction = new AbstractAction("MEI verovio View", e -> {
            private static final long serialVersionUID = 2L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
              // Show the custom view when the action is triggered
              //pluginWorkspaceAccess.getViewManager().showView(org.music_encoding.workspace.view.MeiVerovioView.VIEW_ID);
              PluginDescriptor descriptor = MeiOxygenPlugin.getInstance.getDescriptor();
              // Get the current file from the plugin workspace
              String currentFilePath = pluginWorkspace.getCurrentEditorLocation();
              File file = ro.sync.util.editorvars.CURRENT_FILE;

              try {
                boolean open = pluginWorkspaceAccess.open(ro.sync.util.editorvars.CURRENT_FILE);
                if (open) {
                    pluginWorkspaceAccess.getViewManager().showView(org.music_encoding.workspace.view.MeiVerovioView.VIEW_ID, false);
                    URL fileURL = new URL("file", null, currentFilePath);
                  pluginWorkspaceAccess.getViewManager().showView(org.music_encoding.workspace.view.MeiVerovioView.VIEW_ID, fileURL);
                }

              } catch (MalformedURLException e1) {
                pluginWorkspaceAccess.showErrorMessage("Invalid file URL: " + e1.getMessage(), e1);
              } catch (IOException e) {
                e.printStackTrace();
              } catch (XPathException e) {
                e.printStackTrace();
              }
            }
          };
          //viewAction.setIcon(URLUtil.getResourceURL(PluginDescriptor.class, "mei-verovio-icon.png"));
          //viewAction.setToolTipText("Open the MEI verovio view to render the current MEI file.");
          //toolbarInfo.addAction(viewAction);
          ToolbarButton verovioButton = new ToolbarButton(openSampleAction, true);
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
    registerView(new org.music_encoding.workspace.view.MeiverovioView());
  } */

  @Override
  public boolean applicationClosing() {
    return true;
  }
}