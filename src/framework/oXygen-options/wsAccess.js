function applicationStarted(pluginWorkspaceAccess) {
  optionsXMLUrl = jsDirURL.toString() + "/options.xml";
  //Global options file.
  optionsFile = pluginWorkspaceAccess.getUtilAccess().locateFile(new Packages.java.net.URL(optionsXMLUrl));
  pluginWorkspaceAccess.importGlobalOptions(optionsFile);
}

function applicationClosing(pluginWorkspaceAccess) {

}
