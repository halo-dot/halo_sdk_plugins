const { getDefaultConfig, mergeConfig } = require('@react-native/metro-config');
const path = require('path');

// Point Metro at the local plugin so we can import 'halo-sdk-react-native'
const pluginPath = path.resolve(__dirname, '../../plugins/react-native');
const appNodeModules = path.resolve(__dirname, 'node_modules');

// Block Metro from descending into the plugin's own node_modules — those
// packages (e.g. react-native) contain TypeScript syntax that Metro can't parse,
// and all peer-deps are already provided by the test app's node_modules.
const pluginNodeModules = path.join(pluginPath, 'node_modules');
const escapedPluginNodeModules = pluginNodeModules.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');

const config = {
  watchFolders: [pluginPath],
  resolver: {
    extraNodeModules: {
      'halo-sdk-react-native': pluginPath,
    },
    // When resolving modules from within the plugin (which has no node_modules),
    // fall back to the test app's node_modules so peer deps like react-native resolve.
    nodeModulesPaths: [appNodeModules],
    blockList: [new RegExp(`^${escapedPluginNodeModules}`)],
  },
};

module.exports = mergeConfig(getDefaultConfig(__dirname), config);
