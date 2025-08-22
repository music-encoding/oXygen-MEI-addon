package org.music_encoding.workspace;

/* import the verovio toolkit */
import org.rismch.verovio.toolkit;
import org.rismch.verovio.NativeUtils;

import org.apache.commons.io.FilenameUtils;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorDocumentController;
import ro.sync.ecss.extensions.api.node.AuthorDocumentFragment;
import ro.sync.ecss.extensions.api.structure.AuthorPopupMenuCustomizer;
import ro.sync.exml.editor.EditorPageConstants;
import ro.sync.exml.plugin.PluginDescriptor;

import ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension;
import ro.sync.exml.workspace.api.Platform;
import ro.sync.exml.workspace.api.editor.WSEditor;
import ro.sync.exml.workspace.api.editor.page.author.WSAuthorEditorPage;
import ro.sync.exml.workspace.api.editor.page.text.WSTextEditorPage;
import ro.sync.exml.workspace.api.listeners.WSEditorChangeListener;
import ro.sync.exml.workspace.api.standalone.InputURLChooser;
import ro.sync.exml.workspace.api.standalone.InputURLChooserCustomizer;
import ro.sync.exml.workspace.api.standalone.MenuBarCustomizer;
import ro.sync.exml.workspace.api.editor.page.text.xml.XPathException;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
import ro.sync.exml.workspace.api.standalone.ToolbarComponentsCustomizer;
import ro.sync.exml.workspace.api.standalone.ToolbarInfo;
import ro.sync.exml.workspace.api.standalone.ViewComponentCustomizer;
import ro.sync.exml.workspace.api.standalone.ViewInfo;
import ro.sync.exml.workspace.api.standalone.ditamap.TopicRefInfo;
import ro.sync.exml.workspace.api.standalone.ditamap.TopicRefTargetInfo;
import ro.sync.exml.workspace.api.standalone.ditamap.TopicRefTargetInfoProvider;
import ro.sync.exml.workspace.api.standalone.ui.Menu;
import ro.sync.exml.workspace.api.standalone.ui.ToolbarButton;
import ro.sync.util.URLUtil;
import ro.sync.exml.workspace.api.util.RelativeReferenceResolver;
import ro.sync.ui.Icons;

/**
 * A oXygen workspace access extension that adds a custom view. This view can
 * render the current MEI file as verovio MeiOxygenWorkspaceAccess.
 *
 * @author Benjamin W. Bohl
 */
public class MeiOxygenWorkspaceAccessPluginExtension implements WorkspaceAccessPluginExtension {
	/**
	 * The ID of the plugin.
	 */
	public static final String PLUGIN_ID = "org.music_encoding.workspace.MeiOxygenWorkspaceAccessPluginExtension";

	/**
	 * Called when the application is closing.
	 */
	@Override
	public boolean applicationClosing() {
		// Perform any necessary cleanup here if needed
		return true;
	}

	/**
	 * A toolbar that adds a button to render the current file with verovio. This
	 * button will be visible when the current file is an MEI file. The button will
	 * trigger the rendering of the MEI file as verovio MeiOxygenWorkspaceAccess.
	 * The rendered MeiOxygenWorkspaceAccess will be displayed in the custom view.
	 * The button will be disabled if the current file is not an MEI file. The
	 * button will be enabled if the current file is an MEI file. The button will be
	 * added to the toolbar with the ID "mei.oxygen.toolbar". The button will have
	 * the label "Render with verovio". The button will have the icon
	 * "mei-verovio-icon.png" located in the plugin's resources. The button will
	 * have a tooltip "Render the current MEI file with verovio." The button will be
	 * registered in the oXygen plugin workspace. This toolbar is registered in the
	 * oXygen plugin workspace. The toolbar can be customized to add the button to
	 * the desired location. The toolbar can be used to render the current MEI file
	 * as verovio MeiOxygenWorkspaceAccess. The toolbar can be used to display the
	 * rendered MeiOxygenWorkspaceAccess in the custom view.
	 */
	private static final String TOOLBAR_ID = "mei.oxygen.toolbar";

	/**
	 * The panel that will be used to display the verovio MeiOxygenWorkspaceAccess.
	 * This panel will be registered in the oXygen plugin workspace. The panel will
	 * be used to display the rendered MeiOxygenWorkspaceAccess of the current MEI
	 * file. The panel will be used to display the MeiOxygenWorkspaceAccess in the
	 * custom view. The panel will be used to render the current MEI file as verovio
	 * MeiOxygenWorkspaceAccess. The panel will be used to display the rendered
	 * MeiOxygenWorkspaceAccess in the custom view. The panel will be used to
	 * display the MeiOxygenWorkspaceAccess in the custom view.
	 */
	private MeiOxygenWorkspaceAccessSvgViewerPanel verovioViewPanel = new MeiOxygenWorkspaceAccessSvgViewerPanel();

	/**
	 * Controller for keeping the verovio image viewer in sync with the current MEI
	 * file.
	 */
	private MeiOxygenWorkspaceAccessImageViewerController MeiOxygenWorkspaceAccessImageViewerController = new MeiOxygenWorkspaceAccessImageViewerController(
			verovioViewPanel);

	/**
	 * Plugin workspace access.
	 */
	private StandalonePluginWorkspace pluginWorkspaceAccess;

//  /**
//   * The Oxygen menu bar. You can set this field on startup
//   * and then provide custom actions or filters when a certain file is opened.
//   */
//  private JMenuBar oxygenMenuBar;

	/**
	 * @see ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension#applicationStarted(ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace)
	 */
	@Override
	public void applicationStarted(final StandalonePluginWorkspace pluginWorkspaceAccess) {

		// Prevent 'UnsupportedOprationException'
		if (!pluginWorkspaceAccess.getPlatform().equals(Platform.WEBAPP)) {

			this.pluginWorkspaceAccess = pluginWorkspaceAccess;

			pluginWorkspaceAccess.addMenuBarCustomizer(new MenuBarCustomizer() {
				// Sample of using method getOxygenActionID() from StandalonePluginWorkspace
				// Instead of reverting the file , save it under other name
				// private Action revertAction;
				// private Action saveAsAction = null;

				/**
				 * @see ro.sync.exml.workspace.api.standalone.MenuBarCustomizer#customizeMainMenu(javax.swing.JMenuBar)
				 */
				@Override
				public void customizeMainMenu(JMenuBar mainMenuBar) {
					// oxygenMenuBar = mainMenuBar;
					// CMS menu
					JMenu menuMEI = createMEIMenu(renderAction);
					// Add the CMS menu before the Help menu
					mainMenuBar.add(menuMEI, mainMenuBar.getMenuCount() - 1);

					// Sample of using method getOxygenActionID() from StandalonePluginWorkspace
					// Instead of reverting the file , save it under other name

					// revertAction = null;
					// String fileRevertID = "File/Revert";
					// String saveAsID = "File/File_Save_As";
					// String actionName = null;
					//
					// int menuCount = mainMenuBar.getMenuCount();
					// // Iterate over menus to find the revert and Save As actions
					// menuLabel :for (int i = 0; i < menuCount; i++) {
					// // Revert action index in menu
					// int revertActionIndex = 0;
					// JMenu menu = mainMenuBar.getMenu(i);
					// int itemCount = menu.getItemCount();
					// for (int j = 0; j < itemCount; j++) {
					// JMenuItem item = menu.getItem(j);
					// if (item != null) {
					// Action action = item.getAction();
					// String oxygenActionID = pluginWorkspaceAccess.getOxygenActionID(action);
					// if (fileRevertID.equals(oxygenActionID)) {
					// revertAction = action;
					// revertActionIndex = j;
					// actionName = (String) revertAction.getValue(Action.NAME);
					// }
					// if (saveAsID.equals(oxygenActionID)) {
					// saveAsAction = action;
					// }
					//
					// if (revertAction != null && saveAsAction != null) {
					// JMenuItem revertMenuItem = menu.getItem(revertActionIndex);
					// // Replace Revert action with Save As action
					// revertMenuItem.setAction(new AbstractAction(actionName) {
					//
					// public void actionPerformed(ActionEvent e) {
					// saveAsAction.actionPerformed(e);
					// }
					// });
					// break menuLabel;
					// }
					// }
					// }
					// }
				}
			});

			pluginWorkspaceAccess.addToolbarComponentsCustomizer(new ToolbarComponentsCustomizer() {
				/**
				 * @see ro.sync.exml.workspace.api.standalone.ToolbarComponentsCustomizer#customizeToolbar(ro.sync.exml.workspace.api.standalone.ToolbarInfo)
				 */
				@SuppressWarnings("serial")
				@Override
				public void customizeToolbar(ToolbarInfo toolbarInfo) {
					// The toolbar ID is defined in the "plugin.xml"
					if ("SampleWorkspaceAccessToolbarID".equals(toolbarInfo.getToolbarID())) {

						List<JComponent> comps = new ArrayList<JComponent>();
						JComponent[] initialComponents = toolbarInfo.getComponents();
						boolean hasInitialComponents = initialComponents != null && initialComponents.length > 0;
						if (hasInitialComponents) {
							// Add initial toolbar components
							for (JComponent toolbarItem : initialComponents) {
								comps.add(toolbarItem);
							}
						}

						// render with verovio button
						ToolbarButton verovioButton = new ToolbarButton(renderAction, true);
						verovioButton.setText("MEI Verovio View");

						// Add to toolbar
						comps.add(verovioButton);
						toolbarInfo.setComponents(comps.toArray(new JComponent[0]));

						// Set title
						String initialTitle = toolbarInfo.getTitle();
						String title = "";
						if (hasInitialComponents && initialTitle != null && initialTitle.trim().length() > 0) {
							// Include initial tile
							title += initialTitle + " | ";
						}
						title += "MEI";
						toolbarInfo.setTitle(title);
					} else if ("Author_custom_actions1".equals(toolbarInfo.getToolbarID())) {
						// Contribute a new action directly in the Author toolbar which was dynamically
						// created from the document type
						// associated to the XML file. You can add a new action or remove an existing
						// one.
						// See the Javadoc for:
						// ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace.addToolbarComponentsCustomizer(ToolbarComponentsCustomizer)
						List<JComponent> comps = new ArrayList<JComponent>(Arrays.asList(toolbarInfo.getComponents()));
						comps.add(new ToolbarButton(new AbstractAction("MY ACTION") {
							@Override
							public void actionPerformed(ActionEvent e) {
								// You can obtain the current editor, get access to its WSAuthorPage and modify
								// it using the API.
								System.err.println("Perform action on " + pluginWorkspaceAccess
										.getCurrentEditorAccess(StandalonePluginWorkspace.MAIN_EDITING_AREA)
										.getEditorLocation());
							}
						}, true));
						toolbarInfo.setComponents(comps.toArray(new JComponent[0]));
					}
				}
			});

			pluginWorkspaceAccess.addViewComponentCustomizer(new ViewComponentCustomizer() {
				/**
				 * @see ro.sync.exml.workspace.api.standalone.ViewComponentCustomizer#customizeView(ro.sync.exml.workspace.api.standalone.ViewInfo)
				 */
				@Override
				public void customizeView(ViewInfo viewInfo) {
					if (
					// The view ID defined in the "plugin.xml"
					"SampleWorkspaceAccessID".equals(viewInfo.getViewID())) {
						// Set the custom view panel as the content of the view
						JPanel jPanel = new JPanel(new BorderLayout());

						JPanel northPanel = new JPanel(new GridBagLayout());

						viewInfo.setTitle("Verovio SVG Viewers");
						// viewInfo.setIcon(Icons.getIcon(Icons.CMS_MESSAGES_CUSTOM_VIEW_STRING));
						jPanel.add(northPanel, BorderLayout.NORTH);
						jPanel.add(verovioViewPanel, BorderLayout.CENTER);
						viewInfo.setComponent(jPanel);
					}
				}
			});
		}
	}

	/**
	 * Create MEI menu that contains the following actions: <code>render</code>
	 *
	 * @param renderAction The render action.
	 *
	 * @return The MEI menu.
	 */
	private JMenu createMEIMenu(final Action renderAction) {
		// MEI menu
		Menu menuMEI = new Menu("MEI", true);

		// Add verovio In action on the menu
		final JMenuItem renderItem = new JMenuItem(renderAction);
		renderItem.setText("render");
		menuMEI.add(renderItem);

		return menuMEI;
	}

	/**
	 * The action to render the current MEI file as SVG using Verovio.
	 * Fetches the current file URL from the active editor in oXygen
	 */
	private final Action renderAction = new AbstractAction() {
		private static final long serialVersionUID = -3361572441178434523L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			// get active editor from workspace access
			WSEditor editorAccess = pluginWorkspaceAccess
					.getCurrentEditorAccess(StandalonePluginWorkspace.MAIN_EDITING_AREA);
			
			// if editorAccess is available do the logic
			if (editorAccess != null) {
				
				// get the current file URL from the editor access
				URL currentFileURL = editorAccess.getEditorLocation() != null
						? editorAccess.getEditorLocation()
						: null;
				

				if (currentFileURL != null) {

					// echo the retrieved URL in a message box
					pluginWorkspaceAccess
							.showInformationMessage("Trying to load MEI into Verovio from: " + currentFileURL.toString());
					
					// Read the file and check for MEI XML namespace
					//File meiFile = new File(currentFileURL);
					
					// Load the MEI file to string
					String meiData = "";
					try {
						meiData = new String(Files.readAllBytes(Paths.get(currentFileURL.toURI())));
					}
					catch (IOException e ) {
						pluginWorkspaceAccess.showErrorMessage("Error reading MEI file: " + e.getMessage());
					}
					catch (Exception e) {
						pluginWorkspaceAccess.showErrorMessage("Error: " + e.getMessage());
					}
					// try (java.io.FileInputStream fis = new
					// java.io.FileInputStream(currentFilePath)) {
					// javax.xml.parsers.DocumentBuilderFactory dbFactory =
					// javax.xml.parsers.DocumentBuilderFactory.newInstance();
					// dbFactory.setNamespaceAware(true);
					// javax.xml.parsers.DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					// org.w3c.dom.Document doc = dBuilder.parse(fis);
					// String rootNamespace = doc.getDocumentElement().getNamespaceURI();
					// if ("http://www.music-encoding.org/ns/mei".equals(rootNamespace)) {
					// The current file is in the MEI namespace
					// send current file path to verovio toolkit
					//try {

						// Initialize the Verovio toolkit
						//toolkit verovioToolkit = new toolkit(false);

						// create options for the Verovio toolkit
						//String options = "{'adjustPageHeight': true, 'breaks': 'auto', 'scale': 50}";

						/* set some options */
						//verovioToolkit.setOptions(options);

						

						//pluginWorkspaceAccess.showInformationMessage("MEI: " + meiData);

						//verovioToolkit.loadData(meiData);

						// Render the MEI file as SVG
						//String svg = verovioToolkit.renderToSVG();
						
						// Write the SVG to a file in the original file's directory with the original
						// file name
						
						// but with a .svg extension
						
						
						// save basename to variable
						String baseName = FilenameUtils.getBaseName(currentFileURL.getPath());
						String filePath = FilenameUtils.getPath(currentFileURL.getPath());
						String svgPath = filePath + baseName + ".svg";
						
						pluginWorkspaceAccess.showInformationMessage("SVG will be written to: " + svgPath);
						
						//int dotIndex = baseName.lastIndexOf('.');
						//if (dotIndex > 0) {
						//	baseName = baseName.substring(0, dotIndex);
						//}
						
						// create temporary SVG file
						//BufferedWriter svgWriter = new BufferedWriter(new FileWriter);
						
						try {
							Files.write(Paths.get(svgPath), meiData.getBytes());
						} catch (IOException e) {
							pluginWorkspaceAccess.showErrorMessage("Error writing SVG file: " + e.getMessage());
						}
						
						// try (FileOutputStream fos = new FileOutputStream(tempSvgFile)) {
						// fos.write(svg.getBytes());
						// Notify the controller to update the view with the rendered SVG
						//MeiOxygenWorkspaceAccessImageViewerController.loadFile(tempSvgFile.getAbsolutePath());
						// }

					//} catch (FileNotFoundException e) {
					//	pluginWorkspaceAccess.showErrorMessage("MEI file not found: " + e.getMessage());
					//} catch (IOException e) {
					//	pluginWorkspaceAccess.showErrorMessage("Error reading MEI file: " + e.getMessage());
					//} catch (Exception e) {
					//	pluginWorkspaceAccess.showErrorMessage("Error: " + e.getMessage());
					//}

					// } else {
					// throw new IOException("The current file is not in the MEI namespace.");
					// }
					// } catch (Exception e) {
					// pluginWorkspaceAccess.showErrorMessage("Error rendering MEI file: " +
					// e.getMessage());
					// }
				} else {
					pluginWorkspaceAccess.showErrorMessage("No file open.");
				}
			} else {
				pluginWorkspaceAccess.showErrorMessage("No editor access.");
			}
		}
	};
}
